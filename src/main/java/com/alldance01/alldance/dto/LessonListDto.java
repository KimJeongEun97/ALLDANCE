package com.alldance01.alldance.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LessonListDto {
    private int ls_num;
    private String ls_academy;
    private String ls_addr;
    private String ls_title;
    private Timestamp ls_date;
    private int ls_total;
    private String ls_mid;
    private int ls_price;
    private String lif_oriname;
    private String lif_sysname;
}
