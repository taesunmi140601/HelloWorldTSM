package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.attachFile.vo.PostAttachFileVO;
import kr.or.ddit.works.notice.vo.NoticeVO;
import kr.or.ddit.works.organization.vo.DepartmentVO;

@Mapper
public interface NoticeMapper {


	/**
	 * 공지사항 전체 조회(페이징처리)
	 * @param paging
	 * @return
	 */
	public List<NoticeVO> selectAllNotice(Map<String, Object> paramMap);

	public NoticeVO basicSelectAllWithCompany(@Param("notice") NoticeVO notice);

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
	public int insertNotice(NoticeVO notice);

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
	 * @param params
	 * @return
	 */
	public int deleteNotice(Map<String, Object> params);

	/**
	 * 공지사항 수정
	 * @param params
	 * @return
	 */
	public int updateNotice(NoticeVO notice);

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
	public int insertNoticeFile(Map<String, Object> fileMap);

	/**
	 * 첨부파일 시퀀스 번호 가져오기
	 * @return
	 */
	public long seqNoticeFile();

	/**
	 * 공지사항 시퀀스 번호 가져오기
	 * @return
	 */
	public int seqNotice();

	/**
	 * 파일과 공지사항을 한번에 관리하기 위한 메서드
	 * @return
	 */
	public int insertFileNotice(Map<String, Object> param);

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

	/**
	 * 메인페이지 공지사항 위젯용
	 * @param companyNo
	 * @return
	 */
	public List<NoticeVO> selectRecentNoticeList(String companyNo);

	/**
	 * 공지사항 등록 권한을 위한 로그인 계정 조회
	 * @param empId
	 * @return
	 */
	public DepartmentVO selectLogin(String managerEmpId);
}
