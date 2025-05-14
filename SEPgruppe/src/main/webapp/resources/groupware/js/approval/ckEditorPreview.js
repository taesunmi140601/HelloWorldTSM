/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	JYS            최초 생성
 *
 * </pre>
 */
let editor;  // CKEditor 인스턴스를 저장할 변수

// CKEditor 초기화
window.onload = function() {
    // CKEditor 4 초기화
    editor = CKEDITOR.replace('editor');  // 'editor'라는 ID를 가진 textarea에 CKEditor를 적용
};

// 새 창에 CKEditor 내용 띄우는 함수 (미리보기)
function previewContent() {
	if (!editor) {
        alert("CKEditor가 아직 초기화되지 않았습니다.");
        return;  // editor가 초기화되지 않았을 경우, 미리보기 기능을 실행하지 않음
    }

    const content = editor.getData();  // CKEditor에서 입력한 HTML 가져오기

    // 새 창 열기 (크기 및 속성 설정)
    const newWindow = window.open("", "_blank", "width=800,height=600");

    if (newWindow) {
        // 새 창에 HTML 내용 삽입
        newWindow.document.write(`
            <html>
            <head>
                <title>문서 미리보기</title>
                <style>
                    body { font-family: Arial, sans-serif; padding: 20px; }
                    h1 { color: #333; }
                </style>
            </head>
            <body>
                ${content}  <!-- CKEditor 내용 삽입 -->
            </body>
            </html>
        `);
        newWindow.document.close();  // 새 창 내용 로딩 완료
    } else {
        alert("새 창을 열 수 없습니다. 팝업 차단기를 확인해 주세요.");
    }
}
