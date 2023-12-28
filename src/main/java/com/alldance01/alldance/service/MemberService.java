package com.alldance01.alldance.service;

import com.alldance01.alldance.dao.MemberDao;
import com.alldance01.alldance.dto.MemberDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Service
@Slf4j
public class MemberService {
    @Autowired
    MemberDao mDao;

    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;
    //비밀번호 인코더
    private BCryptPasswordEncoder pEncoder = new BCryptPasswordEncoder();

    //아이디 체크
    public String idCheck(String m_id) {
        log.info("idCheck()");
        String result = "";
        int mcnt = mDao.selectId(m_id);
        if (mcnt == 0) {
            result = "ok";
        } else {
            result = "fail";
        }
        return result;
    }

    //이메일 체크
    public String emailCheck(String m_email) {
        log.info("emailCheck()");
        String result = "";
        int mcnt = mDao.selectEmail(m_email);
        if (mcnt == 0) {
            result = "ok";
        } else {
            result = "fail";
        }
        return result;
    }

    //회원가입 처리
    public String mJoin(MemberDto member, RedirectAttributes rttr) {
        log.info("mJoin()");
        String view = null;
        String msg = null;

        //비밀번호 암호화
        String encpwd = pEncoder.encode(member.getM_pwd());
        log.info(encpwd);
        member.setM_pwd(encpwd);//암호화문구로 덮기

        try {
            mDao.insertMember(member);
            msg = "가입에 성공했습니다.";
            view = "redirect:loginForm";
        }catch (Exception e){
            e.printStackTrace();
            msg = "가입 실패";
            view = "redirect:joinForm";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //로그인 처리
    public String mLogin(MemberDto member, HttpSession session, RedirectAttributes rttr) {
        log.info("loginProc()");
        String view = null;
        String msg = null;
        String encPwd = mDao.selectPwd(member.getM_id());
        if (encPwd != null){
            if (pEncoder.matches(member.getM_pwd(), encPwd)){
                member = mDao.selectMember(member.getM_id());
                session.setAttribute("member",member);
                view = "redirect:/";
            }else {
                view = "redirect:loginForm";
                msg = "비밀번호가 틀립니다.";
            }
        }else {
            view = "redirect:joinForm";
            msg = "아이디가 존재하지 않습니다. 회원가입 후 이용바랍니다";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //로그아웃 처리
    public void logout(HttpSession session) {
        log.info("logout()");
        session.invalidate();
    }

    //아이디 찾기 처리
    public String mfindId(String m_name, String m_phone, HttpSession session, RedirectAttributes rttr) {
        log.info("findIdProc()");
        String view = null;
        String msg = null;

        String foundId = mDao.selectFoundId(m_name, m_phone);
        if (foundId != null){
            session.setAttribute("mid", foundId);
            view = "redirect:findIdComplete";
            msg = "아이디를 조회하였습니다.";
        }else {
            view = "redirect:findId";
            msg = "회원정보가 존재하지 않습니다.";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //비밀번호 변경 처리
    public String findPwChangeProc(MemberDto member, HttpSession session, RedirectAttributes rttr) {
        log.info("findPwChangeProc()");
        String view = null;
        String msg = null;

        try {
            String encpwd = pEncoder.encode(member.getM_pwd());
            member.setM_pwd(encpwd);//암호화문구로 덮기
            mDao.updatePassword(member);
            msg = "변경 완료";
            view = "redirect:loginForm";
        }catch (Exception e){
            e.printStackTrace();
            msg = "변경 실패";
            view = "redirect:findPwChange";
        }

        return view;
    }

    //마이페이지 회원정보 가져오기
    public ModelAndView myPage(HttpSession session) {
        log.info("myPageInfo()");
        ModelAndView mv = new ModelAndView();

        MemberDto member = (MemberDto) session.getAttribute("member");

        if (member != null) {
            MemberDto memberInfo = mDao.selectMemberInfo(member.getM_id());
            mv.addObject("member", memberInfo);
            mv.setViewName("myPage");
        } else {
            mv.setViewName("redirect:myPage");
        }

        return mv;
    }

    //마이페이지 회원정보 저장
    public String updateMyPage(MemberDto member, HttpSession session, RedirectAttributes rttr) {
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        try {
            mDao.updateMypage(member);
            String encpwd = pEncoder.encode(member.getM_pwd());
            member.setM_pwd(encpwd);
            mDao.updatePassword(member);
            manager.commit(status);
            view = "redirect:/";
            msg = "수정되었습니다.";
        }catch (Exception e){
            manager.rollback(status);
            view = "redirect:myPage";
            msg = "수정 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }
}

















