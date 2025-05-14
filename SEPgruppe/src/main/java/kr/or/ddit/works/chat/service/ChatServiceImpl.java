package kr.or.ddit.works.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.chat.vo.ChatRoomUserVO;
import kr.or.ddit.works.chat.vo.ChatRoomVO;
import kr.or.ddit.works.chat.vo.MessageVO;
import kr.or.ddit.works.mybatis.mappers.ChatMapper;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatMapper chatMapper;

	/**
	 * 본인이 참여 중인 모든 채팅방 목록 조회
	 * - 최근 메시지 정보 포함
	 * @param empId 로그인한 사원의 ID
	 * @return 채팅방 + 참여자 + 최근 메시지 정보를 포함한 리스트
	 */
	@Override
	public List<ChatRoomUserVO> selectListAllChatRoom(String empId) {
		return chatMapper.selectListAllChatRoom(empId);
	}

	/**
	 * 채팅방 상세 정보 조회
	 * - 채팅방 ID를 기반으로 방 정보 + 생성자 정보 등을 가져옴
	 * @param chatRoomNo 채팅방 번호
	 * @return 채팅방 VO
	 */
	@Override
	public ChatRoomVO selectChatRoomDetail(long chatRoomNo) {
		return chatMapper.selectChatRoomDetail(chatRoomNo);
	}

	/**
	 * 채팅방 생성
	 * - 이미 동일한 ID로 존재할 경우 예외 발생 -> 중복으로 간주하고 true 반환
	 * @param chatRoomVO 생성할 채팅방 정보
	 * @return 성공 여부 (true: 생성 성공 또는 이미 존재, false: 실패)
	 */
	@Override
	public boolean insertChatRoom(ChatRoomVO chatRoomVO) {

		try {
	        return chatMapper.insertChatRoom(chatRoomVO) > 0;
	    } catch (DataIntegrityViolationException e) {
	        return true;
	    }
	}

	/**
	 * 채팅방에 사용자 추가
	 * - 이미 추가된 사용자일 경우 예외 발생 → 중복으로 간주하고 true 반환
	 * @param roomUser 추가할 사용자 정보
	 * @return 성공 여부 (true: 추가 성공 또는 이미 존재, false: 실패)
	 */
	@Override
	public boolean insertChatRoomUser(ChatRoomUserVO roomUser) {
		try {
	        return chatMapper.insertChatRoomUser(roomUser) > 0;
	    } catch (DataIntegrityViolationException e) {
	        return true;
	    }
	}

	/**
	 * 메시지 저장
	 * - 사용자가 채팅방에서 보낸 메시지를 DB에 저장
	 * @param message 저장할 메시지 객체
	 * @return 저장된 메시지 (ID는 미반환, 입력값 그대로 반환)
	 */
	@Override
	public MessageVO insertMessage(MessageVO message) {
		chatMapper.insertMessage(message);
		return message;
	}

	/**
	 * 채팅방의 모든 메시지 조회
	 * - 해당 채팅방에서의 메시지를 시간 순서로 정렬하여 반환
	 * @param roomId 채팅방 ID
	 * @return 메시지 목록
	 */
	@Override
	public List<MessageVO> findAllMessageByRoomId(String roomId) {
		return chatMapper.findAllMessageByRoomId(roomId);
	}

	/**
	 * 채팅방 삭제
	 * - 방 자체를 DB에서 제거
	 * @param roomId 삭제할 채팅방 ID
	 * @return 성공 여부
	 */
	@Override
	public boolean deleteChatRoom(String roomId) {
		return chatMapper.deleteChatRoom(roomId) > 0;
	}

	/**
	 * 채팅방에서 특정 사용자 제거
	 * - 참여자 목록에서 사용자 제거 (강제 퇴장, 자발적 나가기 등)
	 * @param roomId 채팅방 ID
	 * @param empId 사용자 ID
	 * @return 성공 여부
	 */
	@Override
	public boolean deleteChatRoomUser(String roomId, String empId) {
		return chatMapper.deleteChatRoomUser(roomId, empId) > 0;
	}

	/**
	 * 채팅방 참여자 목록 조회
	 * - 채팅방에 현재 참여 중인 모든 사용자 목록 반환
	 * @param roomId 채팅방 ID
	 * @return 참여자 리스트
	 */
	@Override
	public List<ChatRoomUserVO> selectChatRoomUser(String roomId) {
		return chatMapper.selectChatRoomUser(roomId);
	}

}
