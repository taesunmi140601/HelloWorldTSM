package kr.or.ddit.works.organization.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 근태 상세 DCLZ_PARTICULAR VO
 * 
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
public class DclzParticularVO implements Serializable{
	
	private Long dclzPartclrNo;      	//특이사항번호
	private Long dclzNo;      			//근태번호
	private String startDate;      		//시작일
	private String endDate;      		//종료일
	private String partclrContent;      //내용(지각/조퇴/외근/출장/휴가)

}
