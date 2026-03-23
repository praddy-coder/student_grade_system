package com.smartgrade.config;

import com.smartgrade.entity.*;
import com.smartgrade.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

        private final UserRepository userRepository;
        private final StudentRepository studentRepository;
        private final StaffRepository staffRepository;
        private final SubjectRepository subjectRepository;
        private final MarkRepository markRepository;
        private final PasswordEncoder passwordEncoder;

        public DataSeeder(UserRepository userRepository,
                        StudentRepository studentRepository,
                        StaffRepository staffRepository,
                        SubjectRepository subjectRepository,
                        MarkRepository markRepository,
                        PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.studentRepository = studentRepository;
                this.staffRepository = staffRepository;
                this.subjectRepository = subjectRepository;
                this.markRepository = markRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) {
                // Removed early return to ensure new test users are seeded into existing DB
                // (Supabase)
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // ========================
                // STUDENTS
                // ========================
                List<Student> students = List.of(
                                Student.builder().regNo("814423104034").name("S. PRADEEP").dob(LocalDate.of(2006, 5, 8))
                                                .department("CSE").year(3).classSection("A").build(),
                                Student.builder().regNo("CS2024002").name("R. ARUN KUMAR")
                                                .dob(LocalDate.of(2004, 3, 22))
                                                .department("CSE").year(3).classSection("A").build(),
                                Student.builder().regNo("CS2024003").name("M. DIVYA").dob(LocalDate.of(2004, 8, 10))
                                                .department("CSE").year(3).classSection("A").build(),
                                Student.builder().regNo("CS2024004").name("K. HARISH").dob(LocalDate.of(2004, 11, 5))
                                                .department("CSE").year(3).classSection("A").build(),
                                Student.builder().regNo("CS2024005").name("P. SWETHA").dob(LocalDate.of(2004, 1, 28))
                                                .department("CSE").year(3).classSection("A").build(),
                                Student.builder().regNo("CS2024006").name("V. KARTHIK").dob(LocalDate.of(2004, 7, 19))
                                                .department("CSE").year(3).classSection("B").build(),
                                Student.builder().regNo("CS2024007").name("L. NITHYA").dob(LocalDate.of(2004, 9, 3))
                                                .department("CSE").year(3).classSection("B").build(),
                                Student.builder().regNo("CS2024008").name("S. RAVI").dob(LocalDate.of(2004, 12, 25))
                                                .department("CSE").year(3).classSection("B").build(),
                                Student.builder().regNo("CS2024009").name("A. MEENA").dob(LocalDate.of(2004, 4, 14))
                                                .department("CSE").year(3).classSection("B").build(),
                                Student.builder().regNo("CS2024010").name("T. SURESH").dob(LocalDate.of(2004, 6, 30))
                                                .department("CSE").year(3).classSection("B").build());

                for (Student s : students) {
                        if (studentRepository.findById(s.getRegNo()).isEmpty()) {
                                studentRepository.save(s);
                        }
                }

                // ========================
                // STAFF
                // ========================
                List<Staff> staffList = List.of(
                                Staff.builder().empId("EMP001").name("Dr. R. VENKATESH").dob(LocalDate.of(1975, 3, 10))
                                                .department("CSE").role(Staff.StaffRole.HOD).build(),
                                Staff.builder().empId("EMP002").name("Prof. S. LAKSHMI").dob(LocalDate.of(1980, 7, 22))
                                                .department("CSE").role(Staff.StaffRole.CC).build(),
                                Staff.builder().empId("EMP003").name("M. KUMAR").dob(LocalDate.of(1985, 11, 5))
                                                .department("CSE").role(Staff.StaffRole.STAFF).build(),
                                Staff.builder().empId("EMP004").name("K. PRIYA").dob(LocalDate.of(1982, 9, 18))
                                                .department("CSE").role(Staff.StaffRole.STAFF).build(),
                                Staff.builder().empId("EMP005").name("J. ANAND").dob(LocalDate.of(1988, 2, 14))
                                                .department("CSE").role(Staff.StaffRole.STAFF).build(),
                                Staff.builder().empId("EMP006").name("V. MEERA").dob(LocalDate.of(1986, 5, 23))
                                                .department("CSE").role(Staff.StaffRole.STAFF).build(),
                                Staff.builder().empId("EMP007").name("S. RAM").dob(LocalDate.of(1990, 10, 8))
                                                .department("CSE").role(Staff.StaffRole.STAFF).build(),
                                Staff.builder().empId("EMP008").name("T. KRISHNA").dob(LocalDate.of(1984, 1, 30))
                                                .department("CSE").role(Staff.StaffRole.STAFF).build());

                for (Staff s : staffList) {
                        if (staffRepository.findById(s.getEmpId()).isEmpty()) {
                                staffRepository.save(s);
                        }
                }

                // ========================
                // SUBJECTS
                // ========================
                List<Subject> subjects = List.of(
                                Subject.builder().subjectId("CS301").subjectName("Data Structures & Algorithms")
                                                .classId("3A").department("CSE").staffId("EMP003").build(),
                                Subject.builder().subjectId("CS302").subjectName("Database Management Systems")
                                                .classId("3A").department("CSE").staffId("EMP004").build(),
                                Subject.builder().subjectId("CS303").subjectName("Operating Systems")
                                                .classId("3A").department("CSE").staffId("EMP005").build(),
                                Subject.builder().subjectId("CS304").subjectName("Computer Networks")
                                                .classId("3A").department("CSE").staffId("EMP006").build(),
                                Subject.builder().subjectId("CS305").subjectName("Software Engineering")
                                                .classId("3A").department("CSE").staffId("EMP007").build(),
                                Subject.builder().subjectId("CS306").subjectName("Artificial Intelligence")
                                                .classId("3A").department("CSE").staffId("EMP008").build());

                for (Subject s : subjects) {
                        if (subjectRepository.findById(s.getSubjectId()).isEmpty()) {
                                subjectRepository.save(s);
                        }
                }

                // ========================
                // USERS (password = DOB in DD/MM/YYYY, BCrypt hashed)
                // ========================
                // Student users
                for (Student s : students) {
                        String password = s.getDob().format(fmt);
                        User existingUser = userRepository.findByUsername(s.getRegNo()).orElse(null);
                        if (existingUser != null) {
                                userRepository.save(User.builder().id(existingUser.getId()).username(existingUser.getUsername())
                                        .passwordHash(passwordEncoder.encode(password)).role(existingUser.getRole()).refId(existingUser.getRefId()).build());
                        } else {
                                userRepository.save(User.builder()
                                                .username(s.getRegNo())
                                                .passwordHash(passwordEncoder.encode(password))
                                                .role(User.Role.STUDENT)
                                                .refId(s.getRegNo())
                                                .build());
                        }
                }

                // Staff users
                for (Staff s : staffList) {
                        String password = s.getDob().format(fmt);
                        User.Role userRole;
                        switch (s.getRole()) {
                                case HOD:
                                        userRole = User.Role.HOD;
                                        break;
                                case CC:
                                        userRole = User.Role.CC;
                                        break;
                                default:
                                        userRole = User.Role.STAFF;
                        }

                        User existingUser = userRepository.findByUsername(s.getEmpId()).orElse(null);
                        if (existingUser != null) {
                                userRepository.save(User.builder().id(existingUser.getId()).username(existingUser.getUsername())
                                        .passwordHash(passwordEncoder.encode(password)).role(existingUser.getRole()).refId(existingUser.getRefId()).build());
                        } else {
                                userRepository.save(User.builder()
                                                .username(s.getEmpId())
                                                .passwordHash(passwordEncoder.encode(password))
                                                .role(userRole)
                                                .refId(s.getEmpId())
                                                .build());
                        }
                }

                // ========================
                // MARKS (seed with dummy data)
                // ========================
                Staff updater = staffRepository.findById("EMP003").orElse(staffList.get(2));

                int[][] marksData = {
                                // 6 subjects x 10 students
                                { 45, 48 }, { 52, 55 }, { 38, 42 }, { 50, 47 }, { 28, 32 }, { 55, 50 }, // 814423104034
                                { 55, 58 }, { 42, 45 }, { 48, 50 }, { 35, 38 }, { 44, 46 }, { 50, 52 }, // CS2024002
                                { 32, 28 }, { 45, 48 }, { 25, 30 }, { 40, 42 }, { 38, 35 }, { 40, 45 }, // CS2024003
                                { 58, 55 }, { 50, 52 }, { 55, 53 }, { 48, 50 }, { 52, 48 }, { 56, 58 }, // CS2024004
                                { 22, 25 }, { 30, 28 }, { 35, 32 }, { 42, 40 }, { 28, 26 }, { 32, 28 }, // CS2024005
                                { 48, 50 }, { 55, 52 }, { 42, 45 }, { 38, 40 }, { 50, 48 }, { 45, 44 }, // CS2024006
                                { 40, 42 }, { 35, 38 }, { 50, 48 }, { 55, 52 }, { 45, 42 }, { 52, 50 }, // CS2024007
                                { 30, 33 }, { 28, 32 }, { 45, 42 }, { 50, 48 }, { 35, 38 }, { 40, 42 }, // CS2024008
                                { 55, 52 }, { 48, 50 }, { 42, 45 }, { 35, 38 }, { 52, 55 }, { 48, 45 }, // CS2024009
                                { 42, 45 }, { 38, 35 }, { 32, 35 }, { 28, 30 }, { 40, 42 }, { 35, 38 }, // CS2024010
                };

                int idx = 0;
                for (Student student : students) {
                        for (Subject subject : subjects) {
                                if (markRepository.findByStudent_RegNoAndSubject_SubjectId(student.getRegNo(),
                                                subject.getSubjectId()).isEmpty()) {
                                        markRepository.save(Mark.builder()
                                                        .student(student)
                                                        .subject(subject)
                                                        .ct1Marks(marksData[idx][0])
                                                        .ct2Marks(marksData[idx][1])
                                                        .lastUpdatedBy(updater)
                                                        .build());
                                }
                                idx++;
                        }
                }

                System.out.println("================================");
                System.out.println("  DATA SEEDING / SYNC COMPLETE");
                System.out.println("  Test Student: 814423104034");
                System.out.println("  Test CC:      EMP002");
                System.out.println("================================");
        }

}
