package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.DanceBoardDto;
import com.alldance01.alldance.dto.DanceImgFileDto;
import com.alldance01.alldance.dto.DanceReplyDto;
import com.alldance01.alldance.dto.DanceVideoFileDto;
import com.alldance01.alldance.service.DanceBoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DanceBoardRestController {
    @Autowired
    DanceBoardService dbServ;
    //댓글 작성 처리 메소드
    @PostMapping("replyInsert")
    public DanceReplyDto replyInsert(DanceReplyDto reply, DanceBoardDto dbDto){
        log.info("replyInsert()");
        reply = dbServ.replyInsert(reply, dbDto);
        return reply;
    }
    //게시글 수정시 비디오파일 삭제
    @PostMapping("delVideoFile")
    public List<DanceVideoFileDto> delVideoFile(DanceVideoFileDto dvFile, HttpSession session){
        log.info("delVideoFile()");
        List<DanceVideoFileDto> vfList = dbServ.delVideoFile(dvFile, session);
        return vfList;
    }
    //게시글 수정시 이미지 파일 삭제
    @PostMapping("delImgFile")
    public List<DanceImgFileDto> delImgFile(DanceImgFileDto diFile, HttpSession session){
        log.info("delImgFile()");
        List<DanceImgFileDto> ifList = dbServ.delImgFile(diFile, session);
        return ifList;
    }

}
