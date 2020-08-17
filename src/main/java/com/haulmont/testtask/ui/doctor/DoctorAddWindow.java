package com.haulmont.testtask.ui.doctor;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.ui.AbstractPersonWindow;
import com.vaadin.ui.*;

public class DoctorAddWindow extends AbstractPersonWindow {
    public DoctorAddWindow() {
        super("Добавление доктора");

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        TextField firstName = createTextField("Имя");

        TextField lastName = createTextField("Фамилия");

        TextField secondName = createTextField("Отчество");

        TextField specialization = createTextField("Специализация");

        Button addButton = getOkButton();
        //При нажатии кнопки будет проверено заполнение полей
        //Если все заполнены, создаётся новый доктор и отправляется в БД
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (validate(firstName, lastName, secondName, specialization)) {
                Doctor doctor = new Doctor(null, firstName.getValue(), lastName.getValue(),
                        secondName.getValue(), specialization.getValue());
                DoctorDAO doctorDAO = new DoctorDAO();
                doctorDAO.add(doctor);
                close();
            }
        });
        //Поля добавляются в окно
        content.addComponentsAndExpand(firstName, lastName, secondName, specialization);
        //Создаётся горизонтальное расположение кнопок
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, getCancelButton());
        content.addComponent(buttonsLayout);
        setContent(content);
    }
}
