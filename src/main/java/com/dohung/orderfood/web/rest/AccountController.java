package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.PermissionCurrent;
import com.dohung.orderfood.domain.UserPermission;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.PermissionCurrentRepository;
import com.dohung.orderfood.repository.UserPermissionRepository;
import com.dohung.orderfood.web.rest.request.ChangePasswordKeycloak;
import com.dohung.orderfood.web.rest.request.UserChangePasswordRequestModel;
import com.dohung.orderfood.web.rest.request.UserDetailsRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.PUT;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.config.JHipsterDefaults;

@RestController
@RequestMapping("/api")
@Slf4j
public class AccountController {

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private PermissionCurrentRepository permissionCurrentRepository;

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

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity getAccountInfor(@PathVariable("accountId") String accountId) {
        RestTemplate restTemplate = new RestTemplate();

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();
        System.out.println("accessToken: " + accessToken);

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String getObjectURL = StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users/" + accountId;

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

    @PostMapping("/verify-password")
    public ResponseEntity verifyPassword() throws Exception {
        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        String clientId = "orderfoodservice";
        String username = "haitt";
        String password = "@Hung123";
        String clientSecret = "c782ec20-4cdf-4797-bcb6-4d6b211962ef";

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

        System.out.println(responseMessage.getStatusCode());
        System.out.println(responseMessage.getStatusCodeValue());
        System.out.println(responseMessage.getBody());

        //        net.minidev.json.JSONObject jsonObject = new net.minidev.json.JSONObject(responseMessage.getBody());

        //        accessToken = (String) jsonObject.get("access_token");
        //        System.out.println("access_token verifyCurrentPassword: " + accessToken);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, responseMessage.getStatusCodeValue()), HttpStatus.OK);
    }

    @PutMapping("/change-password/{userId}")
    @Transactional
    public ResponseEntity changePassword(
        @PathVariable("userId") String userId,
        @RequestBody UserChangePasswordRequestModel userChangePasswordRequestModel
    ) throws Exception {
        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        String clientId = "orderfoodservice";
        String clientSecret = "c782ec20-4cdf-4797-bcb6-4d6b211962ef";

        try {
            ResponseEntity<String> verifyPassword = MethodCommon.verifyCurrentPassword(
                clientId,
                userChangePasswordRequestModel.getUsername(),
                userChangePasswordRequestModel.getCurrentPassword(),
                clientSecret,
                accessToken
            );
            System.out.println("verifyPassword: " + verifyPassword);
        } catch (Exception e) {
            throw new ErrorException("Loi: " + e.getMessage());
        }

        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        ObjectMapper objectMapper = new ObjectMapper();

        ChangePasswordKeycloak changePasswordKeycloak = new ChangePasswordKeycloak(
            "password",
            userChangePasswordRequestModel.getNewPassword(),
            false
        );

        String accountObj = objectMapper.writeValueAsString(changePasswordKeycloak); //convert
        System.out.println("accountObj: " + accountObj);
        //        log.info("hello");

        HttpEntity<String> entity = new HttpEntity<String>(accountObj, headers);

        String createObjectURL =
            StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users/" + userId + "/reset-password";

        ResponseEntity<String> answer = restTemplate.exchange(createObjectURL, HttpMethod.PUT, entity, String.class);

        System.out.println("answer: " + answer);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 204 là thành công!
        JSONObject jsonObject = new JSONObject(answer);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.get("statusCodeValue")), HttpStatus.OK);
        //        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }

    @DeleteMapping("/delete-account/{id}")
    @Transactional
    public ResponseEntity deleteAccount(@PathVariable("id") String id, @RequestParam String username) throws Exception {
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

        Optional<UserPermission> optionalUserPermission = userPermissionRepository.findByUsername(username);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException("Không tìm thấy UserPermission với username: " + username);
        }
        userPermissionRepository.delete(optionalUserPermission.get());

        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            throw new ErrorException("Không tìm thấy PermissionCurrent với username: " + username);
        }
        permissionCurrentRepository.delete(optionalPermissionCurrent.get());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.get("statusCodeValue")), HttpStatus.OK);
        //        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }
}
