package com.haulmont.testtask.ui.recipe;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.Recipe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;

import java.time.LocalDate;
import java.util.Locale;

public class RecipeAddWindow extends Window {

    private static TextField description;

    private static ComboBox<Patient> patientSelect;

    private static ComboBox<Doctor> doctorSelect;

    private static DateField expireDateField;

    private static ComboBox<Priority> prioritySelect;

    public RecipeAddWindow() {
        super("Добавление рецепта");
        setModal(true);
        setResizable(false);
        setClosable(false);
        setHeight(50, Unit.PERCENTAGE);
        setWidth(20, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        description = new TextField("Описание");
        description.setWidth(90, Unit.PERCENTAGE);
        description.setMaxLength(200);
        description.addValueChangeListener(event -> description.setStyleName("default"));

        patientSelect = new ComboBox<>("Пациент");
        PatientDAO patientDAO = new PatientDAO();
        patientSelect.setItems(patientDAO.findAll());
        patientSelect.setWidth(90, Unit.PERCENTAGE);
        patientSelect.setEmptySelectionAllowed(false);
        patientSelect.addValueChangeListener(event -> patientSelect.setStyleName("default"));

        doctorSelect = new ComboBox<>("Доктор");
        DoctorDAO doctorDAO = new DoctorDAO();
        doctorSelect.setItems(doctorDAO.findAll());
        doctorSelect.setWidth(90, Unit.PERCENTAGE);
        doctorSelect.setEmptySelectionAllowed(false);
        doctorSelect.addValueChangeListener(event -> doctorSelect.setStyleName("default"));

        expireDateField = new DateField("Срок действия");
        expireDateField.setValue(LocalDate.now());
        expireDateField.setWidth(90, Unit.PERCENTAGE);
        expireDateField.setLocale(new Locale("ru", "RU"));
        expireDateField.addValueChangeListener(event -> expireDateField.setStyleName("default"));

        prioritySelect = new ComboBox<>("Приоритет");
        prioritySelect.setItems(Priority.values());
        prioritySelect.setWidth(90, Unit.PERCENTAGE);
        prioritySelect.setEmptySelectionAllowed(false);
        prioritySelect.addValueChangeListener(event -> prioritySelect.setStyleName("default"));

        Button addButton = new Button("ОК");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if(validate()) {
                Recipe recipe = new Recipe(null, description.getValue(), patientSelect.getValue(),
                        doctorSelect.getValue(), LocalDate.now(), expireDateField.getValue(),
                        prioritySelect.getValue());
                RecipeDAO recipeDAO = new RecipeDAO();
                recipeDAO.add(recipe);
                close();
            }
        });

        Button cancelButton = new Button("Отменить");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());

        content.addComponentsAndExpand(description, patientSelect, doctorSelect, expireDateField, prioritySelect);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, cancelButton);

        content.addComponent(buttonsLayout);
        setContent(content);
    }

    private static boolean validate(){
        boolean isValid = true;
        if(description.getValue() == null || description.getValue().isEmpty()){
            description.setStyleName("error");
            isValid = false;
        }
        if(patientSelect.getValue() == null){
            patientSelect.setStyleName("error");
            isValid = false;
        }
        if(doctorSelect.getValue() == null){
            doctorSelect.setStyleName("error");
            isValid = false;
        }
        if(expireDateField.getValue() == null){
            expireDateField.setStyleName("error");
            isValid = false;
        }
        if(prioritySelect.getValue() == null){
            prioritySelect.setStyleName("error");
            isValid = false;
        }

        return isValid;
    }
}
