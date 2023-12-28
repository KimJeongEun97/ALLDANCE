package com.alldance01.alldance.controller;

import com.alldance01.alldance.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MemberRestController {
    @Autowired
    private MemberService mServ;

    //아이디 체크
    @GetMapping("idCheck")
    public String idCheck(String m_id){
        log.info("idCheck()");
        String res = mServ.idCheck(m_id);
        return res;
    }
    //이메일 체크
    @GetMapping("emailCheck")
    public String emailCheck(String m_email){
        log.info("emailCheck()");
        String res = mServ.emailCheck(m_email);
        return res;
    }
}
