package com.hoangtien2k3.springboot3jwtauth.controller;

import com.hoangtien2k3.springboot3jwtauth.constants.Constants;
import com.hoangtien2k3.springboot3jwtauth.dto.ApiResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.request.RefreshTokenRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.request.SigninRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.request.SignupRequest;
import com.hoangtien2k3.springboot3jwtauth.dto.response.RefreshTokenResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.response.SigninResponse;
import com.hoangtien2k3.springboot3jwtauth.dto.response.SignupResponse;
import com.hoangtien2k3.springboot3jwtauth.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @PostMapping({"/signup", "/register"})
    public ApiResponse<SignupResponse> signUp(@RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = authService.signUp(signupRequest);
        return ApiResponse.<SignupResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(Constants.SUCCESS)
                .data(signupResponse)
                .build();
    }

    @PostMapping({"/signin", "/login"})
    public ApiResponse<SigninResponse> signIn(@RequestBody SigninRequest signinRequest){
        SigninResponse signinResponse = authService.signIn(signinRequest);
        boolean authenticated = signinResponse.isAuthenticated();

        return ApiResponse.<SigninResponse>builder()
                .statusCode(authenticated
                        ? HttpStatus.OK.value()
                        : HttpStatus.BAD_REQUEST.value())
                .message(authenticated
                        ? Constants.SUCCESS : Constants.FAILED)
                .data(signinResponse)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshTokenRequest);
        boolean authenticated = refreshTokenResponse.isAuthenticated();

        return ApiResponse.<RefreshTokenResponse>builder()
                .statusCode(authenticated
                        ? HttpStatus.OK.value()
                        : HttpStatus.BAD_REQUEST.value())
                .message(authenticated
                        ? Constants.SUCCESS : Constants.FAILED)
                .data(refreshTokenResponse)
                .build();
    }
}
