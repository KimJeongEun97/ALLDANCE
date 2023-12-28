package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.DanceBoardDto;
import com.alldance01.alldance.dto.DanceCategoryDto;
import com.alldance01.alldance.dto.SearchDto;
import com.alldance01.alldance.service.DanceBoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class DanceBoardController {
    @Autowired
    private DanceBoardService dbServ;

    //게시판 이동
    @GetMapping("danceBoardList")
    public ModelAndView danceBoardList(SearchDto sDto, DanceBoardDto dbDto, HttpSession session){
        log.info("danceBoardList()");
        ModelAndView mv = dbServ.dbListMove(sDto, dbDto, session);
        return mv;
    }
    //게시글 작성 이동
    @GetMapping("danceBoardWrite")
    public String danceBoardWrite(){
        log.info("danceBoardWrite()");
        return "danceBoardWrite";
    }

    //게시글 작성 처리 메소드
    @PostMapping("dbWriteProc")
    public String dbWriteProc(@RequestPart List<MultipartFile> dbvideofiles,
                            @RequestPart List<MultipartFile> dbimgfiles,
                            DanceBoardDto dance,
                            DanceCategoryDto category,
                            RedirectAttributes rttr,
                            HttpSession session){
        log.info("dbWriteProc()");
        String view = dbServ.dbWirte(dbvideofiles, dbimgfiles, dance, category, rttr, session);
        return view;
    }

    //게시글 상세보기
    @GetMapping("danceBoardDetail")
    public ModelAndView danceBoardDetail(DanceBoardDto dbDto, HttpSession session){
        log.info("danceBoardDetail() : {}", dbDto.getD_mid());
        ModelAndView mv = dbServ.dbDetail(dbDto, session);
        return mv;
    }

    //게시글 삭제
    @GetMapping("danceBoardDelete")
    public String danceBoardDelete(int d_num, RedirectAttributes rttr, HttpSession session){
        log.info("danceBoardDelete()");
        String view = dbServ.deleteDanceBoard(d_num, rttr, session);
        return view;
    }

    //게시글 수정페이지 이동
    @GetMapping("danceBoardUpdate")
    public ModelAndView danceBoardUpdate(int d_num){
        log.info("danceBoardUpdate()");
        ModelAndView mv = dbServ.danceBoardUpdateInfo(d_num);
        return mv;
    }
    //게시글 수정 처리
    @PostMapping("dbUpdateProc")
    public String dbUpdateProc(List<MultipartFile> dbvideofiles,
                               List<MultipartFile> dbimgfiles,
                                 DanceBoardDto dance,
                                 HttpSession session,
                                 RedirectAttributes rttr){
        log.info("dbUpdateProc()");
        String view = dbServ.updateDance(dbvideofiles,dbimgfiles,dance,session,rttr);
        return view;
    }

    //댓글 삭제 처리
    @GetMapping("replyDelete")
    public String replyDelete(int dbr_num, int d_num,RedirectAttributes rttr){
        log.info("replyDelete()");

        String view = dbServ.replyDelete(dbr_num, d_num, rttr);
        return view;
    }
}
