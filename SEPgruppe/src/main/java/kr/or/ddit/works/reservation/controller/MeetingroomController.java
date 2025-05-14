package kr.or.ddit.works.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.works.reservation.service.MeetingroomService;
import kr.or.ddit.works.reservation.vo.MeetingRoomVO;

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
 *
 * </pre>
 */
@Controller
@RequestMapping("/{companyNo}/meetingroom")
public class MeetingroomController {
    
    @Autowired
    private MeetingroomService meetingroomService;

    /** 관리자 회의실 목록 조회 */
    @GetMapping("")
    public String selectListAllRoom(@PathVariable("companyNo") String companyNo,
    		Model model) {
    	
        List<MeetingRoomVO> meetingRooms = meetingroomService.selectListAllRoom();
        model.addAttribute("meetingRooms", meetingRooms);
        model.addAttribute("companyNo", companyNo);
        
        return "gw:meetingroom/meetingroomList"; // JSP 경로 수정
    }

    /** 관리자 회의실 상세 조회 */
    @GetMapping("/{roomNo}")
    public String selectRoomDetail(@PathVariable("roomNo") long roomNo, 
                                    @PathVariable("companyNo") String companyNo, 
                                    Model model) {
        // 회의실 정보 조회
        MeetingRoomVO roomDetail = meetingroomService.selectRoomDetail(roomNo);
        model.addAttribute("roomDetail", roomDetail);
        model.addAttribute("companyNo", companyNo);

        // AJAX 요청에 필요한 부분만 반환하도록 수정
        return "groupware/meetingroom/meetingroomDetail";
    }

    /** 관리자 회의실 등록 */
    @PostMapping("")
    public String insertRoom(@PathVariable("companyNo") String companyNo,
                             @RequestParam String roomNm,
                             @RequestParam String roomLocation,
                             @RequestParam String roomAvailability,
                             @RequestParam long roomCapacity,
                             Model model) {
        MeetingRoomVO meetingRoom = new MeetingRoomVO();
        meetingRoom.setRoomNm(roomNm);
        meetingRoom.setRoomLocation(roomLocation);
        meetingRoom.setRoomAvailability(roomAvailability);
        meetingRoom.setRoomCapacity(roomCapacity);
        
        meetingroomService.insertRoom(meetingRoom);
        
        List<MeetingRoomVO> meetingRooms = meetingroomService.selectListAllRoom();
        model.addAttribute("meetingRooms", meetingRooms);
        model.addAttribute("companyNo", companyNo);
        
        return "gw:meetingroom/meetingroomList"; // JSP 경로 수정
    }

    /** 관리자 - 회의실 수정 폼 이동 */
    @GetMapping("/{roomNo}/edit")
    public String updateRoomFormUI(@PathVariable("roomNo") long roomNo, 
                                     @PathVariable("companyNo") String companyNo, 
                                     Model model) {
        MeetingRoomVO roomDetail = meetingroomService.selectRoomDetail(roomNo);
        model.addAttribute("roomDetail", roomDetail);
        model.addAttribute("companyNo", companyNo);
        return "gw:meetingroom/meetingroomEdit"; // JSP 경로 수정
    }

    /** 관리자 - 회의실 수정 */
    @PostMapping("/{roomNo}/edit")
    public String updateRoom(@PathVariable("roomNo") long roomNo,
                             @PathVariable("companyNo") String companyNo,
                             @RequestParam String roomNm,
                             @RequestParam String roomLocation,
                             @RequestParam String roomAvailability,
                             @RequestParam long roomCapacity) {
        MeetingRoomVO meetingRoom = new MeetingRoomVO();
        meetingRoom.setRoomNo(roomNo);
        meetingRoom.setRoomNm(roomNm);
        meetingRoom.setRoomLocation(roomLocation);
        meetingRoom.setRoomAvailability(roomAvailability);
        meetingRoom.setRoomCapacity(roomCapacity);
        
        meetingroomService.updateRoom(meetingRoom);
        return "redirect:/" + companyNo + "/meetingroom/" + roomNo; // 수정 후 해당 회의실 상세 페이지로 리다이렉트
    }

    /** 관리자 - 회의실 삭제 */
    @DeleteMapping("/{roomNo}/delete")
    public String deleteRoom(@PathVariable("roomNo") long roomNo,
                             @PathVariable("companyNo") String companyNo) {
        meetingroomService.deleteRoom(roomNo);
        return "redirect:/" + companyNo + "/meetingroom"; // 삭제 후 회의실 목록 페이지로 리다이렉트
    }

    // POST 요청을 DELETE로 변환
    @PostMapping("/{roomNo}/delete")
    public String deleteRoomViaPost(@PathVariable("roomNo") long roomNo, 
                                     @PathVariable("companyNo") String companyNo) {
        return deleteRoom(roomNo, companyNo); // 실제 삭제 로직 호출
    }
}
