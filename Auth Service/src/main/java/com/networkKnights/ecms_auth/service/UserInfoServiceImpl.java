package com.networkKnights.ecms_auth.service;

import com.networkKnights.ecms_auth.dao.AuthPriviledgeLookUpRepository;
import com.networkKnights.ecms_auth.dao.OtpRepo;
import com.networkKnights.ecms_auth.dao.UserInfoRepository;
import com.networkKnights.ecms_auth.dto.RegisterRequest;
import com.networkKnights.ecms_auth.dto.UserInfoDto;
import com.networkKnights.ecms_auth.entity.AuthPriviledgeLookUp;
import com.networkKnights.ecms_auth.entity.ESMSError;
import com.networkKnights.ecms_auth.entity.OTP;
import com.networkKnights.ecms_auth.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private OtpRepo otpRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AuthPriviledgeLookUpRepository authRepository;

    @Override
    public Optional<UserInfo> findUserInfoByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    public ResponseEntity<?> getUserDetailsById(long id) {
        try {
            UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            UserInfoDto userInfoDto = fromUserInfoToUserInfoDto(userInfo);
            AuthPriviledgeLookUp lookUp = authRepository.findById(userInfoDto.getPriviledgeId()).orElseThrow(() -> new RuntimeException("Priviledge not found"));
            userInfoDto.setPriviledgeName(lookUp.getPriviledgeName());
            return (userInfo != null) ? new ResponseEntity<>(userInfoDto, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("Please enter valid id "), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getAllUserDetails() {
        try {
            List<UserInfo> userInfos = userInfoRepository.findAll();
            List<UserInfoDto> userInfoDtos = new ArrayList<>();
            userInfos.forEach(userInfo -> userInfoDtos.add(fromUserInfoToUserInfoDto(userInfo)));
            AuthPriviledgeLookUp lookUp;
            for (int i = 0; i < userInfoDtos.size(); i++) {
                lookUp = authRepository.findById(userInfoDtos.get(i).getPriviledgeId()).orElseThrow(() -> new RuntimeException("Priviledge not found"));
                userInfoDtos.get(i).setPriviledgeName(lookUp.getPriviledgeName());
            }
            return (userInfos.size() > 0) ? new ResponseEntity<>(userInfoDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data found "), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //save user
    @Override
    public ResponseEntity<?> saveUserDetails(RegisterRequest registerRequest) {
        try {
            boolean exist = authRepository.existsById(registerRequest.getPriviledgeId());
            if (!exist)
                return new ResponseEntity<>(new ESMSError("Role not exist"), HttpStatus.BAD_REQUEST);
            UserInfo user = fromRegisterRequestToUserInfo(registerRequest);
            try {
                user = userInfoRepository.save(user);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Email already exists"), HttpStatus.OK);
            }
            return (user.getUserId() > 0) ? new ResponseEntity<>(new ESMSError("User created"), HttpStatus.OK) : new ResponseEntity<>(new ESMSError("Error while saving user"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //update user details [privilege and password]
    @Override
    public ResponseEntity<?> updateUserDetails(RegisterRequest registerRequest) {
        try {
            boolean exist = authRepository.existsById(registerRequest.getPriviledgeId());
            if (!exist)
                return new ResponseEntity<>(new ESMSError("Role not exist"), HttpStatus.BAD_REQUEST);
            UserInfo userInfo = userInfoRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() -> new RuntimeException("user not found"));
            userInfo.setPriviledgeId(registerRequest.getPriviledgeId());
            if (!registerRequest.getPassword().equals(""))
                userInfo.setPwd(encoder.encode(registerRequest.getPassword()));
            userInfo.setModifiedDate(new Date());
            userInfo = userInfoRepository.save(userInfo);
            return (userInfo.getUserId() > 0) ? new ResponseEntity<>(new ESMSError("Updation Successful"), HttpStatus.OK) : new ResponseEntity<>(new ESMSError("Updation failed"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //hard delete user
    @Override
    public ResponseEntity<?> deleteUserDetails(long id) {
        try {
            if (userInfoRepository.existsById(id)) {
                userInfoRepository.deleteById(id);
                return new ResponseEntity<>(new ESMSError("User deleted"), HttpStatus.OK);
            } else
                return new ResponseEntity<>(new ESMSError("User with given id not exists"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    public UserInfo fromRegisterRequestToUserInfo(RegisterRequest registerRequest) {
        return UserInfo.builder()
                .pwd(encoder.encode(registerRequest.getPassword()))
                .empCode(registerRequest.getEmpCode())
                .email(registerRequest.getEmail())
                .createdDate(new Date())
                .modifiedDate(new Date())
                .isActive(true)
                .priviledgeId(registerRequest.getPriviledgeId())
                .build();
    }

    public UserInfoDto fromUserInfoToUserInfoDto(UserInfo userInfo) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(userInfo.getUserId());
        userInfoDto.setEmail(userInfo.getEmail());
        userInfoDto.setEmpCode(userInfo.getEmpCode());
        userInfoDto.setActive(userInfo.isActive());
        userInfoDto.setPriviledgeId(userInfo.getPriviledgeId());
        return userInfoDto;
    }

    @Override
    public boolean checkPermission(String username, String url) {
        UserInfo userInfo = findUserInfoByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        AuthPriviledgeLookUp authPriviledgeLookUp = authRepository.findById(userInfo.getPriviledgeId()).orElseThrow(() -> new RuntimeException("role not found"));
        String[] roles = authPriviledgeLookUp.getPriviledge().split("#");
        List<String> role = Arrays.asList(roles);
        url = url.substring(1);
        String urlParts[] = url.split("/");
        if (role.contains("ADMIN")) {
            return true;
        } else {
            //check for dashboard
            if(urlParts[0].equals("dashboard"))
            {
                return true;
            }

            //check permission for Employee
            if (urlParts[0].equals("employee")) {
                if (urlParts[1].startsWith("get")) {
                    if (role.contains("EV"))
                        return true;
                    else
                        return false;
                } else if (urlParts[1].startsWith("update")) {
                    if (role.contains("EU"))
                        return true;
                    else
                        return false;
                } else if (urlParts[1].startsWith("save")) {
                    if (role.contains("ES"))
                        return true;
                    else
                        return false;
                } else if (urlParts[1].startsWith("delete")) {
                    if (role.contains("ED"))
                        return true;
                    else
                        return false;
                }
            }
            //check permission for department
            if (urlParts[0].equals("department") || urlParts[0].equals("team")) {
                if (urlParts[1].startsWith("get")) {
                    if (role.contains("DV"))
                        return true;
                    else
                        return false;
                } else if (urlParts[1].startsWith("update")) {
                    if (role.contains("DU"))
                        return true;
                    else
                        return false;
                } else if (urlParts[1].startsWith("save")) {
                    if (role.contains("DS"))
                        return true;
                    else
                        return false;
                } else if (urlParts[1].startsWith("delete")) {
                    if (role.contains("DD"))
                        return true;
                    else
                        return false;
                }
            }
            //check permission for reports
            if (urlParts[0].equals("reports")) {
                if (role.contains("RV") || role.contains("RW"))
                    return true;
                else
                    return false;
            }
            //check permission for logs
            if (urlParts[0].equals("activityLogs")) {
                if (urlParts[1].startsWith("get")) {
                    if (role.contains("LV"))
                        return true;
                    else
                        return false;
                }
            }
        }
        return false;
    }

    //check email exists in userInfo table
    @Override
    public ResponseEntity<?> checkEmailExists(String email) {
        try {
            UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not exists"));
            return new ResponseEntity<>(userInfo.getEmpCode(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Wrong email"), HttpStatus.BAD_REQUEST);
        }
    }

    //send otp code for forgot password
    @Override
    public ResponseEntity<?> forgotPassword(String mail) {
        try {
            UserInfo userInfo = userInfoRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
            OTP o = new OTP();
            o.setEmail(mail);
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            o.setOtp(String.valueOf(number));
            long millis = Calendar.getInstance().getTimeInMillis();
            o.setExpiry(new Date(millis + 5 * 60 * 1000));
            otpRepo.save(o);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setSubject("OTP for password reset.");
            message.setText("Your OTP is : " + String.valueOf(number));
            mailSender.send(message);
            return new ResponseEntity<>(new ESMSError("Otp sent to your mail"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //set new password using otp
    @Override
    public ResponseEntity<?> setPwdWithOtp(String mail, String otp, String password) {
        try {
            OTP o = otpRepo.findById(mail).orElseThrow(() -> new RuntimeException("Wrong details"));
            if (o.getOtp().equals(otp)) {
                if (o.getExpiry().after(new Date())) {
                    UserInfo userInfo = userInfoRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
                    userInfo.setPwd(encoder.encode(password));
                    userInfoRepository.save(userInfo);
                    return new ResponseEntity<>(new ESMSError("password Changed"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ESMSError("OTP expired"), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ESMSError("Wrong OTP"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //update user password by taking user current password
    @Override
    public ResponseEntity<?> updateUserPassword(String mail, String current, String password) {
        try {
            UserInfo userInfo = userInfoRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
            if (encoder.matches(current, userInfo.getPassword())) {
                userInfo.setPwd(encoder.encode(password));
                userInfoRepository.save(userInfo);
                return new ResponseEntity<>(new ESMSError("Password changed"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ESMSError("Wrong password"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //verify OTP
    @Override
    public ResponseEntity<?> verifyOtp(String mail, String otp) {
        try {
            OTP o = otpRepo.findById(mail).orElseThrow(() -> new RuntimeException("Wrong details"));
            if (o.getOtp().equals(otp)) {
                if (o.getExpiry().after(new Date())) {
                    return new ResponseEntity<>(new ESMSError("true"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ESMSError("OTP expired"), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ESMSError("false"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    //activate user by its mail
    @Override
    public ResponseEntity<?> activateUser(String mail) {
        try {
            UserInfo userInfo = userInfoRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
            if (userInfo.isActive()) {
                return new ResponseEntity<>("Already active", HttpStatus.BAD_REQUEST);
            } else {
                userInfo.setActive(true);
                userInfoRepository.save(userInfo);
                return new ResponseEntity<>("Activated", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
