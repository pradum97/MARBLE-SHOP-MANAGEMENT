package com.shop.management.Controller.SellItems;

import com.shop.management.Controller.Login;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.Sale_Main;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class PayDues implements Initializable {
    public ComboBox<String> paymentModeC;
    public Label duesAmount;
    public TextField receivedAmountTF;
    public TextField duesAmountTF;
    private Sale_Main saleMain;
    private DecimalFormat df = new DecimalFormat("0.##");
    private Method method;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();

        saleMain = (Sale_Main) Main.primaryStage.getUserData();

        setValue();
        comboBoxConfig();

        receivedAmountTF.textProperty().addListener((observableValue, s, t1) -> {
            double totalDues = saleMain.getDuesAmount();
            double receivedAmount = 0;
            try {
                receivedAmount = Double.parseDouble(t1);
            } catch (NumberFormatException ignored) {
            }

            if (receivedAmount <= totalDues) {

                double avlDues = totalDues - receivedAmount;

                duesAmountTF.setText(String.valueOf(Double.valueOf(df.format(avlDues))));
            } else {
                receivedAmountTF.setText("");
                duesAmountTF.setText("");
                method.show_popup("YOUR INVOICE VALUE IS : " + totalDues, receivedAmountTF);
            }

        });

    }

    private void comboBoxConfig() {

        paymentModeC.setItems(new StaticData().getPaymentMode());
        paymentModeC.getSelectionModel().selectFirst();

        paymentModeC.setButtonCell(new ListCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER);
                    Insets old = getPadding();
                    setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                }
            }
        });

        paymentModeC.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            setAlignment(Pos.CENTER);
                            setPadding(new Insets(3, 3, 3, 0));
                        }
                    }
                };
            }
        });
    }

    private void setValue() {
        duesAmount.setText(String.valueOf(saleMain.getDuesAmount()));
        duesAmountTF.setEditable(false);
        duesAmountTF.setText(String.valueOf(saleMain.getDuesAmount()));
    }

    public void PAY_100(ActionEvent actionEvent) {
        receivedAmountTF.setText("");
        receivedAmountTF.setText(String.valueOf(saleMain.getDuesAmount()));
    }

    public void payDues(ActionEvent event) {

        String paidAmount = receivedAmountTF.getText();
        String avlDues = duesAmountTF.getText();
        String paymentMode = paymentModeC.getSelectionModel().getSelectedItem();

        if (paidAmount.isEmpty()) {
            method.show_popup("Please Enter Received Amount", receivedAmountTF);
            return;
        }
        double paidAmountD = 0, avlDuesD = 0;
        try {
            paidAmountD = Double.parseDouble(paidAmount);
        } catch (NumberFormatException e) {
            method.show_popup("Enter Valid Amount", receivedAmountTF);
            return;
        }

        try {
            avlDuesD = Double.parseDouble(avlDues);
        } catch (NumberFormatException ignored) {

        }

        if (paidAmountD < 1) {
            method.show_popup("Enter more than 0", receivedAmountTF);
            return;
        }

        ImageView image = new ImageView(method.getImage("src/main/resources/com/shop/management/img/icon/warning_ic.png"));
        image.setFitWidth(45);
        image.setFitHeight(45);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setGraphic(image);
        alert.setHeaderText("Are you sure you want to pay the dues?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {

            Connection connection = null;
            PreparedStatement ps = null, psH = null;

            try {
                connection = new DBConnection().getConnection();
                if (null == connection) {
                    System.out.println("Connection failed");
                    return;
                }
                ps = connection.prepareStatement("UPDATE tbl_dues SET dues_amount = dues_amount-? where dues_id = ?");
                ps.setDouble(1, paidAmountD);
                ps.setInt(2, saleMain.getDuesId());

                int res = ps.executeUpdate();

                if (res > 0) {

                    ps = null;

                    ps = connection.prepareStatement("UPDATE TBL_SALE_MAIN SET received_amount = received_amount+? WHERE SALE_MAIN_ID = ?");
                    ps.setDouble(1,paidAmountD);
                    ps.setInt(2,saleMain.getSale_main_id());
                    ps.executeUpdate();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    String query = "INSERT INTO dues_history (DUES_ID, CUSTOMER_ID, SALE_MAIN_ID, PREVIOUS_DUES, PAID_AMOUNT, CURRENT_DUES, PAYMENT_MODE)\n" +
                            " VALUES (?,?,?,?,?,?,?)";
                    psH = connection.prepareStatement(query);
                    psH.setInt(1, saleMain.getDuesId());
                    psH.setInt(2, saleMain.getCustomerId());
                    psH.setInt(3, saleMain.getSale_main_id());
                    psH.setDouble(4, saleMain.getDuesAmount());
                    psH.setDouble(5, paidAmountD);
                    psH.setDouble(6, avlDuesD);
                    psH.setString(7, paymentMode);
                    int resH = psH.executeUpdate();

                    if (resH > 0) {

                        if (avlDuesD == 0){

                            ps  = null;
                            String duesQuery = "DELETE FROM TBL_DUES where dues_id ="+saleMain.getDuesId();
                            ps = connection.prepareStatement(duesQuery);
                           ps.executeUpdate();

                        }
                        stage.close();
                    }

                }


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnection.closeConnection(connection, ps, null);
                try {
                    if (null != psH) {
                        psH.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            alert.close();
        }
    }
}
