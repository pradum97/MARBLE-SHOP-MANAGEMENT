package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Avatar implements Initializable {
    public GridPane gridPane;
   private String path = "src/main/resources/com/shop/management/img/Avatar/avtar_";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int cols=2, colCnt = 0, rowCnt = 0;

        for (int i = 2; i <= 16; i++) {
            InputStream is = null;

            ImageView iv = new ImageView();
            iv.setPreserveRatio(true);
            iv.setFitWidth(60);
            iv.setFitHeight(60);
            final  int i1 = i;
            iv.setStyle("-fx-cursor: hand");
            iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    String imgAvatarPath = "avtar_"+i1+".png";


                    Main.primaryStage.setUserData(imgAvatarPath);

                    Stage stage =  new  CustomDialog().stage2;

                    if (stage.isShowing()){
                        stage.hide();
                    }
                }
            });

            try {
                 is = new FileInputStream(new File(path+i+".png").getAbsolutePath());
                iv.setImage(new Image(is));

                gridPane.add(iv, colCnt, rowCnt);
                colCnt++;

                if (colCnt>cols) {
                    rowCnt++;
                    colCnt=0;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {

                try{

                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
