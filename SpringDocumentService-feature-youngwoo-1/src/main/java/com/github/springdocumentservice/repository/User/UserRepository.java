package com.github.springdocumentservice.repository.User;

import com.github.springdocumentservice.Dto.UserDto;
import com.github.springdocumentservice.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {

    UserDto save(UserDto userDto);
    UserDto findByEmail(String email);
    Optional<User> findByEmail2(String email);

}