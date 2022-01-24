package com.shop.management.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConnection {
    public static void main(String[] args) {
        DbConnection dbConnection=new DbConnection();
        System.out.println(dbConnection.connection());
    }

    public  Properties propertiesFile(){
        FileInputStream file=null;
        Properties prop=null;
        try{
            file= new FileInputStream("src/main/java/com/shop/management/utilfile/queary.properties");
            prop=new Properties();
            prop.load(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public boolean connection(){
        Properties prop=propertiesFile();
        String user= prop.getProperty("username");
        String password=prop.getProperty("password");
        String url=prop.getProperty("url");
        Connection conn=null;
        try{
            conn= DriverManager.getConnection(user,password,url);
            Statement stmt=conn.createStatement();
        } catch (SQLException sql) {
            sql.printStackTrace();
            System.out.println("connection fail");
        }
        return false;
    }

}
