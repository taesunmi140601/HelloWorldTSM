/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 21.     	JSW            최초 생성
 *
 * </pre>
 */
function updateTime() {
    const now = new Date();
	const date = now.getFullYear() + '-' + (now.getMonth() + 1).toString().padStart(2, '0') + '-' + now.getDate().toString().padStart(2, '0');
		
    document.getElementById("current-date").textContent = date;
}

updateTime();