package com.github.springdocumentservice.controller;

import com.github.springdocumentservice.Dto.NewUserDto;
import com.github.springdocumentservice.Dto.ResponseToken;
import com.github.springdocumentservice.Dto.Token;
import com.github.springdocumentservice.Dto.UserDto;
import com.github.springdocumentservice.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequestMapping("/api") //내부에 선언한 메서드의 URL 리소스 앞에 @RequestMapping의 값이 공통 값으로 추가됨.
@RequiredArgsConstructor
@RestController //사용자 요청을 제어하는 controller 클래스
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    @Operation(summary = "유저 회원가입", description = "이메일, 비밀번호, 닉네임을 넣고 회원가입합니다.")
    public ResponseEntity<?> join(
            @RequestParam("email") String email,
            @RequestParam("password") String userPassword,
            @RequestParam("nickname") String userNickname,
            @RequestParam("status") String status
           ) {



        try {
            NewUserDto newUserDto = NewUserDto.builder()
                    .email(email)
                    .userPassword(userPassword)
                    .userNickname(userNickname)
                    .userStatus(status)
                    .build();

            // 유효성 검사
            if (!isValidEmail(newUserDto.getEmail())) {
                log.info("이메일 형식에 맞게 입력하세요.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 형식에 맞게 입력하세요.");
            }

            if (!isValidPassword(newUserDto.getUserPassword())) {
                log.info("비밀번호 형식에 맞게 입력해주세요.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 형식에 맞게 입력해주세요.");
            }

            // 사용자 등록
            UserDto savedUser = userService.register(newUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/users/login")
    @Operation(summary = "유저 로그인", description = "유저 이메일, 비밀번호를 이용하여 로그인합니다.")
    public ResponseEntity<?> login(@RequestBody UserDto login) {
        try {
            String email = login.getEmail();
            String password = login.getUserPassword();
            Token token = userService.login(email, password);
            UserDto userDto = userService.findUserByEmail(email);
            return ResponseEntity.ok().body(ResponseToken.of(token, userDto.getUserInterests()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/users/logout")
    @Operation(summary = "유저 로그아웃", description = "해당 이메일을 가진 유저를 로그아웃합니다.")
    public ResponseEntity<?> logout(HttpServletRequest request, @RequestBody UserDto userDto) {
        String res = userService.logout(request, userDto.getEmail());
        return ResponseEntity.ok().body(res);
    }


    //이메일 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    //비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        String passwordRegex = "(?=.*[0-9])(?=.*[A-Za-z]).{8,20}$";
        return password.matches(passwordRegex);
    }

    //핸드폰 번호 유효성 검사
    private boolean isValidPhone (String user_phone) {
        String phoneRegex = "^01[0|1|6|7|8|9][0-9]{7,8}$";
        return user_phone.matches(phoneRegex);
    }

}