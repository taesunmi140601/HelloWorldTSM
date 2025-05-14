package kr.or.ddit.works.notice.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.paging.DefaultPaginationRenderer;
import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.PaginationRenderer;
import kr.or.ddit.paging.SimpleCondition;
import kr.or.ddit.security.RealUserWrapper;
import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.validate.UpdateGroup;
import kr.or.ddit.works.alarm.controller.GWAlarmController;
import kr.or.ddit.works.attachFile.service.AttachFileService;
import kr.or.ddit.works.attachFile.service.AttachFileServiceImpl;
import kr.or.ddit.works.attachFile.vo.AttachFileVO;
import kr.or.ddit.works.attachFile.vo.PostAttachFileVO;
import kr.or.ddit.works.mybatis.mappers.OrganizationMapper;
import kr.or.ddit.works.notice.service.NoticeService;
import kr.or.ddit.works.notice.vo.NoticeVO;
import kr.or.ddit.works.organization.vo.EmployeeVO;
import kr.or.ddit.works.organization.vo.DepartmentVO;
import kr.or.ddit.works.organization.vo.OrganizationVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 공지사항 컨트롤러
 *
 * @author JYS
 * @since 2025. 3. 14.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 14.     	JYS	          최초 생성
 *
 * </pre>
 */
@Slf4j
@Controller
@RequestMapping("/{companyNo}/notice")
public class NoticeController {

	private static final String MODELNAME = "notice";
    @Autowired
    private NoticeService service;

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private OrganizationMapper organiMapper;

    @Autowired
	private GWAlarmController gwAlarmController;

    long fileGroupNo = 3;

