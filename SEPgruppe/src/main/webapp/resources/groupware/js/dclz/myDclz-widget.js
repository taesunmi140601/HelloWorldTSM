/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 11.     	JSW            최초 생성
 *
 * </pre>
 */
function updateTime() {
  const now = new Date();
  const days = ['일', '월', '화', '수', '목', '금', '토'];
  const date = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')}(${days[now.getDay()]})`;
  const time = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`;
  $("#current-date").text(date);
  $("#current-time").text(time);
}
setInterval(updateTime, 1000);

function checkAttendanceStatus() {
  $.get(`${contextPath}/${companyNo}/dclz/${empId}/attendance-status`, function(data) {
    $("#start").prop("disabled", data.isCheckedIn);
    $("#end").prop("disabled", !data.isCheckedIn || data.isCheckedOut);
  });
}

document.addEventListener('DOMContentLoaded', function () {
  checkAttendanceStatus();
  updateTime();

  $("#start").click(function () {
    $.post(`${contextPath}/${companyNo}/dclz/${empId}/check-in`, function (data) {
      $(".clock-in").text(data.todayDclzStatus.attendDate);
      checkAttendanceStatus();
      updateSummaryPanel();
      $.get(`${contextPath}/${companyNo}/dclz/${empId}/dclz-list`, reloadWorkAccordion);
    });
  });

  $("#end").click(function () {
    $.ajax({
      url: `${contextPath}/${companyNo}/dclz/${empId}/check-out`,
      method: "PUT",
      success: function (data) {
        $(".clock-out").text(data.todayDclzStatus.lvffcDate);
        checkAttendanceStatus();
        updateSummaryPanel();
        $.get(`${contextPath}/${companyNo}/dclz/${empId}/dclz-list`, reloadWorkAccordion);
      }
    });
  });
});
