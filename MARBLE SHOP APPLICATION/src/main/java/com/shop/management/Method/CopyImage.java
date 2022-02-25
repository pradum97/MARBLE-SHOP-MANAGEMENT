package com.shop.management.Method;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class CopyImage {

    public String copy(String filePath, String fileName) {

        Random random = new Random();
        int num = random.nextInt(1, 1000);
        fileName = fileName.concat(String.valueOf(num));

        String newPath = "src/main/resources/com/shop/management/img/";
        File file_dir = new File(newPath);

        if (!file_dir.exists()) {
            if (file_dir.mkdirs()) {
                System.out.println("File Successfully Created");
            }
        }
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        File sourceFile = new File(filePath);
        File destinationFile = new File(newPath + fileName + "." + extension);
        try {

            Path path = Files.copy(sourceFile.toPath(), destinationFile.toPath());
            return path.toFile().getName();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
