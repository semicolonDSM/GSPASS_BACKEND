package com.semicolon.gspass.service.teacher;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.RegisterRequest;
import com.semicolon.gspass.entity.refreshtoken.RefreshToken;
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository;
import com.semicolon.gspass.entity.school.SchoolRepository;
import com.semicolon.gspass.entity.teacher.Teacher;
import com.semicolon.gspass.entity.teacher.TeacherRepository;
import com.semicolon.gspass.exception.InvalidPasswordException;
import com.semicolon.gspass.exception.SchoolNotFoundException;
import com.semicolon.gspass.exception.TeacherAlreadyExistException;
import com.semicolon.gspass.exception.TeacherNotFoundException;
import com.semicolon.gspass.facade.auth.AuthenticationFacade;
import com.semicolon.gspass.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationFacade authenticationFacade;

    @Value("${jwt.exp.refresh}")
    private Long refreshTokenExpiration;

    @Override
    public TokenResponse registerTeacher(RegisterRequest request) {
        if(teacherRepository.existsById(request.getId()) ||
                schoolRepository.findByRandomCode(request.getRandomCode())
                        .orElseThrow(SchoolNotFoundException::new).getTeacher() != null
        ) throw new TeacherAlreadyExistException();

        schoolRepository.findByRandomCode(request.getRandomCode())
                .map(school -> teacherRepository.save(
                        Teacher.builder()
                        .id(request.getId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .school(school)
                        .build()
                )).orElseThrow(SchoolNotFoundException::new);

        return generateToken(request.getId());
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Teacher teacher = teacherRepository.findById(request.getId())
                .orElseThrow(TeacherNotFoundException::new);

        if(!passwordEncoder.matches(request.getPassword(), teacher.getPassword()))
            throw new InvalidPasswordException();

        return generateToken(request.getId());
    }

    @Override
    public void changePassword(PasswordRequest request) {
        String teacherId = authenticationFacade.getTeacherId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(TeacherNotFoundException::new);

        if(!passwordEncoder.matches(request.getOldPassword(), teacher.getPassword()))
            throw new InvalidPasswordException();

        teacherRepository.save(
                teacher.setPassword(passwordEncoder.encode(request.getNewPassword()))
        );
    }

    private TokenResponse generateToken(String name) {
        String accessToken = jwtTokenProvider.generateAccessToken(name, "teacher");
        String refreshToken = jwtTokenProvider.generateRefreshToken(name, "teacher");
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(name)
                        .refreshExp(refreshTokenExpiration)
                        .refreshToken(refreshToken)
                        .build()
        );
        return new TokenResponse(accessToken, refreshToken);
    }

}
