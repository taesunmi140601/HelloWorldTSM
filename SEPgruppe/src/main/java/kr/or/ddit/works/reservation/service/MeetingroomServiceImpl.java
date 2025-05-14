package kr.or.ddit.works.reservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.MeetingRoomMapper;
import kr.or.ddit.works.reservation.vo.MeetingRoomVO;

/**
 * 예약 서비스 임플
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
@Service
public class MeetingroomServiceImpl implements MeetingroomService {
	
	@Autowired
	private MeetingRoomMapper meetingroomMapper;
	
	 // 고객사 관리자 모든 회의실 목록 조회
    @Override
    public List<MeetingRoomVO> selectListAllRoom() {
        return meetingroomMapper.selectListAllRoom();
    }

    // 고객사 관리자 회의실 상세 조회
    @Override
    public MeetingRoomVO selectRoomDetail(long roomNo) {
        return meetingroomMapper.selectRoomDetail(roomNo);
    }

    // 고객사 관리자 회의실 등록
    @Override
    public boolean insertRoom(MeetingRoomVO meetingroom) {
        return meetingroomMapper.insertRoom(meetingroom) > 0;
    }

    // 고객사 관리자 회의실 수정
    @Override
    public boolean updateRoom(MeetingRoomVO meetingroom) {
        return meetingroomMapper.updateRoom(meetingroom) > 0;
    }

    // 고객사 관리자 회의실 삭제
    @Override
    public boolean deleteRoom(long roomNo) {
        return meetingroomMapper.deleteRoom(roomNo) > 0;
    }

}
