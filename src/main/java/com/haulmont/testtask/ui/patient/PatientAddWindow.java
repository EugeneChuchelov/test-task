package com.haulmont.testtask.ui.patient;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

public class PatientAddWindow extends Window {

    private static TextField firstName;

    private static TextField lastName;

    private static TextField secondName;

    private static TextField phoneNumber;

    public PatientAddWindow() {
        super("Добавление пациента");
        setModal(true);
        setResizable(false);
        setClosable(false);
        setHeight(50, Unit.PERCENTAGE);
        setWidth(20, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        firstName = new TextField("Имя");
        firstName.setWidth(90, Unit.PERCENTAGE);
        firstName.setMaxLength(20);
        firstName.addValueChangeListener(click -> firstName.setStyleName("default"));

        lastName = new TextField("Фамилия");
        lastName.setWidth(90, Unit.PERCENTAGE);
        lastName.setMaxLength(20);
        lastName.addValueChangeListener(click -> lastName.setStyleName("default"));

        secondName = new TextField("Отчество");
        secondName.setWidth(90, Unit.PERCENTAGE);
        secondName.setMaxLength(20);
        secondName.addValueChangeListener(click -> secondName.setStyleName("default"));

        phoneNumber = new TextField("Телефон");
        phoneNumber.setWidth(90, Unit.PERCENTAGE);
        phoneNumber.setMaxLength(20);
        phoneNumber.addValueChangeListener(click -> phoneNumber.setStyleName("default"));

        Button addButton = new Button("ОК");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if(validate()) {
                Patient patient = new Patient(null, firstName.getValue(), lastName.getValue(),
                        secondName.getValue(), phoneNumber.getValue());
                PatientDAO patientDAO = new PatientDAO();
                patientDAO.add(patient);
                close();
            }
        });

        Button cancelButton = new Button("Отменить");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());

        content.addComponentsAndExpand(firstName, lastName, secondName, phoneNumber);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, cancelButton);
        content.addComponent(buttonsLayout);
        setContent(content);
    }

    private static boolean validate(){
        boolean isValid = true;
        if(firstName.getValue() == null || firstName.getValue().isEmpty()){
            firstName.setStyleName("error");
            isValid = false;
        }
        if(lastName.getValue() == null || lastName.getValue().isEmpty()){
            lastName.setStyleName("error");
            isValid = false;
        }
        if(secondName.getValue() == null || secondName.getValue().isEmpty()){
            secondName.setStyleName("error");
            isValid = false;
        }
        if(phoneNumber.getValue() == null || phoneNumber.getValue().isEmpty()){
            phoneNumber.setStyleName("error");
            isValid = false;
        }

        return isValid;
    }
}
