package kr.or.ddit.works.approval.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.ddit.CustomRootContextConfig;
import kr.or.ddit.works.approval.vo.DocFormVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CustomRootContextConfig
class ApprDocServiceImplTest {
	
	@Autowired
	private ApprDocService apprDocService;

	@Test
	void testSelectListAllDocForm() {
		List<DocFormVO> docFormList = apprDocService.selectListAllDocForm();
		log.info("조회된 문서 리스트: {}", docFormList);
	}

	@Test
	void testSelectListSearchDocForm() {
		String docFrmName = "연";
		List<DocFormVO> searchList = apprDocService.selectListSearchDocForm(docFrmName);
		log.info("검색된 문서 리스트: {}", searchList);
	}

	@Test
	void testSelectdocFrmDeatil() {
		String docFrmNo = "DFR_HR_002_001";
		DocFormVO docFormDetail = apprDocService.selectdocFrmDeatil(docFrmNo);
		log.info("조회된 문서 상세정보: {}", docFormDetail);
	}

	@Test
	void testInsertDocForm() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateDocFormHtml() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateDocForm() {
		fail("Not yet implemented");
	}

	@Test
	void testDeactivateDocForm() {
		fail("Not yet implemented");
	}

	@Test
	void testActivateDocForm() {
		fail("Not yet implemented");
	}

	
}
