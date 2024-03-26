package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.RoleLookUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleLookUpDao extends JpaRepository<RoleLookUp,Long> {
    @Query("SELECT r.roleName FROM RoleLookUp r WHERE r.roleId = :id")
    String findRoleNameById(@Param("id") Long id);
}
