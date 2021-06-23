package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Bill;
import com.dohung.orderfood.domain.Card;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.BillRepository;
import com.dohung.orderfood.repository.CardRepository;
import com.dohung.orderfood.web.rest.request.BillRequestModel;
import com.dohung.orderfood.web.rest.request.CardRequestModel;
import com.dohung.orderfood.web.rest.response.BillResponseDto;
import com.dohung.orderfood.web.rest.response.CardResponseDto;
import java.time.LocalDateTime;
import java.util.*;
import net.bytebuddy.asm.Advice;
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
public class BillController {

    @Autowired
    private BillRepository billRepository;

    // get all
    @GetMapping("/bill")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<Bill> listReturn = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<Bill> billPage = billRepository.findAll(paging);
        listReturn = billPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", billPage.getNumber());
        response.put("totalItems", billPage.getTotalElements());
        response.put("totalPages", billPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //save
    @PostMapping("/bill")
    public ResponseEntity save(@RequestBody BillRequestModel billRequestModel) {
        BillResponseDto billReturn = new BillResponseDto();

        Optional<Bill> optional = billRepository.findAllByOrderId(billRequestModel.getOrderId());
        if (optional.isPresent()) {
            throw new ErrorException("Lỗi, đã tồn tại Bill với orderId:= " + billRequestModel.getOrderId());
        }
        Bill billParam = new Bill();

        billParam.setOrderId(billRequestModel.getOrderId());
        billParam.setUsername(billRequestModel.getUsername());
        billParam.setTotalMoney(billRequestModel.getTotalMoney());

        billParam.setCreatedBy("api");
        billParam.setCreatedDate(LocalDateTime.now());
        billParam.setLastModifiedDate(LocalDateTime.now());

        Bill billRest = billRepository.save(billParam);

        BeanUtils.copyProperties(billRest, billReturn);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, billReturn), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/bill/{billId}")
    public ResponseEntity delete(@PathVariable("billId") Integer billId) {
        Optional<Bill> optional = billRepository.findById(billId);
        if (!optional.isPresent()) {
            throw new ErrorException("Không tìm thấy Bill với billId:= " + billId);
        }
        billRepository.delete(optional.get());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
