package kr.or.ddit.works.organization.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 직위 POSTION VO
 * @author JYS
 * @since 2025. 3. 12.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 12.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class PositionVO implements Serializable{
	
	private String positionCd;      	// 직위코드
	private String positionName;      	// 직위명
	private Integer sortOrder;			// 정렬 순서 (순서바꾸기용)
	
	private int memberCount;			//사용 멤버 수

}
