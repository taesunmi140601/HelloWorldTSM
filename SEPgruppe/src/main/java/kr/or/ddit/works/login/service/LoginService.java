package kr.or.ddit.works.login.service;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.login.vo.AllUserVO;

public interface LoginService {
	
	public CompaniesVO findAccount(CompaniesVO company);
	
	public boolean updateCompany(CompaniesVO company);
}
