package kr.or.ddit.works.webhard.service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.common.reflection.qual.GetClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;

import kr.or.ddit.works.mybatis.mappers.WebhardMapper;
import kr.or.ddit.works.webhard.config.WebhardConfig;
import kr.or.ddit.works.webhard.vo.GoogleDriveFileVO;
import kr.or.ddit.works.webhard.vo.WebhardVO;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;


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
@Service
public class WebhardServiceImpl implements WebhardService {
	
	@Autowired
	private WebhardMapper webhardMapper;
	

	
//	@Override
//    public List<GoogleDriveFileVO> getDriveFiles() { //내 구글 드라이브 전체 조회
//        List<GoogleDriveFileVO> fileList = new ArrayList<>();
//        try {
//            Drive driveService = WebhardConfig.getDriveService();
//
//            FileList result = driveService.files().list()
//                    .setPageSize(10)
//                    .setFields("nextPageToken, files(id, name, createdTime, fileExtension, size, webViewLink)")
//                    .setOrderBy("createdTime desc")
//                    .execute();
//                                   
//
//            for (File file : result.getFiles()) {
//                GoogleDriveFileVO vo = new GoogleDriveFileVO(
//                        file.getId(),
//                        file.getName(),
//                        file.getCreatedTime(),
//                        file.getFileExtension(),
//                        file.getSize(),
//                        file.getWebViewLink()
//                );
//                fileList.add(vo);
//            }
//        } catch (IOException | GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        return fileList;
//    }
	
	@Override
	public Map<String, Object> getDriveFiles(String pageToken) { 
	    List<GoogleDriveFileVO> fileList = new ArrayList<>();
	    String nextPageToken = null;

	    try {
	        Drive driveService = WebhardConfig.getDriveService();

	        Drive.Files.List request = driveService.files().list()
	                .setPageSize(10)
	                .setFields("nextPageToken, files(id, name, createdTime, fileExtension, size, webViewLink)")
	                .setOrderBy("createdTime desc");

	        // ✅ 페이지네이션을 위한 pageToken 설정
	        if (pageToken != null && !pageToken.isEmpty()) {
	            request.setPageToken(pageToken);
	        }

	        FileList result = request.execute();
	        nextPageToken = result.getNextPageToken(); // ✅ 다음 페이지 토큰 가져오기

	        for (File file : result.getFiles()) {
	            GoogleDriveFileVO vo = new GoogleDriveFileVO(
	                    file.getId(),
	                    file.getName(),
	                    file.getCreatedTime(),
	                    file.getFileExtension(),
	                    file.getSize(),
	                    file.getWebViewLink()
	            );
	            fileList.add(vo);
	        }

	    } catch (IOException | GeneralSecurityException e) {
	        e.printStackTrace();
	    }

	    // ✅ 파일 목록과 nextPageToken을 Map에 담아 반환
	    Map<String, Object> resultData = new HashMap<>();
	    resultData.put("files", fileList);
	    resultData.put("nextPageToken", nextPageToken);
	    return resultData;
	}


	@Override
	public List<WebhardVO> getFileList() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String uploadWebhard(WebhardVO webhard, MultipartFile upload) throws IOException {
	    Drive driveService;
	    try {
	        driveService = WebhardConfig.getDriveService();
	        
	        File fileData = new File();
	        fileData.setName(webhard.getWebhardNm()); // 파일명 설정
	        
	        // ❌ 직접 설정 불가능한 필드 제거
	        // fileData.setSize((long) webhard.getFileSz());
	        // fileData.setFileExtension(webhard.getFileExtnNm());

	        // ✅ MultipartFile을 java.io.File로 변환
	        java.io.File convertedFile = convertMultiPartToFile(upload);
	        FileContent mediaContent = new FileContent(upload.getContentType(), convertedFile);

	        // ✅ Google Drive 업로드 실행
	        File uploadedFile = driveService.files()
	            .create(fileData, mediaContent)
	            .setFields("id, name")  // Google Drive에서 가져올 필드 지정
	            .execute();

	        String fileId = uploadedFile.getId();  // Google Drive 파일 ID 가져오기

	        // ✅ Google Drive 업로드 성공 로그
	        System.out.println("파일 업로드 성공! Google Drive 파일 ID: " + fileId);

	        // ✅ DB 저장
	        webhard.setFileId(fileId);
	        //webhard.setFilePathNm(fileId);  // Google Drive 파일 ID를 DB에 저장
	        webhard.setUldYmd(LocalDateTime.now());
	        webhard.setFileDelYn("N");
	        webhard.setFileFavorite("N");
	        webhard.setFileUpdateDate(LocalDateTime.now());
	        webhardMapper.insertWebhard(webhard);

	        return fileId; // 업로드된 파일 ID 반환
	    } catch (IOException | GeneralSecurityException e) {
	        e.printStackTrace();
	        return null; // 실패 시 null 반환
	    }
	}

	// ✅ MultipartFile → java.io.File 변환 메서드 추가
	private java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
	    java.io.File convFile = new java.io.File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
	    file.transferTo(convFile);
	    return convFile;
	}
	
	// 다운로드
	@Override
    public ResponseEntity<InputStreamResource> downloadFile(String fileId) {
        try {
            // 데이터베이스에서 파일 정보 조회
            WebhardVO webhardVO = webhardMapper.selectWebhardByFileId(fileId);

            if (webhardVO == null) {
                return ResponseEntity.notFound().build();
            }

            // Google Drive API에서 파일 다운로드
            Drive driveService = WebhardConfig.getDriveService();
            File file = driveService.files().get(webhardVO.getFileId()).execute();

            HttpResponse response = driveService.files().get(file.getId()).executeMedia();
            InputStream inputStream = response.getContent();

            // HTTP 응답 생성
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8))
                    .contentType(MediaType.valueOf(file.getMimeType()))
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

	@Override
	public WebhardVO getWebhardVOByFileId(String fileId) {
		return webhardMapper.selectWebhardByFileId(fileId);
	}

	@Override
	public void deleteFile(String fileId) {
		// DB에서 파일 삭제
		webhardMapper.deleteWebhard(fileId);
		
		// Google Drive에서 파일 삭제
		try {
			Drive driveSerDrive = WebhardConfig.getDriveService();
			driveSerDrive.files().delete(fileId).execute();
			System.out.println("구글 드라이브 파일 삭제 성공" + fileId);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			System.out.println("구글 드라이브 파일 삭제 실패" + fileId);
		}
		
	}
	
	@Override
	public List<WebhardVO> searchFilesByName(String keyword) {
	    return webhardMapper.findFilesByName(keyword);
	}





}