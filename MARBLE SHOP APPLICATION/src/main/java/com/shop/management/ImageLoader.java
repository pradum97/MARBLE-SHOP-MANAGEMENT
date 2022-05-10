package com.shop.management;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageLoader {

    public Image load(String imagePath){

        try {
         return  new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            return  new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon/img_preview.png")));
        }
    }

    public Image loadWithSize(String imagePath){

        try {
         return  new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)) , 100,100 , false , true);
        } catch (Exception e) {
            return  new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon/img_preview.png")));
        }
    }
}
