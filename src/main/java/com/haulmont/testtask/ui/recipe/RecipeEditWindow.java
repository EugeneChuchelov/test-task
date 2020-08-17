package com.haulmont.testtask.ui.recipe;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.Recipe;
import com.vaadin.ui.*;

import java.util.Locale;

public class RecipeEditWindow extends AbstractRecipeWindow {
    public RecipeEditWindow(Recipe recipe) {
        super("Изменение рецепта");

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //Все поля заполняются значениями изменяемого рецепта
        //Текстовое поле для описания рецепта
        TextField description = new TextField("Описание");
        description.setWidth(90, Unit.PERCENTAGE);
        description.setMaxLength(500);
        description.setValue(recipe.getDescription());
        description.addValueChangeListener(click -> description.setStyleName("default"));
        //Поле выбора пациента
        ComboBox<Patient> patientSelect = new ComboBox<>("Пациент");
        PatientDAO patientDAO = new PatientDAO();
        patientSelect.setItems(patientDAO.findAll());
        patientSelect.setWidth(90, Unit.PERCENTAGE);
        patientSelect.setValue(recipe.getPatient());
        patientSelect.setEmptySelectionAllowed(false);
        patientSelect.addValueChangeListener(click -> patientSelect.setStyleName("default"));
        //Поле выбора доктора
        ComboBox<Doctor> doctorSelect = new ComboBox<>("Доктор");
        DoctorDAO doctorDAO = new DoctorDAO();
        doctorSelect.setItems(doctorDAO.findAll());
        doctorSelect.setWidth(90, Unit.PERCENTAGE);
        doctorSelect.setValue(recipe.getDoctor());
        doctorSelect.setEmptySelectionAllowed(false);
        doctorSelect.addValueChangeListener(click -> doctorSelect.setStyleName("default"));
        //Поле выбора даты срока действия
        DateField expireDateField = new DateField("Срок действия");
        expireDateField.setWidth(90, Unit.PERCENTAGE);
        expireDateField.setValue(recipe.getExpirationDate());
        //Дата в российском формате
        expireDateField.setLocale(new Locale("ru", "RU"));
        expireDateField.addValueChangeListener(click -> expireDateField.setStyleName("default"));
        //Поле выбора Приоритета
        ComboBox<Priority> prioritySelect = new ComboBox<>("Приоритет");
        prioritySelect.setItems(Priority.values());
        prioritySelect.setWidth(90, Unit.PERCENTAGE);
        prioritySelect.setValue(recipe.getPriority());
        prioritySelect.setEmptySelectionAllowed(false);
        prioritySelect.addValueChangeListener(click -> prioritySelect.setStyleName("default"));

        setWidth(description, patientSelect, doctorSelect, expireDateField, prioritySelect);

        Button editButton = new Button("ОК");
        editButton.addClickListener((Button.ClickListener) clickEvent -> {
            //Если все поля не пустые обновляет рецепт в БД
            if (validate(description, patientSelect, doctorSelect, expireDateField, prioritySelect)) {
                Recipe newRecipe = new Recipe(recipe.getId(), description.getValue(), patientSelect.getValue(),
                        doctorSelect.getValue(), recipe.getCreationDate(), expireDateField.getValue(),
                        prioritySelect.getValue());
                RecipeDAO recipeDAO = new RecipeDAO();
                recipeDAO.update(newRecipe);
                close();
            }
        });

        content.addComponentsAndExpand(description, patientSelect, doctorSelect,
                expireDateField, prioritySelect);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(editButton, getCancelButton());

        content.addComponent(buttonsLayout);
        setContent(content);
    }
}
