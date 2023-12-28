package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.MemberDto;
import com.alldance01.alldance.service.MailService;
import com.alldance01.alldance.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@Slf4j
public class MemberController {
    @Autowired
    MemberService mServ;
    @Autowired
    MailService mailServ;

    //메인 페이지
    @GetMapping("/")
    public String home(){
        log.info("home()");
        return "index";
    }
    //소개글 페이지 이동
    @GetMapping("introduction")
    public String introduction(){
        log.info("introduction()");
        return "introduction";
    }

    //문의하기 페이지 이동
    @GetMapping("adInfo")
    public String adInfo(){
        log.info("adInfo()");
        return "adInfo";
    }

    //회원가입 페이지 이동
    @GetMapping("joinForm")
    public String joinForm(){
        log.info("joinForm()");
        return "joinForm";
    }
    //회원가입 처리
    @PostMapping("joinProc")
    public String joinProc(MemberDto member, RedirectAttributes rttr){
        log.info("joinProc()");
        String view = mServ.mJoin(member,rttr);
        return view;
    }
    //로그인 페이지 이동
    @GetMapping("loginForm")
    public String loginForm(){
        log.info("loginForm()");
        return "loginForm";
    }
    //로그인 처리
    @PostMapping("loginProc")
    public String loginProc(MemberDto member, HttpSession session, RedirectAttributes rttr){
        log.info("loginProc()");
        String view = mServ.mLogin(member, session, rttr);
        return view;
    }
    //로그아웃 처리
    @GetMapping("logout")
    public String logout(HttpSession session){
        log.info("logout()");
        mServ.logout(session);
        return "redirect:/";
    }

    //======================================================================================================

    //아이디 찾기 페이지 이동
    @GetMapping("findId")
    public String findId(){
        log.info("findId()");
        return "findId";
    }
    //아이디 찾기 처리
    @PostMapping("findIdProc")
    public String findIdProc (String m_name, String m_phone, HttpSession session, RedirectAttributes rttr){
        log.info("findIdProc()");
        String result = mServ.mfindId(m_name, m_phone, session, rttr);
        return result;
    }
    //아이디 찾기 완료된 페이지
    @GetMapping("findIdComplete")
    public String findIdComplete (){
        log.info("findIdComplete()");
        return "findIdComplete";
    }

    //======================================================================================================

    //비밀번호 찾기 페이지 이동
    @GetMapping("findPwCertification")
    public String findPwCertification(){
        log.info("findPwCertification()");
        return "findPwCertification";
    }

    //비밀번호 변경 페이지 이동
    @GetMapping("findPwChange")
    public String findPwChange(String m_id, Model model){
        log.info("findPwChange");
        model.addAttribute("m_id", m_id);
        return "findPwChange";
    }
    //비밀번호 변경 기능 처리
    @PostMapping("findPwChangeProc")
    public String findPwChangeProc(MemberDto member, HttpSession session, RedirectAttributes rttr){
        log.info("findPwChangeProc()");
        String result = mServ.findPwChangeProc(member,session,rttr);
        return result;
    }

    //======================================================================================================

    //마이페이지 이동
    @GetMapping("myPage")
    public ModelAndView myPage(HttpSession session){
        log.info("myPage()");
        ModelAndView mv = mServ.myPage(session);
        return mv;
    }

    //마이페이지 변경 정보 저장
    @PostMapping("myPageProc")
    public String myPageProc(MemberDto member, HttpSession session, RedirectAttributes rttr){
        log.info("myPageProc()");
        String view = mServ.updateMyPage(member, session, rttr);
        return view;
    }

}
