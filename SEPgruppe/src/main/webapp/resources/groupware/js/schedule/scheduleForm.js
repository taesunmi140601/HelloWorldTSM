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
      scheduleTypeNo: "3",
      schdlNm: "신입사원 오리엔테이션",
      schdlCn: "신입사원 오리엔테이션 진행합니다.",
      sschdlBgngYmd: "2025-04-21",
      sschdlEndYmd: "2025-04-21",
      schdlLocation: "중회의실",
      schdlStatus: "참석"
    };

    $("#scheduleTypeNo").val(autoFillData.scheduleTypeNo);
    $("#schdlNm").val(autoFillData.schdlNm);
    $("#schdlCn").val(autoFillData.schdlCn);
    $("#sschdlBgngYmd").val(autoFillData.sschdlBgngYmd);
    $("#sschdlEndYmd").val(autoFillData.sschdlEndYmd);
    $("#schdlLocation").val(autoFillData.schdlLocation);
    $("#schdlStatus").val(autoFillData.schdlStatus);

  });
});