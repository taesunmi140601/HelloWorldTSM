package kr.or.ddit.works.reservation.service;

import java.util.List;

import kr.or.ddit.works.reservation.vo.MeetingRoomVO;

/**
 * 예약 RESERVATION SERVICE
 * 
 * @author KKM
 * @since 2025. 3. 28.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 28.     	KKM	          최초 생성
 * </pre>
 */
public interface MeetingroomService {
	
	// 모든 회의실 목록 조회
		public List<MeetingRoomVO> selectListAllRoom();

	    // 회의실 상세 조회
		public MeetingRoomVO selectRoomDetail(long roomNo);

	    // 회의실 등록
		public boolean insertRoom(MeetingRoomVO meetingroom);

	    // 회의실 수정
		public boolean updateRoom(MeetingRoomVO meetingroom);

	    // 회의실 삭제
		public boolean deleteRoom(long roomNo);
}