    // 첨부파일 업로드 공통 로직
    private List<AttachFileVO> handleFileUpload(List<MultipartFile> uploadFiles, NoticeVO notice) {

    	// 반환할 첨부파일 리스트 생성
        List<AttachFileVO> fileList = new ArrayList<>();

        // 업로드 파일 목록이 비어있지 않으면 처리
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            for (MultipartFile file : uploadFiles) {

            	// 첨부파일 VO 생성
                AttachFileVO attachFile = new AttachFileVO();

                attachFile.setFileGroupNo(fileGroupNo);  					// 파일 그룹번호 저장
                attachFile.setAttachFile(file);  							// 파일 정보 저장
                attachFile.setEmpId(notice.getEmpId());  					// 업로드한 회원 정보
                attachFile.setAttachFileDate(LocalDate.now().toString());   // 파일 업로드 날짜
                attachFile.setAttachFileStatus("Y");  						// 상태여부

                // 파일명이 존재하는 경우에만 리스트에 추가
                if (attachFile.getAttachFileName() != null) {
                    fileList.add(attachFile);
                }
            }
        }
        return fileList;
    }


    /** 공지사항 목록 조회 */
    @GetMapping("")
    public String selectListAllNotice(
        @PathVariable("companyNo") String companyNo
        // 키워드 검색 조건 (제목, 내용, 제목+내용)
        , @ModelAttribute("condition") SimpleCondition condition
        // 분류 선택 조건 (전체, 전사공지, 부서공지)
        , @RequestParam(name = "category", required = false, defaultValue = "all") String category
        // 페이징 처리
        , @RequestParam(name = "page", required = false, defaultValue = "1") int currentPage
        , Model model
        , Authentication authentication
        , HttpSession session
    ) {
    	// 시큐리티 로그인 계정 RoleName 가져오기
        RealUserWrapper userWrapper = (RealUserWrapper) authentication.getPrincipal();
        session.setAttribute("realUser", userWrapper.getRealUser());

        // 로그인 된 사용자의 부서코드 가져오기
        OrganizationVO member = organiMapper.selectOrganization(authentication.getName());
        String deptCd = member.getDeptCd();

        // 권한 설정
        String empId = authentication.getName();
        DepartmentVO advice = service.selectLogin(empId);

        // VO에 부서코드 + 회사코드 가져오기
        NoticeVO noticeVo = new NoticeVO();
        noticeVo.setDeptCd(deptCd);
        noticeVo.setCategory(category);
        noticeVo.setCompanyNo(companyNo);

        // 페이징 처리 + 분류선택 처리
        PaginationInfo<NoticeVO> paging = new PaginationInfo<>();
        paging.setCurrentPage(currentPage); 	// 페이징처리
        paging.setSimpleCondition(condition); 	// 키워드검색
        paging.setDetailCondition(noticeVo); 	// 분류선택

        // 서비스 실행
        List<NoticeVO> noticeList = service.selectAllNotice(paging, companyNo);

        // 조회된 데이터 개수 -> 페이징 처리를 위함
        int total_cnt = service.selectAllNoticeTotalRecord(paging);
        paging.setTotalRecord(total_cnt);

        // 페이징 처리 렌더링
        PaginationRenderer renderer = new DefaultPaginationRenderer();
        String pagingHTML = renderer.renderPagination(paging);

        // model 담기
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("companyNo", companyNo);
        model.addAttribute("category", category);
        model.addAttribute("member", member);
        model.addAttribute("advice", advice);

        return "gw:notice/noticeList";
    }

    /** 공지사항 상세 조회 */
    @GetMapping("/{noticeNo}")
    public String selectNoticeDetail(
        @PathVariable("companyNo") String companyNo
        , @PathVariable("noticeNo") int noticeNo
        , @ModelAttribute("member") NoticeVO member
        , Authentication authentication
        , Model model
    ) {
    	// 로그인한 사용자 ID 저장
    	String empId = authentication.getName();
    	member.setEmpId(empId);

    	// 조회 조건 객체 생성
    	NoticeVO condition = new NoticeVO();
    	condition.setNoticeNo(noticeNo);
    	condition.setCompanyNo(companyNo);

    	NoticeVO detailNotice = service.basicSelectAllWithCompany(condition);
    	log.info("detailNotice =========== {}", detailNotice);
    	if (detailNotice != null) {
    	    service.noticeViewCnt(noticeNo); // 조회수 증가
	    	model.addAttribute("detailNotice", detailNotice);
	        model.addAttribute("companyNo", companyNo);
	        model.addAttribute("member", member);
	        return "gw:notice/noticeDetail";
	    } else {
	        // 게시글 없거나 회사코드 불일치
	        return "redirect:/" + companyNo + "/notice?error=notfound";
	    }
    }

    /** 관리자 - 새 공지글 등록 폼 이동 */
    @GetMapping("new")
    public String insertNoticeFormUI(
        @PathVariable("companyNo") String companyNo
        , @ModelAttribute("member") NoticeVO member
        , Model model
        , Authentication authentication
    ) {
    	// 로그인한 사용자 ID 저장
    	String empId = authentication.getName();
    	member.setEmpId(empId);

    	// 임시저장한 글 개수
        int draftCnt = service.isDraftCnt(empId);
        // 임시저장한 글 내용
        List<NoticeVO> draftList = service.isDraftList(empId);

        // 임시저장한 글 목록을 클릭했을 때 불러오기
        if(!draftList.isEmpty()) {
        	NoticeVO selectDraft = draftList.get(0); // 첫 번째 임시저장 글
        	model.addAttribute("selectDraft", selectDraft);
        }

        // 권한 설정
        DepartmentVO advice = service.selectLogin(empId);

        // scope에 저장
        model.addAttribute("draftList", draftList);
        model.addAttribute("draftCnt", draftCnt);
        model.addAttribute("advice", advice);
        model.addAttribute("companyNo", companyNo);
        return "gw:notice/noticeForm";
    }

    /** 관리자 - 새 공지글 등록 */
    @PostMapping("new")
    public String insertNotice(
        @Validated(InsertGroup.class) @ModelAttribute(MODELNAME) NoticeVO notice,
        BindingResult errors,
        RedirectAttributes redirectAttributes,
        @PathVariable("companyNo") String companyNo,
        @RequestParam(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles,
        Model model
    ) {
        String url = null;

        if (!errors.hasErrors()) {
            int noticeNo = service.seqNotice();
            notice.setNoticeNo(noticeNo);

            // 파일 업로드 처리
            List<AttachFileVO> fileList = handleFileUpload(uploadFiles, notice);

            // 첨부파일이 있으면 같이 저장
            if (!fileList.isEmpty()) {
                Map<String, Object> fileMap = new HashMap<>();
                fileMap.put("fileList", fileList);
                notice.setFileGroupNo(fileList.get(0).getFileGroupNo()); // NOTICE테이블 FILE_GROUP_NO와 ATTACHFILE 테이블 FILE_GROUP_NO 매칭
                service.insertNoticeFile(fileMap);
            }

            // 기본 공지사항 저장 (첨부파일 제외)
            service.insertNotice(notice);

            // PPOST_ATTACH_FILE 매핑
            if (!fileList.isEmpty()) {
                Map<String, Object> postAttachMap = new HashMap<>();
                postAttachMap.put("fileList", fileList);
                postAttachMap.put("noticeNo", notice.getNoticeNo());
                service.insertFileNotice(postAttachMap);
            }

            url = "redirect:/" + companyNo + "/notice";
        } else {
            String errorName = BindingResult.MODEL_KEY_PREFIX + MODELNAME;
            redirectAttributes.addFlashAttribute(MODELNAME, notice);
            redirectAttributes.addFlashAttribute(errorName, errors);
            url = "redirect:/" + companyNo + "/notice/new";
        }
        List<EmployeeVO> allEmp = organiMapper.selectAllEmployees(companyNo);

        for (EmployeeVO emp : allEmp) {
            String empId = emp.getEmpId(); // EmployeeVO에 empId가 있다고 가정
            gwAlarmController.sendNoticeAlarm(notice, empId);
        }
        model.addAttribute("companyNo", companyNo);
        return url;
    }

    /** 관리자 - 공지사항 수정 폼으로 이동 */
    @GetMapping("{noticeNo}/editForm")
    public String updateFormUI(
    	@PathVariable("companyNo") String companyNo
    	, @PathVariable("noticeNo") int noticeNo
    	, Model model
        , @ModelAttribute("member") NoticeVO member
        , Authentication authentication

    ) {
    	// 로그인한 사용자 ID 저장
    	String empId = authentication.getName();
    	member.setEmpId(empId);

        int draftCnt = service.isDraftCnt(empId); // 임시저장한 글 개수

        List<NoticeVO> draftList = service.isDraftList(empId); // 임시저장한 글 내용

        NoticeVO condition = new NoticeVO();
        condition.setNoticeNo(noticeNo);
        condition.setCompanyNo(companyNo);
        NoticeVO selectNotice = service.basicSelectAllWithCompany(condition);

    	model.addAttribute("companyNo", companyNo);
    	model.addAttribute("selectNotice", selectNotice);
    	model.addAttribute("draftCnt", draftCnt);
    	model.addAttribute("draftList", draftList);

    	return "gw:notice/noticeEdit";
    }

    /** 관리자 - 공지글 수정 */
    @PostMapping("{noticeNo}/edit")
    public String updateNotice(
    	@Validated(UpdateGroup.class) @ModelAttribute(MODELNAME) NoticeVO notice
    	, BindingResult errors
    	, RedirectAttributes redirectAttributes
        , @PathVariable("companyNo") String companyNo
        , @PathVariable("noticeNo") int noticeNo
		, Authentication authentication
	    , @RequestParam(value = "attachFileNo", required = false) String attachFileNo  // 삭제할 파일
	    , @RequestParam(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles // 업로드 파일
        , Model model
    ) {
    	// 로그인한 사용자정보
    	String empId = authentication.getName();
    	notice.setEmpId(empId);
    	notice.setNoticeNo(noticeNo);

    	String url = null;

    	if(!errors.hasErrors()) {

    		// 기본 공지사항 update
    		service.updateNotice(notice);

    		// deleteFileNos가 존재하면 삭제 처리
    		if(attachFileNo != null && !attachFileNo.isEmpty()) {
    			String[] fileNosToDelete = attachFileNo.split(",");
    			for(String fileNoToDelete : fileNosToDelete) {
    				service.deleteFile(fileNoToDelete);
    			}
    		}

    		// 파일 업로드 처리
    		if(uploadFiles != null && !uploadFiles.isEmpty()) {
    			List<AttachFileVO> fileList = handleFileUpload(uploadFiles, notice);

    			// 새 파일이 있으면 ATTACH_FILE 테이블에 저장
    			if(!fileList.isEmpty()) {
    				Map<String, Object> fileMap = new HashMap<>();
    				fileMap.put("fileList", fileList);
    				notice.setFileGroupNo(fileList.get(0).getFileGroupNo()); // NOTICE테이블 FILE_GROUP_NO와 ATTACHFILE 테이블 FILE_GROUP_NO 매칭
    				service.insertNoticeFile(fileMap);
    			}

    			// POST_ATTACH_FILE 테이블에 파일과 공지사항 매핑
    			if(!fileList.isEmpty()) {
                    Map<String, Object> postAttachMap = new HashMap<>();
                    postAttachMap.put("fileList", fileList);
                    postAttachMap.put("noticeNo", notice.getNoticeNo());
                    service.insertFileNotice(postAttachMap);
    			}
    		}
    		url = "redirect:/" + companyNo + "/notice/" + noticeNo;
    	}else {
    		redirectAttributes.addFlashAttribute(MODELNAME, notice);
    		String errorName = BindingResult.MODEL_KEY_PREFIX + MODELNAME;
    		redirectAttributes.addFlashAttribute(errorName, errors);
    		url = "redirect:/" + companyNo + "/notice/" + noticeNo + "/editForm";
    	}
        model.addAttribute("companyNo", companyNo);

        return url;
    }

    /** 공지사항 수정, 삭제 검증을 위한 메서드 */
    @GetMapping("/{noticeNo}/select")
    public ResponseEntity<NoticeVO> getNotice(
    		@PathVariable("companyNo") String companyNo
    		, @PathVariable("noticeNo") int noticeNo
    ) {
    	 NoticeVO condition = new NoticeVO();
    	 condition.setNoticeNo(noticeNo);
    	 condition.setCompanyNo(companyNo);

    	 NoticeVO notice = service.basicSelectAllWithCompany(condition);

    	 return ResponseEntity.ok(notice);
    }

    /** 관리자 - 공지글 삭제 */
    @PostMapping("/delete")
    public String deleteNotice(
    		@PathVariable("companyNo") String companyNo
    		, @RequestParam("noticeNo") String noticeNoStr
    		, Authentication authentication
    		, Model model
    	) {

    	// 로그인된 사용자 정보
    	String empId = authentication.getName();
    	NoticeVO member = new NoticeVO();
    	member.setEmpId(empId);

    	// input태그를 통해 받는 String타입 noticeNo를 int형으로 변환하여 배열에 저장
    	String[] noticeNoArray = noticeNoStr.split(",");
    	int[] noticeNo = new int[noticeNoArray.length];
    	for(int i=0; i<noticeNoArray.length; i++) {
    		noticeNo[i] = Integer.parseInt(noticeNoArray[i]);
    	}

        // 삭제하려는 사람의 ID와 작성자의 ID 비교
        for(int notice : noticeNo) {
        	 NoticeVO condition = new NoticeVO();
             condition.setNoticeNo(notice);
             condition.setCompanyNo(companyNo);
             NoticeVO noticeVo = service.basicSelectAllWithCompany(condition);

        	if(noticeVo == null || !noticeVo.getEmpId().equals(empId)) {
        		break;
        	}else {
            	Map<String, Object> params = new HashMap<>();
        		// 선택된 noticeNo 순환 -> Map에 저장
        		params.put("noticeNo", noticeNo);
        		params.put("empId", member.getEmpId());
        		service.deleteNotice(params);
        	}
        }
    	model.addAttribute("companyNo", companyNo);

    	return "redirect:/" + companyNo + "/notice";
    }

    // 공지사항 리스트 엑셀 다운로드
    @GetMapping("/excelDownload")
    public void noticeExcel(
    	@PathVariable("companyNo") String companyNo
    	, @ModelAttribute("condition") SimpleCondition condition
    	, @RequestParam(name = "category", required = false, defaultValue = "all") String category
    	, HttpServletResponse response
    	, Authentication authentication
    ) throws IOException {

        // 로그인 된 사용자의 부서코드 + 회사코드 가져오기
        OrganizationVO member = organiMapper.selectOrganization(authentication.getName());
        String deptCd = member.getDeptCd();

        // VO에 부서코드 설정
        NoticeVO noticeVo = new NoticeVO();
        noticeVo.setDeptCd(deptCd);
        noticeVo.setCategory(category);
        noticeVo.setCompanyNo(companyNo);

        // 공지사항 목록 조회
        PaginationInfo<NoticeVO> paging = new PaginationInfo<>();
        paging.setSimpleCondition(condition);
        paging.setDetailCondition(noticeVo);
        List<NoticeVO> noticeList = service.selectAllNotice(paging, companyNo);

        // 엑셀 파일 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("공지사항 목록");
        Row headerRow = sheet.createRow(0);
        String[] columns = {"번호", "제목", "내용", "작성자", "작성일", "조회수"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }
        // HTML 태그 제거 (CKEditor에서 저장된 태그 제거)

        // 데이터 채우기
        int rowIdx = 1;
        for (NoticeVO notice : noticeList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(notice.getRnum()); // 번호
            row.createCell(1).setCellValue(notice.getNoticeTitle()); // 제목
            String cleanContent = removeHtmlTags(notice.getNoticeContent());
            row.createCell(2).setCellValue(cleanContent); // 내용
            row.createCell(3).setCellValue(notice.getEmpNm()); // 작성자
            row.createCell(4).setCellValue(notice.getNoticeCreatedAt().toString()); // 작성일
            row.createCell(5).setCellValue(notice.getNoticeViewCount()); // 조회수
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=notice_list.xlsx");

        OutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();

    }
    // HTML 태그 제거 메서드 추가
    private String removeHtmlTags(String content) {
        if (content == null) return "";
        return content.replaceAll("<[^>]*>", ""); // 정규식으로 HTML 태그 제거
    }

    // 파일 다운로드 처리
    @GetMapping("/{noticeNo}/download")
    public ResponseEntity<Resource> downloadFile(
    	@RequestParam("attachFileNo") String attachFileNo
    	, @PathVariable("companyNo") String companyNo
		, @PathVariable("noticeNo") int noticeNo
    ) throws IOException{

    	// 파일 정보 조회
    	AttachFileVO file = service.selectByFileNo(attachFileNo);
    	log.info("========================파일 {} ",file);
    	log.info("attachFileNo = {}", attachFileNo);

    	// 파일 리소스 가져오기
    	Resource resource = attachFileService.getAttachFileDownload(file);
    	log.info("====");
    	log.info("resource = {}", resource);

    	if(resource == null) {
    		return ResponseEntity.notFound().build();
    	}

    	// 파일 인코딩
    	String encodedFileName = URLEncoder.encode(file.getAttachOrgFileName(),"UTF-8").replaceAll("\\+", "%20");

    	// ResponseEntity로 리턴
    	return ResponseEntity.ok()
    			.contentType(MediaType.APPLICATION_OCTET_STREAM)
    			.contentLength(file.getAttachFileSize())
    			.header("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"")
    			.body(resource);

    }
}
