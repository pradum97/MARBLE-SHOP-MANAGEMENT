package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.DatabaseBackup;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DbBackup implements Initializable {
    final String BACKUP_URL = "http://localhost/LicenseManagerAPI/backup.php";
    public Label backupDateL;
    public Label lastBackupL;
    public Label msgLabel;
    public RadioButton localBackupRadio;
    public RadioButton serverBackupRadio;
    private DatabaseBackup backup;
    private CustomDialog customDialog;

    private static final String LOCAL_BACKUP = "local", SERVER_BACKUP = "server";
    private String backupType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backup = new DatabaseBackup();
        customDialog = new CustomDialog();

        setLabelData();

        localBackupRadio.setSelected(true);

        localBackupRadio.selectedProperty()
                .addListener((observableValue, aBoolean, t1) -> {

                    if (serverBackupRadio.isSelected()) {
                        serverBackupRadio.setSelected(false);
                    } else if (!localBackupRadio.isSelected()) {
                        localBackupRadio.setSelected(true);
                    }

                    setBackupType();
                });

        serverBackupRadio.selectedProperty()
                .addListener((observableValue, aBoolean, t1) -> {

                    if (localBackupRadio.isSelected()) {
                        localBackupRadio.setSelected(false);
                    } else if (!serverBackupRadio.isSelected()) {
                        serverBackupRadio.setSelected(true);
                    }

                    setBackupType();
                });

        setBackupType();
    }
    private void setBackupType() {

        if (localBackupRadio.isSelected()) {
            backupType = LOCAL_BACKUP;
        } else if (serverBackupRadio.isSelected()) {
            backupType = SERVER_BACKUP;
        }
    }


    private void setLabelData() {

        backupDateL.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        final String FIRST_REGEX = "backup_";
        final String LAST_REGEX = ".backup";
        final String PATTERN = "dd_MM_yyyy";
        String path = System.getProperty("user.home") + "/DatabaseBackup/";
        File dir = new File(path);
        String[] list = dir.list((dir1, name) -> name.toLowerCase().endsWith(LAST_REGEX));

        List<String> strDateList = new ArrayList<>();
        assert list != null;
        for (String s : list) {
            strDateList.add(s.replaceAll(FIRST_REGEX, "").replaceAll(LAST_REGEX, ""));
        }
        List<LocalDate> dateList = new ArrayList<>();
        for (String ds : strDateList) {
            dateList.add(LocalDate.parse(ds, DateTimeFormatter.ofPattern(PATTERN)));
        }
        Collections.sort(dateList);

        String lastBackup = null;
        for (LocalDate localDate : dateList) {
            lastBackup = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        lastBackupL.setText(lastBackup);
    }

    public void backupNow(ActionEvent actionEvent) throws IOException {

        setBackupType();

        String msg = "Are you sure you want to backup ?";

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(msg);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {

            switch(backupType) {

                case LOCAL_BACKUP -> startLocalBackup(actionEvent.getSource());

                case SERVER_BACKUP -> {

                    String path = backup.startBackup();

                    if (null != path) {
                        startServerBackup(actionEvent.getSource(), path);
                    } else {
                        msgLabel.setText("Backup not completed. Please Try Again!");
                        msgLabel.setStyle("-fx-text-fill: red");
                        customDialog.showAlertBox("", "Backup not completed. Please Try Again!");
                    }
                }
            }

        } else {
            alert.close();
        }
    }

    private void startServerBackup(Object source, String path) {

        msgLabel.setText("Please wait...");
        File file = new File(path);
        String app_id = getApplicationId();

        if (null == app_id) {
            customDialog.showAlertBox("Backup Failed", "Backup failed. because the license is not active");
            return;
        }

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(BACKUP_URL);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(file);

        builder.addPart("file", fileBody);
        builder.addTextBody("app_id", app_id);
        builder.addTextBody("extension", ".backup");
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        try {
            HttpResponse  response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String content = EntityUtils.toString(resEntity);
                JSONObject jo = new JSONObject(content);

                int statusCode = jo.getInt("statusCode");
                String msg = jo.getString("message");

                if (statusCode == 200) {
                    checkExtraFile((Node) source);
                } else {
                    msgLabel.setText("Backup not completed. Please Try Again!");
                    msgLabel.setStyle("-fx-text-fill: red");
                    customDialog.showAlertBox("", "Backup not completed. Please Try Again!");
                }
            }
        } catch (IOException e) {

            msgLabel.setText("");
            String msg = "Internet not connected. please connect to internet!\n";

            customDialog.showAlertBox("Failed", msg);
        }
    }

    private void startLocalBackup(Object source) throws IOException {
        msgLabel.setText("Please wait...");

        String path = backup.startBackup();

        if (null != path) {
            checkExtraFile((Node) source);
        } else {
            msgLabel.setText("Backup not completed. Please Try Again!");
            msgLabel.setStyle("-fx-text-fill: red");
            customDialog.showAlertBox("", "Backup not completed. Please Try Again!");
        }
    }

    private void checkExtraFile() {

        int requiredBackupFile = 5;
        final String FIRST_REGEX = "backup_";
        final String LAST_REGEX = ".backup";
        final String PATTERN = "dd_MM_yyyy";
        String path = System.getProperty("user.home") + "/DatabaseBackup/";
        File dir = new File(path);
        String[] list = dir.list((dir1, name) -> name.toLowerCase().endsWith(LAST_REGEX));

        List<String> strDateList = new ArrayList<>();
        for (String s : list) {
            strDateList.add(s.replaceAll(FIRST_REGEX, "").replaceAll(LAST_REGEX, ""));
        }
        List<LocalDate> dateList = new ArrayList<>();
        for (String ds : strDateList) {
            dateList.add(LocalDate.parse(ds, DateTimeFormatter.ofPattern(PATTERN)));
        }
        Collections.sort(dateList);

        int fileLength = dateList.size();
        if (fileLength >= requiredBackupFile) {
            int extraFileLength = fileLength - requiredBackupFile;
            for (int j = 0; j < extraFileLength; j++) {
                String filaName = FIRST_REGEX.concat(dateList.get(j).format(DateTimeFormatter.ofPattern(PATTERN))).concat(LAST_REGEX);
                String dPath = path.concat(filaName);

                File dFile = new File(dPath);
                if (dFile.exists()) {
                    dFile.delete();
                }
            }
        }
    }

    private void checkExtraFile(Node source) {
        checkExtraFile();
        msgLabel.setText("Successfully backed up");
        msgLabel.setStyle("-fx-text-fill: green");
        customDialog.showAlertBox("success", "Successfully backed up");
        Stage stage = (Stage) source.getScene().getWindow();
        if (null != stage && stage.isShowing()) {
            stage.close();
        }
    }

    private String getApplicationId() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = new DBConnection().getConnection();
            if (null == connection) {
                return null;
            }

            String query = "select application_id from tbl_license";
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                String appId = rs.getString("application_id");
                return appId;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }
}
