package kr.or.ddit.works.approval.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 양식 폴더 DOC_FOLDER VO
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
public class DocFolderVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String docFolderNo;      		//양식폴더번호
	private String parentDocFolder;      	//상위양식폴더번호
	private String docFolderName;      		//폴더명

	
	private List<DocFormVO> docFormList;		// 문서 양식 has many 관계
	
	
}
