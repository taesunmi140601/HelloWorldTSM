package kr.or.ddit.works.organization.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.PaginationRenderer;
import kr.or.ddit.paging.SimpleCondition;
import kr.or.ddit.works.organization.service.EmployeeService;
import kr.or.ddit.works.organization.service.PositionService;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import kr.or.ddit.works.organization.vo.PositionVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 인사관리 컨트롤러
 *
 * @author JYS
 * @since 2025. 3. 13.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 13.     	JYS	          최초 생성
 *
 * </pre>
 */
@Slf4j
@Controller
@RequestMapping("/{companyNo}/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@Autowired
	private PaginationRenderer paginationRenderer;
	
	@Autowired
	private PositionService positionService;

    /** 관리자 - 전사 인사정보 조회 */
    /** 권한을 부여받은 자 - 각 부서 사원 인사정보 조회 */
    @GetMapping("/admin/list")
    public String selectListAllEmployee(
    	@PathVariable("companyNo") String companyNo
    	, @RequestParam(defaultValue = "1") int page
    	, @ModelAttribute SimpleCondition simpleCondition
    	, Model model
    ) {
    	PaginationInfo<OrganizationVO> paging = new PaginationInfo<>(10, 2);
        paging.setCurrentPage(page);
        paging.setSimpleCondition(simpleCondition);

        PaginationInfo<OrganizationVO> pageData = service.getAllEmployees(companyNo, paging);
        List<PositionVO> positionList = positionService.selectPositionList();
        
        model.addAttribute("pageData", pageData);
        model.addAttribute("simpleCondition", simpleCondition);
        model.addAttribute("positionList", positionList);

        return "gw:admin/employee/employeeList";
    }

    /** 관리자 - 상세 인사정보 조회 */
    /** 권한을 부여받은 자 - 각 부서 사원 인사정보 조회 */
    @GetMapping("/{empNo}")
    public String selectEmployee(@PathVariable("companyNo") String companyNo, @PathVariable("empNo") String empNo) {
        return "gw:employee/employeeDetail";
    }

    @GetMapping("/departments")
    @ResponseBody
    public List<DepartmentVO> getDepartments(@PathVariable("companyNo") String companyNo) {
        return service.selectDepartments(companyNo);
    }

    /** 관리자 - 인사 정보 등록 폼 */
//    @GetMapping("/new")
//    public String insertEmployeeFormUI(@PathVariable("companyNo") String companyNo) {
//        return "gw:admin/employee/employeeForm";
//    }

    /** 관리자 - 인사 정보 등록 */
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<?> insertEmployee(
        @PathVariable("companyNo") String companyNo,
        @ModelAttribute EmployeeVO member
    ) {
        member.setCompanyNo(companyNo);
        int result = service.insertEmployee(member);

        return result > 0 ? ResponseEntity.ok().build()
                          : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /** 관리자 - 인사 정보 수정 폼 */
    @GetMapping("/{empNo}/edit")
    public String updateEmployeeFormUI(@PathVariable("companyNo") String companyNo, @PathVariable("empNo") String empNo) {
        return "gw:employee/employeeEdit";
    }

    /** 관리자 - 인사 정보 수정 */
    @PutMapping("/{empNo}/edit")
    public String updateEmployee(@PathVariable("companyNo") String companyNo, @PathVariable("empNo") String empNo) {
        return "redirect:/" + companyNo + "/employee/" + empNo;
    }

    /** 관리자 - 인사 정보 삭제(퇴사 처리) (비활성화) */
    @PutMapping("/{empNo}/deactivate")
    public String deactivateEmployee(@PathVariable("companyNo") String companyNo, @PathVariable("empNo") String empNo) {
        // 삭제 처리 로직 추가 (비활성화 처리)
        return "redirect:/" + companyNo + "/employee";
    }

    /** 관리자 - 인사 관리 권한 설정 */

    /** 사원 - 내 인사 정보 조회 */
    @GetMapping("/myEmployee")
    public String selectMyEmployee(
    		@PathVariable("companyNo") String companyNo
    		, Authentication authentication
        	, Model model
    ) {
    	EmployeeVO member = service.selectEmployee(authentication.getName());
    	model.addAttribute("member", member);
    	return "gw:employee/myEmployee";
    }

    /** 사원 마이페이지 */
    @GetMapping("/mypage")
    public String selectMyPage(
    	@PathVariable("companyNo") String companyNo
    	, Authentication authentication
    	, Model model
    ) {

		EmployeeVO member = service.selectEmployee(authentication.getName()); // 추가함
		model.addAttribute("member", member);
    	return "gw:employee/myPage";
    }

	/** 사원 마이페이지 정보 수정 */
	@PostMapping("/mypage/edit")
	public String updateEmployee(
		Authentication authentication
		, @PathVariable("companyNo") String companyNo
		, @ModelAttribute("member") EmployeeVO member
		, HttpSession session
	) {
		member.setEmpId(authentication.getName());
		service.updateEmployee(member);
		session.setAttribute("authMember", service.selectEmployee(member.getEmpId()));

		return "redirect:/{companyNo}/employee/mypage";
	}

	/** 사원 마이페이지 프로필 사진 삭제 */
	@PostMapping("/deleteImg")
	public void deleteImage(@RequestParam("empId") String empId) {
		log.info("이미지삭제 실행 ==================");
		service.deleteImage(empId);
	}

	@PostMapping("/mypage/verifyPassword")
	public ResponseEntity<Object> verifyPassword(
			Authentication authentication
			, @RequestBody EmployeeVO emp
		) {
		System.out.println("======================= 검증중 =======================");
	    String empId = authentication.getName();

	    boolean checkPw = service.authenticateMember(empId, emp.getEmpPw()); // 비밀번호 검증 호출

	        // 비밀번호 인증 로직 호출
	    if(checkPw)
	        return ResponseEntity.ok().build(); // 인증 성공
	    else
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패
	}

	@PutMapping("/admin/bulkUpdate")
	@ResponseBody
	public ResponseEntity<?> bulkUpdate(
			@PathVariable("companyNo") String companyNo,
			@RequestBody Map<String, Object> payload
	) {
		 List<String> empIds = (List<String>) payload.get("empIds");
		    String fieldType = (String) payload.get("fieldType"); // "position" or "department"
		    String value = (String) payload.get("value");

		    int updatedCount = service.bulkUpdateEmployees(empIds, fieldType, value);

		    return updatedCount > 0 ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@DeleteMapping("/admin/delete")
	@ResponseBody
	public ResponseEntity<?> deleteEmployees(
	    @PathVariable("companyNo") String companyNo,
	    @RequestBody List<String> empIds
	) {
	    int deletedCount = service.deleteEmployees(empIds, companyNo);
	    return deletedCount > 0
	        ? ResponseEntity.ok().build()
	        : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@GetMapping("/admin/ajaxList")
	@ResponseBody
	public Map<String, Object> getAjaxEmployeeList(
	    @PathVariable("companyNo") String companyNo,
	    @RequestParam Map<String, String> param
	) {
	    int start = Integer.parseInt(param.get("start")); // 시작 인덱스
	    int length = Integer.parseInt(param.get("length")); // 페이지 크기
	    int page = (start / length) + 1;

	    String searchType = param.get("searchType");
	    String searchWord = param.get("searchWord");

	    log.info("🎯 searchType: {}", searchType);  // 이거 null이면 문제
	    log.info("🎯 searchWord: {}", searchWord);

	    SimpleCondition condition = new SimpleCondition();
	    condition.setSearchType(searchType);
	    condition.setSearchWord(searchWord);

	    PaginationInfo<OrganizationVO> paging = new PaginationInfo<>(length, 2);
	    paging.setCurrentPage(page);
	    paging.setSimpleCondition(condition);

	    paging = service.getAllEmployees(companyNo, paging);

	    Map<String, Object> result = new HashMap<>();
	    result.put("draw", Integer.parseInt(param.get("draw")));
	    result.put("recordsTotal", paging.getTotalRecord());
	    result.put("recordsFiltered", paging.getTotalRecord());
	    result.put("data", paging.getDataList());
	    return result;
	}

	// apache POI 엑셀로 다운
	@GetMapping("/admin/excelDownload")
	public void downloadExcel(
	    @PathVariable("companyNo") String companyNo,
	    @RequestParam(required = false) String searchType,
	    @RequestParam(required = false) String searchWord,
	    HttpServletResponse response
	) throws IOException {
	    // 1. 전체사원 및 검색 결과 가져오기
		PaginationInfo<OrganizationVO> paging = new PaginationInfo<>(Integer.MAX_VALUE, 1); // 모든 데이터
	    paging.setCurrentPage(1);

	    SimpleCondition condition = new SimpleCondition();
	    condition.setSearchType(searchType);
	    condition.setSearchWord(searchWord);
	    paging.setSimpleCondition(condition);

	    paging = service.getAllEmployees(companyNo, paging);
	    List<OrganizationVO> data = paging.getDataList();

	    // 엑셀 파일 생성
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("사원 리스트");

	    // 헤더
	    Row headerRow = sheet.createRow(0);
	    String[] headers = {"사번", "이름", "부서", "직책", "이메일"};
	    for (int i = 0; i < headers.length; i++) {
	        Cell cell = headerRow.createCell(i);
	        cell.setCellValue(headers[i]);
	    }

	    // 데이터
	    for (int i = 0; i < data.size(); i++) {
	        OrganizationVO emp = data.get(i);
	        Row row = sheet.createRow(i + 1);
	        row.createCell(0).setCellValue(emp.getEmpNo());
	        row.createCell(1).setCellValue(emp.getEmpNm());
	        row.createCell(2).setCellValue(emp.getDeptName());
	        row.createCell(3).setCellValue(emp.getPositionName());
	        row.createCell(4).setCellValue(emp.getEmpEmail());
	    }

	    // 동적 파일명 생성
	    String fileName = companyNo;
	    if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchWord)) {
	        switch (searchType) {
	            case "empNm":
	                fileName += "_이름검색_" + searchWord;
	                break;
	            case "deptName":
	                fileName += "_부서검색_" + searchWord;
	                break;
	            case "positionName":
	                fileName += "_직책검색_" + searchWord;
	                break;
	        }
	    }

	    // 날짜 추가
	    String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
	    fileName += "_" + today + ".xlsx";

	    // 파일명 인코딩 (한글 깨짐 방지)
	    String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
	    String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;

	    // 응답 설정
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", contentDisposition);


	    // 파일 출력
	    workbook.write(response.getOutputStream());
	    workbook.close();
	}
}
