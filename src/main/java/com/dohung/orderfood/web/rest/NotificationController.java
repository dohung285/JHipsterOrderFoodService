package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.domain.Notification;
import com.dohung.orderfood.domain.Order;
import com.dohung.orderfood.domain.OrderDetail;
import com.dohung.orderfood.repository.NotificationRepository;
import com.dohung.orderfood.web.rest.request.UpdateIsDeletedNotificationRequest;
import com.dohung.orderfood.web.rest.response.OrderDetailResponseDto;
import com.dohung.orderfood.web.rest.response.OrderResponseDto;
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
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // get all
    @GetMapping("/notification")
    public ResponseEntity getAll() {
        List<Notification> listReturn = notificationRepository.findAllByIsDeletedEqualsOrderByCreatedDateDesc(0);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    // get all
    @PutMapping("/notification/update-isdeleted")
    @Transactional
    public ResponseEntity updatedIsDeleted(@RequestParam List<Integer> ids) {
        List<Notification> notifications = notificationRepository.findAllByIdIn(ids);
        // lấy ra danh sách id của notification
        List<Integer> notificationIds = notifications.stream().map(Notification::getId).collect(Collectors.toList());
        //update
        notificationRepository.updateIsDeleted(notificationIds);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "successful"), HttpStatus.OK);
    }
}
