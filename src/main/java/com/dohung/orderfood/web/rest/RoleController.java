package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Role;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.RoleRepository;
import com.dohung.orderfood.web.rest.request.CreateRoleRequestModel;
import com.dohung.orderfood.web.rest.request.UserDetailsRequestModel;
import com.dohung.orderfood.web.rest.response.RoleResponstDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDateTime;
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
        // them role vao db
        Role roleEntity = new Role();
        Optional<Role> roleCheck = roleRepository.findByName(createRoleRequestModel.getName());
        if (roleCheck.isPresent()) {
            throw new ErrorException("role name with name is " + createRoleRequestModel.getName() + " exist!");
        }
        roleEntity.setName(createRoleRequestModel.getName());
        roleEntity.setCreatedBy("api");
        roleEntity.setCreatedDate(LocalDateTime.now());

        Role roleRest = roleRepository.save(roleEntity);
        System.out.println(roleRest.toString());
        RoleResponstDto roleReturn = new RoleResponstDto();
        BeanUtils.copyProperties(roleRest, roleReturn);

        //        "statusCode": "CREATED",
        //            "statusCodeValue": 201 là thành công!
        JSONObject jsonObject = new JSONObject(answer);
        jsonObject.get("statusCodeValue");

        return new ResponseEntity(roleReturn, HttpStatus.OK);
    }

    @DeleteMapping("/delete-role/{id}")
    @Transactional
    public ResponseEntity deleteRole(@PathVariable("id") Integer id) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api

        // tìm trong db
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            //            log.info("Not found role object with roleId:= " + id);
            throw new ErrorException("Not found role object with roleId:= " + id);
        }
        Role roleRest = optionalRole.get();
        String roleName = roleRest.getName();

        //Xoa role trong db
        roleRepository.delete(roleRest);

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
    public ResponseEntity addRoleMappingToUser(@RequestParam String userId) throws Exception {
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

    @DeleteMapping("/role-mappings")
    @Transactional
    public ResponseEntity deleteRoleMappingToUser(@RequestParam String userId) throws Exception {
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
}
