package com.alldance01.alldance.controller;

import com.alldance01.alldance.dto.LessonReservationListDto;
import com.alldance01.alldance.service.LessonListService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@Slf4j
public class PayController {
    @Autowired
    private LessonListService lsServ;

    @PostMapping("lsReservProc")
    @ResponseBody
    public String lsReservProc(@RequestBody LessonReservationListDto reservation, String m_id, HttpSession session, RedirectAttributes rttr){
        log.info("lsReservProc()");
        String view = lsServ.lsReservProc(reservation, m_id, session, rttr);
        return view;
    }
}
