package kr.or.ddit.works.mybatis.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.alarm.vo.AlarmHistoryVO;
import kr.or.ddit.works.community.vo.CommentVO;
import kr.or.ddit.works.community.vo.CommunityVO;

/**
 * 커뮤니티 매퍼
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
@Mapper
public interface CommunityMapper {

	/**
	 *  커뮤니티 목록 조회
	 * @return
	 */
//	public List<CommunityVO> selectListAllCommunity();

	/**
	 *  커뮤니티 상세 조회
	 *
	 * @param communityNo 커뮤니티 방 번호
	 * @return
	 */
	public CommunityVO selectCommunityDetail(@Param("communityNo") long communityNo);


	/**
	 * 가입된 커뮤니티 목록 조회
	 * @return
	 */
	public List<CommunityVO> memberCommunityList(String empId);

	/**
	 * 가입된 커뮤니티의 회원 목록 조회
	 * @param communityNo
	 * @return
	 */
	public List<CommunityVO> communityMember(Long communityNo);

	/**
	 * 전체 커뮤니티 목록 조회
	 * @return
	 */
	public List<CommunityVO> communityList(String empId);

	/**
	 * 커뮤니티방에 작성된 게시글 전체 조회
	 * @param communityNo
	 * @return
	 */
	public List<CommunityVO> boardList(Long communityNo);

	/**
	 * 특정 게시판에 해당되는 게시글 전체 조회
	 * @param boardNo
	 * @return
	 */
	public List<CommunityVO> boardNoList(Long boardNo);

	/**
	 * communityNo 가져오기
	 * @return
	 */
	public Long communityNo();

	/**
	 * requestNo 가져오기
	 * @return
	 */
	public Long requestNo();

	/**
	 * 커뮤니티방 만들기(COMMUNITY 테이블 INSERT)
	 * @param empId
	 * @return
	 */
	public int insertRoom(Map<String, Object> roomData);

	/**
	 * 커뮤니티방 만들기(COMMUNITY_MEMBER 테이블 INSERT)
	 * @param empId
	 * @return
	 */
	public int insertRoomMember(Map<String, Object> roomMemberData);

	/**
	 * 커뮤니티방 만들기(JOIN_STATUS 테이블 INSERT)
	 * @param roomJoinData
	 * @return
	 */
	public int insertStatusMember(Map<String, Object> roomJoinData);

	/**
	 * 게시판 추가
	 * @param communityNo
	 * @return
	 */
	public int insertBoard(Map<String, Object> communityData);

	/**
	 * 게시글 상세조회
	 * @param postNo
	 * @return
	 */
	public CommunityVO selectPostDetail(Long postNo);

	/**
	 * 좋아요 개수 확인
	 * @param postNo
	 * @return
	 */
	public int selectLike(Long postNo);

	/**
	 * 좋아요 등록 (LIKES 테이블 INSERT)
	 * @param postNo
	 * @return
	 */
	public int likeInsert(Map<String, Object> likesTBData);

	/**
	 * 좋아요 등록 및 취소 (POST 테이블 UPDATE)
	 * @param postNo
	 * @return
	 */
	public int likeUpdate(Map<String, Object> postTBData);

	/**
	 * 좋아요 상태관리
	 * @param memberNo
	 * @param postNo
	 * @return
	 */
	public Boolean likeStatus(Map<String, Object> likeData);

	/**
	 * 좋아요 취소 (LIKES 테이블 삭제)
	 * @param likeTBData
	 * @return
	 */
	public int likeDelete(Map<String, Object> likeTBData);

	/**
	 * 방 관리를 위한 조회
	 * @param roomData
	 * @return
	 */
	public List<CommunityVO> roomManagement(Map<String, Object> roomData);

	/**
	 * 방 정보 수정
	 * @param roomData
	 * @return
	 */
	public int roomDataUpdate(Map<String, Object> roomData);

	/**
	 * 가입 승인 또는 거절
	 * @param joinStatusData
	 * @return
	 */
	public int roomJoinStatus(Map<String, Object> joinStatusData);

	/**
	 * 요청자의 requestNo 조회
	 * @param empId
	 * @return
	 */
	public Long memberRequestNo(Map<String, Object> requestData);

	/**
	 * 요청자의 memberNo 조회
	 * @param memberData
	 * @return
	 */
	public Long memberNo(Map<String, Object> memberData);

	/**
	 * 강퇴 처리 (COMMUNITY_MEMBER 테이블 삭제)
	 * @param deleteStatusData
	 * @return
	 */
	public int roomMemberDelete(Map<String, Object> deleteMemberData);

	/**
	 * 강퇴 처리 (JOIN_STATUS 테이블 삭제)
	 * @param deleteStatusData
	 * @return
	 */
	public int roomStatusDelete(Map<String, Object> deleteStatusData);

	/**
	 * 폐쇄처리 (LIKES 테이블 DELETE)
	 * @param updateCommunityData
	 * @return
	 */
	public int likesRoomShutdown(Long communityNo);

	/**
	 * 폐쇄처리 (COMMENT 테이블 DELETE)
	 * @param updateCommunityData
	 * @return
	 */
	public int commentRoomShutdown(Long communityNo);

	/**
	 * 폐쇄처리 (POST 테이블 DELETE)
	 * @param updateCommunityData
	 * @return
	 */
	public int postRoomShutdown(Long communityNo);

	/**
	 * 폐쇄처리 (BOARD 테이블 DELETE)
	 * @param updateCommunityData
	 * @return
	 */
	public int boardRoomShutdown(Long communityNo);

	/**
	 * 폐쇄처리 (JOIN_STATUS 테이블 DELETE)
	 * @param updateCommunityData
	 * @return
	 */
	public int joinStatusRoomShutdown(Long communityNo);

	/**
	 * 폐쇄처리 (COMMUNITY_MEMBER 테이블 DELETE)
	 * @param updateCommunityData
	 * @return
	 */
	public int communityMemberRoomShutdown(Long communityNo);


	/**
	 * 폐쇄처리 (COMMUNITY 테이블 UPDATE)
	 * @param updateCommunityData
	 * @return
	 */
	public int communityRoomShutdown(@Param("communityNo")Long communityNo, @Param("closureReason")String closureReason);

	/**
	 * 커뮤니티 가입 요청
	 * @param empId
	 * @return
	 */
	public int joinStatusInsert(Map<String, Object> memberData);

	/**
	 * 게시글 작성
	 * @param postData
	 * @return
	 */
	public int insertPost(Map<String, Object> postData);

	/**
	 * 댓글 작성
	 * @param commentData
	 * @return
	 */
	public int insertComment(Map<String, Object> commentData);

	/**
	 * 댓글 조회
	 * @param replyNo
	 * @return
	 */
	public List<CommentVO> selectComment(Long postNo);

	/**
	 * 방장 권한 이전 (COMMUNITY 테이블)
	 * @param roomMasterData
	 * @return
	 */
	public int updateCommunityRoomMaster(Map<String, Object> communityRoomMasterData);

	/**
	 * 방장 권한 이전 (COMMUNITY_MEMBER 테이블)
	 * 방장 - > member
	 * @param communityMemberRoomMastrData
	 * @return
	 */
	public int updateCommunityMemberRoomMaster(Map<String, Object> communityMemberRoomMasterData);

	/**
	 * 방장 권한 이전 (COMMUNITY_MEMBER 테이블)
	 * member -> 방장
	 * @param communityMemberRoomMastrData
	 * @return
	 */
	public int updateCommunityMemberRoomMember(Map<String, Object> communityMemberRoomMemberData);

	/**
	 * 탈퇴
	 * @param memberNo
	 * @return
	 */
	public int deleteMember(Long memberNo);

	/**
	 * 초대 (COMMUNITY_MEMBER INSERT)
	 * @param communityMemberData
	 * @return
	 */
	public int insertInvitationMember(Map<String, Object> communityMemberData);
	/**
	 * 초대 (JOIN_STATUS INSERT)
	 * @param communityMemberData
	 * @return
	 */
	public int insertInvitationJoin(Map<String, Object> joinStatusData);

}
