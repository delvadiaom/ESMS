package com.networkKnights.ecms_auth.entity;

import com.networkKnights.ecms_auth.dao.UserInfoRepository;
import com.networkKnights.ecms_auth.service.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Entity
@Builder
@Data
@NoArgsConstructor
@Table(name="auth_users",schema = "dbo")
public class UserInfo implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private long userId;
    @Column(name="password",nullable = false,length = 100)
    private String pwd;
    @Column(name="empCode",unique = true,nullable = false)
    private String empCode;
    @Column(name="email",nullable = false,unique = true,length = 100)
    private String email;
    @Column(name="priviledge_id",nullable = false)
    private long priviledgeId;
    @Column(name="created_date",nullable = false)
    private Date createdDate;
    @Column(name="modified_date",nullable = false)
    private Date modifiedDate;
    @Column(name="is_active",nullable = false)
    private boolean isActive;



    public UserInfo(long userId, String pass, String empCode, String email, long privilegeId, Date createdDate, Date modifiedDate, boolean isActive) {
        this.userId = userId;
        this.pwd= pass;
        this.empCode = empCode;
        this.email = email;
        this.priviledgeId = privilegeId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(String.valueOf(priviledgeId).split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
