package com.smartgrade.dto;

public class MarkDTO {
    private Long id;
    private String studentRegNo;
    private String studentName;
    private String subjectId;
    private String subjectName;
    private Integer ct1Marks;
    private Integer ct2Marks;
    private Double totalPercentage;

    public MarkDTO() {}
    public MarkDTO(Long id, String studentRegNo, String studentName, String subjectId, String subjectName, Integer ct1Marks, Integer ct2Marks, Double totalPercentage) {
        this.id = id;
        this.studentRegNo = studentRegNo;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.ct1Marks = ct1Marks;
        this.ct2Marks = ct2Marks;
        this.totalPercentage = totalPercentage;
    }

    public static MarkDTOBuilder builder() { return new MarkDTOBuilder(); }

    public Long getId() { return id; }
    public String getStudentRegNo() { return studentRegNo; }
    public String getStudentName() { return studentName; }
    public String getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public Integer getCt1Marks() { return ct1Marks; }
    public Integer getCt2Marks() { return ct2Marks; }
    public Double getTotalPercentage() { return totalPercentage; }

    public static class MarkDTOBuilder {
        private Long id;
        private String studentRegNo;
        private String studentName;
        private String subjectId;
        private String subjectName;
        private Integer ct1Marks;
        private Integer ct2Marks;
        private Double totalPercentage;
        public MarkDTOBuilder id(Long id) { this.id = id; return this; }
        public MarkDTOBuilder studentRegNo(String studentRegNo) { this.studentRegNo = studentRegNo; return this; }
        public MarkDTOBuilder studentName(String studentName) { this.studentName = studentName; return this; }
        public MarkDTOBuilder subjectId(String subjectId) { this.subjectId = subjectId; return this; }
        public MarkDTOBuilder subjectName(String subjectName) { this.subjectName = subjectName; return this; }
        public MarkDTOBuilder ct1Marks(Integer ct1Marks) { this.ct1Marks = ct1Marks; return this; }
        public MarkDTOBuilder ct2Marks(Integer ct2Marks) { this.ct2Marks = ct2Marks; return this; }
        public MarkDTOBuilder totalPercentage(Double totalPercentage) { this.totalPercentage = totalPercentage; return this; }
        public MarkDTO build() { return new MarkDTO(id, studentRegNo, studentName, subjectId, subjectName, ct1Marks, ct2Marks, totalPercentage); }
    }
}
