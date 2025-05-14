/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일        수정자     수정내용
 *  -----------  ---------  ---------------------------
 *  2025. 4. 2.   JYS        최초 생성 (등록용)
 *  2025. 4. 7.   JYS        조회 후 수정 기능 통합 및 리팩토링
 * </pre>
 */

const path = document.body.getAttribute('data-context-path');

const apprLineAddBtn = document.querySelector("#apprLineAddBtn");
const apprLineChangeBtn = document.querySelector("#apprLineChangeBtn");
const apprLineSaveBtn = document.querySelector("#apprLineSaveBtn");
const apprLineUpdateBtn = document.querySelector("#apprLineUpdateBtn");
const apprLineDeleteBtn = document.querySelector("#apprLineDeleteBtn");
const autoLineSetTbody = document.querySelector("#autoLineSetTbody");


// isReadOnly 값 정의 (읽기 전용 상태 여부)
const isReadOnly = document.body.getAttribute('data-readonly') === 'true';  // 예: body에 data-readonly="true"로 설정

// tr 조회
function getRows() {
    return autoLineSetTbody.querySelectorAll("tr");
}

// DOM 로드 후 실행
document.addEventListener("DOMContentLoaded", async () => {
    apprLineAddBtn?.addEventListener("click", addApprovalLine);
    apprLineChangeBtn?.addEventListener("click", changeApprovalLine);
    apprLineSaveBtn?.addEventListener("click", saveApprovalLine);
    apprLineUpdateBtn?.addEventListener("click", saveApprovalLine); // 필요시 구현
    apprLineDeleteBtn?.addEventListener("click", deleteApprovalLine); // 필요시 구현

    // 조회 후 수정 페이지일 경우 aprvlLineJsonData 존재
    const aprvlLineTag = document.querySelector("#aprvlLineJsonData");
    if (aprvlLineTag) {
        const aprvlLine = JSON.parse(aprvlLineTag.textContent);
        console.log("aprvlLine", aprvlLine);
        const { apprLineType, department, position } = await fetchOptionData();
        renderApprovalLines(aprvlLine, apprLineType, department, position);
    }
});

// 옵션 데이터 요청
async function fetchOptionData() {
    try {
        const resp = await axios.get(`${path}/approval/admin/selectType`);
        return resp.data;
    } catch (err) {
        console.error("옵션 데이터 불러오기 실패:", err);
        showToast("옵션 데이터를 불러오는 데 실패했습니다.");
        return { apprLineType: [], department: [], position: [] };
    }
}

// 옵션 생성 함수
function createOptions(list, valueKey, labelKey, selectedValue = "none", isReadOnly = false) {
    let options = `<option value="none" ${selectedValue === "none" ? "selected" : ""} disabled>==== 선택 ====</option>`;
    options += list.map(item => {
        const isSelected = item[valueKey] === selectedValue ? 'selected' : '';
        return `<option value="${item[valueKey]}" ${isSelected} ${isReadOnly ? 'disabled' : ''}>${item[labelKey]}</option>`;
    }).join("");
    return options;
}

// 결재선 행 추가
function addApprovalLine() {
    if (!autoLineSetTbody) return;

    fetchOptionData().then(({ apprLineType, department, position }) => {
        const apprLineTypeOptions = createOptions(apprLineType, "commCodeNo", "commCodeName");
        const departmentOptions = createOptions(department, "deptCd", "deptName");
        const positionOptions = createOptions(position, "positionCd", "positionName");

        const existingRows = autoLineSetTbody.querySelectorAll("tr");
        const aprvlTurn = existingRows.length + 1;

        const newRow = document.createElement("tr");
        newRow.innerHTML = `
            <td id="aprvlTurn">${aprvlTurn}</td>
            <td>
                <select id="commCodeNo" ${isReadOnly ? "disabled" : ""}>${apprLineTypeOptions}</select>
            </td>
            <td>
                <select id="deptCd" ${isReadOnly ? "disabled" : ""}>${departmentOptions}</select>
            </td>
            <td>
                <select id="positionCd" ${isReadOnly ? "disabled" : ""}>${positionOptions}</select>
            </td>
            <td><i class="fa fa-trash delete-icon" style="cursor:pointer;"></i></td>
        `;
        autoLineSetTbody.appendChild(newRow);
        deleteIcon(newRow);
    });
}

// 결재자 삭제 아이콘 이벤트
function deleteIcon(row) {
    row.querySelector(".delete-icon").addEventListener("click", () => {
        row.remove();
        updateRowNumbers();
    });
}

// 순서 번호 다시 부여
function updateRowNumbers() {
    const rows = getRows();
    rows.forEach((row, idx) => {
        row.querySelector("td:first-child").textContent = idx + 1;
    });
}

// 결재자 순서 변경 기능
let sortableInstance = null;
function changeApprovalLine() {
    const rows = getRows();

    if (rows.length === 0) {
        showToast("결재자가 존재하지 않습니다.");
        return;
    }

    if (!sortableInstance) {
        sortableInstance = new Sortable(autoLineSetTbody, {
            animation: 150,
            handle: "td",
            onEnd: updateRowNumbers,
        });
        apprLineChangeBtn.innerHTML = "순서 변경 완료";
    } else {
        sortableInstance.destroy();
        sortableInstance = null;
        apprLineChangeBtn.innerHTML = `<i class="fa fa-sort"></i>&nbsp;순서 바꾸기`;
    }
}

