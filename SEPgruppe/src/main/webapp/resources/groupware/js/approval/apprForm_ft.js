const newDocBtn = document.querySelector("#newDocBtn");

document.addEventListener("DOMContentLoaded", () => {
    // 데이터 경로 가져오기
    path = document.body.getAttribute('data-context-path');
    console.log(path);

    // fancyTree 초기화 (tree 요소가 있을 때만 실행)
    if (document.querySelector('#tree')) {
        initFancyTree();
    }

    // 새 양식 등록 버튼 처리 (newDocBtn이 있을 때만)
    const newDocBtn = document.querySelector('#newDocBtn');
    if (newDocBtn) {
        newDocBtn.addEventListener("click", handleNewDocBtnClick);
    }

    const apprInfoModal = document.querySelector("#apprInfoModal");
	
	// ** 결재정보 모달 **
    // 모달이 열릴 때마다 실행할 이벤트 등록
    if(apprInfoModal){
		apprInfoModal.addEventListener("shown.bs.modal", () => {
	        // 처음 모달이 열릴 때만 fancyTree를 초기화
	        if (!window.fancyTreeInitialized) {
	            depInitFancyTree(); // fancytree 초기화 함수 호출
	            window.fancyTreeInitialized = true; // fancyTree가 초기화 되었음을 표시
	        }
	        depInitDragAndDrop(); // 드래그앤드롭 초기화
	    });
	}
	
	
	// 저장 버튼 클릭 시, 결재 라인 저장 함수 실행
    const saveButton = document.querySelector("#apprLineSaveBtn");
    if (saveButton) {
        saveButton.addEventListener("click", onSaveModal); // 즉시 실행하지 않도록 함수 참조 전달
    }
})

// 새 양식 등록 (관리자)
function handleNewDocBtnClick(){
	// 현재 선택된 폴더 확인
    const selectedNode = $("#tree").fancytree("getActiveNode");
	console.log("selectedNode");

    if (!selectedNode || !selectedNode.isFolder()) {
        alert("문서를 추가할 폴더를 먼저 선택하세요.");
        return;
    }

    const docFolderNo = selectedNode.key; // 선택된 폴더 ID
	const docFolderName = encodeURIComponent(selectedNode.title); // 중요!
    location.href = `${path}/approval/admin/newDocAdd/${docFolderNo}?title=${docFolderName}`;
	
}
// FancyTree 초기화 함수
function initFancyTree() {
    $("#tree").fancytree({
        extensions: ["filter"], // 필터 플러그인 활성화
        quicksearch: true,      // 빠른 검색 (선택)
        filter: {
            autoExpand: true,   // 일치 항목이 있으면 자동으로 확장
            counter: true,      // 일치 개수 표시
            fuzzy: false,       // 퍼지 매칭 (대략적인 검색 허용 여부)
            hideExpandedCounter: true,
            highlight: true,
            mode: "dim"        // 일치하지 않는 항목을 흐리게 처리 (hide 또는 dim)
        },
        // 최초 로드시 최상위 폴더만 불러옵니다.
        source: { url: `${path}/appr/parentFolder`, cache: false },
        lazyLoad: handleLazyLoad,
        activate: handleNodeActivate,
        beforeSelect: preventFolderSelect,
        // 트리 초기화 후 전체 노드를 자동으로 로드하고 펼치는 작업 수행
        init: function(event, data) {
            console.log("트리가 초기화되었습니다.");

            // 전체 노드를 재귀적으로 방문하여 lazy 노드(자식 미로드)를 모두 로드한 후 펼침
            var tree = data.tree;
            expandAllNodes(tree.getRootNode()).done(function() {
                // 최종적으로 검색어가 있다면 필터링 적용
                const query = $("#docSearch").val();
                filterTree(query);
            });
        }
    });
}

// 재귀적으로 모든 lazy 노드를 로드하고 펼치는 함수
function expandAllNodes(node) {
    var deferred = $.Deferred();
    var promises = [];
    
    // 해당 노드가 lazy 노드라면 로드
    if (node.isLazy() && !node.isLoaded()) {
        promises.push(node.load().done(function() {
            node.setExpanded(true);
        }));
    } else {
        // 이미 로드된 경우에도 펼치기
        if (node.hasChildren()) {
            node.setExpanded(true);
        }
    }
    
    // 모든 자식 노드에 대해 재귀 호출
    if (node.hasChildren()) {
        node.visit(function(child) {
            // 자신과 동일한 수준의 promise 배열에 추가
            promises.push(expandAllNodes(child));
        });
    }
    
    // 모든 promise가 완료되면 현재 deferred를 resolve
    $.when.apply($, promises).done(function() {
        deferred.resolve();
    });
    return deferred.promise();
}

