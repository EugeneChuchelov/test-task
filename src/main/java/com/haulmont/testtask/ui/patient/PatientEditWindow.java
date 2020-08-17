package com.haulmont.testtask.ui.patient;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.ui.AbstractPersonWindow;
import com.vaadin.ui.*;

public class PatientEditWindow extends AbstractPersonWindow {
    public PatientEditWindow(Patient patient) {
        super("Изменение пациента");

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        TextField firstName = createTextField("Имя");
        firstName.setValue(patient.getFirstName());

        TextField lastName = createTextField("Фамилия");
        lastName.setValue(patient.getLastName());

        TextField secondName = createTextField("Отчество");
        secondName.setValue(patient.getSecondName());

        TextField phoneNumber = createTextField("Телефон");
        phoneNumber.setValue(patient.getPhoneNumber());

        Button editButton = getOkButton();
        //При нажатии кнопки будет проверено заполнение полей
        //Если все заполнены, создаётся новый доктор и отправляется в БД
        editButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (validate(firstName, lastName, secondName, phoneNumber)) {
                Patient newPatient = new Patient(patient.getId(), firstName.getValue(), lastName.getValue(),
                        secondName.getValue(), phoneNumber.getValue());
                PatientDAO patientDAO = new PatientDAO();
                patientDAO.update(newPatient);
                close();
            }
        });

        content.addComponentsAndExpand(firstName, lastName, secondName, phoneNumber);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(editButton, getCancelButton());
        content.addComponent(buttonsLayout);
        setContent(content);
    }
}
