package com.dohung.orderfood.common;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
}
