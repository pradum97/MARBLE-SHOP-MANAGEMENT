package com.shop.management.Model;

public class Product_Images {

    int imageID , productID;
    String imagePath;

    public Product_Images(int imageID, int productID, String imagePath) {
        this.imageID = imageID;
        this.productID = productID;
        this.imagePath = imagePath;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
