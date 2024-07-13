package com.github.springdocumentservice.repository.User;

import com.github.springdocumentservice.Dto.UserDto;
import com.github.springdocumentservice.domain.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository //db 연동을 처리하는 DAO 클래스
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;

    //회원 저장
    @Override
    public UserDto save(UserDto userDto) {
        return userJpaRepository.save(User.from(userDto)).toDTO();
    }


    //회원 이메일 조회
    @Override
    public UserDto findByEmail(String email) {
        User userDto = userJpaRepository.findByEmail(email);
        if (userDto == null) {
            return null;
        } else {
            return userDto.toDTO();
        }
    }
    @Override
    public Optional<User> findByEmail2(String email) {
        return Optional.ofNullable(userJpaRepository.findByEmail(email));
    }




}