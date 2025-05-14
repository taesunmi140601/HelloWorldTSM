package kr.or.ddit.works.approval.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.works.alarm.controller.GWAlarmController;
import kr.or.ddit.works.approval.enums.AprvlDocStatus;
import kr.or.ddit.works.approval.enums.AprvlLineStatus;
import kr.or.ddit.works.approval.service.ApprDocService;
import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.approval.vo.AprvlLineAutoVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.mybatis.mappers.ApprDocMapper;
import kr.or.ddit.works.organization.service.EmployeeService;
import kr.or.ddit.works.organization.service.PositionService;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/{companyNo}/approval")
public class ApprDocController {
	
	@Autowired
	private ApprDocMapper apprDocMapper;
	
	@Autowired
	private ApprDocService apprDocService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private GWAlarmController gwAlarmController;
	
	/**
	 * 
	 * @param companyNo
	 * @param docFrmName
	 * @param model
	 * @return
	 */
	@PostMapping
	public String selectListSearchDocForm(
			@Param("companyNo")String companyNo
			, @Param("docFrmName") String docFrmName
			, Model model
			) {
		model.addAttribute("companyNo", companyNo);
		model.addAttribute("docFrmName", docFrmName);
		
		return "";
	}
	
	/**
	 * 새 문서 작성 폼으로 이동
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("new")
	public String insertApprDocFormUI(
		@PathVariable("companyNo")String companyNo,
		Model model
	) {
		model.addAttribute("companyNo",companyNo);
	
		return "gw:appr/apprForm";
	}
	
	
	
	/**
	 * 문서 상세 조회
	 * @param docFrmNo
	 * @param model
	 */
	@GetMapping("detail")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectdocFrmDeatil(
	        @RequestParam("docFrmNo") String docFrmNo,
	        Authentication authentication) {

	    // 문서 양식 조회
	    DocFormVO formDetail = apprDocService.selectdocFrmDeatil(docFrmNo);
	    log.info("formDetail",formDetail);
	    
	    if (formDetail == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
	    // 로그인한 사용자 정보 조회
	    RealUserWrapper userDetails = (RealUserWrapper) authentication.getPrincipal();
	    log.info("userDeatils : {}", userDetails);
	    EmployeeVO realUser = (EmployeeVO) userDetails.getRealUser();
	    String userDeptCd = realUser.getDeptCd();
	   
	    
	    // 자동결재선 리스트 가져오기
	    List<DocFormVO> docFormList = apprDocService.selectDetailAutoLine(docFrmNo);
	    log.info("docFormList : {}", docFormList);
	    
	    List<AprvlLineAutoVO> aprvlLineAutoList = new ArrayList<AprvlLineAutoVO>();

	    if (docFormList != null && !docFormList.isEmpty()) {
	        for (DocFormVO form : docFormList) {
	            // 자동결재선 리스트를 따로 조회
	            List<AprvlLineAutoVO> aprvlLineList = form.getAprvlLineAuto();

	            // 가져온 자동결재선 리스트를 VO에 세팅
	            form.setAprvlLineAuto(aprvlLineList);

	            if (aprvlLineList != null && !aprvlLineList.isEmpty()) {
	                aprvlLineAutoList.addAll(aprvlLineList);
	            }
	        }
	    }

	    for (AprvlLineAutoVO line : aprvlLineAutoList) {
	    	String positionCd = line.getPositionCd();

	        // 직위 코드에 대한 정렬 순서 가져오기
	        int sortOrder = apprDocMapper.getSortOrderByPositionCd(positionCd);

	        String targetDeptCd = null;
	        if (!"none".equals(line.getDeptCd())) {
	            targetDeptCd = line.getDeptCd();
	        } else if (sortOrder >= 6) { // 부장 이하일 경우만 로그인 사용자의 부서 사용
	            targetDeptCd = userDeptCd;
	        }

	        Map<String, Object> searchCondition = new HashMap<>();
	        searchCondition.put("positionCd", line.getPositionCd());
	        searchCondition.put("deptCd", targetDeptCd);

	        List<EmployeeVO> empList = apprDocService.selectApprLineSearchDeptPosition(searchCondition);
	        line.setEmployeeList(empList); // 결재선에 결재자 리스트 세팅
	    }
	    
	    
	    // positionCd로 positionName 조회
	    String positionCd = realUser.getPositionCd();
	    String positionName = positionService.selectPositionName(positionCd); // 서비스 메서드 필요
	    realUser.setPositionName(positionName); // 값 세팅
	    log.info("realUser : {}", realUser);

	    String formattedDate = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date());

	    
	    // JSON 응답 구성
	    Map<String, Object> response = new HashMap<>();
	    response.put("formDetail", formDetail);
	    response.put("today", formattedDate);
	    response.put("aprlLineAuto", aprvlLineAutoList);
	    response.put("realUser", realUser); // 로그인 사용자 정보 추가

	    return ResponseEntity.ok(response);
	}
	
	
	/**
	 * 문서 상신 - 기안자 새 문서 기안
	 * @param aprvlDoc
	 * @return
	 */
	@PostMapping("insertDraft")
	public ResponseEntity<String> insertDraftDoc(@RequestBody AprvlDocVO aprvlDoc) {
		log.info("Received AprvlDocVO: {}", aprvlDoc);
	    log.info("Received apprLineList: {}", aprvlDoc.getAprvlLineList());
		
	    // 1. 문서 번호 생성 (시퀀스)
	    aprvlDoc.setAprvlDocNo(apprDocService.selectAprvlDocSeq());
	    
	    log.info("aprvlDocNo:{}", aprvlDoc.getAprvlDocNo());

	    // 2. 문서 상태는 'SUBMITTED' (상신완료)
	    aprvlDoc.setAprvlDocStatus(AprvlDocStatus.SUBMITTED.getCode());

	    try {
	        // 3. 문서 insert
	        apprDocService.insertDraft(aprvlDoc);

	        // 4. 결재선 리스트 확인 후 각 결재선 insert
	        List<AprvlLineVO> lineList = aprvlDoc.getAprvlLineList();
	        
	        log.info("lineList : {}", lineList);

	        if (lineList != null && !lineList.isEmpty()) {
	            for (AprvlLineVO line : lineList) {
	            	
	                // 1. 결재 문서 번호 연결
	                line.setAprvlDocNo(aprvlDoc.getAprvlDocNo());

	                // 2. 결재선 고유 번호 시퀀스 생성
	                line.setAprvlLineNo(apprDocMapper.selectAprvlLineNoSeq());

	                if (line.getAprvlTurn() == 0) {
	                    // 기안자: 상태 Draft(D)
	                    line.setAprvlStatus(AprvlLineStatus.Draft.getCode());
	                } else {
	                    // 결재자: 상태 Waiting(W)
	                    line.setAprvlStatus(AprvlLineStatus.Waiting.getCode());
	                }

	                // 5. 결재선 DB에 insert
	                apprDocService.insertAprvlLine(line);
	            }
	            
	            gwAlarmController.sendApprAlarm(aprvlDoc, lineList.get(1));
	            
	        }

	        return ResponseEntity.ok("success");

	    } catch (Exception e) {
	        log.error("insertDraftDoc 예외 발생", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}
	
	/**
	 * 기안자 결재선 생성
	 * @param empId
	 * @return
	 */
	@GetMapping("customApprLine")
	public EmployeeVO customApprLine(
			@RequestParam String empId
	){
		return employeeService.selectEmployee(empId); // 단일 사원 정보 반환
	}

}
