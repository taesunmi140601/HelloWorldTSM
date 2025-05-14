let approvalList = [];

// fancytree 초기화 함수
function depInitFancyTree() {
    console.log("modal path : ", path);
    // fancytree를 초기화하고, 데이터 소스를 지정
    $("#depModalTree").fancytree({
        source: { url: `${path}/organization/parentDep`, cache: false }, // 서버에서 부서 정보를 로드
        extensions: ["dnd5"], // 드래그 앤 드롭 확장 기능 활성화
        dnd5: {
            preventVoidMoves: true, // 빈 곳으로의 드래그를 방지
            preventRecursion: true, // 자기 자신으로의 드래그 방지
            autoExpandMS: 400, // 드래그 시 자동 확장 시간
            dragStart: (node, data) => depHandleDragStart(node, data), // 드래그 시작 이벤트
            dragEnter: (node) => !node.folder // 폴더로의 드래그를 막음
        },
        lazyLoad: (event, data) => depHandleLazyLoad(data) // 부서가 동적으로 로드될 때 처리
    });
}

// 드래그 시작 시 데이터 저장
function depHandleDragStart(node, data) {
    if (!node.folder) {
		const id = node.key;
        const name = node.title;
        const department = node.parent ? node.parent.title : "";
        // 드래그된 데이터는 이름과 부서 정보를 포함
        data.dataTransfer.setData("text/plain", JSON.stringify({id, name, department }));
        return true;
    }
    return false;
}

// lazyLoad 설정 시, 데이터를 동적으로 로드하는 함수
function depHandleLazyLoad(data) {
    let mode = data.node.data.parentDeptCd ? "employee" : "department"; // 동적 데이터 로딩 모드 설정
    data.result = {
        url: `${path}/organization/childeDep`,
        data: { mode: mode, parent: data.node.key },
        cache: false
    };
}

// 드래그 앤 드롭 이벤트 초기화
function depInitDragAndDrop() {
    const approvalTableBody = document.querySelector("#approvalTableBody");

    // 기존 이벤트 리스너가 있으면 제거
    approvalTableBody.removeEventListener("dragover", depHandleDragOver);
    approvalTableBody.removeEventListener("dragleave", depHandleDragLeave);
    approvalTableBody.removeEventListener("drop", depHandleDrop);

    // 새로운 이벤트 리스너 추가
    approvalTableBody.addEventListener("dragover", depHandleDragOver); // 드래그 오버 이벤트
    approvalTableBody.addEventListener("dragleave", depHandleDragLeave); // 드래그 리브 이벤트
    approvalTableBody.addEventListener("drop", depHandleDrop); // 드래그 앤 드롭 이벤트
}

// 드래그 오버 시 배경색 변경
function depHandleDragOver(event) {
    event.preventDefault();
    const approvalTableBody = document.querySelector("#approvalTableBody");
    approvalTableBody.style.backgroundColor = "#f0f0f0";
}

// 드래그 리브 시 배경색 초기화
function depHandleDragLeave() {
    const approvalTableBody = document.querySelector("#approvalTableBody");
    approvalTableBody.style.backgroundColor = "";
}

// 드래그가 끝났을 때 데이터 처리 및 중복 체크
function depHandleDrop(event) {
    event.preventDefault();
    const approvalTableBody = document.querySelector("#approvalTableBody");
    approvalTableBody.style.backgroundColor = "";

    const rawData = event.dataTransfer.getData("text/plain"); // 드래그된 데이터 가져오기
    if (!rawData) return;

	console.log("rawData",rawData);
    const {id, name, department } = JSON.parse(rawData); // 드래그된 데이터 파싱

    // 중복된 데이터가 있는지 확인
    if (depIsDuplicate(id, name, department)) {
        const modalToast = initToast("#modalDuplicateToast"); // 중복 시 토스트 메시지 표시
        modalToast.show();
        setTimeout(() => modalToast.hide(), 2000); // 2초 후 토스트 메시지 숨기기
        return;
    }

    depAddApprovalRow(id, name, department); // 새로운 행을 추가
}

