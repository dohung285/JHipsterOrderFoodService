package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.domain.FoodDetail;
import com.dohung.orderfood.domain.FoodIdentity;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.DiscountRepository;
import com.dohung.orderfood.repository.FoodDetailRepository;
import com.dohung.orderfood.repository.FoodRepository;
import com.dohung.orderfood.web.rest.request.CreateDiscountFood;
import com.dohung.orderfood.web.rest.request.FoodRequestModel;
import com.dohung.orderfood.web.rest.request.ObjectFoodDetail;
import com.dohung.orderfood.web.rest.request.UpdateFoodRequestModel;
import com.dohung.orderfood.web.rest.response.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodDetailRepository foodDetailRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    OAuth2AuthorizedClientService clientService;

    //    @GetMapping("/currentAccessToken")
    //    public String getAuthorizationHeader() {
    //        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //        if (authentication instanceof OAuth2AuthenticationToken) {
    //            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    //            String name = oauthToken.getName();
    //            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
    //            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(registrationId, name);
    //
    //            if (null == client) {
    //                throw new OAuth2AuthorizationException(new OAuth2Error("access_denied", "The token is expired", null));
    //            }
    //            OAuth2AccessToken accessToken = client.getAccessToken();
    //
    //            if (accessToken != null) {
    //                String tokenType = accessToken.getTokenType().getValue();
    //                String accessTokenValue = accessToken.getTokenValue();
    //                return accessTokenValue;
    //            }
    //        }
    //        return null;
    //    }

    // get all
    @GetMapping("/food")
    public ResponseEntity getAllNoPaging() {
        List<Tuple> listResult = foodRepository.getAllNoPaging();

        List<ObjectFoodResponseDto> listReturn = listResult
            .stream()
            .map(
                x ->
                    new ObjectFoodResponseDto(
                        x.get(0, Integer.class),
                        x.get(1, String.class),
                        x.get(2, String.class),
                        x.get(3, BigDecimal.class)
                    )
            )
            .collect(Collectors.toList());
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    // get all
    @GetMapping("/food/all/paging")
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

    @GetMapping("/food/all")
    public ResponseEntity getAllFoodForDiscount() {
        List<FoodByCatalogResponseDto> listReturn = foodRepository.getAllFoodForDiscount();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    @GetMapping("/food/had-discount")
    public ResponseEntity getAllFoodHadDiscount() {
        List<FoodByCatalogResponseDto> listReturn = foodRepository.getAllFoodHadDiscount();
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
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

    //search food
    @GetMapping("/food/search")
    public ResponseEntity searchFood(
        @RequestParam(name = "foodGroupId") Integer foodGroupId,
        @RequestParam(name = "foodName") String foodName
    ) {
        List<Tuple> listReturn = foodRepository.getAllByNameContainingAndGroupId(foodName, foodGroupId);

        List<FoodSearchResponseDto> listResultCountAPI = listReturn
            .stream()
            .map(
                x ->
                    new FoodSearchResponseDto(
                        x.get(0, Integer.class),
                        x.get(1, String.class),
                        x.get(2, BigDecimal.class),
                        x.get(3, String.class),
                        x.get(4, Integer.class),
                        x.get(5, Integer.class),
                        x.get(6, Integer.class),
                        x.get(7, String.class)
                    )
            )
            .collect(Collectors.toList());

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listResultCountAPI), HttpStatus.OK);
    }

    //update discountId to create a discount for food
    @GetMapping("/food/byFoodGroup")
    public ResponseEntity getAllFoodByFoodGroup(@RequestParam(name = "foodGroupId") Integer foodGroupId) {
        List<FoodByCatalogResponseDto> listReturn = foodRepository.getAllByGroupId(foodGroupId);
        Map<String, Object> response = new HashMap<>();
        response.put("listReturn", listReturn);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, response), HttpStatus.OK);
    }

    //update price and name of food
    @PutMapping("/food/priceAndName/{id}")
    public ResponseEntity updatePriceAndNameOfProduct(
        @PathVariable("id") Integer id,
        @RequestBody UpdateFoodRequestModel updateFoodRequestModel
    ) {
        Optional<Food> optionalFood = foodRepository.findAllById(id);
        if (!optionalFood.isPresent()) {
            throw new ErrorException("Không tìm thấy Food với id: " + id);
        }

        Food foodRest = optionalFood.get();
        String currentName = foodRest.getName();

        if (currentName != updateFoodRequestModel.getName()) {
            List<Food> optionalFoodName = foodRepository.findAllByNameAndNameNot(updateFoodRequestModel.getName(), currentName);
            if (optionalFoodName.size() > 0) {
                throw new ErrorException("Đã tồn tại tên: " + updateFoodRequestModel.getName());
            }
        }

        foodRest.setName(updateFoodRequestModel.getName());
        foodRest.setPrice(updateFoodRequestModel.getPrice());

        foodRepository.save(foodRest);
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "Success"), HttpStatus.OK);
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

    //update discountId to create a discount for food
    @PutMapping("/food/update-discountid")
    public ResponseEntity updateDiscountIdForListFoodId(@RequestBody CreateDiscountFood createDiscountFood) {
        List<FoodResponseDto> listReturn = new ArrayList<>();

        Optional<Discount> optionalDiscount = discountRepository.findAllById(createDiscountFood.getDiscountId());
        if (!optionalDiscount.isPresent()) {
            throw new ErrorException("Không tìm thấy Discount với id:= " + createDiscountFood.getDiscountId());
        }

        List<Integer> listFoodId = createDiscountFood.getFoodIds();
        for (Integer item : listFoodId) {
            Optional<Food> optionalFood = foodRepository.findAllById(item);
            if (!optionalFood.isPresent()) {
                throw new ErrorException("Không tìm thấy Food với id:= " + item);
            }
            FoodResponseDto foodReturn = new FoodResponseDto();

            Food foodParam = optionalFood.get();
            foodParam.setDiscountId(createDiscountFood.getDiscountId());
            foodParam.setLastModifiedBy("api");
            foodParam.setLastModifiedDate(LocalDateTime.now());

            Food foodRest = foodRepository.save(foodParam);
            BeanUtils.copyProperties(foodRest, foodReturn);

            listReturn.add(foodReturn);
        }
        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }

    public static File convert(MultipartFile file) {
        File convFile = new File("temp_image", file.getOriginalFilename());
        if (!convFile.getParentFile().exists()) {
            System.out.println("mkdir:" + convFile.getParentFile().mkdirs());
        }
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

    //save
    @PostMapping("/food")
    @Transactional
    public ResponseEntity save(
        //        @RequestParam("files") MultipartFile files,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("name") String name,
        @RequestParam("price") BigDecimal price,
        @RequestParam("groupId") Integer groupId,
        @RequestParam("desciption") String desciption
    ) {
        List<MultipartFile> list = Arrays.asList(files);
        System.out.println("list" + list.size());

        String url = "http://localhost:8083/uploadMultipleFiles";
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        for (MultipartFile x : list) {
            bodyMap.add("files", new FileSystemResource(convert(x)));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            throw new ErrorException("Lỗi " + e.getMessage());
        }

        System.out.println("Response code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        List<Integer> listInteger = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response.getBody());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            //            JSONObject jsonBody = jsonObject.getJSONObject("body");
            System.out.println("jsonObject: " + jsonObject);

            System.out.println("id: " + jsonObject.getInt("id"));
            listInteger.add(jsonObject.getInt("id"));
        }

        FoodResponseDto foodReturn = new FoodResponseDto();

        List<Food> checkExist = foodRepository.findAllByNameAndGroupId(name, groupId);
        if (checkExist.size() <= 0) {
            Food foodParam = new Food();

            foodParam.setName(name);
            foodParam.setPrice(price);
            foodParam.setGroupId(groupId);
            foodParam.setImageId(listInteger.get(0));
            foodParam.setDescription(desciption);
            foodParam.setIsDeleted(0);

            foodParam.setCreatedBy("api");
            foodParam.setCreatedDate(LocalDateTime.now());
            foodParam.setLastModifiedDate(LocalDateTime.now());

            Food foodRest = foodRepository.save(foodParam);
            BeanUtils.copyProperties(foodRest, foodReturn);

            Integer foodId = foodRest.getId();
            List<FoodDetailResponseDto> listFoodDetail = new ArrayList<>();
            if (listInteger.size() > 1) {
                for (int i = 1; i < listInteger.size(); i++) {
                    FoodDetailResponseDto foodDetailReturn = new FoodDetailResponseDto();
                    FoodDetail foodDetailParam = new FoodDetail();
                    foodDetailParam.setId(new FoodIdentity(foodId, listInteger.get(i)));

                    foodDetailParam.setCreatedBy("api");
                    foodDetailParam.setCreatedDate(LocalDateTime.now());
                    foodDetailParam.setLastModifiedDate(LocalDateTime.now());

                    FoodDetail foodDetailRest = foodDetailRepository.save(foodDetailParam);
                    BeanUtils.copyProperties(foodDetailRest, foodDetailReturn);
                    listFoodDetail.add(foodDetailReturn);
                }
                foodReturn.setFoodDetails(listFoodDetail);
            }
        } else {
            throw new ErrorException("Tên " + name + " đã tồn tại");
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodReturn), HttpStatus.OK);
    }

    //    //save
    //    @PostMapping("/food-old")
    //    @Transactional
    //    public ResponseEntity saveOld(@RequestBody FoodRequestModel foodRequestModel) {
    //        FoodResponseDto foodReturn = new FoodResponseDto();
    //
    //        List<Food> checkExist = foodRepository.findAllByNameAndGroupId(foodRequestModel.getName(), foodRequestModel.getGroupId());
    //        if (checkExist.size() <= 0) {
    //            Food foodParam = new Food();
    //
    //            foodParam.setName(foodRequestModel.getName());
    //            foodParam.setPrice(foodRequestModel.getPrice());
    //            foodParam.setGroupId(foodRequestModel.getGroupId());
    //            foodParam.setImageId(foodRequestModel.getImageId());
    //            foodParam.setDescription(foodRequestModel.getDesciption());
    //
    //            foodParam.setCreatedBy("api");
    //            foodParam.setCreatedDate(LocalDateTime.now());
    //
    //            Food foodRest = foodRepository.save(foodParam);
    //            BeanUtils.copyProperties(foodRest, foodReturn);
    //
    //            Integer foodId = foodRest.getId();
    //            List<ObjectFoodDetail> list = foodRequestModel.getFoodDetails();
    //            List<FoodDetailResponseDto> listFoodDetail = new ArrayList<>();
    //            for (ObjectFoodDetail item : list) {
    //                FoodDetailResponseDto foodDetailReturn = new FoodDetailResponseDto();
    //
    //                FoodDetail foodDetailParam = new FoodDetail();
    //                foodDetailParam.setId(new FoodIdentity(foodId, item.getImageId()));
    //                foodDetailParam.setDesciption(item.getDescription());
    //
    //                foodDetailParam.setCreatedBy("api");
    //                foodDetailParam.setCreatedDate(LocalDateTime.now());
    //                foodDetailParam.setLastModifiedDate(LocalDateTime.now());
    //
    //                FoodDetail foodDetailRest = foodDetailRepository.save(foodDetailParam);
    //                BeanUtils.copyProperties(foodDetailRest, foodDetailReturn);
    //
    //                listFoodDetail.add(foodDetailReturn);
    //            }
    //            foodReturn.setFoodDetails(listFoodDetail);
    //        } else {
    //            throw new ErrorException("name " + foodRequestModel.getName() + " đã tồn tại");
    //        }
    //
    //        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodReturn), HttpStatus.OK);
    //    }

    //delete
    @DeleteMapping("/food/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        Optional<Food> optionalFood = foodRepository.findById(id);
        if (!optionalFood.isPresent()) {
            throw new ErrorException("Không tìm thấy Food với id:= " + id);
        }
        //xóa parent thực chất là update
        //        foodRepository.delete(optionalFood.get());
        Food foodRest = optionalFood.get();
        foodRest.setIsDeleted(1);
        foodRepository.save(foodRest);

        //Xóa detail
        List<FoodDetail> foodDetails = foodDetailRepository.findAllByIdIn(Collections.singletonList(id));
        // lấy ra list FoodId từ list FoodDetail
        List<FoodIdentity> foodIdentitys = foodDetails.stream().map(FoodDetail::getId).collect(Collectors.toList());

        List<Integer> foodIdsInDetail = foodIdentitys.stream().map(FoodIdentity::getFoodId).collect(Collectors.toList());

        if (foodDetails.size() > 0) {
            // delete all by Ids
            foodDetailRepository.deleteAllByFoodId(foodIdsInDetail);
        }

        //        if (foodDetails.size() > 0) {
        //            List<Integer> foodIds = new ArrayList<>();
        //            for (FoodDetail x : foodDetails) {
        //                FoodIdentity foodIdentity = new FoodIdentity(x.getId().getFoodId(), x.getId().getImageId());
        //                foodDetailRepository.deleteAllById(foodIdentity);
        //            }
        //        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }

    //delete all
    @DeleteMapping("/food/delete-ids")
    @Transactional
    public ResponseEntity deleteListFoodIds(@RequestParam List<Integer> ids) {
        List<Food> foods = foodRepository.findAllByIdIn(ids);

        List<Integer> foodIds = foods.stream().map(Food::getId).collect(Collectors.toList());

        //        //xóa parent
        //        foodRepository.deleteInBatch(foods);

        //xóa parent thực chất là update
        //        foodRepository.delete(optionalFood.get());
        foodRepository.updateIsDeleted(foodIds);

        //Xóa detail
        List<FoodDetail> foodDetails = foodDetailRepository.findAllByIdIn(foodIds);
        // lấy ra list FoodId từ list FoodDetail
        List<FoodIdentity> foodIdentitys = foodDetails.stream().map(FoodDetail::getId).collect(Collectors.toList());

        List<Integer> foodIdsInDetail = foodIdentitys.stream().map(FoodIdentity::getFoodId).distinct().collect(Collectors.toList());

        System.out.println("foodId in foodDetails: " + foodIdsInDetail);
        if (foodDetails.size() > 0) {
            // delete all by Ids
            foodDetailRepository.deleteAllByFoodId(foodIdsInDetail);
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, "sucessful"), HttpStatus.OK);
    }
}
