package kr.or.ddit.works.chat.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.chat.vo.ChatRoomUserVO;
import kr.or.ddit.works.chat.vo.ChatRoomVO;
import kr.or.ddit.works.chat.vo.MessageVO;

public interface ChatService {

	/**
	 * 본인이 속한 채팅방 목록 조회
	 * - 로그인한 사용자가 참여 중인 채팅방과 그에 대한 최근 메시지 정보를 포함하여 반환
	 * @param empId 로그인한 사원의 ID
	 * @return 참여 중인 채팅방 목록
	 */
	public List<ChatRoomUserVO> selectListAllChatRoom(String empId);


	/**
	 * 채팅방 상세 정보 조회
	 * - 채팅방 ID를 통해 해당 채팅방의 상세 정보를 조회
	 * @param chatRoomNo 채팅방 번호(ID)
	 * @return 채팅방 정보
	 */
	public ChatRoomVO selectChatRoomDetail(long chatRoomNo);

	/**
	 * 채팅방 생성
	 * - 새로운 채팅방을 생성하고 성공 여부 반환
	 * @param chatRoomVO 생성할 채팅방 정보
	 * @return 생성 성공 여부
	 */
	public boolean insertChatRoom(ChatRoomVO chatRoomVO);

	/**
	 * 메시지 저장
	 * - 채팅방에 전송된 메시지를 저장하고 해당 메시지 객체 반환
	 * @param message 저장할 메시지 정보
	 * @return 저장된 메시지 객체
	 */
	public MessageVO insertMessage(MessageVO message);

	/**
	 * 채팅방 사용자 추가
	 * - 채팅방에 사용자를 참여자로 등록
	 * @param roomUser 추가할 사용자 정보
	 * @return 추가 성공 여부
	 */
	public boolean insertChatRoomUser(ChatRoomUserVO roomUser);

	/**
	 * 특정 채팅방의 모든 메시지 조회
	 * - 메시지 작성자에 대한 조직 정보 포함
	 * @param roomId 채팅방 ID
	 * @return 메시지 목록
	 */
	public List<MessageVO> findAllMessageByRoomId(String roomId);

	/**
	 * 채팅방 삭제
	 * - 채팅방 자체를 삭제함
	 * @param roomId 삭제할 채팅방 ID
	 * @return 삭제 성공 여부
	 */
	public boolean deleteChatRoom( String roomId);

	/**
	 * 특정 채팅방에 참여 중인 사용자 목록 조회
	 * @param roomId 채팅방 ID
	 * @return 참여자 목록
	 */
	public List<ChatRoomUserVO> selectChatRoomUser(String roomId);

	/**
	 * 채팅방에서 특정 사용자 제거
	 * - 강퇴 또는 자발적 나가기 등의 기능에서 사용
	 * @param roomId 채팅방 ID
	 * @param empId 제거할 사용자 ID
	 * @return 제거 성공 여부
	 */
	public boolean deleteChatRoomUser(String roomId, String empId);

}