function initToast(selector) {
    const toastEl = document.querySelector(selector);
    return new bootstrap.Toast(toastEl);
}

// 중복된 값이 있는지 확인하는 함수
function depIsDuplicate(id, name, department) {
    return approvalList.some(row => row.name === name && row.department === department);
}

// 결재선 테이블에 행을 추가하는 함수
function depAddApprovalRow(id, name, department) {
    const approvalTableBody = document.querySelector("#approvalTableBody");
    const newRow = document.createElement("tr");
    newRow.innerHTML = `
        <td>&nbsp;</td>
        <td id="approverType">
            <select>
                <option>타입</option>
                <option>결재</option>
            </select>
        </td>
        <td id="approverName" data-emp-id="${id}">${name}</td>
        <td id="approverDep">${department}</td>
        <td id="approverState">상태</td>
        <td><i class="fa fa-trash delete-icon" style="cursor:pointer;"></i></td>
    `;

    // 삭제 아이콘 클릭 시 해당 행 삭제
    newRow.querySelector(".delete-icon").addEventListener("click", () => {
        newRow.remove();
        approvalList = approvalList.filter(row => row.name !== name || row.department !== department); // 삭제된 데이터 필터링
    });

    approvalTableBody.appendChild(newRow); // 새로운 행을 테이블에 추가
    approvalList.push({ id, name, department, approverType: "타입", approverState: "상태" }); // 리스트에 새로운 승인자 추가
}

function onSaveModal() {
	console.log("onSaveModal called!");
    // 1) approvalList는 이미 채워져 있다고 가정
    // 2) 모달을 닫음
    document.querySelector("#apprLineCloseBtn").click();

    // 3) 테이블 업데이트는 모달이 완전히 닫힌 후 실행되도록 약간 딜레이 줌
    setTimeout(() => {
        saveApprLine(); // 이 함수가 테이블에 approvalList 렌더링함
    }, 300); // 모달 애니메이션 종료 후 테이블에 반영
}

// 결재선 저장 함수
async function saveApprLine() {
    const tableBody = document.querySelector("#apprLineTbody");
    const docApprLineTable = document.querySelector("#apprLineBody");

    tableBody.innerHTML = ""; // 기존 테이블 초기화
    docApprLineTable.innerHTML = ""; // 결재 양식 결재선 초기화

    const row1 = document.createElement('tr'); // 직위
    const row2 = document.createElement('tr'); // 이름
    const row3 = document.createElement('tr'); // 결재일자
    row1.innerHTML = '<th rowspan="3">승인</th>';

    try {
        const results = await Promise.all(
            approvalList.map(async (approver, index) => {
                const response = await axios.get(`${path}/approval/customApprLine`, {
                    params: { empId: approver.id }
                });
                const emp = response.data.employeeVO;

                // 결재선 목록 테이블 행 추가
                const newRow = document.createElement("tr");
                newRow.innerHTML = `
                    <td>&nbsp;</td>
                    <td>${emp.positionName || ''}</td>
                    <td>${emp.empNm || ''}</td>
                    <td>${emp.deptName || ''}</td>
                    <td>대기</td>
                `;
                tableBody.appendChild(newRow);

				// 결재 양식 라인 행 구성
				row1.innerHTML += `<td>${emp.positionName || ''}</td>`;
				row2.innerHTML += `<td data-emp-id="${emp.empId}">${emp.empNm || ''}</td>`;
				row3.innerHTML += `<td></td>`;
            })
        );

        // 결재 양식 테이블에 행 추가
        docApprLineTable.appendChild(row1);
        docApprLineTable.appendChild(row2);
        docApprLineTable.appendChild(row3);
    } catch (error) {
        console.error("Error fetching employee details:", error);
    }
}
