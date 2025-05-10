package app.javacode;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(Main.class, args);

        //для проверки
        System.out.println("admin: " + new BCryptPasswordEncoder().encode("admin123"));
        System.out.println("moderator: " + new BCryptPasswordEncoder().encode("moderator123"));
        System.out.println("user: " + new BCryptPasswordEncoder().encode("user123"));
    }
}