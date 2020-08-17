package com.haulmont.testtask.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

public abstract class AbstractModalWindow extends Window {

    private final Button okButton;

    private final Button cancelButton;

    public AbstractModalWindow(String caption) {
        super(caption);
        //Настраиваются параметры окна
        setModal(true);
        setResizable(false);
        setClosable(false);
        setHeight(50, Unit.PERCENTAGE);
        setWidth(20, Unit.PERCENTAGE);
        //Создаётся пустая кнопка "ОК"
        okButton = new Button("ОК");
        //Кнопка может быть нажата клавишей Enter
        okButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //И копка "Отмена, закрывающая окно"
        cancelButton = new Button("Отменить");
        //Кнопка может быть нажата клавишей Escape
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());
    }

    public Button getOkButton() {
        return okButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }


}
