package com.smartgrade.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @Column(name = "subject_id")
    private String subjectId;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "class_id")
    private String classId;

    @Column(nullable = false)
    private String department;

    @Column(name = "staff_id")
    private String staffId;

    public Subject() {}

    public Subject(String subjectId, String subjectName, String classId, String department, String staffId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.classId = classId;
        this.department = department;
        this.staffId = staffId;
    }

    public static SubjectBuilder builder() {
        return new SubjectBuilder();
    }

    public String getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public String getClassId() { return classId; }
    public String getDepartment() { return department; }
    public String getStaffId() { return staffId; }

    public static class SubjectBuilder {
        private String subjectId;
        private String subjectName;
        private String classId;
        private String department;
        private String staffId;

        public SubjectBuilder subjectId(String subjectId) { this.subjectId = subjectId; return this; }
        public SubjectBuilder subjectName(String subjectName) { this.subjectName = subjectName; return this; }
        public SubjectBuilder classId(String classId) { this.classId = classId; return this; }
        public SubjectBuilder department(String department) { this.department = department; return this; }
        public SubjectBuilder staffId(String staffId) { this.staffId = staffId; return this; }
        public Subject build() { return new Subject(subjectId, subjectName, classId, department, staffId); }
    }
}
