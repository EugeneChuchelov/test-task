package com.haulmont.testtask.ui.recipe;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.entity.FilterType;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.Recipe;
import com.haulmont.testtask.ui.StartView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

public class RecipeView extends StartView {
    public RecipeView() {
        //Кнопка "Рецепты" становится неактивной
        getMenuBar().getItems().get(2).setEnabled(false);

        RecipeDAO recipeDAO = new RecipeDAO();
        //Создаётся таблица
        Grid<Recipe> grid = new Grid<>();
        grid.setWidth(80, Unit.PERCENTAGE);
        //HorizontalLayout для всех элементов фильтра
        HorizontalLayout filterBlock = new HorizontalLayout();
        //HorizontalLayout для поля ввода в фильтрре
        HorizontalLayout filterInput = new HorizontalLayout();
        //Выбор типа фильтра
        ComboBox<FilterType> typeSelect = new ComboBox<>();
        typeSelect.setItems(FilterType.values());
        typeSelect.setEmptySelectionAllowed(false);
        //Кнопка для фильтра
        Button doFilter = new Button("Поиск");
        filterBlock.addComponents(typeSelect, filterInput, doFilter);
        //В фильтре может быть текст
        TextField filterText = new TextField();
        filterText.addValueChangeListener(event -> filterText.setStyleName("default"));
        //Выбор пациента
        ComboBox<Patient> patientSelect = new ComboBox<>();
        patientSelect.setEmptySelectionAllowed(false);
        patientSelect.addValueChangeListener(event -> filterText.setStyleName("default"));
        //Или выбор пациента
        ComboBox<Priority> prioritySelect = new ComboBox<>();
        prioritySelect.setEmptySelectionAllowed(false);
        prioritySelect.addValueChangeListener(event -> filterText.setStyleName("default"));
        //При переключении типа фильтра HorizontalLayout с полем ввода очищается
        //И заполняется новым элементом
        typeSelect.addValueChangeListener(event -> {
            if (typeSelect.getValue().equals(FilterType.DESCRIPTION)) {
                filterInput.removeAllComponents();
                filterInput.addComponent(filterText);
            } else if (typeSelect.getValue().equals(FilterType.PRIORITY)) {
                filterInput.removeAllComponents();
                prioritySelect.setItems(Priority.values());
                filterInput.addComponent(prioritySelect);
            } else if (typeSelect.getValue().equals(FilterType.PATIENT)) {
                filterInput.removeAllComponents();
                PatientDAO patientDAO = new PatientDAO();
                patientSelect.setItems(patientDAO.findAll());
                filterInput.addComponent(patientSelect);
            } else {
                filterInput.removeAllComponents();
            }
        });
        //При нажатии кнопки фильтра осуществляется фильтрация
        //По выбранному параметру
        doFilter.addClickListener(click -> {
            //Только если поле заполнено
            if (validateFilterInput(typeSelect, filterText, patientSelect, prioritySelect)) {
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
        //По дефолту фильтр показывает все рецепты и поля ввода нет
        typeSelect.setValue(FilterType.ALL);

        addComponent(filterBlock);
        setComponentAlignment(filterBlock, Alignment.MIDDLE_RIGHT);
        //В таблице список рецептов
        grid.setItems(recipeDAO.findAll());
        //Создаются столбцы для полей рецепта
        grid.addColumn(Recipe::getDescription).setCaption("Описание");
        grid.addColumn(Recipe::getPatient).setCaption("Пациент");
        grid.addColumn(Recipe::getDoctor).setCaption("Доктор");
        grid.addColumn(Recipe::getCreationDate).setCaption("Дата создания");
        grid.addColumn(Recipe::getExpirationDate).setCaption("Срок действия");
        grid.addColumn(Recipe::getPriority).setCaption("Приоритет");
        //Добавляется столбец с кнопкой для изменения соответствующего рецепта
        //При нажатии открывается окно редактирования
        grid.addColumn(recipe -> "Изменить",
                new ButtonRenderer(clickEvent -> {
                    RecipeEditWindow editWindow = new RecipeEditWindow((Recipe) clickEvent.getItem());
                    editWindow.addCloseListener(closeEvent -> grid.setItems(recipeDAO.findAll()));
                    UI.getCurrent().addWindow(editWindow);
                }));
        //Добавляется столбец с кнопкой для удаления соответствующего рецепта
        grid.addColumn(recipe -> "Удалить",
                new ButtonRenderer(clickEvent -> {
                    recipeDAO.remove((Recipe) clickEvent.getItem());
                    grid.setItems(recipeDAO.findAll());
                }));

        addComponentsAndExpand(grid);
        //Создаётся кнопка для добавления рецепта
        //При нажатии открывается окно создания
        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            RecipeAddWindow addWindow = new RecipeAddWindow();
            addWindow.addCloseListener(closeEvent -> grid.setItems(recipeDAO.findAll()));
            UI.getCurrent().addWindow(addWindow);
        });

        addComponent(addButton);
    }

    //Проверяет заполнено ли поле ввода в фильтре
    private static boolean validateFilterInput(ComboBox<FilterType> typeSelect,
                                               TextField filterText,
                                               ComboBox<Patient> patientSelect,
                                               ComboBox<Priority> prioritySelect) {
        boolean isValid = true;
        if (typeSelect.getValue().equals(FilterType.DESCRIPTION)) {
            if (filterText.getValue() == null || filterText.getValue().isEmpty()) {
                filterText.setStyleName("error");
                isValid = false;
            }
        } else if (typeSelect.getValue().equals(FilterType.PRIORITY)) {
            if (prioritySelect.getValue() == null) {
                prioritySelect.setStyleName("error");
                isValid = false;
            }
        } else if (typeSelect.getValue().equals(FilterType.PATIENT)) {
            if (patientSelect.getValue() == null) {
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
