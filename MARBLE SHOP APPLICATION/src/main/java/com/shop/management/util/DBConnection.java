package com.shop.management.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {

    public Connection getConnection() {

        Properties properties = getProperties("query.properties");

        String DB_URL = properties.getProperty("DB_URL");
        String DB_USERNAME = properties.getProperty("DB_USERNAME");
        String DB_PASSWORD = properties.getProperty("DB_PASSWORD");

        try {

            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    public Properties getProperties(String filename){

        try {
            File file = new File("src/main/java/com/shop/management/util/" + filename);
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            Properties prop = new Properties();
            prop.load(fileInputStream);
            return prop;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
