package com.dohung.orderfood.common;

import com.dohung.orderfood.domain.Comment;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import liquibase.pro.packaged.T;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MethodCommon {

    public static String getAccessToken() {
        String accessToken = null;
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        String url = "http://localhost:8080/auth/realms/master/protocol/openid-connect/token";
        postParameters.add("client_id", "admin-cli");
        postParameters.add("grant_type", "password");
        postParameters.add("username", "admin");
        postParameters.add("password", "admin");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        RestTemplate restTemplate = new RestTemplate();
        String responseMessage = restTemplate.postForObject(url, r, String.class);
        System.out.println("responseMessage: " + responseMessage); //access_token

        JSONObject jsonObject = new JSONObject(responseMessage);

        accessToken = (String) jsonObject.get("access_token");
        System.out.println("access_token: " + accessToken);

        return accessToken;
    }

    public static String verifyCurrentPassword(String clientId, String username, String password, String clientSecret, String accessToken) {
        //        String accessToken = MethodCommon.getAccessToken();

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        String url = "http://localhost:8080/auth/realms/orderfood/protocol/openid-connect/token";
        postParameters.add("client_id", clientId);
        postParameters.add("grant_type", "password");
        postParameters.add("username", username);
        postParameters.add("password", password);
        postParameters.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseMessage = restTemplate.exchange(url, HttpMethod.POST, r, String.class);

        return String.valueOf(responseMessage.getStatusCodeValue());
    }

    public static boolean isNumeric(final String str) {
        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
