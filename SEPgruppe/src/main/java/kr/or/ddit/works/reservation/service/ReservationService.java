package kr.or.ddit.works.reservation.service;

import java.time.LocalDate;
import java.util.List;

import kr.or.ddit.works.reservation.vo.MeetingRoomVO;
import kr.or.ddit.works.reservation.vo.ReservationVO;

/**
 * 예약 RESERVATION SERVICE
 * 
 * @author KKM
 * @since 2025. 3. 17.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 17.     	KKM	          최초 생성
 * </pre>
 */
/**
 * 예약 관련 서비스 인터페이스
 * 예약 등록, 수정, 삭제, 조회 기능을 선언
 */
public interface ReservationService {

    /**
     * 예약 등록
     * @param reservation 등록할 예약 정보
     * @return 성공 여부
     */
    boolean insertReservation(ReservationVO reservation);

    /**
     * 예약 수정
     * @param reservation 수정할 예약 정보
     * @return 성공 여부
     */
    boolean updateReservation(ReservationVO reservation);

    /**
     * 예약 삭제
     * @param reservationNo 삭제할 예약 번호
     * @return 성공 여부
     */
    boolean deleteReservation(String reservationNo);

    /**
     * 특정 날짜의 예약 목록 조회
     * @param date 조회할 날짜
     * @return 예약 목록
     */
    List<ReservationVO> selectReservationsByDate(LocalDate date);

    /**
     * 특정 사원의 예약 내역 조회
     * @param empId 사원 ID
     * @return 예약 목록
     */
    List<ReservationVO> getMyReservationsByEmpId(String empId);

    /**
     * 예약 상세 조회
     * @param reservationNo 예약 번호
     * @return 예약 정보
     */
    ReservationVO selectReservationDetail(String reservationNo);

	List<MeetingRoomVO> selectListAllRoom();
}
