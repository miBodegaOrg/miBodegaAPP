package com.mibodega.mystore.shared;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public String encryptSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String convertDateToClearFormat(String date){
        String createdAtString = date;

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date createdAtDate = null;
        try {
            createdAtDate = isoFormat.parse(createdAtString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        SimpleDateFormat desiredFormat = new SimpleDateFormat("EEEE d MMM h:mm a", new Locale("es", "ES"));
        String formattedDate = desiredFormat.format(createdAtDate);
        return formattedDate;
    }
}
