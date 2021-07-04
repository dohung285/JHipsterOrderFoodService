package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.web.rest.request.UserDetailsRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.config.JHipsterDefaults;

@RestController
@RequestMapping("/api")
@Slf4j
public class AccountController {

    @GetMapping("/accounts")
    public ResponseEntity authenticate() {
        RestTemplate restTemplate = new RestTemplate();

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();
        System.out.println("accessToken: " + accessToken);

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String getObjectURL = StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users";

        ResponseEntity<String> answer = restTemplate.exchange(getObjectURL, HttpMethod.GET, entity, String.class);

        System.out.println("answer: " + answer);

        JSONObject jsonObject = new JSONObject(answer);
        return new ResponseEntity(jsonObject.get("body"), HttpStatus.OK);
        //        return new ResponseEntity(jsonObject.get("body"), HttpStatus.OK);
    }

    @PostMapping("/create-account")
    public ResponseEntity createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.set("Authorization", "Bearer " + accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String accountObj = objectMapper.writeValueAsString(userDetails); //convert
        System.out.println("accountObj: " + accountObj);
        //        log.info("hello");

        HttpEntity<String> entity = new HttpEntity<String>(accountObj, headers2);

        String createObjectURL = StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users";

        ResponseEntity<String> answer = restTemplate.exchange(createObjectURL, HttpMethod.POST, entity, String.class);

        System.out.println("answer: " + answer);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 201 là thành công!
        JSONObject jsonObject = new JSONObject(answer);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.get("statusCodeValue")), HttpStatus.OK);
        //        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }

    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity deleteAccount(@PathVariable("id") String id) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        UUID uid = UUID.fromString(id);

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String deleteAccountURL = StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users/" + uid;

        ResponseEntity<String> answer = restTemplate.exchange(deleteAccountURL, HttpMethod.DELETE, entity, String.class);

        System.out.println("answer: " + answer);
        JSONObject jsonObject = new JSONObject(answer);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.get("statusCodeValue")), HttpStatus.OK);
        //        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }
}
