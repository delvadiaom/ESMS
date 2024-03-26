package com.networkKnights.ecms_auth.service;

import com.networkKnights.ecms_auth.dto.RegisterRequest;
import com.networkKnights.ecms_auth.dto.UserInfoDto;
import com.networkKnights.ecms_auth.entity.UserInfo;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserInfoService {
    public Optional<UserInfo> findUserInfoByEmail(String email);

    public ResponseEntity<?> getAllUserDetails();

    public ResponseEntity<?> saveUserDetails(RegisterRequest registerRequest);

    public ResponseEntity<?> updateUserDetails(RegisterRequest registerRequest);

    public ResponseEntity<?> deleteUserDetails(long id);

    public ResponseEntity<?> getUserDetailsById(long id);

    UserInfoDto fromUserInfoToUserInfoDto(UserInfo userInfo);

    boolean checkPermission(String username, String url);

    public ResponseEntity<?> checkEmailExists(String email);

    public ResponseEntity<?> forgotPassword(String mail);

    public ResponseEntity<?> setPwdWithOtp(String mail, String otp, String password);

    public ResponseEntity<?> updateUserPassword(String mail, String current, String password);

    public ResponseEntity<?> verifyOtp(String mail, String otp);

    public ResponseEntity<?> activateUser(String mail);
}
