package kr.or.ddit.works.approval.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 기안 APRVL_DOC VO
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class AprvlDocVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String aprvlDocNo;      //기안번호
	private String docFrmNo;      //양식번호
	private String aprvlDocTitle;      //제목
	private String aprvlDocContents;      //내용
	private String aprvlDocStatus;      //진행상태
	private String isEmergency;      //긴급여부
	private String submitDate;      //상신날짜
	private String closingDate;      //마감날짜
	private Long fileGroupNo;      //파일그룹번호
	private String aprvdDocNo;      //결재완료문서번호
	private String aprvdDate;      //결재완료날짜
	
	private String docFrmName;
	
	private String drafterName; // 기안자 이름
	
	private List<AprvlLineVO> aprvlLineList; // 결재선 has many
	
	private String statusName;      //문서 상태 이름
	private String statusColor;		// 문서 상태별 색상 enum
	
	private String leaveStartDate;
	private String leaveEndDate;
	private String usedLeave;

}
