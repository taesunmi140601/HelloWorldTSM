package kr.or.ddit.works.reservation.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.reservation.service.ReservationService;
import kr.or.ddit.works.reservation.vo.MeetingRoomVO;
import kr.or.ddit.works.reservation.vo.ReservationVO;

/**
 * 회의실 컨트롤러
 * @author JYS
 * @since 2025. 3. 15.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 15.     	JYS	          최초 생성
 *  2025. 3. 17.  		KKM  		  개발 중
 *  2025. 4.  2.     	SJH           수정 중
 *
 * </pre>
 */
/**
 * 회의실 예약 관련 컨트롤러
 * - 예약 목록 조회, 등록, 수정, 삭제 처리
 * - 날짜 기반 시간표 렌더링 및 모달 연동
 */
@Controller
@RequestMapping("/{companyNo}/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 예약 현황 조회 (메인 페이지)			
     */
    @GetMapping
    public String getReservations(@PathVariable("companyNo") String companyNo,
                                  @RequestParam(value = "date", defaultValue = "today") String date,
                                  Principal principal,
                                  Model model) {

        String empId = principal != null ? principal.getName() : null;
        model.addAttribute("empId", empId); // 본인 예약 식별용

        // 날짜 파싱
        LocalDate targetDate = "today".equalsIgnoreCase(date) ? LocalDate.now() : LocalDate.parse(date);
        String currentDate = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("currentDate", currentDate);

        // 회의실 및 예약 정보
        List<MeetingRoomVO> allRooms = reservationService.selectListAllRoom();
        List<ReservationVO> reservations = reservationService.selectReservationsByDate(targetDate);

     // 시간표 매핑 (예약된 시간에 예약 객체 매핑)
        Map<Long, Map<String, ReservationVO>> schedule = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm"); // 9:00, 10:00 등으로 포맷

        for (MeetingRoomVO room : allRooms) {
            schedule.putIfAbsent(room.getRoomNo(), new LinkedHashMap<>());
        }

        for (ReservationVO r : reservations) {
            long roomNo = r.getRoomNo();
            LocalTime start = r.getReservationStartTime().toLocalTime();
            LocalTime end = r.getReservationEndTime().toLocalTime();

            for (LocalTime t = start; t.isBefore(end); t = t.plusHours(1)) {
                String timeKey = t.format(formatter); // "9:00", "10:00"
                schedule.get(roomNo).put(timeKey, r);
            }
        }

        model.addAttribute("schedule", schedule);
        model.addAttribute("allRooms", allRooms);
        model.addAttribute("companyNo", companyNo);

        return "gw:reservation/reservationList";
    }

    /**
     * 예약 상세 조회 (예약 상세 모달 + 수정 가능)
     */
    @GetMapping("/{reservationNo}")
    public String selectReservationDetail(@PathVariable("reservationNo") String reservationNo,
                                          @PathVariable("companyNo") String companyNo,
                                          Model model,
                                          Authentication authentication) {

        // 1. 예약 정보 조회
        ReservationVO reservation = reservationService.selectReservationDetail(reservationNo);
        if (reservation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다.");
        }

        // 2. 로그인 사용자와 예약자 비교
        String loginUserId = ((RealUserWrapper) authentication.getPrincipal()).getRealUser().getUserId();
        boolean isOwner = loginUserId.equals(reservation.getEmpId());

        // 3. LocalDateTime → java.util.Date 변환 (JSTL fmt:formatDate 호환)
        LocalDateTime start = reservation.getReservationStartTime();
        LocalDateTime end = reservation.getReservationEndTime();

        ZoneId zone = ZoneId.systemDefault(); // 시스템 기본 시간대
        Date reservationStartDate = Date.from(start.atZone(zone).toInstant());
        Date reservationEndDate = Date.from(end.atZone(zone).toInstant());

        // 4. 포맷된 문자열 값 (드롭다운용)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String reservationDateFormatted = start.toLocalDate().format(dateFormatter);
        String reservationStartTimeFormatted = start.toLocalTime().format(timeFormatter);
        String reservationEndTimeFormatted = end.toLocalTime().format(timeFormatter);

        // 5. 모델 등록
        model.addAttribute("reservation", reservation);
        model.addAttribute("reservationStartDate", reservationStartDate);  
        model.addAttribute("reservationEndDate", reservationEndDate);      
        model.addAttribute("reservationDateFormatted", reservationDateFormatted);
        model.addAttribute("reservationStartTimeFormatted", start.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("reservationEndTimeFormatted", end.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("companyNo", companyNo);

        return "groupware/reservation/reservationDetail";
    }

    /**
     * 예약 등록 처리
     */
    @PostMapping("/new")
    public String insertReservation(@ModelAttribute ReservationVO reservation,
                                    @PathVariable("companyNo") String companyNo,
                                    Authentication authentication
                                    , Model model) {
    	String empId = authentication.getName();
        reservation.setEmpId(empId);
        try {
            if (reservationService.insertReservation(reservation)) {
                // 선택한 날짜로 리다이렉트
                String selectedDate = reservation.getReservationDate().toString();
                return "redirect:/" + companyNo + "/reservation?date=" + selectedDate + "&success=true";
            } else {
                model.addAttribute("error", "예약 등록 실패");
            }
        } catch (Exception e) {
            model.addAttribute("error", "예외 발생: " + e.getMessage());
        }

        return "gw:reservation/reservationList";
    }
    
    /**
     * 예약 수정 처리
     */
    @PostMapping("/update")
    public String updateReservation(@RequestParam("reservationNo") String reservationNo,
                                    @RequestParam("roomNo") long roomNo,
                                    @RequestParam("reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDate,
                                    @RequestParam("reservationStartTime") @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                    @RequestParam("reservationEndTime") @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                    @RequestParam("reservationContent") String reservationContent,
                                    @PathVariable("companyNo") String companyNo,
                                    Authentication authentication,
                                    RedirectAttributes ra) {

    	// ✅ 시작 시간이 종료 시간보다 늦은 경우 → 오류 처리
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            ra.addFlashAttribute("message", "⚠ 시작 시간은 종료 시간보다 빨라야 합니다.");
            return "redirect:/" + companyNo + "/reservation";
        }
        
    	String empId = authentication.getName();
        
        // 날짜 + 시간 결합
        LocalDateTime reservationStart = LocalDateTime.of(reservationDate, startTime);
        LocalDateTime reservationEnd = LocalDateTime.of(reservationDate, endTime);


        ReservationVO reservation = new ReservationVO();
        reservation.setReservationNo(reservationNo);
        reservation.setRoomNo(roomNo);
        reservation.setReservationDate(reservationDate);
        reservation.setReservationStartTime(reservationStart);
        reservation.setReservationEndTime(reservationEnd);
        reservation.setReservationContent(reservationContent);
        reservation.setEmpId(empId);

        boolean updated = reservationService.updateReservation(reservation);

        if (updated) {
            ra.addFlashAttribute("message", "예약이 성공적으로 수정되었습니다.");
        } else {
            ra.addFlashAttribute("message", "예약 수정에 실패했습니다.");
        }

        return "redirect:/" + companyNo + "/reservation";
    }

    /**
     * 예약 삭제 처리 (본인만 가능)
     */
    @PostMapping("/{reservationNo}/delete")
    public String deleteReservationViaPost(@PathVariable("reservationNo") String reservationNo,
                                           @PathVariable("companyNo") String companyNo) {
        reservationService.deleteReservation(reservationNo);
        return "redirect:/" + companyNo + "/reservation";
    }

}
