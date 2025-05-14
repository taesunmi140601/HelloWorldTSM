/** 	
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일       수정자           수정내용
 *  -----------    -------------    ---------------------------
 * 2025. 3. 15.      KMJ            초기 생성
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

function updateSummaryPanel() {
  $.get(`${contextPath}/${companyNo}/dclz/${empId}/summary-info`, function(data) {
    const sections = $(".dclz-summary-panel .summary-section");
    sections.eq(0).find(".summary-value").text(data.weeklyTotalHours);
    sections.eq(1).find(".summary-value").text(data.monthlyRemainTime);
    sections.eq(2).find(".summary-value").text(data.monthlyTotalHours);
    sections.eq(3).find(".summary-value").text(data.monthlyOverTime);
  });
}

function getWeekStart(date) {
  const monday = new Date(date);
  const day = monday.getDay();
  const diff = (day === 0 ? -6 : 1 - day); // 월요일 기준
  monday.setDate(date.getDate() + diff);
  monday.setHours(0, 0, 0, 0);
  return monday;
}

function reloadWorkAccordion(dclzList) {
  const accordion = $("#workAccordion");
  const currentWeek = accordion.data("current-week");
  accordion.empty();

  const now = new Date();
  const thisMonth = now.getMonth();

  const grouped = {};
  dclzList.forEach(item => {
    const date = new Date(item.workingDay);
    const weekStart = getWeekStart(date);
    const key = weekStart.toISOString().split('T')[0];

    const itemMonth = date.getMonth();
    const currentMonthView = thisMonth;

    if (!grouped[key]) grouped[key] = [];
    grouped[key].push(item);
  });

  const sortedKeys = Object.keys(grouped).sort();
  let visualWeekIndex = 1;

  sortedKeys.forEach((key, idx) => {
    const weekList = grouped[key];

    const includeThisMonth = weekList.some(s => {
      const d = new Date(s.workingDay);
      return d.getMonth() === thisMonth;
    });
    if (!includeThisMonth) return; // 이번 달 데이터만 표시

    const isCurrent = visualWeekIndex === currentWeek;
    const rows = weekList.map(s => `
      <tr>
        <td>${s.workingDay}</td>
        <td>${s.attendDate || '-'}</td>
        <td>${s.lvffcDate || '-'}</td>
        <td>${s.workingTime || '-'}</td>
        <td>${s.extndWorkingHours || '-'}</td>
        <td>${s.workStatus || '-'}</td>
      </tr>`).join('');

    accordion.append(`
      <div class="accordion-item">
        <h2 class="accordion-header">
          <button class="accordion-button ${isCurrent ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#week${visualWeekIndex}" aria-expanded="${isCurrent}">
            ${visualWeekIndex} 주차 <span class="text-end">누적 근무시간: ${weekList[0].weeklyTotalHours}</span>
          </button>
        </h2>
        <div id="week${visualWeekIndex}" class="accordion-collapse collapse ${isCurrent ? 'show' : ''}">
          <div class="accordion-body">
            <table class="table table-bordered">
              <thead><tr><th>일자</th><th>업무 시작</th><th>업무 종료</th><th>근무시간</th><th>연장 근무 시간</th><th>근무 상태</th></tr></thead>
              <tbody>${rows}</tbody>
            </table>
          </div>
        </div>
      </div>`);

    visualWeekIndex++;
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
