package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.works.widget.vo.WidgetPositionVO;

@Mapper
public interface WidgetPositionMapper {
	
	// 위젯 위치 저장 전에 기존 위치 제거
	public int deleteByEmpId(String empId);

	// 위젯 위치 저장
	public int insertWidgetPosition(WidgetPositionVO vo);

	// empId 기준 위젯 위치 조회
	public List<WidgetPositionVO> selectPositionsByEmpId(String empId);
}
