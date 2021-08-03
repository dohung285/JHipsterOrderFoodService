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
import com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto;
import com.dohung.orderfood.web.rest.response.FoodDetailResponseDto;
import com.dohung.orderfood.web.rest.response.FoodResponseDto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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

    @GetMapping("/currentAccessToken")
    public String getAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String name = oauthToken.getName();
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(registrationId, name);

            if (null == client) {
                throw new OAuth2AuthorizationException(new OAuth2Error("access_denied", "The token is expired", null));
            }
            OAuth2AccessToken accessToken = client.getAccessToken();

            if (accessToken != null) {
                String tokenType = accessToken.getTokenType().getValue();
                String accessTokenValue = accessToken.getTokenValue();
                return accessTokenValue;
            }
        }
        return null;
    }

    //    @GetMapping("/currentAccessToken")
    //    public Optional<String> getAuthorizationHeader() {
    //        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //        if (authentication instanceof OAuth2AuthenticationToken) {
    //            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    //            String name = oauthToken.getName();
    //            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
    //            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
    //                registrationId,
    //                name);
    //
    //            if (null == client) {
    //                throw new OAuth2AuthorizationException(new OAuth2Error("access_denied", "The token is expired", null));
    //            }
    //            OAuth2AccessToken accessToken = client.getAccessToken();
    //
    //
    //            if (accessToken != null) {
    //                String tokenType = accessToken.getTokenType().getValue();
    //                String accessTokenValue = accessToken.getTokenValue();
    //                if (isExpired(accessToken)) {
    //                    System.out.println("AccessToken expired, refreshing automatically");
    //                    accessTokenValue = refreshToken(client, oauthToken);
    //                    if (null == accessTokenValue) {
    //                        SecurityContextHolder.getContext().setAuthentication(null);
    //                        throw new OAuth2AuthorizationException(new OAuth2Error("access_denied", "The token is expired", null));
    //                    }
    //                }
    //                String authorizationHeaderValue = String.format("%s %s", tokenType, accessTokenValue);
    //                return Optional.of(authorizationHeaderValue);
    //            }
    //
    //        } else if (authentication instanceof JwtAuthenticationToken) {
    //            JwtAuthenticationToken accessToken = (JwtAuthenticationToken) authentication;
    //            String tokenType = (String) accessToken.getToken().getClaims().get("typ");
    //            String tokenValue = accessToken.getToken().getTokenValue();
    //            String authorizationHeaderValue = String.format("%s %s", tokenType, tokenValue);
    //            return Optional.of(authorizationHeaderValue);
    //        }
    //        return Optional.empty();
    //    }
    //
    //    private String refreshToken(OAuth2AuthorizedClient client, OAuth2AuthenticationToken oauthToken) {
    //        OAuth2AccessTokenResponse atr = refreshTokenClient(client);
    //        if (atr == null || atr.getAccessToken() == null) {
    //            System.out.println("Failed to refresh token for ${currentUser.name}");
    //            return null;
    //        }
    //
    //        OAuth2RefreshToken refreshToken = atr.getRefreshToken() != null ? atr.getRefreshToken() : client.getRefreshToken();
    //        OAuth2AuthorizedClient updatedClient = new OAuth2AuthorizedClient(
    //            client.getClientRegistration(),
    //            client.getPrincipalName(),
    //            atr.getAccessToken(),
    //            refreshToken
    //        );
    //
    //        clientService.saveAuthorizedClient(updatedClient, oauthToken);
    //        return atr.getAccessToken().getTokenValue();
    //    }
    //
    //    private OAuth2AccessTokenResponse refreshTokenClient(OAuth2AuthorizedClient currentClient) {
    //
    //        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
    //        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.REFRESH_TOKEN.getValue());
    //        formParameters.add(OAuth2ParameterNames.REFRESH_TOKEN, currentClient.getRefreshToken().getTokenValue());
    //        formParameters.add(OAuth2ParameterNames.CLIENT_ID, currentClient.getClientRegistration().getClientId());
    //        RequestEntity requestEntity = RequestEntity
    //            .post(URI.create(currentClient.getClientRegistration().getProviderDetails().getTokenUri()))
    //            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    //            .body(formParameters);
    //        try {
    //            RestTemplate r = restTemplate(currentClient.getClientRegistration().getClientId(), currentClient.getClientRegistration().getClientSecret());
    //            ResponseEntity<OAuthIdpTokenResponseDTO> responseEntity = r.exchange(requestEntity, OAuthIdpTokenResponseDTO.class);
    //            return toOAuth2AccessTokenResponse(responseEntity.getBody());
    //        } catch (OAuth2AuthorizationException e) {
    //            System.out.println("Unable to refresh token ${e.error.errorCode}");
    //            throw new OAuth2AuthenticationException(e.getError(), e);
    //        }
    //    }
    //
    //    private OAuth2AccessTokenResponse toOAuth2AccessTokenResponse(OAuthIdpTokenResponseDTO oAuthIdpResponse) {
    //        Map<String, Object> additionalParameters = new HashMap<>();
    //        additionalParameters.put("id_token", oAuthIdpResponse.getIdToken());
    //        additionalParameters.put("not-before-policy", oAuthIdpResponse.getNotBefore());
    //        additionalParameters.put("refresh_expires_in", oAuthIdpResponse.getRefreshExpiresIn());
    //        additionalParameters.put("session_state", oAuthIdpResponse.getSessionState());
    //        return OAuth2AccessTokenResponse.withToken(oAuthIdpResponse.getAccessToken())
    //            .expiresIn(oAuthIdpResponse.getExpiresIn())
    //            .refreshToken(oAuthIdpResponse.getRefreshToken())
    //            .scopes(Pattern.compile("\\s").splitAsStream(oAuthIdpResponse.getScope()).collect(Collectors.toSet()))
    //            .tokenType(OAuth2AccessToken.TokenType.BEARER)
    //            .additionalParameters(additionalParameters)
    //            .build();
    //    }
    //
    //    private RestTemplate restTemplate(String clientId, String clientSecret) {
    //        return restTemplateBuilder
    //            .additionalMessageConverters(
    //                new FormHttpMessageConverter(),
    //                new OAuth2AccessTokenResponseHttpMessageConverter())
    //            .errorHandler(new OAuth2ErrorResponseErrorHandler())
    //            .basicAuthentication(clientId, clientSecret)
    //            .build();
    //    }
    //
    //    private boolean isExpired(OAuth2AccessToken accessToken) {
    //        Instant now = Instant.now();
    //        Instant expiresAt = accessToken.getExpiresAt();
    //        return now.isAfter(expiresAt.minus(Duration.ofMinutes(1L)));
    //    }

    //    @GetMapping("/currentAccessToken")
    //    public String accessToken() {
    //        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //        String token =authentication.getPrincipal().toString();
    //        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
    //            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    //            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
    //            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId,
    //                oauthToken.getName());
    //            System.out.println("client.getAccessToken().getTokenValue(): " + client.getAccessToken().getTokenValue());
    //            return client.getAccessToken().getTokenValue();
    //        }
    //
    ////        OAuth2AuthorizedClient client =
    ////            clientService.loadAuthorizedClient(
    ////                oauthToken.getAuthorizedClientRegistrationId(),
    ////                oauthToken.getName());
    ////
    ////        String accessToken = client.getAccessToken().getTokenValue();
    //
    //        System.out.println("client.getAccessToken().getTokenValue() null");
    //        return null;
    //    }

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
            throw new ErrorException("name " + name + " đã tồn tại");
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, foodReturn), HttpStatus.OK);
    }

    //save
    @PostMapping("/food-old")
    @Transactional
    public ResponseEntity saveOld(@RequestBody FoodRequestModel foodRequestModel) {
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
