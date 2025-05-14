package kr.or.ddit.works.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.approval.enums.AprvlDocStatus;
import kr.or.ddit.works.approval.enums.AprvlLineStatus;
import kr.or.ddit.works.approval.service.ApprDocService;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/{companyNo}/box")
public class DocBoxController {
	
	@Autowired
	private ApprDocService apprDocSerive;
	
	/**
	 * 결재문서 목록을 조건에 따라 조회하는 메서드
	 *
	 * @param empId       로그인한 사용자의 사번 (결재자 또는 기안자)
	 * @param status      결재 상태 코드 (예: Draft, Waiting, Approved 등)
	 * @param isDrafter   true: 기안자 기준으로 조회 / false: 결재자 기준으로 조회
	 * @return            조건에 맞는 결재문서 리스트
	 */
	public List<AprvlDocVO> getApprovalDocsByCondition(String empId, String status) {
	    Map<String, Object> searchCondition = new HashMap<>();
	    searchCondition.put("empId", empId);
	    searchCondition.put("status", status);

	    return apprDocSerive.selectApprDocStatusList(searchCondition);
	}
	
	/**
	 * 결재 대기 문서 페이지 이동
	 * @return
	 */
	@GetMapping("awaitDocs")
	public String selectDocsAwait(
		@PathVariable("companyNo") String companyNo,
		Model model
	) {
		
		//apprDocSerive.selectApprDocListByTurn()
		
		return "gw:appr/docAwaitList";
	}
	
	@GetMapping("pendingDocs")
	public String selectDocsPending(
		@PathVariable("companyNo") String companyNo
	) {
		return "gw:appr/docPendingList";
	}
	
	
	/**
	 * 문서함 공통 조회 (기안, 임시, 결재, 참조/열람)
	 * @param companyNo
	 * @param boxType - draft / temp / appr / ref
	 * @param model
	 * @return
	 */
	@GetMapping("{boxType}Docs")
	public String selectDocsByType(
	    @PathVariable("companyNo") String companyNo,
	    @PathVariable("boxType") String boxType,
	    Authentication authentication,
	    Model model
	) {
	    model.addAttribute("companyNo", companyNo);

	    // 로그인한 사용자 정보
	    RealUserWrapper userDetails = (RealUserWrapper) authentication.getPrincipal();
	    EmployeeVO realUser = (EmployeeVO) userDetails.getRealUser();
	    String loginEmpId = realUser.getEmpId();

	    // boxType에 따라 상태 코드 결정
	    String statusCode;
	    String viewName;

	    switch (boxType) {
	        case "draft":
	            statusCode = AprvlLineStatus.Draft.getCode();
	            viewName = "gw:appr/boxDraftDocList";
	            break;
	        case "temp":
	            statusCode = AprvlDocStatus.TEMP_SAVE.getCode(); // 문서 상태 -  임시저장
	            viewName = "gw:appr/boxTempSaveList";
	            break;
	        case "appr":
	            statusCode = AprvlLineStatus.Approved.getCode();
	            viewName = "gw:appr/boxApprDocList";
	            break;
	        case "ref":
	            statusCode = AprvlLineStatus.Reference.getCode();
	            viewName = "gw:appr/boxRefDocList";
	            break;
	        default:
	            throw new IllegalArgumentException("잘못된 문서함 요청: " + boxType);
	    }

	    List<AprvlDocVO> docs = getApprovalDocsByCondition(loginEmpId, statusCode);

	    for (AprvlDocVO doc : docs) {
	        try {
	            AprvlDocStatus docStatus = AprvlDocStatus.fromCode(doc.getAprvlDocStatus());
	            doc.setStatusColor(docStatus.getColor());
	        } catch (IllegalArgumentException e) {
	            doc.setStatusColor("#000000");
	        }
	    }

	    model.addAttribute("docs", docs);
	    return viewName;
	}
	
	
	
	

	
//	/**
//	 * 기안문서함 - 기안문서 전체 조회
//	 * @param companyNo
//	 * @param model
//	 * @return
//	 */
//	@GetMapping("draftDocs")
//	public String selectDraftList(
//		@PathVariable("companyNo") String companyNo
//		, Authentication authentication
//		, Model model
//	) {
//		model.addAttribute("companyNo", companyNo);
//		
//		// 로그인한 사용자 정보 조회
//	    RealUserWrapper userDetails = (RealUserWrapper) authentication.getPrincipal();
//	    log.info("userDeatils : {}", userDetails);
//	    EmployeeVO realUser = (EmployeeVO) userDetails.getRealUser();
//	    String loginEmpId = realUser.getEmpId();
//	    
//	    List<AprvlDocVO> draftDocs = getApprovalDocsByCondition(loginEmpId, AprvlLineStatus.Draft.getCode());
//	    
//	    for (AprvlDocVO doc : draftDocs) {
//	        try {
//	            AprvlDocStatus docStatus = AprvlDocStatus.fromCode(doc.getAprvlDocStatus()); // 상태 코드로 enum 조회
//	            doc.setStatusColor(docStatus.getColor());
//	        } catch (IllegalArgumentException e) {
//	            doc.setStatusColor("#000000"); // 기본값: 검정색
//	        }
//	    }
//
//	    
//	    log.info("draftDocs:{}",draftDocs);
//	    
//	    model.addAttribute("draftDocs",draftDocs);
//		
//		return "gw:appr/boxDraftDocList";
//	}
	

	
	
	
	/**
	 * 부서문서함
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("depDocs")
	public String selectDepDocList(
		@PathVariable("companyNo") String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		return "gw:appr/departmentDocList";
	}
}
