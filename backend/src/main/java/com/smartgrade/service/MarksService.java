package com.smartgrade.service;

import com.smartgrade.dto.MarkDTO;
import com.smartgrade.dto.MarkUpdateRequest;
import com.smartgrade.entity.Mark;
import com.smartgrade.entity.Staff;
import com.smartgrade.repository.MarkRepository;
import com.smartgrade.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarksService {

    private final MarkRepository markRepository;
    private final StaffRepository staffRepository;

    public MarksService(MarkRepository markRepository, StaffRepository staffRepository) {
        this.markRepository = markRepository;
        this.staffRepository = staffRepository;
    }

    public List<MarkDTO> getStudentMarks(String studentId) {
        return markRepository.findByStudentRegNo(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MarkDTO> getAllMarks() {
        return markRepository.findAllWithDetails().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MarkDTO> getMarksByStaff(String staffId) {
        return markRepository.findBySubjectStaffId(staffId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MarkDTO> getMarksByDepartment(String department) {
        return markRepository.findByDepartment(department).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MarkDTO updateMark(MarkUpdateRequest request, String empId) {
        // Validate marks range
        if (request.getMarks() < 0 || request.getMarks() > 60) {
            throw new RuntimeException("Marks must be between 0 and 60");
        }

        Mark mark = markRepository.findByStudent_RegNoAndSubject_SubjectId(
                request.getStudentId(), request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Mark record not found"));

        Staff staff = staffRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if ("CT1".equalsIgnoreCase(request.getExam())) {
            mark.setCt1Marks(request.getMarks());
        } else if ("CT2".equalsIgnoreCase(request.getExam())) {
            mark.setCt2Marks(request.getMarks());
        } else {
            throw new RuntimeException("Exam must be CT1 or CT2");
        }

        mark.setLastUpdatedBy(staff);
        Mark saved = markRepository.save(mark);
        return toDTO(saved);
    }

    private MarkDTO toDTO(Mark mark) {
        int ct1 = mark.getCt1Marks() != null ? mark.getCt1Marks() : 0;
        int ct2 = mark.getCt2Marks() != null ? mark.getCt2Marks() : 0;
        double totalPercentage = ((ct1 + ct2) / 120.0) * 100;

        return MarkDTO.builder()
                .id(mark.getId())
                .studentRegNo(mark.getStudent().getRegNo())
                .studentName(mark.getStudent().getName())
                .subjectId(mark.getSubject().getSubjectId())
                .subjectName(mark.getSubject().getSubjectName())
                .ct1Marks(ct1)
                .ct2Marks(ct2)
                .totalPercentage(Math.round(totalPercentage * 100.0) / 100.0)
                .build();
    }
}
