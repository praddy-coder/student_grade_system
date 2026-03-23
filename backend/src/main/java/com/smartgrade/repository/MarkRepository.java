package com.smartgrade.repository;

import com.smartgrade.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query("SELECT m FROM Mark m JOIN FETCH m.subject WHERE m.student.regNo = :regNo")
    List<Mark> findByStudentRegNo(@Param("regNo") String regNo);

    @Query("SELECT m FROM Mark m JOIN FETCH m.student JOIN FETCH m.subject WHERE m.subject.subjectId = :subjectId")
    List<Mark> findBySubjectId(@Param("subjectId") String subjectId);

    @Query("SELECT m FROM Mark m JOIN FETCH m.student JOIN FETCH m.subject")
    List<Mark> findAllWithDetails();

    Optional<Mark> findByStudent_RegNoAndSubject_SubjectId(String regNo, String subjectId);

    @Query("SELECT m FROM Mark m JOIN FETCH m.student JOIN FETCH m.subject WHERE m.student.department = :dept")
    List<Mark> findByDepartment(@Param("dept") String department);

    @Query("SELECT m FROM Mark m JOIN FETCH m.student JOIN FETCH m.subject s WHERE s.staffId = :staffId")
    List<Mark> findBySubjectStaffId(@Param("staffId") String staffId);
}
