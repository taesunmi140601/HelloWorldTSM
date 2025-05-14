/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 10.     	JYS            최초 생성
 *
 * </pre>
 */

function initVacationScript() {
  const form = document.querySelector('#docForm');
  if (!form || form.dataset.docType !== 'vacation') return;

  const today = new Date().toISOString().split('T')[0];

  const startDateEl = document.querySelector("#startDate");
  const endDateEl = document.querySelector("#endDate");
  const vacOptionEl = document.querySelector("#vacOption");

  if (!startDateEl || !endDateEl || !vacOptionEl) return;

  startDateEl.setAttribute("min", today);
  endDateEl.setAttribute("min", today);

  // axios를 이용한 휴가 옵션 select 박스 비동기 호출
  axios.get(`${path}/annual/annualOption`)
       .then(response => {
	        const options = response.data;
	        const selectHtml = [`<select id="vacOptionSelect">`];
	        selectHtml.push(`<option value="0">선택</option>`);
	        options.forEach(opt => {
	          selectHtml.push(`<option value="${opt.leaveTypeCd}">${opt.leaveType}</option>`);
	        });
	        selectHtml.push(`</select>`);
	        selectHtml.push(`<div id="halfDayTypeWrapper" style="display:none; margin-top:5px;"></div>`);
	        vacOptionEl.innerHTML = selectHtml.join('');
	
	        document.querySelector("#vacOptionSelect").addEventListener("change", calculateVacationDays);
	        startDateEl.addEventListener("change", calculateVacationDays);
	        endDateEl.addEventListener("change", calculateVacationDays);
	
	        calculateVacationDays(); // 초기 계산
      })
      .catch(err => {
        console.error('휴가 옵션 로딩 실패:', err);
        vacOptionEl.innerHTML = `<span style="color:red;">휴가 옵션을 불러오지 못했습니다.</span>`;
      });
}


function calculateVacationDays() {
  const vacOptionText = document.querySelector("#vacOptionSelect")?.selectedOptions[0]?.text;
  const startInput = document.querySelector("#startDate");
  const endInput = document.querySelector("#endDate");
  const vacDaysInput = document.querySelector("#vacDaysInput");
  const halfDaySelectWrapper = document.querySelector("#halfDayTypeWrapper");

  if (!startInput || !endInput || !vacDaysInput || !halfDaySelectWrapper) return;

  if (vacOptionText === "반차") { // 반차
    endInput.disabled = true;
    vacDaysInput.value = 0.5;
    halfDaySelectWrapper.style.display = 'block';
    halfDaySelectWrapper.innerHTML = `
      <label for="halfDayType">반차 시간 선택:</label>
      <select id="halfDayType">
        <option value="AM">오전 반차</option>
        <option value="PM">오후 반차</option>
      </select>
    `;
  } else if (vacOptionText === "연차") { // 연차
    endInput.disabled = false;
    halfDaySelectWrapper.style.display = 'none';
    halfDaySelectWrapper.innerHTML = '';

    const start = new Date(startInput.value);
    const end = new Date(endInput.value);

    if (!isNaN(start) && !isNaN(end) && end >= start) {
      const diffTime = end - start;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      vacDaysInput.value = diffDays;
	  console.lgo("diffDays : ", diffDays)
    } else {
      vacDaysInput.value = '';
    }
  } else {
    endInput.disabled = false;
    vacDaysInput.value = '';
    halfDaySelectWrapper.style.display = 'none';
    halfDaySelectWrapper.innerHTML = '';
  }
}


