package com.shop.management;

import javafx.scene.image.Image;

import java.io.File;
import java.util.Objects;

public class ImageLoader {

    public Image load(String imagePath){

        try {
         return  new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {

            System.out.println(imagePath);
            System.out.println(e.getMessage());
            return  new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon/img_preview.png")));
        }
    }
}
