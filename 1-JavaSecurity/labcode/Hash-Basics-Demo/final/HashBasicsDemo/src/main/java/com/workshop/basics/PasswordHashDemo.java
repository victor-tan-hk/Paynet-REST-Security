package com.workshop.basics;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class PasswordHashDemo {
  
  public static final String PBKDF_ALGORITHM = "PBKDF2WithHmacSHA256";  
  public static final String ALGORITHM_NAME = "AES";
  public static final int PBKDF_ITERATION = 65536; // can configure
  public static final int SALT_LENGTH = 16; // can configure
  public static final int PBKDF_KEY_LENGTH = 256; // fixed based on algorithm

  
  
  public static List<PasswordEntry> passwordList;
  public static Scanner scanner;


  public static void main(String[] args) {
    
    passwordList = new ArrayList<PasswordEntry>();
    scanner = new Scanner(System.in);

    while (true) {
      int mainChoice = getMainMenuChoice();
      
      switch (mainChoice) {
        
        case 1:
          registerAccount();
          break;
        case 2: 
          doLogin();
          break;
        case 3:
          displayTable();
          break;
        case 9:
          System.exit(0);
        default:
          System.out.println("Invalid option, please select again");
      }
      
    }
    

  }
  
  public static int getMainMenuChoice() {
    
    
    System.out.println ("\nOptions available");
    System.out.println ("[1] Register a new account");
    System.out.println ("[2] Login with an existing account");
    System.out.println ("[3] Display password table");
    System.out.println ("[9] Exit");
    System.out.print ("Select an option : ");

    String userInput = scanner.nextLine();

    int userChoice; 
    try {
      userChoice = Integer.parseInt(userInput);
    } catch (NumberFormatException e) {
      userChoice = -99; // invalid choice
    } 
    
    return userChoice;
  }
  
  
  public static void registerAccount() {
    
    System.out.println ("\n** Registering a new account **");
    
    String userName = "";
    boolean foundName = true;
    while (userName.length() < 1 && foundName) {
      System.out.print ("Enter a username : ");
      userName = scanner.nextLine();
      foundName = false;
      for (PasswordEntry pwe: passwordList) {
        if (pwe.getUserName().contentEquals(userName)) {
          System.out.println ("That user name already exists. Try again !");
          foundName = true;
          break;
        }
      }
    }
    String password = "";
    
    while (password.length() < 1) {
      System.out.print ("Enter a password : ");
      password = scanner.nextLine();
    }
    
    System.out.println ("Username : " + userName);
    System.out.println ("Password : " + password);
    
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[SALT_LENGTH];
    random.nextBytes(salt);
    
    SecretKey pbkdfKey = CryptoUtils.createKeyFromPassword(PBKDF_ALGORITHM, PBKDF_KEY_LENGTH, PBKDF_ITERATION, password, salt);
    
    String hashHex = Hex.encodeHexString(pbkdfKey.getEncoded());
    String saltHex = Hex.encodeHexString(salt);
    
    PasswordEntry newEntry = new PasswordEntry(userName,PBKDF_ITERATION,saltHex,hashHex);
    passwordList.add(newEntry);
    
    System.out.println ("Account registered !");

  }
  
  public static void doLogin() {
    
    System.out.println ("\n** Login attempt **");

    String userName = "";
    while (userName.length() < 1) {
      System.out.print ("Enter a username : ");
      userName = scanner.nextLine();
    }
    boolean foundName = false;
    PasswordEntry currEntry = new PasswordEntry();
    for (PasswordEntry pwe: passwordList) {
      if (pwe.getUserName().contentEquals(userName)) {
        currEntry = pwe;
        foundName = true;
        break;
      }
    }
    
    if (!foundName) {
      System.out.println ("That username is not registered in the system");
      return;
    }

    String password = "";
    while (password.length() < 1) {
      System.out.print ("Enter a password : ");
      password = scanner.nextLine();
    }
    
    byte[] salt = new byte[0];
    try {
      salt = Hex.decodeHex(currEntry.getSalt());
    } catch (DecoderException e) {
      e.printStackTrace();
    }
    int numIterations = currEntry.getIterations();
        
    SecretKey pbkdfKey = CryptoUtils.createKeyFromPassword(PBKDF_ALGORITHM, PBKDF_KEY_LENGTH, numIterations, password, salt);
    
    String hashHex = Hex.encodeHexString(pbkdfKey.getEncoded());
    System.out.println ("Computed hash on entered password  : " + hashHex);
    System.out.println ("Retrieved hash from password table : " + currEntry.getHash());
    if (hashHex.equals(currEntry.getHash())) 
      System.out.println ("Hashes match, correct password entered, you are logged in ! ");
    else
      System.out.println ("Hashes don't match, incorrect password entered ! ");
    
  }
  
  
  public static void displayTable() {
    
    if (passwordList.size() == 0) {
      System.out.println("Password table is currently empty");
      return;
    }
      
    System.out.println("\n *** Password table ***");
    for (PasswordEntry pwe: passwordList) {
      System.out.println (pwe.toString());
    }
    
    
  }
  

}
