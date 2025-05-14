package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.works.common.vo.CommCodeVO;

/**
 * 공통코드 매퍼
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
@Mapper
public interface CommCodeMapper {
	
	
	/**
	 * 자동결재선 타입 조회
	 * @return 결재, 합의
	 */
	public List<CommCodeVO> selectApprLineType();

}
