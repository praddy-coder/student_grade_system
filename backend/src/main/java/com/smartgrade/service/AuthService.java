package com.smartgrade.service;

import com.smartgrade.dto.LoginRequest;
import com.smartgrade.dto.LoginResponse;
import com.smartgrade.entity.Staff;
import com.smartgrade.entity.Student;
import com.smartgrade.entity.User;
import com.smartgrade.repository.StaffRepository;
import com.smartgrade.repository.StudentRepository;
import com.smartgrade.repository.UserRepository;
import com.smartgrade.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       StaffRepository staffRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String name = "";
        if (user.getRole() == User.Role.STUDENT) {
            name = studentRepository.findById(user.getRefId())
                    .map(Student::getName).orElse(user.getUsername());
        } else {
            name = staffRepository.findById(user.getRefId())
                    .map(Staff::getName).orElse(user.getUsername());
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getRefId());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .username(user.getUsername())
                .refId(user.getRefId())
                .name(name)
                .build();
    }
}
