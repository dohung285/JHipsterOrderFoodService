package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.*;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.OrderDetailRepository;
import com.dohung.orderfood.repository.OrderRepository;
import com.dohung.orderfood.web.rest.request.FoodRequestModel;
import com.dohung.orderfood.web.rest.request.ObjectFoodDetail;
import com.dohung.orderfood.web.rest.request.OrderDetailRequestModel;
import com.dohung.orderfood.web.rest.request.OrderRequestModel;
import com.dohung.orderfood.web.rest.response.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // get all
    @GetMapping("/order")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<OrderResponseDto> listReturn = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<Order> orderPage = orderRepository.findAll(paging);
        List<Order> listResult = orderPage.getContent();

        List<Integer> orderIds = listResult.stream().map(Order::getId).collect(Collectors.toList());

        List<OrderDetail> orderDetails = orderDetailRepository.findAllByIdIn(orderIds);

        for (Order x : listResult) {
            OrderResponseDto orderResponseTarget = new OrderResponseDto();
            List<OrderDetailResponseDto> detailResponseDtoList = new ArrayList<>();

            Integer orderId = x.getId();
            BeanUtils.copyProperties(x, orderResponseTarget);

            List<OrderDetail> listOD = orderDetails.stream().filter(e -> e.getId().getFoodId() == orderId).collect(Collectors.toList());

            for (OrderDetail od : listOD) {
                OrderDetailResponseDto orderDetailResponseTarget = new OrderDetailResponseDto();
                BeanUtils.copyProperties(od, orderDetailResponseTarget);
                detailResponseDtoList.add(orderDetailResponseTarget);
            }

            orderResponseTarget.setOrderDetails(detailResponseDtoList);

            listReturn.add(orderResponseTarget);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", orderPage.getNumber());
        response.put("totalItems", orderPage.getTotalElements());
        response.put("totalPages", orderPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //save
    @PostMapping("/order")
    @Transactional
    public ResponseEntity save(@RequestBody OrderRequestModel orderRequestModel) {
        OrderResponseDto orderReturn = new OrderResponseDto();

        Order orderParam = new Order();

        orderParam.setAddress(orderRequestModel.getAddress());
        orderParam.setPhone(orderRequestModel.getPhone());
        orderParam.setUsername(orderRequestModel.getUsername());
        orderParam.setDateOrder(orderRequestModel.getDateOrder());

        orderParam.setCreatedBy("api");
        orderParam.setCreatedDate(LocalDateTime.now());
        orderParam.setLastModifiedDate(LocalDateTime.now());

        Order orderRest = orderRepository.save(orderParam);
        BeanUtils.copyProperties(orderRest, orderReturn);

        Integer orderId = orderRest.getId();

        List<ObjectOrderDetail> list = orderRequestModel.getOrderDetails();
        List<OrderDetailResponseDto> listOrderDetail = new ArrayList<>();
        for (ObjectOrderDetail item : list) {
            OrderDetailResponseDto orderDetailReturn = new OrderDetailResponseDto();

            OrderDetail orderDetailParam = new OrderDetail();

            orderDetailParam.setId(new OrderIdentity(orderId, item.getFoodId()));
            orderDetailParam.setAmount(item.getAmount());
            orderDetailParam.setMoney(item.getMoney());

            orderDetailParam.setCreatedBy("api");
            orderDetailParam.setCreatedDate(LocalDateTime.now());
            orderDetailParam.setLastModifiedDate(LocalDateTime.now());

            OrderDetail orderDetailRest = orderDetailRepository.save(orderDetailParam);
            BeanUtils.copyProperties(orderDetailRest, orderDetailReturn);

            listOrderDetail.add(orderDetailReturn);
        }
        orderReturn.setOrderDetails(listOrderDetail);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderReturn), HttpStatus.OK);
    }
}
