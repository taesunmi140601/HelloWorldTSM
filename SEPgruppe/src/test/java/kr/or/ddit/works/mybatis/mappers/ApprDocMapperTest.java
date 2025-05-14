package kr.or.ddit.works.mybatis.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.ddit.CustomRootContextConfig;
import kr.or.ddit.works.approval.vo.AprvlLineAutoVO;
import kr.or.ddit.works.approval.vo.DocFolderVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 전자결재 문서 매퍼 테스트 케이스
 * @author JYS
 * @since 2025. 3. 20.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 20.     	JYS	          최초 생성
 *
 * </pre>
 */
@Slf4j
@CustomRootContextConfig
class ApprDocMapperTest {
	
	@Autowired
	private ApprDocMapper approvalMapper;

	@Test
	void testSelectListAllDocForm() {
		List<DocFormVO> docFormList = approvalMapper.selectListAllDocForm();
		log.info("조회된 문서 리스트: {}", docFormList);
	}
	
	@Test
	void testSelectListSearchDocForm() {
		String search = "연";
		List<DocFormVO> docFormList = approvalMapper.selectListSearchDocForm(search);
		log.info("검색 후 조회된 문서: {}", docFormList);
	}
	
	@Test
	void tesSelectdocFrmDeatil() {
		String docFrmNo = "DFR_HR_002_001";
		DocFormVO docFormDetail = approvalMapper.selectdocFrmDeatil(docFrmNo);
		log.info("조회된 문서 상세정보: {}", docFormDetail);
	}

	@Test
	@Disabled
	void testInsertDocForm() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testUpdateDocFormHtml() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testUpdateDocForm() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testDeactivateDocForm() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testActivateDocForm() {
		fail("Not yet implemented");
	}
	
	@Test
	void testselectListAllAutoLine() {
		List<DocFormVO> line = approvalMapper.selectListAllAutoLine();
		log.info("line : {}", line);
	}

	@Test
	void testselectDetailAutoLine() {
		String aprvlLineNo = "42";
		List<DocFormVO> line = approvalMapper.selectDetailAutoLine(aprvlLineNo);
		
		log.info("line : {}", line);
	}
	
	@Test
	void testSelectApprLineSearchDeptPosition() {
		String positionCd = "P010100";
		String deptCd ="D001";
		Map<String, Object> searchCondition = new HashMap<>();
		searchCondition.put("positionCd", positionCd);
		searchCondition.put("deptCd", deptCd);
		List<EmployeeVO> emp = approvalMapper.selectApprLineSearchDeptPosition(searchCondition);
		
		log.info("emp : {}", emp);
	}
}
