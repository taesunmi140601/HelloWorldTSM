package kr.or.ddit.works.approval.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.approval.enums.AprvlDocStatus;
import kr.or.ddit.works.approval.enums.AprvlLineStatus;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.approval.vo.AprvlLineAutoVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.mybatis.mappers.ApprDocMapper;
import kr.or.ddit.works.organization.service.AnnualLeaveService;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 전자결재 서비스 임플
 * 
 * @author JYS
 * @since 2025. 3. 17.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 17.     	JYS	          최초 생성
 *
 * </pre>
 */

@Slf4j
@Service
public class ApprDocServiceImpl implements ApprDocService {

	@Autowired
	private ApprDocMapper approvalMapper;
	
	@Autowired
	private AnnualLeaveService annualService;
	
	/**
	 * 결재 양식과 폴더 전체 조회
	 * @return 모든 양식 폴더 리스트
	 */
	@Override
	public List<DocFormVO> selectListAllDocForm() {
		return approvalMapper.selectListAllDocForm();
	}

	/**
     * 결재 양식 제목을 검색 시 해당 양식만 조회
     * @return 검색된 양식 조회
     */
	@Override
	public List<DocFormVO> selectListSearchDocForm(@Param("docFrmName") String docFrmName) {
		return approvalMapper.selectListSearchDocForm(docFrmName);
	}

	 /**
     * 해당 폴더에 저장된 결재문서만 목록으로 조회
     * @param docFolderNo
     * @return
     */
	public List<DocFormVO> selectDocument(@Param("docFolderNo")String docFolderNo){
		return approvalMapper.selectDocument(docFolderNo);
	}
	
	/**
     * 문서 상세보기
     * @param docFrmNo 문서 번호
     * @return
     */
	@Override
	public DocFormVO selectdocFrmDeatil(@Param("docFrmNo")String docFrmNo) {
		return approvalMapper.selectdocFrmDeatil(docFrmNo);
	}
	
	/**
     * 기안번호 시퀀스 조회
     * @return
     */
	@Override
	public String selectAprvlDocSeq() {
		int seqVal = approvalMapper.selectAprvlDocSeq(); // 시퀀스 값 가져오기

	    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	    String formattedSeq = String.format("%04d", seqVal); // 4자리로 포맷 (0001, 0002 ...)
	    
	    return "APR-" + date + "-" + formattedSeq;
	}

	
	/**
     * 문서 기안 - 결재요청
     * @param arpvlDoc
     * @return
     */
	@Override
	public boolean insertDraft(AprvlDocVO arpvlDoc) {
		return approvalMapper.insertDraft(arpvlDoc)>0;
	}
	
	/**
     * 결재선 저장 - 결재요청
     * @param aprvlLine
     * @return
     */
	@Override
	public boolean insertAprvlLine(AprvlLineVO aprvlLine) {
		return approvalMapper.insertAprvlLine(aprvlLine)>0;
	}
	

	/**
	 * 관리자 - 새 문서 추가
	 * @param docFormVo 추가할 문서 정보
	 * @return 성공 여부 (1: 성공, 0: 실패)
	 */
	@Override
	public boolean insertDocForm(String docFolderNo, DocFormVO docFormVo) {
		// 1. 다음 일련번호 조회
	    int serialNo = approvalMapper.selectNextSerial(docFolderNo);

	    // 2. 양식번호 생성
	    String docFormNo = generateDocFormNo(docFolderNo, serialNo);

	    // 3. VO에 설정
	    docFormVo.setDocFrmNo(docFormNo);

		
		return approvalMapper.insertDocForm(docFormVo) > 0;
	}
	
	private String generateDocFormNo(String docFolderNo, int serialNo) {
	    String suffix = docFolderNo.substring(3); // DF_ 잘라냄
	    String formattedSerial = String.format("%03d", serialNo);
	    return "DFR_" + suffix + "_" + formattedSerial;
	}

