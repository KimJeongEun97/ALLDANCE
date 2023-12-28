package com.alldance01.alldance.service;

import com.alldance01.alldance.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class MailService {
    //의존성 주입을 통해서 필요한 객체를 가져온다.
    @Autowired
    private JavaMailSender emailSender;
    // 타임리프를사용하기 위한 객체를 의존성 주입으로 가져온다
    @Autowired
    private SpringTemplateEngine templateEngine;

    //메일 양식 작성
    public MimeMessage createEmailForm(MailDto email) throws MessagingException, UnsupportedEncodingException {

        String setFrom = "hg88521@naver.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email.getEmail(); //받는 사람
        String title = "비밀번호 변경 링크"; //제목

        String link = "http://localhost/findPwChange?m_id="+email.getM_id();

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext("<a href='"+ link + "' style='text-decoration: none'>비밀번호 변경</a>"), "utf-8", "html");

        return message;
    }

    //실제 메일 전송
    public String sendEmail(MailDto mail) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(mail);
        //실제 메일 전송
        emailSender.send(emailForm);

        return "ok"; //인증 코드 반환
    }

    //타임리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context); //mail.html
    }
}