// Lazy Load 핸들러 (자식 노드 로드)
function handleLazyLoad(event, data) {
    const node = data.node;
    let mode = node.data.parentDocFolder ? "document" : "folder";
    data.result = {
        url: `${path}/appr/childrenFolder`, // 자식 폴더 요청 URL
        data: { mode: mode, parent: node.key }, // 서버로 전달할 데이터
        success: function(response) {
            if (response && response.length > 0) {
                data.node.addChildren(response);
            }
        }
    };
}


// 필터링 적용 함수
function filterTree(query) {
    const tree = $.ui.fancytree.getTree("#tree");
    if (query) {
        // 모든 노드를 순회하며 검색어가 포함된 노드만 표시
        tree.filterNodes(function(node) {
            // 폴더 노드일 경우에는 노드의 title 기준 필터링
            if (node.folder) {
                return node.title.toLowerCase().includes(query.toLowerCase());
            }
            // 양식 노드(문서)의 경우 node.data.docFrmName으로 필터링 (데이터에 docFrmName 속성이 있어야 함)
            return node.data.docFrmName && node.data.docFrmName.toLowerCase().includes(query.toLowerCase());
        });
    } else {
        // 검색어가 없으면 필터 해제
        tree.clearFilter();
    }
}

// 검색창 이벤트 - 입력할 때마다 필터를 적용합니다.
$("#docSearch").on("input", function () {
    const query = $(this).val();
    console.log(query);
    filterTree(query);
});

// 노드 활성화 핸들러
function handleNodeActivate(event, data) {
    const node = data.node;
    console.log(node);
    if (node.isFolder()) {
        loadFolderDocuments(node.key);
    } else {
        loadDocumentDetail(node.key);
    }
}

// 폴더 선택 방지
function preventFolderSelect(event, data) {
    if (data.node.isFolder()) return false;
}

// 폴더 문서 목록 조회 (AJAX 요청)
function loadFolderDocuments(docFolderNo) {
    $.ajax({
        url: `${path}/approval/admin/folderDoc`,
        type: 'GET',
        data: { docFolderNo: docFolderNo },
        success: updateDocumentTable,
        error: () => alert('문서 목록을 불러오는 데 실패했습니다.')
    });
}

// realUser 정보 화면 출력
function renderRealUserInfo(realUser, today) {
	console.log("realUser : ", realUser);
    if (!realUser) return;

    document.querySelector('#positionName').textContent = realUser.positionName || '';
	
	const empNmEl = document.querySelector('#empNm');
	empNmEl.textContent = realUser.empNm || '';
	empNmEl.setAttribute('data-emp-id', realUser.empId || '');
	
	document.querySelector('#today').textContent = today ||''

}

// 결재 양식 이름 랜더링
function renderDocFrmName(docFrmNo, docFrmName){
	console.log("결재 양식 : ", docFrmName);
	const docFrmNameTd = document.querySelector("#docFrmNameTd");
	
	//값 초기화
	docFrmNameTd.innerHTML = '';
	
	// data 속성으로 양식번호 저장 + 이름 출력
	docFrmNameTd.setAttribute("data-doc-frm-no", docFrmNo);
	docFrmNameTd.innerText = docFrmName;
	
	console.log(docFrmNameTd);
}

