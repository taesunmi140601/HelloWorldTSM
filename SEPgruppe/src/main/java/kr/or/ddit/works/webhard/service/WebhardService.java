package kr.or.ddit.works.webhard.service;
/**
 * 웹하드 서비스 임플
 * @author KMJ
 * @since 2025. 3. 26.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 26.     	KMJ	          최초 생성
 *
 * </pre>
 */

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.Drive;

import kr.or.ddit.works.webhard.vo.GoogleDriveFileVO;
import kr.or.ddit.works.webhard.vo.WebhardVO;

public interface WebhardService {
//	List<GoogleDriveFileVO> getDriveFiles(); //내 구글 드라이브 자료 조회
	
	List<WebhardVO> getFileList();
	
	String uploadWebhard(WebhardVO webhard, MultipartFile file) throws IOException;

	ResponseEntity<InputStreamResource> downloadFile(String Id);

	WebhardVO getWebhardVOByFileId(String fileId);

	void deleteFile(String fileId);

	Map<String, Object> getDriveFiles(String pageToken);

	List<WebhardVO> searchFilesByName(String keyword);

}
