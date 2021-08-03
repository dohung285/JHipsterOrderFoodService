package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Comment;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.CommentRepository;
import com.dohung.orderfood.web.rest.request.CommentRequestModel;
import com.dohung.orderfood.web.rest.response.CommentObjectResponseDto;
import com.dohung.orderfood.web.rest.response.CommentResponeDto;
import com.dohung.orderfood.web.rest.response.ObjectCountRatingResponseDto;
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
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    // get all
    @GetMapping("/comment")
    public ResponseEntity getAll() {
        List<CommentObjectResponseDto> listReturn = commentRepository.getAll();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    // get all comment by foodId
    @GetMapping("/comment/{foodId}")
    public ResponseEntity getAllCommentByFoodId(@PathVariable("foodId") Integer foodId) {
        List<Comment> listReturn = commentRepository.findAllByFoodIdAndOrderByCreatedDateDesc(foodId);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    @GetMapping("/countStar")
    public ResponseEntity countStar(@RequestParam Integer foodId) {
        Object objectResult = commentRepository.countStar(foodId);
        System.out.println("objectResult: " + objectResult);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, objectResult), HttpStatus.OK);
    }

    // get 10 latest comments
    @GetMapping("/tenLastestComment")
    public ResponseEntity getTenLastestComment() {
        List<Comment> listReturn = commentRepository.findTop10ByOrderByCreatedDateDesc();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    //save
    @PostMapping("/comment")
    public ResponseEntity saveFoodGroup(@RequestBody CommentRequestModel commentRequestModel) {
        CommentResponeDto commentReturn = new CommentResponeDto();

        Comment commentParam = new Comment();

        commentParam.setFoodId(commentRequestModel.getFoodId());
        commentParam.setContent(commentRequestModel.getContent());
        commentParam.setUsername(commentRequestModel.getUsername());
        commentParam.setRating(commentRequestModel.getRating());

        commentParam.setCreatedBy("api");
        commentParam.setCreatedDate(LocalDateTime.now());

        Comment commentRest = commentRepository.save(commentParam);

        BeanUtils.copyProperties(commentRest, commentReturn);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, commentReturn), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteFoodGroup(@PathVariable("commentId") Integer commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new ErrorException("Không tìm thấy Commet với commentId:= " + commentId);
        }
        commentRepository.delete(optionalComment.get());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
