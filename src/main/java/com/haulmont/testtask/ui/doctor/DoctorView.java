package com.haulmont.testtask.ui.doctor;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.ui.MainUI;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

public class DoctorView  extends VerticalLayout implements View {

    public DoctorView() {
        MenuBar menuBar = new MenuBar();

        menuBar.addItem("Пациенты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo(MainUI.PATIENT));
        menuBar.addItem("Доктора");
        menuBar.getItems().get(1).setEnabled(false);
        menuBar.addItem("Рецепты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo(MainUI.RECIPE));

        setMargin(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(menuBar);

        DoctorDAO doctorDAO = new DoctorDAO();

        Grid<Doctor> grid = new Grid<>();
        grid.setWidth(80, Unit.PERCENTAGE);
        grid.setItems(doctorDAO.findAll());
        grid.addColumn(Doctor::getFirstName).setCaption("Имя");
        grid.addColumn(Doctor::getLastName).setCaption("Фамилия");
        grid.addColumn(Doctor::getSecondName).setCaption("Отчество");
        grid.addColumn(Doctor::getSpecialization).setCaption("Специализация");

        grid.addColumn(doctor ->"Изменить",
                new ButtonRenderer(clickEvent -> {
                    DoctorEditWindow editWindow = new DoctorEditWindow((Doctor) clickEvent.getItem());
                    editWindow.addCloseListener(closeEvent -> grid.setItems(doctorDAO.findAll()));
                    UI.getCurrent().addWindow(editWindow);
                }));

        grid.addColumn(doctor ->"Удалить",
                new ButtonRenderer(clickEvent -> {
                    if(doctorDAO.remove((Doctor) clickEvent.getItem())){
                        grid.setItems(doctorDAO.findAll());
                    } else {
                        Notification.show("Данный доктор не может быть удалён");
                    }
                }));

        grid.addColumn(doctor ->"Статистика",
                new ButtonRenderer<>(clickEvent -> {
                    DoctorStatisticsWindow statisticsWindow = new DoctorStatisticsWindow(
                            clickEvent.getItem(),
                            doctorDAO.getStatistics(clickEvent.getItem())
                    );
                    UI.getCurrent().addWindow(statisticsWindow);
                }));

        addComponentsAndExpand(grid);

        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            DoctorAddWindow addWindow = new DoctorAddWindow();
            addWindow.addCloseListener(closeEvent -> grid.setItems(doctorDAO.findAll()));
            UI.getCurrent().addWindow(addWindow);
        });
        addComponent(addButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
