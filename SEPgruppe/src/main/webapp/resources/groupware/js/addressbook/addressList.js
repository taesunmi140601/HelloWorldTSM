/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 9.     	SJH            최초 생성
 *
 * </pre>
 */

$(document).ready(function () {
	$('#internal-table').DataTable({
		pageLength: 5,
		lengthChange: false,
		language: {
			search: "검색:",
			paginate: { previous: "이전", next: "다음" },
			zeroRecords: "내부 주소록이 없습니다.",
			info: "_TOTAL_명 중 _START_~_END_ 표시",
			infoEmpty: "표시할 데이터가 없습니다."
		}
	});
	
	$('#external-table').DataTable({
		pageLength: 5,
		lengthChange: false,
		language: {
			search: "검색:",
			paginate: { previous: "이전", next: "다음" },
			zeroRecords: "외부 주소록이 없습니다.",
			info: "_TOTAL_명 중 _START_~_END_ 표시",
			infoEmpty: "표시할 데이터가 없습니다."
		}
	});
});