	/**
	 * 관리자 - 문서 수정 (제목, 내용 등)
	 * @param docFormVo 수정할 문서 정보
	 * @return 성공 여부 (1: 성공, 0: 실패)
	 */
	@Override
	public boolean updateDocForm(DocFormVO docFormVo) {
		return approvalMapper.updateDocForm(docFormVo) > 0;
	}

	/**
	 * 관리자 - 문서 비활성화 (삭제 대신 사용)
	 * @param docFrmNo 문서 번호
	 * @return 성공 여부 (1: 성공, 0: 실패)
	 */
	@Override
	public boolean deactivateDocForm(String docFrmNo) {
		return approvalMapper.deactivateDocForm(docFrmNo) > 0;
	}

	/**
	 * 관리자 - 문서 활성화
	 * @param docFrmNo 문서 번호
	 * @return 성공 여부 (1: 성공, 0: 실패)
	 */
	@Override
	public boolean activateDocForm(String docFrmNo) {
		return approvalMapper.activateDocForm(docFrmNo) > 0;
	}

	/**
     * 관리자, 일반 사원 - 자동결재선 목록 조회
     * @return
     */
	@Override
	public List<DocFormVO> selectListAllAutoLine() {
		return approvalMapper.selectListAllAutoLine();
	}
	
	@Override
	public List<DocFormVO> selectDetailAutoLine(String aprvlLineNo) {
		return approvalMapper.selectDetailAutoLine(aprvlLineNo);
	}

	/**
     * 관리자 - 자동결재선 추가
     * @param aprvlLineVo
     * @return
     */
	@Transactional
	@Override
	public void insertAutoLine(List<AprvlLineAutoVO> aprvlLineAutoList) {
		String aprvlLineNo = approvalMapper.getNextAprvlLineNo(); // 시퀀스 값 가져오기
        
        for (AprvlLineAutoVO line : aprvlLineAutoList) {
            line.setAprvlLineNo(aprvlLineNo);
            approvalMapper.insertAutoLine(line); // 개별 저장
        }
	}

	/**
     * 관리자 - 자동결재선 수정
     * @param aprvlLineVo
     * @return
     */
	@Transactional
	@Override
	public void updateAutoLine(String aprvlLineNo, List<AprvlLineAutoVO> list){
		approvalMapper.deleteAutoLine(aprvlLineNo);
		
		for(AprvlLineAutoVO line : list) {
			approvalMapper.insertAutoLine(line);
		}
		
	}

	/**
	 *  관리자 - 자동결재선 삭제
	 */
	@Override
	public boolean deleteAutoLine(String aprvlLineNo) {
		return approvalMapper.deleteAutoLine(aprvlLineNo) > 0;
	}

	
	/**
     * 자동결재선 조건에 맞는 결재자 조회
     * @param searchCondition
     * @return
     */
	@Override
    public List<EmployeeVO> selectApprLineSearchDeptPosition(Map<String, Object> searchCondition){
		return approvalMapper.selectApprLineSearchDeptPosition(searchCondition);
	}

	/**
     *  사용자의 결재자 상태에 따라 목록화 조회
     * @param searchCondition
     * @return
     */
	@Override
	public List<AprvlDocVO> selectApprDocStatusList(Map<String, Object> searchCondition) {
		return approvalMapper.selectApprDocStatusList(searchCondition);
	}
	
	/**
     * 사용자 결재 순서에 따라 대기와 예정으로 목록화 나뉨
     * @param searchCondition
     * @return
     */

