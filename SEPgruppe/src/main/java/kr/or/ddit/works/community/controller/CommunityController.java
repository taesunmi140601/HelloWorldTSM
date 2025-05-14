package kr.or.ddit.works.community.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.alarm.controller.GWAlarmController;
import kr.or.ddit.works.community.service.CommunityServcie;
import kr.or.ddit.works.community.vo.CommentVO;
import kr.or.ddit.works.community.vo.CommunityVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/{companyNo}/community")
public class CommunityController {

	@Autowired
	private CommunityServcie service;

	@Autowired
	private GWAlarmController gwAlarmController;

	// 커뮤니티 목록 조회
	@GetMapping("")
	public String selectListAllCommunity(
		@PathVariable("companyNo") String companyNo
		, Model model
        , HttpSession session
        , Authentication authentication
	) {

		// 로그인한 사용자 정보 가져오기
        RealUserWrapper userWrapper = (RealUserWrapper) authentication.getPrincipal();

        // 로그인한 사용자의 커뮤니티 목록 가져오기
        List<CommunityVO> communityList = service.memberCommunityList(authentication.getName());

        // 생성된 커뮤니티목록 전체 가져오기
        List<CommunityVO> allCommunityList = service.communityList(authentication.getName());



        log.info("communityList ================= {} ", communityList);
        session.setAttribute("realUser", userWrapper.getRealUser());
		model.addAttribute("companyNo",companyNo);
		model.addAttribute("communityList", communityList);
		model.addAttribute("allCommunityList", allCommunityList);
		return "gw:community/communityList";
	}

	// 가입된 멤버 목록 출력
	@GetMapping("/getMembers")
	public ResponseEntity<Map<String, Object>> communityMembers(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNo") Long communityNo
	){
		List<CommunityVO> members = service.communityMember(communityNo);
		List<CommunityVO> board = service.boardList(communityNo);
		log.info("members =================================members{}", members);
		Map<String, Object> response = new HashMap<>();
		response.put("board", board);
		response.put("members", members);
		log.info("response =================================members{}", response);
		return ResponseEntity.ok(response);
	}

	// 게시글 출력
	@GetMapping("/getPost")
	public ResponseEntity<Map<String, Object>> boardList(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNo") Long communityNo
	){
		List<CommunityVO> board = service.boardList(communityNo);

		Map<String, Object> response = new HashMap<>();
		response.put("board", board);

		log.info("========================== response {}", response);

		return ResponseEntity.ok(response);
	}

	// 특정 게시판에 해당하는 게시글 출력
	@GetMapping("/getBoardNoPost")
	public ResponseEntity<Map<String, Object>> boardNoPostList(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("boardNo") Long boardNo
	){
		List<CommunityVO> boardNoPost = service.boardNoList(boardNo);

		Map<String, Object> response = new HashMap<>();
		response.put("boardNoPost", boardNoPost);

		return ResponseEntity.ok(response);
	}

	// 게시글 상세조회
	@GetMapping("/detailPost")
	public ResponseEntity<Map<String, Object>> selectPostDetail(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("postNo") Long postNo
	){
		CommunityVO postDetail = service.selectPostDetail(postNo);

		Map<String, Object> response = new HashMap<>();
		response.put("postDetail", postDetail);

		return ResponseEntity.ok(response);
	}


	// 커뮤니티방 만들기
	@PostMapping("/insertRoom")
	public String communityRoomInsert(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNm") String communityNm
		, @RequestParam("communityContent") String communityContent
		, Authentication authentication

	) {
		String empId = authentication.getName();

		// COMMUNITY 테이블 INSERT
		Map<String, Object> community = new HashMap<>();
		community.put("empId", empId);
		community.put("communityNm", communityNm);
		community.put("communityContent", communityContent);

		// JOIN_STATUS 테이블 INSERT
		Map<String, Object> joinStatus = new HashMap<>();
		joinStatus.put("empId", empId);

		// COMMUNITY_MEMBER 테이블 INSERT
		Map<String, Object> communityMember = new HashMap<>();
		communityMember.put("empId", empId);

		log.info("communityMember=====================", communityMember);

		service.insertCommunity(community, joinStatus, communityMember);

		return "redirect:/" + companyNo + "/community";

	}

