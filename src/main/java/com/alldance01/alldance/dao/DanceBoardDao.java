package com.alldance01.alldance.dao;

import com.alldance01.alldance.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DanceBoardDao {
    //게시판 목록 조회
    List<DanceBoardDto> selectDanceBoardList(SearchDto sDto);

    //게시판 목록 개수 조회
    int selectDanceBoardCnt(SearchDto sDto);

    //게시글 작성
    void insertDanceBoard(DanceBoardDto dance);

    //게시글작성 비디오파일
    void insertdbVideoFile(DanceVideoFileDto dvfd);

    //게시글작성 섬네일이미지 파일
    void insertdbImgFile(DanceImgFileDto difd);

    //게시글작성 카테고리 저장
    void insertCategory(String dCategory);

    //다른 회원 게시글 조회시 조회수 증가
    void updateViewPoint(DanceBoardDto dbDto);
    //게시글 하나 가져오기
    DanceBoardDto selectDance(int d_num);
    //게시글 이미지 가져오기
    List<DanceImgFileDto> selectImgFileList(int d_num);
    //게시글 동영상 가져오기
    List<DanceVideoFileDto> selectVideoFileList(int d_num);
    //영상파일의 저장 이름 목록 구하는 메소드
    List<String> selectVideoNameList(int d_num);
    //섬네일 이미지의 저장 이름 목록 구하는 메소드
    List<String> selectImgNameList(int d_num);
    //게시글 번호에 해당하는 섬네일 이미지
    void deleteImgFiles(int d_num);
    //게시글 번호에 해당하는 영상 삭제
    void deleteVideoFiles(int d_num);
    //게시글 삭제
    void deleteDanceBoard(int d_num);
    //게시글 번호에 해당하는 댓글목록을 가져오기
    List<DanceReplyDto> selectReplyList(int d_num);
    //댓글 작성
    void insertReply(DanceReplyDto reply);
    //저장 시 생성된 댓글 번호로 정보 가져오기
    DanceReplyDto selectLastReply(int dbr_dnum);
    //댓글 한개 삭제
    void deleteReply(int dbr_num);
    //게시글 번호에 해당하는 댓글 목록 삭제
    void deleteReplyList(int d_num);
    //게시글 수정 처리
    void danceBoardUpdate(DanceBoardDto dance);
    //게시글 수정시 비디오파일 삭제
    void deleteVideoFile(String dvf_sysname);
    //게시글 수정시 섬네일 이미지파일 삭제
    void deleteImgFile(String dif_sysname);
}
