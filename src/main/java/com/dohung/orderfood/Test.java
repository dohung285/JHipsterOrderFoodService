package com.dohung.orderfood;

import com.dohung.orderfood.domain.Discount;
import com.dohung.orderfood.exception.ErrorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(currentDate);
        System.out.println("strDate: " + strDate);

        try {
            Date dateCurrent = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            System.out.println("dateCurrent: " + dateCurrent);
        } catch (ParseException e) {
            throw new ErrorException(e.getMessage());
        }
    }
}
