package org.example.LWords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ServingWebContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class,args);

        /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pass1 = encoder.encode("dkfl");
        String pass2 = encoder.encode("123");
        System.out.println(pass1);
        System.out.println(pass2);*/
    }
}