// 자동결재선 저장
function saveApprovalLine() {
    console.log("저장 프로세스 시작");
    const rows = getRows();
    console.log("rows", rows);
    if (rows.length === 0) {
        showToast("결재자가 존재하지 않습니다.");
        return;
    }
	
	// 결재선명 가져오기
	const aprvlLineName = document.querySelector("#autoLineName")?.value.trim();

	// 입력 폼 처리
	const selectElement = document.querySelector("#docFrmNo"); // select 요소를 선택

	let docFrmNo = null;
	if (selectElement && selectElement.value !== "none") {
	    docFrmNo = selectElement.value;  // 선택된 옵션의 value 값을 가져옵니다.
	} else {
	    console.error("입력 폼에서 선택된 양식이 없습니다.");
	}

	// 수정 폼 처리
	const inputElement = document.querySelector(".docFrmNo"); // input 요소를 선택

	if (inputElement) {
	    docFrmNo = inputElement.dataset.docFrmNo; // 수정 폼에서는 data-doc-frm-no에서 값 가져오기
	} else {
	    console.error("수정 폼에서 요소를 찾을 수 없습니다.");
	}

	console.log("docFrmNo: ", docFrmNo);  // 확인용 출력
	const aprvlLineNo = document.querySelector("#autoLineNo")?.value || null; // 있으면 insert, 없으면 update

    const approvalLines = Array.from(rows).map(row => ({
        aprvlLineNo: aprvlLineNo,
        aprvlLineName: aprvlLineName,
        docFrmNo: docFrmNo,
        aprvlTurn: row.querySelector("#aprvlTurn").innerText.trim(),
        commCodeNo: row.querySelector("#commCodeNo").value,
        deptCd: row.querySelector("#deptCd").value,
        positionCd: row.querySelector("#positionCd").value
    }));

    const isUpdate = aprvlLineNo && aprvlLineNo !== "";
    console.log("isUpdate", isUpdate);
    const method = isUpdate ? 'put' : 'post';
    const url = isUpdate
        ? `${path}/approval/admin/apprLineAutoUpdate/${aprvlLineNo}` // PUT: 수정
        : `${path}/approval/admin/apprLineAutoInsert`;               // POST: 등록
    console.log("approvalLines", approvalLines);
    axios({
        method,
        url,
        headers: {
            'Content-Type': 'application/json'
        },
        data: approvalLines
    })
    .then(resp => {
        if (resp.status === 200) {
            showToast(isUpdate ? "수정 완료" : "저장 완료");
            setTimeout(() => {
                window.location.href = `${path}/approval/admin/apprLineAutoList`;
            }, 1500);
        } else {
            console.warn("예상과 다른 응답:", resp);
        }
    })
    .catch(error => {
        console.error("저장 오류:", error);
        showToast("저장 중 오류가 발생했습니다.");
    });
}


// 조회 후 렌더링
function renderApprovalLines(aprvlLine, apprLineType, department, position) {
    autoLineSetTbody.innerHTML = "";
    aprvlLine.forEach(row => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td id="aprvlTurn">${row.aprvlTurn}</td>
            <td>
                <select name="commCodeNo" id="commCodeNo" ${isReadOnly ? "disabled" : ""}>
                    ${createOptions(apprLineType, "commCodeNo", "commCodeName", row.commCodeNo)}
                </select>
            </td>
            <td>
                <select name="deptCd" id="deptCd" ${isReadOnly ? "disabled" : ""}>
                    ${createOptions(department, "deptCd", "deptName", row.deptCd)}
                </select>
            </td>
            <td>
                <select name="positionCd" id="positionCd" ${isReadOnly ? "disabled" : ""}>
                    ${createOptions(position, "positionCd", "positionName", row.positionCd)}
                </select>
            </td>
            <td><i class="fa fa-trash delete-icon" style="cursor:pointer;"></i></td>
        `;
        autoLineSetTbody.appendChild(tr);
        deleteIcon(tr);
    });
}

function deleteApprovalLine(){
	const rows = getRows();	
	if (rows.length === 0) {
	    showToast("결재자가 존재하지 않습니다.");
	    return;
	}

	// 자동결재선 번호 가져오기
	if (!aprvlLineNo) {
		showToast("삭제할 자동결재선이 지정되지 않았습니다.");
		return;
	}

	// 삭제 요청 보내기
	axios.delete(`${path}/approval/admin/apprLineAutoDelete/${aprvlLineNo}`)
		.then(resp => {
			alert("자동결재선이 삭제되었습니다.");
			window.location.href = `${path}/approval/admin/apprLineAutoList`;// 목록 페이지 등으로 이동
		})
		.catch(err => {
			console.error("삭제 실패", err);
			alert("삭제 중 오류가 발생했습니다.");
		});
}

// 토스트 메시지 출력
function showToast(message) {
    const toastEl = document.getElementById("liveToast");
    const toastBody = toastEl?.querySelector(".toast-body");
    if (toastEl && toastBody) {
        toastBody.textContent = message;
        new bootstrap.Toast(toastEl).show();
    }
}
