/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 11.     	JYS            최초 생성
 *
 * </pre>
 */

// 컨텍스트 경로 가져오기
//let path = document.body.getAttribute('data-context-path');

// 버튼 클릭 이벤트 바인딩//
document.addEventListener('click', (e) => {
  const status = e.target.getAttribute('data-status');
  console.log("status", status);
  if (status === 'A' || status === 'R' || status === 'H') {
	const label = getStatusLabel(status); 
	if(confirm(`${label} 하시겠습니까? `)){
	    handleApprovalAction(status); // 'A', 'R', 'H' 그대로 넘김
	}
  }
});

// 상태 코드(A, R, H 등)를 한글 상태로 바꾸는 유틸 함수
function getStatusLabel(code) {
  switch(code) {
    case 'A': return '승인';
    case 'R': return '반려';
    case 'H': return '보류';
    default: return '대기중'; // 처리되지 않은 상태의 기본값
  }
}

//문서 다운로드 기능 //
async function downloadPDF() {
  const { jsPDF } = window.jspdf;
  const element = document.querySelector("#aprvlDocDetailDiv");

  // HTML 요소를 canvas로 변환
  const canvas = await html2canvas(element, { scale: 2 });
  const imgData = canvas.toDataURL("image/png");

  // PDF 객체 생성 (A4 기준)
  const pdf = new jsPDF("p", "mm", "a4");
  const pageWidth = pdf.internal.pageSize.getWidth();
  const pageHeight = pdf.internal.pageSize.getHeight();

  const imgWidth = pageWidth;
  const imgHeight = (canvas.height * imgWidth) / canvas.width;

  let position = 0;

  // 이미지가 한 페이지에 들어가는 경우
  if (imgHeight < pageHeight) {
    pdf.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
  } else {
    // 이미지가 여러 페이지에 걸치는 경우
    let heightLeft = imgHeight;
    while (heightLeft > 0) {
      pdf.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;
      if (heightLeft > 0) {
        pdf.addPage();
        position = -heightLeft;
      }
    }
  }

  // PDF 저장
  pdf.save("document.pdf");
}

//결재선 정보 관련 //
let aprvlDocDetailDiv = document.querySelector("#aprvlDocDetailDiv");
let docNo = aprvlDocDetailDiv.dataset.docNo; // 문서 번호
let approvalLine = []; // 결재선 정보 저장 배열

// 결재선 정보 조회 함수
async function selectApprovalLine(docNo) {
  try {
    const response = await axios.get(`${path}/apprHome/line/${docNo}`);
    approvalLine = response.data.lineList;
    return approvalLine;
  } catch (error) {
    console.error('결재선 정보 조회 중 오류 발생:', error);
    return null;
  }
}

// 결재 가능한 사용자 여부 확인 함수
function isUserTurn(approvalLine, userId) {
	if (!approvalLine || !Array.isArray(approvalLine)) {
	    console.warn('approvalLine이 유효하지 않음:', approvalLine);
	    showToast('결재선 정보가 없습니다.');
	    return false;
	  }
  const currentIndex = approvalLine.findIndex(line => line.empId === userId);
  console.log("currentIndex : ", currentIndex);

  if (currentIndex === -1) {
    alert('결재선에 사용자가 포함되어 있지 않습니다.');
    return false;
  }

  const user = approvalLine[currentIndex];

  // 기안자는 결재 불가
  if (user.aprvlTurn === 0) {
    showToast('기안자는 결재할 수 없습니다.');
    return false;
  }


  // 기안자가 문서를 회수했는지 확인
  const drafter = approvalLine.find(line => line.aprvlTurn === 0);
  console.log("기안자상태:", drafter?.aprvlStatus);
  if (drafter && drafter.aprvlStatus === 'C') {
    showToast('문서가 회수되어 결재할 수 없습니다.');
    return false;
  }

  // 이전 결재자의 상태 확인
  let previousStatus;
  if (user.aprvlTurn === 1) {
    previousStatus = drafter ? drafter.aprvlStatus : null;
  } else {
    const previousLine = approvalLine.find(line => line.aprvlTurn === user.aprvlTurn - 1);
    previousStatus = previousLine ? previousLine.aprvlStatus : null;
  }

  console.log("이전 결재 상태 (previousStatus):", previousStatus);

  // 이전 결재자가 승인('A')했거나 기안('D') 상태면 결재 가능
  if (previousStatus === 'A' || previousStatus === 'D') {
    return true;
  } else {
    showToast('이전 결재자가 결재를 완료하지 않았습니다.');
    return false;
  }
}

// 기안자 회수 기능
async function handleWithdraw(){
	console.log("기안자 회수 시도.");
	apprDocNo = document.querySelector("#aprvlDocDetailDiv").dataset.docNo;
	approvalLine = await selectApprovalLine(apprDocNo);
	if (canDrafterWithdraw(approvalLine, userId)) {
		if (confirm("문서를 회수하시겠습니까?")) {
	      $.ajax({
	        url: `${path}/apprHome/cancel/${apprDocNo}`,
	        method: 'POST',
	        success: function (response) {
	          showToast("문서가 성공적으로 회수되었습니다.");
			  setTimeout(() => {
			    location.reload(); // 또는 원하는 페이지로 이동
			    // location.href = "/sep/testnum001/apprDoc/list";  // 예시
			  }, 1500);
	        },
	        error: function (xhr) {
	          showToast("회수에 실패했습니다: " + xhr.responseText);
	        }
	      });
		}
	    console.log("회수 가능");
	  } else {
	    showToast("이미 결재가 시작되어 회수할 수 없습니다.");
	  }
 
}

