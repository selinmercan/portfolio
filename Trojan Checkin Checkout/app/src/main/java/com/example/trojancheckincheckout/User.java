package com.example.trojancheckincheckout;

import android.widget.ImageView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    public String email;
    public String first_name;
    public String last_name;
    public String password;
    public String role;
    public String major;
    public long usc_ID;
    public String curr_loc;

    public ImageView profilePic;

    // need empty constructor to serialize for firebase
    public User() { }

    public User(String email, String first_name, String last_name, String password, String role, String major, long usc_ID, String curr_loc) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = hash(password);
        this.role = role;
        this.major = major;
        this.usc_ID = usc_ID;
        this.curr_loc = curr_loc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ImageView getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ImageView profilePic) {
        this.profilePic = profilePic;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getMajor() { return major; }

    public void setMajor(String major) { this.major = major; }

    public long getUsc_ID() { return usc_ID; }

    public void setUsc_ID(long usc_ID) { this.usc_ID = usc_ID; }

    public String getCurr_loc() { return curr_loc; }

    public void setCurr_loc(String curr_loc) { this.curr_loc = curr_loc; }

    /**
     * hashes plain-text passsword using md5
     * @param plain_text password input when creating user
     * @return hashed pasword
     */
    public static String hash(String plain_text) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(plain_text.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return email without @usc.edu part for use as ID in database (tommy@usc.edu --> tommy)
     */
    public static String getUserId(String email) {
        int index_of_at = email.indexOf('@');

        if (index_of_at == -1) {
            return email;
        }

        return email.substring(0, index_of_at);
    }

}
