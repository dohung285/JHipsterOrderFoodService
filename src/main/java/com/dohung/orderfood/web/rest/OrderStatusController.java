package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Order;
import com.dohung.orderfood.domain.OrderStatus;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.OrderRepository;
import com.dohung.orderfood.repository.OrderStatusRepository;
import com.dohung.orderfood.web.rest.request.OderStatusRequestModel;
import com.dohung.orderfood.web.rest.response.OrderStatusResponseDto;
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
public class OrderStatusController {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderRepository orderRepository;

    // get all
    @GetMapping("/orderStatus")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<OrderStatusResponseDto> listReturn = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<OrderStatus> orderPage = orderStatusRepository.findAll(paging);
        List<OrderStatus> listResult = orderPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", orderPage.getNumber());
        response.put("totalItems", orderPage.getTotalElements());
        response.put("totalPages", orderPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //save
    @PostMapping("/orderStatus")
    @Transactional
    public ResponseEntity save(@RequestBody OderStatusRequestModel orderStatusRequestModel) {
        OrderStatusResponseDto orderStatusReturn = new OrderStatusResponseDto();

        Optional<Order> orderOptional = orderRepository.findById(orderStatusRequestModel.getOrderId());
        if (!orderOptional.isPresent()) {
            throw new ErrorException("Không tìm thấy Order với id:= " + orderStatusRequestModel.getOrderId());
        }
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findByOrderId(orderStatusRequestModel.getOrderId());
        if (optionalOrderStatus.isPresent()) {
            throw new ErrorException("Đã tồn tại OrderStatus với id:= " + orderStatusRequestModel.getOrderId());
        }

        OrderStatus orderStatusParam = new OrderStatus();

        orderStatusParam.setOrderId(orderStatusRequestModel.getOrderId());
        orderStatusParam.setStatus(orderStatusRequestModel.getStatus());

        orderStatusParam.setCreatedBy("api");
        orderStatusParam.setCreatedDate(LocalDateTime.now());
        orderStatusParam.setLastModifiedDate(LocalDateTime.now());

        OrderStatus orderStatusRest = orderStatusRepository.save(orderStatusParam);
        BeanUtils.copyProperties(orderStatusRest, orderStatusReturn);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderStatusReturn), HttpStatus.OK);
    }

    //update status
    @PutMapping("/orderStatus/{id}")
    @Transactional
    public ResponseEntity updateStatus(@PathVariable("id") Integer id, @RequestBody OderStatusRequestModel orderStatusRequestModel) {
        OrderStatusResponseDto orderStatusReturn = new OrderStatusResponseDto();

        Optional<Order> orderOptional = orderRepository.findById(orderStatusRequestModel.getOrderId());
        if (!orderOptional.isPresent()) {
            throw new ErrorException("Không tìm thấy Order với id:= " + orderStatusRequestModel.getOrderId());
        }

        //tìm đối tượng cần update
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(id);
        if (!optionalOrderStatus.isPresent()) {
            throw new ErrorException("Không tìm thấy OrderStatus với id:= " + id);
        }

        OrderStatus orderStatusParam = optionalOrderStatus.get();

        orderStatusParam.setOrderId(orderStatusRequestModel.getOrderId());
        orderStatusParam.setStatus(orderStatusRequestModel.getStatus());

        orderStatusParam.setCreatedBy("api");
        orderStatusParam.setLastModifiedDate(LocalDateTime.now());

        OrderStatus orderStatusRest = orderStatusRepository.save(orderStatusParam);
        BeanUtils.copyProperties(orderStatusRest, orderStatusReturn);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderStatusReturn), HttpStatus.OK);
    }
}
