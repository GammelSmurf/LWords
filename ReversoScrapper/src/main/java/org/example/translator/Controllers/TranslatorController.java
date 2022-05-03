package org.example.translator.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.translator.Services.ReversoScrapperService;
import org.example.translator.Services.YandexCloudTranslatorService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/translator")
@RequiredArgsConstructor
public class TranslatorController {
    private final ReversoScrapperService reversoScrapperService;
    private final YandexCloudTranslatorService yandexCloudTranslatorService;

    @GetMapping("reverso")
    public String getTranslationsFromReverso(@RequestParam("phrase") String phrase, @RequestParam("translationCount") int translationCount){
        return reversoScrapperService.getTranslations(phrase, translationCount);
    }

    @GetMapping("yandex")
    public String getTranslationFromYandex(@RequestParam("phrase") String phrase, @RequestParam("targetLanguage") String targetLanguage){
        return yandexCloudTranslatorService.getTranslation(phrase, targetLanguage);
    }

}
