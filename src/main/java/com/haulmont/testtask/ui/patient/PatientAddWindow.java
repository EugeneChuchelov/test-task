package com.haulmont.testtask.ui.patient;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.ui.AbstractPersonWindow;
import com.vaadin.ui.*;

public class PatientAddWindow extends AbstractPersonWindow {
    public PatientAddWindow() {
        super("Добавление пациента");

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        TextField firstName = createTextField("Имя");

        TextField lastName = createTextField("Фамилия");

        TextField secondName = createTextField("Отчество");

        TextField phoneNumber = createTextField("Телефон");

        Button addButton = getOkButton();
        //При нажатии кнопки будет проверено заполнение полей
        //Если все заполнены, создаётся новый пациент и отправляется в БД
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (validate(firstName, lastName, secondName, phoneNumber)) {
                Patient patient = new Patient(null, firstName.getValue(), lastName.getValue(),
                        secondName.getValue(), phoneNumber.getValue());
                PatientDAO patientDAO = new PatientDAO();
                patientDAO.add(patient);
                close();
            }
        });
        content.addComponentsAndExpand(firstName, lastName, secondName, phoneNumber);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, getCancelButton());
        content.addComponent(buttonsLayout);
        setContent(content);
    }
}
