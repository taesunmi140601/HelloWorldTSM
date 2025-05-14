package kr.or.ddit.works.attachFile.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 파일그룹 ATTACH_FILE_GROUP VO
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
@Data
public class AttachFileGroupVO implements Serializable {
	
	private Long fileGroupNo;   				//코드번호
	private String fileGroupName;     			//파일그룹번호
	private String fileGroupCreatedDate;      	//생성일
	private String fileGroupUpdatedDate;      	//수정일
	private String fileGroupStatus;      		//상태여부(활성화,삭제 등)
	
	private List<AttachFileVO> attachFileList; 	// 첨부파일 has-many관계
}
