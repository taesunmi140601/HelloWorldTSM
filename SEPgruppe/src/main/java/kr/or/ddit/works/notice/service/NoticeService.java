package kr.or.ddit.works.notice.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.SimpleCondition;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.notice.vo.NoticeVO;
import kr.or.ddit.works.organization.vo.DepartmentVO;

public interface NoticeService {



	/**
	 * 공지사항 전체 조회 (페이징포함)
	 * @param paging
	 * @return
	 */
	public List<NoticeVO> selectAllNotice(PaginationInfo<NoticeVO> paging, String companyNo);

	public NoticeVO basicSelectAllWithCompany(NoticeVO notice);

	/**
	 * 페이징 처리를 위한 전체 레코드수 조회
	 * @return
	 */
	public int selectAllNoticeTotalRecord(PaginationInfo<NoticeVO> paging);

	/**
	 * 공지사항 등록
	 * @param notice
	 * @return
	 */
	public boolean insertNotice(NoticeVO notice);

	/**
	 * 임시저장 글 불러오기
	 * @param empId
	 * @return
	 */
	public List<NoticeVO> isDraftList(String empId);

	/**
	 * 임시저장 글 개수 가져오기
	 * @param empId
	 * @return
	 */
	public int isDraftCnt(String empId);

	/**
	 * 공지사항 삭제
	 * @param empid
	 * @return
	 */
	public boolean deleteNotice(Map<String, Object> params);

	/**
	 * 공지사항 수정
	 * @param params
	 * @return
	 */
	public boolean updateNotice(NoticeVO notice);

	/**
	 * 공지사항 조회수 증가
	 * @param noticeNo
	 * @return
	 */
	public int noticeViewCnt(int noticeNo);

	/**
	 * 공지사항 파일 업로드
	 * @param empId
	 * @return
	 */
	public boolean insertNoticeFile(Map<String, Object> fileMap);


	/**
	 * 파일과 공지사항을 한번에 관리하기 위한 메서드
	 * @return
	 */
	public int insertFileNotice(Map<String, Object> param);

	/**
	 * 공지사항 시퀀스 번호 가져오기
	 * @return
	 */
	public int seqNotice();

	/**
	 * 첨부파일 삭제
	 * @param attachFileNo
	 * @return
	 */
	public int deleteFile(String attachFileNo);

	/**
	 * 첨부파일 다운로드
	 * @param attachFileNo
	 * @return
	 */
	public AttachFileVO selectByFileNo(String attachFileNo);


	public List<NoticeVO> selectRecentNoticeList(String companyNo);


	/**
	 * 공지사항 등록 권한을 위한 로그인 계정 조회
	 * @param empId
	 * @return
	 */
	public DepartmentVO selectLogin(String managerEmpId);


}
