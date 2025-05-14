package kr.or.ddit.works.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.approval.enums.AprvlDocStatus;
import kr.or.ddit.works.approval.enums.AprvlLineStatus;
import kr.or.ddit.works.approval.service.ApprDocService;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.mybatis.mappers.ApprDocMapper;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/{companyNo}/apprHome")
public class ApprHomeController {

	@Autowired
	private ApprDocService apprDocSerive;
	
	@Autowired
	private ApprDocMapper apprDocMapper;
	
	
	
	/**
	 * 전자결재 홈 이동
	 * @param companyNo
	 * @param authentication
	 * @param model
	 * @return
	 */
	@GetMapping
	public String apprHome(
			@PathVariable("companyNo") String companyNo
			, Authentication authentication
			, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		Map<String, Object> searchCondition = new HashMap<>();
		
		// 로그인한 사용자 정보 조회
	    RealUserWrapper userDetails = (RealUserWrapper) authentication.getPrincipal();
	    log.info("userDeatils : {}", userDetails);
	    EmployeeVO realUser = (EmployeeVO) userDetails.getRealUser();
	    String loginEmpId = realUser.getEmpId();
	    
	    searchCondition.put("loginEmpId", loginEmpId);


	    // 결재 대기
	    List<AprvlDocVO> waitingDocs = getApprovalDocsByCondition(loginEmpId, AprvlLineStatus.Waiting.getCode());
	    log.info("waitingDocs:{}",waitingDocs);
	    // 기안 문서
	    List<AprvlDocVO> draftDocs = getApprovalDocsByCondition(loginEmpId, AprvlLineStatus.Draft.getCode());
	    log.info("draftDocs:{}",draftDocs);
	   
	    
	    // 상신완료, 상신취소
	    List<AprvlDocVO> draftWithDrawnDocs = apprDocMapper.selectApprDocApprDrawnList(loginEmpId, AprvlLineStatus.Draft.getCode(), AprvlLineStatus.Canceled.getCode());
	    log.info("draftWithDrawnDocs: {}",draftWithDrawnDocs);
	    for (AprvlDocVO doc : draftWithDrawnDocs) {
	        try {
	            AprvlDocStatus docStatus = AprvlDocStatus.fromCode(doc.getAprvlDocStatus()); // 상태 코드로 enum 조회
	            doc.setStatusColor(docStatus.getColor());
	        } catch (IllegalArgumentException e) {
	            doc.setStatusColor("#000000"); // 기본값: 검정색
	        }
	    }
	    
	    // 결재완료
	    List<AprvlDocVO> approvedDocs = getApprovalDocsByCondition(loginEmpId, AprvlLineStatus.Approved.getCode());
	    log.info("approvedDocs:{}",approvedDocs);
	    
	    // 문서함
	    
	    
	    model.addAttribute("waitingDocs", getTopFive(waitingDocs));
	    model.addAttribute("draftDocs", getTopFive(draftDocs));
	    model.addAttribute("draftWithDrawnDocs", getTopFive(draftWithDrawnDocs));
	    model.addAttribute("approvedDocs", getTopFive(approvedDocs));
	    
	    

		return "gw:appr/apprHome";
	}
	
	// 최대 5건만 보여줄 유틸 메서드
	private <T> List<T> getTopFive(List<T> list) {
	    return list.size() > 5 ? list.subList(0, 5) : list;
	}
	
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

	    List<AprvlDocVO> docs = apprDocSerive.selectApprDocStatusList(searchCondition);

	    for (AprvlDocVO doc : docs) {
	        try {
	            AprvlDocStatus docStatus = AprvlDocStatus.fromCode(doc.getAprvlDocStatus()); // 상태 코드로 enum 조회
	            doc.setStatusColor(docStatus.getColor());
	        } catch (IllegalArgumentException e) {
	            doc.setStatusColor("#000000"); // 기본값: 검정색
	        }
	    }

