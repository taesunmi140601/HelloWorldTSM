package kr.or.ddit.works.meeting.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 화상회의 참여자 이력 MEETINGS_PARTICIPANTS VO
 * 
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
public class MeetigParticipantVO implements Serializable {
	
	private Long participantNo;      	//참여기록번호
	private Long meetingNo;      		//미팅 번호
	private String empId;      			//사원 아이디
	private String joinTime;      		//참여시간
	private String leaveTime;      		//퇴장시간

}
