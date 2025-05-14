package kr.or.ddit.works.alarm.service;

import java.util.Date;

import javax.inject.Inject;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.alarm.vo.AlarmHistoryVO;
import kr.or.ddit.works.mybatis.mappers.AlarmMapper;

@Service
public class AlarmServiceImpl implements AlarmService {
	
	@Inject
	AlarmMapper mapper;

	@Override
	public void saveAlarm(String receiverId, String message) {
		AlarmHistoryVO alarm = new AlarmHistoryVO();
		alarm.setEmpId(receiverId);
		alarm.setAlarmContent(message);
		alarm.setIsAlarmRead(null);
		alarm.setAlarmReadTime(new Date().toString());
		
	}

}
