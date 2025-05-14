package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.chat.vo.ChatRoomUserVO;
import kr.or.ddit.works.chat.vo.ChatRoomVO;
import kr.or.ddit.works.chat.vo.MessageVO;

/**
 * 채팅 매퍼
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
public interface ChatMapper {

	/**
	 * 본인이 속한 모든 채팅방 목록 조회
	 * - 최근 메시지 1건 포함
	 * @param empId 현재 로그인한 사용자의 사번(ID)
	 * @return 참여 중인 채팅방 목록 (각 채팅방에 대한 기본 정보 및 최근 메시지 포함)
	 */
	public List<ChatRoomUserVO> selectListAllChatRoom(@Param("empId") String empId);

	/**
	 * 특정 채팅방 상세 정보 조회
	 * @param roomId 채팅방 ID
	 * @return 채팅방 정보
	 */
	public ChatRoomVO selectChatRoomDetail(long roomId);

	/**
	 * 채팅방 참여자 추가
	 * - CHAT_ROOM_USER 테이블에 사용자 추가
	 * @param roomUser 참여자 정보
	 * @return insert 성공 여부 (1 이상: 성공)
	 */
	public int insertChatRoomUser(ChatRoomUserVO roomUser);

	/**
	 * 채팅방 생성
	 * - CHAT_ROOM 테이블에 새로운 채팅방 추가
	 * @param chatRoomVO 생성할 채팅방 정보
	 * @return insert 성공 여부
	 */
	public int insertChatRoom(ChatRoomVO chatRoomVO);

	/**
	 * 메시지 저장
	 * - MESSAGE 테이블에 메시지 저장
	 * @param message 저장할 메시지 정보
	 * @return insert 성공 여부
	 */
	public int insertMessage(MessageVO message);

	/**
	 * 특정 채팅방의 모든 메시지 조회
	 * - 메시지 보낸 사람의 조직 정보도 포함됨
	 * @param roomId 채팅방 ID
	 * @return 메시지 목록
	 */
	public List<MessageVO> findAllMessageByRoomId(@Param("roomId") String roomId);

	/**
	 * 채팅방 삭제
	 * - CHAT_ROOM 테이블에서 해당 채팅방 삭제
	 * @param roomId 삭제할 채팅방 ID
	 * @return delete 성공 여부
	 */
	public int deleteChatRoom(@Param("roomId") String roomId);

	/**
	 * 채팅방의 모든 참여자 목록 조회
	 * @param roomId 채팅방 ID
	 * @return 참여자 목록
	 */
	public List<ChatRoomUserVO> selectChatRoomUser(@Param("roomId") String roomId);

	/**
	 * 채팅방에서 특정 사용자 제외
	 * @param roomId 채팅방 ID
	 * @param empId 제외할 사원의 ID
	 * @return delete 성공 여부
	 */
	public int deleteChatRoomUser(@Param("roomId") String roomId, @Param("empId") String empId);

}
