package kr.or.ddit.works.schedule.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.works.mybatis.mappers.ScheduleMapper;
import kr.or.ddit.works.schedule.vo.ScheduleTypeVO;
import kr.or.ddit.works.schedule.vo.ScheduleVO;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 전체 일정 목록 조회
     */
    @Override
    public List<ScheduleVO> readScheduleList() {
        return scheduleMapper.selectScheduleList();
    }

    /**
     * 일정 상세 조회
     * @param schdlNo 일정 번호
     * @return 일정 상세 정보
     */
    @Override
    public ScheduleVO readSchedule(int schdlNo) {
        return (ScheduleVO) scheduleMapper.selectSchedule(schdlNo);
    }

    /**
     * 일정 등록
     * 유효성 검사 후 DB에 등록
     * @param schedule 등록할 일정
     * @return 처리 결과
     */
    @Transactional
    @Override
    public int createSchedule(ScheduleVO schedule) {
        if (schedule == null || schedule.getSchdlBgngYmd() == null || schedule.getSchdlEndYmd() == null) {
            throw new IllegalArgumentException("다시 입력해주세요.");
        }
        return scheduleMapper.insertSchedule(schedule);
    }

    /**
     * 일정 수정
     * 일정 유형 번호가 없을 경우, 일정 유형 객체에서 가져와 설정
     * @param schedule 수정할 일정
     * @return 처리 결과
     */
    @Transactional
    @Override
    public int updateSchedule(ScheduleVO schedule) {
        if (schedule.getScheduleTypeNo() == null && schedule.getScheduleType() != null) {
            schedule.setScheduleTypeNo(schedule.getScheduleType().getScheduleTypeNo());
        }
        return scheduleMapper.updateSchedule(schedule);
    }

    /**
     * 단건 일정 삭제
     * @param schdlNo 삭제할 일정 번호
     * @return 처리 결과
     */
    @Transactional
    @Override
    public int deleteSchedule(int schdlNo) {
        return scheduleMapper.deleteSchedule(schdlNo);
    }

    /**
     * 프로젝트 번호 목록에 해당하는 일정들 일괄 삭제
     * @param projectNos 삭제 대상 프로젝트 번호 리스트
     * @return 처리 결과
     */
    @Transactional
    @Override
    public int deleteSchedulesByProjectNos(List<Long> projectNos) {
        if (projectNos == null || projectNos.isEmpty()) {
            return 0; // 삭제할 대상이 없으면 0 반환
        }
        return scheduleMapper.deleteSchedulesByProjectNos(projectNos);
    }

    /**
     * 일정 유형 전체 조회
     * @return 일정 유형 리스트
     */
    @Override
    public List<ScheduleTypeVO> readScheduleType() {
        return scheduleMapper.selectScheduleType();
    }

    /**
     * 이번 달 특정 사원의 일정 목록 조회
     * @param empId 사원 ID
     * @param start 시작일
     * @param end 종료일
     * @return 일정 리스트
     */
    @Override
    public List<ScheduleVO> selectThisMonthSchedules(String empId, LocalDate start, LocalDate end) {
        return scheduleMapper.selectThisMonthSchedules(empId, start, end);
    }
}
