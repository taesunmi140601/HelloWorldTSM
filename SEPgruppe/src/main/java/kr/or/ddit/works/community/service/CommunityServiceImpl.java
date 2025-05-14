package kr.or.ddit.works.community.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.works.community.vo.CommentVO;
import kr.or.ddit.works.community.vo.CommunityVO;
import kr.or.ddit.works.mybatis.mappers.CommunityMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommunityServiceImpl implements CommunityServcie {

	@Autowired
	private CommunityMapper mapper;

	@Override
	public List<CommunityVO> memberCommunityList(String empId) {
		return mapper.memberCommunityList(empId);
	}

	@Override
	public List<CommunityVO> communityMember(Long communityNo) {
		log.info("==============================서비스 코드 {}",mapper.communityMember(communityNo));
		return mapper.communityMember(communityNo);
	}

	@Override
	public List<CommunityVO> communityList(String empId) {
		return mapper.communityList(empId);
	}

	@Override
	public List<CommunityVO> boardList(Long communityNo) {
		return mapper.boardList(communityNo);
	}

	@Override
	public List<CommunityVO> boardNoList(Long boardNo) {
		return mapper.boardNoList(boardNo);
	}

	@Override
	@Transactional
	public void insertCommunity(Map<String, Object> roomData, Map<String, Object> roomMemberData, Map<String, Object> roomJoinData) {

		// COMMUNITY NO 생성
		Long communityNo = mapper.communityNo()+1;

		// REQUEST NO 생성
		Long requestNo = mapper.requestNo()+1;

		// 방 개설 시 role 기본값
		String memberRole = "방 주인";

		// 방 개설 시 활동 기본값
		String memberActivityStatus = "활동중";

		// COMMUNITY 테이블에 INSERT
		roomData.put("communityNo", communityNo);
		mapper.insertRoom(roomData);

		// JOIN_STATUS 테이블에 INSERT
		roomJoinData.put("communityNo", communityNo);
		roomJoinData.put("requestNo", requestNo);
		mapper.insertStatusMember(roomJoinData);

		// COMMUNITY_MEMBER 테이블에 INSERT
		roomMemberData.put("communityNo", communityNo);
		roomMemberData.put("requestNo", requestNo);
		roomMemberData.put("memberRole", memberRole);
		roomMemberData.put("memberActivityStatus", memberActivityStatus);
		mapper.insertRoomMember(roomMemberData);

	}

	@Override
	public int insertBoard(Map<String, Object> communityData) {
		return mapper.insertBoard(communityData);
	}

	@Override
	public CommunityVO selectPostDetail(Long postNo) {
		return mapper.selectPostDetail(postNo);
	}

	@Override
	@Transactional
	public void updateLike(Long postNo, Map<String, Object> likesTBData, Map<String, Object> postTBData) {

		mapper.likeInsert(likesTBData);

		long likeCnt = mapper.selectLike(postNo);

		postTBData.put("postLikeCount", likeCnt);
		mapper.likeUpdate(postTBData);
	}

	@Override
	public Boolean likeStatus(Map<String, Object>likeData) {
		return mapper.likeStatus(likeData);
	}

	@Override
	@Transactional
	public void likeDelete(Long postNo, Map<String, Object> likeTBData, Map<String, Object> postTBData) {
		mapper.likeDelete(likeTBData);

		long likeCnt = mapper.selectLike(postNo);

		postTBData.put("likeTBData", likeCnt);
		mapper.likeUpdate(postTBData);

	}

	@Override
	public int selectLike(Long postNo) {
		return mapper.selectLike(postNo);
	}

	@Override
	public List<CommunityVO> roomManagement(Map<String, Object> roomData) {
		return mapper.roomManagement(roomData);
	}

	@Override
	public boolean roomDataUpdate(Map<String, Object> roomData) {
		return mapper.roomDataUpdate(roomData) > 0;
	}

	@Transactional
	@Override
	public void roomJoinStatus(Map<String, Object> memberData, Map<String, Object> joinStatusData) {

		// REQUEST NO 조회
		Long requestNo = mapper.memberRequestNo(joinStatusData);

		// JOIN_STATUS UPDATE
		joinStatusData.put("requestNo", requestNo);
		mapper.roomJoinStatus(joinStatusData);

		// userStatus 값 체크
		String userStatus = (String) joinStatusData.get("userStatus");

		// 승인일 경우에만 COMMUNITY_MEMBER INSERT
		if("승인".equals(userStatus)) {
			memberData.put("requestNo", requestNo);
			mapper.insertRoomMember(memberData);
		}
	}

	@Transactional
	@Override
	public void deleteRoomMember(Map<String, Object> deleteMemberData, Map<String, Object> deleteStatusData) {

		// COMMUNITY_MEMBER 테이블 삭제
		log.info("deleteMemberData: {}", deleteMemberData);

		mapper.roomMemberDelete(deleteMemberData);

		// JOIN_STATUS 테이블 삭제
	    log.info("deleteStatusData: {}", deleteStatusData);

		mapper.roomStatusDelete(deleteStatusData);


	}

	@Transactional
	@Override
	public void roomShutdown(Long communityNo, String closureReason) {

		// LIKES 테이블 삭제
		mapper.likesRoomShutdown(communityNo);

		// COMMENT 테이블 삭제
		mapper.commentRoomShutdown(communityNo);

		// POST 테이블 삭제
		mapper.postRoomShutdown(communityNo);

		// BOARD 테이블 삭제
		mapper.boardRoomShutdown(communityNo);

		// COMMUNITY_MEMBER 테이블 삭제
		mapper.communityMemberRoomShutdown(communityNo);

		// JOIN_STATUS 테이블 삭제
		mapper.joinStatusRoomShutdown(communityNo);

		// COMMUNITY 테이블 UPDATE
		mapper.communityRoomShutdown(communityNo, closureReason);

	}

	@Override
	public int joinStatusInsert(Map<String, Object> memberData) {
		return mapper.joinStatusInsert(memberData);
	}

	@Override
	public int insertPost(Map<String, Object> postData) {
		return mapper.insertPost(postData);
	}

	@Override
	public int insertComment(Map<String, Object> commentData) {
		return mapper.insertComment(commentData);
	}

	@Override
	public List<CommentVO> selectComment(Long postNo) {
		return mapper.selectComment(postNo);
	}

	@Override
	@Transactional
	public void transferRoomMaster(Map<String, Object> data) {
		mapper.updateCommunityMemberRoomMaster(data);
		mapper.updateCommunityMemberRoomMember(data);
		mapper.updateCommunityRoomMaster(data);

	}

	@Override
	public int deleteMember(Long memberNo) {
		return mapper.deleteMember(memberNo);
	}

	@Override
	public void insertInvitationMember(Long communityNo, String empId) {

	    Map<String, Object> data = new HashMap<>();
	    data.put("communityNo", communityNo);
	    data.put("empId", empId);

	    // 먼저 JOIN_STATUS에 insert → 여기서 requestNo가 생성됨
	    mapper.insertInvitationJoin(data);

	    // 그 다음에 생성된 requestNo를 다시 조회
	    Long requestNo = mapper.memberRequestNo(data);
	    data.put("requestNo", requestNo);

	    // 이제 COMMUNITY_MEMBER에 insert
	    mapper.insertInvitationMember(data);

	}

	@Override
	public CommunityVO selectCommunityDetail(long communityNo) {
		return mapper.selectCommunityDetail(communityNo);
	}

}


