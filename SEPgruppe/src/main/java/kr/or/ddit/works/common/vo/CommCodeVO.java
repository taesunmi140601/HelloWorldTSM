package kr.or.ddit.works.common.vo;

import java.io.Serializable;
import java.util.List;

import kr.or.ddit.works.approval.vo.DocFormVO;
import lombok.Data;

/**
 * 공통코드 COMM_CODE VO
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
public class CommCodeVO implements Serializable {

	private String commCodeNo;      			//공통코드
	private String parentCommCodeNo;      	//상위공통코드
	private String commCodeName;      		//공통코드명
	private String commCodeDesc;      		//코드설명
	private String commCodeCreatedDate;     //최초등록일시
	private String commCodeUpdateDate;      //수정일자
	private String commCodeYn;      		//사용여부
	
	List<DocFormVO> docFormList;
}