	// 게시판 만들기
	@PostMapping("/insertBoard")
	public ResponseEntity<Map<String, Object>> communityBoardInsert(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNo") Long communityNo
		, @RequestParam("boardContent") String boardContent
		, @RequestParam("boardNm") String boardNm
	) {
		Map<String, Object> communityData = new HashMap<>();
		communityData.put("communityNo", communityNo);
		communityData.put("boardContent", boardContent);
		communityData.put("boardNm", boardNm);
		service.insertBoard(communityData);

		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");
		return ResponseEntity.ok(response);
	}

	// 좋아요 등록
	@PostMapping("/postLike")
	@ResponseBody
	public Map<String, Object> postLikeInsert(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("postNo") Long postNo
		, @RequestParam("memberNo") Long memberNo
		, @RequestParam("isLiked") boolean isLiked
	) {
		// LIKES 테이블 INSERT
		Map<String, Object> likesTBData = new HashMap<>();
		likesTBData.put("postNo", postNo);
		likesTBData.put("memberNo", memberNo);

		// POST 테이블 UPDATE
		Map<String, Object> postTBData = new HashMap<>();
		postTBData.put("postNo", postNo);

		if(isLiked) {
			service.updateLike(postNo, likesTBData, postTBData);
		}else {
			service.likeDelete(postNo, likesTBData, postTBData);
		}

		long updatedLikeCount = service.selectLike(postNo);
		Map<String, Object> result = new HashMap<>();
		result.put("likeCount", updatedLikeCount);
		return result;
	}

	// 좋아요 상태관리
	@GetMapping("/postLiktStatus")
	public ResponseEntity<Map<String, Object>> likeStatus(
			@PathVariable("companyNo") String companyNo
			, @RequestParam("postNo") Long postNo
			, @RequestParam("memberNo") Long memberNo
		){

			Map<String, Object> likeData = new HashMap<>();
			likeData.put("postNo", postNo);
			likeData.put("memberNo", memberNo);

			Boolean isLiked = service.likeStatus(likeData);

			Map<String, Object> response = new HashMap<>();
			response.put("isLiked", isLiked);

			return ResponseEntity.ok(response);
		}

	// 방 관리 클릭
	@GetMapping("/roomManagement")
	public ResponseEntity<Map<String, Object>> roomManagement(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNo") Long communityNo
	){
		Map<String, Object> roomData = new HashMap<>();
		roomData.put("communityNo", communityNo);

		List<CommunityVO> roomManagement = service.roomManagement(roomData);

		Map<String, Object> response = new HashMap<>();
		response.put("roomManagement", roomManagement);

		return ResponseEntity.ok(response);
	}

	// 방 정보 수정
	@PostMapping("/roomDataUpdate")
	public ResponseEntity<Map<String, Object>> roomDataUpdate(
		@PathVariable("companyNo") String companyNo
		, @RequestParam(value = "communityContent", required = false) String communityContent
		, @RequestParam(value = "communityIsClosed", required = false, defaultValue = "N") String communityIsClosed
		, @RequestParam(value = "closureReason", required = false) String closureReason
		, @RequestParam(value = "closureTimestamp", required = false) String closureTimestamp
		, @RequestParam("communityNo") Long communityNo // 방 번호
	    , @RequestParam(value = "newMasterEmpId", required = false) String newMasterEmpId // 방장 변경이 필요할 때만 전달

	){
	    Map<String, Object> result = new HashMap<>();

	    // 1. 방 정보 수정 처리
		Map<String, Object> roomDataUpdate = new HashMap<>();
		roomDataUpdate.put("communityContent", communityContent);
		roomDataUpdate.put("communityIsClosed", communityIsClosed);
		roomDataUpdate.put("closureReason", closureReason);
		roomDataUpdate.put("closureTimestamp", closureTimestamp);
		roomDataUpdate.put("communityNo", communityNo);

		boolean updateResult  = service.roomDataUpdate(roomDataUpdate);
	    result.put("roomDataUpdate", updateResult);

	    // 2. 방장 권한 이전 처리 (선택적)
	    if (newMasterEmpId != null && !newMasterEmpId.isEmpty()) {
	        Map<String, Object> transferData = new HashMap<>();
	        transferData.put("communityNo", communityNo);
	        transferData.put("empId", newMasterEmpId);

	        service.transferRoomMaster(transferData);
	        result.put("transferRoomMaster", true);
	    }

		return ResponseEntity.ok(result);
	}

