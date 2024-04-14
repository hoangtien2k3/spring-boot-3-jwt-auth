package com.hoangtien2k3.springboot3jwtauth.service.AuthServiceImpl;

import com.hoangtien2k3.springboot3jwtauth.exception.EnumConfig.ErrorCode;
import com.hoangtien2k3.springboot3jwtauth.exception.payload.AppException;
import com.hoangtien2k3.springboot3jwtauth.mapper.UserMapper;
import com.hoangtien2k3.springboot3jwtauth.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.userToUserDetail( userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXITSTED))
        );
    }

}
