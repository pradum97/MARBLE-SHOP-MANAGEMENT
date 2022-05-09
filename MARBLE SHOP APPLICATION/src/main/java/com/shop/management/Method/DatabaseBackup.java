package com.shop.management.Method;

import com.shop.management.PropertiesLoader;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

public class DatabaseBackup {
    String DB_USERNAME;
    String DB_PASSWORD;
    String DB_PORT;
    final String BACKUP_PATH = "C:\\Program Files\\PostgreSQL\\14\\bin\\pg_dump.exe";
    final String RESTORE_PATH = "C:\\Program Files\\PostgreSQL\\14\\bin\\pg_restore.exe";

    public DatabaseBackup() {

        Properties properties = new PropertiesLoader().getDbDetails();
        DB_USERNAME = properties.getProperty("DB_USERNAME");
        DB_PASSWORD = properties.getProperty("DB_PASSWORD");
        DB_PORT = properties.getProperty("DB_PORT");

    }
    private void restoreDatabase() throws IOException {

        String dbName = "checkBackup";
        String restorePath = "backup/back_06_05_2022.backup";

        Runtime r = Runtime.getRuntime();
        Process p;
        ProcessBuilder pb;
        r = Runtime.getRuntime();
        pb = new ProcessBuilder(
                RESTORE_PATH,
                "--host", "localhost",
                "--port", DB_PORT,
                "--username", DB_USERNAME,
                "--dbname", dbName,
                "--role", "postgres",
                "--no-password",
                "--verbose",
                restorePath);
        pb.redirectErrorStream(true);
        final Map<String, String> env = pb.environment();
        env.put("PGPASSWORD", DB_PASSWORD);
        p = pb.start();
        InputStream is = p.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String ll;
        while ((ll = br.readLine()) != null) {
            System.out.println(ll);
        }

    }

    public String startBackup() throws IOException {
      String pattern = "dd_MM_yyyy";
        String fileName = "backup_" + DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
        String backupExtension = ".backup";

        String path = System.getProperty("user.home")+"/DatabaseBackup/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            setHiddenAttrib(file);
        }

        String fullPath = path + fileName + backupExtension;
        Runtime rt = Runtime.getRuntime();
        Process p;
        ProcessBuilder pb;
        rt = Runtime.getRuntime();
        pb = new ProcessBuilder(
                BACKUP_PATH,
                "--host", "localhost",
                "--port", DB_PORT,
                "--username", DB_USERNAME,
                "--no-password",
                "--format", "custom",
                "--blobs",
                "--verbose", "--file", fullPath, "MARBLE_SHOP_MANAGEMENT");
        try {
            final Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", DB_PASSWORD);
            p = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                //  System.err.println(line);
                line = r.readLine();
            }
            r.close();
            p.waitFor();

            if (p.exitValue() == 0){

                return fullPath;
            }else {
                return null;
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    private static void setHiddenAttrib(File file) {
        try {
            Process p = Runtime.getRuntime().exec("attrib +H " + file.getPath());
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
