package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.ConnectionUtil;
import com.haulmont.testtask.ui.doctor.DoctorView;
import com.haulmont.testtask.ui.patient.PatientView;
import com.haulmont.testtask.ui.recipe.RecipeView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI{
    public static Navigator navigator;

    private static boolean isDBCreated = false;

    public static final String PATIENT = "patient";

    public static final String DOCTOR = "doctor";

    public static final String RECIPE = "recipe";

    @Override
    protected void init(VaadinRequest request) {
        if(!isDBCreated){
            ConnectionUtil.createDB();
            isDBCreated = true;
        }

        navigator = new Navigator(this, this);
        navigator.addView("", new StartView());
        navigator.addView(PATIENT, new PatientView());
        navigator.addView(DOCTOR, new DoctorView());
        navigator.addView(RECIPE, new RecipeView());
    }
}