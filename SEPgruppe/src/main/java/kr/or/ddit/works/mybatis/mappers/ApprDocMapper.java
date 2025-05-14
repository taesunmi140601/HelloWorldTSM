package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.approval.vo.AprvlLineAutoVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.approval.vo.DocFolderVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;

/**
 * 전자결재 문서 관련 기능 매퍼 (ApprovalMapper)
 */
/**
 * 
 * @author JYS
 * @since 2025. 4. 8.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 4. 8.     	JYS	          최초 생성
 *
 * </pre>
 */
/**
 * 
 * @author JYS
 * @since 2025. 4. 11.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 4. 11.     	JYS	          최초 생성
 *
 * </pre>
 */
@Mapper
public interface ApprDocMapper {
	
	/**
	 * 최상위 폴더 조회
	 * @return
	 */
	public List<DocFolderVO> selectParentFolder();
	
	/**
	 * 하위 폴더 조회
	 * @param docFolderNo
	 * @return
	 */
	public List<DocFolderVO> selectFolderChild(@Param("docFolderNo")String docFolderNo);

	/**
	 * 폴더 내 저장된 문서 조회
	 * @param docFolderNo
	 * @return
	 */
	public List<DocFormVO> selectDocument(@Param("docFolderNo")String docFolderNo);

    /**
     * 결재 양식 전체 조회
     * @return 
     */
    public List<DocFormVO> selectListAllDocForm();
    
    /**
     * 결재 양식 제목을 검색 시 해당 양식만 조회
     * @return 검색된 양식 조회
     */
    public List<DocFormVO> selectListSearchDocForm(@Param("docFrmName")String docFrmName);

    
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
    public int selectAprvlDocSeq();
    
    /**
     * 문서 기안 - 결재요청
     * @param arpvlDoc
     * @return
     */
    public int insertDraft(AprvlDocVO arpvlDoc);
    
    /**
     * 결재선 번호 시퀀스 조회
     * @return
     */
    public long selectAprvlLineNoSeq();
    /**
     * 결재선 저장 - 결재요청
     * @param aprvlLine
     * @return
     */
    public int insertAprvlLine(AprvlLineVO aprvlLine);
    
    /**
     * 양식번호 시퀀스
     * @param docFolderNo
     * @return
     */
    public int selectNextSerial(String docFolderNo);
    
    /**
     * 관리자 - 새 문서 추가
     * @param docFormVo 추가할 문서 정보
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public int insertDocForm(DocFormVO docFormVo);
    
    /**
     * 관리자 - 문서 수정 (제목, 내용 등)
     * @param docFormVo 수정할 문서 정보
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public int updateDocForm(DocFormVO docFormVo);
    
    /**
     * 관리자 - 문서 비활성화 (삭제 대신 사용)
     * @param docFrmNo 문서 번호
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public int deactivateDocForm(@Param("docFrmNo") String docFrmNo);
    
    /**
     * 관리자 - 문서 활성화
     * @param docFrmNo 문서 번호
     * @return 성공 여부 (1: 성공, 0: 실패)
     */
    public int activateDocForm(@Param("docFrmNo") String docFrmNo);
    
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
     * 자동결재선 번호 자동 생성시퀀스 조회
     * @return
     */
    public String getNextAprvlLineNo();
    
    /**
     * 관리자 - 자동결재선 추가
     * @param aprvlLineAutoVo
     * @return
     */
    public int insertAutoLine(AprvlLineAutoVO aprvlLineAutoVo);
    
    /**
     * 관리자 - 자동결재선 수정
     * @param aprvlLineVo
     * @return
     */
    public int updateAutoLine(AprvlLineAutoVO aprvlLineAutoVo );
    
    /**
     * 관리자 - 자동결재선 삭제
     * @param aprvlLineVo
     * @return
     */
    public int deleteAutoLine(String aprvlLineNo );
    
    
    /**
     * 자동결재선 조건에 맞는 결재자 조회
     * @param searchCondition
     * @return
     */
    public List<EmployeeVO> selectApprLineSearchDeptPosition(Map<String, Object> searchCondition);

    
    /**
     *  직위 코드에 대한 정렬 순서 가져오기
     * @param positionCd
     * @return
     */
    public int getSortOrderByPositionCd(String positionCd);

    /**
     *  사용자의 결재자 상태에 따라 목록화 조회´
     * @param searchCondition
     * @return
     */
    public List<AprvlDocVO> selectApprDocStatusList(Map<String, Object> searchCondition);
    
    /**
     *  결재자 결재 대기 문서와 결재 예정문서 목록화 조회
     * @param searchCondition
     * @return
     */
    public List<AprvlDocVO> selectApprDocByTurnType(Map<String, Object> searchCondition);
    
    
    /**
     *  기안 문서 - 기안, 반려 
     * @param empId
     * @param status1
     * @param status2
     * @return
     */
    public List<AprvlDocVO> selectApprDocApprDrawnList(@Param("empId") String empId, @Param("status1") String status1, @Param("status2") String status2);
    
    /**
     * 결재문서 내용 상세 조회
     * @param aprvlDocNo
     * @return
     */
    public String selectAprvlDocContents(String aprvlDocNo);

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
    public int updateApprLineStatus(@Param("aprvlStatus") String aprvlStatus, @Param("aprvlDocNo") String aprvlDocNo, @Param("empId") String empId);

    /**
     * 남은 결재자 수 조회
     * @param aprvlDocNo
     * @return
     */
    public int countApprByWait(@Param("aprvlDocNo")String aprvlDocNo);
    
    /**
     * 결재문서 상태 변경
     * @param aprvlDocNo
     * @param updateDocStauts
     * @return
     */
    public int updateDocStauts(@Param("aprvlDocNo")String aprvlDocNo, @Param("docStatus")String updateDocStauts);
    
    /**
     * 결재문서 내용 변경
     * @param aprvlDocNo
     * @param aprvlDocContents
     * @return
     */
    public int updateApprovalHtml(@Param("aprvlDocNo")String aprvlDocNo, @Param("aprvlDocContents") String aprvlDocContents);
    
    /**
     * 기안자 empId 조회
     * @param aprvlDocNo
     * @return
     */
    public String selectDrafterEmpId(@Param("aprvlDocNo") String aprvlDocNo);
    
    /**
     * 결재문서 내용 + 양식명 조회
     * @param aprvlDocNo
     * @return
     */
    public AprvlDocVO selectApprDocContentsByDocNo(@Param("aprvlDocNo") String aprvlDocNo);
}
