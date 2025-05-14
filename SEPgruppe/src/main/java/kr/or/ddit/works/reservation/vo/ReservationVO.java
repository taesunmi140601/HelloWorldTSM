package kr.or.ddit.works.reservation.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 예약 RESERVATION VO
 * 
 * @author JYS
 * @since 2025. 3. 13.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 13.     	JYS	          최초 생성
 *  2025. 3. 14. 		KKM			  개발 시작
 * </pre>
 */

@Data
public class ReservationVO implements Serializable {
    private String reservationNo;
    private long roomNo; 
    private String roomName;
    private String empId;
    private String empNm;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationEndTime;
    
    private String reservationContent;
}
