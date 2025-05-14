package kr.or.ddit.works.organization.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.organization.service.DclzStatusService;
import kr.or.ddit.works.organization.vo.DclzStatusVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;

/**
 * 근태관리 컨트롤러
 * 
 * @author JYS
 * @since 2025. 3. 12.
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 12.     	JYS	          최초 생성
 *
 *      </pre>
 */
@Controller
@RequestMapping("{companyNo}/dclz")
public class DclzController {

	@Autowired
	private DclzStatusService service;

	/** 관리자 - 전사 근태 관리 목록 조회 */
//    @GetMapping("")
//    public String selectListAllDclz(@PathVariable("companyNo") String companyNo) {
//        return "gw:dclz/dclzList";
//    }
	@GetMapping("/")
	public String selectListAllDclz() {
		return "gw:dclz/dclzList";
	}

	/** 관리자 - 사원 별 근태 관리 조회 */
	@GetMapping("/{empNo}")
	public String selectDclz(@PathVariable("companyNo") String companyNo, @PathVariable("empNo") String empNo) {
		return "gw:dclz/dclzDetail";
	}

	/** 일반 사원 - 내 근태 관리 조회 */
	@GetMapping("/mydclz")
	public String selectMyDclz(@PathVariable("companyNo") String companyNo, Model model) {
		// 로그인한 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		String empId = null;

		if (principal instanceof RealUserWrapper) {
			RealUserWrapper user = (RealUserWrapper) principal;
			AllUserVO allUser = user.getRealUser();

			if (allUser instanceof EmployeeVO) {
				EmployeeVO employee = (EmployeeVO) allUser;
				empId = employee.getEmpId(); // 로그인한 사원의 empId 가져오기
			}
		}

		if (empId == null) {
			return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 이동
		}

		// 오늘 날짜 기준
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 전체 근태 데이터 가져오기
        List<DclzStatusVO> dclzStatusList = service.selectDclzStatus(empId);

        // 주차 데이터를 주 시작일 기준으로 그룹핑
        Map<LocalDate, List<DclzStatusVO>> groupedByWeek = new LinkedHashMap<>();

        for (DclzStatusVO status : dclzStatusList) {
            try {
                LocalDate date = LocalDate.parse(status.getWorkingDay(), formatter);
                LocalDate weekStart = date.with(java.time.DayOfWeek.MONDAY); // 주 시작일

                groupedByWeek.computeIfAbsent(weekStart, k -> new ArrayList<>()).add(status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());
        for (LocalDate d = firstDay; !d.isAfter(lastDay); d = d.plusDays(1)) {
            if (d.getDayOfWeek() == DayOfWeek.MONDAY) {
                groupedByWeek.putIfAbsent(d, new ArrayList<>());
            }
        }
        
        // 한국식 주차 계산: 해당 주에 현재 월이 포함되면 주차로 포함
        Map<Integer, List<DclzStatusVO>> finalWeeklyMap = new LinkedHashMap<>();
        int weekIndex = 1;
        int currentWeekIndex = 1;

        for (Map.Entry<LocalDate, List<DclzStatusVO>> entry : groupedByWeek.entrySet()) {
            LocalDate weekStart = entry.getKey();
            List<DclzStatusVO> weekList = entry.getValue();

            boolean isBelongsToThisMonth = weekList.stream()
                .anyMatch(s -> {
                    LocalDate d = LocalDate.parse(s.getWorkingDay(), formatter);
                    return d.getMonthValue() == currentMonth;
                });

            if (isBelongsToThisMonth) {
                finalWeeklyMap.put(weekIndex, weekList);
                // 이 주가 현재 주인지 여부는 주 시작일과 비교
                if (weekStart.equals(now.with(DayOfWeek.MONDAY))) {
                    currentWeekIndex = weekIndex;
                }
                weekIndex++;
            }
        }
        	int monthlyTotalMinutes = 0;
            for (List<DclzStatusVO> weekList : finalWeeklyMap.values()) {
                int totalMinutes = 0;
                for (DclzStatusVO s : weekList) {
                    String workingTime = s.getWorkingTime();
                    if (workingTime != null && !workingTime.isEmpty()) {
                        try {
                            String[] parts = workingTime.replace("시간", "").replace("분", "").trim().split(" ");
                            int hour = Integer.parseInt(parts[0].trim());
                            int minute = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : 0;
                            totalMinutes += hour * 60 + minute;
                        } catch (Exception ignored) {}
                    }
                }
                monthlyTotalMinutes += totalMinutes;
                
                int hours = totalMinutes / 60;
                int minutes = totalMinutes % 60;
                String weeklyTotal = hours + "시간 " + minutes + "분";
                if (!weekList.isEmpty()) {
                    weekList.get(0).setWeeklyTotalHours(weeklyTotal);
                }
            }
            int monthlyHours = monthlyTotalMinutes / 60;
            int monthlyMinutes = monthlyTotalMinutes % 60;
            String monthlyTotalHours = monthlyHours + "시간 " + monthlyMinutes + "분";
            model.addAttribute("monthlyTotalHours", monthlyTotalHours);
            
            int remainMinutes = 3120 - monthlyTotalMinutes;
            if (remainMinutes < 0) remainMinutes = 0;

            int remainHour = remainMinutes / 60;
            int remainMin = remainMinutes % 60;

            String monthlyRemainTime = remainHour + "시간 " + remainMin + "분";
            model.addAttribute("monthlyRemainTime", monthlyRemainTime);

            DclzStatusVO todayDclzStatus = service.selectTodayDclzStatus(empId);
            String weeklyTotalHours = finalWeeklyMap.containsKey(currentWeekIndex) && !finalWeeklyMap.get(currentWeekIndex).isEmpty()
                    ? finalWeeklyMap.get(currentWeekIndex).get(0).getWeeklyTotalHours()
                    : "0시간 0분";

            model.addAttribute("companyNo", companyNo);
            model.addAttribute("weeklyMap", finalWeeklyMap);
            model.addAttribute("weeklyTotalHours", weeklyTotalHours);
            model.addAttribute("dclzStatusList", dclzStatusList);
            model.addAttribute("todayDclzStatus", todayDclzStatus);
            model.addAttribute("empId", empId);
            model.addAttribute("currentWeekIndex", currentWeekIndex);

            return "gw:dclz/myDclz";
    }


	// 출근 체크
	@PostMapping("/{empId}/check-in")
	@ResponseBody
	public Map<String, Object> checkIn(@PathVariable("companyNo") String companyNo,
			@PathVariable("empId") String empId) {
		service.insertCheckIn(empId); // 출근 처리
		DclzStatusVO todayStatus = service.selectTodayDclzStatus(empId); // 오늘 근태 정보

		Map<String, Object> response = new HashMap<>();
		response.put("todayDclzStatus", todayStatus);
		return response;
	}

	// 퇴근 체크
	@PutMapping("/{empId}/check-out")
	@ResponseBody
	public Map<String, Object> checkOut(@PathVariable("companyNo") String companyNo,
			@PathVariable("empId") String empId) {
		service.updateCheckOut(empId); // 퇴근 처리
		DclzStatusVO todayStatus = service.selectTodayDclzStatus(empId);
		List<DclzStatusVO> dclzStatusList = service.selectDclzStatus(empId);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		java.time.temporal.WeekFields weekFields = java.time.temporal.WeekFields.of(java.util.Locale.KOREA);
		LocalDate now = LocalDate.now();

		int currentYear = now.getYear();
		int currentWeek = now.get(weekFields.weekOfWeekBasedYear());
		int thisWeekKey = currentYear * 100 + currentWeek;

		// 1. 주차별로 데이터 묶기
		Map<Integer, List<DclzStatusVO>> rawWeeklyMap = new HashMap<>();
		for (DclzStatusVO status : dclzStatusList) {
			try {
				LocalDate date = LocalDate.parse(status.getWorkingDay(), formatter);
				int year = date.getYear();
				int week = date.get(weekFields.weekOfWeekBasedYear());
				int weekKey = year * 100 + week;
				rawWeeklyMap.computeIfAbsent(weekKey, k -> new ArrayList<>()).add(status);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 2. 현재 주차 데이터만 추출
		List<DclzStatusVO> thisWeekList = rawWeeklyMap.get(thisWeekKey);

		// 3. 누적 근무시간 계산
		String weeklyTotalHours = "0시간 0분";
		if (thisWeekList != null) {
			int totalMinutes = 0;

			for (DclzStatusVO status : thisWeekList) {
				String workingTime = status.getWorkingTime(); 
				if (workingTime != null && !workingTime.isEmpty()) {
					try {
						String[] parts = workingTime.replace("시간", "").replace("분", "").trim().split(" ");
						int hour = Integer.parseInt(parts[0].trim());
						int minute = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : 0;
						totalMinutes += hour * 60 + minute;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			int hours = totalMinutes / 60;
			int minutes = totalMinutes % 60;
			weeklyTotalHours = hours + "시간 " + minutes + "분";
		}

		Map<String, Object> response = new HashMap<>();
		response.put("todayDclzStatus", todayStatus);
		response.put("weeklyTotalHours", weeklyTotalHours);

		return response;
	}

	@GetMapping("/{empId}/attendance-status")
	@ResponseBody
	public Map<String, Boolean> getAttendanceStatus(@PathVariable("companyNo") String companyNo,
			@PathVariable("empId") String empId) {
		DclzStatusVO todayStatus = service.selectTodayDclzStatus(empId);
		Map<String, Boolean> statusMap = new HashMap<>();
		statusMap.put("isCheckedIn", todayStatus != null && todayStatus.getAttendDate() != null);
		statusMap.put("isCheckedOut", todayStatus != null && todayStatus.getLvffcDate() != null);
		return statusMap;
	}
	
	@GetMapping("/{empId}/dclz-list")
	@ResponseBody
	public List<DclzStatusVO> getDclzList(@PathVariable("companyNo") String companyNo,
	                                      @PathVariable("empId") String empId) {
	    return service.selectDclzStatus(empId);
	}
	
	@GetMapping("/{empId}/summary-info")
	@ResponseBody
	public Map<String, String> getSummaryInfo(@PathVariable String companyNo, @PathVariable String empId) {
	    Map<String, String> result = new HashMap<>();
	    
	    LocalDate now = LocalDate.now();
	    LocalDate monday = now.with(DayOfWeek.MONDAY);

	    List<DclzStatusVO> list = service.selectDclzStatus(empId);

	    int weeklyMin = list.stream()
	        .filter(vo -> {
	            LocalDate d = LocalDate.parse(vo.getWorkingDay());
	            return !d.isBefore(monday);
	        })
	        .mapToInt(vo -> parseMinutes(vo.getWorkingTime()))
	        .sum();

	    int monthlyMin = list.stream()
	        .filter(vo -> {
	            LocalDate d = LocalDate.parse(vo.getWorkingDay());
	            return d.getMonthValue() == now.getMonthValue();
	        })
	        .mapToInt(vo -> parseMinutes(vo.getWorkingTime()))
	        .sum();

	    int limit = 52 * 60;
	    int remain = Math.max(0, limit - monthlyMin);
	    int over = Math.max(0, monthlyMin - limit);

	    result.put("weeklyTotalHours", format(weeklyMin));
	    result.put("monthlyTotalHours", format(monthlyMin));
	    result.put("monthlyRemainTime", format(remain));
	    result.put("monthlyOverTime", format(over));

	    return result;
	}

	private int parseMinutes(String time) {
	    if (time == null) return 0;
	    try {
	        String[] parts = time.replace("시간", "").replace("분", "").trim().split(" ");
	        int h = Integer.parseInt(parts[0].trim());
	        int m = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 0;
	        return h * 60 + m;
	    } catch (Exception e) {
	        return 0;
	    }
	}

	private String format(int totalMin) {
	    int h = totalMin / 60;
	    int m = totalMin % 60;
	    return String.format("%d시간 %d분", h, m);
	}
}