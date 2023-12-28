package com.alldance01.alldance.dao;

import com.alldance01.alldance.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    //아이디 중복 확인
    int selectId(String m_id);
    //이메일 중복 확인
    int selectEmail(String m_email);
    //회원 가입 정보 DB 넘김
    void insertMember(MemberDto member);
    //비밀번호 조회
    String selectPwd(String m_id);
    //view에 있는 멤버조회
    MemberDto selectMember(String m_id);
    //이름과 휴대폰번호를 조회하여 아이디 찾기
    String selectFoundId(String m_name, String m_phone);

    //아이디를 조회하여 회원정보 찾기
    MemberDto selectMemberInfo(String m_id);

    //비밀번호 찾기 아이디 조회
    String selectIdInquiry(String m_id, String m_email);

    //비밀번호 변경 처리
    void updatePassword(MemberDto member);
    //마이페이지 변경 처리
    void updateMypage(MemberDto member);
}
