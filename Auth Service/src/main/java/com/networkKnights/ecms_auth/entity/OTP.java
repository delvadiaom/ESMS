package com.networkKnights.ecms_auth.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name="auth_otp",schema = "dbo")
public class OTP {
    @Id
    @Column(name="email",nullable = false)
    private String  email;
    @Column(name="password",nullable = false,length = 100)
    private String otp;
    @Column(name="expiry",nullable = false)
    private Date expiry;
}
