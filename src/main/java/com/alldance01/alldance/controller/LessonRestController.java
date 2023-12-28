package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.*;
import com.alldance01.alldance.service.LessonListService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@Slf4j
public class LessonRestController {
    @Autowired
    LessonListService lsServ;
    //안무가 파트타임 등록
    @PostMapping("parttimeInsert")
    public LessonPartTimeDto parttimeInsert(LessonPartTimeDto parttime){
        log.info("parttimeInsert()");
        parttime = lsServ.parttimeInsert(parttime);
        return parttime;
    }
    //레슨 학원 수정시 이미지 파일 삭제
    @PostMapping("lsDelImgFile")
    public List<LessonImgFileDto>delImgFile(LessonImgFileDto liFile, HttpSession session){
        log.info("lsDelImgFile()");
        List<LessonImgFileDto> lifList = lsServ.lsDelImgFile(liFile, session);
        return lifList;
    }
    //레슨 안무가 수정시 이미지 파일 삭제
    @PostMapping("lcDelImgFile")
    public List<LessonChoreoImgDto>lcDelImgFile(LessonChoreoImgDto lciFile, HttpSession session){
        log.info("lcDelImgFile()");
        List<LessonChoreoImgDto> lcifList = lsServ.lcDelImgFile(lciFile, session);
        return lcifList;
    }
}
