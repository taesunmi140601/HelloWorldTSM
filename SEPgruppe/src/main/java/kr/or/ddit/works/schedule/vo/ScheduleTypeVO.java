package kr.or.ddit.works.schedule.vo;

import java.io.Serializable;

import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.Data;

/**
 * 일정 유형 SCHEDULE_TYPE VO
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
public class ScheduleTypeVO implements Serializable{
	
	private Long scheduleTypeNo;      	//일정유형번호
	private String scheduleTypeNm;      //(개인/부서/사내/프로젝트)
	
	
    private String department;
    private OrganizationVO employee;

}
