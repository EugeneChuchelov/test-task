package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.List;

import static com.haulmont.testtask.dao.ConnectionManager.readFile;
import static org.junit.jupiter.api.Assertions.*;

class PatientDAOTest {

    PatientDAO patientDAO = new PatientDAO();

    @BeforeAll
    static void init(){
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ConnectionManager.setURL("jdbc:hsqldb:mem:test");
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = ConnectionManager.getConnection()) {
            String dbScript = readFile("src/test/resources/initDB.sql");
            connection.createStatement().executeUpdate(dbScript);
            dbScript = readFile("src/test/resources/fillDB.sql");
            connection.createStatement().executeUpdate(dbScript);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = ConnectionManager.getConnection()) {
            String dbScript = readFile("src/test/resources/dropDB.sql");
            connection.createStatement().executeUpdate(dbScript);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void add() {
        Patient patient = new Patient(null, "q", "w", "e", "r");
        List<Patient> patientsOld = patientDAO.findAll();
        patientDAO.add(patient);
        List<Patient> patientsNew = patientDAO.findAll();
        Assert.assertTrue(patientsNew.size() > patientsOld.size());
    }

    @Test
    void findAll() {
        List<Patient> patients = patientDAO.findAll();
        Assert.assertFalse(patients.isEmpty());
    }

    @Test
    void update() {
        Patient patient = patientDAO.findAll().get(0);
        patient.setFirstName("newName");
        patientDAO.update(patient);
        patient = patientDAO.findAll().get(0);
        Assert.assertEquals("newName", patient.getFirstName());
    }

    @Test
    void removeActive() {
        List<Patient> patientsOld = patientDAO.findAll();
        patientDAO.remove(patientsOld.get(0));
        List<Patient> patientsNew = patientDAO.findAll();
        Assert.assertTrue(patientsNew.size() == patientsOld.size());
    }

    @Test
    void removeNonActive() {
        Patient newDoctor = new Patient(null, "q", "w", "e", "r");
        patientDAO.add(newDoctor);
        List<Patient> patientsOld = patientDAO.findAll();
        patientDAO.remove(patientsOld.get(2));
        List<Patient> patientsNew = patientDAO.findAll();
        Assert.assertTrue(patientsNew.size() < patientsOld.size());
    }
}