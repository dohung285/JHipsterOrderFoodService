package com.dohung.orderfood.service;

import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.DiscountRepository;
import com.dohung.orderfood.repository.FoodRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FoodSchedulingService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private FoodRepository foodRepository;

    //    @Scheduled(fixedRate = 5000)
    @Transactional
    public void updateFoodAPI() {
        //        discountRepository.updateFood();

        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(currentDate);
        System.out.println("strDate: " + strDate);

        List<Discount> discounts = null;
        try {
            discounts = discountRepository.findAllByEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));

            List<Integer> discountIds = discounts.stream().map(Discount::getId).collect(Collectors.toList());

            System.out.println(discountIds);

            List<Food> foods = foodRepository.getAllFoodsWithDiscountIds(discountIds);
            List<Integer> foodIds = foods.stream().map(Food::getId).collect(Collectors.toList());
            System.out.println(foodIds);

            //update
            foodRepository.updateDiscountIdInFood(foodIds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //        System.out.println("discounts: " + discounts);

        //        List<Food> list = foodRepository.findAll();
        //        System.out.println("list: "+list);
        //        System.out.println("==>updateFoodAPI()");
    }
}
