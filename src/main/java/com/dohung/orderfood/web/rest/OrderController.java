package com.dohung.orderfood.web.rest;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import com.dohung.orderfood.common.MethodCommon;
import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.*;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.*;
import com.dohung.orderfood.service.PushNotificationService;
import com.dohung.orderfood.web.rest.request.ObjectReportRequestModel;
import com.dohung.orderfood.web.rest.request.OrderRequestModel;
import com.dohung.orderfood.web.rest.request.PushNotificationRequest;
import com.dohung.orderfood.web.rest.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private NotificationRepository notificationRepository;

    private PushNotificationService pushNotificationService;

    public OrderController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    // get all
    @GetMapping("/order/accessToken")
    public ResponseEntity getAccessToken() {
        //lấy ra accessToken với real master (admin)
        String accessToken = MethodCommon.getAccessToken();

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        String url = "http://localhost:8080/auth/realms/orderfood/protocol/openid-connect/token";
        postParameters.add("client_id", "orderfoodservice");
        postParameters.add("client_secret", "c782ec20-4cdf-4797-bcb6-4d6b211962ef");
        postParameters.add("grant_type", "password");
        //        postParameters.add("scope", "read");
        postParameters.add("username", "hungdx");
        postParameters.add("password", "12369874");

        HttpHeaders headers = new HttpHeaders();
        //        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(
            "Authorization",
            "Bearer " +
            "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2TG8tbExmQWhrejVmREFzdldxZ3dnSGNTYmxfd0xNZlZxMlRyd2JEcXM4In0.eyJleHAiOjE2MzA5NTUzNzgsImlhdCI6MTYzMDkxOTM3OCwianRpIjoiZWUzYzk2Y2EtYTRkZS00Njg2LThhN2EtNDdlZGI4ZTM0Njk4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL29yZGVyZm9vZCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJhM2JhY2M4Mi01MWFlLTRkOGItOTQyOS1iODQyZjczOGUyMzIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvcmRlcmZvb2RzZXJ2aWNlIiwic2Vzc2lvbl9zdGF0ZSI6Ijk3MGQzOTRkLWY2ZWMtNDhkZi04NjA1LWQ2ODU1ZTdlZGE4OCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iLCJSZWFsbUFkbWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkRvIEh1bmciLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJodW5nZHgiLCJnaXZlbl9uYW1lIjoiRG8iLCJmYW1pbHlfbmFtZSI6Ikh1bmciLCJlbWFpbCI6ImRvaHVuZzEyMzY5ODc0QGdtYWlsLmNvbSJ9.n9YpFW66nb-XFxVBc4Ok1taf9Ef4Tt9COvWWOCPARNXBMfhJmRDeCz_tmDE5yno3V9mm_Agsqu88BsTKfjoAp9iYli6SEjWbBVwGWTRE6sVFv9UI07E3ylo90_775LFcURvUgsICw5RL4Y5OsOu15YCx41dY7orGTuUrS8Y_rWxsl8Myg3hsTMFrfQHQXIH1Ay91Q_PaDGkxf2J4JVEmaAB_vkLyP8Rg534jOhCR0EQn_aK5a3gEenCAmT-nOu91VD4bPg6x_s7efiTLi2U-cAIcBuPiVRsFqO_jsXsaXcw3ShRbEnD9crFoql21mt9RuOKVSssxsk6Bj2lCudpO-w"
        );

        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);

        RestTemplate restTemplate = new RestTemplate();
        String responseMessage = restTemplate.postForObject(url, r, String.class);

        System.out.println("responseMessage: " + responseMessage); //access_token

        JSONObject jsonObject = new JSONObject(responseMessage);

        accessToken = (String) jsonObject.get("access_token");
        System.out.println("access_token: " + accessToken);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, ""), HttpStatus.OK);
    }

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
    public ResponseEntity save(@RequestBody OrderRequestModel orderRequestModel) throws JsonProcessingException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

        System.out.println("Timezone: " + TimeZone.getDefault());

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
        //        List<OrderDetail> listOrderDetailRest = new ArrayList<>();
        for (ObjectOrderDetail item : list) {
            OrderDetailResponseDto orderDetailReturn = new OrderDetailResponseDto();

            OrderDetail orderDetailParam = new OrderDetail();

            orderDetailParam.setId(new OrderIdentity(item.getFoodId(), orderId));
            orderDetailParam.setAmount(item.getAmount());
            orderDetailParam.setPrice(item.getPrice());
            orderDetailParam.setMoney(item.getMoney());

            orderDetailParam.setCreatedBy("api");
            orderDetailParam.setCreatedDate(LocalDateTime.now());
            orderDetailParam.setLastModifiedDate(LocalDateTime.now());

            OrderDetail orderDetailRest = orderDetailRepository.save(orderDetailParam);
            //            listOrderDetailRest.add(orderDetailRest);
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

        //create report
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        OrderReport orderReport = new OrderReport();
        orderReport.setId(orderRest.getId());
        orderReport.setUsername(orderRest.getUsername());
        orderReport.setPhone(orderRest.getPhone());
        orderReport.setAddress(orderRest.getAddress());
        orderReport.setDateOrder(sdf.format(orderRest.getDateOrder()));
        orderReport.setNote(orderRest.getNote());

        // lấy ra danh sách orderDetail theo orderId
        List<Tuple> orderDetails = orderDetailRepository.getAllOrderDetailByOrderId(orderId);
        //convert sang orderDetail
        List<OrderDetailReport> listOrderDetailResult = orderDetails
            .stream()
            .map(
                x ->
                    new OrderDetailReport(
                        x.get(0, Integer.class),
                        x.get(1, String.class),
                        x.get(2, Integer.class),
                        x.get(3, Integer.class),
                        x.get(4, BigDecimal.class)
                    )
            )
            .collect(Collectors.toList());

        List<OrderDetailReport> listOrderDetailReport = listOrderDetailResult
            .stream()
            .map(
                x -> new OrderDetailReport(x.getId(), x.getName(), x.getAmount(), x.getPercent() == null ? 0 : x.getPercent(), x.getMoney())
            )
            .collect(Collectors.toList());

        ObjectReportRequestModel objectReportRequestModel = new ObjectReportRequestModel(orderReport, listOrderDetailReport, totalMoney);

        RestTemplate restTemplate = new RestTemplate(); // dùng restemplate để call api
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String reportObject = objectMapper.writeValueAsString(objectReportRequestModel); //convert
        HttpEntity<String> entity = new HttpEntity<String>(reportObject, headers);

        String objectURLUserHasRoleAdmin = StringConstant.URL_API_REPORT + "/report/api/create";
        ResponseEntity<String> response = restTemplate.exchange(objectURLUserHasRoleAdmin, HttpMethod.POST, entity, String.class);
        System.out.println("response: " + response);

        // gửi thông báo lên firebase
        PushNotificationRequest request = new PushNotificationRequest();
        request.setTitle("orderfood_system");
        request.setMessage("Đơn hàng mới có mã đơn là orderId: " + orderId);
        request.setTopic("a");
        request.setToken(orderRequestModel.getFirebaseToken());
        //        request.setToken(
        //            "cT-UrDYHH97lXas8wG2i06:APA91bFpRw5LRgRAkg_so2kFuNW3q2cAXZ078nU9_cO6pqc3gARrN0kG8sVS4hTM2ZKXcxJumWKNbWMqGlQOBsjdB76CpTrOaQK00jv_sCkQSg9cCrdPDBUI-Mc0Pw89ljxwDbn2FWYz"
        //        );
        try {
            pushNotificationService.sendPushNotification(request);
        } catch (Exception e) {
            throw new ErrorException("Lỗi pushNotificationFirebase: " + e.getMessage());
        }
        //Sau đó lưu nội dung thông báo vào db
        Optional<Notification> optionalNotification = notificationRepository.findByOrderId(orderId);
        if (optionalNotification.isPresent()) {
            throw new ErrorException("Thông báo với đơn hàng có mã orderId= " + orderId + " đã tồn tại trong database");
        } else {
            Notification notification = new Notification();
            notification.setOrderId(orderId);
            notification.setContent("Đơn hàng mới có mã đơn là orderId: " + orderId);
            notification.setCreatedBy("api");
            notification.setIsDeleted(0);
            notification.setCreatedDate(LocalDateTime.now());
            notification.setLastModifiedDate(LocalDateTime.now());
            Notification notificationRest = notificationRepository.save(notification);
            if (Objects.isNull(notificationRest)) {
                throw new ErrorException("Có lỗi xảy ra khi lưu notification");
            }
        }
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
