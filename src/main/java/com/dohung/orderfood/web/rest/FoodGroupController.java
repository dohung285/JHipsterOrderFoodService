package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.domain.FoodGroup;
import com.dohung.orderfood.domain.Menu;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.FoodGroupRepository;
import com.dohung.orderfood.repository.FoodRepository;
import com.dohung.orderfood.repository.MenuRepository;
import com.dohung.orderfood.web.rest.request.FoodGroupRequestModel;
import com.dohung.orderfood.web.rest.response.FoodGroupResponseDto;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FoodGroupController {

    @Autowired
    private FoodGroupRepository foodGroupRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MenuRepository menuRepository;

    // get all
    @GetMapping("/foodGroup")
    public ResponseEntity getAllFoodGroup() {
        List<FoodGroup> listReturn = foodGroupRepository.findAll();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    //save
    @PostMapping("/foodGroup")
    @Transactional
    public ResponseEntity saveFoodGroup(@RequestBody FoodGroupRequestModel foodGroupRequestModel) {
        FoodGroupResponseDto foodGroupReturn = new FoodGroupResponseDto();

        if (foodGroupRequestModel.getName() == null || foodGroupRequestModel.getName().equals("")) {
            throw new ErrorException("T??n m??n ??n kh??ng ???????c b??? tr???ng");
        }

        List<FoodGroup> checkExist = foodGroupRepository.findAllByName(foodGroupRequestModel.getName());
        if (checkExist.size() <= 0) {
            FoodGroup foodGroupParam = new FoodGroup();

            foodGroupParam.setName(foodGroupRequestModel.getName());
            foodGroupParam.setCreatedBy("api");
            foodGroupParam.setCreatedDate(LocalDateTime.now());
            foodGroupParam.setLastModifiedDate(LocalDateTime.now());

            FoodGroup foodGroupRest = foodGroupRepository.save(foodGroupParam);

            //l???y ra id v???a th??m
            Integer foodId = foodGroupRest.getId();

            Menu menuParam = new Menu();
            menuParam.setRoleName("admin");
            menuParam.setName(foodGroupRest.getName());
            menuParam.setLevel(1);
            menuParam.setParentId(3); // trong db la 3

            menuParam.setLink("/catalog/" + foodId);

            menuParam.setCreatedBy("api");
            menuParam.setCreatedDate(LocalDateTime.now());
            menuParam.setLastModifiedDate(LocalDateTime.now());

            try {
                menuRepository.save(menuParam);
            } catch (Exception e) {
                throw new ErrorException("L???i: " + e.getMessage());
            }
            BeanUtils.copyProperties(foodGroupRest, foodGroupReturn);
        } else {
            throw new ErrorException("???? t???n t???i FoodGroup v???i name: =" + foodGroupRequestModel.getName());
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodGroupReturn), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/foodGroup/{foodGroupId}")
    @Transactional
    public ResponseEntity deleteFoodGroup(@PathVariable("foodGroupId") Integer foodGroupId) {
        Optional<FoodGroup> optionalFoodGroup = foodGroupRepository.findById(foodGroupId);
        if (!optionalFoodGroup.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y FoodGroup v???i foodGroupId:= " + foodGroupId);
        }
        String foodGroupName = optionalFoodGroup.get().getName();
        foodGroupRepository.delete(optionalFoodGroup.get());

        List<Food> listFoodDeleted = foodRepository.findAllByGroupId(foodGroupId);
        if (listFoodDeleted.size() > 0) {
            System.out.println("listFoodDeleted: " + listFoodDeleted);
            foodRepository.deleteAll(listFoodDeleted);
        }
        //x??a menu
        Optional<Menu> optionalMenu = menuRepository.findAllByName(foodGroupName);
        if (!optionalMenu.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y Menu v???i name:= " + foodGroupName);
        }
        //delete
        menuRepository.delete(optionalMenu.get());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