	// 가입 승인 또는 거절
	@PostMapping("/joinStatus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> joinStatus(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("userStatus") String userStatus
		, @RequestParam(value = "rejectionReason", required = false) String rejectionReason
		, @RequestParam("communityNo") Long communityNo
		, @RequestParam("empId") String empId
		, @RequestParam(value = "memberRole", required = false) String memberRole
		, @RequestParam(value = "memberActivityStatus", required = false) String memberActivityStatus
	){

		// JOIN_STATUS UPDATE
		Map<String, Object> roomJoinStatus = new HashMap<>();
		roomJoinStatus.put("userStatus", userStatus);
		roomJoinStatus.put("rejectionReason", rejectionReason);
		roomJoinStatus.put("communityNo", communityNo);
		roomJoinStatus.put("empId", empId);

		// COMMUNITY_MEMBER INSERT
		Map<String, Object> communityMember = new HashMap<>();
		communityMember.put("communityNo", communityNo);
		communityMember.put("empId", empId);
		communityMember.put("memberRole", memberRole);
		communityMember.put("memberActivityStatus", memberActivityStatus);

		service.roomJoinStatus(communityMember, roomJoinStatus);

		// 응답데이터
		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");

		// 최신 상태 정보 조회
		Map<String, Object> roomData = new HashMap<>();
		roomData.put("communityNo", communityNo);
		List<CommunityVO> roomList = service.roomManagement(roomData);

		roomList.forEach(room->{
			room.getJoinStatus().stream()
			.filter(js -> empId.equals(js.getEmpId()))
			.findFirst()
			.ifPresent(js->response.put("updateStatus", js));
		});
		gwAlarmController.sendCommunityAlarm2(communityNo, empId);
		return ResponseEntity.ok(response);
	}

	// 회원 강퇴처리
	@PostMapping("/deleteMember")
	public ResponseEntity<Map<String, Object>> deleteMember(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("requestNo") Long requestNo
		, @RequestParam("memberNo") Long memberNo

	){
		// COMMUNITY_MEMBER 테이블 DELETE
		Map<String, Object> deleteMemberData = new HashMap<>();
		deleteMemberData.put("memberNo", memberNo);
		deleteMemberData.put("requestNo", requestNo);

		// JOIN_STATUS 테이블 DELETE
		Map<String, Object> deleteStatusData = new HashMap<>();
		deleteStatusData.put("requestNo", requestNo);

		log.info("======================= requestNo{}", requestNo);
		log.info("======================= memberNo{}", memberNo);
		service.deleteRoomMember(deleteMemberData, deleteStatusData);

		log.info("코드실행1~~~~~~~~~~~~~~~~~~~~~~~ {}", deleteMemberData);
		log.info("코드실행2~~~~~~~~~~~~~~~~~~~~~~~ {}", deleteStatusData);

		// 응답데이터
		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");

		return ResponseEntity.ok(response);

	}

