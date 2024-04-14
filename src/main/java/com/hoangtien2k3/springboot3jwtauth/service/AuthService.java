package com.hoangtien2k3.springboot3jwtauth.service;

import com.hoangtien2k3.springboot3jwtauth.dto.request.RefreshTokenRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.request.SigninRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.request.SignupRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.response.RefreshTokenResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.response.SigninResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.response.SignupResponse;

public interface AuthService {
    SigninResponse signIn(SigninRequest signinRequest);
    SignupResponse signUp(SignupRequest signupRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
