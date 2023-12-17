package everymeal.server.global.util;


import java.time.LocalDateTime;

public class TimeFormatUtil {

    static String getTimeFormat(LocalDateTime targetTime) {
        LocalDateTime now = LocalDateTime.now();
        if (targetTime.isAfter(now.minusMinutes(1))) {
            return "방금 전";
        } else if (targetTime.isAfter(now.minusHours(1))) {
            return now.minusMinutes(targetTime.getMinute()).getMinute() + "분 전";
        } else if (targetTime.isAfter(now.minusDays(1))) {
            return now.minusHours(targetTime.getHour()).getHour() + "시간 전";
        } else if (targetTime.isAfter(now.minusDays(6))) {
            return now.minusDays(targetTime.getDayOfWeek().getValue()).getDayOfWeek().getValue()
                    + "일 전";
        } else if (targetTime.isAfter(now.minusDays(27))) {
            return now.minusWeeks(targetTime.getDayOfMonth() / 7).getDayOfMonth() / 7 + "주 전";
        } else {
            return targetTime.getMonthValue() + "월 " + targetTime.getDayOfMonth() + "일";
        }
    }
}
