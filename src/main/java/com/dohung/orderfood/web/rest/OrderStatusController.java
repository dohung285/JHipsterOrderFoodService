package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Order;
import com.dohung.orderfood.domain.OrderStatus;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.OrderDetailRepository;
import com.dohung.orderfood.repository.OrderRepository;
import com.dohung.orderfood.repository.OrderStatusRepository;
import com.dohung.orderfood.web.rest.request.ObjectGmailRequest;
import com.dohung.orderfood.web.rest.request.OderStatusRequestModel;
import com.dohung.orderfood.web.rest.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class OrderStatusController {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // get all
    @GetMapping("/orderStatus")
    public ResponseEntity getAll() {
        List<ObjectOrderStatusResponseDto> listReturn = orderStatusRepository.getAll();

        List<Tuple> listChild = orderDetailRepository.getAllOrderDetail();
        System.out.println("listChild: " + listChild);

        List<ObjectChildOrderDetailResponse> listChildResult = listChild
            .stream()
            .map(
                x ->
                    new ObjectChildOrderDetailResponse(
                        x.get(0, Integer.class),
                        x.get(1, Integer.class),
                        x.get(2, String.class),
                        x.get(3, Integer.class),
                        x.get(4, BigDecimal.class),
                        x.get(5, String.class)
                    )
            )
            .collect(Collectors.toList());

        for (ObjectOrderStatusResponseDto x : listReturn) {
            Integer orderId = x.getId();
            List<ObjectChildOrderDetailResponse> listChildX = listChildResult
                .stream()
                .filter(item -> item.getOrderId() == orderId)
                .collect(Collectors.toList());
            System.out.println("listChildX: " + listChildX);

            if (listChildX.size() > 0) {
                x.setListChild(listChildX);
            }
        }

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
            throw new ErrorException("Kh??ng t??m th???y Order v???i Id: " + orderId);
        }

        Date dateOrder = optionalOrder.get().getDateOrder();

        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.getByOrderId(orderId);
        if (!optionalOrderStatus.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y OrderStatus v???i orderId: " + orderId);
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

        //{ status: 'Ti???p nh???n ????n', date: '15/10/2020 10:30', icon: 'pi pi-shopping-cart', color: '#9C27B0', image: 'http://localhost:8083/downloadFile/order.png' },
        //        { status: '??ang giao h??ng', date: '15/10/2020 14:00', icon: 'pi pi-cog', color: '#673AB7', image: 'http://localhost:8083/downloadFile/process.jpg' },
        //        { status: '???? giao h??ng', date: '16/10/2020 10:00', icon: 'pi pi-check', color: '#607D8B', image: 'http://localhost:8083/downloadFile/delivered.jpg' }

        if (statusRest == 0) {
            status = "Ti???p nh???n ????n";
            icon = "pi pi-shopping-cart";
            color = "#9C27B0";
            image = "http://localhost:8083/downloadFile/order.png";

            //ObjectOrderTrackingReponse(String status, String date, String icon, String color, String image)
            ObjectOrderTrackingReponse objectOrderTrackingReponse = new ObjectOrderTrackingReponse(status, date, icon, color, image);
            ObjectOrderTrackingReponse object2 = new ObjectOrderTrackingReponse(
                "??ang giao h??ng",
                "",
                "pi pi-cog",
                "#673AB7",
                "http://localhost:8083/downloadFile/process.jpg"
            );
            ObjectOrderTrackingReponse object3 = new ObjectOrderTrackingReponse(
                "???? giao h??ng",
                "",
                "pi pi-check",
                "#607D8B",
                "http://localhost:8083/downloadFile/delivered.jpg"
            );

            listReturn.set(0, objectOrderTrackingReponse);
            listReturn.set(1, object2);
            listReturn.set(2, object3);
        } else if (statusRest == 1) {
            status = "??ang giao h??ng";
            icon = "pi pi-cog";
            color = "#673AB7";
            image = "http://localhost:8083/downloadFile/process.jpg";
            String dateProcessed = sdf.format(orderRest.getDateProcessed());

            ObjectOrderTrackingReponse object1 = new ObjectOrderTrackingReponse(
                "Ti???p nh???n ????n",
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
                "???? giao h??ng",
                "",
                "pi pi-check",
                "#607D8B",
                "http://localhost:8083/downloadFile/delivered.jpg"
            );

            listReturn.set(0, object1);
            listReturn.set(1, objectOrderTrackingReponse);
            listReturn.set(2, object3);
        } else {
            status = "Ti???p nh???n ????n";
            icon = "pi pi-check";
            color = "#607D8B";
            image = "http://localhost:8083/downloadFile/delivered.jpg";
            String dateProcessed = sdf.format(orderRest.getDateProcessed());
            String dateDelivered = sdf.format(orderRest.getDateDelivered());

            ObjectOrderTrackingReponse object1 = new ObjectOrderTrackingReponse(
                "Ti???p nh???n ????n",
                String.valueOf(dateOrder),
                "pi pi-shopping-cart",
                "#9C27B0",
                "http://localhost:8083/downloadFile/order.png"
            );
            ObjectOrderTrackingReponse object2 = new ObjectOrderTrackingReponse(
                "??ang giao h??ng",
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
            throw new ErrorException("Kh??ng t??m th???y Order v???i id:= " + orderStatusRequestModel.getOrderId());
        }
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findByOrderId(orderStatusRequestModel.getOrderId());
        if (optionalOrderStatus.isPresent()) {
            throw new ErrorException("???? t???n t???i OrderStatus v???i id:= " + orderStatusRequestModel.getOrderId());
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

        Integer orderId = orderStatusRequestModel.getOrderId();

        Optional<Order> orderOptional = orderRepository.findById(orderStatusRequestModel.getOrderId());
        if (!orderOptional.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y Order v???i id:= " + orderStatusRequestModel.getOrderId());
        }

        //t??m ?????i t?????ng c???n update
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findByOrderId(id);
        if (!optionalOrderStatus.isPresent()) {
            throw new ErrorException("Kh??ng t??m th???y OrderStatus v???i id:= " + id);
        }

        Integer statusParam = orderStatusRequestModel.getStatus();
        OrderStatus orderStatusParam = optionalOrderStatus.get();
        //        orderStatusParam.setOrderId(orderStatusRequestModel.getOrderId());

        Integer currentStatus = orderStatusParam.getStatus();
        if (orderStatusRequestModel.getStatus() <= currentStatus) {
            System.out.println(
                "Trang th??i ????n h??ng kh??ng h???p l???, tr???ng th??i hi???n t???i l??: " +
                currentStatus +
                " tr???ng th??i truy???n v??o l??: " +
                orderStatusRequestModel.getStatus()
            );
            throw new ErrorException("Tr???ng th??i kh??ng h???p l???!");
        }

        orderStatusParam.setStatus(orderStatusRequestModel.getStatus());
        orderStatusParam.setCreatedBy("api");
        orderStatusParam.setLastModifiedDate(LocalDateTime.now());

        String textGmail = null;
        if (statusParam == 1) { // ??ang giao h??ng
            orderStatusParam.setDateProcessed(new Date());
            textGmail =
                "C???m ??n b???n ???? s??? d???ng d???ch c???a ch??ng t??i! \n" +
                "????n h??ng v???i m?? ????n l?? " +
                orderId +
                " ???? ???????c g???i t???i ????n v??? v???n chuy???n, xin vui l??ng ?????i ????n v??? v???n chuy???n giao h??ng ?????n b???n!\n" +
                "Orderfood!";
        } else if (statusParam == 2) { // giao th??nh c??ng
            orderStatusParam.setDateDelivered(new Date());
            textGmail =
                "C???m ??n b???n ???? s??? d???ng d???ch c???a ch??ng t??i! \n" +
                "????n h??ng v???i m?? ????n l?? " +
                orderId +
                " ???? ???????c ???????c ????n v??? giao h??ng ?????n b???n th??nh c??ng!\n" +
                "Xin vui l??ng ki???m tra h??ng c???n th???n v?? thanh to??n s??? ti???n theo h??a ????n ????nh k??m! \n" +
                "Orderfood!";
        } else {
            orderStatusParam.setLastModifiedDate(LocalDateTime.now());
        }

        // g???i email th??ng b??o ?????n kh??ch h??ng
        if (statusParam == 1 || statusParam == 2) {
            ObjectReportReturn report = getReport(orderId);
            if (Objects.isNull(report)) {
                throw new ErrorException("Object report kh??ng ????? th??ng tin: " + report.toString());
            }

            ObjectGmailRequest objectGmailRequest = new ObjectGmailRequest();
            objectGmailRequest.setMailTo("doxuanhungvnhn@gmail.com");
            objectGmailRequest.setSubject("G???i h??ng");
            objectGmailRequest.setText(textGmail);
            objectGmailRequest.setFileName(report.getName() + ".pdf");
            try {
                String answer = sendEmailWhenDeliveryOrder(objectGmailRequest, true);
                System.out.println("answerMail->0k: " + answer);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new ErrorException("Loi: " + e.getMessage());
            }
        }

        OrderStatus orderStatusRest = null;
        try {
            orderStatusRest = orderStatusRepository.save(orderStatusParam);
            BeanUtils.copyProperties(orderStatusRest, orderStatusReturn);
        } catch (Exception e) {
            throw new ErrorException("L???i: " + e.getMessage());
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, orderStatusReturn), HttpStatus.OK);
    }

    private ObjectReportReturn getReport(Integer orderId) {
        // call api
        RestTemplate restTemplate = new RestTemplate(); // d??ng restemplate ????? call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String url = StringConstant.URL_API_REPORT + "/report/api/report/" + orderId;

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            throw new ErrorException(e.getMessage());
        }
        System.out.println("answerGetReport: " + answer);
        if (answer.getStatusCodeValue() == 200) {
            JSONObject jsonObject = new JSONObject(answer.getBody());
            JSONObject jsonObjectReturn = jsonObject.getJSONObject("object");

            Integer id = jsonObjectReturn.getInt("id");
            Integer orderIdReport = jsonObjectReturn.getInt("orderId");
            String fileName = jsonObjectReturn.getString("name");
            String pathReport = jsonObjectReturn.getString("path");

            ObjectReportReturn objectReportReturn = new ObjectReportReturn(id, orderIdReport, fileName, pathReport);
            return objectReportReturn;
        } else {
            return new ObjectReportReturn();
        }
    }

    private String sendEmailWhenDeliveryOrder(ObjectGmailRequest objectGmailRequest, boolean type) throws JsonProcessingException {
        //type = true : g???i mail c?? attachment  || type = false : g???i mail ko c?? attachment
        OrderStatus orderStatusRest = null;

        // call api
        RestTemplate restTemplate = new RestTemplate(); // d??ng restemplate ????? call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String gmailObject = objectMapper.writeValueAsString(objectGmailRequest); //convert
        System.out.println("gmailObject: " + objectGmailRequest);

        HttpEntity<String> entity = new HttpEntity<String>(gmailObject, headers);

        String createObjectURL = null;
        if (type) {
            createObjectURL = StringConstant.URL_API_GMAIL + "/gmail/sendEmailwithAttachment";
        } else {
            createObjectURL = StringConstant.URL_API_GMAIL + "/gmail/sendEmail";
        }

        ResponseEntity<String> answer = null;
        try {
            answer = restTemplate.exchange(createObjectURL, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            throw new ErrorException(e.getMessage());
        }
        System.out.println("answerMail: " + answer);

        //end g???i mail
        return "ok";
    }
}
