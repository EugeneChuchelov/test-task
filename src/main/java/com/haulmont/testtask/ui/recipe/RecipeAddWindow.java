package com.haulmont.testtask.ui.recipe;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.Recipe;
import com.vaadin.ui.*;

import java.time.LocalDate;
import java.util.Locale;

public class RecipeAddWindow extends AbstractRecipeWindow {
    public RecipeAddWindow() {
        super("Добавление рецепта");

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //Текстовое поле для описания рецепта
        TextField description = new TextField("Описание");
        description.setMaxLength(500);
        description.addValueChangeListener(event -> description.setStyleName("default"));
        //Поле выбора пациента
        ComboBox<Patient> patientSelect = new ComboBox<>("Пациент");
        PatientDAO patientDAO = new PatientDAO();
        patientSelect.setItems(patientDAO.findAll());
        patientSelect.setEmptySelectionAllowed(false);
        patientSelect.addValueChangeListener(event -> patientSelect.setStyleName("default"));
        //Поле выбора доктора
        ComboBox<Doctor> doctorSelect = new ComboBox<>("Доктор");
        DoctorDAO doctorDAO = new DoctorDAO();
        doctorSelect.setItems(doctorDAO.findAll());
        doctorSelect.setEmptySelectionAllowed(false);
        doctorSelect.addValueChangeListener(event -> doctorSelect.setStyleName("default"));
        //Поле выбора даты срока действия
        DateField expireDateField = new DateField("Срок действия");
        expireDateField.setValue(LocalDate.now());
        //Дата в российском формате
        expireDateField.setLocale(new Locale("ru", "RU"));
        expireDateField.addValueChangeListener(event -> expireDateField.setStyleName("default"));
        //Поле выбора Приоритета
        ComboBox<Priority> prioritySelect = new ComboBox<>("Приоритет");
        prioritySelect.setItems(Priority.values());
        prioritySelect.setEmptySelectionAllowed(false);
        prioritySelect.addValueChangeListener(event -> prioritySelect.setStyleName("default"));

        setWidth(description, patientSelect, doctorSelect, expireDateField, prioritySelect);

        Button addButton = new Button("ОК");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            //Если все поля не пустые обновляет рецепт в БД
            if (validate(description, patientSelect, doctorSelect, expireDateField, prioritySelect)) {
                Recipe recipe = new Recipe(null, description.getValue(), patientSelect.getValue(),
                        doctorSelect.getValue(), LocalDate.now(), expireDateField.getValue(),
                        prioritySelect.getValue());
                RecipeDAO recipeDAO = new RecipeDAO();
                recipeDAO.add(recipe);
                close();
            }
        });

        content.addComponentsAndExpand(description, patientSelect, doctorSelect, expireDateField, prioritySelect);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, getCancelButton());

        content.addComponent(buttonsLayout);
        setContent(content);
    }
}
