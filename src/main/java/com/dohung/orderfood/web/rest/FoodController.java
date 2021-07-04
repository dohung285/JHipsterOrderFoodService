package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.*;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.CommentRepository;
import com.dohung.orderfood.repository.DiscountRepository;
import com.dohung.orderfood.repository.FoodDetailRepository;
import com.dohung.orderfood.repository.FoodRepository;
import com.dohung.orderfood.web.rest.request.CommentRequestModel;
import com.dohung.orderfood.web.rest.request.CreateDiscountFood;
import com.dohung.orderfood.web.rest.request.FoodRequestModel;
import com.dohung.orderfood.web.rest.request.ObjectFoodDetail;
import com.dohung.orderfood.web.rest.response.CommentResponeDto;
import com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto;
import com.dohung.orderfood.web.rest.response.FoodDetailResponseDto;
import com.dohung.orderfood.web.rest.response.FoodResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import net.bytebuddy.asm.Advice;
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
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodDetailRepository foodDetailRepository;

    @Autowired
    private DiscountRepository discountRepository;

    // get all
    @GetMapping("/food")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<FoodResponseDto> listReturn = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<Food> foodPage = foodRepository.findAll(paging);
        List<Food> listResult = foodPage.getContent();

        List<Integer> foodIds = listResult.stream().map(Food::getId).collect(Collectors.toList());

        //        System.out.println("foodIds: " + foodIds);
        List<FoodDetail> foodDetails = foodDetailRepository.findAllByIdIn(foodIds);
        //        System.out.println("foodDetails: " + foodDetails.toString());
        for (Food x : listResult) {
            FoodResponseDto foodResponseTarget = new FoodResponseDto();
            List<FoodDetailResponseDto> detailResponseDtoList = new ArrayList<>();

            Integer foodId = x.getId();
            BeanUtils.copyProperties(x, foodResponseTarget);

            List<FoodDetail> listFD = foodDetails.stream().filter(e -> e.getId().getFoodId() == foodId).collect(Collectors.toList());

            for (FoodDetail fd : listFD) {
                FoodDetailResponseDto foodDetailResponseTarget = new FoodDetailResponseDto();
                BeanUtils.copyProperties(fd, foodDetailResponseTarget);
                detailResponseDtoList.add(foodDetailResponseTarget);
            }

            foodResponseTarget.setFoodDetails(detailResponseDtoList);

            listReturn.add(foodResponseTarget);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", foodPage.getNumber());
        response.put("totalItems", foodPage.getTotalElements());
        response.put("totalPages", foodPage.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    @GetMapping("/food/foodDetail")
    public ResponseEntity getAllFoodDetail(@RequestParam(name = "foodId") Integer foodId) {
        Optional<FoodByCatalogResponseDto> foodOptional = foodRepository.getAllById(foodId);
        if (!foodOptional.isPresent()) {
            throw new ErrorException("Không tìm thấy Food với id:= " + foodId);
        }
        FoodByCatalogResponseDto foodReturn = foodOptional.get();

        List<String> listImage = foodDetailRepository.getAllImageByFoodId(foodId);
        if (listImage.size() > 0) {
            foodReturn.setListImage(listImage);
        }
        listImage.add(foodReturn.getPath());
        foodReturn.setListImage(listImage);

        //        List<FoodByCatalogResponseDto> listReturn =new ArrayList<>();
        //        listReturn.add(foodReturn);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodReturn), HttpStatus.OK);
    }

    //update discountId to create a discount for food
    @GetMapping("/food/byFoodGroup")
    public ResponseEntity getAllFoodByFoodGroup(
        @RequestParam(name = "foodGroupId") Integer foodGroupId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        List<FoodByCatalogResponseDto> listReturn = new ArrayList<>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<FoodByCatalogResponseDto> pageResult = foodRepository.findAllByGroupId(foodGroupId, paging);
        listReturn = pageResult.getContent();
        System.out.println(pageResult.getContent());

        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //update discountId to create a discount for food
    @PutMapping("/food/{id}")
    public ResponseEntity updateDiscountId(@PathVariable("id") Integer id, @RequestBody CreateDiscountFood createDiscountFood) {
        FoodResponseDto foodReturn = new FoodResponseDto();

        Optional<Food> optionalFood = foodRepository.findAllById(id);
        if (!optionalFood.isPresent()) {
            throw new ErrorException("Không tìm thấy Food với id:= " + id);
        }

        Optional<Discount> optionalDiscount = discountRepository.findAllById(createDiscountFood.getDiscountId());
        if (!optionalDiscount.isPresent()) {
            throw new ErrorException("Không tìm thấy Discount với id:= " + createDiscountFood.getDiscountId());
        }

        Food foodParam = optionalFood.get();
        foodParam.setDiscountId(createDiscountFood.getDiscountId());
        foodParam.setLastModifiedBy("api");
        foodParam.setLastModifiedDate(LocalDateTime.now());

        Food foodRest = foodRepository.save(foodParam);
        BeanUtils.copyProperties(foodRest, foodReturn);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodReturn), HttpStatus.OK);
    }

    //save
    @PostMapping("/food")
    @Transactional
    public ResponseEntity save(@RequestBody FoodRequestModel foodRequestModel) {
        FoodResponseDto foodReturn = new FoodResponseDto();

        List<Food> checkExist = foodRepository.findAllByNameAndGroupId(foodRequestModel.getName(), foodRequestModel.getGroupId());
        if (checkExist.size() <= 0) {
            Food foodParam = new Food();

            foodParam.setName(foodRequestModel.getName());
            foodParam.setPrice(foodRequestModel.getPrice());
            foodParam.setGroupId(foodRequestModel.getGroupId());
            foodParam.setImageId(foodRequestModel.getImageId());
            foodParam.setDescription(foodRequestModel.getDesciption());

            foodParam.setCreatedBy("api");
            foodParam.setCreatedDate(LocalDateTime.now());

            Food foodRest = foodRepository.save(foodParam);
            BeanUtils.copyProperties(foodRest, foodReturn);

            Integer foodId = foodRest.getId();
            List<ObjectFoodDetail> list = foodRequestModel.getFoodDetails();
            List<FoodDetailResponseDto> listFoodDetail = new ArrayList<>();
            for (ObjectFoodDetail item : list) {
                FoodDetailResponseDto foodDetailReturn = new FoodDetailResponseDto();

                FoodDetail foodDetailParam = new FoodDetail();
                foodDetailParam.setId(new FoodIdentity(foodId, item.getImageId()));
                foodDetailParam.setDesciption(item.getDescription());

                foodDetailParam.setCreatedBy("api");
                foodDetailParam.setCreatedDate(LocalDateTime.now());
                foodDetailParam.setLastModifiedDate(LocalDateTime.now());

                FoodDetail foodDetailRest = foodDetailRepository.save(foodDetailParam);
                BeanUtils.copyProperties(foodDetailRest, foodDetailReturn);

                listFoodDetail.add(foodDetailReturn);
            }
            foodReturn.setFoodDetails(listFoodDetail);
        } else {
            throw new ErrorException("name " + foodRequestModel.getName() + " đã tồn tại");
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodReturn), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/food/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        Optional<Food> optionalComment = foodRepository.findById(id);
        if (!optionalComment.isPresent()) {
            throw new ErrorException("Không tìm thấy Food với id:= " + id);
        }
        //xóa parent
        foodRepository.delete(optionalComment.get());

        List<FoodDetail> foodDetails = foodDetailRepository.findAllByIdIn(Collections.singletonList(id));
        if (foodDetails.size() > 0) {
            List<Integer> foodIds = new ArrayList<>();
            for (FoodDetail x : foodDetails) {
                FoodIdentity foodIdentity = new FoodIdentity(x.getId().getFoodId(), x.getId().getImageId());
                foodDetailRepository.deleteAllById(foodIdentity);
            }
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
