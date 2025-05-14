package kr.or.ddit;


import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.security.CustomUserDetailService;
import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.alarm.service.OneSignalService;
import kr.or.ddit.works.approval.enums.AprvlDocStatus;
import kr.or.ddit.works.approval.service.ApprDocService;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.mail.exception.NeedOAuthRedirectException;
import kr.or.ddit.works.mail.service.MailService;
import kr.or.ddit.works.mybatis.mappers.LoginMapper;
import kr.or.ddit.works.mybatis.mappers.ProjectMapper;
import kr.or.ddit.works.notice.service.NoticeService;
import kr.or.ddit.works.notice.vo.NoticeVO;
import kr.or.ddit.works.organization.service.DclzStatusService;
import kr.or.ddit.works.organization.vo.DclzStatusVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.schedule.service.ScheduleService;
import kr.or.ddit.works.schedule.vo.ScheduleVO;
import kr.or.ddit.works.widget.service.WidgetPositionService;
import kr.or.ddit.works.widget.vo.WidgetPositionVO;




/**
 * 
 * @author 손현진
 * @since 2025. 3. 17.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 17.     	손현진	        최초 생성 누구냐
 *  2025. 3. 17.		손현진			고객사 관리자계정 그룹웨어 연동
 *
 * </pre>
 */
@Controller
public class IndexController{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Autowired
	private LoginMapper mapper;
	
	@Autowired
    private MailService mailService;
	
