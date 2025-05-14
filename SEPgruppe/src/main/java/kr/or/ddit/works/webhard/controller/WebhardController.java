package kr.or.ddit.works.webhard.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import kr.or.ddit.works.organization.vo.OrganizationVO;
import kr.or.ddit.works.webhard.config.WebhardConfig;
import kr.or.ddit.works.webhard.service.WebhardService;
import kr.or.ddit.works.webhard.service.WebhardServiceImpl;
import kr.or.ddit.works.webhard.vo.GoogleDriveFileVO;
import kr.or.ddit.works.webhard.vo.WebhardVO;

/**
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
@Controller
@RequestMapping("{companyNo}/webhard")
public class WebhardController {
	
	@Autowired
	private WebhardService webhardService;
	
	 
//	 @GetMapping("")
//    public String listDriveFiles(@PathVariable("companyNo") String companyNo, 
//    		Model model) {
//        List<GoogleDriveFileVO> files = webhardService.getDriveFiles();
//        model.addAttribute("companyNo", companyNo);
//        model.addAttribute("files", files);
//
//        return "gw:webhard/webhardList";
	@GetMapping("")
	public String listDriveFiles(@PathVariable("companyNo") String companyNo, 
	                             @RequestParam(required = false) String pageToken, 
	                             Model model) {
	    Map<String, Object> result = webhardService.getDriveFiles(pageToken);

	    model.addAttribute("companyNo", companyNo);
	    model.addAttribute("files", result.get("files"));
	    model.addAttribute("nextPageToken", result.get("nextPageToken")); // ✅ JSP에서 활용할 nextPageToken 전달

	    return "gw:webhard/webhardList";
	}
	
	

	 
	 @PostMapping("/new")
	 public ResponseEntity<String> uploadFile(
	         @RequestParam("file") MultipartFile upload,
	         @RequestParam("empId") String empId) {

	     try {
	         WebhardVO webhard = new WebhardVO(); //폴더번호
	         webhard.setEmpId(empId); //사원 아이디
	         webhard.setWebhardNm(upload.getOriginalFilename()); //폴더명(파일이름)
	         webhard.setFileExtnNm(FilenameUtils.getExtension(upload.getOriginalFilename())); //파일 확장자
	         webhard.setFileSz(upload.getSize()); //파일 크기

	         // Google Drive + DB 저장
	         String fileId = webhardService.uploadWebhard(webhard, upload);

	         if (fileId != null) {
	             return ResponseEntity.ok("파일 업로드 성공 (파일 ID: " + fileId + ")");
	         } else {
	             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB 저장 실패");
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
	     }
	 }
	 
	 @GetMapping("/download/{fileId}/filename")
    public String getFileName(@PathVariable String fileId) {
        WebhardVO webhardVO = webhardService.getWebhardVOByFileId(fileId);
        if (webhardVO != null) {
            return webhardVO.getWebhardNm();
        }
        return "file"; // 기본 파일 이름
    } 
	 
	@GetMapping("/download/{fileId}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        return webhardService.downloadFile(fileId);
    }
	
	@RequestMapping("/delete/{fileId}")
	public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String fileId) {
	    try {
	        webhardService.deleteFile(fileId);
	        return ResponseEntity.ok(Collections.singletonMap("message", "파일 삭제 성공"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "파일 삭제 실패"));
	    }
	}
	
//	@GetMapping("/search")
//	public String searchFiles(@RequestParam("query") String query, Model model) {
//		List<WebhardVO> result = webhardService.searchFilesByName(query);
//		model.addAttribute("files", result);
//		return "gw:webhard/webhardList/searchResult";
//	}
	@GetMapping("/search")
    public ResponseEntity<List<WebhardVO>> search(
//		@PathVariable("companyNo") String companyNo,
        @RequestParam String keyword
        ) {
        List<WebhardVO> results = webhardService.searchFilesByName(keyword);
        return ResponseEntity.ok(results);
    }
	
}