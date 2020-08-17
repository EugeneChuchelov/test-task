package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.RecipeStatistics;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DoctorDAO {

    public void add(Doctor doctor) {
        String request = "INSERT INTO DOCTOR (FIRST_NAME, LAST_NAME, SECOND_NAME, SPECIALIZATION) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSecondName());
            statement.setString(4, doctor.getSpecialization());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Doctor> findAll() {
        String request = "SELECT * FROM DOCTOR";
        List<Doctor> doctorList = new LinkedList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = new Doctor(resultSet.getLong("ID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("SECOND_NAME"),
                        resultSet.getString("SPECIALIZATION"));
                doctorList.add(doctor);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return doctorList;
    }

    public void update(Doctor doctor) {
        String request = "UPDATE DOCTOR SET FIRST_NAME=?, LAST_NAME=?, SECOND_NAME=?, SPECIALIZATION=? WHERE ID=?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSecondName());
            statement.setString(4, doctor.getSpecialization());
            statement.setLong(5, doctor.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean remove(Doctor doctor) {
        String request = "DELETE FROM DOCTOR WHERE ID=?";
        boolean isRemoved = true;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setLong(1, doctor.getId());

            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            isRemoved = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            isRemoved = false;
        }
        return isRemoved;
    }

    public RecipeStatistics getStatistics(Doctor doctor) {
        String request = "SELECT DISTINCT\n" +
                "(SELECT COUNT(*) FROM DOCTOR INNER JOIN RECIPE ON DOCTOR.ID = RECIPE.DOCTOR " +
                "WHERE DOCTOR.ID = ? AND PRIORITY = 'STANDARD') AS STANDARD, " +
                "(SELECT COUNT(*) FROM DOCTOR INNER JOIN RECIPE ON DOCTOR.ID = RECIPE.DOCTOR " +
                "WHERE DOCTOR.ID = ? AND PRIORITY = 'CITO') AS CITO, " +
                "(SELECT COUNT(*) FROM DOCTOR INNER JOIN RECIPE ON DOCTOR.ID = RECIPE.DOCTOR " +
                "WHERE DOCTOR.ID = ? AND PRIORITY = 'STATIM') AS STATIM " +
                "FROM DOCTOR";

        RecipeStatistics recipeStatistics = new RecipeStatistics();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setLong(1, doctor.getId());
            statement.setLong(2, doctor.getId());
            statement.setLong(3, doctor.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                recipeStatistics.setStandard(resultSet.getLong(Priority.STANDARD.toString()));
                recipeStatistics.setCito(resultSet.getLong(Priority.CITO.toString()));
                recipeStatistics.setStatim(resultSet.getLong(Priority.STATIM.toString()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return recipeStatistics;
    }
}