//자동결재선 랜더링
function renderApprLineAutoInfo(aprlLineAuto) {
	console.log("결재선 랜더링");
    if (!aprlLineAuto || aprlLineAuto.length === 0) return;
	console.log("결재선: ",aprlLineAuto);
    const tbody = document.querySelector("#apprLineBody");
	if (!tbody) {
	    console.log("apprLineBody 요소를 찾을 수 없습니다.");
	    return;
	}
    tbody.innerHTML = ''; // 기존 행 삭제

    const row1 = document.createElement('tr'); // 직위
    const row2 = document.createElement('tr'); // 이름
    const row3 = document.createElement('tr'); // 결재일자

    row1.innerHTML = '<th rowspan="3">승인</th>';

    aprlLineAuto.forEach(line => {
        const empList = line.employeeList || [];
        const approver = empList[0]; // 첫 번째 결재자만 표시

        row1.innerHTML += `<td>${line.positionName || ''}</td>`;
        row2.innerHTML += `<td data-emp-id="${approver? approver.empId : ''}">${approver ? approver.empNm : ''}</td>`;
        row3.innerHTML += `<td></td>`; // 결재일자는 추후 삽입
    });

    tbody.appendChild(row1);
    tbody.appendChild(row2);
    tbody.appendChild(row3);
}
let today = "";
// 문서작성 -  문서 상세 정보 불러오기 (AJAX 요청)
function loadDocumentDetail(docFrmNo) {
    $.ajax({
        url: `${path}/approval/detail`,
        type: 'GET',
        data: { docFrmNo: docFrmNo },
        success: (response) => {
			console.log("응답: ", response)
			const docFrmContent = response.formDetail.docFrmContent;
			const docFrmName = response.formDetail.docFrmName;
			const docFrmNo = response.formDetail.docFrmNo;
            if (response && docFrmContent) {
				today = new Date().toISOString().split('T')[0];

			    const checkConDiv = document.getElementById("checkConDiv");
			    if (checkConDiv) {
					checkConDiv.innerHTML='';
			        checkConDiv.innerHTML = `
			            <table>
			                <tr>
			                    <th>마감 일자</th>
			                    <td><input type="date" id="closingDate" min="${today}"></td>
			                </tr>
			                <tr>
			                    <th>긴급</th>
			                    <td><input type="checkbox" id="isEmergency" value="Y"><td>
			                </tr>
			            </table>
			        `;
			    }

				renderDocFrmName(docFrmNo, docFrmName);
				
				renderRealUserInfo(response.realUser, response.today);
				
				renderApprLineAutoInfo(response.aprlLineAuto);
				
				document.querySelector('#templateDetails').innerHTML = `
				    <div style="margin: 5px;">
				        <label id="aprvlDocTitle"><strong>제목</strong></label>
				        <input type="text" id="docTitle" style="width: 80%;" placeholder="제목을 입력하세요">
				    </div>
					<div>
						${docFrmContent}
					</div>
				    
				`;
                initializeCKEditor();
				
				initVacationScript(); // 선택된 문서가 휴가신청서일 경우에만 동작
				
            } else {
                alert('문서를 불러오는 데 실패했습니다.');
            }
        },
        error: () => alert('서버와의 연결에 실패했습니다.')
    });
}

// CKEditor 초기화 함수 (중복 제거)
function initializeCKEditor(callback) {
    document.querySelectorAll("#editor").forEach((editorElement) => {
        if (!editorElement.classList.contains("ckeditor-initialized")) {
            CKEDITOR.replace(editorElement, { versionCheck: false, height: '100%', width: '100%' });
            editorElement.classList.add("ckeditor-initialized");
        }
    });

    // CKEditor 초기화 후 callback 실행
    if (callback) callback();
}


// 결재요청
async function draftDoc(){
	templateClone(); // finalHtml 최신화
	const docFrmNo = document.querySelector("#docFrmNameTd").dataset.docFrmNo;
	const aprvlDocTitle = document.querySelector("#docTitle").value;
	const emergencyValue = document.querySelector("#isEmergency").checked ? document.querySelector("#isEmergency").value: "";
	const closingDate = document.querySelector("#closingDate").value;
	
	const aprvlLineList = getApprovalLineData();
	console.log("docFrmNo : ", docFrmNo);
	console.log("aprvlDocTitle : ", aprvlDocTitle);
	console.log("emergencyValue : ", emergencyValue);
	console.log("closingDate : ", closingDate);
	console.log("apprLineList : ", aprvlLineList);
	try {
		const response = await axios.post(`${path}/approval/insertDraft`, {
			docFrmNo,
			aprvlDocTitle:aprvlDocTitle,
    		aprvlDocContents: finalHtml,
			isEmergency:emergencyValue,
			submitDate: today,
			closingDate,
			//첨부파일 있을 시 추가예정.
			aprvlLineList: aprvlLineList
    	},{
			headers: {
			        'Content-Type': 'application/json'
			    }
		}
		);

	    if (response.data === 'success') {
	      	showToast("기안 문서가 성공적으로 저장되었습니다.");
			setTimeout(()=>{
				window.location.href = `${path}/box/draftDocs`; // 기안문서함
			},2000);
			
	    } else {
	      	alert("문서 저장 중 문제가 발생했습니다.");
	    }
	} catch (error) {
		console.error("저장 실패:", error);
	    alert("문서 저장 중 오류가 발생했습니다.");
	}
}

