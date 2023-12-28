package com.alldance01.alldance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonReservationListDto {
    private int lrl_num;
    private String lrl_mid;
    private String m_name;
    private String m_phone;
    private int lrl_lsnum;
    private String lrl_lsacademy;
    private String lrl_lsaddr;
    private String lrl_lstitle;
    private int lrl_chonum;
    private String lrl_choname;
    private String lrl_lptparttime;
    private String lrl_lptptimedate;
    private int lrl_lstotal;
    private int lrl_lsprice;
    private String lif_oriname;
    private String lif_sysname;
}