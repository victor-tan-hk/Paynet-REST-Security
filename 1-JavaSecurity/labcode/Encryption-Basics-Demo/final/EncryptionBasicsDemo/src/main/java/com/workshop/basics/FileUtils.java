package com.workshop.basics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

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

  public static void encryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
      File inputFile, File outputFile, int bufferSize) {
      
      try {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();

      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
      } catch (BadPaddingException e) {
        e.printStackTrace();
      }

  }
  
  
  public static void decryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
      File inputFile, File outputFile, int bufferSize) {
      
      try {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();

      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
      } catch (InvalidKeyException e) {
        e.printStackTrace();
      } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
      } catch (BadPaddingException e) {
        e.printStackTrace();
      }

  }  
  
  public static void encryptWithCipherOutputStream(String algorithm, SecretKey key, IvParameterSpec ivParamSpec, byte[] contentToEncrypt, File outputFileName) {

    try {
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, key, ivParamSpec);
      byte[] iv = cipher.getIV();

      try (FileOutputStream fileOut = new FileOutputStream(outputFileName);
          CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
        fileOut.write(iv);
        cipherOut.write(contentToEncrypt);
      } catch (IOException e) {
        e.printStackTrace();
      }

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }

  }
  
  
  public static String decryptWithCipherInputStream(String algorithm, SecretKey key, int ivLength, File inputFileName) {

    String content = "";

    try (FileInputStream fileIn = new FileInputStream(inputFileName)) {
      byte[] fileIv = new byte[ivLength];
      fileIn.read(fileIv);
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(fileIv));

      try (CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
          InputStreamReader inputReader = new InputStreamReader(cipherIn);
          BufferedReader reader = new BufferedReader(inputReader)) {

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
        content = sb.toString();
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return content;

  }

  
}
