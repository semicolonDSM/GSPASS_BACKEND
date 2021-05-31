package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.dto.school.RegisterRequest;
import com.semicolon.gspass.dto.school.SchoolResponse;
import com.semicolon.gspass.service.school.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT", name = "Authorization", in = SecuritySchemeIn.HEADER)
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/meals")
    @Operation(summary = "급식 가져오기", security = @SecurityRequirement(name = "Authorization"))
    public MealResponse getMeals(@RequestParam("date") String date) {
        return schoolService.getMeals(date);
    }

    @GetMapping("/school")
    public List<SchoolResponse> getSchools(@RequestParam("name") String name) {
        return schoolService.getSchools(name);
    }

    @PostMapping("/school")
    public String registerSchool(@RequestBody RegisterRequest request) {
        return schoolService.registerSchool(request);
    }

}
