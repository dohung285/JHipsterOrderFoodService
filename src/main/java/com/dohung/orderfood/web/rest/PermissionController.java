package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.*;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.ActionSystemRepository;
import com.dohung.orderfood.repository.FunctionSystemRepository;
import com.dohung.orderfood.repository.PermissionCurrentRepository;
import com.dohung.orderfood.repository.UserPermissionRepository;
import com.dohung.orderfood.web.rest.request.CreatePermissionRequestModel;
import com.dohung.orderfood.web.rest.request.CreateRoleRequestModel;
import com.dohung.orderfood.web.rest.response.FoodDetailResponseDto;
import com.dohung.orderfood.web.rest.response.FoodResponseDto;
import com.dohung.orderfood.web.rest.response.ObjectChildrenTree;
import com.dohung.orderfood.web.rest.response.ObjectTreePer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
                userPermissionParam.setActionId(UUID.fromString(x));

                userPermissionParam.setCreatedBy("api");
                userPermissionParam.setCreatedDate(LocalDateTime.now());
                userPermissionParam.setLastModifiedDate(LocalDateTime.now());

                listParam.add(userPermissionParam);
            }
        }
        List<UserPermission> listReturn = userPermissionRepository.saveAll(listParam);
        if (listReturn.size() > 0) {
            Map<String, Object> map = createPermissionRequestModel.getCurrentPermission();

            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            System.out.println("jsonObject: " + jsonObject);

            PermissionCurrent permissionCurrentParam = null;
            Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
            if (optionalPermissionCurrent.isPresent()) {
                permissionCurrentParam = optionalPermissionCurrent.get();

                permissionCurrentParam.setUsername(username);
                permissionCurrentParam.setCurrentPer(jsonObject.toString());
                permissionCurrentParam.setLastModifiedDate(LocalDateTime.now());
            } else {
                permissionCurrentParam = new PermissionCurrent();

                permissionCurrentParam.setUsername(username);
                permissionCurrentParam.setCurrentPer(jsonObject.toString());
                permissionCurrentParam.setCreatedBy("api");
                permissionCurrentParam.setCreatedDate(LocalDateTime.now());
                permissionCurrentParam.setLastModifiedDate(LocalDateTime.now());
            }
            permissionCurrentRepository.save(permissionCurrentParam);
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }
}
