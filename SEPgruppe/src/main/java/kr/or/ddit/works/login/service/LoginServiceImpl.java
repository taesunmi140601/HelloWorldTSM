package kr.or.ddit.works.login.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.works.company.vo.CompaniesVO;
import kr.or.ddit.works.login.vo.AllUserVO;
import kr.or.ddit.works.mybatis.mappers.LoginMapper;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Inject
	public LoginMapper mapper;
	
	@Override
	public CompaniesVO findAccount(CompaniesVO company) {
		
		return mapper.findAccount(company);
	}

	@Override
	public boolean updateCompany(CompaniesVO company) {
		return mapper.updateCompany(company) > 0;
	}
	
	
}
