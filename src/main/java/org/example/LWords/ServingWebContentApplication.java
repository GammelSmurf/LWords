package org.example.LWords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ServingWebContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class,args);
        /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pass = encoder.encode("12345");
        System.out.println(pass);*/
    }
}
