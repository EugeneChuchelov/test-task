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

public class RecipeEditWindow extends Window {

    private static TextField description;

    private static ComboBox<Patient> patientSelect;

    private static ComboBox<Doctor> doctorSelect;

    private static DateField expireDateField;

    private static ComboBox<Priority> prioritySelect;

    public RecipeEditWindow(Recipe recipe) {
        super("Изменение рецепта");
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
        description.setValue(recipe.getDescription());
        description.addValueChangeListener(click -> description.setStyleName("default"));

        patientSelect = new ComboBox<>("Пациент");
        PatientDAO patientDAO = new PatientDAO();
        patientSelect.setItems(patientDAO.findAll());
        patientSelect.setWidth(90, Unit.PERCENTAGE);
        patientSelect.setValue(recipe.getPatient());
        patientSelect.setEmptySelectionAllowed(false);
        patientSelect.addValueChangeListener(click -> patientSelect.setStyleName("default"));

        doctorSelect = new ComboBox<>("Доктор");
        DoctorDAO doctorDAO = new DoctorDAO();
        doctorSelect.setItems(doctorDAO.findAll());
        doctorSelect.setWidth(90, Unit.PERCENTAGE);
        doctorSelect.setValue(recipe.getDoctor());
        doctorSelect.setEmptySelectionAllowed(false);
        doctorSelect.addValueChangeListener(click -> doctorSelect.setStyleName("default"));

        expireDateField = new DateField("Срок действия");
        expireDateField.setWidth(90, Unit.PERCENTAGE);
        expireDateField.setValue(recipe.getExpirationDate());
        expireDateField.setLocale(new Locale("ru", "RU"));
        expireDateField.addValueChangeListener(click -> expireDateField.setStyleName("default"));

        prioritySelect = new ComboBox<>("Приоритет");
        prioritySelect.setItems(Priority.values());
        prioritySelect.setWidth(90, Unit.PERCENTAGE);
        prioritySelect.setValue(recipe.getPriority());
        prioritySelect.setEmptySelectionAllowed(false);
        prioritySelect.addValueChangeListener(click -> prioritySelect.setStyleName("default"));

        Button editButton = new Button("ОК");
        editButton.addClickListener((Button.ClickListener) clickEvent -> {
            if(validate()){
                Recipe newRecipe = new Recipe(recipe.getId(), description.getValue(), patientSelect.getValue(),
                        doctorSelect.getValue(), LocalDate.now(), expireDateField.getValue(),
                        prioritySelect.getValue());
                RecipeDAO recipeDAO = new RecipeDAO();
                recipeDAO.update(newRecipe);
                close();
            }
        });

        Button cancelButton = new Button("Отменить");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());

        content.addComponentsAndExpand(description, patientSelect, doctorSelect,
                expireDateField, prioritySelect);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(editButton, cancelButton);

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
