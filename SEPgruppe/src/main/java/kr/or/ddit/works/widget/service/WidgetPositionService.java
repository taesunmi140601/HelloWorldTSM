package kr.or.ddit.works.widget.service;

import java.util.List;

import kr.or.ddit.works.widget.vo.WidgetPositionVO;

public interface WidgetPositionService {
	
	public void saveWidgetPositions(String empId, List<WidgetPositionVO> positions);

	public List<WidgetPositionVO> getWidgetPositions(String empId);
}
