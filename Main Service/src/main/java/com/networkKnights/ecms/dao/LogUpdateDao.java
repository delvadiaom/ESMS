package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.LogUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface LogUpdateDao extends JpaRepository < LogUpdate, Long > {

}
