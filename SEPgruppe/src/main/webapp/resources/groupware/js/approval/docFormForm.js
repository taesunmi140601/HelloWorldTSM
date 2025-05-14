/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 17.     	JYS            최초 생성
 *
 * </pre>
 */



async function insertDoc(){
	const docFolderNo = document.querySelector("#folder").dataset.docFolderNo;
	const docName = document.querySelector("#docName").value.trim();
	const docContent = CKEDITOR.instances.editor.getData();
	
	if (!docName || !docContent || docFolderNo === "양식 그룹") {
		alert("모든 항목을 입력해주세요.");
		return;
	}
	
	const formData = {
		docFolderNo:docFolderNo,
		docFrmName: docName,
		docFrmContent: docContent,
		
	};
	
	try{
		const response = await axios.post(
					`${path}/approval/admin/newDocAdd`,
					formData,
					{
						headers: {
							"Content-Type": "application/json"
						}
					}
				);

				if (response.status === 200) {
					alert("양식이 등록되었습니다.");
					location.href = `${path}/approval/admin/docFormList`;
				} else {
					alert("등록에 실패했습니다.");
				}
	}catch(error){
		
	}
}