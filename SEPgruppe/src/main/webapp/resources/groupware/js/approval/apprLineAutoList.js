/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 6.     	JYS            최초 생성
 *
 * </pre>
 */
const path = document.body.getAttribute('data-context-path');

document.querySelectorAll("#apprLineTr").forEach(row=>{
	row.addEventListener("click",()=>{
		const docFrmNo = row.getAttribute("data-doc-no");
		console.log("docFrmNo",docFrmNo);
		console.log(`${path}/approval/admin/apprLineAutoDetail/${docFrmNo}`);
		window.location.href = `${path}/approval/admin/apprLineAutoDetail/${docFrmNo}`
	});
});