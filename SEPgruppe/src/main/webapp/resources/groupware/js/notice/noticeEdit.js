/**
 *
 */


function removeFileFromList(fileNo) {
	const attachFileNo = document.querySelector("#attachFileNo");
	const currentValue = attachFileNo.value;
	const fileElement = document.querySelector("#file-" + fileNo);


	// 파일 삭제 시 서버로 전송하기 위한 value값 넣기
	attachFileNo.value = currentValue ? currentValue + "," + fileNo : fileNo;

	// x버튼 클릭 시 목록에서 제외
	if(fileElement){
		fileElement.remove();
	}
}
