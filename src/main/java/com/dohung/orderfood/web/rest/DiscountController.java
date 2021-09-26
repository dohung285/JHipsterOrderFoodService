package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.DiscountRepository;
import com.dohung.orderfood.repository.FoodRepository;
import com.dohung.orderfood.web.rest.request.DiscountRequestModel;
import com.dohung.orderfood.web.rest.request.UpdateDiscountRequestModel;
import com.dohung.orderfood.web.rest.response.DiscountObjectResponseDto;
import com.dohung.orderfood.web.rest.response.DiscountResponseDto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private FoodRepository foodRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @GetMapping("/discounts/{discountId}")
    public ResponseEntity getAllDiscountByDiscountId(@PathVariable("discountId") Integer discountId) {
        Optional<Discount> optionalDiscount = discountRepository.findByIdAndIsDeletedEquals(discountId, 0);
        if (!optionalDiscount.isPresent()) {
            throw new ErrorException("Không tìm thấy Discount với id: " + discountId);
        }
        Discount discountRest = optionalDiscount.get();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, discountRest), HttpStatus.OK);
    }

    @GetMapping("/discounts")
    public ResponseEntity getAllDiscounts() {
        //        Date currentDate = new Date();
        //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //        String strDate = sdf.format(currentDate);
        //        System.out.println("strDate: " + strDate);

        List<Discount> listReturn = discountRepository.findAll();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    // get all
    @GetMapping("/discountId-foods")
    public ResponseEntity getAllDiscountsForFood() throws ParseException {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(currentDate);
        System.out.println("strDate: " + strDate);

        List<DiscountObjectResponseDto> listReturn = new ArrayList<>();
        List<Discount> listResult = discountRepository.findByIsDeletedEqualsAndEndDateGreaterThanEqual(
            0,
            new SimpleDateFormat("yyyy-MM-dd").parse(strDate)
        );
        for (Discount x : listResult) {
            Integer id = x.getId();
            String nameAndPercent = x.getName() + "-" + x.getPercent() + "%";
            DiscountObjectResponseDto discountObjectResponseDto = new DiscountObjectResponseDto();
            discountObjectResponseDto.setCode(id);
            discountObjectResponseDto.setName(nameAndPercent);

            listReturn.add(discountObjectResponseDto);
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    //save
    @PostMapping("/discount")
    public ResponseEntity saveDiscount(@RequestBody DiscountRequestModel discountRequestModel) {
        DiscountResponseDto discountReturn = new DiscountResponseDto();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<Discount> checkExist = discountRepository.findByStartDateEqualsAndEndDateEqualsAndIsDeletedEqualsAndNameEquals(
            discountRequestModel.getStartDate(),
            discountRequestModel.getEndDate(),
            0,
            discountRequestModel.getName()
        );
        if (checkExist.size() > 0) {
            throw new ErrorException(
                "Đã tồn tại đợt giảm giá  trong khoảng thời gian startDate: =" +
                sdf.format(discountRequestModel.getStartDate()) +
                " và " +
                sdf.format(discountRequestModel.getEndDate()) +
                " tên: " +
                discountRequestModel.getName()
            );
        }

        Discount discountParam = new Discount();

        discountParam.setName(discountRequestModel.getName());
        discountParam.setPercent(discountRequestModel.getPercent());
        discountParam.setStartDate(discountRequestModel.getStartDate());
        discountParam.setEndDate(discountRequestModel.getEndDate());
        discountParam.setIsDeleted(0);
        discountParam.setCreatedBy("api");
        discountParam.setCreatedDate(LocalDateTime.now());

        Discount discountRest = discountRepository.save(discountParam);

        BeanUtils.copyProperties(discountRest, discountReturn);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, discountReturn), HttpStatus.OK);
    }

    //(delete ) update is_deleted
    @PutMapping("/discount/{discountId}")
    @Transactional
    public ResponseEntity updateIsDeletedDiscount(@PathVariable("discountId") Integer discountId) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);
        if (!optionalDiscount.isPresent()) {
            throw new ErrorException("Không tìm thấy Discount với discountId:= " + discountId);
        }
        Discount discountRest = optionalDiscount.get();
        discountRest.setIsDeleted(1);

        discountRepository.save(discountRest);

        //update discountId ở trong bảng Food

        List<Food> foodRests = foodRepository.findAllByDiscountId(discountId);

        //Lấy ra listFoodId
        List<Integer> listFoodId = foodRests.stream().map(Food::getId).collect(Collectors.toList());

        // update foods
        foodRepository.updateDiscountIdOfFood(listFoodId);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }

    //(delete ) update name and percent
    @PutMapping("/discount/percentAndName/{discountId}")
    @Transactional
    public ResponseEntity updatePercentAndName(
        @PathVariable("discountId") Integer discountId,
        @RequestBody UpdateDiscountRequestModel updateDiscountRequestModel
    ) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);
        if (!optionalDiscount.isPresent()) {
            throw new ErrorException("Không tìm thấy Discount với discountId:= " + discountId);
        }
        Discount discountRest = optionalDiscount.get();
        discountRest.setPercent(updateDiscountRequestModel.getPercent());
        discountRest.setName(updateDiscountRequestModel.getName());

        discountRepository.save(discountRest);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/discount/{discountId}")
    public ResponseEntity deleteDiscount(@PathVariable("discountId") Integer discountId) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);
        if (!optionalDiscount.isPresent()) {
            throw new ErrorException("Không tìm thấy Discount với discountId:= " + discountId);
        }
        discountRepository.delete(optionalDiscount.get());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
