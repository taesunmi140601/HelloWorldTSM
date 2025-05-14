package kr.or.ddit.works.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.chat.service.ChatService;
import kr.or.ddit.works.chat.vo.ChatRoomUserVO;
import kr.or.ddit.works.chat.vo.ChatRoomVO;
import kr.or.ddit.works.mybatis.mappers.EmployeeMapper;
import kr.or.ddit.works.organization.vo.EmployeeVO;

// 채팅방 생성과 관련된 부분을 처리하는 컨트롤러
@Controller
@RequestMapping("/{companyNo}/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private EmployeeMapper empMapper;

	/**
	 * 채팅방 목록 화면을 보여주는 메서드
	 * - 현재 로그인한 사용자 정보를 기반으로 채팅방 목록을 조회하고 화면에 전달
	 * @param companyNo 회사 번호
	 * @param model 뷰에 전달할 데이터 모델
	 * @param authentication 로그인 정보
	 * @return 채팅방 목록 화면 뷰 경로
	 */
	@GetMapping("")
	public String ChatHome(
		@PathVariable ("companyNo") String companyNo
		, Model model
		, Authentication authentication
	) throws JsonProcessingException {
		// 로그인된 사용자 ID 가져오기
		String empId = authentication.getName();
		// 로그인된 사용자의 상세정보 가져오기
		Object principal = authentication.getPrincipal();

		// 안전, 보안을 위한 조건문
		// principal 객체가 RealUserWrapper이며 내부 realUser가 EmployeeVO인지 확인 -> 맞다면 사용자 이름을 추출
		if (principal instanceof RealUserWrapper) {
	        RealUserWrapper wrapper = (RealUserWrapper) principal;
	        Object realUser = wrapper.getRealUser();

	        if (realUser instanceof EmployeeVO) {
	        	EmployeeVO user = (EmployeeVO) realUser;
	        	String userName = user.getEmpNm();
	        	model.addAttribute("userName", userName);
	        }
		}

		// 사용자 정보의 채팅방 목록 조회
		List<ChatRoomUserVO> rooms = chatService.selectListAllChatRoom(empId);
		model.addAttribute("rooms", rooms);

		// 채팅방 목록을 JSON 형식으로 변환하여 전달
		ObjectMapper mapper = new ObjectMapper();
	    String roomsJson = mapper.writeValueAsString(rooms);
	    model.addAttribute("roomsJson", roomsJson);

		model.addAttribute("empId", empId);
		model.addAttribute("companyNo", companyNo);

		return "/groupware/chat/messenger";
	}

	/**
	 * 채팅방 등록 (참여) 요청을 처리하는 메서드
	 * - 두 사용자가 채팅방에 참여하도록 처리하고, 채팅방 정보를 DB에 저장
	 * @param chatRoom 채팅방 정보
	 * @param model 뷰에 전달할 데이터 모델
	 * @return 응답 결과 (채팅방에 참여한 상대방 이름 포함)
	 */
	@PostMapping("/register")
    public ResponseEntity<?> registerChatRoomMembership(
    	@RequestBody ChatRoomVO chatRoom
    	, Model model
    ) {
        // 사용자 ID와 채팅방 ID를 이용하여 채팅방 참여 처리
		String myId = chatRoom.getCreateEmpId();                                // 사용자 ID 가져오기
		String setId = chatRoom.getRoomId().replace(myId, "").replace("_", ""); // 상대방 ID 추출
		String myName = empMapper.selectEmployee(myId).getEmpNm();              // 사용자 ID의 이름 가져오기
		String setName = empMapper.selectEmployee(setId).getEmpNm();            // 상대방 ID의 이름 가져오기
		ChatRoomUserVO roomUser1 = new ChatRoomUserVO();
		ChatRoomUserVO roomUser2 = new ChatRoomUserVO();

		// 채팅방 이름을 설정 (두 사용자의 이름)
		chatRoom.setRoomName(myName+","+setName);

		// 사용자 정보를 ChatRoomUserVO 객체로 설정
		roomUser1.setEmpId(myId);
		roomUser1.setRoomId(chatRoom.getRoomId());
		roomUser2.setEmpId(setId);
		roomUser2.setRoomId(chatRoom.getRoomId());

		// 채팅방과 사용자 정보를 DB에 저장
		chatService.insertChatRoom(chatRoom);
		chatService.insertChatRoomUser(roomUser1);
		chatService.insertChatRoomUser(roomUser2);

		// 상대방 이름을 응답으로 반환
		Map<String, String> response = new HashMap<>();
	    response.put("toName", setName);

        return ResponseEntity.ok(response);
    }

	/**
	 * 채팅방 생성 요청을 처리하는 메서드
	 * - 채팅방 생성 후 생성된 채팅방 정보를 클라이언트에게 전달
	 * @param roomRequest 채팅방 생성 요청 정보
	 * @return 생성된 채팅방 정보
	 */
	@MessageMapping("/chat.createRoom") // 웹소켓 경로
    @SendToUser("/queue/roomCreated")   // 요청 보낸 사용자에게만 결과 메시지 전송
    public ChatRoomVO createRoom(
    	@Payload ChatRoomVO roomRequest // 웹소켓으로 전달된 JSON 데이터를 객체로 자동 매핑
    ) {
        // 새로운 채팅방을 DB에 저장
		chatService.insertChatRoom(roomRequest);

        // 생성된 채팅방 정보를 반환
        return roomRequest;
    }

	/**
	 * 사용자가 속한 채팅방 목록을 조회하는 메서드
	 * @param companyNo 회사 번호
	 * @param model 뷰에 전달할 데이터 모델
	 * @return 채팅방 목록 화면 뷰 경로
	 */
	@GetMapping("room")
	public String selectListAllChatRoom(
		@PathVariable("companyNo") String companyNo
		, Model model
	) {
		// 회사 번호를 뷰에 전달
		model.addAttribute("companyNo", companyNo);
		return "gw:chat/chatList";
	}


	//채팅방입장
	@GetMapping("{roomId}")
	public String selectChatRoomDetail(
		@Param("roomId") String roomId
		, Model model
	) {
		model.addAttribute("roomId", roomId);
		return "gw:chat/chatDetail";
	}

	//채팅방 생성 폼UI
	@GetMapping("new")
	public String insertChatRoomFormUI(
		@Param("companyNo") String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		return "gw:chat/chatForm";
	}

	//채팅방 생성
	@PostMapping("new")
	public String insertChatRoom(
		@Param("companyNo") String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		return "gw:chat/chatForm";
	}

}