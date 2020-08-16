package com.haulmont.testtask.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class StartView extends VerticalLayout implements View {
    public StartView() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Пациенты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo("patient"));
        menuBar.addItem("Доктора",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo("doctor"));
        menuBar.addItem("Рецепты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo("recipe"));
        //menuBar.setWidth(200, Unit.PERCENTAGE);
        setMargin(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(menuBar);
    }

    //    public StartView() {
//        setSplitPosition(25, Unit.PERCENTAGE);
//        setLocked(true);
//
//        Button patientButton = new Button("Пациенты",
//                (Button.ClickListener) event -> MainUI.navigator.navigateTo("patient"));
//        patientButton.setWidth(100, Unit.PERCENTAGE);
//        patientButton.setHeight(200, Unit.PIXELS);
//
//        Button doctorButton = new Button("Доктора",
//                (Button.ClickListener) event -> MainUI.navigator.navigateTo("doctor"));
//        doctorButton.setWidth(100, Unit.PERCENTAGE);
//
//        Button recipeButton = new Button("Рецепты",
//                (Button.ClickListener) event -> MainUI.navigator.navigateTo("recipe"));
//        recipeButton.setWidth(100, Unit.PERCENTAGE);
//
//        VerticalLayout buttonsLayout = new VerticalLayout();
//        buttonsLayout.setSizeFull();
//        buttonsLayout.addComponents(patientButton, doctorButton, recipeButton);
//
//        addComponent(buttonsLayout);
//    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
