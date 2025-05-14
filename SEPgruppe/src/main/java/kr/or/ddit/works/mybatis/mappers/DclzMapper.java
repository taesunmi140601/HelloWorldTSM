package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.organization.vo.DclzStatusVO;

/**
 * 근태관리 매퍼
 * @author JYS
 * @since 2025. 3. 16.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 16.     	JYS	          최초 생성
 *
 * </pre>
 */

@Mapper
public interface DclzMapper {
	public List<DclzStatusVO> selectDclzStatus(@Param("empId") String empId);
    public void insertCheckIn(@Param("empId") String empId);
    public void updateCheckOut(@Param("empId") String empId);
	public DclzStatusVO selectTodayDclzStatus(String empId);
	public String selectWeeklyTotalHours(String empId);
}
