package sptech.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class Main {
    public static void main(String[] args) {
        var encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10);
        System.out.println("HASH CORRETO PARA 123456: " + encoder.encode("123456"));

        SpringApplication.run(Main.class, args);
    }
}