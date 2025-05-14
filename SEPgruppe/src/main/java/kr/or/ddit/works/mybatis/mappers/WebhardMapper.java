package kr.or.ddit.works.mybatis.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.works.webhard.vo.WebhardVO;

@Mapper
public interface WebhardMapper {
	List<WebhardVO> selectWebhardList();
	WebhardVO selectWebhard(int webhardNo);
	String insertWebhard(WebhardVO webhard);
	int updateWebhard(WebhardVO webhard);
	WebhardVO selectWebhardByFileId(String fileId);
	void deleteWebhard(String fileId);
	List<WebhardVO> findFilesByName(String keyword);
}
