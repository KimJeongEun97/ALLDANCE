package com.alldance01.alldance.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class DanceBoardDto {
    private int d_num;
    private String d_title;
    private String d_category;
    private String d_contents;
    private String d_mid;
    private Timestamp d_date;
    private int d_views;
    private String dif_oriname;
    private String dif_sysname;
}
