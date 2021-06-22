package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Card;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.CardRepository;
import com.dohung.orderfood.web.rest.request.CardRequestModel;
import com.dohung.orderfood.web.rest.response.CardResponseDto;
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
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    // get all
    @GetMapping("/card")
    public ResponseEntity getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam String username
    ) {
        List<Card> listReturn = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<Card> cardPage = cardRepository.findAllByUsername(paging, username);
        listReturn = cardPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", cardPage.getNumber());
        response.put("totalItems", cardPage.getTotalElements());
        response.put("totalPages", cardPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //save
    @PostMapping("/card")
    public ResponseEntity save(@RequestBody CardRequestModel cardRequestModel) {
        CardResponseDto cardReturn = new CardResponseDto();

        Card cardParam = new Card();

        cardParam.setFoodId(cardRequestModel.getFoodId());
        cardParam.setUsername(cardRequestModel.getUsername());
        cardParam.setCreatedBy("api");
        cardParam.setCreatedDate(LocalDateTime.now());

        Card cardRest = cardRepository.save(cardParam);

        BeanUtils.copyProperties(cardRest, cardReturn);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, cardReturn), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/card/{cardId}")
    public ResponseEntity delete(@PathVariable("cardId") Integer cardId) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (!optionalCard.isPresent()) {
            throw new ErrorException("Không tìm thấy Card với cardId:= " + cardId);
        }
        cardRepository.delete(optionalCard.get());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
