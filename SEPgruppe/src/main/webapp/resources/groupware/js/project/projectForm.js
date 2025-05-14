/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 17.     	JSW            최초 생성
 *
 * </pre>
 */
$(document).ready(function () {
  $('#mecro').on('click', function () {
    // 자동완성 값 설정
    var autoFillData = {
      projectTitle: '신규 웹사이트 구현',
      projectEndTime: '2025-05-21',
	  projectDesc: '클라이언트 요청에 의한 신규 웹사이트 구현 입니다.',
	  projectStatus: '진행중'
    };

    $('#projectTitle').val(autoFillData.projectTitle);
    $('#projectEndTime').val(autoFillData.projectEndTime);
    $('#projectDesc').val(autoFillData.projectDesc);
    $('#projectStatus').val(autoFillData.projectStatus);
  });
});