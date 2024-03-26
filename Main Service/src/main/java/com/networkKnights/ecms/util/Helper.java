package com.networkKnights.ecms.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class Helper {

    public String ChangeCosting(String oldCosting,String ctc,String flag){
//        2022:456987###2023:32005205
        int x = oldCosting.lastIndexOf(":");
        String change = oldCosting.substring(x+1);
        Long ans = 0L;
        if(flag.equals("add")) ans = Long.parseLong(change) + Long.parseLong(ctc);
        else if(flag.equals("remove")) ans = Long.parseLong(change) - Long.parseLong(ctc);
        return oldCosting.substring(0,x+1) + ans;
    }
}
