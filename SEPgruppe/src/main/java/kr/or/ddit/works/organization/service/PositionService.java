package kr.or.ddit.works.organization.service;

import java.util.List;

import kr.or.ddit.works.organization.vo.PositionVO;

/**
 * 직위 서비스
 * @author JYS
 * @since 2025. 4. 2.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 4. 2.     	JYS	          최초 생성
 *
 * </pre>
 */
public interface PositionService {

	/**
	 * 직위 목록 조회
	 * @return
	 */
	public List<PositionVO> selectPositionList();
	
	/**
	 * 사원 직급 상세 조회
	 * @return
	 */
	public String selectPositionName(String positionCd);
	
	public int deletePositions(List<String> positionCds);
	
	public List<PositionVO> selectPositionListCount();
	
	public int insertPosition(PositionVO positionVO);
	
	public int updateSortOrder(String positionCd, int sortOrder);
}
