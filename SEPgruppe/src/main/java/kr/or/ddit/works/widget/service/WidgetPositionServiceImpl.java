package kr.or.ddit.works.widget.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.mybatis.mappers.WidgetPositionMapper;
import kr.or.ddit.works.widget.vo.WidgetPositionVO;

@Service
public class WidgetPositionServiceImpl implements WidgetPositionService {

	@Autowired
	private WidgetPositionMapper mapper;

	@Override
	public void saveWidgetPositions(String empId, List<WidgetPositionVO> positions) {
		mapper.deleteByEmpId(empId);
		for (WidgetPositionVO vo : positions) {
			mapper.insertWidgetPosition(vo);
		}
	}

	@Override
	public List<WidgetPositionVO> getWidgetPositions(String empId) {
		return mapper.selectPositionsByEmpId(empId);
	}

}
