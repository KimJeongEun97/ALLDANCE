package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.MailDto;
import com.alldance01.alldance.service.MailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
@Slf4j
public class MailController {
    @Autowired
    private MailService mServ;

    //비밀번호 찾기 이메일 인증
    @PostMapping("mailConfirm")
    @ResponseBody
    public String mailConfirm(MailDto mail) throws MessagingException, UnsupportedEncodingException {

        String authCode = mServ.sendEmail(mail);
        return authCode;
    }
}
