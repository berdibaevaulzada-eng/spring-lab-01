package kz.iitu.springlab;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class PasswordHashGenerator {

    public static void main(String[] args) {

        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("bcrypt", new BCryptPasswordEncoder());

        encoders.put(
                "argon2",
                Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        );

        PasswordEncoder encoder =
                new DelegatingPasswordEncoder("argon2", encoders);

        // New password uses Argon2
        String newPassword = "argon123";
        String argonHash = encoder.encode(newPassword);

        // Old password uses BCrypt
        String oldPassword = "user123";
        String bcryptHash = "{bcrypt}" +
                new BCryptPasswordEncoder().encode(oldPassword);

        System.out.println("New hash: " + argonHash);
        System.out.println("Argon2 verified: " +
                encoder.matches(newPassword, argonHash));

        System.out.println("Old hash: " + bcryptHash);
        System.out.println("BCrypt verified: " +
                encoder.matches(oldPassword, bcryptHash));
    }
}
