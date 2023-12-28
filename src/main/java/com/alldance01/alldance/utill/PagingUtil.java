package com.alldance01.alldance.utill;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PagingUtil {
    private int maxNum; //전체 게시글 개수
    private int pageNum; //현재 보이는 페이지의 번호
    private int listCnt; //페이지 당 보여질 게시글 개수
    private int pageCnt; //보여질 페이지 번호의 개수
    private String listName; //게시판이 여러개일 때 페이징을 처리해야하는 목록 이름 저장

    public String makePaging() {
        String page = null;
        StringBuffer sb = new StringBuffer();
        int totalPage = (maxNum % listCnt) > 0 ?
                maxNum / listCnt + 1 :
                maxNum / listCnt;
        if (totalPage == 0) totalPage = 1;
        int curGroup = (pageNum % pageCnt) > 0 ?
                pageNum / pageCnt + 1 :
                pageNum / pageCnt;
        int start = (curGroup * pageCnt) - (pageCnt - 1);
        int end = (curGroup * pageCnt) >= totalPage ? totalPage : curGroup * pageCnt;
        if (start != 1) {
            sb.append("<a class='pno' href='/" + listName + "pageNum=" + (start - 1) + "'>◀</a>");
        }
        for (int i = start; i <= end; i++) {
            if (pageNum != i) {
                sb.append("<a class='pno' href='/" + listName + "pageNum=" + i + "'>" + i + "</a>");
            } else {
                sb.append("<font class='pno' style='color : red'>" + i + "</font>");
            }
        }
        if (end != totalPage) {
            sb.append("<a class='pno' href='/" + listName + "pageNum=" + (end + 1) + "'>▶</a>");
        }
        page = sb.toString();
        return page;
    }
}
