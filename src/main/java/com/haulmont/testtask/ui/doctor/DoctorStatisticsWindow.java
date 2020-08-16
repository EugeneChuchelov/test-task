package com.haulmont.testtask.ui.doctor;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.RecipeStatistics;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class DoctorStatisticsWindow extends Window {
    public DoctorStatisticsWindow(Doctor doctor, RecipeStatistics statistics) {
        super("Статистика доктора");
        setHeight(50, Unit.PERCENTAGE);
        setWidth(15, Unit.PERCENTAGE);
        center();

        VerticalLayout content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        Label name = new Label(doctor.toString());
        name.addStyleName(ValoTheme.LABEL_H2);

        Label total = new Label("Всего:" + statistics.getTotal());
        total.addStyleName(ValoTheme.LABEL_H3);
        Label standard = new Label("Нормальных: " + statistics.getStandard());
        standard.addStyleName(ValoTheme.LABEL_H3);
        Label cito = new Label("Срочных: " + statistics.getCito());
        cito.addStyleName(ValoTheme.LABEL_H3);
        Label statim = new Label("Немедленных: " + statistics.getStatim());
        statim.addStyleName(ValoTheme.LABEL_H3);

        Button closeButton = new Button("ОК");
        closeButton.addClickListener(click -> close());

        content.addComponentsAndExpand(name, total, standard, cito, statim);
        content.addComponent(closeButton);
        content.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);

        setContent(content);
    }
}
