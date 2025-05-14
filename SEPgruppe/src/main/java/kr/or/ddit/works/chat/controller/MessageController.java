package kr.or.ddit.works.chat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.works.chat.service.ChatService;
import kr.or.ddit.works.chat.vo.ChatRoomUserVO;
import kr.or.ddit.works.chat.vo.MessageVO;
import kr.or.ddit.works.mybatis.mappers.OrganizationMapper;
import kr.or.ddit.works.organization.vo.OrganizationVO;

//채팅방 내에서 이루어지는 실시간 메시지 전송 및 수신을 담당하는 컨트롤러 + 채팅방 나갈 때 방을 삭제하는 컨트롤러
@Controller
@RequestMapping("/chat")
public class MessageController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private OrganizationMapper organiMapper;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	/**
	 * 웹소켓을 통해 채팅 메시지를 수신하고, 해당 채팅방 구독자들에게 메시지를 전송
	 * @param roomId 채팅방 ID
	 * @param message 전송된 메시지 내용
	 * @return 전송된 메시지 객체
	 */
	@MessageMapping("/{roomId}") // 웹소캣 메시지 수신을 위한 핸들러 (웹소캣 경로에 매핑)
	@SendTo("/topic/{roomId}")   // 해당 채팅방 ID에 맞는 구독자들에게 메시지를 전송
	public MessageVO broadcastMessage(
		@DestinationVariable String roomId // 경로 변수로 받은 채팅방 ID
		, MessageVO message 			   // 전송된 메시지 내용
	) {
		// 보낸 사람(empId) 정보를 통해 조직도 정보를 조회
		String empId = message.getSenderEmpId();
		OrganizationVO organi = organiMapper.selectOrganization(empId);
		message.setOrganization(organi);

		// 메시지를 DB 저장
		chatService.insertMessage(message);

		// 수신자 ID 추출 후 알람 경로로 메시지 전송
		String reId = roomId.replace(empId, "").replace("_", "");
		messagingTemplate.convertAndSend("/topic/alarm/" + reId, message);

		// 전송된 메시지 반환
		return message;
	}

	/**
	 * 구독 요청 시 호출되는 핸들러
	 * - 현재 로그인된 사용자의 ID 반환
	 * @param headers STOMP 헤더
	 * @param authentication 로그인 정보
	 * @return 사용자 ID
	 */
	@SubscribeMapping("/{roomId}")
	public String subscribeHandler(
		@Headers Map<String, Object> headers
		, Authentication authentication
	) {
		String empId = authentication.getName();

		// 현재 로그인된 사용자의 ID
		String sub_id = empId;
		return sub_id;
	}

	/**
	 * 채팅방 상세보기 요청 처리
	 * - 채팅방에 속한 메시지들을 불러와 뷰에 전달
	 * @param roomId 채팅방 ID
	 * @param toName 상대방 이름
	 * @param model 뷰 전달용 모델
	 * @return 채팅 화면 뷰 경로
	 */
	@GetMapping("{roomId}")
	public String selectChatRoomDetail(
		@PathVariable("roomId") String roomId
		, Model model
		, @RequestParam(required = false) String toName
	) {
		// 채팅방에 속한 모든 메시지 조회
		List<MessageVO> messageList = chatService.findAllMessageByRoomId(roomId);

		// 뷰에 전달한 데이터 설정
		model.addAttribute("roomId", roomId);
		model.addAttribute("toName", toName);
		model.addAttribute("messageList", messageList);

		return "groupware/chat/chatHome";
	}

	/**
	 * 현재 사용자가 참여 중인 채팅방 목록 조회
	 * @param model 뷰에 전달할 모델
	 * @param authentication 로그인 정보
	 * @return 채팅방 목록 화면 뷰 경로
	 */
	@GetMapping("list")
	public String selectListAllChatRoom(
		Model model
		, Authentication authentication
	) {
		// 현재 로그인한 사용자 정보
		String empId = authentication.getName();

		// 사용자 정보의 채팅방 목록 조회
		List<ChatRoomUserVO> rooms = chatService.selectListAllChatRoom(empId);

		// 뷰에 전달할 데이터 설정
		model.addAttribute("rooms", rooms);
		return "groupware/chat/chatList";
	}

	/**
	 * 채팅방 나가기 처리
	 * - 사용자가 채팅방에서 나가며, 마지막 사용자일 경우 방 삭제 처리
	 * @param roomId 나갈 채팅방 ID
	 * @param authentication 로그인 정보
	 * @return 성공 메시지 (채팅방 삭제 여부 포함)
	 */
	@DeleteMapping("{roomId}")
	public ResponseEntity<?> deleteChatRoom(
		@PathVariable String roomId
		, Authentication authentication
	) {
		// 현재 로그인한 사용자 정보
		String empId = authentication.getName();

		// 사용자가 채팅방 나가도록 처리
		chatService.deleteChatRoomUser(roomId, empId);

		// 채팅방에 남아있는 사용자가 있는지 확인
		List<ChatRoomUserVO> remainingUser = chatService.selectChatRoomUser(roomId);
		if (remainingUser == null || remainingUser.isEmpty()) {
			// 마지막 사용자가 나가면 방 삭제 처리
			chatService.deleteChatRoom(roomId);
			return ResponseEntity.ok("마지막 사용자 - 방 삭제됨");
		}

		return ResponseEntity.ok("나가기 성공"); // 채팅방 나가기를 성공 메시지 반환
	}
}