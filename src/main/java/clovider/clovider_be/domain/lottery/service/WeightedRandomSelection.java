package clovider.clovider_be.domain.lottery.service;

import java.util.*;

// 추첨 진행
public class WeightedRandomSelection {

    public static List<Map<String, Object>> weightedRandomSelection(List<Map<String, Object>> applicants, int numSelected) {
        // 총 가중치 계산
        double totalWeight = applicants.stream()
                .mapToDouble(applicant -> ((Number) applicant.get("weight")).doubleValue())
                .sum();

        // 누적 가중치 계산
        List<Double> cumulativeWeights = new ArrayList<>();
        double cumulativeSum = 0;
        for (Map<String, Object> applicant : applicants) {
            cumulativeSum += ((Number) applicant.get("weight")).doubleValue();
            cumulativeWeights.add(cumulativeSum);
        }

        // 추첨된 신청자 집합
        Set<Integer> selectedApplicantsSet = new HashSet<>();
        List<Map<String, Object>> selectedApplicantsList = new ArrayList<>();

        Random random = new Random();

        // 추첨 진행
        while (selectedApplicantsSet.size() < numSelected) {
            double value = random.nextDouble() * totalWeight;
            for (int i = 0; i < cumulativeWeights.size(); i++) {
                if (value <= cumulativeWeights.get(i)) {
                    if (!selectedApplicantsSet.contains(((Number) applicants.get(i).get("id")).intValue())) {
                        selectedApplicantsSet.add(((Number) applicants.get(i).get("id")).intValue());
                        selectedApplicantsList.add(applicants.get(i));
                    }
                    break;
                }
            }
        }

        return selectedApplicantsList;
    }
}