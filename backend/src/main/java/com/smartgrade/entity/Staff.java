package com.smartgrade.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @Column(name = "emp_id")
    private String empId;

    @Column(nullable = false)
    private String name;

    private LocalDate dob;

    @Column(nullable = false)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole role;

    public Staff() {}

    public Staff(String empId, String name, LocalDate dob, String department, StaffRole role) {
        this.empId = empId;
        this.name = name;
        this.dob = dob;
        this.department = department;
        this.role = role;
    }

    public static StaffBuilder builder() {
        return new StaffBuilder();
    }

    public String getEmpId() { return empId; }
    public String getName() { return name; }
    public LocalDate getDob() { return dob; }
    public String getDepartment() { return department; }
    public StaffRole getRole() { return role; }

    public enum StaffRole {
        STAFF, CC, HOD
    }

    public static class StaffBuilder {
        private String empId;
        private String name;
        private LocalDate dob;
        private String department;
        private StaffRole role;

        public StaffBuilder empId(String empId) { this.empId = empId; return this; }
        public StaffBuilder name(String name) { this.name = name; return this; }
        public StaffBuilder dob(LocalDate dob) { this.dob = dob; return this; }
        public StaffBuilder department(String department) { this.department = department; return this; }
        public StaffBuilder role(StaffRole role) { this.role = role; return this; }
        public Staff build() { return new Staff(empId, name, dob, department, role); }
    }
}
