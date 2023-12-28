package com.alldance01.alldance.dao;

import com.alldance01.alldance.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LessonListDao {
    //레슨리스트 조회
    List<LessonListDto> selectLessonList(SearchDto sDto);
    //레슨리스트의 안무가 조회
    List<LessonChoreoDto> selectChoreoList(SearchDto sDto);
    //레슨리스트의 안무가 파트타임 조회
    List<LessonPartTimeDto> selectPartTimeList(SearchDto sDto);

    //레슨리스트 개수 조회
    int selectLessonListCnt(SearchDto sDto);

    //학원 레슨 등록
    void insertLessonList(LessonListDto lesson);

    //학원 레슨 이미지 등록
    void insertLsImgFile(LessonImgFileDto lifd);

    //학원 레슨 안무가 등록
    void insertLessonChoreo(LessonChoreoDto choreo);

    //학원 레슨 안무가 이미지 등록
    void insertLcImgFile(LessonChoreoImgDto lcid);

    //안무가등록 페이지 이동시 아카데미 찾기
    LessonListDto selectAcademy(int ls_num);

    //안무가 파트타임 페이지 이동시 안무가 이름 찾기
    LessonChoreoDto selectChoreo(int cho_num);

    //안무가 번호를 통해 파트타임 조회
    List<LessonPartTimeDto> selectParttimeList(int lpt_num);
    //파트타임 등록
    void insertParttime(LessonPartTimeDto parttime);

    //마지막 파트타임 조회
    LessonPartTimeDto selectLastParttime(int lpt_num);

    //파트타임 삭제
    void deleteParttime(int lpt_num);
    //레슨정보 하나 가져오기
    LessonListDto selectLessonView(int ls_num);
    //레슨번호를 통해 안무가 조회
    LessonChoreoDto selectChoreoView(int ls_num);
    //안무가 번호를 통해 이미지 목록 조회
    List<LessonChoreoImgDto> selectChoreoImgfile(int cho_num);
    //안무가 번호를 통해 파트타임 조회
    List<LessonPartTimeDto> selectParttimeView(int cho_num);

    //학원 이미지 삭제 목록 구하기
    List<String> selectLessonImgList(int ls_num);
    //안무가 이미지 삭제 목록 구하기
    List<String> selectChoreoImgList(int cho_num);
    //레슨 삭제시 파트타임 삭제
    void lessonDeleteParttime(int cho_num);
    //레슨 삭제시 안무가 이미지 삭제
    void lessonDeleteChoreoImg(int cho_num);
    //레슨 삭제시 안무가 삭제
    void lessonDeleteChoreo(int cho_num);
    //레슨 삭제시 학원 이미지 삭제
    void lessonDeleteImg(int ls_num);
    //레슨 삭제
    void lessonDelete(int ls_num);
    //수정할 학원정보 가져오기
    LessonListDto selectLesson(int ls_num);
    //수정할 학원 이미지 가져오기
    List<LessonImgFileDto> selectLessonImg(int ls_num);
    //수정한 학원 정보 저장
    void lessonUpdate(LessonListDto lesson);
    //수정한 학원 이미지 저장
    void insertLessonImgFile(LessonImgFileDto lsif);
    //수정시 학원 이미지 삭제
    void lessonImgDelete(String lif_sysname);
    //수정할 안무가 정보 가져오기
    LessonChoreoDto selectLessonChoreo(int ls_num);
    //수정할 안무가 이미지 가져오기
    List<LessonChoreoImgDto> selectChoreoImg(int cho_num);
    //수정한 안무가 저장
    void lessonChoreoUpdate(LessonChoreoDto choreo);
    //수정한 안무가 이미지 저장
    void insertChoreoImgFile(LessonChoreoImgDto lcif);
    //수정시 안무가 이미지 삭제
    void choreoImgDelete(String lcif_sysname);
    //이전 파트타임 목록 조회
    List<LessonPartTimeDto> selectLastPartTimeList(int lpt_chonum);
    //이전 파트타임 삭제
    void lessonParttimeDelete(LessonPartTimeDto parttime);
    //예약하기 기능
    void insertReservation(LessonReservationListDto reservation);
    //개인회원 예약한 목록 조회
    List<LessonReservationListDto> selectLessonReservationList(SearchDto sDto);
    //개인회원 예약한 목록 5개로 카운트
    int selectReservationListCnt(SearchDto sDto);
    //개인회원 예약한 내용 출력
    LessonReservationListDto selectLessonReserv(int lrl_num);
    //개인회원 예약한 내용의 안무가 필모영상이미지 출력
    List<LessonChoreoImgDto> selectReservChoreoImg(int lrl_chonum);
    //예약취소
    void deleteReservation(int lrl_num);
    //사업자회원 예약현황 목록 조회
    List<LessonListDto> selectLessonStatusList(SearchDto sDto);
    //사업자회원 예약 현황 목록 5개로 카운트
    int selectStatusListCnt(SearchDto sDto);
    //사업자회원 예약 현황 목록으로 상세보기
    List<LessonReservationListDto> selectLessonStatusDetail(int ls_num);
    //사업자회원 고객 예약 취소
    void deleteStatus(int lrl_num);
}
