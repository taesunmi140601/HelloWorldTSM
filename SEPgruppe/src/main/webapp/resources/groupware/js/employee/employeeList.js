/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 3. 26.     	JSW            최초 생성
 *
 * </pre>
 */
$(document).ready(function () {
       const table = $("#multi-filter-select").DataTable({
			serverSide: true,
		    processing: true,
			dom: 'lfrtip',
		    ajax: {
		        url: contextPath + "/" + companyNo + "/employee/admin/ajaxList",
		        type: "GET",
		        data: function(d) {
					d.searchType = $('select[name="searchType"]').val();
		            d.searchWord = $('#custom-search-input').val();
					console.log("데이터 ==>", d);
		        }
		    },
		    columns: [
				{
			        data: 'empId',
			        render: function (data, type, row) {
			            return `<input type="checkbox" class="row-checkbox" name="empCheck" value="${data}"/>`;
			        },
			        orderable: false,
			        searchable: false
			    },
		        { data: 'empNo' },
		        { data: 'empNm' },
		        { data: 'deptName' },
		        { data: 'positionName' },
		        { data: 'empEmail' }
		    ],
			  lengthMenu: [10, 15, 20, 25],
			  info: false,

			  language: {
			    lengthMenu: "_MENU_ 개씩 보기",
			    zeroRecords: "일치하는 사원이 없습니다.",
			    info: "_TOTAL_명 중 _START_부터 _END_까지 표시",
			    infoEmpty: "사원이 없습니다.",
			    infoFiltered: "(전체 _MAX_명 중 필터링됨)",
			    search: "검색:",
			    paginate: {
			      previous: "이전",
			      next: "다음"
			    },
			    processing: "로딩 중...",
			  }
        });
		// 기본 자동 검색창 제거
		$('.dataTables_filter').html(`
		  <select name="searchType" class="form-select form-select-sm" style="width: 120px;">
	        <option value="empNm">이름</option>
	        <option value="deptName">부서</option>
	        <option value="positionName">직책</option>
	      </select>
		    <input type="text" id="custom-search-input" class="form-control form-control-sm" placeholder="검색어 입력" style="width:150px !important;">
		    <button class="btn btn-primary btn-sm" id="dt-search-btn">검색</button>
		`);
		
			// 전체 체크박스 클릭 시
			$(document).on('change', '#checkAll', function () {
			    const isChecked = $(this).is(':checked');
			    $('.row-checkbox').prop('checked', isChecked);
			});
	
			// 개별 체크박스 해제 시 전체 체크박스도 해제
			$(document).on('change', '.row-checkbox', function () {
			    const allChecked = $('.row-checkbox').length === $('.row-checkbox:checked').length;
			    $('#checkAll').prop('checked', allChecked);
			});	
	
		  // 검색 버튼 클릭 시 검색 실행
		  $(document).on('click', '#dt-search-btn', function () {
		    table.ajax.reload();
		  });

		  // 엔터 입력 시 검색 실행
		  $(document).on('keydown', '#custom-search-input', function (e) {
		    if (e.key === 'Enter') {
		      e.preventDefault();
		      $('#dt-search-btn').click();
		    }
		  });
		function loadDepartments() {
			const $wrap = $('#dept-select-wrap');
	
			// 버튼 숨기기
			$('#loadDeptBtn').hide();
	
			if ($wrap.find('select[name="deptCd"]').length > 0) return;

			 const $deptSelect = $('<select id="deptCd" name="deptCd" class="form-select mt-2"></select>');
			 $deptSelect.append('<option value="">부서 선택</option>');

			$.ajax({
				url: contextPath + "/" + companyNo + "/employee/departments",
				type: 'GET',
				success: function (departments) {
					departments.forEach(dept => {
						$deptSelect.append(`<option value="${dept.deptCd}">${dept.deptName}</option>`);
					});
					$wrap.append($deptSelect);
				},
				error: function () {
					Swal.fire({
					   icon: 'error',
					   title: '부서 불러오기 실패',
					   text: '서버로부터 부서 정보를 가져오지 못했습니다.'
				 	});
				}
			});
		}
		
		// 버튼 클릭 시 부서 select 보여주기
		$('#dept-select-wrap').on('click', '#loadDeptBtn', loadDepartments);

		
		$('#employeeForm').on('submit', function (e) {
		  e.preventDefault();
		  const formData = new FormData(this);
		  
		  // 동적으로 생성된 셀렉트에서 값 가져오기
		  const $deptSelect = $('select[name="deptCd"]').first();
	      if ($deptSelect.length) {
	        const deptCd = $deptSelect.val();
	        if (deptCd) {
	          formData.set('deptCd', deptCd); //ㄴ 중복 append 방지
	        }
	      }
			
		  $.ajax({
		    url: contextPath + "/" + companyNo + "/employee/new",
		    type: 'POST',
		    data: formData,
		    processData: false,
		    contentType: false,
		    success: function () {
				Swal.fire({
				    icon: 'success',
				    title: '사원 등록 성공!',
				    showConfirmButton: false,
				    timer: 1500
			  	});
				  $('#employeeModal').modal('hide');
				  setTimeout(() => location.reload(), 1500);
				},
		    error: function () {
				Swal.fire({
				    icon: 'error',
				    title: '사원 등록 실패',
				    text: '입력 정보를 다시 확인해주세요.'
			  	});
		    }
		  });
		});
		
		$(document).on('click', '#downExcel', function (e) {
		    e.preventDefault();

		    const searchType = $('select[name="searchType"]').val();
		    const searchWord = $('#custom-search-input').val(); 
			
			console.log("searchType:", searchType);
			   console.log("searchWord:", searchWord);

		    const query = new URLSearchParams({
		        searchType: searchType || '',
		        searchWord: searchWord || ''
		    }).toString();

		    const downloadUrl = `${contextPath}/${companyNo}/employee/admin/excelDownload?${query}`;
		    window.location.href = downloadUrl;
		});
		
		function getSelectedEmpIds() {
		    return $('.row-checkbox:checked').map(function () {
		        return $(this).val();
		    }).get(); // 배열로 리턴
		}
		
		$(document).on('click', '#mainPosition', function (e) {
		    e.preventDefault();

		    const checked = $('input[name="empCheck"]:checked');

		    if (checked.length === 0) {
		        Swal.fire({
		            toast: true,
		            position: 'top',
		            icon: 'warning',
		            title: '먼저 변경할 사원을 선택하세요!',
		            showConfirmButton: false,
		            timer: 2000,
		            background: '#fff3cd',
		            color: '#856404'
		        });
				return;
    		} 
			
			$('#bulkCount').text(checked.length);
		  	$('#bulkModal').modal('show');
		});
		
		// 직위 및 부서 수정버튼 클릭시
		$(document).on('click', '#confirmBulkUpdate', function () {
		  const empIds = $('input[name="empCheck"]:checked').map(function () {
		    return $(this).val();
		  }).get();

		  const value = $('#bulkValue').val();
		  const fieldType = 'position'; // 또는 'department'로 변경 가능
		  	
		  	let message = '';
			if (empIds.length > 1) {
	           	message = `✅ ${empIds.length}명 직위 일괄 수정 완료!`;
	       	} else {
	           	const empName = $(`input[name="empCheck"]:checked`).closest('tr').find('td:eq(2)').text(); // 이름 칸
	           	message = `✅ ${empName} 직위 수정 완료!`;
	       	}

		  $.ajax({
		    url: contextPath + "/" + companyNo + "/employee/admin/bulkUpdate",
		    method: 'PUT',
		    contentType: 'application/json',
		    data: JSON.stringify({ empIds, fieldType, value }),
		    success: function () {
		      Swal.fire({
				toast: true,
            	position: 'top',
            	icon: 'success',
            	title: message,
            	showConfirmButton: false,
            	timer: 2000
		      });
		      $('#bulkModal').modal('hide');
		      $('#multi-filter-select').DataTable().ajax.reload();
		    },
		    error: function () {
		      Swal.fire({
				icon: 'error',
                title: '수정 실패!',
                text: '잠시 후 다시 시도해주세요.',
                toast: true,
                position: 'top',
                timer: 2000,
                showConfirmButton: false
		      });
		    }
		  });
		});
		
		// 사원 삭제 기능
		$(document).on('click', '#bulkDelete', function (e) {
		    e.preventDefault();

		    const checked = $('input[name="empCheck"]:checked');
		    const empIds = checked.map(function () {
		        return $(this).val();
		    }).get();

		    if (empIds.length === 0) {
		        Swal.fire({
		            toast: true,
		            position: 'top',
		            icon: 'warning',
		            title: '삭제할 사원을 선택해주세요!',
		            showConfirmButton: false,
		            timer: 2000,
		            background: '#fff3cd',
		            color: '#856404'
		        });
		        return;
		    }

		    // 사전 확인 경고창
		    Swal.fire({
		        icon: 'warning',
		        title: '정말 삭제하시겠습니까?',
				html: `${empIds.length > 1 ? empIds.length + '명의 사원을' : '선택한 사원을'} 삭제합니다.<br>
				        <span style="color:red;">선택한 멤버 삭제 시, 해당 사원(들)의 모든 자료가 함께 삭제되며 삭제 후 복구할 수 없습니다.</span>`,
		        showCancelButton: true,
		        confirmButtonText: '삭제',
		        cancelButtonText: '취소',
		        confirmButtonColor: '#dc3545'
		    }).then((result) => {
		        if (result.isConfirmed) {
		            $.ajax({
		                url: `${contextPath}/${companyNo}/employee/admin/delete`,
		                method: 'DELETE',
		                contentType: 'application/json',
		                data: JSON.stringify(empIds),
		                success: function () {
		                    let message = '';
		                    if (empIds.length > 1) {
		                        message = `${empIds.length}명 일괄 삭제 완료!`;
		                    } else {
		                        const empName = checked.closest('tr').find('td:eq(2)').text();
		                        message = `${empName} 삭제 완료!`;
		                    }

		                    Swal.fire({
		                        toast: true,
		                        position: 'top',
		                        icon: 'success',
		                        title: message,
		                        showConfirmButton: false,
		                        timer: 2000
		                    });

		                    $('#multi-filter-select').DataTable().ajax.reload();
		                },
		                error: function () {
		                    Swal.fire({
		                        icon: 'error',
		                        title: '삭제 실패!',
		                        text: '잠시 후 다시 시도해주세요.',
		                        toast: true,
		                        position: 'top',
		                        timer: 2000,
		                        showConfirmButton: false
		                    });
		                }
		            });
		        }
		    });
		});
		$('#mecro').on('click', function () {
		    var autoFillData = {
		      empNm: '김그룹',
		      empId: 'kim1234',
		      empPw: 'java',
			  empPwConfirm: 'java',
			  empEmail: 'kim1234@gamil.com',
		    };

		    $('#empNm').val(autoFillData.empNm);
		    $('#empId').val(autoFillData.empId);
		    $('#empPw').val(autoFillData.empPw);
		    $('#empPwConfirm').val(autoFillData.empPwConfirm);
		    $('#empEmail').val(autoFillData.empEmail);
		  });
      });