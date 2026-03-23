package com.smartgrade.repository;

import com.smartgrade.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByDepartment(String department);
    List<Student> findByDepartmentAndYear(String department, Integer year);
}
