package org.example.reversoscrapper.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.reversoscrapper.Services.ScrapperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/scrapper")
@RequiredArgsConstructor
public class ScrapperController {
    private final ScrapperService scrapperService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestHeader("username") String username){
        return new ResponseEntity<>("Hello, " + username, HttpStatus.OK);
    }

    @GetMapping
    public String getTranslations(@RequestParam("phrase") String phrase, @RequestParam("translationCount") int translationCount){
        return scrapperService.getTranslations(phrase, translationCount);
    }

}
