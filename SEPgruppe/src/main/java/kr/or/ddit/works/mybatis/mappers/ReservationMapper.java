package kr.or.ddit.works.mybatis.mappers;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.reservation.vo.MeetingRoomVO;
import kr.or.ddit.works.reservation.vo.ReservationVO;

/**
 * 회의실 예약 매퍼
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
 * </pre>
 */
@Mapper
public interface ReservationMapper {
	 // 전체 예약 목록 조회
    public List<ReservationVO> selectListAllReservation();

    // 예약 상세 조회
    public ReservationVO selectReservationDetail(String reservationNo);

    // 예약 추가
    public int insertReservation(ReservationVO reservation);

    // 예약 수정
    public int updateReservation(ReservationVO reservation);

    // 예약 삭제
    public int deleteReservation(String reservationNo);

    // 특정 날짜의 예약 조회
    public List<ReservationVO> selectReservationsByDate(LocalDate reservationDate);

    // 로그인한 사용자의 예약 목록 조회
    public List<ReservationVO> getMyReservationsByEmpId(String empId);

    // 특정 날짜와 회의실 번호에 대한 예약 현황 조회
    public List<ReservationVO> getReservationsByDateAndRoom(@Param("date") LocalDate date, @Param("roomNo") long roomNo);
    
 // 특정 날짜의 예약 조회
    public List<ReservationVO> findReservationsByDate(@Param("date") LocalDate date);
    
 // 모든 회의실 목록 조회
    public List<MeetingRoomVO> selectListAllRoom();
    
    // reservationNo 마지막 번호 읽어오기
    public String getMaxReservationNo();
}
