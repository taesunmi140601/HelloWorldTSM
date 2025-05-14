package kr.or.ddit.works.schedule.service;

import java.time.LocalDate;
import java.util.List;
import kr.or.ddit.works.schedule.vo.ScheduleTypeVO;
import kr.or.ddit.works.schedule.vo.ScheduleVO;

public interface ScheduleService {

    /**
     * 일정 전체 목록 조회
     * @return 일정 리스트
     */
    public List<ScheduleVO> readScheduleList();

    /**
     * 일정 상세 조회
     * @param schdlNo 일정 번호
     * @return 일정 상세 정보
     */
    public ScheduleVO readSchedule(int schdlNo);

    /**
     * 일정 등록
     * @param schedule 등록할 일정 정보
     * @return 처리 결과 (성공시 > 0)
     */
    public int createSchedule(ScheduleVO schedule);

    /**
     * 일정 수정
     * @param schedule 수정할 일정 정보
     * @return 처리 결과 (성공시 > 0)
     */
    public int updateSchedule(ScheduleVO schedule);

    /**
     * 일정 삭제 (단건)
     * @param schdlNo 삭제할 일정 번호
     * @return 처리 결과 (성공시 > 0)
     */
    public int deleteSchedule(int schdlNo);

    /**
     * 프로젝트 번호 목록에 해당하는 일정들 일괄 삭제
     * @param projectNos 삭제할 프로젝트 번호 리스트
     * @return 처리 결과 (성공시 > 0)
     */
    public int deleteSchedulesByProjectNos(List<Long> projectNos);

    /**
     * 일정 유형 전체 조회
     * @return 일정 유형 리스트
     */
    public List<ScheduleTypeVO> readScheduleType();

    /**
     * 특정 사원의 이번 달 일정 조회
     * @param empId 사원 ID
     * @param start 시작일
     * @param end 종료일
     * @return 일정 리스트
     */
    public List<ScheduleVO> selectThisMonthSchedules(String empId, LocalDate start, LocalDate end);

}
