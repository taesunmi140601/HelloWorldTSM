/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {
	const $searchForm = $("#search-form");
	const $searchUi = $("#search-ui");
	const $searchBtn = $("#search-btn");

	// 검색 UI 초기값 세팅
	$searchUi.find(":input[data-init-value]").each(function (idx, input) {
		let initValue = input.dataset.initValue;
		$(input).val(initValue);
	});

	// 페이지 버튼 클릭 시 (외부에서 fnPaging 호출)
	window.fnPaging = function (page) {
		$searchForm.find("[name='page']").val(page);
		$searchForm.submit();
	};

	// 검색 버튼 클릭 시
	$searchBtn.on("click", function () {
		// 검색 UI에서 입력값 가져와서 search-form에 복사
		$searchUi.find(":input[name]").each(function (idx, input) {
			let name = input.name;
			let value = $(input).val();

			let $target = $searchForm.find(`[name='${name}']`);
			if ($target.length > 0) {
				$target.val(value);
			} else {
				// 없는 경우는 동적으로 추가
				$searchForm.append(`<input type="hidden" name="${name}" value="${value}"/>`);
			}
		});
		$searchForm.find("[name='page']").val("1"); // 검색 시 페이지는 1로 초기화
		$searchForm.submit();
	});
});













