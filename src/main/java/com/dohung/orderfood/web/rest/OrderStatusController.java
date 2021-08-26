package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Order;
import com.dohung.orderfood.domain.OrderStatus;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.OrderRepository;
import com.dohung.orderfood.repository.OrderStatusRepository;
import com.dohung.orderfood.web.rest.request.OderStatusRequestModel;
import com.dohung.orderfood.web.rest.response.ObjectOrderStatusResponseDto;
import com.dohung.orderfood.web.rest.response.ObjectOrderStatusWithDateOrderRespone;
import com.dohung.orderfood.web.rest.response.ObjectOrderTrackingReponse;
import com.dohung.orderfood.web.rest.response.OrderStatusResponseDto;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
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
    public ResponseEntity getAll() {
        List<ObjectOrderStatusResponseDto> listReturn = orderStatusRepository.getAll();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    // get all orderStatus by orderId
    @GetMapping("/orderStatus/orderId")
    public ResponseEntity getOrderStatusById(@RequestParam Integer orderId) {
        List<ObjectOrderTrackingReponse> listReturn = new ArrayList<>();
        listReturn.add(new ObjectOrderTrackingReponse());
        listReturn.add(new ObjectOrderTrackingReponse());
        listReturn.add(new ObjectOrderTrackingReponse());

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new ErrorException("Không tìm thấy Order với Id: " + orderId);
        }

        Date dateOrder = optionalOrder.get().getDateOrder();

        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.getByOrderId(orderId);
        if (!optionalOrderStatus.isPresent()) {
            throw new ErrorException("Không tìm thấy OrderStatus với orderId: " + orderId);
        }

        OrderStatus orderRest = optionalOrderStatus.get();
        System.out.println("order: " + orderRest);
        Integer statusRest = orderRest.getStatus();

        //        Date dateConvert = MethodCommon.convertLocalDateTimeToDate(orderRest.getLastModifiedDate());
        //        System.out.println(dateConvert);

        SimpleDateFormat sdf = new SimpleDateFormat(" dd/MM/yyyy HH:mm:ss");

        String status = null;
        String icon = null;
        String date = sdf.format(dateOrder);
        String color = null;
        String image = null;

        //{ status: 'Tiếp nhận đơn', date: '15/10/2020 10:30', icon: 'pi pi-shopping-cart', color: '#9C27B0', image: 'http://localhost:8083/downloadFile/order.png' },
        //        { status: 'Đang giao hàng', date: '15/10/2020 14:00', icon: 'pi pi-cog', color: '#673AB7', image: 'http://localhost:8083/downloadFile/process.jpg' },
        //        { status: 'Đã giao hàng', date: '16/10/2020 10:00', icon: 'pi pi-check', color: '#607D8B', image: 'http://localhost:8083/downloadFile/delivered.jpg' }

        if (statusRest == 0) {
            status = "Tiếp nhận đơn";
            icon = "pi pi-shopping-cart";
            color = "#9C27B0";
            image = "http://localhost:8083/downloadFile/order.png";

            //ObjectOrderTrackingReponse(String status, String date, String icon, String color, String image)
            ObjectOrderTrackingReponse objectOrderTrackingReponse = new ObjectOrderTrackingReponse(status, date, icon, color, image);
            ObjectOrderTrackingReponse object2 = new ObjectOrderTrackingReponse(
                "Đang giao hàng",
                "",
                "pi pi-cog",
                "#673AB7",
                "http://localhost:8083/downloadFile/process.jpg"
            );
            ObjectOrderTrackingReponse object3 = new ObjectOrderTrackingReponse(
                "Đã giao hàng",
                "",
                "pi pi-check",
                "#607D8B",
                "http://localhost:8083/downloadFile/delivered.jpg"
            );

            listReturn.set(0, objectOrderTrackingReponse);
            listReturn.set(1, object2);
            listReturn.set(2, object3);
        } else if (statusRest == 1) {
            status = "Đang giao hàng";
            icon = "pi pi-cog";
            color = "#673AB7";
            image = "http://localhost:8083/downloadFile/process.jpg";
            String dateProcessed = sdf.format(orderRest.getDateProcessed());

            ObjectOrderTrackingReponse object1 = new ObjectOrderTrackingReponse(
                "Tiếp nhận đơn",
                String.valueOf(dateOrder),
                "pi pi-shopping-cart",
                "#9C27B0",
                "http://localhost:8083/downloadFile/order.png"
            );
            ObjectOrderTrackingReponse objectOrderTrackingReponse = new ObjectOrderTrackingReponse(
                status,
                dateProcessed,
                icon,
                color,
                image
            );
            ObjectOrderTrackingReponse object3 = new ObjectOrderTrackingReponse(
                "Đã giao hàng",
                "",
                "pi pi-check",
                "#607D8B",
                "http://localhost:8083/downloadFile/delivered.jpg"
            );

            listReturn.set(0, object1);
            listReturn.set(1, objectOrderTrackingReponse);
            listReturn.set(2, object3);
        } else {
            status = "Tiếp nhận đơn";
            icon = "pi pi-check";
            color = "#607D8B";
            image = "http://localhost:8083/downloadFile/delivered.jpg";
            String dateProcessed = sdf.format(orderRest.getDateProcessed());
            String dateDelivered = sdf.format(orderRest.getDateDelivered());

            ObjectOrderTrackingReponse object1 = new ObjectOrderTrackingReponse(
                "Tiếp nhận đơn",
                String.valueOf(dateOrder),
                "pi pi-shopping-cart",
                "#9C27B0",
                "http://localhost:8083/downloadFile/order.png"
            );
            ObjectOrderTrackingReponse object2 = new ObjectOrderTrackingReponse(
                "Đang giao hàng",
                dateProcessed,
                "pi pi-cog",
                "#673AB7",
                "http://localhost:8083/downloadFile/process.jpg"
            );
            ObjectOrderTrackingReponse objectOrderTrackingReponse = new ObjectOrderTrackingReponse(
                status,
                dateDelivered,
                icon,
                color,
                image
            );

            listReturn.set(0, object1);
            listReturn.set(1, object2);
            listReturn.set(2, objectOrderTrackingReponse);
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
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
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findByOrderId(id);
        if (!optionalOrderStatus.isPresent()) {
            throw new ErrorException("Không tìm thấy OrderStatus với id:= " + id);
        }

        Integer statusParam = orderStatusRequestModel.getStatus();
        OrderStatus orderStatusParam = optionalOrderStatus.get();
        //        orderStatusParam.setOrderId(orderStatusRequestModel.getOrderId());
        orderStatusParam.setStatus(orderStatusRequestModel.getStatus());
        orderStatusParam.setCreatedBy("api");
        orderStatusParam.setLastModifiedDate(LocalDateTime.now());

        if (statusParam == 1) {
            orderStatusParam.setDateProcessed(new Date());
        } else if (statusParam == 2) {
            orderStatusParam.setDateDelivered(new Date());
        } else {
            orderStatusParam.setLastModifiedDate(LocalDateTime.now());
        }

        OrderStatus orderStatusRest = orderStatusRepository.save(orderStatusParam);
        BeanUtils.copyProperties(orderStatusRest, orderStatusReturn);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderStatusReturn), HttpStatus.OK);
    }
}
