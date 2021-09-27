package com.workshop.basics;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class FileDemo {

  public void writeBytesToFile(String fileName, byte[] byteArray) throws IOException {

    try (FileOutputStream fos = new FileOutputStream(fileName)) {
      fos.write(byteArray);
    }
  }

  public void writeBytesToFileNio(String fileName, byte[] byteArray) {

    try {
      Path path = Paths.get(fileName);
      Files.write(path, byteArray);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void writeStringsToFile(String fileName, List<String> stringsToWrite) {

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
  
  public List<String> readStringsFromFile(String fileName) {
    
    try {
      return Files.readAllLines(Paths.get(fileName));
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public static void main(String[] args) {

    FileDemo fd = new FileDemo();

    // Fill byte array with ASCII values corresponding to unprintable characters
    byte[] invisibleArray = new byte[30];
    for (int i = 0; i < 30; i++)
      invisibleArray[i] = (byte) i;

    System.out.println ("Invisible Byte Array");
    System.out.println (Arrays.toString(invisibleArray));    
    
    String firstFile = "invisible.txt";

    System.out.println("Writing byte array with unprintable characters to : " + firstFile);
    fd.writeBytesToFileNio(firstFile, invisibleArray);

    BasicUtils.waitKeyPress();

    System.out.println("Reading back from " + firstFile + " into a new byte array");

    byte[] fileByteContent = new byte[0];

    try {
      fileByteContent = Files.readAllBytes(Paths.get(firstFile));
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println (Arrays.toString(fileByteContent));    



    String secondFile = "base64.txt";
    System.out.println("Converting invisible byte array to base64 string and writing to : " + secondFile);

    String base64String = Base64.getEncoder().encodeToString(invisibleArray);
    List<String> stringsToAdd = new ArrayList<String>();
    stringsToAdd.add(base64String);
    fd.writeStringsToFile(secondFile, stringsToAdd);
    
    BasicUtils.waitKeyPress();
    
    System.out.println("Reading back from " + secondFile + " into a String list");
    List<String> stringsToRead = new ArrayList<String>();
    try {
      stringsToRead = Files.readAllLines(Paths.get(secondFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println ("Converting the first string back into a byte array");
    fileByteContent = Base64.getDecoder().decode(stringsToRead.get(0));
    System.out.println (Arrays.toString(fileByteContent));    

    

    String thirdFile = "hexstring.txt";
    System.out.println("Converting invisible byte array to hex string and writing to : " + thirdFile);
    String hexString = Hex.encodeHexString(invisibleArray);
    stringsToAdd = new ArrayList<String>();
    stringsToAdd.add(hexString);
    fd.writeStringsToFile(thirdFile, stringsToAdd);
    
    BasicUtils.waitKeyPress();
    
    System.out.println("Reading back from " + thirdFile + " into a String list");
    stringsToRead = new ArrayList<String>();
    try {
      stringsToRead = Files.readAllLines(Paths.get(thirdFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println ("Converting the first string back into a byte array");
    try {
      fileByteContent = Hex.decodeHex(stringsToRead.get(0));
    } catch (DecoderException e) {
      e.printStackTrace();
    }

    System.out.println (Arrays.toString(fileByteContent));    

  }

}
