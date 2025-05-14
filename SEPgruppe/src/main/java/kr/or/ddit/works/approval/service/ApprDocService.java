package kr.or.ddit.works.approval.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.approval.vo.AprvlLineAutoVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;

/**
 * 전자결재 서비스
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
public interface ApprDocService {
	
    /**
     * 결재 양식 전체 조회
     * @return 모든 양식 폴더 리스트
     */
    public List<DocFormVO> selectListAllDocForm();
    
    /**
     * 결재 양식 제목을 검색 시 해당 양식만 조회
     * @return 검색된 양식 조회
     */
    public List<DocFormVO> selectListSearchDocForm(@Param("docFrmName")String docFrmName);
    
    /**
     * 해당 폴더에 저장된 결재문서만 목록으로 조회
     * @param docFolderNo
     * @return
     */
    public List<DocFormVO> selectDocument(@Param("docFolderNo")String docFolderNo);
    
    
    /**
     * 문서 상세보기
     * @param docFrmNo 문서 번호
     * @return 
     */
    public DocFormVO selectdocFrmDeatil(@Param("docFrmNo")String docFrmNo);
    
    /**
     * 기안번호 시퀀스 조회
     * @return
     */
    public String selectAprvlDocSeq();
    
    /**
     * 문서 기안 - 결재요청
     * @param arpvlDoc
     * @return
     */
    public boolean insertDraft(AprvlDocVO arpvlDoc);
    
    /**
     * 결재선 저장 - 결재요청
     * @param aprvlLine
     * @return
     */
    public boolean insertAprvlLine(AprvlLineVO aprvlLine);
    
    /**
     * 관리자 - 새 문서 추가
     * @param docFormVo 추가할 문서 정보
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public boolean insertDocForm(String docFolderNo, DocFormVO docFormVo);
    
    
    /**
     * 관리자 - 문서 수정 (제목, 내용 )
     * @param docFormVo 수정할 문서 정보
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public boolean updateDocForm(DocFormVO docFormVo);
    
    /**
     * 관리자 - 문서 비활성화 (삭제 대신 사용)
     * @param docFrmNo 문서 번호
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public boolean deactivateDocForm(@Param("docFrmNo") String docFrmNo);
    
    /**
     * 관리자 - 문서 활성화
     * @param docFrmNo 문서 번호
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public boolean activateDocForm(@Param("docFrmNo") String docFrmNo);
    
    
    /**
     * 관리자, 일반 사원 - 자동결재선 목록 조회
     * @return
     */
    public List<DocFormVO> selectListAllAutoLine();
    
    /**
     * 자동결재선 상세 조회
     * @return
     */
    public List<DocFormVO> selectDetailAutoLine(String aprvlLineNo);
    
    /**
     * 관리자 - 자동결재선 추가
     * @param aprvlLineVo
     * @return
     */
    public void insertAutoLine(List<AprvlLineAutoVO> aprvlLineAutoList);
    
    /**
     * 관리자 - 자동결재선 수정
     * @param aprvlLineVo
     * @return
     */
    public void updateAutoLine(String aprvlLineNo, List<AprvlLineAutoVO> list);
    
    /**
     * 관리자 - 자동결재선 삭제
     * @param aprvlLineVo
     * @return
     */
    public boolean deleteAutoLine(String aprvlLineNo );
    
    /**
     * 자동결재선 조건에 맞는 결재자 조회
     * @param searchCondition
     * @return
     */
    public List<EmployeeVO> selectApprLineSearchDeptPosition(Map<String, Object> searchCondition);
    
    /**
     *  사용자의 결재자 상태에 따라 목록화 조회
     * @param searchCondition
     * @return
     */
    public List<AprvlDocVO> selectApprDocStatusList(Map<String, Object> searchCondition);
    
    
    /**
     * 사용자 결재 순서에 따라 대기와 예정으로 목록화 나뉨
     * @param searchCondition
     * @return
     */
    public List<AprvlDocVO> selectApprDocListByTurn(Map<String, Object> searchCondition);
    
    
    /**
     * 결재문서의 결재선 조회
     * @param docNo
     * @return
     */
    public List<AprvlLineVO> selectApprLine(String docNo);

    /**
     * 결재선의 결재자 결재상태 업데이트
     * @param aprvlStatus
     * @param aprvlDocNo
     * @param empId
     * @return
     */
    public boolean updateApprLineStatus(String aprvlStatus, String aprvlDocNo, String empId);
    
    /**
     * 결재문서 내용 변경
     * @param aprvlDocNo
     * @param aprvlDocContents
     * @return
     */
    public boolean updateApprovalHtml(String aprvlDocNo, String aprvlDocContents);

    /**
     * 기안자 문서 회수 
     * @param aprvlDocNo
     * @param userId
     * @return
     */
    public boolean updateCancleApprDoc(String aprvlDocNo, String userId);
    
}
