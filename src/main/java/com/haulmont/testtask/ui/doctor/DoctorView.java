package com.haulmont.testtask.ui.doctor;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.ui.StartView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ButtonRenderer;

public class DoctorView extends StartView {

    public DoctorView() {
        //Кнопка "Доктора" становится неактивной
        getMenuBar().getItems().get(1).setEnabled(false);

        DoctorDAO doctorDAO = new DoctorDAO();

        //Создаётся таблица
        Grid<Doctor> grid = new Grid<>();
        grid.setWidth(80, Unit.PERCENTAGE);
        //В таблице список докторов
        grid.setItems(doctorDAO.findAll());
        //Создаются столбцы для полей доктора
        grid.addColumn(Doctor::getFirstName).setCaption("Имя");
        grid.addColumn(Doctor::getLastName).setCaption("Фамилия");
        grid.addColumn(Doctor::getSecondName).setCaption("Отчество");
        grid.addColumn(Doctor::getSpecialization).setCaption("Специализация");
        //Добавляется столбец с кнопкой для изменения соответствующего доктора
        //При нажатии открывается окно редактирования
        grid.addColumn(doctor -> "Изменить",
                new ButtonRenderer(clickEvent -> {
                    DoctorEditWindow editWindow = new DoctorEditWindow((Doctor) clickEvent.getItem());
                    //После закрытия окна таблица обновляет список
                    editWindow.addCloseListener(closeEvent -> grid.setItems(doctorDAO.findAll()));
                    //Открытие окна
                    UI.getCurrent().addWindow(editWindow);
                }));
        //Добавляется столбец с кнопкой для удаления соответствующего доктора
        grid.addColumn(doctor -> "Удалить",
                new ButtonRenderer(clickEvent -> {
                    if (doctorDAO.remove((Doctor) clickEvent.getItem())) {
                        grid.setItems(doctorDAO.findAll());
                    } else {
                        Notification.show("Данный доктор не может быть удалён");
                    }
                }));
        //Добавляется столбец с кнопкой для вывода статистики соответствующего доктора
        grid.addColumn(doctor -> "Статистика",
                new ButtonRenderer<>(clickEvent -> {
                    DoctorStatisticsWindow statisticsWindow = new DoctorStatisticsWindow(
                            clickEvent.getItem(),
                            doctorDAO.getStatistics(clickEvent.getItem())
                    );
                    UI.getCurrent().addWindow(statisticsWindow);
                }));

        addComponentsAndExpand(grid);
        //Создаётся кнопка для добавления пациента
        //При нажатии открывается окно создания
        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            DoctorAddWindow addWindow = new DoctorAddWindow();
            //После закрытия окна таблица обновляет список
            addWindow.addCloseListener(closeEvent -> grid.setItems(doctorDAO.findAll()));
            //Открытие окна
            UI.getCurrent().addWindow(addWindow);
        });
        addComponent(addButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
