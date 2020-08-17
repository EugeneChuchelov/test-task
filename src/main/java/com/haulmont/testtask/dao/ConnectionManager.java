package com.haulmont.testtask.dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static final String USER = "SA";
    public static final String PASSWORD = "";
    public static String URL = "jdbc:hsqldb:file:database/taskDB";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public static void createDB() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = ConnectionManager.getConnection()) {
            String dbScript = readFile("src/main/resources/initDB.sql");
            connection.createStatement().executeUpdate(dbScript);
        } catch (SQLException throwables) {
            System.out.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }
    }

    public static String readFile(String fileName) {
        String content = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            content = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void setURL(String URL) {
        ConnectionManager.URL = URL;
    }
}
