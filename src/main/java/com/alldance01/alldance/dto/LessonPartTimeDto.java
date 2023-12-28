package com.alldance01.alldance.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
public class LessonPartTimeDto {
    private int lpt_num;
    private int lpt_chonum;
    private String lpt_choname;
    private String lpt_parttime;
    private String lpt_ptimedate;
}

