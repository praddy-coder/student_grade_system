package com.smartgrade.repository;

import com.smartgrade.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findByDepartment(String department);
}
