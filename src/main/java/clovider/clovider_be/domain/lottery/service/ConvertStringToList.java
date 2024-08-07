package clovider.clovider_be.domain.lottery.service;


import java.util.ArrayList;
import java.util.List;

public class ConvertStringToList {

    public static List<Long> convertStringToList(String str) {
        List<Long> ids = new ArrayList<>();
        if (str != null && !str.isEmpty()) {
            String[] parts = str.split(",");
            for (String part : parts) {
                try {
                    ids.add(Long.parseLong(part.trim()));
                } catch (NumberFormatException e) {
                    // 로깅 또는 예외 처리 (옵션)
                }
            }
        }
        return ids;
    }
}
