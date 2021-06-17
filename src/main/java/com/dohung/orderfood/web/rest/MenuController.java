package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.domain.Menu;
import com.dohung.orderfood.repository.MenuRepository;
import com.dohung.orderfood.web.rest.response.ObjectMenuParentDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

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
