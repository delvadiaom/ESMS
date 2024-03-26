package com.networkKnights.ecms_auth.service;

import com.networkKnights.ecms_auth.dto.PrivilegeDto;
import com.networkKnights.ecms_auth.entity.AuthPriviledgeLookUp;
import org.springframework.http.ResponseEntity;

public interface AuthPriviledgeLookUpService {

    public ResponseEntity<?> getPriviledgeDetails();

    public ResponseEntity<?> getPriviledgeDetailsById(long id);

    public ResponseEntity<?> savePriviledgeDetails(PrivilegeDto privilegeDto);

    public ResponseEntity<?> updatePriviledgeDetails(PrivilegeDto privilegeDto);

    ResponseEntity<?> deletePriviledgeDetails(long id);
}
