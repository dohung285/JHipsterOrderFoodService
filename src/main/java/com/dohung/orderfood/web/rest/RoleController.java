package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Role;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.RoleRepository;
import com.dohung.orderfood.web.rest.request.CreateRoleRequestModel;
import com.dohung.orderfood.web.rest.request.RoleMappingRequestModel;
import com.dohung.orderfood.web.rest.request.UserDetailsRequestModel;
import com.dohung.orderfood.web.rest.response.RoleResponstDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@Slf4j
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/roles")
    public ResponseEntity getAllRoleIntoRealm() throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String getObjectURL = StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/roles";

        ResponseEntity<String> answer = restTemplate.exchange(getObjectURL, HttpMethod.GET, entity, String.class);

        System.out.println("answer: " + answer);
        JSONObject jsonObject = new JSONObject(answer);
        //        jsonObject.get("statusCodeValue");
        //        jsonObject.get("body");

        return new ResponseEntity(jsonObject.get("body"), HttpStatus.OK);
    }

    @PostMapping("/create-role")
    @Transactional
    public ResponseEntity createRole(@RequestBody CreateRoleRequestModel createRoleRequestModel) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String roleObj = objectMapper.writeValueAsString(createRoleRequestModel); //convert
        System.out.println("createRoleRequestModel: " + createRoleRequestModel);
        //        log.info("hello");

        HttpEntity<String> entity = new HttpEntity<String>(roleObj, headers);
        // Thêm vào db keycloak
        String createObjectURL = "http://localhost:8080/auth/admin/realms/" + StringConstant.REALM + "/roles";

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(createObjectURL, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            ObjectMapper objectMapperError = new ObjectMapper();
            System.out.println(objectMapperError.writeValueAsString(e.getMessage()));
            throw new ErrorException(e.getMessage());
        }
        System.out.println("answer: " + answer);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 201 là thành công!
        JSONObject jsonObject = new JSONObject(answer);
        //        jsonObject.get("statusCodeValue");
        //        jsonObject.get("body");

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.get("statusCodeValue")), HttpStatus.OK);
    }

    @DeleteMapping("/delete-role/{roleName}")
    @Transactional
    public ResponseEntity deleteRole(@PathVariable("roleName") String roleName) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //Xoa role trong db-keycloak (keycloak)

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String deleteAccountURL = "http://localhost:8080/auth/admin/realms/" + StringConstant.REALM + "/roles/" + roleName;

        ResponseEntity<String> answer = restTemplate.exchange(deleteAccountURL, HttpMethod.DELETE, entity, String.class);

        System.out.println("answer: " + answer);
        JSONObject jsonObject = new JSONObject(answer);

        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }

    //Get role mappings

    @GetMapping("/role-mappings")
    @Transactional
    public ResponseEntity getRoleMappingByUserId(@RequestParam String userId) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        // Thêm vào db keycloak
        String createObjectURL =
            StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users/" + userId + "/role-mappings";

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(createObjectURL, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            ObjectMapper objectMapperError = new ObjectMapper();
            System.out.println(objectMapperError.writeValueAsString(e.getMessage()));
            throw new ErrorException(e.getMessage());
        }

        System.out.println("answer: " + answer);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 201 là thành công!
        JSONObject jsonObject = new JSONObject(answer);
        System.out.println("jsonObject: " + jsonObject);
        //        jsonObject.get("statusCodeValue");
        //        jsonObject.get("body");
        //        System.out.println("object: " + jsonObject.get("body"));

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(jsonObject.get("body"));
        System.out.println("jsonInString: " + jsonInString);

        return new ResponseEntity(jsonObject.get("body"), HttpStatus.OK);
    }

    @PostMapping("/role-mappings")
    @Transactional
    public ResponseEntity addRoleMappingToUser(
        @RequestParam String userId,
        @RequestBody List<RoleMappingRequestModel> roleMappingRequestModel
    ) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String roleObj = objectMapper.writeValueAsString(roleMappingRequestModel); //convert
        System.out.println("roleMappingRequestModel: " + roleMappingRequestModel);

        HttpEntity<String> entity = new HttpEntity<String>(roleObj, headers);

        String createObjectURL =
            StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users/" + userId + "/role-mappings/realm";

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(createObjectURL, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            ObjectMapper objectMapperError = new ObjectMapper();
            System.out.println(objectMapperError.writeValueAsString(e.getMessage()));
            throw new ErrorException(e.getMessage());
        }

        System.out.println("answer: " + answer);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 201 là thành công! 204
        JSONObject jsonObject = new JSONObject(answer);
        //        System.out.println("jsonObject: " + jsonObject);
        //        jsonObject.get("statusCodeValue");

        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }

    @DeleteMapping("/role-mappings") // return 200 ok
    @Transactional
    public ResponseEntity deleteRoleMappingToUser(
        @RequestParam String userId,
        @RequestBody List<RoleMappingRequestModel> roleMappingRequestModel
    ) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String createObjectURL =
            StringConstant.URL_API + "/auth/admin/realms/" + StringConstant.REALM + "/users/" + userId + "/role-mappings/realm";

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(createObjectURL, HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
            ObjectMapper objectMapperError = new ObjectMapper();
            System.out.println(objectMapperError.writeValueAsString(e.getMessage()));
            throw new ErrorException(e.getMessage());
        }

        System.out.println("answer: " + answer);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 201 là thành công!
        JSONObject jsonObject = new JSONObject(answer);
        //        System.out.println("jsonObject: " + jsonObject);
        //        //        jsonObject.get("statusCodeValue");
        //        //        jsonObject.get("body");
        //        //        System.out.println("object: " + jsonObject.get("body"));
        //
        //        ObjectMapper mapper = new ObjectMapper();
        //        String jsonInString = mapper.writeValueAsString(jsonObject.get("body"));
        //        System.out.println("jsonInString: " + jsonInString);

        return new ResponseEntity(jsonObject.get("statusCodeValue"), HttpStatus.OK);
    }
}
