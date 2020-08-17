package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.entity.Recipe;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.haulmont.testtask.dao.ConnectionManager.readFile;
import static org.junit.jupiter.api.Assertions.*;

class RecipeDAOTest {
    DoctorDAO doctorDAO = new DoctorDAO();
    PatientDAO patientDAO = new PatientDAO();
    RecipeDAO recipeDAO = new RecipeDAO();

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
        Doctor doctor = doctorDAO.findAll().get(0);
        Patient patient = patientDAO.findAll().get(0);
        Recipe recipe = new Recipe(null, "qwerty", patient,
                doctor, LocalDate.now(), LocalDate.now(), Priority.STANDARD);

        List<Recipe> recipesOld = recipeDAO.findAll();
        recipeDAO.add(recipe);
        List<Recipe> recipesNew = recipeDAO.findAll();

        Assert.assertTrue(recipesNew.size() > recipesOld.size());
    }

    @Test
    void findAll() {
        List<Recipe> recipes = recipeDAO.findAll();
        Assert.assertFalse(recipes.isEmpty());
    }

    @Test
    void findByDescription() {
        int count = recipeDAO.findByDescription("q").size();
        Assert.assertEquals(2, count);
    }

    @Test
    void findByPriority() {
        int count = recipeDAO.findByPriority(Priority.STANDARD).size();
        Assert.assertEquals(2, count);
    }

    @Test
    void findByPatient() {
        Patient patient = patientDAO.findAll().get(0);
        int count = recipeDAO.findByPatient(patient).size();
        Assert.assertEquals(2, count);
    }

    @Test
    void update() {
        Recipe recipe = recipeDAO.findAll().get(0);
        recipe.setDescription("newDescription");
        recipeDAO.update(recipe);
        recipe = recipeDAO.findAll().get(0);
        Assert.assertEquals("newDescription", recipe.getDescription());
    }

    @Test
    void remove() {
        List<Recipe> recipesOld = recipeDAO.findAll();
        recipeDAO.remove(recipesOld.get(0));
        List<Recipe> recipesNew = recipeDAO.findAll();
        Assert.assertTrue(recipesNew.size() < recipesOld.size());
    }
}