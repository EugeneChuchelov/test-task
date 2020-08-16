package com.haulmont.testtask.ui.recipe;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.entity.FilterType;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.Recipe;
import com.haulmont.testtask.ui.MainUI;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

public class RecipeView  extends VerticalLayout implements View {
    public RecipeView() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Пациенты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo(MainUI.PATIENT));
        menuBar.addItem("Доктора",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo(MainUI.DOCTOR));
        menuBar.addItem("Рецепты");
        menuBar.getItems().get(2).setEnabled(false);

        setMargin(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(menuBar);

        RecipeDAO recipeDAO = new RecipeDAO();

        HorizontalLayout filterBlock = new HorizontalLayout();
        HorizontalLayout filterInput = new HorizontalLayout();
        ComboBox<FilterType> typeSelect = new ComboBox<>();
        typeSelect.setItems(FilterType.values());
        typeSelect.setEmptySelectionAllowed(false);
        Button doFilter = new Button("Поиск");
        filterBlock.addComponents(typeSelect, filterInput, doFilter);

        TextField filterText = new TextField();
        filterText.addValueChangeListener(event -> filterText.setStyleName("default"));
        ComboBox<Patient> patientSelect = new ComboBox<>();
        patientSelect.setEmptySelectionAllowed(false);
        patientSelect.addValueChangeListener(event -> filterText.setStyleName("default"));
        ComboBox<Priority> prioritySelect = new ComboBox<>();
        prioritySelect.setEmptySelectionAllowed(false);
        prioritySelect.addValueChangeListener(event -> filterText.setStyleName("default"));
        filterInput.addComponent(filterText);

        typeSelect.addValueChangeListener(event -> {
            if(typeSelect.getValue().equals(FilterType.DESCRIPTION)){
                filterInput.removeAllComponents();
                filterInput.addComponent(filterText);
            } else if(typeSelect.getValue().equals(FilterType.PRIORITY)){
                filterInput.removeAllComponents();
                prioritySelect.setItems(Priority.values());
                filterInput.addComponent(prioritySelect);
            } else if(typeSelect.getValue().equals(FilterType.PATIENT)){
                filterInput.removeAllComponents();
                PatientDAO patientDAO = new PatientDAO();
                patientSelect.setItems(patientDAO.findAll());
                filterInput.addComponent(patientSelect);
            } else {
                filterInput.removeAllComponents();
            }
        });

        Grid<Recipe> grid = new Grid<>();

        doFilter.addClickListener(click -> {
            if(validateFilterInput(typeSelect, filterText, patientSelect, prioritySelect)) {
                if (typeSelect.getValue().equals(FilterType.DESCRIPTION)) {
                    grid.setItems(recipeDAO.findByDescription(filterText.getValue()));
                } else if (typeSelect.getValue().equals(FilterType.PRIORITY)) {
                    grid.setItems(recipeDAO.findByPriority(prioritySelect.getValue()));
                } else if (typeSelect.getValue().equals(FilterType.PATIENT)) {
                    grid.setItems(recipeDAO.findByPatient(patientSelect.getValue()));
                } else {
                    grid.setItems(recipeDAO.findAll());
                }
            }
        });

        typeSelect.setValue(FilterType.ALL);

        addComponent(filterBlock);
        setComponentAlignment(filterBlock, Alignment.MIDDLE_RIGHT);


        grid.setWidth(80, Unit.PERCENTAGE);
        grid.setItems(recipeDAO.findAll());
        grid.addColumn(Recipe::getDescription).setCaption("Описание");
        grid.addColumn(Recipe::getPatient).setCaption("Пациент");
        grid.addColumn(Recipe::getDoctor).setCaption("Доктор");
        grid.addColumn(Recipe::getCreationDate).setCaption("Дата создания");
        grid.addColumn(Recipe::getExpirationDate).setCaption("Срок действия");
        grid.addColumn(Recipe::getPriority).setCaption("Приоритет");

        grid.addColumn(recipe -> "Изменить",
                new ButtonRenderer(clickEvent -> {
                    RecipeEditWindow editWindow = new RecipeEditWindow((Recipe) clickEvent.getItem());
                    editWindow.addCloseListener(closeEvent -> grid.setItems(recipeDAO.findAll()));
                    UI.getCurrent().addWindow(editWindow);
                }));

        grid.addColumn(recipe -> "Удалить",
                new ButtonRenderer(clickEvent -> {
                    recipeDAO.remove((Recipe) clickEvent.getItem());
                    grid.setItems(recipeDAO.findAll());
                }));

        addComponentsAndExpand(grid);

        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            RecipeAddWindow addWindow = new RecipeAddWindow();
            addWindow.addCloseListener(closeEvent -> grid.setItems(recipeDAO.findAll()));
            UI.getCurrent().addWindow(addWindow);
        });

        addComponent(addButton);
    }

    private static boolean validateFilterInput(ComboBox<FilterType> typeSelect,
                                               TextField filterText,
                                               ComboBox<Patient> patientSelect,
                                               ComboBox<Priority> prioritySelect){
        boolean isValid = true;
        if(typeSelect.getValue().equals(FilterType.DESCRIPTION)){
            if(filterText.getValue() == null || filterText.getValue().isEmpty()){
                filterText.setStyleName("error");
                isValid = false;
            }
        } else if (typeSelect.getValue().equals(FilterType.PRIORITY)){
            if(prioritySelect.getValue() == null){
                prioritySelect.setStyleName("error");
                isValid = false;
            }
        } else if (typeSelect.getValue().equals(FilterType.PATIENT)){
            if(patientSelect.getValue() == null){
                patientSelect.setStyleName("error");
                isValid = false;
            }
        }
        return isValid;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