	@Autowired
	private DclzStatusService dclzService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ApprDocService apprDocService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private WidgetPositionService widgetService;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@GetMapping("/")
	public String index(HttpSession session, Model model, Authentication authentication){
		
		if (authentication == null || !authentication.isAuthenticated()) {
	        return "sep:indexSep";
	    }
		
		boolean isAdmin = authentication.getAuthorities().stream()
	            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		
		if(isAdmin) {
			CompaniesVO admin = mapper.autoLogin(authentication.getName());
			String loginId = admin.getContactId();
			autoLogin(loginId);
		}
		// 관리자모드 진입 -> 홈 버튼 눌렀을때 상단 메뉴바 동적 변경을 위한 isAdmin 세션 삭제
		session.removeAttribute("isAdmin");
		return "sep:indexSep";
	}
	
	
	@GetMapping("/{companyNo}/groupware")
	public String index2(
		@PathVariable String companyNo, Model model, Authentication authentication
		
	) throws Exception{
		model.addAttribute("companyNo", companyNo);
		Object principal = authentication.getPrincipal();

	    
		if (principal instanceof RealUserWrapper) { 
	        RealUserWrapper wrapper = (RealUserWrapper) principal;
	        Object realUser = wrapper.getRealUser();

	        if (realUser instanceof CompaniesVO) { 
	        	CompaniesVO user = (CompaniesVO) realUser;
	            String adminId = user.getAdminId();
	            if (adminId != null) {
	                autoLogin(adminId);
	                try {
	                	mailService.tryAutoAuth(adminId);
	                } catch (NeedOAuthRedirectException e) {
	                    return "redirect:/mail/oauth/start?empId=" + adminId;
	                }
	                publisher.publishEvent(new FetchEvent(authentication));
	            }
	        }
		}
		    
		String empId = authentication.getName();
		publisher.publishEvent(new FetchEvent(authentication));
		
		// 위젯 위치 조회
	    List<WidgetPositionVO> widgetPositions = widgetService.getWidgetPositions(empId);

	    // 디폴트 세팅
	    if (widgetPositions == null || widgetPositions.isEmpty()) {
	        widgetPositions = List.of(
	            new WidgetPositionVO(empId, "dclz", 0, "leftColumn"),
	            new WidgetPositionVO(empId, "schedule", 1, "leftColumn"),
	            new WidgetPositionVO(empId, "notice", 0, "rightColumn"),
	            new WidgetPositionVO(empId, "approval-waiting", 1, "rightColumn"),
	            new WidgetPositionVO(empId, "project-task", 2, "rightColumn")
	        );
	        widgetService.saveWidgetPositions(empId, widgetPositions);
	    }

	    // JSP에서 바로 쓸 수 있게 좌/우 컬럼 나눔
	    List<WidgetPositionVO> leftWidgets = widgetPositions.stream()
	            .filter(w -> "leftColumn".equals(w.getColumnName()))
	            .sorted((a, b) -> Integer.compare(a.getPositionNo(), b.getPositionNo()))
	            .collect(Collectors.toList());

	    List<WidgetPositionVO> rightWidgets = widgetPositions.stream()
	            .filter(w -> "rightColumn".equals(w.getColumnName()))
	            .sorted((a, b) -> Integer.compare(a.getPositionNo(), b.getPositionNo()))
	            .collect(Collectors.toList());

	    model.addAttribute("leftWidgets", leftWidgets);
	    model.addAttribute("rightWidgets", rightWidgets);
		return "gw:indexGW";
	}
	
	
	private void autoLogin(String loginId) {
	    UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

	    UsernamePasswordAuthenticationToken authToken =
	        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

	    SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	@Autowired
	private OneSignalService oneSignalService;

	@GetMapping("tests")
	public void testSendNotification() {
	    String message = "테스트 알림입니다!";
	    List<String> playerIds = Arrays.asList("b367f1e4-3a7a-45c4-82ca-42552c78fdba");

	    // 알림 전송
	    oneSignalService.sendNotification(message, playerIds);
	}
	
	// 근태 위젯
	@GetMapping("{companyNo}/dclz/main-panel")
	public String renderDclzPanel(@PathVariable String companyNo, Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = auth.getPrincipal();

	    String empId = null;
	    if (principal instanceof RealUserWrapper user && user.getRealUser() instanceof EmployeeVO emp) {
	        empId = emp.getEmpId();
	    }

	    if (empId == null) return "error/unauthorized";
	    
	    DclzStatusVO todayStatus = dclzService.selectTodayDclzStatus(empId);
	    String weeklyTotalHours = formatTime(
	    	    Optional.ofNullable(dclzService.getWeeklyTotalHours(empId))
	    	            .map(Integer::parseInt)
	    	            .orElse(0)
	    	);

	    model.addAttribute("todayDclzStatus", todayStatus);
	    model.addAttribute("weeklyTotalHours", weeklyTotalHours);
	    model.addAttribute("empId", empId);
	    model.addAttribute("companyNo", companyNo);
	    
	    return "groupware/dclz/mainDclzPanel"; // 이 JSP는 include용 partial 페이지
	}
	  private String formatTime(int totalMinutes) {
	        int h = totalMinutes / 60;
	        int m = totalMinutes % 60;
	        return h + "시간 " + m + "분";
	    }
	  
	  // 일정 위젯
	  @GetMapping("/{companyNo}/widget/schedule")
	  public String scheduleWidgetPartial(
	      @PathVariable String companyNo,
	      Model model,
	      Authentication authentication
	  ) throws JsonProcessingException {
		  model.addAttribute("companyNo", companyNo);

		    String empId = authentication.getName();
		    Object principal = authentication.getPrincipal();
		    if (principal instanceof RealUserWrapper wrapper &&
		        wrapper.getRealUser() instanceof EmployeeVO emp) {
		        empId = emp.getEmpId();
		    }

		    YearMonth thisMonth = YearMonth.now();
		    LocalDate startDate = thisMonth.atDay(1);
		    LocalDate endDate = thisMonth.atEndOfMonth();

		    List<ScheduleVO> schedules = scheduleService.selectThisMonthSchedules(empId, startDate, endDate);

		    List<Map<String, Object>> events = new ArrayList<>();
		    for (ScheduleVO s : schedules) {
		        Map<String, Object> event = new HashMap<>();
		        event.put("title", s.getSchdlNm());
		        event.put("start", new SimpleDateFormat("yyyy-MM-dd").format(s.getSchdlBgngYmd()));

		        // ✅ 커스텀 데이터는 extendedProps 내부로
		        Map<String, String> extendedProps = new HashMap<>();
		        extendedProps.put("schdlType", s.getScheduleTypeNm());
		        event.put("extendedProps", extendedProps);

		        events.add(event);
		    }
		    String scheduleJson = new ObjectMapper().writeValueAsString(events);
		    model.addAttribute("scheduleJson", scheduleJson);

		    return "groupware/schedule/mainScheduleWidget";
	  }
	  
	  // 전자결재 위젯
	  @GetMapping("/{companyNo}/widget/approval-waiting")
		public String approvalWaitingWidgetPartial(
		    @PathVariable String companyNo,
		    Model model,
		    Authentication authentication
		) {
		    model.addAttribute("companyNo", companyNo);

		    // 로그인 사원 정보
		    RealUserWrapper wrapper = (RealUserWrapper) authentication.getPrincipal();
		    EmployeeVO emp = (EmployeeVO) wrapper.getRealUser();
		    String empId = emp.getEmpId();

		    // 결재 대기 문서 조회
		    List<AprvlDocVO> waitingDocs = apprDocService.selectApprDocStatusList(
		        Map.of("empId", empId, "status", "W")
		    );
		    
		    // ✅ 상태 컬러 세팅
		    for (AprvlDocVO doc : waitingDocs) {
		        try {
		            AprvlDocStatus docStatus = AprvlDocStatus.fromCode(doc.getAprvlDocStatus()); // enum 매핑
		            doc.setStatusColor(docStatus.getColor());
		        } catch (IllegalArgumentException e) {
		            doc.setStatusColor("#000000"); // 기본값
		        }
		    }
		    
		    // 최대 5건까지만 보여주도록 잘라냄
		    if (waitingDocs.size() > 5) {
		        waitingDocs = waitingDocs.subList(0, 5);
		    }

		    model.addAttribute("waitingDocs", waitingDocs);

		    return "groupware/approval/mainApprWatingWidget";
		}

	  	  // 공지사항 위젯 조회 메서드
		  @GetMapping("/{companyNo}/widget/notice")
		  public String noticeWidgetPartial(
		      @PathVariable String companyNo,
		      Model model
		  ) {
		      List<NoticeVO> noticeList = noticeService.selectRecentNoticeList(companyNo);
		      model.addAttribute("noticeList", noticeList);
		      model.addAttribute("companyNo", companyNo);
		      return "groupware/notice/mainNoticeWidget";
		  }
		  
		  // 프로젝트 위젯
		  @GetMapping("/{companyNo}/widget/project-task")
		  public String projectTaskWidgetPartial(
		      @PathVariable String companyNo,
		      Model model,
		      Authentication authentication
		  ) {
		      RealUserWrapper wrapper = (RealUserWrapper) authentication.getPrincipal();
		      EmployeeVO emp = (EmployeeVO) wrapper.getRealUser();
		      String empId = emp.getEmpId();

		      List<Map<String, Object>> taskList = projectMapper.selectMyProjectTasks(empId);
		      model.addAttribute("taskList", taskList);
		      model.addAttribute("companyNo", companyNo);

		      return "groupware/project/mainProjectTaskWidget"; // JSP 위젯 경로
		  }
		
		/**
		 * 결재문서 목록을 조건에 따라 조회하는 메서드
		 *
		 * @param empId       로그인한 사용자의 사번 (결재자 또는 기안자)
		 * @param status      결재 상태 코드 (예: Draft, Waiting, Approved 등)
		 * @param isDrafter   true: 기안자 기준으로 조회 / false: 결재자 기준으로 조회
		 * @return            조건에 맞는 결재문서 리스트
		 */
		public List<AprvlDocVO> getApprovalDocsByCondition(String empId, String status) {
		    Map<String, Object> searchCondition = new HashMap<>();
		    searchCondition.put("empId", empId);
		    searchCondition.put("status", status);

		    List<AprvlDocVO> docs = apprDocService.selectApprDocStatusList(searchCondition);

		    for (AprvlDocVO doc : docs) {
		        try {
		            AprvlDocStatus docStatus = AprvlDocStatus.fromCode(doc.getAprvlDocStatus()); // 상태 코드로 enum 조회
		            doc.setStatusColor(docStatus.getColor());
		        } catch (IllegalArgumentException e) {
		            doc.setStatusColor("#000000"); // 기본값: 검정색
		        }
		    }

		    return docs;
		}
		
		// 위젯 위치 저장 ajax
		@PostMapping("/{companyNo}/widget/save")
		@ResponseBody
		public ResponseEntity<?> saveWidgetPositions(
		        @PathVariable String companyNo,
		        @RequestBody List<WidgetPositionVO> positions,
		        Authentication authentication) {
		    
		    RealUserWrapper wrapper = (RealUserWrapper) authentication.getPrincipal();
		    EmployeeVO emp = (EmployeeVO) wrapper.getRealUser();
		    String empId = emp.getEmpId();

		    positions.forEach(p -> p.setEmpId(empId));
		    widgetService.saveWidgetPositions(empId, positions);
		    
		    return ResponseEntity.ok(Map.of("success", true));
		}

}

