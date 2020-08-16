package com.haulmont.testtask.ui.patient;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.ui.MainUI;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

import java.util.List;

public class PatientView extends VerticalLayout implements View {
    public PatientView() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Пациенты");
        menuBar.addItem("Доктора",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo(MainUI.DOCTOR));
        menuBar.addItem("Рецепты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo(MainUI.RECIPE));
        menuBar.getItems().get(0).setEnabled(false);

        setMargin(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(menuBar);

        PatientDAO patientDAO = new PatientDAO();
        //List<Patient> patientList = patientDAO.findAll();

        Grid<Patient> grid = new Grid<>();
        grid.setWidth(80, Unit.PERCENTAGE);
        grid.setItems(patientDAO.findAll());
        grid.addColumn(Patient::getFirstName).setCaption("Имя");
        grid.addColumn(Patient::getLastName).setCaption("Фамилия");
        grid.addColumn(Patient::getSecondName).setCaption("Отчество");
        grid.addColumn(Patient::getPhoneNumber).setCaption("Телефон");

        grid.addColumn(patient -> "Изменить",
                new ButtonRenderer(clickEvent -> {
                    PatientEditWindow editWindow = new PatientEditWindow((Patient) clickEvent.getItem());
                    editWindow.addCloseListener(closeEvent -> grid.setItems(patientDAO.findAll()));
                    UI.getCurrent().addWindow(editWindow);
                }));

        grid.addColumn(patient -> "Удалить",
                new ButtonRenderer(clickEvent -> {
                    if(patientDAO.remove((Patient) clickEvent.getItem())){
                        grid.setItems(patientDAO.findAll());
                    } else {
                        Notification.show("Данный пациент не может быть удалён");
                    }
                }));

        addComponentsAndExpand(grid);

        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            PatientAddWindow addWindow = new PatientAddWindow();
            addWindow.addCloseListener(closeEvent -> grid.setItems(patientDAO.findAll()));
            UI.getCurrent().addWindow(addWindow);
        });
        addComponent(addButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
