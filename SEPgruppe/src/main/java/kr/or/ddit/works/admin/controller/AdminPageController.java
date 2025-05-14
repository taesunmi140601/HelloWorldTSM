package kr.or.ddit.works.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.mybatis.mappers.CompanyMapper;
import kr.or.ddit.works.mybatis.mappers.EmployeeMapper;
import kr.or.ddit.works.mybatis.mappers.SubscriptionMapper;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import kr.or.ddit.works.subscription.vo.SubscriptionsVO;

@Controller
@RequestMapping("{companyNo}/adminpage")
public class AdminPageController {
		
	@Autowired
	private EmployeeMapper mapper;
	@Autowired
	private CompanyMapper comMapper;
	
	@Autowired
	private SubscriptionMapper subMapper;
	
	@GetMapping
	public String adminPage(@PathVariable("companyNo") String companyNo, Model model) {
		int countEmp = mapper.countAllEmployees(companyNo, null);
		int countActiveEmp = mapper.countActiveEmployees(companyNo);

		CompaniesVO company = comMapper.selectCompanyNo(companyNo);
		
		
		
		SubscriptionsVO subStart = subMapper.selectSubscription(company.getContactId());
				
		model.addAttribute("countEmp", countEmp);
		model.addAttribute("countActiveEmp", countActiveEmp);
		model.addAttribute("company", company);
		model.addAttribute("subStart",subStart);
		return "gw:admin/admin/adminPage";
	}
}
