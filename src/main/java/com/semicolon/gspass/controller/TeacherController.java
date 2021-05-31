package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.RegisterRequest;
import com.semicolon.gspass.service.teacher.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    @Operation(summary = "관리자 회원가입")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return teacherService.registerTeacher(request);
    }

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return teacherService.login(request);
    }

}
