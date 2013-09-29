package gsn.atl.nativeencryption;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Decryption {

    // statically load the native library
    static {
        System.loadLibrary("decrypt");
    }

    // our native decryption functions
    public native static void initCipher(byte[] key, int keylen, byte[] iv, int ivlen);

    public native static void update(byte[] in, int len, byte[] out);

    public native static void finalize(byte[] in, int len, byte[] out);

    public static void decryptLibrary(SecretKey key, byte[] iv) {
        Cipher decryptCipher = null;

        // AES with PKCS padding
        try {
            decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        // Need to use the initialization vector from encryption
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Initialize the cipher in decryption mode
        try {
            decryptCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Paths for encrypted and decrypted files
        File directory = Environment.getExternalStorageDirectory();
        File inputFile = new File(directory.getAbsolutePath() + "/encrypted_jibberish.data");
        File outputFile = new File(directory.getAbsolutePath() + "/decrypted_jibberish.data");

        // Create our output file
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open up the encrypted file
        FileInputStream in = null;
        try {
            in = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Open up output for writing
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Ciphered reader makes reading and applying crypto easy
        CipherInputStream cipherIn = null;
        try {
            cipherIn = new CipherInputStream(in, decryptCipher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Read the encrypted file in 16k chunks and write to output file
        final int bufferSize = 16 * 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            while (in.available() > 0) {
                cipherIn.read(buffer, 0, buffer.length);
                out.write(buffer, 0, buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clean things up
        try {
            in.close();
            cipherIn.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decryptNative(SecretKey key, byte[] iv) {
        initCipher(key.getEncoded(), key.getEncoded().length, iv, iv.length);

        // Paths for encrypted and decrypted files
        File directory = Environment.getExternalStorageDirectory();
        File inputFile = new File(directory.getAbsolutePath() + "/encrypted_jibberish.data");
        File outputFile = new File(directory.getAbsolutePath() + "/decrypted_jibberish.data");

        // Create our output file
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open up the encrypted file
        FileInputStream in = null;
        try {
            in = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Open up output for writing
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Read the encrypted file in 16k chunks and write to output file
        final int bufferSize = 16 * 1024;
        byte[] buffer = new byte[bufferSize];
        byte[] decrypted = new byte[bufferSize];
        int read;
        try {
            while (in.available() > 0) {
                read = in.read(buffer, 0, buffer.length);
                update(buffer, buffer.length, decrypted);
                if (read < bufferSize) {
                    finalize(buffer, buffer.length, decrypted);
                } else {
                    update(buffer, buffer.length, decrypted);
                }
                out.write(decrypted, 0, decrypted.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clean things up
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
