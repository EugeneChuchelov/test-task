package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class RecipeDAO {

    public void add(Recipe recipe) {
        String request = "INSERT INTO RECIPE (DESCRIPTION, PATIENT, DOCTOR, CREATION_DATE, " +
                "EXPIRATION_DATE, PRIORITY) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, recipe.getDescription());
            statement.setLong(2, recipe.getPatient().getId());
            statement.setLong(3, recipe.getDoctor().getId());
            statement.setObject(4, recipe.getCreationDate());
            statement.setObject(5, recipe.getExpirationDate());
            statement.setString(6, recipe.getPriority().toString());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Recipe> findAll() {
        String request = "SELECT * FROM RECIPE " +
                "INNER JOIN PATIENT ON RECIPE.PATIENT = PATIENT.ID " +
                "INNER JOIN DOCTOR ON RECIPE.DOCTOR = DOCTOR.ID";
        List<Recipe> recipeList = new LinkedList<>();

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                recipeList.add(createRecipe(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return recipeList;
    }

    public List<Recipe> findByDescription(String description){
        String request = "SELECT * FROM RECIPE " +
                "INNER JOIN PATIENT ON RECIPE.PATIENT = PATIENT.ID " +
                "INNER JOIN DOCTOR ON RECIPE.DOCTOR = DOCTOR.ID " +
                "WHERE DESCRIPTION LIKE ?";
        List<Recipe> recipeList = new LinkedList<>();

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1, "%" + description + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                recipeList.add(createRecipe(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return recipeList;
    }

    public List<Recipe> findByPriority(Priority priority){
        String request = "SELECT * FROM RECIPE " +
                "INNER JOIN PATIENT ON RECIPE.PATIENT = PATIENT.ID " +
                "INNER JOIN DOCTOR ON RECIPE.DOCTOR = DOCTOR.ID " +
                "WHERE PRIORITY LIKE ?";
        List<Recipe> recipeList = new LinkedList<>();

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1, priority.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                recipeList.add(createRecipe(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return recipeList;
    }

    public List<Recipe> findByPatient(Patient patient){
        String request = "SELECT * FROM RECIPE " +
                "INNER JOIN PATIENT ON RECIPE.PATIENT = PATIENT.ID " +
                "INNER JOIN DOCTOR ON RECIPE.DOCTOR = DOCTOR.ID " +
                "WHERE PATIENT.ID = ?";
        List<Recipe> recipeList = new LinkedList<>();

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setLong(1, patient.getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                recipeList.add(createRecipe(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return recipeList;
    }

    private Recipe createRecipe(ResultSet resultSet) throws SQLException {
        Patient patient = new Patient(resultSet.getLong("PATIENT.ID"),
                resultSet.getString("PATIENT.FIRST_NAME"),
                resultSet.getString("PATIENT.LAST_NAME"),
                resultSet.getString("PATIENT.SECOND_NAME"),
                resultSet.getString("PATIENT.PHONE_NUMBER"));

        Doctor doctor = new Doctor(resultSet.getLong("DOCTOR.ID"),
                resultSet.getString("DOCTOR.FIRST_NAME"),
                resultSet.getString("DOCTOR.LAST_NAME"),
                resultSet.getString("DOCTOR.SECOND_NAME"),
                resultSet.getString("DOCTOR.SPECIALIZATION"));

        Recipe recipe = new Recipe(resultSet.getLong("RECIPE.ID"),
                resultSet.getString("RECIPE.DESCRIPTION"),
                patient,
                doctor,
                resultSet.getObject("RECIPE.CREATION_DATE", LocalDate.class),
                resultSet.getObject("RECIPE.EXPIRATION_DATE", LocalDate.class),
                Priority.valueOf(resultSet.getString("RECIPE.PRIORITY").toUpperCase())
        );

        return recipe;
    }

    public void update(Recipe recipe) {
        String request = "UPDATE RECIPE SET DESCRIPTION=?, PATIENT=?, DOCTOR=?, " +
                "CREATION_DATE=?, EXPIRATION_DATE=?, PRIORITY=? WHERE ID=?";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, recipe.getDescription());
            statement.setLong(2, recipe.getPatient().getId());
            statement.setLong(3, recipe.getDoctor().getId());
            statement.setObject(4, recipe.getCreationDate());
            statement.setObject(5, recipe.getExpirationDate());
            statement.setString(6, recipe.getPriority().toString());
            statement.setLong(7, recipe.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void remove(Recipe recipe) {
        String request = "DELETE FROM RECIPE WHERE ID=?";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setLong(1, recipe.getId());

            statement.executeUpdate();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