// 회수 가능 여부 판단 함수 (기안자용)
function canDrafterWithdraw(approvalLine, userId) {
 console.log("approvalLine",approvalLine , "userId",userId);
  const drafter = approvalLine.find(line => line.aprvlTurn === 0 && line.empId === userId);
  console.log("drafter : ", drafter);
  if (!drafter) return false; // 기안자가 아니면 false

  const firstApprover = approvalLine.find(line => line.aprvlTurn === 1);
  if (!firstApprover) return false;

  return firstApprover.aprvlStatus === 'W'; // 첫 번째 결재자가 결재 대기중이면 회수 가능
}


// 이름 셀(td)에 상태(승인/반려/보류)를 추가하는 함수
function updateApproverStatusText(empId, statusCode) {
  const nameTd = document.querySelector(`#apprLineBody tr:nth-child(2) td[data-emp-id="${empId}"]`);
  if (nameTd) {
    const name = nameTd.textContent.trim();
    const statusLabel = getStatusLabel(statusCode);
    nameTd.innerHTML = `${name}<br><span style="color:red;" class="status">${statusLabel}</span>`;
  }
}

// 세 번째 줄(td)에서 empId와 같은 인덱스의 td를 찾아 날짜 추가
function updateApproverDateCell(empId) {
  const nameTds = document.querySelectorAll(`#apprLineBody tr:nth-child(2) td`);
  const dateTds = document.querySelectorAll(`#apprLineBody tr:nth-child(3) td`);
  
  nameTds.forEach((td, idx) => {
    if (td.dataset.empId === empId) {
      const today = new Date().toISOString().split('T')[0].replace(/-/g, '/');
      dateTds[idx].textContent = today;
    }
  });
}

// 상태와 날짜를 함께 업데이트하는 통합 함수
function updateApprovalInfo(empId, statusCode) {
  updateApproverStatusText(empId, statusCode);
  updateApproverDateCell(empId);
}

// 결재선 HTML 내용을 서버로 전송해 DB에 저장하는 함수
function sendUpdatedHtmlToServer() {
  const updatedHtml = document.querySelector("#aprvlDocDetailDiv").innerHTML; // 결재선 div 전체 HTML
  console.log("updatedHtml", updatedHtml);
  
  // 서버에 HTML 전송 (문서 번호와 함께)
  axios.post(`${path}/apprHome/updateContents`, {
    docNo: docNo,// 문서 번호
    htmlContent: updatedHtml
  }, {
    headers: { 'Content-Type': 'application/json' }
  })
  .then(res => {
      if (res.data.success) {
        console.log("HTML 업데이트 성공");
		openModalWithContent()
      } else {
        console.error("서버 응답 실패");
      }
    })
  .catch(err => console.error("HTML 업데이트 실패", err)); // 실패 로그
}

// 모달을 열고 내부에 전달받은 내용을 표시하는 함수
function openModalWithContent(content) {
  const modal = document.querySelector("#approvalModal");
  const modalContent = document.querySelector("#modalContent");

  modalContent.innerHTML = content; // 전달받은 HTML을 삽입
  modal.style.display = "block"; // 모달 열기
}

const userId = document.querySelector("#currentUserId").value; // 현재 로그인한 사용자 ID
// 공통 결재 처리 함수 (승인/반려/보류 전용)
async function handleApprovalAction(statusType) {
  // 문서 번호는 전역 또는 상위에서 선언된 변수 사용
  const approvalLine = await selectApprovalLine(docNo); // 현재 문서의 결재선 조회
  if (!approvalLine) return;

  
  if (!isUserTurn(approvalLine, userId)) return; // 본인 차례 아니면 중단

  // 상태 코드(A/R/H)를 한글 라벨로 변환
  const statusLabelMap = {
    'A': '승인',
    'R': '반려',
    'H': '보류'
  };
  const statusLabel = statusLabelMap[statusType] || statusType;

  try {
    // 결재 상태 업데이트 요청
    const response = await axios.post(
      `${path}/apprHome/updateStatus`,
      {
        aprvlStatus: statusType,
        aprvlDocNo: docNo,
        empId: userId
      },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    // 서버 응답이 성공이면 처리 시작
    if (response.data.success) {
	
      updateApprovalInfo(userId, statusType); // 1. 결재선 HTML 업데이트
      sendUpdatedHtmlToServer(); // 2. 수정된 HTML을 서버에 저장
      showToast(`${statusLabel} 되었습니다.`)
	  setTimeout(() => {
	      location.href =`${path}/box/apprDocs`;
	  }, 3000); // 3초 뒤에 실행
	  
    } else {
      showToast('문서 처리에 실패했습니다.'); // 실패 알림
    }
  } catch (error) {
    console.error(`${statusLabel} 요청 중 오류 발생:`, error);
    showToast(`문서 ${statusLabel} 중 오류가 발생했습니다.`);
  }
}



