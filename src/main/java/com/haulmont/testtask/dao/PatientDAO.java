package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Patient;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PatientDAO {

    public void add(Patient patient){
        String request = "INSERT INTO PATIENT (FIRST_NAME, LAST_NAME, SECOND_NAME, PHONE_NUMBER) VALUES (?, ?, ?, ?)";
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getSecondName());
            statement.setString(4, patient.getPhoneNumber());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Patient> findAll(){
        String request = "SELECT * FROM PATIENT";
        List<Patient> patientList = new LinkedList<>();

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Patient patient = new Patient(resultSet.getLong("ID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("SECOND_NAME"),
                        resultSet.getString("PHONE_NUMBER"));
                patientList.add(patient);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return patientList;
    }

    public void update(Patient patient){
        String request = "UPDATE PATIENT SET FIRST_NAME=?, LAST_NAME=?, SECOND_NAME=?, PHONE_NUMBER=? WHERE ID=?";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getSecondName());
            statement.setString(4, patient.getPhoneNumber());
            statement.setLong(5, patient.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean remove(Patient patient) {
        String request = "DELETE FROM PATIENT WHERE ID=?";
        boolean isRemoved = true;

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {

            statement.setLong(1, patient.getId());

            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e){
            isRemoved = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            isRemoved = false;
        }
        return isRemoved;
    }
}
