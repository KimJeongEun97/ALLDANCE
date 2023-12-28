package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.*;
import com.alldance01.alldance.service.LessonListService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class LessonController {
    @Autowired
    private LessonListService lsServ;

    //클래스 목록 이동
    @GetMapping("lessonList")
    public ModelAndView lessonList(SearchDto sDto, LessonListDto lsDto, HttpSession session) {
        log.info("lessonList()");
        ModelAndView mv = lsServ.lsListMove(sDto, lsDto, session);
        return mv;
    }

    //클래스 등록 이동
    @GetMapping("lessonAForm")
    public String lessonAForm() {
        log.info("lessonAForm()");
        return "lessonAForm";
    }

    //클래스 작성 처리
    @PostMapping("lessonAProc")
    public String lessonAProc(@RequestPart List<MultipartFile> lsimgfiles,
                              LessonListDto lesson,
                              RedirectAttributes rttr,
                              HttpSession session) {
        log.info("lessonAProc()");
        String view = lsServ.lessonAProc(lsimgfiles, lesson, rttr, session);
        return view;
    }

    //안무가 등록 이동
    @GetMapping("lessonCForm")
    public ModelAndView lessonCForm(int ls_num) {
        log.info("lessonCForm()");
        ModelAndView mv = lsServ.academyInfo(ls_num);
        return mv;
    }

    //안무가 등록 처리
    @PostMapping("lessonCProc")
    public String lessonCProc(@RequestPart List<MultipartFile> lcimgfiles,
                              LessonChoreoDto choreo,
                              RedirectAttributes rttr,
                              HttpSession session) {
        log.info("lessonCProc()");
        String view = lsServ.lessonCProc(lcimgfiles, choreo, rttr, session);
        return view;
    }

    //안무가 파트타임 등록 이동
    @GetMapping("lessonTForm")
    public ModelAndView lessonTForm(int cho_num, LessonPartTimeDto parttime) {
        log.info("lessonTForm()");
        ModelAndView mv = lsServ.selectChoreo(cho_num, parttime);
        return mv;
    }

    //안무가 파트타임 삭제
    @GetMapping("parttimeDelete")
    public String parttimeDelete(@RequestParam Optional<Integer> cho_num, int lpt_num, RedirectAttributes rttr) {
        log.info("parttimeDelete()");
        String view = lsServ.parttimeDelete(cho_num, lpt_num, rttr);
        return view;
    }

    //레슨 상세보기 페이지 이동
    @GetMapping("lessonDetail")
    public ModelAndView lessonDetail(LessonListDto lsDto, HttpSession session) {
        log.info("lessonDetail()");
        ModelAndView mv = lsServ.lessonDetail(lsDto, session);
        return mv;
    }

    //레슨 삭제하기
    @GetMapping("lessonDelete")
    public String lessonDelete(int ls_num, RedirectAttributes rttr, HttpSession session) {
        log.info("lessonDelete()");
        String view = lsServ.lessonDelete(ls_num, rttr, session);
        return view;
    }

    //레슨 학원 수정페이지 이동
    @GetMapping("lessonAUpdate")
    public ModelAndView lessonAUpdate(int ls_num){
        log.info("lessonAUpdate()");
        ModelAndView mv = lsServ.lessonAUpdate(ls_num);
        return mv;
    }
    //레슨 학원 수정 처리
    @PostMapping("lsAUpdateProc")
    public String lsAUpdateProc(List<MultipartFile> lsimgfiles,
                               LessonListDto lesson,
                               HttpSession session,
                               RedirectAttributes rttr){
        log.info("lsAUpdateProc()");
        String view = lsServ.lsAUpdateProc(lsimgfiles,lesson,session,rttr);
        return view;
    }
    //레슨 안무가 수정페이지 이동
    @GetMapping("lessonCUpdate")
    public ModelAndView lessonCUpdate(int ls_num){
        log.info("lessonCUpdate()");
        ModelAndView mv = lsServ.lessonCUpdate(ls_num);
        return mv;
    }
    //레슨 안무가 수정 처리
    @PostMapping("lsCUpdateProc")
    public String lsCUpdateProc(List<MultipartFile> lcimgfiles,
                                LessonChoreoDto choreo,
                                HttpSession session,
                                RedirectAttributes rttr){
        log.info("lsCUpdateProc()");
        String view = lsServ.lsCUpdateProc(lcimgfiles,choreo,session,rttr);
        return view;
    }
    //레슨 안무가 파트타임 수정페이지 이동
    @GetMapping("lessonTUpdate")
    public ModelAndView lessonTUpdate(int cho_num,LessonPartTimeDto parttime){
        log.info("lessonTUpdate()");
        ModelAndView mv = lsServ.lessonTUpdate(cho_num, parttime);
        return mv;
    }
    //안무가 파트타임 삭제
    @GetMapping("lastParttimeDelete")
    public String lastParttimeDelete(int lpt_num, int cho_num, RedirectAttributes rttr) {
        log.info("lastParttimeDelete()");

        String view = lsServ.lastParttimeDelete(lpt_num, cho_num, rttr);
        return view;
    }

    //레슨 예약하기
//    @PostMapping("lsReservProc")
//    public String lsReservProc(LessonReservationListDto reservation,
//                               int ls_num,
//                               String lrl_mid,
//                               RedirectAttributes rttr,
//                               HttpSession session){
//        log.info("lsReservProc()");
//        String view = lsServ.lsReservProc(reservation, ls_num, lrl_mid, rttr, session);
//        return view;
//    }


    //레슨 예약 내역 목록 이동
    @GetMapping("lessonReservationList")
    public ModelAndView lessonReservationList(SearchDto sDto, LessonReservationListDto lsReservation, HttpSession session){
        log.info("lessonReservationList()");
        ModelAndView mv = lsServ.lsReservationListMove(sDto, lsReservation, session);
        return mv;
    }
    //레슨 예약 내역 상세보기 이동
    @GetMapping("lessonReservationDetail")
    public ModelAndView lessonReservationDetail(LessonReservationListDto lrDto, HttpSession session){
        log.info("lessonReservationDetail()");
        ModelAndView mv = lsServ.lrDetail(lrDto, session);
        return mv;
    }

    //레슨 예약 취소
    @GetMapping("lessonReservationDelete")
    public String lessonReservationDelete(int lrl_num, String m_id, RedirectAttributes rttr) {
        log.info("lessonReservationDelete()");
        String view = lsServ.lessonReservationDelete(lrl_num, m_id, rttr);
        return view;
    }

    //사업자 회원 등록한 레슨 현황 목록 보기
    @GetMapping("lessonStatusList")
    public ModelAndView lessonStatusList(SearchDto sDto, LessonListDto lesson, HttpSession session){
        log.info("lessonStatusList()");
        ModelAndView mv = lsServ.lessonStatusListMove(sDto, lesson, session);
        return mv;
    }
    //사업자 회원 등록한 레슨 현황 상세보기
    @GetMapping("lessonStatusDetail")
    public ModelAndView lessonStatusDetail(SearchDto sDto, int ls_num, HttpSession session){
        log.info("lessonStatusDetail()");
        ModelAndView mv = lsServ.lessonStatusDetail(sDto, ls_num, session);
        return mv;
    }
    //사업자 회원 고객예약취소
    @GetMapping("lessonStatusDelete")
    public String lessonStatusDelete(int lrl_num, int ls_num, RedirectAttributes rttr) {
        log.info("lessonStatusDelete()");
        String view = lsServ.lessonStatusDelete(lrl_num, ls_num, rttr);
        return view;
    }
}
