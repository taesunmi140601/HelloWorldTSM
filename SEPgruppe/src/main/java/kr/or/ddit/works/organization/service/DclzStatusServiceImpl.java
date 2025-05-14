package kr.or.ddit.works.organization.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.DclzMapper;
import kr.or.ddit.works.organization.vo.DclzStatusVO;

/**
 * 
 * @author JSW
 * @since 2025. 3. 18.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 18.     	JSW	          최초 생성
 *
 * </pre>
 */
@Service
public class DclzStatusServiceImpl implements DclzStatusService {

	@Autowired
	private DclzMapper mapper;

	@Override
	public List<DclzStatusVO> selectDclzStatus(String empId) {
		List<DclzStatusVO> dclzStatusList = mapper.selectDclzStatus(empId);
        groupByWeek(dclzStatusList); // 주차별로 그룹화
        return dclzStatusList;
	}
	
	private void groupByWeek(List<DclzStatusVO> dclzStatusList) {
       Map<Integer, Integer> weeklyTotalTimeMap = new HashMap<>();
		
		for (DclzStatusVO status : dclzStatusList) {
            LocalDate date = LocalDate.parse(status.getWorkingDay());
            int weekOfMonth = getWeekOfMonth(date);
            status.setWeekOfMonth(weekOfMonth); // 주차 정보 설정
            
            int workingTimes = parseWorkingTime(status.getWorkingTime());
            weeklyTotalTimeMap.put(weekOfMonth, weeklyTotalTimeMap.getOrDefault(weekOfMonth, 0) + workingTimes);
            
        }
		for (DclzStatusVO status : dclzStatusList) {
            int weekOfMonth = status.getWeekOfMonth();
            int totalTimes = weeklyTotalTimeMap.getOrDefault(weekOfMonth, 0);
            status.setWeeklyTotalHours(formatMinutesToTime(totalTimes));
		}
    }
	
	private int getWeekOfMonth(LocalDate date) {
		LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        LocalDate startOfFirstWeek = firstDayOfMonth.minusDays(dayOfWeek % 7);
        long daysBetween = ChronoUnit.DAYS.between(startOfFirstWeek, date);
        int weekOfMonth = (int) (daysBetween / 7) + 1;

        return weekOfMonth;
    }
	
	private int parseWorkingTime(String workingTime) {
		 if (workingTime == null || workingTime.isEmpty() || workingTime.trim().isEmpty()) {
		        return 0; // 빈 문자열이면 0을 반환
		    }
		 	
		    try {
		        String[] parts = workingTime.split("시간 |분");
		        if (parts.length < 2) {
		        	if (workingTime.contains(":")) {
		                // "08:30" 형식 처리
		                String[] timeParts = workingTime.split(":");
		                if (timeParts.length == 2) {
		                    int hours = NumberUtils.toInt(timeParts[0].trim(), 0);
		                    int minutes = NumberUtils.toInt(timeParts[1].trim(), 0);
		                    return hours * 60 + minutes;
		                }
		            }
		            return 0; // 형식이 잘못된 경우 0을 반환
		        }

		        int hours = NumberUtils.toInt(parts[0].trim(), 0);
		        int minutes = NumberUtils.toInt(parts[1].trim(), 0);

		        return hours * 60 + minutes; // 총 분 단위로 반환
		    } catch (Exception e) {
		        System.err.println("근무시간 파싱 오류: " + workingTime);
		        return 0; // 파싱 오류 시 0을 반환
		    }
	}
	
	private String formatMinutesToTime(int totalMinutes) {
		if (totalMinutes < 0) {
	        return "00시간 00분";
		}
		
		int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%02d시간 %02d분", hours, minutes);
    }
	
	private String parseAndFormatWorkingTime(String workingTime) {
	    int totalMinutes = parseWorkingTime(workingTime); // 분 단위로 변환
	    return formatMinutesToTime(totalMinutes); // 시 분 형식으로 변환
	}
	
	@Override
	public String getThisWeekTotalTime(String empId) {
	    List<DclzStatusVO> list = mapper.selectDclzStatus(empId);
	    if (list == null || list.isEmpty()) return "0시간 0분";

	    LocalDate now = LocalDate.now();
	    WeekFields weekFields = WeekFields.of(Locale.KOREA);
	    int currentWeek = now.get(weekFields.weekOfWeekBasedYear());
	    int currentYear = now.getYear();

	    int totalMinutes = list.stream()
	        .filter(vo -> {
	            try {
	                LocalDate day = LocalDate.parse(vo.getWorkingDay());
	                int week = day.get(weekFields.weekOfWeekBasedYear());
	                int year = day.getYear();
	                return week == currentWeek && year == currentYear;
	            } catch (Exception e) {
	                return false;
	            }
	        })
	        .mapToInt(vo -> parseWorkingTime(vo.getWorkingTime()))
	        .sum();

	    return formatMinutesToTime(totalMinutes); // 기존 메서드 활용
	}
	

	@Transactional
	@Override
	public void insertCheckIn(String empId) {
		DclzStatusVO todayStatus = mapper.selectTodayDclzStatus(empId);
	    if (todayStatus != null && todayStatus.getAttendDate() != null) {
	        throw new IllegalStateException("이미 출근 기록이 있습니다.");
	    }
		mapper.insertCheckIn(empId);
	}

	@Transactional
	@Override
	public void updateCheckOut(String empId) {
		DclzStatusVO todayStatus = mapper.selectTodayDclzStatus(empId);
	    if (todayStatus == null || todayStatus.getAttendDate() == null) {
	        throw new IllegalStateException("출근 기록이 없습니다.");
	    }
	    if (todayStatus.getLvffcDate() != null) {
	        throw new IllegalStateException("이미 퇴근 기록이 있습니다.");
	    }
		mapper.updateCheckOut(empId);
	}

	@Override
	public DclzStatusVO selectTodayDclzStatus(String empId) {
		return mapper.selectTodayDclzStatus(empId);
	}

	@Override
	public String getWeeklyTotalHours(String empId) {
		 return mapper.selectWeeklyTotalHours(empId); // Mapper에서 주간 누적 시간 조회
	}
	
	
}
