package com.smartgrade.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_reg_no", referencedColumnName = "reg_no", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "ct1_marks")
    private Integer ct1Marks;

    @Column(name = "ct2_marks")
    private Integer ct2Marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_updated_by", referencedColumnName = "emp_id")
    private Staff lastUpdatedBy;

    public Mark() {}

    public Mark(Long id, Student student, Subject subject, Integer ct1Marks, Integer ct2Marks, Staff lastUpdatedBy) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.ct1Marks = ct1Marks;
        this.ct2Marks = ct2Marks;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public static MarkBuilder builder() {
        return new MarkBuilder();
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Subject getSubject() { return subject; }
    public Integer getCt1Marks() { return ct1Marks; }
    public Integer getCt2Marks() { return ct2Marks; }
    public Staff getLastUpdatedBy() { return lastUpdatedBy; }

    public void setCt1Marks(Integer ct1Marks) { this.ct1Marks = ct1Marks; }
    public void setCt2Marks(Integer ct2Marks) { this.ct2Marks = ct2Marks; }
    public void setLastUpdatedBy(Staff lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    public static class MarkBuilder {
        private Long id;
        private Student student;
        private Subject subject;
        private Integer ct1Marks;
        private Integer ct2Marks;
        private Staff lastUpdatedBy;

        public MarkBuilder id(Long id) { this.id = id; return this; }
        public MarkBuilder student(Student student) { this.student = student; return this; }
        public MarkBuilder subject(Subject subject) { this.subject = subject; return this; }
        public MarkBuilder ct1Marks(Integer ct1Marks) { this.ct1Marks = ct1Marks; return this; }
        public MarkBuilder ct2Marks(Integer ct2Marks) { this.ct2Marks = ct2Marks; return this; }
        public MarkBuilder lastUpdatedBy(Staff lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; return this; }
        public Mark build() { return new Mark(id, student, subject, ct1Marks, ct2Marks, lastUpdatedBy); }
    }
}
