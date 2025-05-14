package kr.or.ddit.works.schedule.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.mybatis.mappers.OrganizationMapper;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import kr.or.ddit.works.schedule.service.ScheduleService;
import kr.or.ddit.works.schedule.vo.ScheduleTypeVO;
import kr.or.ddit.works.schedule.vo.ScheduleVO;


@Controller
@RequestMapping("/{companyNo}/schedule")
public class ScheduleController {

	public static final String MODELNAME = "schedule";

	@Autowired
	private ScheduleService scheduleService;


	// 일정 목록 조회 화면 요청 처리
	@GetMapping("")
	public String readScheduleList(
		@PathVariable("companyNo") String companyNo
		, Authentication authentication
		, Model model
		, HttpSession session
	) {
		// 전체 일정 목록 및 일정 유형 목록 조회
	    List<ScheduleVO> scheduleList = scheduleService.readScheduleList();
	    List<ScheduleTypeVO> scheduleType = scheduleService.readScheduleType();

	    // 현재 로그인한 사용자 정보 저장
	    RealUserWrapper userWrapper = (RealUserWrapper) authentication.getPrincipal();
	    session.setAttribute("realUser", userWrapper.getRealUser());

	    // 일정 유형에 따라 필터링된 일정 목록을 담을 리스트
	    List<ScheduleVO> filteredScheduleList = new ArrayList<>();

	    // 일정 리스트를 순회하며 조건에 따라 필터링
	    for (ScheduleVO schedule : scheduleList) {
	    	// 해당 일정의 유형명을 조회
	        String scheduleTypeNm = scheduleType.stream()
	                .filter(type -> schedule.getScheduleTypeNo().equals(type.getScheduleTypeNo()))
	                .findFirst()
	                .map(ScheduleTypeVO::getScheduleTypeNm)
	                .orElse("");

	        // 개인 일정은 본인 것만 필터링
	        if ("개인".equals(scheduleTypeNm)) {
	            if (authentication.getName().equals(schedule.getEmpId())) {
	                filteredScheduleList.add(schedule);
	            }

	        // 사내 또는 프로젝트 일정은 모두 보여줌
	        } else if ("사내".equals(scheduleTypeNm) || "프로젝트".equals(scheduleTypeNm)) {
	            filteredScheduleList.add(schedule);
	        }
	    }

	    model.addAttribute("scheduleList", filteredScheduleList);
	    model.addAttribute("scheduleType", scheduleType);
	    model.addAttribute("companyNo", companyNo);
	    return "gw:schedule/scheduleList";
	}

	// 일정 상세조회 화면 요청 처리
	@GetMapping("/scheduleDetail.do")
	public String doGet(
		@RequestParam("what") String schdlNo
		, Model model
	) {
		// 문자열로 전달된 일정 번호를 정수로 변환
		int scheduleId = Integer.parseInt(schdlNo);

		// 일정 번호를 기반으로 상세 일정 정보를 조회
		ScheduleVO schedule = scheduleService.readSchedule(scheduleId);

		model.addAttribute("schedule", schedule);
		return "groupware/schedule/scheduleDetail";

	}

	// 일정 등록 폼 화면 요청 처리
    @GetMapping("/form")
    public String scheduleForm(
    	@PathVariable("companyNo") String companyNo
    	, Model model
    ) {
    	model.addAttribute("companyNo", companyNo);
        return "groupware/schedule/scheduleForm";
    }


    // 일정 등록 요청 처리
    // JSON 형태로 ScheduleVO 객체를 응답으로 반환
	@ResponseBody
	@PostMapping("/createSchedule.ajax")
	public ScheduleVO createSchedule(
		@ModelAttribute(MODELNAME) ScheduleVO pvo
		, ScheduleVO rvo
		, HttpSession session
		, Model model
		, Authentication authentication
	){
		// 현재 로그인한 사용자의 아이디를 일정에 설정
		pvo.setEmpId(authentication.getName());

		// 일정 등록 서비스 호출 및 결과 처리
        int cnt = scheduleService.createSchedule(pvo);

        // 등록 결과를 응답 객체에 설정
        rvo.setCnt(cnt);

		return rvo;
	}


	// 일정 수정 폼 화면 요청 처리
    @GetMapping("/formEdit")
    public String scheduleFormEdit(
		@RequestParam("schdlNo") String schdlNo
		, Model model
    ) {

    	// 문자열로 받은 일정 번호를 정수로 변환
		int scheduleId = Integer.parseInt(schdlNo);

		// 일정 번호를 이용해 일정 상세 정보 조회
		ScheduleVO schedule = scheduleService.readSchedule(scheduleId);

		model.addAttribute("schedule", schedule);

        return "groupware/schedule/scheduleEdit";
    }


    // 일정 수정 요청 처리
	@PutMapping("/updateSchedule.ajax")
	@ResponseBody
	public ScheduleVO updateSchedule(
		@RequestBody ScheduleVO pvo
		, Authentication authentication
	) {
		// 일정 번호가 0이면 잘못된 요청으로 간주하고 예외 발생
	    if (pvo.getSchdlNo() == 0) {
	        throw new IllegalArgumentException("일정 번호가 올바르지 않습니다.");
	    }

	    // 현재 로그인한 사용자의 ID를 일정 정보에 설정
	    pvo.setEmpId(authentication.getName());

	    // 일정 정보 수정 처리
	    int cnt = scheduleService.updateSchedule(pvo);

	    // 응답으로 반환할 ScheduleVO 객체 생성 및 처리 결과 설정
	    ScheduleVO rvo = new ScheduleVO();
	    rvo.setCnt(cnt);

	    return rvo;
	}

	// 일정 삭제 요청 처리
	@ResponseBody
	@DeleteMapping("/deleteSchedule.ajax/{schdlNo}")
	public ScheduleVO deleteSchedule(@PathVariable("schdlNo") int schdlNo) {

		// 일정 번호에 해당하는 일정 삭제 처리
	    int cnt = scheduleService.deleteSchedule(schdlNo);

	    // 삭제 결과를 응답 객체에 담아 반환
	    ScheduleVO rvo = new ScheduleVO();
	    rvo.setCnt(cnt);

	    return rvo;
	}


}
