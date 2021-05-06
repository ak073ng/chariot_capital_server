package com.chariotcapital.cashapp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Constants {
    public static String APP_SALT = "cHaRiOt-CaPiTaL";

    public static String getSecurePassword(String passwordToHash, byte[] salt){
        String generatedPassword = null;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            //Add password bytes to digest
            md.update(salt);

            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();

            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //Get complete hashed password in hex format
            generatedPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }
}
