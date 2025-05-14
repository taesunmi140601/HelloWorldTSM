package kr.or.ddit.works.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.ReservationMapper;
import kr.or.ddit.works.reservation.vo.MeetingRoomVO;
import kr.or.ddit.works.reservation.vo.ReservationVO;

/**
 * 예약 서비스 임플
 * 
 * @author KKM
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 * </pre>
 */
/**
 * ReservationService 인터페이스 구현체
 * 회의실 예약 관련 비즈니스 로직 처리
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    /**
     * 예약 등록
     */
    @Override
    public boolean insertReservation(ReservationVO reservation) {
        return reservationMapper.insertReservation(reservation) > 0;
    }

    /**
     * 예약 수정
     */
    @Override
    public boolean updateReservation(ReservationVO reservation) {
        return reservationMapper.updateReservation(reservation) > 0;
    }

    /**
     * 예약 삭제
     */
    @Override
    public boolean deleteReservation(String reservationNo) {
        return reservationMapper.deleteReservation(reservationNo) > 0;
    }

    /**
     * 특정 날짜의 예약 목록 조회
     */
    @Override
    public List<ReservationVO> selectReservationsByDate(LocalDate date) {
        return reservationMapper.selectReservationsByDate(date);
    }

    /**
     * 사원 ID 기준 본인의 예약 목록 조회
     */
    @Override
    public List<ReservationVO> getMyReservationsByEmpId(String empId) {
        return reservationMapper.getMyReservationsByEmpId(empId);
    }

    /**
     * 예약 상세 조회
     */
    @Override
    public ReservationVO selectReservationDetail(String reservationNo) {
        return reservationMapper.selectReservationDetail(reservationNo);
    }

    /**
     * 전체 회의실 목록 조회
     */
    public List<MeetingRoomVO> selectListAllRoom() {
        return reservationMapper.selectListAllRoom();
    }
}
