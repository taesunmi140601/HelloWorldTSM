/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 4.     	JSW            최초 생성
 *
 * </pre>
 */
function fetchParticipants(projectNo) {
            if (!projectNo) {
                document.getElementById('empId').innerHTML = '<option value="">선택하세요</option>';
                return;
            }

            
            const url = contextPath + "/" + companyNo + "/task/new/participants/" + projectNo;
            
            console.log("Final URL:", url);

            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Fetched participants:", data);  // 데이터 구조 확인

                    const participantSelect = document.getElementById('empId');
                    participantSelect.innerHTML = '<option value="">선택하세요</option>';

                    if (data.length === 0) {
                        const option = document.createElement('option');
                        option.value = '';
                        option.text = '참여자가 없습니다.';
                        participantSelect.appendChild(option);
                    } else {
                        data.forEach(participant => {
                            const option = document.createElement('option');
                            option.value = participant.empId; // ← 혹은 participant.empId로도 테스트
                            option.text = `${participant.empId} (${participant.empNm})`;
                            participantSelect.appendChild(option);
                        });
                    }
                })
                .catch(error => console.error('Error fetching participants:', error));
        }

        document.addEventListener('DOMContentLoaded', function () {
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('taskStartDate').value = today;
			
			document.getElementById("mecro").addEventListener("click", function () {
			    // 자동완성 값 설정
			    const autoFillData = {
			      taskTitle: "로그인 구현",
			      taskContent: "프론트앤드와 백앤드 나눠 로그인을 구현 합니다.",
			      taskEndDate: "2025-04-20",
				  priority: '보통',
				  progress: '10%',
			    };
	
			    document.getElementById("taskTitle").value = autoFillData.taskTitle;
			    document.getElementById("taskContent").value = autoFillData.taskContent;
			    document.getElementById("taskEndDate").value = autoFillData.taskEndDate;
			    document.getElementById("priority").value = autoFillData.priority;
			    document.getElementById("progress").value = autoFillData.progress;
			  });
			
            const selectedProjectNo = "${selectedProjectNo}";
            if (selectedProjectNo && selectedProjectNo !== 'null' && selectedProjectNo.trim() !== '') {
                fetchParticipants(selectedProjectNo);
            }

            const projectSelect = document.getElementById('projectNo');
            projectSelect.addEventListener('change', function () {
                const projectNo = this.value;
                if (projectNo && projectNo.trim() !== '') {
                    fetchParticipants(projectNo);
                } else {
                    document.getElementById('empId').innerHTML = '<option value="">선택하세요</option>';
                }
            });
			
    });