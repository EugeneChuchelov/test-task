package com.haulmont.testtask.ui.doctor;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.ui.AbstractPersonWindow;
import com.vaadin.ui.*;

public class DoctorEditWindow extends AbstractPersonWindow {
    public DoctorEditWindow(Doctor doctor) {
        super("Изменение доктора");

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //Поля после создания заполняются значениями изменяемого доктора
        TextField firstName = createTextField("Имя");
        firstName.setValue(doctor.getFirstName());

        TextField lastName = createTextField("Фамилия");
        lastName.setValue(doctor.getLastName());

        TextField secondName = createTextField("Отчество");
        secondName.setValue(doctor.getSecondName());

        TextField specialization = createTextField("Специализация");
        specialization.setValue(doctor.getSpecialization());

        Button editButton = getOkButton();
        //При нажатии кнопки будет проверено заполнение полей
        //Если все заполнены, создаётся новый доктор и отправляется в БД
        editButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (validate(firstName, lastName, secondName, specialization)) {
                Doctor newDoctor = new Doctor(doctor.getId(), firstName.getValue(), lastName.getValue(),
                        secondName.getValue(), specialization.getValue());
                DoctorDAO doctorDAO = new DoctorDAO();
                doctorDAO.update(newDoctor);
                close();
            }
        });
        //Поля добавляются в окно
        content.addComponentsAndExpand(firstName, lastName, secondName, specialization);
        //Создаётся горизонтальное расположение кнопок
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(editButton, getCancelButton());
        content.addComponent(buttonsLayout);
        setContent(content);
    }


}