	@Override
	public List<AprvlDocVO> selectApprDocListByTurn(Map<String, Object> searchCondition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * 결재문서의 결재선 조회
     * @param docNo
     * @return
     */
	@Override
	public List<AprvlLineVO> selectApprLine(String docNo) {
		return approvalMapper.selectApprLine(docNo);
	}

	/**
     * 결재선의 결재자,기안자 결재상태 업데이트
     * @param aprvlStatus
     * @param aprvlDocNo
     * @param empId
     * @return
     */
	@Override
	@Transactional
	public boolean updateApprLineStatus(String aprvlStatus, String aprvlDocNo, String empId) {
		//결재선 상태 업데이트
        int updateCount = approvalMapper.updateApprLineStatus(aprvlStatus, aprvlDocNo, empId);

        //문서 상태 결정
        String docStatus = "";

        if (aprvlStatus.equals("R")) {
            // 결재자가 반려한 경우 → 문서는 반려
            docStatus = "REJECTED";
        } else if (aprvlStatus.equals("H")) {
            // 결재자가 보류한 경우 → 문서는 보류
            docStatus = "ON_HOLD";
        } else {
            // 승인한 경우 남은 결재자 확인
            int remain = approvalMapper.countApprByWait(aprvlDocNo);
            if (remain == 0) {
                docStatus = "APPROVED"; // 모두 결재 완료
            } else {
                docStatus = "IN_PROGRESS"; // 다음 결재자 대기 중
            }
        }

        // 3. 문서 상태 업데이트
        int statusUpdate = approvalMapper.updateDocStauts(aprvlDocNo, docStatus);
        
        // === 연차 처리 시작 ======================
        if ("APPROVED".equals(docStatus) || "REJECTED".equals(docStatus)) {
            AprvlDocVO doc = approvalMapper.selectApprDocContentsByDocNo(aprvlDocNo); // 새로운 메서드 필요
            if (doc != null && "연차 신청서".equals(doc.getDocFrmName())) {
                String html = doc.getAprvlDocContents();
                String usedLeave = "";

                try {
                    Document jsoupDoc = Jsoup.parse(html);
                    usedLeave = jsoupDoc.select("td:contains(연차 일수) + td span").text();
                    empId = approvalMapper.selectDrafterEmpId(aprvlDocNo); // 추가 쿼리
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (empId != null && !usedLeave.isEmpty()) {
                    double leave = Double.parseDouble(usedLeave);
                    if ("APPROVED".equals(docStatus)) {
                        annualService.updateUsedAnnualLeave(empId, leave);
                    } else if ("REJECTED".equals(docStatus)) {
                        annualService.rollbackUsedAnnualLeave(empId, leave);
                    }
                }
            }
        }
        // === 연차 처리 끝 =======================

        return updateCount > 0 && statusUpdate > 0;
	}

	/**
     * 결재문서 내용 변경
     * @param aprvlDocNo
     * @param aprvlDocContents
     * @return
     */
	@Override
	public boolean updateApprovalHtml(String aprvlDocNo, String aprvlDocContents) {
		return approvalMapper.updateApprovalHtml(aprvlDocNo, aprvlDocContents)>0;
	}
	/**
     * 기안자 문서 회수 (첫번째 결재자 결재 이전에만 가능)
     * @param aprvlDocNo
     * @param userId
     * @return
     */
	@Override
	@Transactional
	public boolean updateCancleApprDoc(String aprvlDocNo, String userId) {
		log.info("aprvlDocNo: {}, userId: {}", aprvlDocNo, userId);
		List<AprvlLineVO> apprlLines = approvalMapper.selectApprLine(aprvlDocNo);
		
		
		// 기안자 확인
		 AprvlLineVO drafter = apprlLines.stream()
											.filter(line->line.getAprvlTurn() == 0)
											.findFirst()
											.orElse(null);
		
		if(drafter == null || !userId.equals(drafter.getEmpId())){
			return false;
		}
		
		// 첫번째 결재 확인
		AprvlLineVO firstApprover = apprlLines.stream()
								  .filter(line->line.getAprvlTurn() == 1)
								  .findFirst()
								  .orElse(null);
		if (firstApprover == null || !"W".equals(firstApprover.getAprvlStatus())) {
            return false; // 이미 결재 시작됨
        }
		
		// 문서 상태 'C' (회수)로 업데이트
		approvalMapper.updateDocStauts(aprvlDocNo, AprvlDocStatus.WITHDRAWN.getCode());
		
		// 결재선 상태도 비활성화 
		for(AprvlLineVO line : apprlLines) {
			approvalMapper.updateApprLineStatus(AprvlLineStatus.Canceled.getCode(), aprvlDocNo, line.getEmpId());
		}
		
		return true;
	}

	



	
}
