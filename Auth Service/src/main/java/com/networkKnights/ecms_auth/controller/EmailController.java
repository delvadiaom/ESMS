package com.networkKnights.ecms_auth.controller;

import com.networkKnights.ecms_auth.constant.EmailConstant;
import com.networkKnights.ecms_auth.constant.URIConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(URIConstant.URI_EMAIL)
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendMail(Map<String, String> mailData) {
        try {
            //System.out.println("calling\n"+mailData);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(EmailConstant.EMAIL_FROM);
            message.setTo(mailData.get(EmailConstant.MAP_EMAIL_TO));
            if (mailData.containsKey(EmailConstant.MAP_EMAIL_CC) && !"".equals(mailData.get(EmailConstant.MAP_EMAIL_CC))) {
                message.setCc(mailData.get(EmailConstant.MAP_EMAIL_CC));
            }
            if (mailData.containsKey(EmailConstant.MAP_EMAIL_BCC) && !"".equals(mailData.get(EmailConstant.MAP_EMAIL_BCC))) {
                message.setBcc(mailData.get(EmailConstant.MAP_EMAIL_BCC));
            }
            message.setSubject(mailData.get(EmailConstant.MAP_EMAIL_SUBJECT));
            message.setText(mailData.get(EmailConstant.MAP_EMAIL_DATA));
            System.out.println("sending..");
            mailSender.send(message);
            System.out.println("send");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping(URIConstant.URI_INFORM_SALARY_CHANGE)
    private boolean informEmployeeAboutSalaryChange(@RequestBody Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        map.put(EmailConstant.MAP_EMAIL_TO, data.get(EmailConstant.MAP_EMAIL_TO));
        map.put(EmailConstant.MAP_EMAIL_SUBJECT, "Update About Salary Change");
        map.put(EmailConstant.MAP_EMAIL_DATA, "Your old salary was " + data.get("oldSalary") + ".\n\n" +
                "Your new salary is " + data.get("newSalary") +
                " ( Base pay: " + data.get("baseSalary") + " + " +
                "PF: " + data.get("pf") + " + " +
                "HRA: " + data.get("hra") + " + " +
                "other: " + data.get("other") +
                " ).");
        sendMail(map);
        return false;
    }

}
