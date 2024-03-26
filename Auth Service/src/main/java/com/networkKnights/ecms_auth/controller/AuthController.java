package com.networkKnights.ecms_auth.controller;

import com.networkKnights.ecms_auth.constant.URIConstant;
import com.networkKnights.ecms_auth.dto.AuthRequest;
import com.networkKnights.ecms_auth.dto.UserInfoDto;
import com.networkKnights.ecms_auth.entity.ESMSError;
import com.networkKnights.ecms_auth.entity.UserInfo;
import com.networkKnights.ecms_auth.service.JwtService;
import com.networkKnights.ecms_auth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(URIConstant.URI_AUTH)
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping(URIConstant.URI_UNAUTHORIZED)
    public ResponseEntity<?> unauthorized() {
        return new ResponseEntity<>(new ESMSError("Not Authorized user"), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(URIConstant.URI_LOGIN)
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            UserInfo newUser = userInfoService.findUserInfoByEmail(authRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
            String token = jwtService.generateToken(newUser);
            Map<String, UserInfoDto> result = new HashMap<String, UserInfoDto>();
            result.put(token, userInfoService.fromUserInfoToUserInfoDto(newUser));
            return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        } else {
            throw new UsernameNotFoundException("invalid user");
        }
    }

    @PostMapping(URIConstant.URI_VALIDATE)
    public boolean validate(@RequestBody Map<String, String> token) {
        try {
            String username = jwtService.extractUsername(token.get("token"));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean valid = false;
            valid = jwtService.validateToken(token.get("token"), userDetails);
            valid = userInfoService.checkPermission(username, token.get("url"));
            return valid;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping(URIConstant.URI_CHECK_EMAIL + URIConstant.URI_PATH_VARIABLE_EMAIL)
    public ResponseEntity<?> userExists(@PathVariable String email) {
        return userInfoService.checkEmailExists(email);
    }

    @GetMapping(URIConstant.URI_VALIDATE_TOKEN)
    public ResponseEntity<?> validateToken(@RequestHeader Map<String, String> headers) {
        try {
            String username = jwtService.extractUsername(headers.get("token"));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UserInfo newUser = userInfoService.findUserInfoByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
            boolean valid = false;
            valid = jwtService.validateToken(headers.get("token"), userDetails);
            if (valid) {
                return new ResponseEntity<>(userInfoService.fromUserInfoToUserInfoDto(newUser), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(URIConstant.URI_FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            return userInfoService.forgotPassword(body.get("mail"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(URIConstant.URI_SET_PASSWORD_WITH_OTP)
    public ResponseEntity<?> setPasswordWithOtp(@RequestBody Map<String, String> body) {
        try {
            return userInfoService.setPwdWithOtp(body.get("mail"), body.get("otp"), body.get("password"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(URIConstant.URI_VERIFY_OTP)
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        return userInfoService.verifyOtp(body.get("mail"), body.get("otp"));
    }
}
