package com.shop.management;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public Properties getInsertProp() {
        String path = "util/insertQuery.properties";
        InputStream is = getClass().getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Properties getReadProp() {
        String path = "util/readQuery.properties";
        InputStream is = getClass().getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Properties getUpdateProp() {
        String path = "util/updateQuery.properties";
        InputStream is = getClass().getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Properties getDeleteProp() {
        String path = "util/deleteQuery.properties";
        InputStream is = getClass().getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Properties getDbDetails() {
        String path = "util/db.properties";
        InputStream is = getClass().getResourceAsStream(path);
        Properties properties = new Properties();

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return properties;
    }
}
