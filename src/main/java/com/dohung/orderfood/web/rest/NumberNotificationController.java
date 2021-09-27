package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.NumberNotification;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.NumberNotificationRepository;
import com.dohung.orderfood.web.rest.response.NumberNotificationResponse;
import java.util.Optional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NumberNotificationController {

    @Autowired
    private NumberNotificationRepository numberNotificationRepository;

    @GetMapping("/notification/number")
    public ResponseEntity getNumberOfNotification() {
        Optional<NumberNotification> optionalNumberNotification = numberNotificationRepository.findById(1);
        if (!optionalNumberNotification.isPresent()) {
            throw new ErrorException("Không tìm thấy NumberNotification với id = 1");
        }
        NumberNotification numberNotification = optionalNumberNotification.get();

        NumberNotificationResponse numberNotificationResponse = new NumberNotificationResponse(numberNotification.getNumber());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, numberNotificationResponse), HttpStatus.OK);
    }

    @PutMapping("/notification/number/increment")
    public ResponseEntity updateNumberIncrement() {
        Optional<NumberNotification> optionalNumberNotification = numberNotificationRepository.findById(1);
        if (!optionalNumberNotification.isPresent()) {
            throw new ErrorException("Không tìm thấy NumberNotification với id = 1");
        }
        NumberNotification numberNotification = optionalNumberNotification.get();
        Integer currentNumber = numberNotification.getNumber();

        Integer newNumber = currentNumber.intValue() + 1;
        numberNotification.setNumber(newNumber);

        NumberNotification numberNotificationRest = numberNotificationRepository.save(numberNotification);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, numberNotificationRest), HttpStatus.OK);
    }

    @PutMapping("/notification/number/clear")
    public ResponseEntity updateNumberClear() {
        Optional<NumberNotification> optionalNumberNotification = numberNotificationRepository.findById(1);
        if (!optionalNumberNotification.isPresent()) {
            throw new ErrorException("Không tìm thấy NumberNotification với id = 1");
        }
        NumberNotification numberNotification = optionalNumberNotification.get();
        numberNotification.setNumber(0);
        NumberNotification numberNotificationRest = numberNotificationRepository.save(numberNotification);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, numberNotificationRest), HttpStatus.OK);
    }
}
