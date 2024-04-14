package com.hoangtien2k3.springboot3jwtauth.service.AuthServiceImpl;

import com.hoangtien2k3.springboot3jwtauth.dto.request.RefreshTokenRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.request.SigninRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.request.SignupRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.response.RefreshTokenResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.response.SigninResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.response.SignupResponse;
import com.hoangtien2k3.springboot3jwtauth.entity.Timestamps;
import com.hoangtien2k3.springboot3jwtauth.entity.User;
import com.hoangtien2k3.springboot3jwtauth.exception.EnumConfig.ErrorCode;
import com.hoangtien2k3.springboot3jwtauth.exception.payload.AppException;
import com.hoangtien2k3.springboot3jwtauth.mapper.UserMapper;
import com.hoangtien2k3.springboot3jwtauth.repository.UserRepository;
import com.hoangtien2k3.springboot3jwtauth.secutiry.JWTUtils;
import com.hoangtien2k3.springboot3jwtauth.secutiry.UserDetail;
import com.hoangtien2k3.springboot3jwtauth.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    JWTUtils jwtUtils;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    UserMapper userMapper;

    @Override
    public SigninResponse signIn(SigninRequest signinRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequest.getEmail(),
                            signinRequest.getPassword()
                    )
            );

            var user = userRepository.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXITSTED));
            var userDetailsService = new UserDetail(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles(),
                    user.getTimestamps()
            );

            var token = jwtUtils.generateToken(userDetailsService);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userDetailsService);

            return SigninResponse.builder()
                    .authenticated(true)
                    .token(token)
                    .refreshToken(refreshToken)
                    .expirationTime(JWTUtils.EXPIRATION_TIME)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SignupResponse signUp(SignupRequest signupRequest) {
        try {
            User user = new User();
            Set<String> roles = new HashSet<>();
            roles.add(String.join(", ", signupRequest.getRole()));

            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setRoles(roles);
            user.setTimestamps(
                    Timestamps.builder()
                            .created_at(LocalDateTime.now())
                            .updated_at(LocalDateTime.now())
                            .build()
            );

            return userMapper.toSignupResponse(userRepository.save(user));
        } catch (DataAccessException e) {
            throw new AppException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXITSTED));
        UserDetail userDetail = userMapper.userToUserDetail(user);

        if (!jwtUtils.isTokenValid(refreshTokenRequest.getToken(), userDetail)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String jwtToken = jwtUtils.generateToken(userDetail);

        return RefreshTokenResponse.builder()
                .authenticated(true)
                .token(jwtToken)
                .refreshToken(refreshTokenRequest.getToken())
                .expirationTime(JWTUtils.EXPIRATION_TIME)
                .build();
    }

}
