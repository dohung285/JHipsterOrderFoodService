package com.dohung.orderfood.web.rest;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.*;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.BillRepository;
import com.dohung.orderfood.repository.OrderDetailRepository;
import com.dohung.orderfood.repository.OrderRepository;
import com.dohung.orderfood.repository.OrderStatusRepository;
import com.dohung.orderfood.web.rest.request.OrderRequestModel;
import com.dohung.orderfood.web.rest.response.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private BillRepository billRepository;

    // get all
    @GetMapping("/order/allYear")
    public ResponseEntity getAllYearOrder() {
        List<ObjectDropdown> listReturn = new ArrayList<>();

        List<Order> listResult = orderRepository.findAll();

        List<ObjectDropdown> listReturnDate = listResult
            .stream()
            .map(x -> new ObjectDropdown(String.valueOf(x.getId()), String.valueOf(x.getDateOrder())))
            .collect(Collectors.toList());

        //        System.out.println("listReturnDate: " + listReturnDate);

        List<ObjectDropdownInteger> listDropdownInteger = new ArrayList<>();
        for (ObjectDropdown x : listReturnDate) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(x.getName()));
                Integer year = calendar.get(Calendar.YEAR);

                ObjectDropdownInteger objectDropdownInteger = new ObjectDropdownInteger(x.getCode(), year);
                listDropdownInteger.add(objectDropdownInteger);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<ObjectDropdownInteger> listReturnInteger = listDropdownInteger
            .stream()
            .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(ObjectDropdownInteger::getName))), ArrayList::new));

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturnInteger), HttpStatus.OK);
    }

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

            List<OrderDetail> listOD = orderDetails.stream().filter(e -> e.getId().getOrderId() == orderId).collect(Collectors.toList());

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

    // get all
    @GetMapping("/order/{username}")
    public ResponseEntity getAllByUsername(@PathVariable("username") String username) {
        List<OrderOfUserResponseDto> listReturn = orderRepository.getAllOrderByUsername(username);
        System.out.println(listReturn);

        List<ObjectOrderDetailOfUserResponseDto> listOrderDetail = orderDetailRepository.getAllOrderDetailByUsername(username);
        //        System.out.println(listOrderDetail);

        for (OrderOfUserResponseDto x : listReturn) {
            Integer orderId = x.getOrderId();
            System.out.println("orderId: " + orderId);
            List<ObjectOrderDetailOfUserResponseDto> listChild = listOrderDetail
                .stream()
                .filter(item -> item.getOrderId() == orderId)
                .collect(Collectors.toList());
            System.out.println("listChild: " + listChild);
            if (listChild.size() > 0) {
                x.setListObjectOrderDetailOfUserResponseDto(listChild);
            }
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    //save - đặt hàng thông qua giỏ hàng
    @PostMapping("/order")
    @Transactional
    public ResponseEntity save(@RequestBody OrderRequestModel orderRequestModel) {
        //        System.out.println(orderRequestModel.getDateOrder());
        //        SimpleDateFormat sdf1 = new SimpleDateFormat();
        //        sdf1.applyPattern("dd/MM/yyyy HH:mm:ss.SS");
        //        Date date = sdf1.parse(orderRequestModel.getDateOrder()+"");
        //        String string=sdf1.format(date);
        //        System.out.println("Current date in Date Format: " + string);

        OrderResponseDto orderReturn = new OrderResponseDto();

        Order orderParam = new Order();

        orderParam.setAddress(orderRequestModel.getAddress());
        orderParam.setPhone(orderRequestModel.getPhone());
        orderParam.setUsername(orderRequestModel.getUsername());
        orderParam.setDateOrder(orderRequestModel.getDateOrder());
        orderParam.setNote(orderRequestModel.getNote());

        orderParam.setCreatedBy("api");
        orderParam.setCreatedDate(LocalDateTime.now());
        orderParam.setLastModifiedDate(LocalDateTime.now());

        Order orderRest = orderRepository.save(orderParam);
        BeanUtils.copyProperties(orderRest, orderReturn);

        System.out.println("Timezone: " + java.util.TimeZone.getDefault());

        Integer orderId = orderRest.getId();
        // thêm vào bảng order_status

        OrderStatus orderStatusParam = new OrderStatus();

        orderStatusParam.setOrderId(orderId);
        orderStatusParam.setStatus(0); // Tiếp nhận đơn hàng

        orderStatusParam.setCreatedBy("api");
        orderStatusParam.setCreatedDate(LocalDateTime.now());
        orderStatusParam.setLastModifiedDate(LocalDateTime.now());

        try {
            OrderStatus orderStatusRest = orderStatusRepository.save(orderStatusParam);
        } catch (Exception e) {
            throw new ErrorException("Có lỗi xảy ra lưu orderStatus");
        }

        List<ObjectOrderDetail> list = orderRequestModel.getOrderDetails();
        List<OrderDetailResponseDto> listOrderDetail = new ArrayList<>();
        for (ObjectOrderDetail item : list) {
            OrderDetailResponseDto orderDetailReturn = new OrderDetailResponseDto();

            OrderDetail orderDetailParam = new OrderDetail();

            orderDetailParam.setId(new OrderIdentity(item.getFoodId(), orderId));
            orderDetailParam.setAmount(item.getAmount());
            orderDetailParam.setMoney(item.getMoney());

            orderDetailParam.setCreatedBy("api");
            orderDetailParam.setCreatedDate(LocalDateTime.now());
            orderDetailParam.setLastModifiedDate(LocalDateTime.now());

            OrderDetail orderDetailRest = orderDetailRepository.save(orderDetailParam);
            BeanUtils.copyProperties(orderDetailRest, orderDetailReturn);

            listOrderDetail.add(orderDetailReturn);
        }

        BigDecimal totalMoney = caculatorTotalMoney(listOrderDetail);
        Bill billParam = new Bill();

        billParam.setOrderId(orderId);
        billParam.setTotalMoney(totalMoney);
        billParam.setUsername(orderRequestModel.getUsername());

        billParam.setCreatedBy("api");
        billParam.setCreatedDate(LocalDateTime.now());
        billParam.setLastModifiedDate(LocalDateTime.now());

        try {
            billRepository.save(billParam);
        } catch (Exception e) {
            throw new ErrorException(" Lỗi: " + e.getMessage());
        }

        orderReturn.setOrderDetails(listOrderDetail);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderReturn), HttpStatus.OK);
    }

    private BigDecimal caculatorTotalMoney(List<OrderDetailResponseDto> listOrderDetail) {
        BigDecimal sum = new BigDecimal("0.0");
        for (OrderDetailResponseDto x : listOrderDetail) {
            sum = sum.add(x.getMoney());
        }

        return sum;
    }

    //    //save - đặt hàng không qua giỏ hàng
    //    @PostMapping("/orderDirect")
    //    @Transactional
    //    public ResponseEntity saveDirect(@RequestBody OrderRequestModel orderRequestModel) {
    //        OrderResponseDto orderReturn = new OrderResponseDto();
    //
    //        Order orderParam = new Order();
    //
    //        orderParam.setAddress(orderRequestModel.getAddress());
    //        orderParam.setPhone(orderRequestModel.getPhone());
    //        orderParam.setUsername(orderRequestModel.getUsername());
    //        orderParam.setDateOrder(orderRequestModel.getDateOrder());
    //
    //        orderParam.setCreatedBy("api");
    //        orderParam.setCreatedDate(LocalDateTime.now());
    //        orderParam.setLastModifiedDate(LocalDateTime.now());
    //
    //        Order orderRest = orderRepository.save(orderParam);
    //        BeanUtils.copyProperties(orderRest, orderReturn);
    //
    //        Integer orderId = orderRest.getId();
    //
    //        List<ObjectOrderDetail> list = orderRequestModel.getOrderDetails();
    //        List<OrderDetailResponseDto> listOrderDetail = new ArrayList<>();
    //        for (ObjectOrderDetail item : list) {
    //            OrderDetailResponseDto orderDetailReturn = new OrderDetailResponseDto();
    //
    //            OrderDetail orderDetailParam = new OrderDetail();
    //
    //            orderDetailParam.setId(new OrderIdentity(item.getFoodId(), orderId));
    //            orderDetailParam.setAmount(item.getAmount());
    //            orderDetailParam.setMoney(item.getMoney());
    //
    //            orderDetailParam.setCreatedBy("api");
    //            orderDetailParam.setCreatedDate(LocalDateTime.now());
    //            orderDetailParam.setLastModifiedDate(LocalDateTime.now());
    //
    //            OrderDetail orderDetailRest = orderDetailRepository.save(orderDetailParam);
    //            BeanUtils.copyProperties(orderDetailRest, orderDetailReturn);
    //
    //            listOrderDetail.add(orderDetailReturn);
    //        }
    //        orderReturn.setOrderDetails(listOrderDetail);
    //
    //        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderReturn), HttpStatus.OK);
    //    }

    //delete
    @DeleteMapping("/order/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new ErrorException("Không tìm thấy Order với id:= " + id);
        }
        //xóa parent
        orderRepository.delete(orderOptional.get());
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByIdIn(Collections.singletonList(id));
        if (orderDetails.size() > 0) {
            List<Integer> foodIds = new ArrayList<>();
            for (OrderDetail x : orderDetails) {
                OrderIdentity orderIdentity = new OrderIdentity(x.getId().getFoodId(), x.getId().getOrderId());
                orderDetailRepository.deleteAllById(orderIdentity);
            }
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }

    //delete one item in detal with foodId
    @DeleteMapping("/order/itemDetail/{id}")
    @Transactional
    public ResponseEntity deleteOneItemInDetail(@PathVariable("id") Integer id, @RequestParam Integer foodId) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new ErrorException("Không tìm thấy Order với id:= " + id);
        }
        OrderIdentity orderIdentity = new OrderIdentity(foodId, id);
        orderDetailRepository.deleteAllById(orderIdentity);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
