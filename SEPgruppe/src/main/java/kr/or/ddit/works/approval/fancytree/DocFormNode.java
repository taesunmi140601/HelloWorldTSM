package kr.or.ddit.works.approval.fancytree;

import java.io.Serializable;

import kr.or.ddit.works.approval.vo.DocFolderVO;
import kr.or.ddit.works.approval.vo.DocFormVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DocFormNode implements FancyTreeNode<DocFormVO>, Serializable {
	
	private final DocFormVO adaptee;
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return adaptee.getDocFrmNo();
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return adaptee.getDocFrmName();
	}

	@Override
	public boolean isFolder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DocFormVO getData() {
		// TODO Auto-generated method stub
		return adaptee;
	}
}
