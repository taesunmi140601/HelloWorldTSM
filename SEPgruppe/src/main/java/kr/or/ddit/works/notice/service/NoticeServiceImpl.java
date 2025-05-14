package kr.or.ddit.works.notice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.works.attachFile.service.AttachFileService;
import kr.or.ddit.works.attachFile.service.AttachFileServiceImpl;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.mybatis.mappers.NoticeMapper;
import kr.or.ddit.works.notice.vo.NoticeVO;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeMapper mapper;

	@Autowired
	private AttachFileService service;




	/**
	 * 공지사항 전체 조회
	 * @param paging
	 * @return
	 */
	@Override
	public List<NoticeVO> selectAllNotice(PaginationInfo<NoticeVO> paging, String companyNo) {
		Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("startRow", paging.getStartRow());
	    paramMap.put("endRow", paging.getEndRow());
	    paramMap.put("simpleCondition", paging.getSimpleCondition());
	    paramMap.put("detailCondition", paging.getDetailCondition());
	    paramMap.put("companyNo", companyNo);
	    return mapper.selectAllNotice(paramMap);
	}

	/**
	 * 페이징 처리를 위한 전체 레코드수 조회
	 * @return
	 */
	@Override
	public int selectAllNoticeTotalRecord(PaginationInfo<NoticeVO> paging) {
		return mapper.selectAllNoticeTotalRecord(paging);
	}

	/**
	 * 공지사항 등록
	 * @param notice
	 * @return
	 */
	@Override
	public boolean insertNotice(NoticeVO notice) {
		log.info("노티스번호(컨트롤러에서 세팅한 값) {}", notice.getNoticeNo());

		return mapper.insertNotice(notice) > 0;
	}

	/**
	 * 임시저장 글 불러오기
	 * @param empId
	 * @return
	 */
	@Override
	public List<NoticeVO> isDraftList(String empId) {
		return mapper.isDraftList(empId);
	}

	/**
	 * 임시저장 글 개수 가져오기
	 * @param empId
	 * @return
	 */
	@Override
	public int isDraftCnt(String empId) {
		return mapper.isDraftCnt(empId);
	}

	/**
	 * 공지사항 삭제
	 * @param empid
	 * @return
	 */
	@Override
	public boolean deleteNotice(Map<String, Object> params) {
		return mapper.deleteNotice(params) > 0;
	}

	/**
	 * 공지사항 수정
	 * @param params
	 * @return
	 */
	@Override
	public boolean updateNotice(NoticeVO notice) {
		return mapper.updateNotice(notice) > 0;
	}

	/**
	 * 공지사항 조회수 증가
	 * @param noticeNo
	 * @return
	 */
	@Override
	public int noticeViewCnt(int noticeNo) {
		return mapper.noticeViewCnt(noticeNo);
	}

	/**
	 * 공지사항 파일 업로드
	 * @param empId
	 * @return
	 */
	@Override
	public boolean insertNoticeFile(Map<String, Object> fileMap) {
	    List<AttachFileVO> fileList = (List<AttachFileVO>) fileMap.get("fileList");

	    if (fileList != null && !fileList.isEmpty()) {
	        // 쿼리 실행 전 시퀀스 값 먼저 할당
	        for (int i = 0; i < fileList.size(); i++) {
	        	long seq = mapper.seqNoticeFile();
	            fileList.get(i).setAttachFileNo("NOTICE_" + (seq));
	        }
	        log.info("파일번호 할당 {}", fileList);
			Map<String, Object> postAttachFileVo = new HashMap<>();
			postAttachFileVo.put("attachFileNo", fileList);

	        // 파일 정보를 DB에 저장
	        if (mapper.insertNoticeFile(fileMap) > 0) {
	            for (AttachFileVO file : fileList) {
	            	service.fileUpload(file); // 실제 파일 저장
	            }
	            return true;
	        }
	    }
	    return false;
	}

	/**
	 * 파일과 공지사항을 한번에 관리하기 위한 메서드
	 * @return
	 */
	@Override
	public int insertFileNotice(Map<String, Object> postAttachFileVo) {
		List<AttachFileVO> fileList = (List<AttachFileVO>) postAttachFileVo.get("fileList"); // 파일 정보 가져오기
		int noticeNo = (int) postAttachFileVo.get("noticeNo"); // NOTICE_NO 정보 가져오기

		int insertCount = 0;

		if(fileList != null && noticeNo > 0) {
			for(AttachFileVO file : fileList) {
				Map<String, Object> param = new HashMap<>();
				param.put("noticeNo", noticeNo);
				param.put("attachFileNo", file.getAttachFileNo());

				insertCount += mapper.insertFileNotice(param);
			}
		}

		return insertCount;
	}

	/**
	 * 공지사항 시퀀스 번호 가져오기
	 * @return
	 */
	@Override
	public int seqNotice() {
		return mapper.seqNotice();
	}

	/**
	 * 첨부파일 삭제
	 * @param attachFileNo
	 * @return
	 */
	@Override
	public int deleteFile(String attachFileNo) {
		return mapper.deleteFile(attachFileNo);
	}

	/**
	 * 첨부파일 다운로드
	 * @param attachFileNo
	 * @return
	 */
	@Override
	public AttachFileVO selectByFileNo(String attachFileNo) {

		return mapper.selectByFileNo(attachFileNo);
	}

	@Override
	public List<NoticeVO> selectRecentNoticeList(String companyNo) {
	    return mapper.selectRecentNoticeList(companyNo);
	}

	@Override
	public NoticeVO basicSelectAllWithCompany(NoticeVO notice) {
	    return mapper.basicSelectAllWithCompany(notice);
	}

	@Override
	public DepartmentVO selectLogin(String managerEmpId) {
		return mapper.selectLogin(managerEmpId);
	}

}