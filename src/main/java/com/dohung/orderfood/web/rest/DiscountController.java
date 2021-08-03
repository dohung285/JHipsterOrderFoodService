package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Comment;
import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.DiscountRepository;
import com.dohung.orderfood.web.rest.request.DiscountRequestModel;
import com.dohung.orderfood.web.rest.response.DiscountObjectResponseDto;
import com.dohung.orderfood.web.rest.response.DiscountResponseDto;
import java.text.SimpleDateFormat;
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
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @GetMapping("/discounts")
    public ResponseEntity getAllDiscounts() {
        List<Discount> listReturn = discountRepository.findAll();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    // get all
    @GetMapping("/discountId-foods")
    public ResponseEntity getAllDiscountsForFood() {
        List<DiscountObjectResponseDto> listReturn = new ArrayList<>();
        List<Discount> listResult = discountRepository.findAll();
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

        Discount discountParam = new Discount();

        discountParam.setName(discountRequestModel.getName());
        discountParam.setPercent(discountRequestModel.getPercent());
        discountParam.setStartDate(discountRequestModel.getStartDate());
        discountParam.setEndDate(discountRequestModel.getEndDate());
        discountParam.setCreatedBy("api");
        discountParam.setCreatedDate(LocalDateTime.now());

        Discount discountRest = discountRepository.save(discountParam);

        BeanUtils.copyProperties(discountRest, discountReturn);

        //        List<Discount> checkExist = discountRepository.findAllByStartDateIsBeforeAndEndDateIsBefore(
        //            discountRequestModel.getStartDate(),
        //            discountRequestModel.getEndDate()
        //        );
        //        if (checkExist.size() <= 0) {
        //
        //        } else {
        //            throw new ErrorException(
        //                "Đã tồn tại đợt giảm giá  trong khoảng thời gian startDate: =" +
        //                discountRequestModel.getStartDate() +
        //                " và " +
        //                discountRequestModel.getEndDate()
        //            );
        //        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, discountReturn), HttpStatus.OK);
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
