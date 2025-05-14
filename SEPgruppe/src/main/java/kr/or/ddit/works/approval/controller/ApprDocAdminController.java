package kr.or.ddit.works.approval.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.works.approval.service.ApprDocService;
import kr.or.ddit.works.approval.vo.AprvlLineAutoVO;
import kr.or.ddit.works.approval.vo.AprvlLineVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.common.vo.CommCodeVO;
import kr.or.ddit.works.mybatis.mappers.CommCodeMapper;
import kr.or.ddit.works.organization.service.DepartmentService;
import kr.or.ddit.works.organization.service.PositionService;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.PositionVO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/{companyNo}/approval/admin")
public class ApprDocAdminController {
	
	@Autowired
	private ApprDocService apprDocService;
	
	@Autowired
	private CommCodeMapper commCodeMapper;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private PositionService positionService;
	
	
	/**
	 * 전자결재 환경설정 - 부재/위임 설정
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("/setting")
	public String selectAbsenceList(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		return "gw:appr/absenceList";
	}
	
	/**
	 * 전자결재 환경설정 Form UI
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("/insertAbsence")
	public String insertAbsence(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		return "gw:appr/absenceForm";
	}
	
	/**
	 * 결재 양식 전체 조회 
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("docFormList")
	public String selectDocFormList(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		List<DocFormVO> docForms = apprDocService.selectListAllDocForm();
		model.addAttribute("docForms", docForms);
		
		return "gw:admin/approval/docFormList";
	}
	
	
	/**
	 * 새 결재 양식 등록 폼으로 이동
	 * 
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("newDocAdd/{docFolderNo}")
	public String insertDocFormUI(
		@PathVariable("companyNo")String companyNo,
		@PathVariable("docFolderNo") String docFolderNo,
		@RequestParam("title")String docFolderName,
		Model model
	) {
		model.addAttribute("companyNo", companyNo);
		model.addAttribute("docFolderNo", docFolderNo);
		model.addAttribute("docFolderName", docFolderName);
		
		
		
		return "gw:admin/approval/docFormForm";
	}
	
	@PostMapping("newDocAdd")
	@ResponseBody
	public ResponseEntity<String> insertDocForm(@RequestBody DocFormVO docForm) {
		
		try {
			String docFolderNo = docForm.getDocFolderNo();
			apprDocService.insertDocForm(docFolderNo, docForm); // DB 저장 처리
			return ResponseEntity.ok("success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
		}
	}
	

	/**
	 * 결재양식 수정
	 * !!!매퍼 수정 필요!!!!
	 * @param companyNo
	 * @param req
	 * @return 
	 */
	@PostMapping("updateDoc")
	public ResponseEntity<Map<String, Object>> updateDocForm(
		@PathVariable("companyNo")String companyNo,
		@RequestBody DocFormVO req
	) {
		log.info("req : {}", req);
		boolean result = apprDocService.updateDocForm(req);
		
		// JSON 응답을 위한 Map 생성
	    Map<String, Object> response = new HashMap<>();

	    // 두 개의 수정 작업이 모두 성공했을 때
	    if (result) {
	        response.put("success", true);
	        response.put("message", "문서가 성공적으로 업데이트되었습니다.");
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("success", false);
	        response.put("message", "문서 업데이트에 실패했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	/**
	 * 결재 문서 관리
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("docmangement")
	public String selectApprDocList(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		return "gw:admin/approval/apprDocList";
	}
	
	/**
	 * 폴더 내 문서 목록 조회 ()
	 * @param docFolderNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("folderDoc")
	public List<DocFormVO> selectFolderDoc(
			@RequestParam("docFolderNo")String docFolderNo
	) {
		List<DocFormVO> folderDocList = apprDocService.selectDocument(docFolderNo);
		return folderDocList;
	}
	/**
	 * 새 양식 폴더 추가
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@PostMapping("insertFolder")
	public String insertFolder(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		return " ";
	}
	
	// 결재선
	
	/**
	 * 자동결재선 페이지 로드 - 결재선 목록
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("apprLineAutoList")
	public String selectListApprLineAuto(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		// 모든 결재선 기본정보 + 상세정보 포함 조회
		List<DocFormVO> lineList = apprDocService.selectListAllAutoLine();
	    // 같은 aprvlLineNo를 가진 항목을 하나로 묶기 위한 Map
	    Map<String, List<AprvlLineAutoVO>> lineMap = new HashMap<>();

	    for (DocFormVO line : lineList) {
	        String aprvlLineNo = line.getAprvlLineNo();

	     // 해당 결재선 번호가 없으면 리스트 생성
	        if (!lineMap.containsKey(aprvlLineNo)) {
	            lineMap.put(aprvlLineNo, new ArrayList<>());
	        }

	        // 결재자 목록 추가 (리스트에 개별적으로 추가)
	        List<AprvlLineAutoVO> existingList = lineMap.get(aprvlLineNo);
	        for (AprvlLineAutoVO detail : line.getAprvlLineAuto()) {
	            existingList.add(detail);
	        }	    
        }
	    
	    model.addAttribute("lineList", lineList);
	    model.addAttribute("lineMap", lineMap);
	    
	    log.info("lineList : {}", lineList);
	    log.info("lineMap : {}", lineMap);
		
		//model.addAttribute("lineList", aprvlLineAutoList);
		return "gw:admin/approval/apprLineAutoList";
		
	}

	/**
	 * 자동결재선 추가UI 이동
	 * @param companyNo
	 * @param model
	 * @return
	 */
	@GetMapping("apprLineAutoAdd")
	public String insertApprLineAutoFormUI(
		@PathVariable("companyNo")String companyNo
		, Model model
	) {
		model.addAttribute("companyNo", companyNo);
		
		List<DocFormVO> apprDocList = apprDocService.selectListAllDocForm();
		model.addAttribute("apprDocList", apprDocList);
		
		return "gw:admin/approval/apprLineAutoForm";
		
	}
	
	@GetMapping(value="selectType", produces="application/json")
	@ResponseBody
	public Map<String, Object> selectType(){
		Map<String, Object> type = new HashMap<>();
		
		// 결재선 타입 조회
		List<CommCodeVO> apprLineType = commCodeMapper.selectApprLineType();
		type.put("apprLineType", apprLineType);
		
		// 결재선 부서 조회
		List<DepartmentVO> department = departmentService.selectListAllDepartment();
		type.put("department", department);
		
		// 결재선 직위 조회
		List<PositionVO> position = positionService.selectPositionList();
		type.put("position", position);
		
		return type;
	}
	
	// 새 자동결재선 생성
	@PostMapping("apprLineAutoInsert")
	@ResponseBody
	public ResponseEntity<?> insertApprLineAuto(
		@RequestBody List<AprvlLineAutoVO> approvalLines
	) {
		if (approvalLines.isEmpty()) {
	        return ResponseEntity.badRequest().body("결재선 데이터가 없습니다.");
	    }
	    
	    try {
	        apprDocService.insertAutoLine(approvalLines);
	        return ResponseEntity.ok("저장 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패: " + e.getMessage());
	    }
	}
	
	//자동결재선 상세 조회
	@GetMapping("apprLineAutoDetail/{docFrmNo}")
	public String selectApprLineAutoDetail(
		@PathVariable("companyNo")String companyNo,
		Model model,
		@PathVariable("docFrmNo") String docFrmNo
	) throws JsonProcessingException {
		log.info("docFrmNo:{}",docFrmNo);
		List<DocFormVO> aprvlLineList = apprDocService.selectDetailAutoLine(docFrmNo);
		
		model.addAttribute("companyNo", companyNo);
		model.addAttribute("lines", aprvlLineList);
		
		List<AprvlLineAutoVO> line = new ArrayList<AprvlLineAutoVO>();
		for(DocFormVO aprvlLine : aprvlLineList ) {
			line.addAll(aprvlLine.getAprvlLineAuto()); 
		}
		
		log.info("aprvlLineList : {}", aprvlLineList);
		ObjectMapper mapper = new ObjectMapper(); 
		String jsonString = mapper.writeValueAsString(line);
		
		log.info("jsonString : {}", jsonString);
		model.addAttribute("json", jsonString);
		
		return "gw:admin/approval/apprLineAutoDetail";
	}
	
	/**
	 * 결재선 수정
	 * @param aprvlLineNo
	 * @param list
	 * @return
	 */
	@PutMapping("apprLineAutoUpdate/{aprvlLineNo}")
	@ResponseBody
	public ResponseEntity<?> updateApprLineAuto(
		@PathVariable String aprvlLineNo,
		@RequestBody List<AprvlLineAutoVO> list
	){
		log.info("aprvlLineNo : {}", aprvlLineNo);
		apprDocService.updateAutoLine(aprvlLineNo,list);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 결재선 삭제
	 * @param aprvlLineNo
	 * @return
	 */
	@DeleteMapping("apprLineAutoDelete/{aprvlLineNo}")
	@ResponseBody
	public ResponseEntity<?> deleteApprLineAuto(
		@PathVariable String aprvlLineNo
	){
		apprDocService.deleteAutoLine(aprvlLineNo);
		return ResponseEntity.ok().build();
	}
	
}
