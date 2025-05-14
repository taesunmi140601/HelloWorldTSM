package kr.or.ddit.works.organization.service;


import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.SimpleCondition;
import kr.or.ddit.security.CustomUserDetailService;
import kr.or.ddit.works.mybatis.mappers.EmployeeMapper;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper mapper;


    @Autowired
    private CustomUserDetailService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Value("#{fileInfo.attachFiles}")
	private String profileImageUrl;
	@Value("#{fileInfo.attachFiles}")
	private Resource profileImageRes;
	@Value("#{fileInfo.attachFiles}")
	private File profileImageFolder;


	// 프로필 사진
	public void profileImage(EmployeeVO member) {
		try {
			MultipartFile profileImage = member.getAttachFile();
			if(profileImage == null) return;

			String profileImg = member.getEmpImg();
			File destFile = new File(profileImageFolder, profileImg);
			profileImage.transferTo(destFile);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 변경된 정보로 새로운 인증 생성 (패스워드를 변경할 경우 새롭게 인증정보가 업데이트 되어야 함)
    private void createNewAuthentication() {
        Authentication beforeAuth = SecurityContextHolder.getContext().getAuthentication();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetails principal = userDetailService.loadUserByUsername(beforeAuth.getName());
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        Object details = beforeAuth.getDetails();

        // 새로운 인증 토큰 생성 (비밀번호는 null로 설정)
        UsernamePasswordAuthenticationToken newAuthentication =
                UsernamePasswordAuthenticationToken.authenticated(principal, null, authorities);

        newAuthentication.setDetails(details);

        context.setAuthentication(newAuthentication);
        SecurityContextHolder.setContext(context);
    }

    // 마이페이지 인증
    @Override
    public boolean authenticateMember(String empId, String empPw) {
    	System.out.println("=================================서비스 호출 ===============================");
        String realPw = mapper.selectEmployee(empId).getEmpPw();
        return passwordEncoder.matches(empPw, realPw);
    }

	// 마이페이지 정보 수정
	@Override
	public boolean updateEmployee(EmployeeVO member) {
	    // 패스워드 수정할 경우 암호화 하여 UPDATE
	    String plain = member.getEmpPw();

	    // 비밀번호가 비어 있지 않은 경우에만 암호화
	    if (plain != null && !plain.isEmpty()) {
	        String encoded = passwordEncoder.encode(plain);
	        member.setEmpPw(encoded);
	    } else {
	        // 비밀번호가 비어 있으면 기존 비밀번호 유지
	        member.setEmpPw(member.getEmpPw());
	    }

	    boolean result = mapper.updateEmployee(member) > 0;

	    if (result) {
	        profileImage(member);
	        // 정보변경 완료되면 새로운 인증 생성
	        createNewAuthentication();
	        return true;
	    } else {
	        return false;
	    }
	}
	
	// 사원(들) 부서 및 직위 수정
	@Override
	public int bulkUpdateEmployees(List<String> empIds, String fieldType, String value) {
		 if (CollectionUtils.isEmpty(empIds) || StringUtils.isBlank(fieldType) || StringUtils.isBlank(value)) {
		        return 0;
		    }

		    if (fieldType.equals("position")) {
		        return mapper.updateEmployeesPosition(empIds, value);
		    } else if (fieldType.equals("department")) {
		        return mapper.updateEmployeesDepartment(empIds, value);
		    }

		    return 0;
		}
	
	// 사원(들) 삭제
	@Override
	public int deleteEmployees(List<String> empIds, String companyNo) {
	    return mapper.deleteEmployees(empIds, companyNo);
	}
	
	// 마이페이지 정보 조회
	@Override
	public EmployeeVO selectEmployee(String empId) {
		return mapper.selectEmployee(empId);
	}

	// 마이페이지 이미지 삭제
	@Override
	public boolean deleteImage(String empId) {
		return mapper.deleteImage(empId) > 0;
	}
	
	// 전사 정보 리스트 조회(페이징)
	@Override
	public PaginationInfo<OrganizationVO> getAllEmployees(String companyNo, PaginationInfo<OrganizationVO> paging) {
		 SimpleCondition condition = paging.getSimpleCondition();
		
		 paging.setTotalRecord(mapper.countAllEmployees(companyNo, condition));

		 List<OrganizationVO> employees = mapper.selectAllEmployees(companyNo, paging, condition);
		 paging.setDataList(employees);

		 return paging;
}

	
	@Override
	public List<DepartmentVO> selectDepartments(String companyNo) {
		return mapper.selectDepartments(companyNo);
	}
	
	  @Override
	    public int insertEmployee(EmployeeVO member) {
	        // 자동 empNo 생성
	        String lastEmpNo = mapper.selectLastEmpNo(member.getCompanyNo());
	        String newEmpNo = (lastEmpNo == null) ? "E001"
	            : String.format("E%03d", Integer.parseInt(lastEmpNo.substring(1)) + 1);
	        member.setEmpNo(newEmpNo);

	        // 비밀번호 암호화
	        if (StringUtils.isNotBlank(member.getEmpPw())) {
	            member.setEmpPw(passwordEncoder.encode(member.getEmpPw()));
	        }
	        return mapper.insertEmployee(member);
	    }

	
}
