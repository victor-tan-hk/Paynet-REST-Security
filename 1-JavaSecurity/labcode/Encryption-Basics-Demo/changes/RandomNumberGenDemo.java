package com.workshop.basics;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class RandomNumberGenDemo {
  
  
  public static void main( String[] args )
  {
    SecureRandom secureRandom = null;
    try {
      
      // checking default algo and provider
      secureRandom = new SecureRandom();
      System.out.println ("On this platform, the default algo is : " + secureRandom.getAlgorithm());
      System.out.println ("On this platform, the provider is : " + secureRandom.getProvider());
      // For windows the most secure algo is 
      secureRandom = SecureRandom.getInstance("Windows-PRNG");
      System.out.println ("Now using a new algo of : " + secureRandom.getAlgorithm());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    
    System.out.println ("Generating 5 random integers");
    for (int i = 0; i < 5; i++)
      System.out.print(secureRandom.nextInt() + " ");
    
    System.out.println ("\n\nGenerating 5 random floats");
    for (int i = 0; i < 5; i++)
      System.out.print(secureRandom.nextFloat() + " ");
    
    System.out.println ("\n\nGenerating 5 random integers with an upper bound of 20");
    for (int i = 0; i < 5; i++)
      System.out.print(secureRandom.nextInt(20) + " ");
    
    System.out.println ("\n\nGenerating a sequence of 10 random bytes");
    byte[] randBytes = new byte[10];
    secureRandom.nextBytes(randBytes);
    System.out.println (Arrays.toString(randBytes));
    
    
    System.out.println ("\n\nCreating two SecureRandom objects sr1 and sr2 with the same seed");
    byte[] someSeed = {1, 2, 3, 4, 5};
    SecureRandom sr1 = new SecureRandom(someSeed);
    SecureRandom sr2 = new SecureRandom(someSeed);
    
    System.out.println ("Generating 5 random integers from sr1");
    for (int i = 0; i < 5; i++)
      System.out.print(sr1.nextInt() + " ");
    
    System.out.println ("\n\nGenerating 5 random integers from sr2");
    for (int i = 0; i < 5; i++)
      System.out.print(sr2.nextInt() + " ");
    
    
    System.out.println ("\n\nReseeding sr2");
    byte[] anotherSeed = {6, 7, 8};
    sr2.setSeed(anotherSeed);

    System.out.println ("\n\nGenerating another 5 random integers from sr1");
    for (int i = 0; i < 5; i++)
      System.out.print(sr1.nextInt() + " ");
    
    System.out.println ("\n\nGenerating another 5 random integers from sr2");
    for (int i = 0; i < 5; i++)
      System.out.print(sr2.nextInt() + " ");
    
    // Generate a seed from one SecureRandom object
    // in order to reseed another one
    SecureRandom sr3 = new SecureRandom();
    sr1.setSeed(sr3.generateSeed(10));
    
  }
  
  

}
