package com.shop.management.Method;

import com.shop.management.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class TableCreate {
    public void createLicenseTable() {
        Connection connection = null;
        Statement ps = null;

        try {
            connection = new DBConnection().getConnection();
            if (null == connection) {
                return;
            }
            String query = "CREATE TABLE IF NOT EXISTS TBL_LICENSE\n" +
                    "(\n" +
                    "    LICENSE_ID          SERIAL PRIMARY KEY,\n" +
                    "    COMPANY_ID          INT     unique     NOT NULL,\n" +
                    "    APPLICATION_ID      VARCHAR(50) unique NOT NULL,\n" +
                    "    SERIAL_KEY          VARCHAR(100) unique NOT NULL,\n" +
                    "    START_ON            VARCHAR(20)  NOT NULL,\n" +
                    "    EXPIRES_ON          VARCHAR(20)  NOT NULL,\n" +
                    "    LICENSE_TYPE        VARCHAR(50)  NOT NULL,\n" +
                    "    LICENSE_PERIOD_DAYS INTEGER      NOT NULL,\n" +
                    "    REGISTERED_EMAIL    VARCHAR(100) NOT NULL\n" +
                    ");";

            ps = connection.createStatement();
            ps.addBatch(query);
            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    //
                }
            }
            DBConnection.closeConnection(connection, null, null);
        }

    }
}
