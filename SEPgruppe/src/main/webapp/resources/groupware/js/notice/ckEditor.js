/**
 *
 */

/* CKEditor 설정 */
document.addEventListener("DOMContentLoaded", () => {
    CKEDITOR.replace('editor', {
        versionCheck: false,
        height: 400,
        width: '100%'
    });
});

/* isDraft 서버 전송 */
document.getElementById('isDraftButton').addEventListener('click', function() {
    document.getElementById('isDraftInput').value = 'Y';
});

/* 임시저장글 불러오기 */
function loadDraftContent(noticeNo, title, content) {
    document.getElementById('title').value = title; // 제목 설정

    // CKEditor가 로드될 때까지 대기 후 데이터 삽입
    if (CKEDITOR.instances.editor) {
        CKEDITOR.instances.editor.setData(content);
    } else {
        setTimeout(() => {
            CKEDITOR.instances.editor.setData(content);
        }, 100);
    }
}