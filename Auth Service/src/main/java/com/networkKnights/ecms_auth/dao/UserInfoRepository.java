package com.networkKnights.ecms_auth.dao;

import com.networkKnights.ecms_auth.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByEmail(String email);
    boolean existsByPriviledgeId(long id);

    //String getEmpCodeByEmail(String email);
}

