package com.dohung.orderfood.service;

import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.repository.DiscountRepository;
import com.dohung.orderfood.repository.FoodRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void updateFoodAPI() {
        System.out.println("======================= CHECK xem đã đến ngày update giảm giá hay chưa? =========================");

        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(currentDate);
        System.out.println("strDate: " + strDate);

        List<Discount> discounts = null;
        try {
            //            discounts = discountRepository.findAllByEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));
            discounts = discountRepository.findAllByEndDateLessThanEqual(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));

            List<Integer> discountIds = discounts.stream().map(Discount::getId).collect(Collectors.toList());

            System.out.println("Danh sách mã giảm giá hết hạn: " + discountIds);
            // xóa đi mã giảm giá đến ngày hết hạn!
            discountRepository.updateDiscountHasEndDateExpireDate(discountIds);

            List<Food> foods = foodRepository.getAllFoodsWithDiscountIds(discountIds);
            List<Integer> foodIds = foods.stream().map(Food::getId).collect(Collectors.toList());
            System.out.println("Danh sách mã món ăn có mã giảm giá hết hạn: " + foodIds);

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
