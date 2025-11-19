package exceptions;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class User {
    private String firstName;
    private String lastName;
    private Character gender;
    private String address;
    private String userName;
    private String password;
    private MessageDigest digest;

    private static final int maxLoginAttempts = 3;
    private int loginAttempts = 0;

    private String hash(String strToHash) {
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(strToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public User(String firstName, String lastName, Character gender, String address, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.userName = userName;
        this.password = hash(password);
    }

    public boolean login(String username, String password) throws ExcessiveFailedLoginException {

        // Jika sudah pernah gagal 3 kali
        if (loginAttempts >= maxLoginAttempts) {
            throw new ExcessiveFailedLoginException("Anda telah mencapai jumlah batas login");
        }

        // Cocok username dulu
        if (this.userName.equals(username)) {

            // Jika password benar
            if (this.password.equals(hash(password))) {
                loginAttempts = 0;
                return true;

            } else {
                loginAttempts++;
                System.out.println("Password salah. Kesempatan tersisa: " + (maxLoginAttempts - loginAttempts));

                if (loginAttempts >= maxLoginAttempts) {
                    throw new ExcessiveFailedLoginException();
                }
            }
        }
        return false;
    }

    public String greeting() {
        String greet = "Selamat Datang!";

        switch (gender) {
            case 'L': greet += " Tuan "; break;
            case 'P': greet += " Nona "; break;
        }

        return greet + this.firstName + " " + this.lastName;
    }

    public String getUserName() {
        return userName;
    }
}
