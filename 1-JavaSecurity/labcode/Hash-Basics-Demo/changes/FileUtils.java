package com.workshop.basics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
  
  
  public static void writeBytesToFile(String fileName, byte[] byteArray) {

    try {
      Path path = Paths.get(fileName);
      Files.write(path, byteArray);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  
  public static byte[] readBytesFromFile(String fileName) {

    byte[] fileByteContent = new byte[0];

    try {
      fileByteContent = Files.readAllBytes(Paths.get(fileName));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileByteContent;

  }

  public static void writeStringsToFile(String fileName, List<String> stringsToWrite) {

    try {
      FileWriter writer = new FileWriter(fileName);
      for (String s : stringsToWrite) {
        writer.write(s);
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  
  public static List<String> readStringsFromFile(String fileName) {
    
    try {
      return Files.readAllLines(Paths.get(fileName));
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
  
  public static void writeObjectToFile(String fileName, Object serObj) {
    
    try {
      
      FileOutputStream fileOut = new FileOutputStream(new File(fileName));
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(serObj);
      objectOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  public static Object readObjectFromFile(String fileName) {
    
    Object objRead = null;
    try {
      FileInputStream fi = new FileInputStream(new File(fileName));
      ObjectInputStream oi = new ObjectInputStream(fi);
      objRead = oi.readObject();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return objRead;
    
  }


}
