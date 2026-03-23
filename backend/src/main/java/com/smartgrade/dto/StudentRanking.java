package com.smartgrade.dto;

import java.util.List;

public class StudentRanking {
    private String regNo;
    private String name;
    private String department;
    private Integer totalMarks;
    private Double overallPercentage;
    private Integer rank;
    private List<SubjectMark> subjectMarks;

    public StudentRanking() {}
    public StudentRanking(String regNo, String name, String department, Integer totalMarks, Double overallPercentage, Integer rank, List<SubjectMark> subjectMarks) {
        this.regNo = regNo;
        this.name = name;
        this.department = department;
        this.totalMarks = totalMarks;
        this.overallPercentage = overallPercentage;
        this.rank = rank;
        this.subjectMarks = subjectMarks;
    }

    public static StudentRankingBuilder builder() { return new StudentRankingBuilder(); }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }
    public Double getOverallPercentage() { return overallPercentage; }
    public void setOverallPercentage(Double overallPercentage) { this.overallPercentage = overallPercentage; }
    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
    public List<SubjectMark> getSubjectMarks() { return subjectMarks; }
    public void setSubjectMarks(List<SubjectMark> subjectMarks) { this.subjectMarks = subjectMarks; }

    public static class SubjectMark {
        private String subjectId;
        private String subjectName;
        private Integer ct1Marks;
        private Integer ct2Marks;
        private Integer total;
        private Double percentage;

        public SubjectMark() {}
        public SubjectMark(String subjectId, String subjectName, Integer ct1Marks, Integer ct2Marks, Integer total, Double percentage) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.ct1Marks = ct1Marks;
            this.ct2Marks = ct2Marks;
            this.total = total;
            this.percentage = percentage;
        }
        public static SubjectMarkBuilder builder() { return new SubjectMarkBuilder(); }
        public String getSubjectId() { return subjectId; }
        public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Integer getCt1Marks() { return ct1Marks; }
        public void setCt1Marks(Integer ct1Marks) { this.ct1Marks = ct1Marks; }
        public Integer getCt2Marks() { return ct2Marks; }
        public void setCt2Marks(Integer ct2Marks) { this.ct2Marks = ct2Marks; }
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        public Double getPercentage() { return percentage; }
        public void setPercentage(Double percentage) { this.percentage = percentage; }

        public static class SubjectMarkBuilder {
            private String subjectId;
            private String subjectName;
            private Integer ct1Marks;
            private Integer ct2Marks;
            private Integer total;
            private Double percentage;
            public SubjectMarkBuilder subjectId(String subjectId) { this.subjectId = subjectId; return this; }
            public SubjectMarkBuilder subjectName(String subjectName) { this.subjectName = subjectName; return this; }
            public SubjectMarkBuilder ct1Marks(Integer ct1Marks) { this.ct1Marks = ct1Marks; return this; }
            public SubjectMarkBuilder ct2Marks(Integer ct2Marks) { this.ct2Marks = ct2Marks; return this; }
            public SubjectMarkBuilder total(Integer total) { this.total = total; return this; }
            public SubjectMarkBuilder percentage(Double percentage) { this.percentage = percentage; return this; }
            public SubjectMark build() { return new SubjectMark(subjectId, subjectName, ct1Marks, ct2Marks, total, percentage); }
        }
    }

    public static class StudentRankingBuilder {
        private String regNo;
        private String name;
        private String department;
        private Integer totalMarks;
        private Double overallPercentage;
        private Integer rank;
        private List<SubjectMark> subjectMarks;
        public StudentRankingBuilder regNo(String regNo) { this.regNo = regNo; return this; }
        public StudentRankingBuilder name(String name) { this.name = name; return this; }
        public StudentRankingBuilder department(String department) { this.department = department; return this; }
        public StudentRankingBuilder totalMarks(Integer totalMarks) { this.totalMarks = totalMarks; return this; }
        public StudentRankingBuilder overallPercentage(Double overallPercentage) { this.overallPercentage = overallPercentage; return this; }
        public StudentRankingBuilder rank(Integer rank) { this.rank = rank; return this; }
        public StudentRankingBuilder subjectMarks(List<SubjectMark> subjectMarks) { this.subjectMarks = subjectMarks; return this; }
        public StudentRanking build() { return new StudentRanking(regNo, name, department, totalMarks, overallPercentage, rank, subjectMarks); }
    }
}
