package clovider.clovider_be.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    public static String formattedDateTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH");

        return time.format(formatter);
    }

    public static String formattedDate(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return time.format(formatter);
    }

    public static int formattedRemain(LocalDateTime now, LocalDateTime end) {
        return (int) ChronoUnit.DAYS.between(now, end);
    }
}
