package com.smartgrade.controller;

import com.smartgrade.dto.MarkDTO;
import com.smartgrade.dto.MarkUpdateRequest;
import com.smartgrade.service.MarksService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marks")
public class MarksController {

    private final MarksService marksService;

    public MarksController(MarksService marksService) {
        this.marksService = marksService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<MarkDTO>> getStudentMarks(@PathVariable String studentId) {
        return ResponseEntity.ok(marksService.getStudentMarks(studentId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('STAFF', 'CC', 'HOD')")
    public ResponseEntity<List<MarkDTO>> getAllMarks(
            @RequestParam(required = false) String department,
            Authentication auth) {
        
        String empId = (String) auth.getDetails();
        boolean isCCorHOD = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CC") || a.getAuthority().equals("ROLE_HOD"));
        
        if (!isCCorHOD) {
            return ResponseEntity.ok(marksService.getMarksByStaff(empId));
        }

        if (department != null && !department.isEmpty()) {
            return ResponseEntity.ok(marksService.getMarksByDepartment(department));
        }
        return ResponseEntity.ok(marksService.getAllMarks());
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('STAFF', 'CC', 'HOD')")
    public ResponseEntity<?> updateMark(@RequestBody MarkUpdateRequest request,
                                         Authentication auth) {
        try {
            String empId = (String) auth.getDetails();
            MarkDTO updated = marksService.updateMark(request, empId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
