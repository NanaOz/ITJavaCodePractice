package app.javacode;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        SpringApplication.run(Main.class, args);
    }
}