import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Credentials {

 private String username;
 private String password;
 private final Set<String> inappropriateWords = new HashSet<>();
 public String filePath;
 static Scanner sc = new Scanner(System.in);
 public Crypto crypto;
 private User currentUser;

 public Credentials(String filePath) {
  this.filePath = filePath;
  crypto = new Crypto();
  crypto.init();
  loadInappropriateWords(filePath);
 }

 private void loadInappropriateWords(String filePath) {
  try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
   String line;
   while ((line = br.readLine()) != null) {
    String word = line.replaceAll("^,+|,+$", "")
            .replaceAll("^\"|\"$", "")
            .trim()
            .toLowerCase();
    if (!word.isEmpty()) {
     inappropriateWords.add(word);
    }
   }
  } catch (IOException e) {
   LoggerUtility.severe("Inappropriate word filtering failed, CODE: 00001X, in class: " + Credentials.class.getName());
  }
 }

 private boolean usernameExists(String inputUsername) {
  try (BufferedReader reader = new BufferedReader(new FileReader("encrypted/credentials.txt"))) {
   String line;
   crypto.loadKeyAndIv();
   while ((line = reader.readLine()) != null) {
    String[] encryptedCredentials = line.trim().split(":");
    if (encryptedCredentials.length < 2) {
     LoggerUtility.severe("Malformed credentials line: " + line);
     continue;
    }

    String encryptedUsername = encryptedCredentials[0];
    String decryptedUsername;

    try {
     decryptedUsername = crypto.decrypt(encryptedUsername);
    } catch (Exception e) {
     LoggerUtility.severe("Decryption exception, CODE: 00002X, in class: " + Credentials.class.getName());
     continue;
    }

    if (inputUsername.equals(decryptedUsername)) {
     return true;
    }
   }
  } catch (IOException e) {
   LoggerUtility.severe("Error reading credentials file, CODE: 00003X, in class: " + Credentials.class.getName());
  }
  return false;
 }

 private boolean containsInappropriateLanguage(String input) {
  input = input.toLowerCase();
  for (String word : inappropriateWords) {
   if (input.contains(word)) {
    return true;
   }
  }
  return false;
 }

 private boolean isWhitespace(String input) {
  return input == null || input.trim().isEmpty();
 }

 public void createLogin() {
  boolean validInput = false;

  while (!validInput) {
   System.out.println("Press 0 to exit..");
   System.out.println();
   System.out.println("Please enter your new username:");
   String inputUsername = sc.nextLine().trim().toLowerCase();

   if (inputUsername.equals("0")) {
    System.out.println("Exiting login creation..");
    return;
   }

   if (isWhitespace(inputUsername)) {
    System.out.println("Username cannot be empty or whitespace. Please choose another username.");
    continue;
   }

   if (containsInappropriateLanguage(inputUsername)) {
    System.out.println("Username contains inappropriate language. Please choose another username.");
    continue;
   }

   if (usernameExists(inputUsername)) {
    System.out.println("This username is already taken. Please choose another username.");
    continue;
   }

   System.out.println("Please enter your new password:");
   String inputPassword = sc.nextLine().trim().toLowerCase();

   if (inputPassword.equals("0")) {
    System.out.println("Exiting login creation..");
    return;
   }

   if (isWhitespace(inputPassword)) {
    System.out.println("Password cannot be empty or whitespace. Please choose another password.");
    continue;
   }

   if (containsInappropriateLanguage(inputPassword)) {
    System.out.println("Password contains inappropriate language. Please choose another password.");
    continue;
   }

   this.username = inputUsername;
   this.password = inputPassword;

   try {
    String encryptedUsername = crypto.encrypt(this.username);
    String encryptedPassword = crypto.encrypt(this.password);
    String credentialsFilePath = "encrypted/credentials.txt";

    try (FileWriter writer = new FileWriter(credentialsFilePath, true)) {
     writer.write(encryptedUsername + ":" + encryptedPassword + "\n");
    } catch (IOException e) {
     LoggerUtility.severe("Failed to write encrypted credentials to file, CODE: 00004X, in class: " + Credentials.class.getName());
    }

    crypto.storeKeyAndIv("encrypted/keyFile", "encrypted/ivFile");
    System.out.println("Login has been created successfully with username: " + this.username);
    validInput = true;
   } catch (Exception e) {
    LoggerUtility.severe("Encryption exception, CODE: 00005X, in class: " + Credentials.class.getName());
   }
  }
 }

 public User login() {
  boolean validLogin = false;
  User loggedInUser = null;

  while (!validLogin) {
   System.out.println("Press 0 to exit.");
   System.out.println("Please enter your username:");
   String inputUsername = sc.nextLine().trim().toLowerCase();

   if (inputUsername.equals("0")) {
    System.out.println("Exiting login.");
    return null;
   }

   if (isWhitespace(inputUsername)) {
    System.out.println("Username cannot be empty or whitespace. Please enter a valid username.");
    continue;
   }

   try (BufferedReader reader = new BufferedReader(new FileReader("encrypted/credentials.txt"))) {
    String line;
    boolean found = false;
    crypto.loadKeyAndIv();
    while ((line = reader.readLine()) != null) {
     line = line.trim();
     String[] encryptedCredentials = line.split(":");
     if (encryptedCredentials.length < 2) {
      LoggerUtility.severe("Malformed credentials line: " + line);
      continue;
     }

     String encryptedUsername = encryptedCredentials[0];
     String encryptedPassword = encryptedCredentials[1];
     String decryptedUsername;
     String decryptedPassword;

     try {
      decryptedUsername = crypto.decrypt(encryptedUsername);
      decryptedPassword = crypto.decrypt(encryptedPassword).trim();
     } catch (Exception e) {
      LoggerUtility.severe("Decryption exception, CODE: 00006X, in class: " + Credentials.class.getName());
      continue;
     }

     if (inputUsername.equals(decryptedUsername)) {
      found = true;
      System.out.println("Please enter your password:");
      String inputPassword = sc.nextLine().trim().toLowerCase();

      if (inputPassword.equals("0")) {
       System.out.println("Exiting login.");
       return null;
      }

      if (isWhitespace(inputPassword)) {
       System.out.println("Password cannot be empty or whitespace. Please enter a valid password.");
       continue;
      }

      if (inputPassword.equals(decryptedPassword)) {
       System.out.println("Login successful!");
       currentUser = new User(decryptedUsername, decryptedPassword);
       validLogin = true;
       break;
      } else {
       System.out.println("Incorrect password.");
      }
      break;
     }
    }
    if (!found && !inputUsername.equals("0")) {
     System.out.println("Username not found.");
    }
   } catch (IOException e) {
    LoggerUtility.severe("Error reading credentials file, CODE: 00007X, in class: " + Credentials.class.getName());
   }
  }
  return currentUser;
 }

 public User getCurrentUser() {
  return currentUser;
 }

 public String getUsername() {
  return this.username;
 }

 public String getPassword() {
  return this.password;
 }

 public String prepare() {
  return username + ":" + password;
 }
}