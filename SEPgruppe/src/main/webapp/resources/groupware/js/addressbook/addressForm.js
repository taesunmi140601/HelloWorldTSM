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
  $("#mecro").on("click", function () {
    const autoFillData = {
      adbkIsExternal: "E",
      adbkName: "안기모",
      adbkCompany: "루멘 주식회사",
      adbkDept: "개발팀",
      adbkPosition: "대리",
      adbkEmail: "ankimo213@naver.com",
      adbkPhone: "010-9402-1143"
    };

    $("#adbkIsExternal").val(autoFillData.adbkIsExternal);
    $("#adbkName").val(autoFillData.adbkName);
    $("#adbkCompany").val(autoFillData.adbkCompany);
    $("#adbkDept").val(autoFillData.adbkDept);
    $("#adbkPosition").val(autoFillData.adbkPosition);
    $("#adbkEmail").val(autoFillData.adbkEmail);
    $("#adbkPhone").val(autoFillData.adbkPhone);

  });
});