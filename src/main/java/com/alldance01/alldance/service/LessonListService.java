package com.alldance01.alldance.service;

import com.alldance01.alldance.dao.LessonListDao;
import com.alldance01.alldance.dto.*;
import com.alldance01.alldance.utill.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LessonListService {
    @Autowired
    private LessonListDao lsDao;

    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    private int lcnt = 5;

    //레슨 리스트 목록 처리
    public ModelAndView lsListMove(SearchDto sDto, LessonListDto lsDto, HttpSession session) {
        log.info("lsListMove()");
        ModelAndView mv = new ModelAndView();
        if (session.getAttribute("member") == null) {
            mv.setViewName("redirect:loginForm"); //로그인 폼으로 리다이렉트
            return mv; //메소드 종료
        }
        int num = sDto.getPageNum();
        if (num == 0) num = 1;
        if (sDto.getListCnt() == 0) {
            sDto.setListCnt(lcnt);
        }
        sDto.setPageNum((num - 1) * sDto.getListCnt());

        List<LessonListDto> lsList = lsDao.selectLessonList(sDto);
        mv.addObject("lsList", lsList);
        List<LessonChoreoDto> choList = lsDao.selectChoreoList(sDto);
        mv.addObject("choList", choList);
        List<LessonPartTimeDto> ptList = lsDao.selectPartTimeList(sDto);
        mv.addObject("ptList", ptList);


        sDto.setPageNum(num);
        String pageHtml = getPaging(sDto);
        mv.addObject("paging", pageHtml);

        if (sDto.getColname() != null) {
            session.setAttribute("sDto", sDto);
        } else {
            session.removeAttribute("sDto");
        }
        session.setAttribute("pageNum", num);

        mv.setViewName("lessonList");
        return mv;
    }

    //페이징 기능 처리
    private String getPaging(SearchDto sDto) {
        String pageHtml = null;
        int maxNum = lsDao.selectLessonListCnt(sDto);
        int pageCnt = 5;
        String listName = "lessonList?";
        if (sDto.getColname() != null) {
            listName += "colname=" + sDto.getColname() + "&keyword=" + sDto.getKeyword() + "&";
        }

        PagingUtil paging = new PagingUtil(
                maxNum,
                sDto.getPageNum(),
                sDto.getListCnt(),
                pageCnt,
                listName);

        pageHtml = paging.makePaging();

        return pageHtml;

    }

    //레슨 학원 등록 기능 처리
    public String lessonAProc(List<MultipartFile> lsimgfiles,
                              LessonListDto lesson,
                              RedirectAttributes rttr,
                              HttpSession session) {
        log.info("lessonAProc()");
        String view = null;
        String msg = null;
        TransactionStatus status = manager.getTransaction(definition);
        try {
            log.info("레슨 등록 번호 : " + lesson.getLs_num());

            lsDao.insertLessonList(lesson);

            lsimgfileUpload(lsimgfiles, session, lesson.getLs_num());

            manager.commit(status);
            msg = "클래스 등록완료. 안무가를 추가해주세요.";
            view = "redirect:lessonCForm?ls_num=" + lesson.getLs_num();
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status); //취소
            msg = "작성 실패";
            view = "redirect::lessonAForm";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //레슨 학원 이미지 등록 처리
    private void lsimgfileUpload(List<MultipartFile> lsimgfiles,
                                 HttpSession session,
                                 int ls_num) throws Exception {
        log.info("lsimgfileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "lsimgupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }
        for (MultipartFile mf : lsimgfiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            LessonImgFileDto lifd = new LessonImgFileDto();
            lifd.setLif_lsnum(ls_num);
            lifd.setLif_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            lifd.setLif_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            lsDao.insertLsImgFile(lifd);
        }
    }

    //안무가 등록 페이지 이동시 학원 이름 찾기
    public ModelAndView academyInfo(int ls_num) {
        log.info("academyInfo()");
        ModelAndView mv = new ModelAndView();
        LessonListDto academy = lsDao.selectAcademy(ls_num);
        mv.addObject("academy", academy);
        mv.setViewName("lessonCForm");
        return mv;
    }

    //레슨 안무가 등록 처리
    public String lessonCProc(List<MultipartFile> lcimgfiles,
                              LessonChoreoDto choreo,
                              RedirectAttributes rttr, HttpSession session) {
        log.info("lessonAProc()");
        String view = null;
        String msg = null;
        TransactionStatus status = manager.getTransaction(definition);
        try {
            log.info("레슨등록 안무가 번호 : " + choreo.getCho_num());

            lsDao.insertLessonChoreo(choreo);
            lcimgfileUpload(lcimgfiles, session, choreo.getCho_num());

            manager.commit(status);
            msg = "안무가 등록완료. 수업을 진행할 시간을 설정해주세요";
            view = "redirect:lessonTForm?cho_num=" + choreo.getCho_num();
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status); //취소
            msg = "작성 실패";
            view = "redirect::LessonCForm";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }


    //레슨 안무가 필모이미지 등록 처리
    private void lcimgfileUpload(List<MultipartFile> lcimgfiles, HttpSession session, int cho_num) throws Exception {
        log.info("lcimgfileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "lcimgupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }
        for (MultipartFile mf : lcimgfiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            LessonChoreoImgDto lcid = new LessonChoreoImgDto();
            lcid.setLcif_chonum(cho_num);
            lcid.setLcif_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            lcid.setLcif_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            lsDao.insertLcImgFile(lcid);
        }
    }

    //파트타임 등록 페이지 이동시 안무가 이름 조회
    public ModelAndView selectChoreo(int cho_num, LessonPartTimeDto parttime) {
        log.info("selectChoreo()");
        ModelAndView mv = new ModelAndView();
        LessonChoreoDto Choreo = lsDao.selectChoreo(cho_num);
        mv.addObject("Choreo", Choreo);
        //저장한 파트타임 목록 가져오기
        List<LessonPartTimeDto> ptList = lsDao.selectParttimeList(parttime.getLpt_num());
        mv.addObject("ptList", ptList);
        mv.setViewName("lessonTForm");
        return mv;
    }

    //안무가 파트타임 등록
    public LessonPartTimeDto parttimeInsert(LessonPartTimeDto parttime) {
        log.info("parttimeInsert()");
        try {
            lsDao.insertParttime(parttime);
            parttime = lsDao.selectLastParttime(parttime.getLpt_num());
        } catch (Exception e) {
            e.printStackTrace();
            parttime = null;
        }
        return parttime;
    }

    //파트타임 삭제 기능처리
    public String parttimeDelete(@RequestParam Optional<Integer> cho_num, int lpt_num, RedirectAttributes rttr) {
        log.info("parttimeDelete()");
        String view = null;
        String msg = null;
        try {
            lsDao.deleteParttime(lpt_num);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "삭제실패";
        }
        view = "redirect:lessonTForm?cho_num" + cho_num;
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //레슨 상세보기 기능 처리
    public ModelAndView lessonDetail(LessonListDto lsDto, HttpSession session) {
        log.info("lessonDetail()");
        ModelAndView mv = new ModelAndView();

        //레슨 넘버로 레슨 정보 가져오기
        LessonListDto lesson = lsDao.selectLessonView(lsDto.getLs_num());
        mv.addObject("lesson", lesson);
        //레슨 넘버로 안무가 정보 가져오기
        LessonChoreoDto choreo = lsDao.selectChoreoView(lesson.getLs_num());
        mv.addObject("choreo", choreo);
        //안무가 넘버로 안무가 필모 사진영상 가져오기
        List<LessonChoreoImgDto> choreofiles = lsDao.selectChoreoImgfile(choreo.getCho_num());
        mv.addObject("choreofiles", choreofiles);
        //안무가 넘버로 레슨시간 가져오기
        List<LessonPartTimeDto> parttime = lsDao.selectParttimeView(choreo.getCho_num());
        mv.addObject("parttime", parttime);

        mv.setViewName("lessonDetail");
        return mv;
    }

    //레슨 삭제 처리
    public String lessonDelete(int ls_num, RedirectAttributes rttr, HttpSession session) {
        log.info("lessonDelete()");
        String view = null;
        String msg = null;
        TransactionStatus status = manager.getTransaction(definition);

        LessonChoreoDto choreo = lsDao.selectChoreoView(ls_num);
        try {
            //파일 삭제 목록 구하기
            List<String> lsiList = lsDao.selectLessonImgList(ls_num);
            List<String> lciList = lsDao.selectChoreoImgList(choreo.getCho_num());


            //파트타임 삭제
            lsDao.lessonDeleteParttime(choreo.getCho_num());
            //안무가 이미지 삭제
            lsDao.lessonDeleteChoreoImg(choreo.getCho_num());
            //학원 이미지 삭제
            lsDao.lessonDeleteImg(ls_num);
            //안무가 삭제
            lsDao.lessonDeleteChoreo(choreo.getCho_num());
            //레슨 삭제
            lsDao.lessonDelete(ls_num);

            //이미지 파일 삭제 처리
            deleteChoreoImgFiles(lciList, session);
            if (lciList.size() != 0) {
                deleteChoreoImgFiles(lciList, session);
            }
            deleteLessonImgFiles(lsiList, session);
            if (lsiList.size() != 0) {
                deleteLessonImgFiles(lsiList, session);
            }
            manager.commit(status);
            view = "redirect:lessonList?pageNum=1";
            msg = "삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:lessonDetail?ls_num=" + ls_num;
            msg = "삭제 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //레슨 삭제시 학원 이미지 파일 삭제 처리
    private void deleteLessonImgFiles(List<String> lsiList, HttpSession session) {
        log.info("deleteLessonImgFiles()");
        //파일위치
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "lsimgupload/";

        for (String sn : lsiList) {
            File file = new File(realPath + sn);
            if (file.exists() == true) {//파일 유무 확인
                file.delete();//파일을 삭제
            }
        }
    }

    //레슨 삭제시 안무가 이미지 파일 삭제처리
    private void deleteChoreoImgFiles(List<String> lciList, HttpSession session) {
        log.info("deleteChoreoImgFiles()");
        //파일위치
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "lcimgupload/";

        for (String sn : lciList) {
            File file = new File(realPath + sn);
            if (file.exists() == true) {//파일 유무 확인
                file.delete();//파일을 삭제
            }
        }
    }

    //레슨 학원 수정 이동
    public ModelAndView lessonAUpdate(int ls_num) {
        log.info("lessonAUpdate()");
        ModelAndView mv = new ModelAndView();
        //학원 정보 가져오기
        LessonListDto lesson = lsDao.selectLesson(ls_num);
        //학원 이미지 가져오기
        List<LessonImgFileDto> lifList = lsDao.selectLessonImg(ls_num);
        mv.addObject("lesson", lesson);
        mv.addObject("lifList", lifList);
        mv.setViewName("lessonAUpdate");
        return mv;
    }

    //레슨 학원 정보 수정
    public String lsAUpdateProc(List<MultipartFile> lsimgfiles,
                                LessonListDto lesson,
                                HttpSession session,
                                RedirectAttributes rttr) {
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        try {
            lsDao.lessonUpdate(lesson);
            lessonimgfileupload(lsimgfiles, session, lesson.getLs_num());
            manager.commit(status);
            view = "redirect:lessonCUpdate?ls_num=" + lesson.getLs_num();
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:lessonAUpdate?ls_num=" + lesson.getLs_num();
            msg = "수정 실패";
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //레슨 학원 이미지 수정 기능 처리
    private void lessonimgfileupload(List<MultipartFile> lsimgfiles, HttpSession session, int ls_num) throws Exception {
        log.info("lessonimgfileupload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "lsimgupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }

        for (MultipartFile mf : lsimgfiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            LessonImgFileDto lsif = new LessonImgFileDto();
            lsif.setLif_lsnum(ls_num);
            lsif.setLif_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            lsif.setLif_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            lsDao.insertLessonImgFile(lsif);
        }
    }

    //레슨 수정 시 원래 있던 파일 삭제
    public List<LessonImgFileDto> lsDelImgFile(LessonImgFileDto liFile, HttpSession session) {
        log.info("lsDelImgFile()");
        List<LessonImgFileDto> lifList = null;
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "lsimgupload/" + liFile.getLif_sysname();
        try {
            //파일 삭제
            File file = new File(realPath);
            if (file.exists()) {
                if (file.delete()) {
                    //해당 파일 정보 삭제(DB)
                    lsDao.lessonImgDelete(liFile.getLif_sysname());
                    //나머지 파일 목록 다시 가져오기
                    lifList = lsDao.selectLessonImg(liFile.getLif_lsnum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lifList;
    }

    //레슨 안무가 수정 이동
    public ModelAndView lessonCUpdate(int ls_num) {
        log.info("lessonCUpdate()");
        ModelAndView mv = new ModelAndView();
        LessonListDto academy = lsDao.selectAcademy(ls_num);
        //안무가 정보 가져오기
        LessonChoreoDto choreo = lsDao.selectLessonChoreo(ls_num);
        //학원 이미지 가져오기
        List<LessonChoreoImgDto> lcifList = lsDao.selectChoreoImg(choreo.getCho_num());
        mv.addObject("academy", academy);
        mv.addObject("choreo", choreo);
        mv.addObject("lcifList", lcifList);
        mv.setViewName("lessonCUpdate");
        return mv;
    }

    //레슨 안무가 수정 기능 처리
    public String lsCUpdateProc(List<MultipartFile> lcimgfiles,
                                LessonChoreoDto choreo,
                                HttpSession session,
                                RedirectAttributes rttr) {
        log.info("lsCUpdateProc()");
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;

        try {
            lsDao.lessonChoreoUpdate(choreo);
            lessonchoreoimgfileupload(lcimgfiles, session, choreo.getCho_num());
            manager.commit(status);
            view = "redirect:lessonTUpdate?cho_num=" + choreo.getCho_num();
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:lessonCUpdate?cho_num=" + choreo.getCho_num();
            msg = "수정 실패";
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //레슨 안무가 이미지 수정 기능 처리
    private void lessonchoreoimgfileupload(List<MultipartFile> lcimgfiles, HttpSession session, int cho_num) throws Exception {
        log.info("lessonchoreoimgfileupload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "lcimgupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }

        for (MultipartFile mf : lcimgfiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            LessonChoreoImgDto lcif = new LessonChoreoImgDto();
            lcif.setLcif_chonum(cho_num);
            lcif.setLcif_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            lcif.setLcif_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            lsDao.insertChoreoImgFile(lcif);
        }
    }

    //레슨 안무가 수정시 원래 있던 파일 삭제
    public List<LessonChoreoImgDto> lcDelImgFile(LessonChoreoImgDto lciFile, HttpSession session) {
        log.info("lsDelImgFile()");
        List<LessonChoreoImgDto> lcifList = null;
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "lcimgupload/" + lciFile.getLcif_sysname();
        try {
            //파일 삭제
            File file = new File(realPath);
            if (file.exists()) {
                if (file.delete()) {
                    //해당 파일 정보 삭제(DB)
                    lsDao.choreoImgDelete(lciFile.getLcif_sysname());
                    //나머지 파일 목록 다시 가져오기
                    lcifList = lsDao.selectChoreoImg(lciFile.getLcif_chonum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lcifList;
    }

    //레슨 파트타임 수정 기능 처리
    public ModelAndView lessonTUpdate(int cho_num, LessonPartTimeDto parttime) {
        log.info("lessonTUpdate()");
        ModelAndView mv = new ModelAndView();
        List<LessonPartTimeDto> ptimeList = lsDao.selectLastPartTimeList(cho_num);
        mv.addObject("ptimeList", ptimeList);
        LessonChoreoDto choreo = lsDao.selectChoreo(cho_num);
        mv.addObject("choreo", choreo);
        List<LessonPartTimeDto> ptList = lsDao.selectParttimeList(parttime.getLpt_num());
        mv.addObject("ptList", ptList);

        mv.setViewName("lessonTUpdate");
        return mv;
    }

    //레슨 파트타임 수정 시 파트타임 삭제
    public String lastParttimeDelete(int lpt_num, int cho_num, RedirectAttributes rttr) {
        log.info("lastParttimeDelete()");
        String view = null;
        String msg = null;
        try {
            lsDao.deleteParttime(lpt_num);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "삭제실패";
        }
        view = "redirect:lessonTUpdate?cho_num=" + cho_num;
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //예약하기 기능 처리
    public String lsReservProc(@RequestBody LessonReservationListDto reservation,
                               String m_id,
                               HttpSession session,
                               RedirectAttributes rttr) {
        log.info("lsReservProc()");
        String view = null;
        String msg = null;
        TransactionStatus status = manager.getTransaction(definition);

        try {
            lsDao.insertReservation(reservation);
            manager.commit(status);
            view = "redirect:lessonReservationList?m_id="+m_id;
            msg = "예약에 성공하였습니다";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            msg = "예약 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //개인회원 예약 목록 보기
    public ModelAndView lsReservationListMove(SearchDto sDto,
                                              LessonReservationListDto lsReservation,
                                              HttpSession session) {
        log.info("lsReservationListMove()");
        ModelAndView mv = new ModelAndView();
        int num = sDto.getPageNum();

        if (num == 0) num = 1;
        if (sDto.getListCnt() == 0) {
            sDto.setListCnt(lcnt);
        }
        sDto.setPageNum((num - 1) * sDto.getListCnt());
        List<LessonReservationListDto> lrList = lsDao.selectLessonReservationList(sDto);
        mv.addObject("lrList", lrList);

        sDto.setPageNum(num);
        String pageHtml = reservatinGetPaging(sDto);
        mv.addObject("paging", pageHtml);

        if (sDto.getColname() != null) {
            session.setAttribute("sDto", sDto);
        } else {
            session.removeAttribute("sDto");
        }
        session.setAttribute("pageNum", num);
        mv.setViewName("lessonReservationList");
        return mv;
    }

    //개인회원 예약목록 페이징 처리
    private String reservatinGetPaging(SearchDto sDto) {
        log.info("reservatinGetPaging()");
        String pageHtml = null;
        int maxNum = lsDao.selectReservationListCnt(sDto);
        int pageCnt = 5;
        String listName = "lessonReservationList?m_id=" + sDto.getM_id() + "&";

        PagingUtil paging = new PagingUtil(
                maxNum,
                sDto.getPageNum(),
                sDto.getListCnt(),
                pageCnt,
                listName);

        pageHtml = paging.makePaging();

        return pageHtml;
    }

    //개인회원 예약 목록 상세보기
    public ModelAndView lrDetail(LessonReservationListDto lrDto, HttpSession session) {
        log.info("lrDetail()");
        ModelAndView mv = new ModelAndView();

        LessonReservationListDto reservation = lsDao.selectLessonReserv(lrDto.getLrl_num());
        mv.addObject("reservation", reservation);
        List<LessonChoreoImgDto> choreo = lsDao.selectReservChoreoImg(reservation.getLrl_chonum());
        mv.addObject("choreo", choreo);

        mv.setViewName("lessonReservationDetail");
        return mv;
    }

    //개인회원 예약취소 기능 처리
    public String lessonReservationDelete(int lrl_num, String m_id, RedirectAttributes rttr) {
        log.info("lessonReservationDelete()");
        String view = null;
        String msg = null;
        LessonReservationListDto lesson = lsDao.selectLessonReserv(lrl_num);
        try {
            lsDao.deleteReservation(lrl_num);
            msg = "레슨 예약이 취소되었습니다.";
            view = "redirect:lessonReservationList?m_id=" + lesson.getLrl_mid();
        } catch (Exception e) {
            e.printStackTrace();
            msg = "삭제실패";
            view = "redirect:lessonReservationDetail?lrl_num=" + lrl_num;
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //사업자 회원 예약 현황 목록 보기
    public ModelAndView lessonStatusListMove(SearchDto sDto, LessonListDto lesson, HttpSession session) {
        log.info("lessonStatusListMove()");
        ModelAndView mv = new ModelAndView();
        int num = sDto.getPageNum();

        if (num == 0) num = 1;
        if (sDto.getListCnt() == 0) {
            sDto.setListCnt(lcnt);
        }

        sDto.setPageNum((num - 1) * sDto.getListCnt());
        List<LessonListDto> lstList = lsDao.selectLessonStatusList(sDto);
        mv.addObject("lstList", lstList);

        sDto.setPageNum(num);
        String pageHtml = lessonGetPaging(sDto);
        mv.addObject("paging", pageHtml);

        if (sDto.getColname() != null) {
            session.setAttribute("sDto", sDto);
        } else {
            session.removeAttribute("sDto");
        }
        session.setAttribute("pageNum", num);
        mv.setViewName("lessonStatusList");
        return mv;
    }

    //사업자 회원 예약 현황 페이징 처리
    private String lessonGetPaging(SearchDto sDto) {
        log.info("lessonGetPaging()");
        String pageHtml = null;
        int maxNum = lsDao.selectStatusListCnt(sDto);
        int pageCnt = 5;
        String listName = "lessonStatusList?ls_mid=" + sDto.getM_id();

        PagingUtil paging = new PagingUtil(
                maxNum,
                sDto.getPageNum(),
                sDto.getListCnt(),
                pageCnt,
                listName);

        pageHtml = paging.makePaging();

        return pageHtml;
    }

    //사업자회원 레슨번호에 해당하는 예약 현황 리스트로 상세보기
    public ModelAndView lessonStatusDetail(SearchDto sDto, int ls_num, HttpSession session) {
        log.info("lessonStatusDetail()");
        ModelAndView mv = new ModelAndView();

        List<LessonReservationListDto> lrList = lsDao.selectLessonStatusDetail(ls_num);
        mv.addObject("lrList", lrList);
        LessonListDto lesson = lsDao.selectLesson(ls_num);
        mv.addObject("lesson",lesson);
        mv.setViewName("lessonStatusDetail");
        return mv;
    }
    public String lessonStatusDelete(int lrl_num, int ls_num, RedirectAttributes rttr) {
        log.info("lessonStatusDelete()");
        String view = null;
        String msg = null;

        try {
            lsDao.deleteStatus(lrl_num);
            msg = "레슨 예약이 취소되었습니다.";
            view = "redirect:lessonStatusDetail?ls_num="+ls_num;
        } catch (Exception e) {
            e.printStackTrace();
            msg = "예약 취소 실패";
            view = "redirect:lessonStatusDetail?ls_num="+ls_num;
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }
}



//        List<LessonReservationListDto> memberId = lsDao.selectMemberId(ls_num);
//        List<MemberDto> member = new ArrayList<>();
//        for (LessonReservationListDto lr : memberId) {
//            MemberDto mem = lsDao.selectMember(lr.getLrl_mid());
//            member.add(mem);
//        }
//        mv.addObject("member", member);



























