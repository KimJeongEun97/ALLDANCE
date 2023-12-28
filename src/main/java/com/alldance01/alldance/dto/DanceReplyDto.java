package com.alldance01.alldance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class DanceReplyDto {
    private int dbr_num;
    private int dbr_dnum;
    private String dbr_mid;
    private String dbr_contents;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    private Timestamp dbr_date;
}
