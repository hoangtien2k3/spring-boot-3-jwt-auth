package com.hoangtien2k3.springboot3jwtauth.mapper;

import com.hoangtien2k3.springboot3jwtauth.dto.response.SignupResponse;
import com.hoangtien2k3.springboot3jwtauth.entity.User;
import com.hoangtien2k3.springboot3jwtauth.secutiry.UserDetail;
import org.mapstruct.Mapper;


/**
 * MapStruc in spring boot
 * **/
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(SignupResponse userCreationRequest);
    SignupResponse toSignupResponse(User user);
    UserDetail userToUserDetail(User user);
}
