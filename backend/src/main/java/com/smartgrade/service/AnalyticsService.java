package com.smartgrade.service;

import com.smartgrade.dto.AnalyticsResponse;
import com.smartgrade.dto.StudentRanking;
import com.smartgrade.entity.Mark;
import com.smartgrade.repository.MarkRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final MarkRepository markRepository;

    public AnalyticsService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public AnalyticsResponse getAnalytics(String department) {
        List<Mark> allMarks;
        if (department != null && !department.isEmpty()) {
            allMarks = markRepository.findByDepartment(department);
        } else {
            allMarks = markRepository.findAllWithDetails();
        }

        // Group marks by student
        Map<String, List<Mark>> studentMarks = allMarks.stream()
                .collect(Collectors.groupingBy(m -> m.getStudent().getRegNo()));

        // Build student rankings
        List<StudentRanking> rankings = new ArrayList<>();
        for (Map.Entry<String, List<Mark>> entry : studentMarks.entrySet()) {
            List<Mark> marks = entry.getValue();
            Mark firstMark = marks.get(0);

            List<StudentRanking.SubjectMark> subjectMarks = marks.stream()
                    .map(m -> {
                        int ct1 = m.getCt1Marks() != null ? m.getCt1Marks() : 0;
                        int ct2 = m.getCt2Marks() != null ? m.getCt2Marks() : 0;
                        int total = ct1 + ct2;
                        return StudentRanking.SubjectMark.builder()
                                .subjectId(m.getSubject().getSubjectId())
                                .subjectName(m.getSubject().getSubjectName())
                                .ct1Marks(ct1)
                                .ct2Marks(ct2)
                                .total(total)
                                .percentage(Math.round((total / 120.0) * 100 * 100.0) / 100.0)
                                .build();
                    })
                    .collect(Collectors.toList());

            int totalMarks = subjectMarks.stream().mapToInt(StudentRanking.SubjectMark::getTotal).sum();
            int maxTotal = marks.size() * 120; // 60 per CT * 2 CTs per subject
            double overallPct = maxTotal > 0 ? Math.round((totalMarks * 100.0 / maxTotal) * 100.0) / 100.0 : 0;

            rankings.add(StudentRanking.builder()
                    .regNo(entry.getKey())
                    .name(firstMark.getStudent().getName())
                    .department(firstMark.getStudent().getDepartment())
                    .totalMarks(totalMarks)
                    .overallPercentage(overallPct)
                    .subjectMarks(subjectMarks)
                    .build());
        }

        // Sort by total marks desc and assign ranks
        rankings.sort((a, b) -> b.getTotalMarks().compareTo(a.getTotalMarks()));
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        // Subject-wise analytics
        Map<String, List<Mark>> subjectGroups = allMarks.stream()
                .collect(Collectors.groupingBy(m -> m.getSubject().getSubjectId()));

        Map<String, AnalyticsResponse.SubjectAnalytics> subjectAnalytics = new LinkedHashMap<>();
        int totalFail = 0;
        int totalPass = 0;

        for (Map.Entry<String, List<Mark>> entry : subjectGroups.entrySet()) {
            List<Mark> marks = entry.getValue();
            String subjectName = marks.get(0).getSubject().getSubjectName();

            double avgCt1 = marks.stream()
                    .mapToInt(m -> m.getCt1Marks() != null ? m.getCt1Marks() : 0).average().orElse(0);
            double avgCt2 = marks.stream()
                    .mapToInt(m -> m.getCt2Marks() != null ? m.getCt2Marks() : 0).average().orElse(0);

            int passCount = 0;
            int failCount = 0;
            for (Mark m : marks) {
                int ct1 = m.getCt1Marks() != null ? m.getCt1Marks() : 0;
                int ct2 = m.getCt2Marks() != null ? m.getCt2Marks() : 0;
                // Pass threshold: 30 marks per CT (50% of 60)
                if (ct1 >= 30 && ct2 >= 30) {
                    passCount++;
                } else {
                    failCount++;
                }
            }
            totalPass += passCount;
            totalFail += failCount;

            subjectAnalytics.put(entry.getKey(), AnalyticsResponse.SubjectAnalytics.builder()
                    .subjectId(entry.getKey())
                    .subjectName(subjectName)
                    .averageCt1(Math.round(avgCt1 * 100.0) / 100.0)
                    .averageCt2(Math.round(avgCt2 * 100.0) / 100.0)
                    .averageTotal(Math.round((avgCt1 + avgCt2) * 100.0) / 100.0)
                    .passCount(passCount)
                    .failCount(failCount)
                    .passPercentage(marks.isEmpty() ? 0 : Math.round(passCount * 100.0 / marks.size() * 100.0) / 100.0)
                    .build());
        }

        int totalStudents = studentMarks.size();
        List<StudentRanking> topPerformers = rankings.stream().limit(5).collect(Collectors.toList());

        return AnalyticsResponse.builder()
                .rankings(rankings)
                .subjectAnalytics(subjectAnalytics)
                .totalStudents(totalStudents)
                .totalPass(totalPass)
                .totalFail(totalFail)
                .overallPassPercentage(
                    (totalPass + totalFail) > 0
                        ? Math.round(totalPass * 100.0 / (totalPass + totalFail) * 100.0) / 100.0
                        : 0.0)
                .topPerformers(topPerformers)
                .build();
    }
}
