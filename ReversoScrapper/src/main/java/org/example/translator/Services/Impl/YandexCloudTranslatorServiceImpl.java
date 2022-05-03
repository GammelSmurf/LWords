package org.example.translator.Services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.translator.Services.YandexCloudTranslatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexCloudTranslatorServiceImpl implements YandexCloudTranslatorService {
    private final String YANDEX_GET_IAM_TOKEN_URL = "https://iam.api.cloud.yandex.net/iam/v1/tokens";
    private final String YANDEX_CLOUD_TRANSLATOR_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";
    private final String FOLDER_ID = "b1g4all8pkba53rt363q";

    @Value("${yandex.oath-token}")
    private String oathToken;

    @Override
    public String getTranslation(String phrase, String targetLanguage) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            JSONObject requestBody = new JSONObject();
            requestBody.put("folderId", FOLDER_ID);
            requestBody.put("texts", Collections.singletonList(phrase));
            requestBody.put("targetLanguageCode", targetLanguage);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getIamToken());

            HttpEntity<String> request =
                    new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> yandexResponse
                    = restTemplate.postForEntity(YANDEX_CLOUD_TRANSLATOR_URL, request, String.class);

            return parseYandexResponse(yandexResponse.getBody());
        }
        catch (JSONException ex){
            log.error("Error while getting translation via Yandex!", ex);
        }
        return null;
    }

    private String parseYandexResponse(String yandexResponse) throws JSONException {
        JSONObject response = new JSONObject(yandexResponse);
        return StringUtils.substringBetween(response.getJSONArray("translations").getJSONObject(0).getString("text"), "[","]");
    }

    private String getIamToken() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("yandexPassportOauthToken", oathToken);
        HttpEntity<String> request =
                new HttpEntity<>(requestBody.toString());

        ResponseEntity<String> response
                = restTemplate.postForEntity(YANDEX_GET_IAM_TOKEN_URL, request, String.class);

        return new JSONObject(response.getBody()).getString("iamToken");
    }
}