function getApprovalLineData() {
  const apprLine = [];
  const draftEmp = document.querySelector("#empNm").dataset.empId;
  
  apprLine.push({
	empId : draftEmp,
	aprvlTurn : 0
  });

  const row2 = document.querySelector("#apprLineBody").children[1]; // 이름 row
  const empTds = row2.querySelectorAll("td");
  console.log("row2 : ", row2);
  console.log("empTds : ", empTds);
  

  empTds.forEach((td, index) => {
    const empId = td.dataset.empId; // render 시 심어둔 data-emp-No
	console.log(empId);
    if (empId) {
      apprLine.push({
        empId,
        aprvlTurn: index + 1
      });
    }
  });

  return apprLine;
}





// 문서 테이블 업데이트 함수 (admin)
function updateDocumentTable(documents) {
    const tableBody = document.querySelector("#docTableTbody");
	
	// tableBody 요소가 존재하지 않으면 종료
   if (!tableBody) {
       return;
   }
    tableBody.innerHTML = "";
    if (documents.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='2'>문서가 없습니다.</td></tr>";
        return;
    }

    documents.forEach(doc => {
        const row = document.createElement("tr");
        row.id = doc.docFrmNo;
        row.dataset.name = doc.docFrmName;
        row.dataset.content = doc.docFrmContent;
        row.dataset.enabled = doc.isEnabled;
        
        row.innerHTML = `
            <td>${doc.docFrmName}</td>
            <td>${doc.isEnabled}</td>
        `;
        row.style.cursor = "pointer"; // 🎯 마우스를 올리면 손가락 모양으로 변경
    
        tableBody.appendChild(row);
        row.addEventListener("click", handleRowClick); // 클릭 이벤트 추가
    });
}

// 결재 양식 수정 모달 - 관리자
function handleRowClick(event) {
    const row = event.currentTarget;  // 클릭한 행을 가져옵니다.
    console.log("row", row);
    console.log("row.dataset", row.dataset);
    console.log(row.dataset.name);

    // 데이터 가져오기
	const docFrmNo = row.id;
    const docFrmName = row.dataset.name;  // 문서 제목
    const editorContent = row.dataset.content;  // 문서 내용 (CKEditor용)
    const isEnabled = row.dataset.enabled === 'Y';  // 활성화 여부 (boolean)

    // 2. 양식에 데이터 채우기
    setModalContent(docFrmNo, docFrmName, isEnabled);  // 제목과 활성화 여부 설정

    // 3. CKEditor에 내용 삽입
    const editorInstance = CKEDITOR.instances['editor'];  // CKEditor 인스턴스 가져오기
    if (editorInstance) {
        editorInstance.setData(editorContent);  // 이미 CKEditor가 초기화되어 있으면 내용 삽입
    } else {
        // CKEditor가 초기화되지 않았다면, 초기화 후 내용 삽입
        initializeCKEditor(() => {
            const editorInstance = CKEDITOR.instances['editor'];
            if (editorInstance) {
                editorInstance.setData(editorContent);  // CKEditor에 내용 삽입
            }
        });
    }

    // 4. 문서 상세 모달 띄우기
    docDetailModalOpen();  // 모달을 띄웁니다.
}

function setModalContent(docFrmNo, docFrmName, isEnabled) {
    // 양식에 데이터를 채웁니다.
	const docFrmNoElement = document.querySelector("td[data-docFrmNo]");  // 'data-docFrmNo' 속성으로 값 설정
	    if (docFrmNoElement) {
	        docFrmNoElement.setAttribute("data-docFrmNo", docFrmNo);  // 'data-docFrmNo' 속성에 값 설정
	    }
    document.querySelector("#docFrmName").value = docFrmName;  // 제목 설정
    document.querySelector("#isEnabled").checked = isEnabled;  // 활성화 여부 설정
}

function docDetailModalOpen() {
    // 모달 요소 가져오기
    const modalElement = document.querySelector("#docDetailModal");
    if (modalElement) {
        // Bootstrap 모달 인스턴스 생성 후 띄우기
        const modal = new bootstrap.Modal(modalElement);
        modal.show();  // 모달 띄우기
    } else {
        // 모달 요소가 없으면 에러 출력
        console.error("모달 요소가 존재하지 않습니다.");
    }
	
	const docDetailUpdateBtn = document.querySelector("#docDetailUpdateBtn");
	
	if(docDetailUpdateBtn){
		docDetailUpdate(docDetailUpdateBtn);
	}
	
}

