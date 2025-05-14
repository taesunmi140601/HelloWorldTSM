package kr.or.ddit.works.chat.vo;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.Data;

/**
 * 채팅방 CHAT_ROOM VO
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
public class ChatRoomVO implements Serializable {

	private String roomType;       // 채팅방 유형
	private String roomId;         // 채팅방 번호
	private String roomName;       // 채팅방 이름
	private String createEmpId;    // 채팅방 생성자 ID
	private String roomCreateDate; // 채팅방 생성일자

	private List<ChatRoomUserVO> chatRoomUser; // 1대N 관계
	private MessageVO message;                 // 1대1 관계
	private EmployeeVO employee;               // 1대1 관계

}
