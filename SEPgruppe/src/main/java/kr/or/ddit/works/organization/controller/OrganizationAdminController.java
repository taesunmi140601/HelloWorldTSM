package kr.or.ddit.works.organization.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.works.organization.fancytree.DepartmentNode;
import kr.or.ddit.works.organization.fancytree.EmployeeNode;
import kr.or.ddit.works.organization.service.OrganizationService;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;

/**
 * 조직도 관리 (organization) 컨트롤러
 * 
 * @author JYS
 * @since 2025. 3. 12.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 12.     	JYS	          최초 생성
 *
 * </pre>
 */
@Controller
@RequestMapping("/{companyNo}/organization/admin")
public class OrganizationAdminController {
	
	@Autowired
	private OrganizationService service;

    /** 조직도 전체 목록 조회 */
    @GetMapping("organizationList")
    public String selectListAllOrganization(@PathVariable("companyNo") String companyNo, Model model) {
    	model.addAttribute("companyNo", companyNo);
        return "gw:admin/organization/organizationList";
    }
    
    // 사원 목록 가져오기
    @GetMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EmployeeVO> employeeListForSelect(@PathVariable("companyNo") String companyNo) {
        return service.selectAllEmployees(companyNo);
    }
    
    /**
     * 최상위 부서 조회 (JSON)
     */
    @GetMapping(value="/parentDep", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DepartmentNode> parentDep(@PathVariable("companyNo") String companyNo) {
    	 return service.selectParentDep(companyNo).stream()
    	            .map(DepartmentNode::new)
    	            .collect(Collectors.toList());
    }
    
    /**
     * 하위 부서 또는 사원 조회 (JSON)
     */
    @GetMapping(value="/childeDep", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<?> childDep(
        @RequestParam String mode,
        @RequestParam String parent,
        @PathVariable("companyNo") String companyNo
    ) {
    	if("department".equals(mode)) {
            return service.selectChildDep(parent, companyNo).stream()
                    .map(DepartmentNode::new)
                    .collect(Collectors.toList()); // List<DepartmentNode>
        } else {
            return service.selectEmployee(parent, companyNo).stream()
                    .map(EmployeeNode::new)
                    .collect(Collectors.toList()); // List<EmployeeNode>
        }
    }
 // 기본 검색 (이름/ID)
    @GetMapping("search")
    public ResponseEntity<List<OrganizationVO>> search(
		@PathVariable("companyNo") String companyNo,
        @RequestParam String keyword,
        Model model
        ) {
        List<OrganizationVO> results = service.searchEmployees(keyword, companyNo);
        model.addAttribute("companyNo", companyNo);
        return ResponseEntity.ok(results);
    }

    // 부서별 검색
    @GetMapping("search/dept")
    public ResponseEntity<List<OrganizationVO>> searchByDepartment(
		@RequestParam("companyNo") String companyNo,
        @RequestParam String deptName,
        @RequestParam String keyword,
        Model model
    	) {
        List<OrganizationVO> results = service.searchByDepartment(deptName, keyword);
        model.addAttribute("companyNo", companyNo);
        return ResponseEntity.ok(results);
    }
    
    
    
    /** 조직도 내 사원 상세 조회 */
    @GetMapping("/{empId}/detail")
    public String selectOrganizationEmp(@PathVariable("companyNo") String companyNo, @PathVariable("empId") int empId) {
        return "gw:organization/organizationDetail";
    }
    
 
    
    
   
}
