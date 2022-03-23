package com.shop.management;

import java.io.InputStream;

public class FileLoader {

    public InputStream load (String path){

      return   getClass().getResourceAsStream(path);
    }
}
