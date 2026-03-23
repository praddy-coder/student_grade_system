package com.smartgrade.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsResponse {
    private List<StudentRanking> rankings;
    private Map<String, SubjectAnalytics> subjectAnalytics;
    private Integer totalStudents;
    private Integer totalPass;
    private Integer totalFail;
    private Double overallPassPercentage;
    private List<StudentRanking> topPerformers;

    public AnalyticsResponse() {}
    public AnalyticsResponse(List<StudentRanking> rankings, Map<String, SubjectAnalytics> subjectAnalytics, Integer totalStudents, Integer totalPass, Integer totalFail, Double overallPassPercentage, List<StudentRanking> topPerformers) {
        this.rankings = rankings;
        this.subjectAnalytics = subjectAnalytics;
        this.totalStudents = totalStudents;
        this.totalPass = totalPass;
        this.totalFail = totalFail;
        this.overallPassPercentage = overallPassPercentage;
        this.topPerformers = topPerformers;
    }

    public static AnalyticsResponseBuilder builder() { return new AnalyticsResponseBuilder(); }

    public List<StudentRanking> getRankings() { return rankings; }
    public Map<String, SubjectAnalytics> getSubjectAnalytics() { return subjectAnalytics; }
    public Integer getTotalStudents() { return totalStudents; }
    public Integer getTotalPass() { return totalPass; }
    public Integer getTotalFail() { return totalFail; }
    public Double getOverallPassPercentage() { return overallPassPercentage; }
    public List<StudentRanking> getTopPerformers() { return topPerformers; }

    public static class SubjectAnalytics {
        private String subjectId;
        private String subjectName;
        private Double averageCt1;
        private Double averageCt2;
        private Double averageTotal;
        private Integer passCount;
        private Integer failCount;
        private Double passPercentage;

        public SubjectAnalytics() {}
        public SubjectAnalytics(String subjectId, String subjectName, Double averageCt1, Double averageCt2, Double averageTotal, Integer passCount, Integer failCount, Double passPercentage) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.averageCt1 = averageCt1;
            this.averageCt2 = averageCt2;
            this.averageTotal = averageTotal;
            this.passCount = passCount;
            this.failCount = failCount;
            this.passPercentage = passPercentage;
        }

        public static SubjectAnalyticsBuilder builder() { return new SubjectAnalyticsBuilder(); }

        public String getSubjectId() { return subjectId; }
        public String getSubjectName() { return subjectName; }
        public Double getAverageCt1() { return averageCt1; }
        public Double getAverageCt2() { return averageCt2; }
        public Double getAverageTotal() { return averageTotal; }
        public Integer getPassCount() { return passCount; }
        public Integer getFailCount() { return failCount; }
        public Double getPassPercentage() { return passPercentage; }

        public static class SubjectAnalyticsBuilder {
            private String subjectId;
            private String subjectName;
            private Double averageCt1;
            private Double averageCt2;
            private Double averageTotal;
            private Integer passCount;
            private Integer failCount;
            private Double passPercentage;
            public SubjectAnalyticsBuilder subjectId(String subjectId) { this.subjectId = subjectId; return this; }
            public SubjectAnalyticsBuilder subjectName(String subjectName) { this.subjectName = subjectName; return this; }
            public SubjectAnalyticsBuilder averageCt1(Double averageCt1) { this.averageCt1 = averageCt1; return this; }
            public SubjectAnalyticsBuilder averageCt2(Double averageCt2) { this.averageCt2 = averageCt2; return this; }
            public SubjectAnalyticsBuilder averageTotal(Double averageTotal) { this.averageTotal = averageTotal; return this; }
            public SubjectAnalyticsBuilder passCount(Integer passCount) { this.passCount = passCount; return this; }
            public SubjectAnalyticsBuilder failCount(Integer failCount) { this.failCount = failCount; return this; }
            public SubjectAnalyticsBuilder passPercentage(Double passPercentage) { this.passPercentage = passPercentage; return this; }
            public SubjectAnalytics build() { return new SubjectAnalytics(subjectId, subjectName, averageCt1, averageCt2, averageTotal, passCount, failCount, passPercentage); }
        }
    }

    public static class AnalyticsResponseBuilder {
        private List<StudentRanking> rankings;
        private Map<String, SubjectAnalytics> subjectAnalytics;
        private Integer totalStudents;
        private Integer totalPass;
        private Integer totalFail;
        private Double overallPassPercentage;
        private List<StudentRanking> topPerformers;
        public AnalyticsResponseBuilder rankings(List<StudentRanking> rankings) { this.rankings = rankings; return this; }
        public AnalyticsResponseBuilder subjectAnalytics(Map<String, SubjectAnalytics> subjectAnalytics) { this.subjectAnalytics = subjectAnalytics; return this; }
        public AnalyticsResponseBuilder totalStudents(Integer totalStudents) { this.totalStudents = totalStudents; return this; }
        public AnalyticsResponseBuilder totalPass(Integer totalPass) { this.totalPass = totalPass; return this; }
        public AnalyticsResponseBuilder totalFail(Integer totalFail) { this.totalFail = totalFail; return this; }
        public AnalyticsResponseBuilder overallPassPercentage(Double overallPassPercentage) { this.overallPassPercentage = overallPassPercentage; return this; }
        public AnalyticsResponseBuilder topPerformers(List<StudentRanking> topPerformers) { this.topPerformers = topPerformers; return this; }
        public AnalyticsResponse build() { return new AnalyticsResponse(rankings, subjectAnalytics, totalStudents, totalPass, totalFail, overallPassPercentage, topPerformers); }
    }
}
