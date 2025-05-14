package kr.or.ddit.works.meeting.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 화상회의 이력 MEETINGS VO
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
public class MeetingVO implements Serializable {
	
	private Long meetingNo;      			//미팅 번호
	private String empId;     		 		//사원 아이디
	private String meetingTitle;      		//회의 제목
	private String meetingStartTime;      	//회의 시작 시간
	private String meetingEndTime;      	//회의 종료 시간
	private String meetingCreatedAt;      	//회의 생성일

}
