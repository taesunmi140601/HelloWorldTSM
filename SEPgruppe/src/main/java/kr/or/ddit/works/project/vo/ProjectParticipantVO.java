package kr.or.ddit.works.project.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 프로젝트 참여자 PROJECT_PARTICIPANT VO
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
@Data
public class ProjectParticipantVO implements Serializable{
	private long projectParticipantNo;      //참여자 번호
	private long projectNo;      			//프로젝트 번호
	private String empId;      				//사원 아이디
	private String empNm;     // 사원 이름

}
