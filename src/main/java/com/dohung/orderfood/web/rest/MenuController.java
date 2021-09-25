package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.FunctionSystem;
import com.dohung.orderfood.domain.Menu;
import com.dohung.orderfood.domain.PermissionCurrent;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.*;
import com.dohung.orderfood.web.rest.response.AddMenuMonAnResponse;
import com.dohung.orderfood.web.rest.response.ObjectMenuParentDto;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.persistence.Tuple;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FunctionSystemRepository functionSystemRepository;

    @Autowired
    private PermissionCurrentRepository permissionCurrentRepository;

    @GetMapping("/menu/item-monan")
    public ResponseEntity getMenuItemMonAnAndSubMonAn() {
        List<Tuple> list = menuRepository.getAllMenuItemMonAn();
        System.out.println("list: " + list);

        List<AddMenuMonAnResponse> listMenuMonAn = list
            .stream()
            .map(
                x ->
                    new AddMenuMonAnResponse(
                        x.get(0, String.class),
                        x.get(1, String.class),
                        x.get(2, Integer.class),
                        x.get(3, String.class)
                    )
            )
            .collect(Collectors.toList());

        System.out.println("listMenuMonAn: " + listMenuMonAn);

        //lấy ra item parrent

        //Láy ra menu Món ăn trong listMenuMonAn
        int indexMonAn = IntStream
            .range(0, listMenuMonAn.size())
            .filter(i -> listMenuMonAn.get(i).getFoodGroupId() == null)
            .findFirst()
            .orElse(-1);

        if (indexMonAn < 0) {
            throw new ErrorException("Lỗi - Không tìm thấy item MonAn trong listMenuMonAn");
        }
        AddMenuMonAnResponse itemMonAn = listMenuMonAn.get(indexMonAn);

        //lấy ra danh sách item child
        List<AddMenuMonAnResponse> listMenuChildOfLevel0 = listMenuMonAn
            .stream()
            .filter(x -> x.getFoodGroupId() != null)
            .collect(Collectors.toList());

        System.out.println("listMenuChildOfLevel0: " + listMenuChildOfLevel0);

        JSONObject jsonObjectItemMenu = new JSONObject();
        JSONArray arrayItems = new JSONArray();

        jsonObjectItemMenu.put("label", itemMonAn.getName());
        jsonObjectItemMenu.put("icon", itemMonAn.getIcon());

        for (AddMenuMonAnResponse item : listMenuChildOfLevel0) {
            JSONObject objectInItems = new JSONObject();

            objectInItems.appendField("label", item.getName());
            objectInItems.appendField("icon", item.getIcon());
            objectInItems.appendField("command", item.getLink());

            arrayItems.appendElement(objectInItems);
        }

        jsonObjectItemMenu.put("items", arrayItems);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObjectItemMenu), HttpStatus.OK);
    }

    @GetMapping("/menu/permit-function/{username}")
    public ResponseEntity getFunctionalInItemSystemForUser(@PathVariable("username") String username) { //@RequestParam(value = "params") List<String> params
        List<Menu> list = menuRepository.getAllMenuNotRole();

        //        System.out.println("list: " + list.toString());

        //lấy ra danh sách menu ở cấp 0 -- và tên là " Hệ Thống "
        List<Menu> listMenuLevel0 = list.stream().filter(x -> x.getLevel() == 0).collect(Collectors.toList());

        //Láy ra menu hệ thống
        int indexHeThong = IntStream.range(0, listMenuLevel0.size()).filter(i -> listMenuLevel0.get(i).getId() == 1).findFirst().orElse(-1);
        //        System.out.println("index of He thong: " + indexHeThong);
        Menu menuHeThong = listMenuLevel0.get(indexHeThong);

        //        System.out.println("Menu Hệ thống: " + menuHeThong);

        //Lọc ra các menu con của theo id parent của menu cấp 0 của menu hệ thống ( menuHeThong id = 1 = parentId )
        List<Menu> listMenuChildOfLevel0OfMenuHeThong = list.stream().filter(x -> x.getParentId() == 1).collect(Collectors.toList()); // do menu hệ thống có id = 1
        //        System.out.println("listMenuChildOfLevel0OfMenuHeThong: " + listMenuChildOfLevel0OfMenuHeThong);

        Optional<PermissionCurrent> optionalPermissionCurrent = permissionCurrentRepository.findAllByUsername(username);
        if (!optionalPermissionCurrent.isPresent()) {
            //            throw new ErrorException("Không tìm thấy PermissionCurrent với username: = " + username);
            return new ResponseEntity(
                new ResponseData(
                    StringConstant.iERROR_EXCEPTION,
                    "Không tìm thấy PermissionCurrent với username: " + username + " == Là user mới chưa được cấp quyền"
                ),
                HttpStatus.OK
            );
        }

        PermissionCurrent permissionCurrentRest = optionalPermissionCurrent.get();

        org.json.JSONObject jsonObject = new org.json.JSONObject(permissionCurrentRest.getCurrentPer().toString());

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

        //        System.out.println("listStringName: " + listStringName);

        List<Menu> listPermissionOfUser = new ArrayList<>();
        //        System.out.println("****************************************");
        for (String item : listStringName) {
            int index = IntStream
                .range(0, listMenuChildOfLevel0OfMenuHeThong.size())
                .filter(i -> listMenuChildOfLevel0OfMenuHeThong.get(i).getName().equals(item.toString()))
                .findFirst()
                .orElse(-1);
            //            System.out.println("item: "+item);
            if (index >= 0) {
                Menu menu = listMenuChildOfLevel0OfMenuHeThong.get(index);
                listPermissionOfUser.add(menu);
                //                System.out.println("Menu: "+menu);
            }
        }

        JSONObject jsonObjectItemMenu = new JSONObject();
        JSONArray arrayItems = new JSONArray();

        jsonObjectItemMenu.put("label", menuHeThong.getName());
        jsonObjectItemMenu.put("icon", menuHeThong.getIcon());

        for (Menu itemMenuChildOfLevel0 : listPermissionOfUser) {
            JSONObject objectInItems = new JSONObject();
            //                    String s = "() => history.push( \" " + itemMenuChildOfLevel0.getLink() + " \"  )";
            objectInItems.appendField("label", itemMenuChildOfLevel0.getName());
            objectInItems.appendField("icon", itemMenuChildOfLevel0.getIcon());
            //                objectInItems.appendField("command", s);
            objectInItems.appendField("command", itemMenuChildOfLevel0.getLink());
            arrayItems.appendElement(objectInItems);
        }

        jsonObjectItemMenu.put("items", arrayItems);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, jsonObjectItemMenu), HttpStatus.OK);
    }

    @GetMapping("/menu/byWithRole")
    public ResponseEntity getAllMenuWithRoleName() { //@RequestParam(value = "params") List<String> params
        JSONArray arrayReturn = new JSONArray();
        List<ObjectMenuParentDto> listReturn = new ArrayList<>();

        //        List<Menu> list = menuRepository.getAllMenuNotRole(); // tất cả
        //        List<Menu> list = menuRepository.getAllMenuWithoutItemMonAnAndSubMonAn(); // loại bỏ item Món ăn

        List<Menu> list = menuRepository.getAllMenuWithoutItemHeThongAndSubHeThong(); // loại bỏ item Hệ thống

        System.out.println("list: " + list.toString());

        //lấy ra danh sách menu ở cấp 0
        List<Menu> listMenuLevel0 = list.stream().filter(x -> x.getLevel() == 0).collect(Collectors.toList());

        //Lọc ra các menu con của theo id parent của menu cấp 0
        List<Menu> listMenuChildOfLevel0 = null;
        ObjectMenuParentDto objectMenuParentDto = null;
        for (Menu item : listMenuLevel0) {
            System.out.println("item: " + item.toString());
            listMenuChildOfLevel0 = list.stream().filter(x -> x.getParentId() == item.getId()).collect(Collectors.toList());

            System.out.println("listMenuChildOfLevel0: " + listMenuChildOfLevel0);
            System.out.println("===================================");

            JSONObject jsonObject = new JSONObject();
            JSONArray arrayItems = new JSONArray();

            jsonObject.put("label", item.getName());
            jsonObject.put("icon", item.getIcon());

            Integer[] intArray = new Integer[] { 2, 4, 5 }; //id của các menu ko có submenu
            List<Integer> intList = new ArrayList<>(Arrays.asList(intArray));
            if (!intList.contains(item.getId())) {
                for (Menu itemMenuChildOfLevel0 : listMenuChildOfLevel0) {
                    JSONObject objectInItems = new JSONObject();
                    //                    String s = "() => history.push( \" " + itemMenuChildOfLevel0.getLink() + " \"  )";
                    objectInItems.appendField("label", itemMenuChildOfLevel0.getName());
                    objectInItems.appendField("icon", itemMenuChildOfLevel0.getIcon());
                    //                objectInItems.appendField("command", s);
                    objectInItems.appendField("command", itemMenuChildOfLevel0.getLink());
                    arrayItems.appendElement(objectInItems);
                }
                jsonObject.put("items", arrayItems);
            } else {
                if (item.getId() == 2) { // trang chủ
                    jsonObject.appendField("command", item.getLink());
                }
                if (item.getId() == 4) { // giới thiệu
                    jsonObject.appendField("command", item.getLink());
                }
                if (item.getId() == 5) { //liên hệ
                    jsonObject.appendField("command", item.getLink());
                }
            }
            arrayReturn.appendElement(jsonObject);
        }

        return new ResponseEntity(arrayReturn, HttpStatus.OK);
    }

    @GetMapping("/menu/byNotRole")
    public ResponseEntity getAllMenuNotRoleName() { //@RequestParam(value = "params") List<String> params
        JSONArray arrayReturn = new JSONArray();
        List<ObjectMenuParentDto> listReturn = new ArrayList<>();

        List<Menu> list = menuRepository.getAllMenuNotRole();

        System.out.println("list: " + list.toString());

        //lấy ra danh sách menu ở cấp 0
        List<Menu> listMenuLevel0 = list.stream().filter(x -> x.getLevel() == 0).collect(Collectors.toList());

        //Lọc ra các menu con của theo id parent của menu cấp 0
        List<Menu> listMenuChildOfLevel0 = null;
        ObjectMenuParentDto objectMenuParentDto = null;
        for (Menu item : listMenuLevel0) {
            if (item.getId() == 1) {
                continue;
            }
            System.out.println("item: " + item.toString());
            listMenuChildOfLevel0 = list.stream().filter(x -> x.getParentId() == item.getId()).collect(Collectors.toList());

            System.out.println("listMenuChildOfLevel0: " + listMenuChildOfLevel0);
            System.out.println("===================================");

            JSONObject jsonObject = new JSONObject();
            JSONArray arrayItems = new JSONArray();

            jsonObject.put("label", item.getName());
            jsonObject.put("icon", item.getIcon());

            Integer[] intArray = new Integer[] { 2, 4, 5 }; //id của các menu ko có submenu
            List<Integer> intList = new ArrayList<>(Arrays.asList(intArray));
            if (!intList.contains(item.getId())) {
                for (Menu itemMenuChildOfLevel0 : listMenuChildOfLevel0) {
                    JSONObject objectInItems = new JSONObject();
                    //                    String s = "() => history.push( \" " + itemMenuChildOfLevel0.getLink() + " \"  )";
                    objectInItems.appendField("label", itemMenuChildOfLevel0.getName());
                    objectInItems.appendField("icon", itemMenuChildOfLevel0.getIcon());
                    //                objectInItems.appendField("command", s);
                    objectInItems.appendField("command", itemMenuChildOfLevel0.getLink());
                    arrayItems.appendElement(objectInItems);
                }
                jsonObject.put("items", arrayItems);
            } else {
                if (item.getId() == 2) { // trang chủ
                    jsonObject.appendField("command", item.getLink());
                }
                if (item.getId() == 4) { // trang chủ
                    jsonObject.appendField("command", item.getLink());
                }
                if (item.getId() == 5) { // trang chủ
                    jsonObject.appendField("command", item.getLink());
                }
            }
            arrayReturn.appendElement(jsonObject);
        }

        return new ResponseEntity(arrayReturn, HttpStatus.OK);
    }

    @GetMapping("/menu/public/byRoleName")
    public ResponseEntity getAllMenuPublicApi() { //@RequestParam(value = "params") List<String> params
        RestTemplate restTemplate = new RestTemplate();
        // call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String getObjectURL = StringConstant.PUBLIC_URL_API + "/api/menu/byNotRole";

        ResponseEntity<String> answer = restTemplate.exchange(getObjectURL, HttpMethod.GET, entity, String.class);

        //        System.out.println("answer: " + answer.toString());
        //        org.json.JSONObject jsonObject = new org.json.JSONObject(answer);
        //        System.out.println(jsonObject.get("body"));
        //        org.json.JSONArray jsonArray = new org.json.JSONArray(jsonObject.get("body").toString());
        //        for (int i = 0; i < jsonArray.length(); i++) {
        //
        //            System.out.println("item: " + jsonArray.get(i));
        //
        //            org.json.JSONObject jsonObjectItemChild = (org.json.JSONObject) jsonArray.get(i);
        //            System.out.println("jsonObjectItemChild - 50: "+jsonObjectItemChild);
        //            ObjectMenuChildDto objectMenuChildDto = new ObjectMenuChildDto();
        //            String icon = jsonObjectItemChild.optString("icon",null);
        //            String label = jsonObjectItemChild.getString("label");
        //            String command = null;
        //
        //            String[] strArray = new String[]{"Trang chủ", "Giới thiệu", "Liên hệ"}; //id của các menu ko có submenu
        //            List<String> strList = new ArrayList<>(Arrays.asList(strArray));
        //
        //
        //            if (strList.contains(label)) {
        //                command = jsonObjectItemChild.getString("command");
        //                objectMenuChildDto.setIcon(icon);
        //                objectMenuChildDto.setLabel(label);
        //                objectMenuChildDto.setCommand(command);
        //
        //                System.out.println("objectMenuChildDto: "+objectMenuChildDto);
        //            }else {
        //                org.json.JSONArray jsonArrayChildItems = jsonObjectItemChild.getJSONArray("items");
        //                for (int j = 0; j < jsonArrayChildItems.length(); j++) {
        //                    System.out.println("jsonArrayChildItems: "+jsonArrayChildItems);
        //                }
        //            }
        //
        //
        //        }
        org.json.JSONObject jsonObjectReturn = new org.json.JSONObject(answer);

        return new ResponseEntity(jsonObjectReturn.get("body").toString(), HttpStatus.OK);
    }

    @GetMapping("/menu/byRoleName")
    public ResponseEntity getAllMenuByRoleName(@RequestParam(value = "params") List<String> params) { //@RequestParam(value = "params") List<String> params
        JSONArray arrayReturn = new JSONArray();
        List<ObjectMenuParentDto> listReturn = new ArrayList<>();

        List<Menu> list = menuRepository.getAllMenuByRole(params);

        System.out.println("list: " + list.toString());

        //lấy ra danh sách menu ở cấp 0
        List<Menu> listMenuLevel0 = list.stream().filter(x -> x.getLevel() == 0).collect(Collectors.toList());

        //Lọc ra các menu con của theo id parent của menu cấp 0
        List<Menu> listMenuChildOfLevel0 = null;
        ObjectMenuParentDto objectMenuParentDto = null;
        for (Menu item : listMenuLevel0) {
            System.out.println("item: " + item.toString());
            listMenuChildOfLevel0 = list.stream().filter(x -> x.getParentId() == item.getId()).collect(Collectors.toList());

            System.out.println("listMenuChildOfLevel0: " + listMenuChildOfLevel0);
            System.out.println("===================================");

            JSONObject jsonObject = new JSONObject();
            JSONArray arrayItems = new JSONArray();

            jsonObject.put("label", item.getName());
            jsonObject.put("icon", item.getIcon());

            for (Menu itemMenuChildOfLevel0 : listMenuChildOfLevel0) {
                JSONObject objectInItems = new JSONObject();
                String s = "() => history.push( \" " + itemMenuChildOfLevel0.getLink() + " \"  )";
                objectInItems.appendField("label", itemMenuChildOfLevel0.getName());
                objectInItems.appendField("icon", itemMenuChildOfLevel0.getIcon());
                //                objectInItems.appendField("command", s);
                objectInItems.appendField("command", itemMenuChildOfLevel0.getLink());

                arrayItems.appendElement(objectInItems);
            }
            jsonObject.put("items", arrayItems);
            arrayReturn.appendElement(jsonObject);
        }

        return new ResponseEntity(arrayReturn, HttpStatus.OK);
    }
}
