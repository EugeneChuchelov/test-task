package com.haulmont.testtask.dao;

import org.hsqldb.util.DatabaseManager;
import org.hsqldb.util.DatabaseManagerSwing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static final String URL = "jdbc:hsqldb:file:task";

    public static final String USER ="SA";

    public static final String PASSWORD = "";

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public static void createDB() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        //DatabaseManager.main(new String[] {
        //        "--url", ConnectionUtil.URL, "--noexit"});

        try (Connection connection = ConnectionUtil.getConnection()) {
            String dbScript = readFile("src/main/resources/initDB.sql");
            connection.createStatement().executeUpdate(dbScript);
            /*
            Only for test purpose
             */
            //dbScript = readFile("src/main/resources/fillDB.sql");
            //connection.createStatement().executeUpdate(dbScript);
            /*
            End of test intended code
             */
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static String readFile(String fileName){
        String content = "";
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            content = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
