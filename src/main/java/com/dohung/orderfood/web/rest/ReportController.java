package com.dohung.orderfood.web.rest;

import com.dohung.orderfood.common.ResponseData;
import com.dohung.orderfood.constant.StringConstant;
import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.domain.UserPermission;
import com.dohung.orderfood.exception.ErrorException;
import com.dohung.orderfood.repository.BillRepository;
import com.dohung.orderfood.repository.FoodRepository;
import com.dohung.orderfood.web.rest.response.*;
import io.swagger.models.auth.In;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/report/revenueOfYear")
    public ResponseEntity getTotalMoneyOfTwelve() {
        List<BarChartObjectResponseDto> listReturn = new ArrayList<>();

        BarChartObjectResponseDto[] objectInit = new BarChartObjectResponseDto[] {
            new BarChartObjectResponseDto(new BigDecimal(0), 1),
            new BarChartObjectResponseDto(new BigDecimal(0), 2),
            new BarChartObjectResponseDto(new BigDecimal(0), 3),
            new BarChartObjectResponseDto(new BigDecimal(0), 4),
            new BarChartObjectResponseDto(new BigDecimal(0), 5),
            new BarChartObjectResponseDto(new BigDecimal(0), 6),
            new BarChartObjectResponseDto(new BigDecimal(0), 7),
            new BarChartObjectResponseDto(new BigDecimal(0), 8),
            new BarChartObjectResponseDto(new BigDecimal(0), 9),
            new BarChartObjectResponseDto(new BigDecimal(0), 10),
            new BarChartObjectResponseDto(new BigDecimal(0), 11),
            new BarChartObjectResponseDto(new BigDecimal(0), 12),
        };
        listReturn = Arrays.asList(objectInit);
        //        System.out.println("Init: "+listReturn);

        List<Tuple> listResult = billRepository.getTotalMoneyOfTwelve();
        //        System.out.println("Tuple: " + listResult);

        List<BarChartObjectResponseDto> listResultAPI = listResult
            .stream()
            .map(x -> new BarChartObjectResponseDto(x.get(0, BigDecimal.class), x.get(1, Integer.class)))
            .collect(Collectors.toList());

        if (listResultAPI.size() > 0) {
            listReturn.forEach(
                x -> listResultAPI.stream().filter(y -> x.getMonth() == y.getMonth()).forEach(y -> x.setTotal(y.getTotal()))
            );
        }
        String[] arrayLabels = new String[] {
            "Tháng 1",
            "Tháng 2",
            "Tháng 3",
            "Tháng 4",
            "Tháng 5",
            "Tháng 6",
            "Tháng 7",
            "Tháng 8",
            "Tháng 9",
            "Tháng 10",
            "Tháng 11",
            "Tháng 12",
        };
        List<String> labels = Arrays.asList(arrayLabels);

        List<BigDecimal> data = listReturn.stream().map(BarChartObjectResponseDto::getTotal).collect(Collectors.toList());

        ObjectDatasetBarChart objectDatasetBarChart = new ObjectDatasetBarChart(data, "Tổng doanh thu", "#42A5F5");
        List<ObjectDatasetBarChart> objectDatasetBarCharts = new ArrayList<>();
        objectDatasetBarCharts.add(objectDatasetBarChart);

        ObjectBarChartData objectBarChartData = new ObjectBarChartData(labels, objectDatasetBarCharts);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, objectBarChartData), HttpStatus.OK);
    }

    @GetMapping("/report/countBuy")
    public ResponseEntity getCountBuyOfAllFood() {
        List<PieChartObjectResponseDto> listReturn = new ArrayList<>();

        List<Food> listResultAPI = foodRepository.findAll();
        System.out.println("listResultAPI" + listResultAPI);
        if (listResultAPI.size() > 0) {
            for (Food x : listResultAPI) {
                listReturn.add(new PieChartObjectResponseDto(x.getName(), new BigInteger(String.valueOf(0))));
            }
            System.out.println("listReturn: " + listReturn);
        }
        List<Tuple> listResult = foodRepository.getCountBuyOfAllFood();
        System.out.println("listResult: " + listResult);

        List<PieChartObjectResponseDto> listResultCountAPI = listResult
            .stream()
            .map(x -> new PieChartObjectResponseDto(x.get(0, String.class), x.get(1, BigInteger.class)))
            .collect(Collectors.toList());

        if (listResultCountAPI.size() > 0) {
            listReturn.forEach(
                x -> listResultCountAPI.stream().filter(y -> x.getName().equals(y.getName())).forEach(y -> x.setNumber(y.getNumber()))
            );
        }

        List<String> labels = listReturn.stream().map(PieChartObjectResponseDto::getName).collect(Collectors.toList());

        List<BigInteger> data = listReturn.stream().map(PieChartObjectResponseDto::getNumber).collect(Collectors.toList());

        String[] arrayColor = new String[] {
            "#42A5F5",
            "#66BB6A",
            "#FFA726",
            "#FF0000",
            "#00FF00",
            "#55acee",
            "#dc4e41",
            "#bd081c",
            "#0077b5",
            "#000000",
            "#3399ff",
            "#eb4924",
            "#00405d",
            "#45668e",
            "#f67c1a",
            "#338d11",
            "#ff4500",
            "#000000",
            "#ce1126",
            "#ef4056",
            "#026466",
            "#cfdc00",
            "#ed812b",
            "#43d854",
            "#ff6400",
            "#f57d00",
            "#000000",
            "#ff9900",
            "#410093",
            "#ea4335",
            "#00b488",
            "#2dbe60",
            "#00405d",
            "#f07355",
            "#00c300",
            "#e12828",
            "#00405d",
            "#00405d",
            "#00405d",
            "#3f729b",
            "#cd201f",
            "#1ab7ea",
            "#00405d",
            "#1769ff",
            "#00405d",
            "#00aff0",
            "#fffa37",
        };

        List<String> backgroundColor = Arrays.asList(arrayColor);

        ObjectDatasetPieChart objectDatasetPieChart = new ObjectDatasetPieChart(data, backgroundColor);
        List<ObjectDatasetPieChart> objectDatasetPieCharts = new ArrayList<>();
        objectDatasetPieCharts.add(objectDatasetPieChart);

        ObjectPieChartData objectPieChartData = new ObjectPieChartData(labels, objectDatasetPieCharts);

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, objectPieChartData), HttpStatus.OK);
    }

    @GetMapping("/report/total")
    public ResponseEntity getTotalByMonthAndYear(@RequestParam("month") Integer month, @RequestParam("year") Integer year) {
        List<ReportTotalObjectResponseDto> listReturn = new ArrayList<>();
        List<Food> listResultFoodAPI = foodRepository.findAll();

        System.out.println("listResultFoodAPI" + listResultFoodAPI);
        if (listResultFoodAPI.size() > 0) {
            for (Food x : listResultFoodAPI) {
                listReturn.add(
                    new ReportTotalObjectResponseDto(
                        x.getId(),
                        x.getName(),
                        new BigInteger(String.valueOf(month)),
                        new BigDecimal("0"),
                        x.getIsDeleted()
                    )
                );
            }
            System.out.println("listReturn: " + listReturn);
        }

        List<Tuple> listResult = billRepository.getTotalMoneyByMonthAndYear(month, year);

        List<ReportTotalObjectResponseDto> listResultAPI = listResult
            .stream()
            .map(
                x ->
                    new ReportTotalObjectResponseDto(
                        x.get(0, Integer.class),
                        x.get(1, String.class),
                        x.get(2, BigInteger.class),
                        x.get(3, BigDecimal.class),
                        x.get(4, Integer.class)
                    )
            )
            .collect(Collectors.toList());
        System.out.println("listResultAPI: " + listResultAPI);

        //update lai object return
        if (listResultAPI.size() > 0) {
            listReturn.forEach(x -> listResultAPI.stream().filter(y -> x.getId() == y.getId()).forEach(y -> x.setTotal(y.getTotal())));
        }

        return new ResponseEntity(new ResponseData(StringConstant.iSUCCESS, listReturn), HttpStatus.OK);
    }
}
