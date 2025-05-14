package kr.or.ddit.works.approval.vo;

import java.io.Serializable;
import java.util.List;

import kr.or.ddit.works.attachFile.vo.AttachFileGroupVO;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.common.vo.CommCodeVO;
import lombok.Data;
import lombok.ToString;

/**
 * 문서 양식 DOC_FORM VO
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
@ToString(callSuper = true)
public class DocFormVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String docFrmNo;      //양식번호
	private String docFolderNo;      //양식폴더번호
	private String docFrmName;      //양식명
	private String docFrmContent;      //양식내용
	private String isEnabled;      //활성화여부
	private Long fileGroupNo;      //첨부파일 그룹 코드번호
	
	private String aprvlLineNo;      // 결재선 번호
    private String aprvlLineName;    // 결재선명
	
	private List<AprvlLineAutoVO> aprvlLineAuto; // 결재선 상세 정보 (1:N)
	
	private DocFolderVO docFolderVo;	// 양식 폴더 has a 관계
	private AttachFileGroupVO attachFileGroupVo; // 첨부파일 그룹 has a 관계
	private AttachFileVO attachFileVo; // 첨부파일 has a 관계
}
