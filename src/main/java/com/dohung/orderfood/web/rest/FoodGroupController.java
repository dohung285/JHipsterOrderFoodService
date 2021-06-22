package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.domain.FoodGroup;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.FoodGroupRepository;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FoodGroupController {

    @Autowired
    private FoodGroupRepository foodGroupRepository;

    // get all
    @GetMapping("/foodGroup")
    public ResponseEntity getAllFoodGroup(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<FoodGroup> listReturn = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<FoodGroup> foodGroupPage = foodGroupRepository.findAll(paging);
        listReturn = foodGroupPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", foodGroupPage.getNumber());
        response.put("totalItems", foodGroupPage.getTotalElements());
        response.put("totalPages", foodGroupPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //save
    @PostMapping("/foodGroup")
    public ResponseEntity saveFoodGroup(@RequestBody FoodGroupRequestModel foodGroupRequestModel) {
        FoodGroupResponseDto foodGroupReturn = new FoodGroupResponseDto();

        List<FoodGroup> checkExist = foodGroupRepository.findAllByName(foodGroupRequestModel.getName());
        if (checkExist.size() <= 0) {
            FoodGroup foodGroupParam = new FoodGroup();

            foodGroupParam.setName(foodGroupRequestModel.getName());
            foodGroupParam.setCreatedBy("api");
            foodGroupParam.setCreatedDate(LocalDateTime.now());

            FoodGroup foodGroupRest = foodGroupRepository.save(foodGroupParam);

            BeanUtils.copyProperties(foodGroupRest, foodGroupReturn);
        } else {
            throw new ErrorException("Đã tồn tại FoodGroup với name: =" + foodGroupRequestModel.getName());
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodGroupReturn), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/foodGroup/{foodGroupId}")
    public ResponseEntity deleteFoodGroup(@PathVariable("foodGroupId") Integer foodGroupId) {
        Optional<FoodGroup> optionalFoodGroup = foodGroupRepository.findById(foodGroupId);
        if (!optionalFoodGroup.isPresent()) {
            throw new ErrorException("Không tìm thấy FoodGroup với foodGroupId:= " + foodGroupId);
        }
        foodGroupRepository.delete(optionalFoodGroup.get());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
