package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.works.reservation.vo.MeetingRoomVO;

/**
 * 회의실 예약 매퍼
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
@Mapper
public interface MeetingRoomMapper {
	// 고객사 관리자 모든 회의실 목록 조회
    public List<MeetingRoomVO> selectListAllRoom();

    // 고객사 관리자 회의실 상세 조회
    public MeetingRoomVO selectRoomDetail(long roomNo);

    // 고객사 관리자 회의실 등록
    public int insertRoom(MeetingRoomVO meetingroom);

    // 고객사 관리자 회의실 수정
    public int updateRoom(MeetingRoomVO meetingroom);

    // 고객사 관리자 회의실 삭제
    public int deleteRoom(long roomNo);
}
