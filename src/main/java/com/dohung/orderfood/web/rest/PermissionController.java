package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.ActionSystem;
import com.dohung.orderfood.domain.FunctionSystem;
import com.dohung.orderfood.domain.PermissionCurrent;
import com.dohung.orderfood.domain.UserPermission;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.ActionSystemRepository;
import com.dohung.orderfood.repository.FunctionSystemRepository;
import com.dohung.orderfood.repository.PermissionCurrentRepository;
import com.dohung.orderfood.repository.UserPermissionRepository;
import com.dohung.orderfood.web.rest.request.CreatePermissionRequestModel;
import com.dohung.orderfood.web.rest.response.ObjectChildrenTree;
import com.dohung.orderfood.web.rest.response.ObjectTreePer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PermissionController {

    @Autowired
    private FunctionSystemRepository functionSystemRepository;

    @Autowired
    private ActionSystemRepository actionSystemRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private PermissionCurrentRepository permissionCurrentRepository;

    @GetMapping("/permission/check")
    public ResponseEntity checkPermission(@RequestParam String username, @RequestParam String pathName, @RequestParam String action) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkExistPermission(username, pathName, action);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException(" Không có quyền truy cập");
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "Có quyền truy cập"), HttpStatus.OK);
    }

    @GetMapping("/permission/check-root")
    public ResponseEntity checkAccountIsRoot(@RequestParam String username) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkUsernameIsRoot(username);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException(" Không phải root");
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "Là root"), HttpStatus.OK);
    }

    @GetMapping("/permission/add-check")
    public ResponseEntity checkAddPermission(@RequestParam String username) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkAddPermission(username);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException(" Không có quyền truy cập");
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "Có quyền truy cập"), HttpStatus.OK);
    }

    // get all
    @GetMapping("/permission")
    public ResponseEntity getAll() {
        List<FunctionSystem> functionSystemList = functionSystemRepository.findAll();
        List<ObjectTreePer> objectTreePerList = functionSystemList
            .stream()
            .map(o -> new ObjectTreePer(o.getId(), o.getName()))
            .collect(Collectors.toList());

        List<ActionSystem> actionSystemList = actionSystemRepository.findAll();
        //        List<ObjectChildrenTree> objectChildrenTreeList = actionSystemList.stream().map(o -> new ObjectChildrenTree(o.getId(),o.getName())).collect(Collectors.toList());

        for (ObjectTreePer itemParent : objectTreePerList) {
            List<ObjectChildrenTree> childrenTreeList = actionSystemList
                .stream()
                .filter(x -> x.getFunctId() == itemParent.getKey())
                .map(o -> new ObjectChildrenTree(o.getId().toString(), o.getAction()))
                .collect(Collectors.toList());
            if (childrenTreeList.size() > 0) {
                itemParent.setChildren(childrenTreeList);
            }
        }

        System.out.println("objectTreePerList: " + objectTreePerList);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, objectTreePerList), HttpStatus.OK);
    }

    @GetMapping("/permission/function/{username}")
    public ResponseEntity getAllFunctionNameByUsername(@PathVariable("username") String username) {
        List<String> listReturn = functionSystemRepository.getAllFunctionNameByUsername(username);

        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            throw new ErrorException("Không tìm thấy PermissionCurrent với username: = " + username);
        }

        PermissionCurrent permissionCurrentRest = optionalPermissionCurrent.get();

        JSONObject jsonObject = new JSONObject(permissionCurrentRest.getCurrentPer().toString());

        System.out.println("permissionCurrentRest.getCurrentPer(): " + permissionCurrentRest.getCurrentPer());
        List<Integer> functIds = new ArrayList<>();
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            // loop to get the dynamic key
            String currentFunctKey = (String) keys.next();
            //            System.out.println("key: " + currentFunctKey);
            //            System.out.println(NumberUtils.isCreatable(currentFunctKey));
            if (NumberUtils.isCreatable(currentFunctKey)) {
                functIds.add(Integer.parseInt(currentFunctKey));
            }
        }
        //        for (Integer x : functIds) {
        //            System.out.println("x: " + x);
        //        }

        List<FunctionSystem> listResult = functionSystemRepository.findByIdIn(functIds);
        System.out.println("listResult: " + listResult);

        List<String> listStringName = listResult.stream().flatMap(o -> Stream.of(o.getName())).collect(Collectors.toList());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listStringName), HttpStatus.OK);
    }

    @GetMapping("/permission/{username}")
    public ResponseEntity getAllPermissionCurrentByUsername(@PathVariable("username") String username) {
        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            throw new ErrorException("Không tìm thấy PermissionCurrent với username: = " + username);
        }
        PermissionCurrent permissionCurrentRest = optionalPermissionCurrent.get();
        System.out.println("permissionCurrentRest.getCurrentPer(): " + permissionCurrentRest.getCurrentPer());
        JSONObject jsonObject = new JSONObject(permissionCurrentRest.getCurrentPer());
        System.out.println("json" + jsonObject);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.toString()), HttpStatus.OK);
    }

    @PostMapping("/permission")
    public ResponseEntity create(@RequestBody CreatePermissionRequestModel createPermissionRequestModel) throws Exception {
        System.out.println("currentPermission: " + createPermissionRequestModel.getCurrentPermission());

        List<UserPermission> listParam = new ArrayList<>();
        String username = createPermissionRequestModel.getUsername();

        for (String x : createPermissionRequestModel.getActionIds()) {
            System.out.println("createPermissionRequestModel.getActionIds(): " + x);
            if (!MethodCommon.isNumeric(x)) {
                UserPermission userPermissionParam = new UserPermission();
                userPermissionParam.setUsername(username);
                userPermissionParam.setActionId(x);

                userPermissionParam.setCreatedBy("api");
                userPermissionParam.setCreatedDate(LocalDateTime.now());
                userPermissionParam.setLastModifiedDate(LocalDateTime.now());

                listParam.add(userPermissionParam);
            }
        }
        List<UserPermission> listReturn = userPermissionRepository.saveAll(listParam);
        if (listReturn.size() > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = createPermissionRequestModel.getCurrentPermission();
            String jsonObjectMapper = objectMapper.writeValueAsString(map);

            System.out.println("jsonObjectMapper: " + jsonObjectMapper);

            PermissionCurrent permissionCurrentParam = null;
            Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
            if (optionalPermissionCurrent.isPresent()) { //TH đã có quyền  rồi thì là EDIT
                permissionCurrentParam = optionalPermissionCurrent.get();

                permissionCurrentParam.setUsername(username);
                permissionCurrentParam.setCurrentPer(jsonObjectMapper);
                permissionCurrentParam.setLastModifiedDate(LocalDateTime.now());
            } else { //TH đã có quyền  rồi thì là ADD
                permissionCurrentParam = new PermissionCurrent();

                permissionCurrentParam.setUsername(username);
                permissionCurrentParam.setCurrentPer(jsonObjectMapper);
                permissionCurrentParam.setCreatedBy("api");
                permissionCurrentParam.setCreatedDate(LocalDateTime.now());
                permissionCurrentParam.setLastModifiedDate(LocalDateTime.now());
            }
            permissionCurrentRepository.save(permissionCurrentParam);
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }
}
