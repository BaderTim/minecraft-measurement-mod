package io.github.mmm;

import java.io.RandomAccessFile;

import static io.github.mmm.MMM.MMM_ROOT_PATH;

public class Utils {

    public static void saveStringToFile(String newData, String savePath, String fileName) {
        final String finalSavePath = MMM_ROOT_PATH + savePath + "/" + fileName;
        try {
            // Open the file in "rw" mode (read and write)
            RandomAccessFile file = new RandomAccessFile(finalSavePath, "rw");
            // Move the file pointer to the end of the file
            file.seek(file.length());
            // Write the data to the end of the file
            file.writeBytes(newData);
            // Close the file
            file.close();
        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

}
