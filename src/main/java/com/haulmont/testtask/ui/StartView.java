package com.haulmont.testtask.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

public class StartView extends VerticalLayout implements View {
    private static MenuBar menuBar;

    public StartView() {
        //Создаётся меню для переключения между страницами
        menuBar = new MenuBar();
        menuBar.addItem("Пациенты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo("patient"));
        menuBar.addItem("Доктора",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo("doctor"));
        menuBar.addItem("Рецепты",
                (MenuBar.Command) selectedItem -> MainUI.navigator.navigateTo("recipe"));
        setMargin(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(menuBar);
    }

    public static MenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