	// 방 폐쇄처리
	@PostMapping("/shutDown")
	public ResponseEntity<Map<String, Object>> shutDown(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNo") Long communityNo
		, @RequestParam("closureReason") String closureReason
	){
		service.roomShutdown(communityNo, closureReason);

		// 응답데이터
		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");

		return ResponseEntity.ok(response);
	}
	// 방 가입하기
	@PostMapping("/joinStatusInsert")
	public String insertJoinStatus(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("communityNo") Long communityNo
        , Authentication authentication


	) {
		String empId = authentication.getName();

		Map<String, Object> memberData = new HashMap<>();
		memberData.put("empId", empId);
		memberData.put("communityNo", communityNo);

		String receiverEmpId = service.selectCommunityDetail(communityNo).getEmpId();
		service.joinStatusInsert(memberData);
		gwAlarmController.sendCommunityAlarm(communityNo, receiverEmpId);
		return "redirect:/" + companyNo + "/community";
	}

	// 게시글 작성
	@PostMapping("/postInsert")
	@ResponseBody
	public Map<String, Object> postInsert(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("boardNo") Long boardNo
		, @RequestParam("memberNo") Long memberNo
		, @RequestParam("postTitle") String postTitle
		, @RequestParam("postContent") String postContent
		, Model model
        , Authentication authentication

	) {
		String empId = authentication.getName();

		Map<String, Object> insertPost = new HashMap<>();
		insertPost.put("empId", empId);
		insertPost.put("boardNo", boardNo);
		insertPost.put("memberNo", memberNo);
		insertPost.put("postTitle", postTitle);
		insertPost.put("postContent", postContent);
		service.insertPost(insertPost);
	    Long postNo = (Long) insertPost.get("postNo"); // ★ 여기서 꺼내기
	    //model.addAttribute("postNo", postNo);

	    Map<String, Object> response = new HashMap<>();
	    response.put("redirectUrl", "/sep/" + companyNo + "/community/detailPost?postNo=" + postNo);
	    response.put("postNo", postNo);

	    return response;
	}

	// 댓글 등록
	@PostMapping("/insertComment")
	public ResponseEntity<Map<String, Object>> commentInsert(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("postNo") Long postNo
		, @RequestParam("memberNo") Long memberNo
		, @RequestParam("replyContent") String replyContent

	){

		Map<String, Object> commentData = new HashMap<>();
		commentData.put("postNo", postNo);
		commentData.put("memberNo", memberNo);
		commentData.put("replyContent", replyContent);
		service.insertComment(commentData);
		CommunityVO post = service.selectPostDetail(postNo);

		// 알림 대상
		String empId = post.getEmpId();

		// 응답데이터
		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");

		return ResponseEntity.ok(response);
	}

	// 댓글 조회
	@GetMapping("/selectComment")
	public ResponseEntity<Map<String, Object>> commentSelect(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("postNo") Long postNo

	){

	    List<CommentVO> commentList = service.selectComment(postNo); // 조회 결과 저장

		// 응답데이터
		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");
	    response.put("comments", commentList); // 댓글 리스트 응답에 포함


		return ResponseEntity.ok(response);
	}

	// 탈퇴
	@PostMapping("/communityMemberdeleteMember")
	public ResponseEntity<Map<String, Object>> deleteMember(
		@PathVariable("companyNo") String companyNo
		, @RequestParam("memberNo") Long memberNo

	) {

		int deleteMember = service.deleteMember(memberNo);

		Map<String, Object> response = new HashMap<>();
		response.put("result", "success");
		response.put("message", "성공");
	    response.put("comments", deleteMember);

		return ResponseEntity.ok(response);
	}


	// 멤버 초대 기능
	@PostMapping("/{communityNo}/invite")
	public ResponseEntity<Void> inviteMember(
		@PathVariable("companyNo") String companyNo,
        @PathVariable("communityNo") Long communityNo,
        @RequestBody Map<String, String> requestBody

    ) {
	    String empId = requestBody.get("empId");
	    service.insertInvitationMember(communityNo, empId);
	    return ResponseEntity.ok().build();
    }

}