	    return docs;
	}
	
	/**
	 * 결재 문서 보기
	 * @param companyNo
	 * @param aprvlDocNo
	 * @param model
	 * @return
	 */
	@GetMapping("/aprvlDocDetail/{aprvlDocNo}")
	public String viewApprovalDoc(
		@PathVariable String companyNo, 
		@PathVariable String aprvlDocNo,
		Model model,
		Authentication authentication
	) {
		// 로그인한 사용자 정보 조회
	    RealUserWrapper userDetails = (RealUserWrapper) authentication.getPrincipal();
	    log.info("userDeatils : {}", userDetails);
	    EmployeeVO realUser = (EmployeeVO) userDetails.getRealUser();
	    String loginEmpId = realUser.getEmpId();
		
	    model.addAttribute("loginEmpId", loginEmpId);
		model.addAttribute("aprvlDocNo", aprvlDocNo);
		model.addAttribute("aprvlDocContents", apprDocMapper.selectAprvlDocContents(aprvlDocNo));
		return "gw:appr/aprvlDocDetail";
	}
	
	/**
	 * 결재문서의 결재선 조회
	 * @param companyNo
	 * @param docNo
	 * @return
	 */
	@GetMapping("line/{docNo}")
	public ResponseEntity<Map<String, Object>> selectAprvlLineByDoc(
		@PathVariable String companyNo,
		@PathVariable String docNo
	){
		
		log.info("docNo:{}",docNo);
		Map<String, Object> response = new HashMap<>();
		
		List<AprvlLineVO> apprLineList = apprDocMapper.selectApprLine(docNo);
		log.info("apprLineList:{}",apprLineList);
		response.put("lineList", apprLineList);
		
		return ResponseEntity.ok(response);
		
	}

	/**
	 * 결재선 상태 update
	 * @param companyNo
	 * @param req
	 * @return
	 */
	@PostMapping("updateStatus")
	public ResponseEntity<Map<String, Object>> updateAprvlLineStauts(
		@PathVariable String companyNo,
		@RequestBody AprvlLineVO req
	) {
		String aprvlStatus = req.getAprvlStatus();
		String aprvlDocNo = req.getAprvlDocNo();
		String empId = req.getEmpId();
		
		// 처리 로직 예시
	    boolean result = apprDocSerive.updateApprLineStatus(aprvlStatus, aprvlDocNo, empId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("success", result);

	    return ResponseEntity.ok(response);
	}
	
	
	/**
	 * 기안자 - 기안문서 회수
	 * @param companyNo
	 * @param apprDocNo
	 * @param authentication
	 * @return
	 */
	@PostMapping("cancel/{apprDocNo}")
	public ResponseEntity<String> updateCancleApprDoc(
		@PathVariable("companyNo") String companyNo
		, @PathVariable("apprDocNo")String apprDocNo
		, Authentication authentication
	){
		// 로그인한 사용자 정보 조회
	    RealUserWrapper userDetails = (RealUserWrapper) authentication.getPrincipal();
	    log.info("userDeatils : {}", userDetails);
	    EmployeeVO realUser = (EmployeeVO) userDetails.getRealUser();
	    String loginEmpId = realUser.getEmpId();
	    
	    boolean result = apprDocSerive.updateCancleApprDoc(apprDocNo, loginEmpId);
	    if (result) {
	        return ResponseEntity.ok("회수 완료");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회수 실패: 이미 결재가 시작되었습니다.");
	    }
	}
 	
	/**
	 * 결재 후 html update
	 * @param companyNo
	 * @param payload
	 * @return
	 */
	@PostMapping("updateContents")
	public ResponseEntity<?> updateApprovalHtml(
	    @PathVariable String companyNo,
	    @RequestBody Map<String, String> payload
	) {
	    String docNo = payload.get("docNo");
	    String htmlContent = payload.get("htmlContent");

	    boolean result = apprDocSerive.updateApprovalHtml(docNo, htmlContent);

	    return ResponseEntity.ok(Map.of("success", result));
	}

	

}
