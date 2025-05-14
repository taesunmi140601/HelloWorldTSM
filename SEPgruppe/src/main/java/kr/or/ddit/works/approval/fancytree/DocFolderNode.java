package kr.or.ddit.works.approval.fancytree;

import java.io.Serializable;

import kr.or.ddit.works.approval.vo.DocFolderVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DocFolderNode implements FancyTreeNode<DocFolderVO>, Serializable {
	
	private final DocFolderVO adaptee;
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return adaptee.getDocFolderNo();
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return adaptee.getDocFolderName();
	}

	@Override
	public boolean isFolder() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public DocFolderVO getData() {
		// TODO Auto-generated method stub
		return adaptee;
	}
}
