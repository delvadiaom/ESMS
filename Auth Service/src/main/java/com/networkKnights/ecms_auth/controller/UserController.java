package com.networkKnights.ecms_auth.controller;

import com.networkKnights.ecms_auth.dto.AuthRequest;
import com.networkKnights.ecms_auth.dto.PrivilegeDto;
import com.networkKnights.ecms_auth.dto.RegisterRequest;
import com.networkKnights.ecms_auth.dto.UserInfoDto;
import com.networkKnights.ecms_auth.entity.AuthPriviledgeLookUp;
import com.networkKnights.ecms_auth.entity.UserInfo;
import com.networkKnights.ecms_auth.service.AuthPriviledgeLookUpService;
import com.networkKnights.ecms_auth.service.JwtService;
import com.networkKnights.ecms_auth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AuthPriviledgeLookUpService authPriviledgeLookUpService;

    @GetMapping("/getAllUserDetails")
    public ResponseEntity<?> getAllUserDetails() {
        return userInfoService.getAllUserDetails();
    }

    @PostMapping("/saveUserDetails")
    public ResponseEntity<?> saveUserDetails(@RequestBody RegisterRequest registerRequest) {
        return userInfoService.saveUserDetails(registerRequest);
    }

    @PutMapping("/updateUserDetails")
    public ResponseEntity<?> updateUserDetails(@RequestBody RegisterRequest registerRequest) {
        return userInfoService.updateUserDetails(registerRequest);
    }

    @PutMapping("/deleteUserDetails/{id}")
    public ResponseEntity<?> deleteUserDetails(@PathVariable long id) {
        return userInfoService.deleteUserDetails(id);
    }

    @GetMapping("/getUserDetailsById/{id}")
    public ResponseEntity<?> getUserDetailsById(@PathVariable long id) {
        return userInfoService.getUserDetailsById(id);
    }

    @PostMapping("/activateUser")
    public  ResponseEntity<?> activateUser(@RequestBody Map<String,String> body)
    {
        return userInfoService.activateUser(body.get("mail"));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changeUserPassword(@RequestBody Map<String,String> body)
    {
        return userInfoService.updateUserPassword(body.get("mail"),body.get("current"),body.get("password"));
    }

    @GetMapping("/getPriviledgeDetails")
    public ResponseEntity<?> getPriviledgeDetails() {
        return authPriviledgeLookUpService.getPriviledgeDetails();
    }

    @GetMapping("/getPriviledgeDetailsById/{id}")
    public ResponseEntity<?> getPriviledgeDetailsById(@PathVariable long id) {
        return authPriviledgeLookUpService.getPriviledgeDetailsById(id);
    }

    @PostMapping("/savePriviledgeDetails")
    public ResponseEntity<?> savePriviledgeDetails(@RequestBody PrivilegeDto privilegeDto) {
        return authPriviledgeLookUpService.savePriviledgeDetails(privilegeDto);
    }

    @PutMapping("/updatePriviledgeDetails")
    public ResponseEntity<?> updatePriviledgeDetails(@RequestBody PrivilegeDto privilegeDto) {
        return authPriviledgeLookUpService.updatePriviledgeDetails(privilegeDto);
    }

    @DeleteMapping("/deletePriviledgeDetails/{id}")
    public ResponseEntity<?> deletePriviledgeDetails(@PathVariable long id) {
        return authPriviledgeLookUpService.deletePriviledgeDetails(id);
    }

}
