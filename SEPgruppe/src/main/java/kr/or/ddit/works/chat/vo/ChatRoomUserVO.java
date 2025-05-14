package kr.or.ddit.works.chat.vo;

import java.io.Serializable;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.Data;

/**
 * 채팅 CHAT VO
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
public class ChatRoomUserVO implements Serializable{

	private String userId;   // 참여자 고유번호
	private String roomId;   // 채팅방ID
	private String empId;    // 채팅 참여자ID
	private String joinTime; // 참여 시간
	private String role;     // 채팅방 내 역할

	private EmployeeVO employee; // 1대1 관계
	private ChatRoomVO chatRoom; // 1대1 관계


}
