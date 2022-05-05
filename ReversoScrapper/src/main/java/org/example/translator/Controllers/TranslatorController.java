package org.example.translator.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.translator.Services.ReversoScrapperService;
import org.example.translator.Services.YandexCloudTranslatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/translator")
@RequiredArgsConstructor
public class TranslatorController {
    private final ReversoScrapperService reversoScrapperService;
    private final YandexCloudTranslatorService yandexCloudTranslatorService;

    @GetMapping("reverso")
    public String getTranslationsFromReverso(@RequestParam("phrase") String phrase, @RequestParam("translationCount") int translationCount) {
        return reversoScrapperService.getTranslations(phrase, translationCount);
    }

    @GetMapping("yandex")
    public String getTranslationFromYandex(@RequestParam("phrase") String phrase, @RequestParam("targetLanguage") String targetLanguage) {
        return yandexCloudTranslatorService.getTranslation(phrase, targetLanguage);
    }

    @GetMapping("translate")
    public String getTranslations(@RequestParam("phrase") String phrase, @RequestParam("targetLanguage") String targetLanguage, @RequestParam("translationCount") int translationCount) {
        String yandexResult = yandexCloudTranslatorService.getTranslation(phrase, targetLanguage);
        String scrapperResults = reversoScrapperService.getTranslations(phrase, translationCount);
        StringBuilder result = new StringBuilder(yandexResult + ';');

        if(scrapperResults != null){
            for (String scrapperResult : scrapperResults.split(";")) {
                if (!scrapperResult.equals(yandexResult)) {
                    result.append(scrapperResult).append(';');
                }
            }
        }

        return result.toString();
    }
}
