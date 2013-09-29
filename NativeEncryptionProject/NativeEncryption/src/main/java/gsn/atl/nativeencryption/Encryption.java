package gsn.atl.nativeencryption;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Encryption {
    public static byte[] encryptLibrary(SecretKey key) {
        Cipher encryptCipher = null;

        try {
            encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File directory = Environment.getExternalStorageDirectory();
        File inputFile = new File(directory.getAbsolutePath() + "/jibberish.data");
        File outputFile = new File(directory.getAbsolutePath() + "/encrypted_jibberish.data");

        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream in = null;
        try {
            in = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CipherOutputStream cipherOut = null;
        try {
            cipherOut = new CipherOutputStream(out, encryptCipher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int bufferSize = 16 * 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            while (in.available() > 0) {
                in.read(buffer, 0, buffer.length);
                cipherOut.write(buffer, 0, buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
            cipherOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encryptCipher.getIV();
    }
}
