package kr.or.ddit.works.approval.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.works.approval.fancytree.DocFolderNode;
import kr.or.ddit.works.approval.fancytree.DocFormNode;
import kr.or.ddit.works.approval.vo.DocFolderVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import kr.or.ddit.works.mybatis.mappers.ApprDocMapper;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/{companyNo}/appr")
public class ApprDocControllerFT {
	
	@Autowired
	private ApprDocMapper mapper;
	
	/**
	 * 최상위 폴더 조회
	 * @return
	 */
	@RequestMapping("parentFolder")
	@ResponseBody
	public List<DocFolderNode> parentFolder() {
		List<DocFolderVO> parents = mapper.selectParentFolder();
		return parents.stream().map(DocFolderNode::new) //f-> new DocFolderNode(f)
							.toList();
	}
	
	/**
	 * 하위 폴더 조회
	 * @param mode
	 * @param docFolderNo
	 * @return
	 */
	@RequestMapping("childrenFolder")
	@ResponseBody
	public List<?> childrenFolder(
			@RequestParam("mode") String mode
			, @RequestParam("parent") String docFolderNo){
		if(mode.equals("document")) {
			List<DocFormVO> form = mapper.selectDocument(docFolderNo);
			return form.stream().map(DocFormNode::new).toList();
		} else {
			List<DocFolderVO> childrens = mapper.selectFolderChild(docFolderNo);
			return childrens.stream().map(DocFolderNode::new).toList();
		}
	}

	
}
