package com.networkKnights.ecms_auth.dao;

import com.networkKnights.ecms_auth.entity.AuthPriviledgeLookUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthPriviledgeLookUpRepository extends JpaRepository<AuthPriviledgeLookUp,Long> {
}
