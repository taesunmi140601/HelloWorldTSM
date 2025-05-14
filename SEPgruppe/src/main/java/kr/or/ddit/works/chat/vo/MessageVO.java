package kr.or.ddit.works.chat.vo;

import java.io.Serializable;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.Data;

/**
 * 메시지 MESSAGE VO
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
public class MessageVO implements Serializable{

	private Long msgId;         // 메시지 번호
	private String roomId;      // 채팅방 ID
	private String senderEmpId; // 송신자 ID
	private String msgContent;  // 메시지 내용
	private String sendDate;    // 메시지 전송일자
	private String sendTime;    // 메시지 전송 시간

	private EmployeeVO employee;         // 1대1 관계
	private ChatRoomVO chatRoom;         // 1대1 관계
	private OrganizationVO organization; // 1대1 관계

}
