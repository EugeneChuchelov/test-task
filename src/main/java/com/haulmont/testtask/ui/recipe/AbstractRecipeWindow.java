package com.haulmont.testtask.ui.recipe;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.ui.AbstractModalWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

public abstract class AbstractRecipeWindow extends AbstractModalWindow {
    public AbstractRecipeWindow(String caption) {
        super(caption);
    }

    public static void setWidth(AbstractComponent... components) {
        for (AbstractComponent component : components)
            component.setWidth(90, Unit.PERCENTAGE);
    }

    //Проверяет все ли поля заполнены
    public static boolean validate(TextField description, ComboBox<Patient> patientSelect,
                                   ComboBox<Doctor> doctorSelect, DateField expireDateField,
                                   ComboBox<Priority> prioritySelect) {
        boolean isValid = true;
        if (description.getValue() == null || description.getValue().isEmpty()) {
            description.setStyleName("error");
            isValid = false;
        }
        if (patientSelect.getValue() == null) {
            patientSelect.setStyleName("error");
            isValid = false;
        }
        if (doctorSelect.getValue() == null) {
            doctorSelect.setStyleName("error");
            isValid = false;
        }
        if (expireDateField.getValue() == null) {
            expireDateField.setStyleName("error");
            isValid = false;
        }
        if (prioritySelect.getValue() == null) {
            prioritySelect.setStyleName("error");
            isValid = false;
        }

        return isValid;
    }
}
