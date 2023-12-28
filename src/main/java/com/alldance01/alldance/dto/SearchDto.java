package com.alldance01.alldance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto{
    private String m_id;
    private String colname;
    private String keyword;
    private int pageNum;
    private int listCnt;
}
