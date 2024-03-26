package com.networkKnights.ecms_auth.dao;

import com.networkKnights.ecms_auth.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepo extends JpaRepository<OTP,String> {
}