// 수정 양식에 입력된 데이터를 비동기처리(axios)
function docDetailUpdate(docDetailUpdateBtn) {
    docDetailUpdateBtn.addEventListener("click", () => {
        // 입력된 값 가져오기
		const docFrmNo = document.querySelector("td[data-docFrmNo]").getAttribute("data-docFrmNo");  // data-docFrmNo에서 양식 번호 가져오기
        const docFrmName = document.querySelector("#docFrmName").value;  // 양식명
        const docFrmContent = CKEDITOR.instances['editor'] ? CKEDITOR.instances['editor'].getData() : '';  // CKEditor 내용
        const isEnabled = document.querySelector("#isEnabled").checked ? 'Y' : null;  // 활성화 여부 (Y 또는 null)

        // 서버로 보낼 데이터 객체 생성
        const data = {
            docFrmNo: docFrmNo,
            docFrmName: docFrmName,
            docFrmContent: docFrmContent,
            isEnabled: isEnabled
        };

        // axios로 비동기 요청 (POST 방식으로 서버에 데이터 전송)
        axios.post(`${path}/approval/admin/updateDoc`, data)
            .then(response => {
                // 성공적으로 업데이트된 경우
                if (response.data.success) {
                    alert('문서가 성공적으로 업데이트되었습니다.');
                    // 모달 닫기 또는 다른 처리 로직 추가
					document.querySelector('#docDetailModal').style.display = 'none';
					location.href = `${path}/approval/admin/docFormList`;
                } else {
                    alert('문서 업데이트에 실패했습니다.');
                }
            })
            .catch(error => {
                // 요청 실패 시 처리
                console.error('문서 업데이트 실패:', error);
            });
    });
}


// html 양식 추출
let finalHtml = "";
function templateClone(){
	// 1. CKEditor에서 데이터 먼저 가져와
  	const ckData = CKEDITOR.instances.editor.getData();

  	// 2. 양식 클론
  	const templateClone = document.querySelector("#draftDocDiv").cloneNode(true);

  	// 3. CKEditor의 원래 textarea 위치 찾기
    const editorTextarea = templateClone.querySelector("#editor");
	templateClone.querySelectorAll("[id^='cke_'], .cke, .cke_reset, .cke_chrome").forEach(el => el.remove());

    if (editorTextarea) {
      // 4. 에디터 UI를 감싸고 있는 부모 (.cke_ 로 시작하는 div) 제거
      let ckeWrapper = editorTextarea.closest('[class^="cke_"], [id^="cke_"]');
      if (ckeWrapper) {
        // wrapper를 div로 새로 만들어서 ckData만 넣어줌
        const contentDiv = document.createElement("div");
        contentDiv.innerHTML = ckData;

        // 원래 wrapper 자리에 본문만 삽입
        ckeWrapper.parentNode.replaceChild(contentDiv, ckeWrapper);
      } else {
        // fallback: wrapper 못 찾으면 그냥 textarea에 내용 넣어줌
        editorTextarea.outerHTML = `<div>${ckData}</div>`;
      }
    }
	
	// 5. 체크되지 않은 체크박스 제거
	  templateClone.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
	    if (!checkbox.checked) {
	      checkbox.remove(); // 체크 안된 건 제거
	    }
	  });

  	// 6. input, textarea, select 값 복사
	templateClone.querySelectorAll("input, textarea, select").forEach(el => {
	  const value = el.value;

	  const span = document.createElement("span");
	  span.textContent = value;

	  el.parentNode.replaceChild(span, el);
	});

	 // 7. 완성된 HTML 미리보기용 저장
	 finalHtml = templateClone.innerHTML;
	
}

// 미리보기 기능
let previewWindow = null;

function previewContent() {
	if (previewWindow && !previewWindow.closed) {
		alert("미리보기 창이 이미 열려 있습니다. 내용을 새로 보시려면 창을 닫고 다시 시도하세요.");
		return;
	}

	templateClone();
	localStorage.setItem("ckeditor_content", finalHtml);

	previewWindow = window.open(`${path}/content`, "_blank", "width=800,height=600");

	previewWindow.addEventListener("beforeunload", () => {
		localStorage.removeItem("ckeditor_content");
		previewWindow = null;
	});
}


