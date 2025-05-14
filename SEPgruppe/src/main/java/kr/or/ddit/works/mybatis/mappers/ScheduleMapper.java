package kr.or.ddit.works.mybatis.mappers;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.schedule.vo.ScheduleTypeVO;
import kr.or.ddit.works.schedule.vo.ScheduleVO;

@Mapper
public interface ScheduleMapper {

    /**
     * 일정 전체 목록 조회
     * @return 전체 일정 리스트
     */
    public List<ScheduleVO> selectScheduleList();

    /**
     * 일정 상세 조회
     * @param schdlNo 일정 번호
     * @return 해당 일정 상세 정보
     */
    public ScheduleVO selectSchedule(int schdlNo);

    /**
     * 일정 등록
     * @param schedule 등록할 일정 정보
     * @return 등록된 행 수
     */
    public int insertSchedule(ScheduleVO schedule);

    /**
     * 일정 수정
     * @param schedule 수정할 일정 정보
     * @return 수정된 행 수
     */
    public int updateSchedule(ScheduleVO schedule);

    /**
     * 일정 삭제 (단건)
     * @param schdlNo 삭제할 일정 번호
     * @return 삭제된 행 수
     */
    public int deleteSchedule(int schdlNo);

    /**
     * 프로젝트 번호 목록에 해당하는 일정들 일괄 삭제
     * @param projectNos 삭제할 프로젝트 번호 리스트
     * @return 삭제된 행 수
     */
    public int deleteSchedulesByProjectNos(List<Long> projectNos);

    /**
     * 일정 유형 목록 조회
     * @return 일정 유형 리스트
     */
    public List<ScheduleTypeVO> selectScheduleType();

    /**
     * 대시보드용 - 특정 사원의 이번 달 일정 조회
     * @param empId 사원 ID
     * @param start 시작일 (월의 첫 날)
     * @param end 종료일 (월의 마지막 날)
     * @return 일정 리스트
     */
    public List<ScheduleVO> selectThisMonthSchedules(
    	@Param("empId") String empId
    	, @Param("start") LocalDate start
    	, @Param("end") LocalDate end
    );
}
