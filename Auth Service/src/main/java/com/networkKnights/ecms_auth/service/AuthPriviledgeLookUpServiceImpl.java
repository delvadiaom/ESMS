package com.networkKnights.ecms_auth.service;

import com.networkKnights.ecms_auth.dao.AuthPriviledgeLookUpRepository;
import com.networkKnights.ecms_auth.dao.UserInfoRepository;
import com.networkKnights.ecms_auth.dto.PrivilegeDto;
import com.networkKnights.ecms_auth.entity.AuthPriviledgeLookUp;
import com.networkKnights.ecms_auth.entity.ESMSError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthPriviledgeLookUpServiceImpl implements AuthPriviledgeLookUpService {
    @Autowired
    private AuthPriviledgeLookUpRepository repository;
    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public ResponseEntity<?> getPriviledgeDetails() {
        try {
            List<AuthPriviledgeLookUp> authPriviledgeLookUp = repository.findAll();
            return (authPriviledgeLookUp.size() > 0) ? new ResponseEntity<>(authPriviledgeLookUp, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No data is available"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getPriviledgeDetailsById(long id) {
        try {
            Optional<AuthPriviledgeLookUp> authPriviledgeLookUp = repository.findById(id);
            return (authPriviledgeLookUp.isPresent()) ? new ResponseEntity<>(authPriviledgeLookUp, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("Plese enter valid id"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> savePriviledgeDetails(PrivilegeDto privilegeDto) {
        if (privilegeDto.getPriviledgeName().isEmpty() || privilegeDto.getPriviledge().isEmpty()) {
            return new ResponseEntity<>(new ESMSError("data missing"), HttpStatus.BAD_REQUEST);
        }
        try {
            AuthPriviledgeLookUp authPriviledgeLookUp = new AuthPriviledgeLookUp();
            authPriviledgeLookUp.setPriviledge(privilegeDto.getPriviledge());
            authPriviledgeLookUp.setPriviledgeName(privilegeDto.getPriviledgeName());
            authPriviledgeLookUp.setCreatedDate(new Date());
            authPriviledgeLookUp.setModifiedDate(new Date());
            authPriviledgeLookUp = repository.save(authPriviledgeLookUp);
            return (authPriviledgeLookUp.getPriviledgeId() > 0) ? new ResponseEntity<>(new ESMSError("Role added"), HttpStatus.OK) : new ResponseEntity<>(new ESMSError("Please enter details carefully "), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Role already exists"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> updatePriviledgeDetails(PrivilegeDto privilegeDto) {
        if (privilegeDto.getPriviledgeName().isEmpty() || privilegeDto.getPriviledge().isEmpty()) {
            return new ResponseEntity<>(new ESMSError("data missing"), HttpStatus.BAD_REQUEST);
        }
        try {
            AuthPriviledgeLookUp privilege = repository.findById(privilegeDto.getPriviledgeId()).orElseThrow(() -> new RuntimeException("Role not exists"));
            privilege.setPriviledgeName(privilegeDto.getPriviledgeName());
            privilege.setPriviledge(privilegeDto.getPriviledge());
            privilege.setModifiedDate(new Date());
            privilege = repository.save(privilege);
            return (privilege.getPriviledgeId() > 0) ? new ResponseEntity<>(new ESMSError("Updated"), HttpStatus.OK) : new ResponseEntity<>(new ESMSError("Please enter details carefully "), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Role already exists"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> deletePriviledgeDetails(long id) {
        if (userInfoRepository.existsByPriviledgeId(id)) {
            return new ResponseEntity<>(new ESMSError("Can't delete some users have this role"), HttpStatus.OK);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(new ESMSError("deleted"), HttpStatus.OK);
    }
}
