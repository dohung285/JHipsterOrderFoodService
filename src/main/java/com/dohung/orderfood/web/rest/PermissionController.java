package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.ActionSystem;
import com.dohung.orderfood.domain.FunctionSystem;
import com.dohung.orderfood.domain.PermissionCurrent;
import com.dohung.orderfood.domain.UserPermission;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.*;
import com.dohung.orderfood.web.rest.request.CreatePermissionRequestModel;
import com.dohung.orderfood.web.rest.response.ObjectChildrenTree;
import com.dohung.orderfood.web.rest.response.ObjectTreePer;
import com.dohung.orderfood.web.rest.response.OrderIdResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Tuple;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/permission/check-order")
    public ResponseEntity checkPermissionOrdered(@RequestParam String username, @RequestParam Integer foodId) {
        List<Tuple> listResult = orderRepository.checkOrder(username, foodId);

        List<Integer> listReturn = listResult
            .stream()
            .map(x -> x.get(0, Integer.class))
            //            .map(x -> new OrderIdResponseDto())
            .collect(Collectors.toList());

        if (listReturn.size() <= 0) {
            return new ResponseEntity(new ResponseData(StringConstant.iERROR_EXCEPTION, "fail"), HttpStatus.OK);
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "successful"), HttpStatus.OK);
    }

    @GetMapping("/permission/check")
    public ResponseEntity checkPermission(@RequestParam String username, @RequestParam String pathName, @RequestParam String action) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkExistPermission(username, pathName, action);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException(" Kh??ng c?? quy???n truy c???p");
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "C?? quy???n truy c???p"), HttpStatus.OK);
    }

    @GetMapping("/permission/check-root")
    public ResponseEntity checkAccountIsRoot(@RequestParam String username) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkUsernameIsRoot(username);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException(" Kh??ng ph???i root");
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "L?? root"), HttpStatus.OK);
    }

    @GetMapping("/permission/add-check")
    public ResponseEntity checkAddPermission(@RequestParam String username) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkAddPermission(username);
        if (!optionalUserPermission.isPresent()) {
            throw new ErrorException(" Kh??ng c?? quy???n truy c???p");
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "C?? quy???n truy c???p"), HttpStatus.OK);
    }

    @GetMapping("/permission/get-notification")
    public ResponseEntity checkPermissionGetNotification(@RequestParam String username) {
        Optional<UserPermission> optionalUserPermission = userPermissionRepository.checkPermissionGetNotification(username);
        if (!optionalUserPermission.isPresent()) {
            //            throw new ErrorException(" Kh??ng c?? quy???n nh???n th??ng b??o");
            return new ResponseEntity(new ResponseData(StringConstant.iERROR_EXCEPTION, "Kh??ng c?? quy???n nh???n th??ng b??o"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "C?? quy???n nh???n th??ng b??o"), HttpStatus.OK);
    }

    // get all
    @GetMapping("/permission")
    public ResponseEntity getAll() {
        List<FunctionSystem> functionSystemList = functionSystemRepository.findAll();
        List<ObjectTreePer> objectTreePerList = functionSystemList
            .stream()
            .map(o -> new ObjectTreePer(o.getId(), o.getName()))
            .collect(Collectors.toList());

        //        List<ActionSystem> actionSystemList = actionSystemRepository.findAll();
        List<ActionSystem> actionSystemList = actionSystemRepository.getAllIgnoreCaseActionIsParent();
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

        //        System.out.println("objectTreePerList: " + objectTreePerList);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, objectTreePerList), HttpStatus.OK);
    }

    @GetMapping("/permission/checknew/{username}")
    public ResponseEntity checkUserIsNew(@PathVariable("username") String username) {
        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            return new ResponseEntity(new ResponseData(StringConstant.iERROR_EXCEPTION, "false"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "true"), HttpStatus.OK);
    }

    @GetMapping("/permission/function/{username}")
    public ResponseEntity getAllFunctionNameByUsername(@PathVariable("username") String username) {
        List<String> listReturn = functionSystemRepository.getAllFunctionNameByUsername(username);

        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y PermissionCurrent v???i username: = " + username);
        }

        PermissionCurrent permissionCurrentRest = optionalPermissionCurrent.get();

        JSONObject jsonObject = new JSONObject(permissionCurrentRest.getCurrentPer().toString());

        //        System.out.println("permissionCurrentRest.getCurrentPer(): " + permissionCurrentRest.getCurrentPer());
        List<Integer> functIds = new ArrayList<>();
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            String currentFunctKey = (String) keys.next();
            if (NumberUtils.isCreatable(currentFunctKey)) {
                functIds.add(Integer.parseInt(currentFunctKey));
            }
        }

        List<FunctionSystem> listResult = functionSystemRepository.findByIdIn(functIds);
        //        System.out.println("listResult: " + listResult);

        List<String> listStringName = listResult.stream().flatMap(o -> Stream.of(o.getName())).collect(Collectors.toList());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listStringName), HttpStatus.OK);
    }

    @GetMapping("/permission/{username}")
    public ResponseEntity getAllPermissionCurrentByUsername(@PathVariable("username") String username) {
        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y PermissionCurrent v???i username: = " + username);
        }
        PermissionCurrent permissionCurrentRest = optionalPermissionCurrent.get();
        //        System.out.println("permissionCurrentRest.getCurrentPer(): " + permissionCurrentRest.getCurrentPer());
        JSONObject jsonObject = new JSONObject(permissionCurrentRest.getCurrentPer());
        //        System.out.println("json" + jsonObject);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObject.toString()), HttpStatus.OK);
    }

    @PostMapping("/permission")
    @Transactional
    public ResponseEntity create(@RequestBody CreatePermissionRequestModel createPermissionRequestModel) throws Exception {
        //        System.out.println("currentPermission: " + createPermissionRequestModel.getCurrentPermission());

        if (createPermissionRequestModel.getActionIds().isEmpty()) {
            throw new ErrorException("Ph???i ch???n ch???c n??ng cho user!!");
        }

        List<UserPermission> listParamAdd = new ArrayList<>();
        List<UserPermission> listParamEdit = new ArrayList<>();
        String username = createPermissionRequestModel.getUsername();

        //x??a ??i c??c UserPermission hi???n t???i c???a username
        userPermissionRepository.deleteByUsername(username);

        for (String x : createPermissionRequestModel.getActionIds()) {
            //            System.out.println("createPermissionRequestModel.getActionIds(): " + x);

            if (username != "hungdx" && (x.equals("1") || x.equals("a1"))) {
                throw new ErrorException("Ch???c n??ng  Ng?????i D??ng ch??? c?? ng?????i qu???n tr??? cao nh???t ???????c ph??p!");
            }

            if (
                username != "hungdx" &&
                (x.equals("1") || x.equals("a1") || x.equals("2") || x.equals("a2") || x.equals("a3") || x.equals("a12"))
            ) {
                throw new ErrorException("Ch???c n??ng Vai tr?? ch??? c?? ng?????i qu???n tr??? cao nh???t ???????c ph??p!");
            }

            //            if (!MethodCommon.isNumeric(x)) { // check xem c?? ph???i l?? s??? hay l?? ch???
            //                UserPermission userPermissionParam = new UserPermission();
            //                userPermissionParam.setUsername(username);
            //                userPermissionParam.setActionId(x);
            //
            //                userPermissionParam.setCreatedBy("api");
            //                userPermissionParam.setCreatedDate(LocalDateTime.now());
            //                userPermissionParam.setLastModifiedDate(LocalDateTime.now());
            //
            //                listParamAdd.add(userPermissionParam);
            //            }
            UserPermission userPermissionParam = new UserPermission();
            userPermissionParam.setUsername(username);
            userPermissionParam.setActionId(x);

            userPermissionParam.setCreatedBy("api");
            userPermissionParam.setCreatedDate(LocalDateTime.now());
            userPermissionParam.setLastModifiedDate(LocalDateTime.now());

            listParamAdd.add(userPermissionParam);
        }

        //Th??m ph???n t??? m???i
        List<UserPermission> listReturn = userPermissionRepository.saveAll(listParamAdd); // l??u v??o b???ng user_permission

        //Lo???i b??? ph???n t???

        if (listReturn.size() > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = createPermissionRequestModel.getCurrentPermission();
            String jsonObjectMapper = objectMapper.writeValueAsString(map);

            //            System.out.println("jsonObjectMapper: " + jsonObjectMapper);

            PermissionCurrent permissionCurrentParam = null;
            Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
            if (optionalPermissionCurrent.isPresent()) { //TH ???? c?? quy???n  r???i th?? l?? EDIT
                permissionCurrentParam = optionalPermissionCurrent.get();

                permissionCurrentParam.setUsername(username);
                permissionCurrentParam.setCurrentPer(jsonObjectMapper);
                permissionCurrentParam.setLastModifiedDate(LocalDateTime.now());
            } else { //TH ch??a c?? quy???n  r???i th?? l?? ADD
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
