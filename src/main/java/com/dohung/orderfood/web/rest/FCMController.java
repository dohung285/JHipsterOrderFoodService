package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.web.rest.request.PushNotificationRequest;
import com.dohung.orderfood.web.rest.response.PushNotificationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class FCMController {

    @GetMapping("/notification/data")
    public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest pushNotificationRequest)
        throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + pushNotificationRequest.getToken());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String objecJson = objectMapper.writeValueAsString(pushNotificationRequest); //convert
        System.out.println("pushNotificationRequest: " + pushNotificationRequest);

        HttpEntity<String> entity = new HttpEntity<String>(objecJson, headers);

        String createObjectURL = StringConstant.URL_API_FCM + "/notification/data";

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(createObjectURL, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            ObjectMapper objectMapperError = new ObjectMapper();
            System.out.println(objectMapperError.writeValueAsString(e.getMessage()));
            throw new ErrorException(e.getMessage());
        }

        System.out.println("answer: " + answer);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "Notification has been sent."), HttpStatus.OK);
    }
}
