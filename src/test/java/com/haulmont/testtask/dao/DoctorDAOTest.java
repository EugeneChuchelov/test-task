package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.RecipeStatistics;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.haulmont.testtask.dao.ConnectionManager.readFile;

class DoctorDAOTest {

    DoctorDAO doctorDAO = new DoctorDAO();

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
        List<Doctor> doctorsOld = doctorDAO.findAll();
        Doctor newDoctor = new Doctor(null, "q", "w", "e", "r");
        doctorDAO.add(newDoctor);
        List<Doctor> doctorsNew = doctorDAO.findAll();
        Assert.assertTrue(doctorsNew.size() > doctorsOld.size());
    }

    @Test
    void findAll() {
        List<Doctor> doctors = doctorDAO.findAll();
        Assert.assertFalse(doctors.isEmpty());
    }

    @Test
    void update() {
        Doctor doctor = doctorDAO.findAll().get(0);
        doctor.setFirstName("newName");
        doctorDAO.update(doctor);
        doctor = doctorDAO.findAll().get(0);
        Assert.assertEquals("newName", doctor.getFirstName());
    }

    @Test
    void removeActive() {
        List<Doctor> doctorsOld = doctorDAO.findAll();
        doctorDAO.remove(doctorsOld.get(0));
        List<Doctor> doctorsNew = doctorDAO.findAll();
        Assert.assertTrue(doctorsNew.size() == doctorsOld.size());
    }

    @Test
    void removeNonActive() {
        Doctor newDoctor = new Doctor(null, "q", "w", "e", "r");
        doctorDAO.add(newDoctor);
        List<Doctor> doctorsOld = doctorDAO.findAll();
        doctorDAO.remove(doctorsOld.get(2));
        List<Doctor> doctorsNew = doctorDAO.findAll();
        Assert.assertTrue(doctorsNew.size() < doctorsOld.size());
    }

    @Test
    void getStatistics() {
        Doctor doctor = doctorDAO.findAll().get(0);
        RecipeStatistics statistics = doctorDAO.getStatistics(doctor);
        Assert.assertEquals(2, statistics.getTotal());
        Assert.assertEquals(1, statistics.getStandard());
        Assert.assertEquals(1, statistics.getCito());
        Assert.assertEquals(0, statistics.getStatim());
    }
}