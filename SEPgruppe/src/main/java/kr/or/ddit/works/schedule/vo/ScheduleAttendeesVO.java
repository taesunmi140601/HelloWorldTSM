package kr.or.ddit.works.schedule.vo;

/**
 * 일정 참석자 SCHEDULE_ATTENDEES VO
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
public class ScheduleAttendeesVO {
	
	private Long schdlNo;      				//일정순번
	private String empId;      				//사원 아이디
	private String attendanceStatus;      	//참석 여부 (참석/불참/미정)

}
