package org.example.LWords.Services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.LWords.Services.TranslatorClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class TranslatorClientImpl implements TranslatorClient {
    private final String REVERSO_SCRAPPER_ROOT_URL = "http://localhost:8085/translator/translate";

    @Override
    public String getTranslations(String phrase, int translationCount) {
        RestTemplate restTemplate = new RestTemplate();
        String uriTemplate = UriComponentsBuilder.fromUriString(REVERSO_SCRAPPER_ROOT_URL)
                .queryParam("phrase", phrase)
                .queryParam("translationCount", translationCount)
                .toUriString();
        ResponseEntity<String> response
                = restTemplate.getForEntity(uriTemplate, String.class);
        return response.getBody();
    }
}
