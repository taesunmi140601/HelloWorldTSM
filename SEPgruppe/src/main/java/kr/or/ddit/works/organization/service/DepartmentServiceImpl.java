package kr.or.ddit.works.organization.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.works.mybatis.mappers.DepartmentMapper;
import kr.or.ddit.works.mybatis.mappers.EmployeeMapper;
import kr.or.ddit.works.organization.vo.DepartmentVO;

/**
 * 
 * @author JSW
 * @since 2025. 3. 31.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 31.     	JSW	          최초 생성
 *
 * </pre>
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	private DepartmentMapper mapper;
	
	@Autowired
	private EmployeeMapper empMapper;
	
	@Override
	public List<DepartmentVO> selectListAllDepartment() {
		return mapper.selectListAllDepartment();
	}
	
	@Override
	public int addDepartment(DepartmentVO dept) {
		return mapper.insertDepartment(dept);
	}

	@Override
	public int deleteDepartment(String companyNo, String deptCd) {
		return mapper.deleteDepartment(companyNo, deptCd);
	}

	@Override
	public int updateDepartmentField(DepartmentVO dept) {
		// 1. 기존 부서장 조회
		String managerEmpId = mapper.selectManagerDeptCd(dept.getDeptCd(), dept.getCompanyNo());
		
		// 2. department 테이블 update
		int result = mapper.updateDepartmentField(dept);
		
		// 3. 기존 부서장과 새 부서장이 다르면 -> 기존 부서장의 dept_cd 제거
		 if (managerEmpId != null && !managerEmpId.equals(dept.getManagerEmpId())) {
		        empMapper.clearDeptCd(managerEmpId); // dept_cd = NULL
		 }
		
		// 4. 새 부서장이 있으면 그 사원에게 dept_cd 설정
	    if (dept.getManagerEmpId() != null && !dept.getManagerEmpId().isEmpty()) {
	        empMapper.updateDeptCd(dept.getManagerEmpId(), dept.getDeptCd());
	    }
	    
	    return result;
	}

	@Override
	public List<DepartmentVO> parseExcel(MultipartFile file, String companyNo) throws Exception {
	    List<DepartmentVO> list = new ArrayList<>();

	    try (InputStream is = file.getInputStream()) {
	        Workbook workbook = WorkbookFactory.create(is);
	        Sheet sheet = workbook.getSheetAt(0);

	        int startRowIndex = -1;

	        // "부서코드"라는 셀을 가진 행을 찾는다 (헤더 시작 지점)
	        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            for (Cell cell : row) {
	                cell.setCellType(CellType.STRING);
	                if ("부서코드".equals(cell.getStringCellValue().trim())) {
	                    startRowIndex = i + 1; //실제 데이터는 그 다음 행부터
	                    break;
	                }
	            }

	            if (startRowIndex != -1) break;
	        }

	        if (startRowIndex == -1) {
	            throw new IllegalArgumentException("엑셀 파일에서 '부서코드' 헤더를 찾을 수 없습니다.");
	        }

	        //본격적으로 데이터 읽기
	        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null || row.getCell(0) == null) continue;

	            DepartmentVO dept = new DepartmentVO();
	            dept.setDeptCd(getCellValue(row.getCell(0)));       // 부서코드
	            dept.setParentDeptCd(getCellValue(row.getCell(1))); // 상위부서코드
	            dept.setDeptName(getCellValue(row.getCell(3)));     // 부서명 (index 3: 정렬코드가 index 2)
	            dept.setManagerEmpId(getCellValue(row.getCell(4))); // 부서장
	            dept.setCreateAt(LocalDate.now().toString());
	            dept.setCompanyNo(companyNo);

	            if (dept.getDeptCd() != null && !dept.getDeptCd().isEmpty()) {
	                list.add(dept);
	            }
	        }
	    }

	    return list;
	}

	private String getCellValue(Cell cell) {
	    if (cell == null) return null;
	    cell.setCellType(CellType.STRING);
	    return cell.getStringCellValue().trim();
	}

	@Override
	public int bulkInsertDepartments(List<DepartmentVO> deptList) {
	    int count = 0;
	    for (DepartmentVO dept : deptList) {
	        try {
	            // 1. 부서코드가 이미 존재하는지 확인
	            DepartmentVO existing = mapper.selectDepartmentByCode(dept.getDeptCd(), dept.getCompanyNo());

	            if (existing != null) {
	                mapper.updateDepartment(dept);
	                dept.setStatus("수정됨");
	            } else {
	                mapper.insertDepartment(dept);
	                dept.setStatus("신규 등록");
	            }
	            count++;
	        } catch (Exception e) {
	            dept.setStatus("실패: " + e.getMessage());
	        }
	    }

	    return count;
	}
}
	
