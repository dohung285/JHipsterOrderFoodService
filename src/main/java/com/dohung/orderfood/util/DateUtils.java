package com.dohung.orderfood.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.util.ObjectUtils;

public class DateUtils {

    public static LocalDateTime getDateTimeStartOfDay() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);

        return startOfDay;
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        if (ObjectUtils.isEmpty(localDateTime)) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return localDateTime.format(formatter);
    }
}
