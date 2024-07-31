package com.sparta.myselectshop.domain.user.service;

import com.sparta.myselectshop.domain.user.dto.SignupRequestDto;
import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;
import com.sparta.myselectshop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("이름이 중복된 사용자가 있습니다.")
        );

        // email 중복확인
        String email = requestDto.getEmail();
        User userByEmail = userRepository.findByEmail(email).orElseThrow(
                ()-> new IllegalArgumentException("이메일이 중복된 사용자가 있습니다.")
        );

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User userToSave = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .build();

        userRepository.save(userToSave);
    }
}
