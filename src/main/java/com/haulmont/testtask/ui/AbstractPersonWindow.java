package com.haulmont.testtask.ui;

import com.vaadin.ui.TextField;

public abstract class AbstractPersonWindow extends AbstractModalWindow {
    public AbstractPersonWindow(String caption) {
        super(caption);
    }

    //Создаёт текстовое поле с указанным названием
    //Задаёт ему ширину, максимальную длину текста
    //И переключание на стандартный стиль при изменении значения - для валидации
    public static TextField createTextField(String caption) {
        TextField textField = new TextField(caption);
        textField.setWidth(90, Unit.PERCENTAGE);
        textField.setMaxLength(20);
        textField.addValueChangeListener(click -> textField.setStyleName("default"));
        return textField;
    }

    //Валидация проверяет, что все поля не пустые
    //Помечает пустые поля стилем ошибки
    public static boolean validate(TextField... textField) {
        boolean isValid = true;
        for (TextField field : textField) {
            if (field.getValue() == null || field.getValue().isEmpty()) {
                field.setStyleName("error");
                isValid = false;
            }
        }

        return isValid;
    }
}
