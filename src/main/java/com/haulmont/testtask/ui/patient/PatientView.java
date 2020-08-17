package com.haulmont.testtask.ui.patient;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.ui.StartView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ButtonRenderer;

public class PatientView extends StartView {
    public PatientView() {
        //Кнопка "Пациенты" становится неактивной
        getMenuBar().getItems().get(0).setEnabled(false);

        PatientDAO patientDAO = new PatientDAO();

        //Создаётся таблица
        Grid<Patient> grid = new Grid<>();
        grid.setWidth(80, Unit.PERCENTAGE);
        //В таблице список пациентов
        grid.setItems(patientDAO.findAll());
        //Создаются столбцы для полей пациента
        grid.addColumn(Patient::getFirstName).setCaption("Имя");
        grid.addColumn(Patient::getLastName).setCaption("Фамилия");
        grid.addColumn(Patient::getSecondName).setCaption("Отчество");
        grid.addColumn(Patient::getPhoneNumber).setCaption("Телефон");
        //Добавляется столбец с кнопкой для изменения соответствующего пациента
        //При нажатии открывается окно редактирования
        grid.addColumn(patient -> "Изменить",
                new ButtonRenderer(clickEvent -> {
                    PatientEditWindow editWindow = new PatientEditWindow((Patient) clickEvent.getItem());
                    //После закрытия окна таблица обновляет список
                    editWindow.addCloseListener(closeEvent -> grid.setItems(patientDAO.findAll()));
                    //Открытие окна
                    UI.getCurrent().addWindow(editWindow);
                }));
        //Добавляется столбец с кнопкой для удаления соответствующего пациента
        grid.addColumn(patient -> "Удалить",
                new ButtonRenderer(clickEvent -> {
                    if (patientDAO.remove((Patient) clickEvent.getItem())) {
                        grid.setItems(patientDAO.findAll());
                    } else {
                        Notification.show("Данный пациент не может быть удалён");
                    }
                }));
        //Таблица добавляется на UI
        addComponentsAndExpand(grid);
        //Создаётся кнопка для добавления пациента
        //При нажатии открывается окно создания
        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            PatientAddWindow addWindow = new PatientAddWindow();
            //После закрытия окна таблица обновляет список
            addWindow.addCloseListener(closeEvent -> grid.setItems(patientDAO.findAll()));
            //Открытие окна
            UI.getCurrent().addWindow(addWindow);
        });
        //Кнопка добавления добавляется на UI
        addComponent(addButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
