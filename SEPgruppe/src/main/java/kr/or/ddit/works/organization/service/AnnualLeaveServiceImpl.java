package kr.or.ddit.works.organization.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.works.approval.vo.AprvlDocVO;
import kr.or.ddit.works.mybatis.mappers.AnnualLeaveMapper;
import kr.or.ddit.works.organization.vo.AnnualLeaveVO;

/**
 * 
 * @author JSW
 * @since 2025. 3. 19.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 19.     	JSW	          최초 생성
 *
 * </pre>
 */
@Service
public class AnnualLeaveServiceImpl implements AnnualLeaveService {
	
	@Autowired
	private AnnualLeaveMapper mapper;
	
	
	@Override
	public List<AnnualLeaveVO> selectAnnual(String empId) {
		List<AnnualLeaveVO> annualList = mapper.selectAnnual(empId);

	    // 1. 사용 연차: 결재 완료된 연차 신청서 합산
	    List<AprvlDocVO> approvedDocs = getApprovedAnnualDocs(empId);
	    double totalUsed = approvedDocs.stream()
	        .mapToDouble(doc -> Double.parseDouble(doc.getUsedLeave()))
	        .sum();

	    // 2. 발생 연차: 결재 중인 연차 신청서 합산 (IN_PROGRESS, SUBMITTED)
	    List<AprvlDocVO> pendingDocs = getPendingAnnualDocs(empId);
	    double totalPending = pendingDocs.stream()
	        .mapToDouble(doc -> Double.parseDouble(doc.getUsedLeave()))
	        .sum();

	    // annualList는 보통 1건만 있다고 가정
	    if (!annualList.isEmpty()) {
	        AnnualLeaveVO annual = annualList.get(0);
	        annual.setUsedLeave(totalUsed); // 사용 연차
	        annual.setRemainingLeave(annual.getTotalLeave() - totalUsed); // 잔여 연차
	        annual.setCreatedLeave(totalPending); // 기안 발생 연차 (추가 필드 필요)
	    }

	    return annualList; 
	}
	
	@Override
	public int updateUsedAnnualLeave(String empId, double usedLeave) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("empId", empId);
	    param.put("usedLeave", usedLeave);
	    return mapper.updateUsedAnnualLeave(param);
	}

	@Override
	public int rollbackUsedAnnualLeave(String empId, double usedLeave) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("empId", empId);
	    param.put("usedLeave", usedLeave);
	    return mapper.rollbackUsedAnnualLeave(param);
	}
	
	@Override
	public List<AprvlDocVO> getApprovedAnnualDocs(String empId) {
		List<AprvlDocVO> list = mapper.selectApprovedAnnualDocs(empId);
	    extractLeaveInfo(list);
		return list;
	}

	@Override
	public List<AprvlDocVO> getPendingAnnualDocs(String empId) {
		List<AprvlDocVO> list = mapper.selectPendingAnnualDocs(empId);
	    extractLeaveInfo(list);
		return list;
	}
	
	private void extractLeaveInfo(List<AprvlDocVO> docList) {
	    for (AprvlDocVO doc : docList) {
	        String html = doc.getAprvlDocContents();
	        if (html == null || html.isBlank()) continue;

	        try {
	            Document jsoupDoc = Jsoup.parse(html);

	            String startDate = jsoupDoc.select("#vacDate span").get(0).text();
	            String endDate = jsoupDoc.select("#vacDate span").get(1).text();
	            String usedLeave = jsoupDoc.select("td:contains(연차 일수) + td span").text();

	            doc.setLeaveStartDate(startDate);
	            doc.setLeaveEndDate(endDate);
	            doc.setUsedLeave(usedLeave);

	        } catch (Exception e) {
	            e.printStackTrace(); // 혹은 로그 처리
	        }
	    }
	}

}
