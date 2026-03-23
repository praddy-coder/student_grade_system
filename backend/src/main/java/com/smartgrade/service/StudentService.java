package com.smartgrade.service;

import com.smartgrade.entity.Student;
import com.smartgrade.entity.User;
import com.smartgrade.repository.StudentRepository;
import com.smartgrade.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public int bulkRegister(MultipartFile file) throws Exception {
        int count = 0;
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Student> students = new ArrayList<>();
            List<User> users = new ArrayList<>();
            
            DataFormatter dataFormatter = new DataFormatter();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Assume Header: RegNo | Name | DOB (dd/MM/yyyy) | Dept | Year | Section
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell regNoCell = row.getCell(0);
                String regNo = dataFormatter.formatCellValue(regNoCell).trim();
                if (regNo.isEmpty()) continue;

                String name = dataFormatter.formatCellValue(row.getCell(1)).trim();
                String dobStr = dataFormatter.formatCellValue(row.getCell(2)).trim();
                String dept = dataFormatter.formatCellValue(row.getCell(3)).trim();
                
                String yearStr = dataFormatter.formatCellValue(row.getCell(4)).trim();
                int year = yearStr.isEmpty() ? 1 : Integer.parseInt(yearStr);
                
                String section = dataFormatter.formatCellValue(row.getCell(5)).trim();
                if(section.isEmpty()) section = "A";

                LocalDate dob = null;
                try {
                	dob = LocalDate.parse(dobStr, dateFormatter);
                } catch(Exception e) {
                	dob = LocalDate.now();
                }

                Student student = Student.builder()
                        .regNo(regNo)
                        .name(name)
                        .dob(dob)
                        .department(dept)
                        .year(year)
                        .classSection(section)
                        .build();

                students.add(student);

                // Check if user exists
                if (userRepository.findByUsername(regNo).isEmpty()) {
                    User user = User.builder()
                            .username(regNo)
                            .passwordHash(passwordEncoder.encode(dobStr)) // Password is DOB
                            .role(User.Role.STUDENT)
                            .refId(regNo)
                            .build();
                    users.add(user);
                }
                count++;
            }
            studentRepository.saveAll(students);
            userRepository.saveAll(users);
        }
        return count;
    }
}
