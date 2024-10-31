import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;

public class Crypto {

    private SecretKey userKey;
    private byte[] iv;
    private final int KEY_SIZE = 128;

    public void init() {
        loadKeyAndIv();
    }

    public void loadKeyAndIv() {
        File keyFile = new File("encrypted/keyFile");
        File ivFile = new File("encrypted/ivFile");

        if (keyFile.exists() && keyFile.length() > 0 && ivFile.exists() && ivFile.length() > 0) {
            try (ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(keyFile));
                 ObjectInputStream ivIn = new ObjectInputStream(new FileInputStream(ivFile))) {
                userKey = (SecretKey) keyIn.readObject();
                iv = (byte[]) ivIn.readObject();
            } catch (Exception e) {
                LoggerUtility.severe("Error loading key and IV, Code: 00006X, in Class: " + Crypto.class.getName());
                generateKeyAndIv();
            }
        } else {
            generateKeyAndIv();
        }
    }

    private void generateKeyAndIv() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(KEY_SIZE);
            userKey = keyGen.generateKey();

            iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            storeKeyAndIv("encrypted/keyFile", "encrypted/ivFile");
        } catch (Exception e) {
            LoggerUtility.severe("Key and IV generation failed, Code: 00007X, in Class:  " + Crypto.class.getName());
        }
    }

    public void storeKeyAndIv(String keyFilePath, String ivFilePath) {
        try (ObjectOutputStream keyOut = new ObjectOutputStream(new FileOutputStream(keyFilePath));
             ObjectOutputStream ivOut = new ObjectOutputStream(new FileOutputStream(ivFilePath))) {
            keyOut.writeObject(userKey);
            ivOut.writeObject(iv);
        } catch (IOException e) {
            LoggerUtility.severe("Failed to write key and IV to file, Code: 00008X, in Class:" + Crypto.class.getName());
        }
    }

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, userKey, new IvParameterSpec(iv));
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            LoggerUtility.severe("Encryption failed, CODE: 00009X, in Class: " + Crypto.class.getName());
            return null;
        }
    }

    public String decrypt(String encryptedData) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, userKey, new IvParameterSpec(iv));
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            LoggerUtility.severe("Decryption failed, CODE: 00010X, in Class: " + Crypto.class.getName());
            return null;
        }
    }
}