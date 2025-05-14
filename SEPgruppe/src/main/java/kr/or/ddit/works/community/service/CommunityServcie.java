package kr.or.ddit.works.community.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.community.vo.CommentVO;
import kr.or.ddit.works.community.vo.CommunityVO;

public interface CommunityServcie {

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
	public CommunityVO selectCommunityDetail(long communityNo);


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
	 * 커뮤니티방 만들기
	 * @param empId
	 * @return
	 */
	public void insertCommunity(Map<String, Object> roomData, Map<String, Object> roomMemberData, Map<String, Object> roomJoinData);

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
	 * 좋아요 등록
	 * @param likesTBData
	 * @param postTBData
	 * @return
	 */
	public void updateLike(Long postNo, Map<String, Object> likesTBData, Map<String, Object> postTBData);

	/**
	 * 좋아요 상태관리
	 * @param memberNo
	 * @param postNo
	 * @return
	 */
	public Boolean likeStatus(Map<String, Object>likeData);

	/**
	 * 좋아요 취소 (LIKES 테이블 삭제)
	 * @param likeTBData
	 * @return
	 */
	public void likeDelete(Long postNo, Map<String, Object> likeTBData, Map<String, Object> postTBData);

	/**
	 * 좋아요 개수 확인
	 * @param postNo
	 * @return
	 */
	public int selectLike(Long postNo);

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
	public boolean roomDataUpdate(Map<String, Object> roomData);

	/**
	 * 가입 승인 또는 거절
	 */
	public void roomJoinStatus(Map<String, Object> memberData, Map<String, Object> joinStatusData);

	/**
	 * 회원 강퇴처리
	 * @param deleteStatusData
	 * @param deleteMemberData
	 */
	public void deleteRoomMember(Map<String, Object> deleteMemberData, Map<String, Object> deleteStatusData);

	/**
	 * 방 폐쇄처리
	 */
	public void roomShutdown(Long communityNo, String closureReason);

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
	 * 방권한 이전
	 * @param data
	 */
	public void transferRoomMaster(Map<String, Object> data);

	/**
	 * 탈퇴
	 * @param memberNo
	 * @return
	 */
	public int deleteMember(Long memberNo);

	/**
	 * 초대
	 * @param communityMemberData
	 * @param joinStatusData
	 */
	public void insertInvitationMember(Long communityNo, String empId);


}
