package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Card;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.CardRepository;
import com.dohung.orderfood.web.rest.request.CardRequestModel;
import com.dohung.orderfood.web.rest.request.CardUpdateAmountRequestModel;
import com.dohung.orderfood.web.rest.response.CardResponseDto;
import com.dohung.orderfood.web.rest.response.FoodCardResponseDto;
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
        //        @RequestParam(defaultValue = "0") int page,
        //        @RequestParam(defaultValue = "50") int size,
        @RequestParam String username
    ) {
        List<FoodCardResponseDto> listReturn = new ArrayList<>();

        //        Pageable paging = PageRequest.of(page - 1, size);
        //        Page<FoodCardResponseDto> cardPage = cardRepository.findAllByUsername(paging, username);
        //        listReturn = cardPage.getContent();
        listReturn = cardRepository.findAllByUsernameQuery(username);

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        //        response.put("currentPage", cardPage.getNumber());
        //        response.put("totalItems", cardPage.getTotalElements());
        //        response.put("totalPages", cardPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    // get all
    @GetMapping("/card/number")
    public ResponseEntity countNumberFoodInCard(@RequestParam String username) {
        List<Card> listReturn = cardRepository.findAllByUsername(username);

        Map<String, Object> response = new HashMap<>();

        response.put("totalItems", listReturn.size());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //save
    @PostMapping("/card")
    public ResponseEntity save(@RequestBody CardRequestModel cardRequestModel) {
        CardResponseDto cardReturn = new CardResponseDto();

        Card cardParam = null;
        Optional<Card> optionalCard = cardRepository.findAllByUsernameAndFoodId(
            cardRequestModel.getUsername(),
            cardRequestModel.getFoodId()
        );
        if (optionalCard.isPresent()) {
            cardParam = optionalCard.get();
        } else {
            cardParam = new Card();
        }
        cardParam.setFoodId(cardRequestModel.getFoodId());
        cardParam.setUsername(cardRequestModel.getUsername());
        cardParam.setAmount(cardRequestModel.getAmount());

        cardParam.setCreatedBy("api");
        cardParam.setCreatedDate(LocalDateTime.now());

        Card cardRest = cardRepository.save(cardParam);

        BeanUtils.copyProperties(cardRest, cardReturn);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, cardReturn), HttpStatus.OK);
    }

    @PutMapping("/card")
    public ResponseEntity save(@RequestParam Integer cardId, @RequestBody CardUpdateAmountRequestModel cardUpdateAmountRequestModel) {
        CardResponseDto cardReturn = new CardResponseDto();

        Card cardParam = null;
        Optional<Card> optionalCard = cardRepository.findAllById(cardId);
        if (!optionalCard.isPresent()) {
            throw new ErrorException("Không tìm thấy card với id:= " + cardId);
        }

        cardParam = optionalCard.get();

        cardParam.setAmount(cardUpdateAmountRequestModel.getAmount());
        cardParam.setLastModifiedDate(LocalDateTime.now());

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

    //delete all
    @DeleteMapping("/card/all")
    public ResponseEntity deleteAll(@RequestParam("cardIds") List<Integer> cardIds) {
        List<Card> list = cardRepository.findAllByIdIn(cardIds);
        if (list.size() > 0) {
            for (Card x : list) {
                cardRepository.delete(x);
            }
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
