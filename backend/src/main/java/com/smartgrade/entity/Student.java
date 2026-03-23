package com.smartgrade.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "reg_no")
    private String regNo;

    @Column(nullable = false)
    private String name;

    private LocalDate dob;

    @Column(nullable = false)
    private String department;

    private Integer year;

    @Column(name = "class_section")
    private String classSection;

    public Student() {}

    public Student(String regNo, String name, LocalDate dob, String department, Integer year, String classSection) {
        this.regNo = regNo;
        this.name = name;
        this.dob = dob;
        this.department = department;
        this.year = year;
        this.classSection = classSection;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public String getRegNo() { return regNo; }
    public String getName() { return name; }
    public LocalDate getDob() { return dob; }
    public String getDepartment() { return department; }
    public Integer getYear() { return year; }
    public String getClassSection() { return classSection; }

    public static class StudentBuilder {
        private String regNo;
        private String name;
        private LocalDate dob;
        private String department;
        private Integer year;
        private String classSection;

        public StudentBuilder regNo(String regNo) { this.regNo = regNo; return this; }
        public StudentBuilder name(String name) { this.name = name; return this; }
        public StudentBuilder dob(LocalDate dob) { this.dob = dob; return this; }
        public StudentBuilder department(String department) { this.department = department; return this; }
        public StudentBuilder year(Integer year) { this.year = year; return this; }
        public StudentBuilder classSection(String classSection) { this.classSection = classSection; return this; }
        public Student build() { return new Student(regNo, name, dob, department, year, classSection); }
    }
}
