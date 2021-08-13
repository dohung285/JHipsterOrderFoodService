package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Menu;
import com.dohung.orderfood.repository.MenuRepository;
import com.dohung.orderfood.web.rest.response.ObjectMenuChildDto;
import com.dohung.orderfood.web.rest.response.ObjectMenuParentDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/menu/byWithRole")
    public ResponseEntity getAllMenuWithRoleName() { //@RequestParam(value = "params") List<String> params
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
