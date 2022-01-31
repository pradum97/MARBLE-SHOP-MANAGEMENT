package com.shop.management.util;

import com.shop.management.Method.Method;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    Method method;

    public Connection getConnection() {
        method = new Method();

        Properties properties = method.getProperties("query.properties");

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


}
