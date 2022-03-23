package com.shop.management;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public Properties load(String fileName) {
        String path = "util/"+fileName;
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
