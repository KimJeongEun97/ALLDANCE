package com.alldance01.alldance.service;

import com.alldance01.alldance.dao.DanceBoardDao;
import com.alldance01.alldance.dto.*;
import com.alldance01.alldance.utill.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class DanceBoardService {
    @Autowired
    private DanceBoardDao dbDao;

    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    private int lcnt = 5;

    //게시판 목록 처리
    public ModelAndView dbListMove(SearchDto sDto, DanceBoardDto dbDto, HttpSession session) {
        log.info("dbListMove()");
        ModelAndView mv = new ModelAndView();
        int num = sDto.getPageNum();
        if (num == 0) num = 1;
        if (sDto.getListCnt() == 0) {
            sDto.setListCnt(lcnt);
        }
        sDto.setPageNum((num - 1) * sDto.getListCnt());
        List<DanceBoardDto> dbList = dbDao.selectDanceBoardList(sDto);
        mv.addObject("dbList", dbList);

        sDto.setPageNum(num);
        String pageHtml = getPaging(sDto);
        mv.addObject("paging", pageHtml);

        if (sDto.getColname() != null) {
            session.setAttribute("sDto", sDto);
        } else {
            session.removeAttribute("sDto");
        }
        session.setAttribute("pageNum", num);
        mv.setViewName("danceBoardList");
        return mv;
    }

    //게시판 목록 페이징 기능 처리
    private String getPaging(SearchDto sDto) {
        String pageHtml = null;
        int maxNum = dbDao.selectDanceBoardCnt(sDto);
        int pageCnt = 5;
        String listName = "danceBoardList?";
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

    //게시글 작성 기능 처리
    public String dbWirte(List<MultipartFile> dbvideofiles,
                          List<MultipartFile> dbimgfiles,
                          DanceBoardDto dance,
                          DanceCategoryDto category,
                          RedirectAttributes rttr,
                          HttpSession session) {
        log.info("dbWirte()");
        String view = null;
        String msg = null;
        TransactionStatus status = manager.getTransaction(definition);
        try {
            log.info("게시글 번호 : " + dance.getD_num());
            if (dance.getD_category().equals(category.getDc_category())) {
                category.setDc_category(null);
            } else {
                dbDao.insertCategory(dance.getD_category());
            }
            dbDao.insertDanceBoard(dance);

            dbvideofileUpload(dbvideofiles, session, dance.getD_num());
            dbimgfileUpload(dbimgfiles, session, dance.getD_num());

            manager.commit(status);
            msg = "공유 감사합니다!";
            view = "redirect:danceBoardList";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status); //취소
            msg = "작성 실패";
            view = "redirect:danceBoardWrite";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //게시글 작성시 섬네일 이미지 업로드
    private void dbimgfileUpload(List<MultipartFile> dbimgfiles, HttpSession session, int d_num) throws Exception {
        log.info("dbimgfileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "dbimgupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }
        for (MultipartFile mf : dbimgfiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            DanceImgFileDto difd = new DanceImgFileDto();
            difd.setDif_dnum(d_num);
            difd.setDif_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            difd.setDif_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            dbDao.insertdbImgFile(difd);
        }
    }

    //게시글 작성시 영상파일 업로드
    private void dbvideofileUpload(List<MultipartFile> dbvideofiles, HttpSession session, int d_num) throws Exception {
        log.info("dbvideofileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "dbvideoupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }
        for (MultipartFile mf : dbvideofiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            DanceVideoFileDto dvfd = new DanceVideoFileDto();
            dvfd.setDvf_dnum(d_num);
            dvfd.setDvf_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            dvfd.setDvf_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            dbDao.insertdbVideoFile(dvfd);
        }
    }

    //게시글 상세보기 기능 처리
    public ModelAndView dbDetail(DanceBoardDto dbDto, HttpSession session) {
        log.info("getDance()");
        ModelAndView mv = new ModelAndView();
        MemberDto member = (MemberDto) session.getAttribute("member");

        int views = dbDto.getD_views();
        if (member.getM_id().equals(dbDto.getD_mid())) {
            views += 0;
            dbDto.setD_views(views);
            dbDao.updateViewPoint(dbDto);
        } else {
            views++;
            dbDto.setD_views(views);
            dbDao.updateViewPoint(dbDto);
        }
        //조회수가 증가하게 세션에 저장
        dbDto = dbDao.selectDance(dbDto.getD_num());
        session.setAttribute("dance", dbDto);

        //게시글 번호로 선택한 게시글 가져오기
        DanceBoardDto dance = dbDao.selectDance(dbDto.getD_num());
        mv.addObject("dance", dance);
        //게시글 동영상 파일목록 가져오기
        List<DanceVideoFileDto> dvfList = dbDao.selectVideoFileList(dbDto.getD_num());
        mv.addObject("dvfList", dvfList);
        //게시글의 댓글목록 가져오기
        List<DanceReplyDto> dbrList = dbDao.selectReplyList(dance.getD_num());
        mv.addObject("dbrList", dbrList);

        mv.setViewName("danceBoardDetail");
        return mv;
    }

    //게시글 삭제 기능처리
    public String deleteDanceBoard(int d_num, RedirectAttributes rttr, HttpSession session) {
        log.info("deleteDance()");
        String view = null;
        String msg = null;
        TransactionStatus status = manager.getTransaction(definition);
        try {
            //파일 삭제 목록 구하기
            List<String> vfList = dbDao.selectVideoNameList(d_num);
            List<String> ifList = dbDao.selectImgNameList(d_num);

            //섬네일 삭제
            dbDao.deleteImgFiles(d_num);
            //영상파일 삭제
            dbDao.deleteVideoFiles(d_num);
            //댓글 삭제
            dbDao.deleteReplyList(d_num);
            //게시글 삭제
            dbDao.deleteDanceBoard(d_num);
            //게시글 영상 삭제 처리
            deleteVideoFiles(vfList, session);
            if (vfList.size() != 0) {
                deleteVideoFiles(vfList, session);
            }
            //게시글 섬네일 파일 삭제 처리
            deleteImgFiles(ifList, session);
            if (ifList.size() != 0) {
                deleteImgFiles(ifList, session);
            }

            manager.commit(status);
            view = "redirect:danceBoardList?pageNum=1";
            msg = "삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:danceBoardDetail?d_num=" + d_num;
            msg = "삭제 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //이미지폴더 안에 저장된 파일 삭제처리
    private void deleteImgFiles(List<String> ifList, HttpSession session) {
        log.info("deleteImgFiles()");
        //파일위치
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "dbimgupload/";

        for (String sn : ifList) {
            File file = new File(realPath + sn);
            if (file.exists() == true) {//파일 유무 확인
                file.delete();//파일을 삭제
            }
        }
    }

    //영상폴더 안에 저장돤 파일 삭제처리
    private void deleteVideoFiles(List<String> vfList, HttpSession session) {
        log.info("deleteVideoFiles()");
        //파일위치
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "dbvideoupload/";

        for (String sn : vfList) {
            File file = new File(realPath + sn);
            if (file.exists() == true) {//파일 유무 확인
                file.delete();//파일을 삭제
            }
        }
    }

    //게시글 수정 페이지 이동 시 게시글 정보 가져오기
    public ModelAndView danceBoardUpdateInfo(int d_num) {
        log.info("danceBoardUpdateInfo()");
        ModelAndView mv = new ModelAndView();
        //게시글 내용 가져오기
        DanceBoardDto dance = dbDao.selectDance(d_num);
        //영상파일목록 가져오기
        List<DanceVideoFileDto> dvfList = dbDao.selectVideoFileList(d_num);
        //이미지파일목록 가져오기
        List<DanceImgFileDto> difList = dbDao.selectImgFileList(d_num);
        //mv에 담기
        mv.addObject("dance", dance);
        mv.addObject("dvfList", dvfList);
        mv.addObject("difList", difList);
        mv.setViewName("danceBoardUpdate");
        return mv;
    }

    //게시글 댓글 작성
    public DanceReplyDto replyInsert(DanceReplyDto reply, DanceBoardDto dbDto) {
        log.info("replyInsert()");
        String view = null;
        try {
            dbDao.insertReply(reply);
            reply = dbDao.selectLastReply(reply.getDbr_num());
        } catch (Exception e) {
            e.printStackTrace();
            reply = null;
        }
        return reply;
    }

    //댓글 한개 삭제 기능처리
    public String replyDelete(int dbr_num, int d_num, RedirectAttributes rttr) {
        log.info("replyDelete()");
        String view = null;
        String msg = null;
        try {
            dbDao.deleteReply(dbr_num);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "삭제실패";
        }
        view = "redirect:danceBoardDetail?d_num=" + d_num;
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //게시판 수정 기능 처리
    public String updateDance(List<MultipartFile> dbvideofiles,
                              List<MultipartFile> dbimgfiles,
                              DanceBoardDto dance,
                              HttpSession session,
                              RedirectAttributes rttr) {
        TransactionStatus status = manager.getTransaction(definition);

        String view = null;
        String msg = null;
        try {
            dbDao.danceBoardUpdate(dance);
            videofileUpload(dbvideofiles, session, dance.getD_num());
            imgfileUpload(dbimgfiles, session, dance.getD_num());

            manager.commit(status);
            view = "redirect:danceBoardDetail?d_num=" + dance.getD_num();
            msg = "수정 성공";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:danceBoardUpdate?d_num=" + dance.getD_num();
            msg = "수정 실패";
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    //게시판 수정 시 비디오파일 업로드
    private void videofileUpload(List<MultipartFile> dbvideofiles,
                                 HttpSession session, int d_num) throws Exception {
        log.info("fileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "dbvideoupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }
        for (MultipartFile mf : dbvideofiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            DanceVideoFileDto dvfd = new DanceVideoFileDto();
            dvfd.setDvf_dnum(d_num);
            dvfd.setDvf_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            dvfd.setDvf_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            dbDao.insertdbVideoFile(dvfd);
        }
    }

    //게시판 수정 시 섬네일 이미지파일 업로드
    private void imgfileUpload(List<MultipartFile> dbimgfiles,
                               HttpSession session, int d_num) throws Exception {
        log.info("fileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "dbimgupload/";

        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }

        for (MultipartFile mf : dbimgfiles) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;
            }

            DanceImgFileDto difd = new DanceImgFileDto();
            difd.setDif_dnum(d_num);
            difd.setDif_oriname(oriname);
            String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            difd.setDif_sysname(sysname);

            File file = new File(realPath + sysname);
            mf.transferTo(file);

            dbDao.insertdbImgFile(difd);
        }
    }

    //게시글 수정시 비디오파일 삭제 처리
    public List<DanceVideoFileDto> delVideoFile(DanceVideoFileDto dvFile, HttpSession session) {
        log.info("delVideoFile()");
        List<DanceVideoFileDto> vfList = null;

        String realPath = session.getServletContext().getRealPath("/");
        realPath += "dbvideoupload/" + dvFile.getDvf_sysname();

        try {
            //파일 삭제
            File file = new File(realPath);
            if (file.exists()) {
                if (file.delete()) {
                    //해당 파일 정보 삭제(DB)
                    dbDao.deleteVideoFile(dvFile.getDvf_sysname());
                    //나머지 파일 목록 다시 가져오기
                    vfList = dbDao.selectVideoFileList(dvFile.getDvf_dnum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vfList;
    }

    //게시글 수정시 섬네일 이미지파일 삭제 처리
    public List<DanceImgFileDto> delImgFile(DanceImgFileDto diFile, HttpSession session) {
        log.info("delImgFile()");
        List<DanceImgFileDto> ifList = null;

        String realPath = session.getServletContext().getRealPath("/");
        realPath += "dbimgupload/" + diFile.getDif_sysname();

        try {
            //파일 삭제
            File file = new File(realPath);
            if (file.exists()) {
                if (file.delete()) {
                    //해당 파일 정보 삭제(DB)
                    dbDao.deleteImgFile(diFile.getDif_sysname());
                    //나머지 파일 목록 다시 가져오기
                    ifList = dbDao.selectImgFileList(diFile.getDif_dnum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ifList;
    }
}
