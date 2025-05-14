document.addEventListener('DOMContentLoaded', function() {

    // 좌측 가입커뮤니티 목록 클릭 시 오른쪽 내용 동적으로 변경 (AJAX를 통해 서버에서 데이터 가져오기)
	const card = document.querySelector('#card'); // 처음 화면에서만 보이고 나머지는 다 안보여야함
    const content = document.querySelector('.content'); // 모든 오른쪽 요소들이 그려져야할 곳

    let currentCommunityNo = null; // 커뮤니티번호 여러곳에서 사용을 위한 전역변수
    let noPosts = true;            // 게시글 유무에 따라 다르게 출력하기 위한 전역변수
	let currentPostNo = null;
	let currentMemberNo = null;

	// 방 관리에서 회원관리를 위한 input 전역 변수
	let userStatusInput, communityNoInput, empIdInput, memberRoleInput, memberActivityStatusInput, requestNoInput, memberNoInput;

    // 커뮤니티 토글 이벤트
    function toggleCommunityList() {
        const toggleIcon = document.querySelector('#toggle-communities');
        const communityList = document.querySelector('#community-list');

        toggleIcon.addEventListener('click', function() {
            if (communityList.style.display === 'none' || communityList.style.display === '') {
                communityList.style.display = 'block';
                toggleIcon.classList.remove("bi-chevron-down");
                toggleIcon.classList.add("bi-chevron-up");
            } else {
                communityList.style.display = 'none';
                toggleIcon.classList.remove("bi-chevron-up");
                toggleIcon.classList.add("bi-chevron-down");
            }
        });
    }

    // 좌측 가입 커뮤니티 토글 이벤트
    toggleCommunityList();

	// 첫 화면 탭 이동 이벤트 호출
	bindCustomTabEvents();

	function handleLeaveButtonClick() {
		console.log("zzzzz", currentMemberNo);

		Swal.fire({
			title: '정말로 탈퇴하시겠습니까?',
			text: "탈퇴하면 이 커뮤니티에서 나가게 됩니다.",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: '네, 탈퇴할게요',
			cancelButtonText: '취소'
		}).then((result) => {
			if (result.isConfirmed) {
				$.ajax({
					url: `/sep/${companyNo}/community/communityMemberdeleteMember`,
					method: 'POST',
					data: {
						memberNo: currentMemberNo
					},
					success: function (data) {
						if (data.result === 'success') {
							Swal.fire('탈퇴 완료!', '커뮤니티를 탈퇴했습니다.', 'success').then(() => {
								location.reload();
							});
						} else {
							Swal.fire('실패', '탈퇴 처리 중 오류가 발생했습니다.', 'error');
						}
					},
					error: function () {
						Swal.fire('실패', '서버와의 통신 중 오류가 발생했습니다.', 'error');
					}
				});
			}
		});
	}
    // 가입커뮤니티, 전체커뮤니티 탭 클릭 시 콘텐츠 전환
	function bindCustomTabEvents() {
    const tabs = document.querySelectorAll('.nav-link');
    const tabContents = document.querySelectorAll('.tab-pane');

    tabs.forEach(function(tab) {
        tab.addEventListener('click', function(event) {
            const targetId = event.target.getAttribute('data-bs-target');
            const targetContent = document.querySelector(`${targetId}`);

            tabContents.forEach(function(content) {
                content.classList.remove('show', 'active');
            });
            targetContent.classList.add('show', 'active');

            tabs.forEach(function(t) {
                t.classList.remove('active');
            });
            event.target.classList.add('active');
        });
    });
}




    // 좌측 가입커뮤니티 목록 클릭 시 좌측 내용 동적으로 변경
    const communityItems = document.querySelectorAll('.community-item');
    const communityLeft = document.querySelector('#community-left');

    communityItems.forEach(function(item) {
        item.addEventListener('click', function() {
            const communityNo = this.getAttribute('data-community-no');

			const memberNo = this.getAttribute('data-member-no'); // 이게 이제 제대로 됨


            currentCommunityNo = communityNo;
			currentMemberNo = memberNo;

			console.log("communityNo:", communityNo);
			console.log("memberNo:", memberNo);

            loadCommunityDetails(communityNo, memberNo); // 아래 이벤트 호출
        });
    });


    // 좌측 가입커뮤니티 목록 클릭 시 좌측 내용 동적으로 변경 (AJAX를 통해 서버에서 데이터 가져오기)
    function loadCommunityDetails(communityNo, memberNo) {

        // communityLeft.innerHTML = '';
		card.innerHTML = '';
		// articlesList.innerHTML = '';
        const communityContent = `
            <div class="community-header">
                <i class="bi bi-door-open"></i>
                <span id="communityName"></span>
            </div>

            <a class="btn btn-secondary btn-sm w-100" id="addBoard">
                <i class="bi bi-plus-circle"></i> 게시판 추가
            </a>

            <ul class="list-unstyled">
                <li class="d-flex justify-content-between align-items-center">
                    <h6 class="mb-0" style="font-weight: 500;">게시판목록</h6>
                    <i class="bi bi-chevron-down toggle-communities" id="toggle-communities"></i>
                </li>
                <ul class="community-list" id="community-list" style="display: block; list-style-type: none; padding-left: 0;"></ul>
            </ul>

            <ul class="list-unstyled" style="margin-top: 30px;">
                <li class="d-flex justify-content-between align-items-center">
                    <h6 class="mb-0" style="font-weight: 500;">가입 멤버</h6>
                </li>
                <ul class="member-list list-unstyled" id="member-list" style="display: block; list-style-type: none; padding-left: 0;"></ul>
            </ul>

            <div id="button-container">
                <button class="btn" id="invite-btn">
                    <i class="fas fa-user-plus fa-1x"></i> 초대
                </button>

                <button class="btn" id="leave-btn" class="leave-btn">
                    <i class="fas fa-user-minus fa-1x"></i> 탈퇴
                </button>
            </div>
        `;

        communityLeft.innerHTML = communityContent;
		history.pushState({ page: "communityDetails", communityNo: communityNo }, "Community Details", `#community-${communityNo}`);


        // 가입 멤버 하단에 가입된 멤버 목록 출력
        $.ajax({
            url: '/sep/${companyNo}/community/getMembers',
            data: { communityNo: communityNo },
            success: function(data) {
                const memberList = document.querySelector('#member-list');
                memberList.innerHTML = '';

                // 등록된 게시판 정보 가져오기
                data.board.forEach(function(boards) {
                    const communityName = document.querySelector('#communityName');
                    communityName.innerHTML = `${boards.communityNm}`;
                    const boardDiv = document.querySelector('#community-list');

                    if (boards.board.length === 0) {
                        boardDiv.innerHTML = `<li class="community-item boardNoPost">등록된 게시판이 없습니다.</li>`;
                    } else {
                        boards.board.forEach(function(boardList) {
                            boardDiv.innerHTML += `<li class="community-item boardNoPost" data-board-no="${boardList.boardNo}">${boardList.boardNm}</li>`;
                        });
                    }
                });

                // 가입된 멤버 정보 가져오기
                const ownerList = [];
                const memberListArray = [];

                data.members.forEach(function(member) {
                    member.communityMember.forEach(function(communityMemberItem) {
                        const listItem = document.createElement('li');
                        listItem.classList.add('member-item');
                        listItem.style.display = 'flex';
                        listItem.style.marginTop = '10px';

                        const icon = document.createElement('i');
                        icon.classList.add('fas');
                        if (communityMemberItem.memberRole === '방 주인') {
                            icon.classList.add('fa-crown');
                            icon.style.color = 'rgb(240, 169, 46)';
                            ownerList.push(listItem);
                        } else {
                            icon.classList.add('fa-user');
                            icon.style.setProperty('marginRight', '12px', 'important');
                            icon.style.marginLeft = '3px';
                            icon.style.marginRight = '12px';
                            icon.style.color = 'rgb(46, 120, 240)';
                            memberListArray.push(listItem);
                        }
                        icon.style.fontSize = '1.5rem';
                        icon.style.marginRight = '10px';

                        const nameText = document.createTextNode(communityMemberItem.employee.empNm);
                        listItem.appendChild(icon);
                        listItem.appendChild(nameText);

                        if (communityMemberItem.memberRole === '방 주인') {
                            ownerList.push(listItem);
                        } else {
                            memberListArray.push(listItem);
                        }
                    });
                });

                ownerList.forEach(function(item) {
                    memberList.appendChild(item);
                });

                memberListArray.forEach(function(item) {
                    memberList.appendChild(item);
                });

                // 좌측 게시판 목록 토글 이벤트
                toggleCommunityList();

                // 좌측 게시판 목록 클릭 시 오른쪽 내용 동적으로 변경
                const boardNoPost = document.querySelectorAll('.boardNoPost');
                boardNoPost.forEach(function(item) {
                    item.addEventListener('click', function() {
                        const boardNo = this.getAttribute('data-board-no');
                        boardNumberPostList(boardNo); // 아래 이벤트 호출
                    });
                });

                function boardNumberPostList(boardNo, skipPush) {
					card.innerHTML = '';

                    let cnt = 0;

                    const tableHeader = `
					<div class="d-flex justify-content-end mb-2">
					    <button class="btn btn-sm btn-primary" id="writePostBtn">
					        <i class="bi bi-pencil-square me-1"></i> 게시글 작성
					    </button>
					</div>
                        <div class="table-responsive">
                            <table id="add-row" class="table tableData table-hover display">
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="select-all" /></th>
                                        <th>글번호</th>
                                        <th>제목</th>
                                        <th>작성자</th>
                                        <th>작성일</th>
                                        <th>조회수</th>
                                        <th>좋아요</th>
                                    </tr>
                                </thead>
                                <tbody id="tableBody"></tbody>
                            </table>
                        </div>
                    `;

					const writeForm = `
					    <div class="card p-3">
					        <form id="postForm" enctype="multipart/form-data">
					            <div class="mb-3">
					                <input type="text" name="postTitle" class="form-control" placeholder="제목을 입력하세요" required>
					            </div>
					            <div class="mb-3">
					                <textarea id="editor" name="postContent" class="form-control" rows="10" placeholder="내용을 입력하세요"></textarea>
					            </div>
					            <div class="mb-3">
					                <input type="file" name="file" class="form-control" multiple>
					            </div>

					            <input type="hidden" name="boardNo" id="boardNo">
					            <input type="hidden" name="memberNo" id="memberNo">

					            <div class="d-flex justify-content-between">
					                <button type="submit" class="btn btn-primary">작성하기</button>
					            </div>
					        </form>
					    </div>
					`;
					content.innerHTML = tableHeader;
					if (!skipPush) {
					history.pushState({ page: "boardPostList", boardNo: boardNo }, "Board Post List", `#boardPostList/${boardNo}`);
}
                    // 우측에 게시글 목록 출력
                    $.ajax({
                        url: `/sep/${companyNo}/community/getBoardNoPost`,
                        data: { boardNo: boardNo },
                        success: function(data) {
                            data.boardNoPost.forEach(function(boardNoPosts) {
                                boardNoPosts.board.forEach(function(boards) {

                                    if(boards.post.length === 0){
                                        noPosts = true;
                                    }else{
                                        noPosts = false;
                                        boards.post.forEach(function(boardList) {
                                            const communityContent = `
                                                <tr class="post-item" data-post-no="${boardList.postNo}">
													<input type="hidden" data-member-no="${boardList.memberNo}" value="${boardList.memberNo}">
                                                    <td><input type="checkbox"></td>
                                                    <td>${cnt += 1}</td>
                                                    <td>${boardList.postTitle}</td>
                                                    <td>${boardNoPosts.employee.empNm}</td>
                                                    <td>${boardList.postCreatedAt}</td>
                                                    <td>${boardList.postViewCount}</td>
                                                    <td>${boardList.postLikeCount}</td>
                                                </tr>
                                            `;

                                            tableBody.innerHTML += communityContent;
                                        });
                                    }

                                });
                            });
                            // 등록된 게시글이 없을 경우 출력
                            if (noPosts) {
                                tableBody.innerHTML = '<tr><td colspan="8" style="text-align: center;">등록된 게시글이 없습니다.</td></tr>';
                            }
							initDataTables();
                        }

                    });

					// 게시글 작성
					const writePostBtn = document.querySelector('#writePostBtn');
					writePostBtn.addEventListener('click',function(){
						console.log("클릭 ㅅㅂ");

						content.innerHTML = writeForm;

						CKEDITOR.replace('editor', {
						    versionCheck: false,
						    height: 400,
						    width: '100%'
						});

						    setTimeout(() => {
						        const postForm = document.querySelector('#postForm');
								let memberNo = null;

								console.log("data", data);
								data.members.forEach(function(member) {
								    member.communityMember.forEach(function(tlqkf){
								        console.log("tlqkf", tlqkf.memberNo);
								        memberNo = tlqkf.memberNo;
								    });
								});

								// ✅ 여기서 input에 값 넣기 (memberNo 다 돈 뒤에)
								postForm.querySelector('#boardNo').value = boardNo;
								postForm.querySelector('#memberNo').value = memberNo;

								console.log("최종 memberNo:", memberNo);
						        postForm.addEventListener('submit', function (e) {
						            e.preventDefault(); // 폼 기본 동작 막기

						            const postTitle = postForm.postTitle.value;
						            const postContent = CKEDITOR.instances.editor.getData();
						            //const boardNo = postForm.boardNo.value;
						            //const memberNo = postForm.memberNo.value;

						            const formData = new FormData(postForm);
						            formData.set('postContent', postContent); // 에디터 내용 수동으로 설정

						            $.ajax({
						                url: `/sep/${companyNo}/community/postInsert`,
						                type: 'POST',
						                data: {
											boardNo: boardNo,
											memberNo: memberNo,
											postTitle: postTitle,
											postContent: postContent
										    },

											success: function (data) {
												// 응답으로 받은 경로에서 postNo 추출
												const postNo = data.postNo;
												const redirectUrl = data.redirectUrl;

												history.pushState({ page: "postDetail", postNo: postNo, memberNo: memberNo }, "", redirectUrl);

												// 게시글 상세 불러오기
												boardPostDetail(postNo, memberNo);
											},
						                error: function (err) {
						                    console.error("작성 실패", err);
						                }
						            });
						        });
						    }, 100); // CKEditor 로딩 등 고려해 잠깐 딜레이
						});




					// 게시글 목록 클릭 시 상세페이지 진입
                    tableBody.addEventListener('click', function(e){
						card.innerHTML = '';

                        const tr = e.target.closest('tr.post-item');
                        const postNo = tr.getAttribute('data-post-no');

						const inputElement = tr.querySelector('input[data-member-no]');
						const memberNo = inputElement.getAttribute('data-member-no');
                        boardPostDetail(postNo, memberNo); // 아래 함수 호출
                    });

                    function boardPostDetail(postNo, memberNo){
						currentPostNo = postNo;   // ← 전역 변수에 저장

						currentMemberNo = memberNo;

						// 아이콘 스타일 반영 (선택사항)
						function updateLikeIconStyle() {
							const icon = document.querySelector('#likeIcon');
							icon.style.color = isLiked ? 'red' : '';
						}

                        const boardPostPage = `
                            <div class="notice-container">
                                <div class="button-group">
                                    <button type="button" class="btn btn-sm btn-info">수정</button>
                                </div>

                                <div class="notice-info">
                                    <strong name="boardNm"></strong>
                                </div>

                                <div class="notice-title" name="postTitle"></div>

                                <div class="notice-info">
                                    <span name="postEmpNm"></span>

									<div class="icon-view-count">
									<i class="fas fa-thumbs-up" id="likeIcon" style="cursor: pointer;"></i>
									    <span name="postLikeCount" id="postLikeCount"></span>

									    <i class="fas fa-eye"></i>
									    <span name="postViewCount"></span>
									</div>
                                </div>

                                <div class="notice-container">
                                    <div class="notice-content" name="postContent"></div>
                                </div>

								<div class="comment-section mt-4">
								    <div id="commentList" class="mb-3">
								        <p class="text-muted" id="noCommentsText">등록된 댓글이 없습니다.</p>
								    </div>

								    <div class="d-flex justify-content-between align-items-center mb-2">
								        <button class="btn btn-outline-secondary btn-sm" id="toggleCommentInput">
								            <i class="bi bi-chat-dots"></i> 댓글 작성
								        </button>
								    </div>

								    <div id="commentInputContainer" class="d-none">
								        <div class="mb-2">
								            <textarea id="commentText" class="form-control" rows="3" placeholder="댓글을 입력하세요..."></textarea>
								        </div>
								        <div class="d-flex justify-content-end">
								            <button class="btn btn-primary btn-sm" id="submitCommentBtn">
								                <i class="bi bi-send"></i> 댓글 작성
								            </button>
								        </div>
								    </div>
								</div>
                            </div>
                        `;
                        content.innerHTML = boardPostPage;

						if (!skipPush) {
						    history.pushState({ page: "postDetail", postNo: postNo, memberNo: memberNo }, "Post Detail", `#postDetail/${postNo}`);
						}

                        $.ajax({
                            url: `/sep/${companyNo}/community/detailPost`,
                            data: {postNo: postNo},
                            success: function(data){

                                // 작성자
                                document.querySelector('[name="postEmpNm"]').textContent = '작성자 : ' + data.postDetail.employee.empNm;

                                // 제목, 내용, 작성일, 조회수, 좋아요수
                                data.postDetail.board.forEach(function(boardItem){
                                    // 게시판 제목
                                    document.querySelector('[name="boardNm"]').textContent = boardItem.boardNm

                                    boardItem.post.forEach(function(postItem){
                                        document.querySelector('[name="postTitle"]').textContent = postItem.postTitle
                                        document.querySelector('[name="postViewCount"]').textContent = postItem.postViewCount
										document.querySelector('#postLikeCount').textContent = postItem.postLikeCount ?? 0;
										document.querySelector('[name="postContent"]').textContent = postItem.postContent.replace(/<[^>]*>/g, '');
                                    });
                                });
								loadComments(postNo); // ← 여기에 추가

                            }
                        })

                        // 좋아요 아이콘 색 부드럽게 전환
						let isLiked = false;
						$.ajax({
							url: `/sep/${companyNo}/community/postLiktStatus`,
							data: { postNo: postNo, memberNo: memberNo },
							success: function(data){
								isLiked = data.isLiked;

								updateLikeIconStyle(); // 초기 스타일 적용

						    }
						});
                        document.querySelector('#likeIcon').addEventListener('click', function(){
							const url = `/sep/${companyNo}/community/postLike`;
							const method = 'POST';

							// 좋아요 취소
							if(isLiked){
								isLiked = false;
								$.ajax({
									url: url,
									method: method,
									data: { postNo: postNo, memberNo: memberNo, isLiked: false },
									success: function(data){
										document.querySelector('#postLikeCount').textContent = data.likeCount;
										updateLikeIconStyle();

									}
								})
							// 좋아요 등록
							}else{
								isLiked = true;
								$.ajax({
									url: url,
									method: method,
									data: { postNo: postNo, memberNo: memberNo, isLiked: true },
									success: function(data){
										document.querySelector('#postLikeCount').textContent = data.likeCount;
										updateLikeIconStyle();

									}
								})
							}

						});


                    }

				}


            }
        });
        // 게시판 만들기
        const addBoard = document.querySelector('#addBoard');
        addBoard.addEventListener('click', function() {
            boardInsert(); // 게시판 만들기 이벤트 호출
        });
		// 초대 버튼 클릭 이벤트
		const inviteBtn = document.querySelector('#invite-btn');
		inviteBtn.addEventListener('click', function (e) {
			const icon = e.currentTarget;

			const popup = document.getElementById("organizationPopup");
			const detailPopup = document.getElementById("detailPopup");

			// 팝업 먼저 보여주기
			popup.style.display = "block";

			let confirmBtn = document.getElementById('confirmOrgSelectionBtn');
			if (!confirmBtn) {
				confirmBtn = document.createElement('button');
				confirmBtn.id = 'confirmOrgSelectionBtn';
				confirmBtn.className = 'btn btn-sm btn-primary mt-2';
				confirmBtn.textContent = '확인';
				document.getElementById('organizationPopup').appendChild(confirmBtn);

				// ✅ "확인" 버튼 클릭 이벤트
				confirmBtn.addEventListener('click', function () {
					const tree = $.ui.fancytree.getTree("#depTree");
					const selectedNode = tree.getActiveNode();

					if (!selectedNode || selectedNode.folder) {
						alert("직원을 선택해주세요.");
						return;
					}

					const empId = selectedNode.key;
					const communityNo = currentCommunityNo;
					console.log("empId", empId);
					console.log("communityNo", communityNo);

					fetch(`/sep/${companyNo}/community/${communityNo}/invite`, {
						method: 'POST',
						headers: { 'Content-Type': 'application/json' },
						body: JSON.stringify({ empId: empId })
					})
					.then(response => {
						if (response.ok) {
							Swal.fire({
								toast: true,
								position: 'top',
							  title: "초대 완료되었습니다",
							  icon: "success",
							  draggable: true,
							  showConfirmButton: false,
							  timer: 2000
							});
							popup.style.display = "none";
							detailPopup.style.display = "none";
							// 💡 필요하면 멤버 리스트 갱신 함수 호출
							loadCommunityDetails(communityNo, memberNo);

						} else {
							alert("초대 실패 (이미 초대되었을 수 있음)");
						}
					})
					.catch(err => {
						console.error("초대 중 오류:", err);
						alert("서버 오류");
					});
				});
			}

			// 위치 설정
			const rect = icon.getBoundingClientRect();
			const scrollTop = window.scrollY || document.documentElement.scrollTop;
			const scrollLeft = window.scrollX || document.documentElement.scrollLeft;

			popup.style.position = "absolute";
			popup.style.top = `${rect.bottom + scrollTop + 5}px`;
			popup.style.left = `${rect.left + scrollLeft}px`;
			popup.style.zIndex = 9999;

			// detailPopup 위치 조정 감지
			const observer = new MutationObserver(() => {
				if (detailPopup.style.display === 'block') {
					const popupRect = popup.getBoundingClientRect();
					detailPopup.style.position = "absolute";
					detailPopup.style.top = `${popupRect.top + scrollTop}px`;
					detailPopup.style.left = `${popupRect.right + scrollLeft + 10}px`;
					detailPopup.style.zIndex = 9999;
					observer.disconnect();
				}
			});
			observer.observe(detailPopup, { attributes: true, attributeFilter: ['style'] });

			// FancyTree 한 번만 초기화
			if (!$.ui.fancytree.getTree("#depTree")) {
				setTimeout(() => {
					initFancyTree();
				}, 50);
			}
		});
		// 탈퇴 버튼
		const leaveBtn = document.querySelector('#leave-btn');
		if (leaveBtn) {
			leaveBtn.addEventListener('click', handleLeaveButtonClick);
		}
    }

	// 좌측 톱니바퀴 모양 클릭 시 오른쪽 화면 동적으로 변경
	const roomManagement = document.querySelectorAll('.roomManagement');

	roomManagement.forEach(function(icon) {
		icon.addEventListener('click', function(e) {
			e.stopPropagation();
			const communityNo = icon.getAttribute('data-community-no');
			currentCommunityNo = communityNo;

			card.innerHTML = '';

			const roomManagementData = `
				<div class="mb-3" id="mainCard">
					<div class="card-body">
						<ul class="nav nav-tabs nav-tabs-bordered" id="community-room-tabs">
							<li class="nav-item">
								<button class="nav-link active" id="community-roomUpdate-tab" data-bs-target="#community-roomUpdate">
									방 편집
								</button>
							</li>
							<li class="nav-item">
								<button class="nav-link" id="community-member-tab" data-bs-target="#community-member">
									회원 관리
								</button>
							</li>
							<li class="nav-item">
								<button class="nav-link" id="community-roomDelete-tab" data-bs-target="#community-roomDelete">
									방 폐쇄
								</button>
							</li>
						</ul>
					</div>
				</div>

				<div id="community-roomUpdate" class="tab-pane fade show active">
					<div class="card shadow-sm border-0 p-4" style="background-color: #f9f9fb;">
						<div class="mb-4">
							<label for="communityName" class="form-label fw-bold text-secondary">커뮤니티명</label>
							<div class="form-control bg-white" readonly style="min-height: 40px;">
								<span name="communityNm"></span>
							</div>
						</div>

						<div class="mb-4">
							<label for="communityDescription" class="form-label fw-bold text-secondary">커뮤니티 설명</label>
							<textarea class="form-control" name="communityContent" rows="4" placeholder="커뮤니티 설명을 입력하세요."></textarea>
						</div>

						<div class="mb-2">
						    <label for="master" class="form-label fw-bold text-secondary">마스터명</label>
						    <div class="d-flex align-items-center justify-content-between bg-white form-control" style="min-height: 40px;">
						        <span name="roomMasterName" class="text-dark"></span>
						        <div class="oval-icon-container ms-2">
						            <i class="fas fa-plus fs-6 roomMasterBtn" style="cursor: pointer; color: #0d6efd;"></i>
						        </div>
						    </div>
						</div>
					</div>

					<div class="d-grid gap-2 col-1 mx-auto">
					  <button class="btn btn-primary" type="button" id="roomUpdateBtn">저장하기</button>
					</div>
				</div>

					<div id="community-member" class="tab-pane fade">
						<div class="table-responsive">
							<table id="add-row" class="table tableData table-hover display">
								<thead>
									<tr>
										<th><input type="checkbox" id="select-all" /></th>
										<th>No</th>
										<th>아이디</th>
										<th>이름</th>
										<th>가입일</th>
										<th>상태</th>
										<th>상태처리</th>
									</tr>
								</thead>
								<tbody id="tableBody"></tbody>
							</table>
						</div>
						<div class="d-grid gap-2 col-1 mx-auto">
						  <button class="btn btn-primary" type="button" id="joinStatusBtn">저장하기</button>
						  <input type="hidden" name="userStatus">
						  <input type="hidden" name="communityNo">
						  <input type="hidden" name="empId">
						  <input type="hidden" name="memberRole">
						  <input type="hidden" name="memberActivityStatus">
						  <input type="hidden" name="requestNo">
						  <input type="hidden" name="memberNo">
						</div>
					</div>

					<div id="community-roomDelete" class="tab-pane fade">
						<div class="alert alert-warning mt-3" role="alert" style="line-height: 1.6;">
							<p class="mb-2">
								커뮤니티를 폐쇄하시면, 현재 커뮤니티 내에 있는 모든 자료들이 삭제됩니다.
							</p>
							<p class="fw-bold text-danger mb-0">
								자료가 삭제된 이후에는 복구가 불가능합니다.
							</p>
						</div>
						<div class="text-end mt-3">
							<button class="btn btn-danger" id="shutDown" data-bs-toggle="modal" data-bs-target="#closureModal">
								폐쇄하기
							</button>
						</div>
					</div>
				</div>
			`;
			content.innerHTML = roomManagementData;

			bindCustomTabEvents(); // 톱니바퀴 탭 이동 이벤트 호출


			history.pushState({ page: "roomManagement" }, "Room Management", "#roomManagement");


			const tableBody = document.querySelector('#tableBody');

			$.ajax({
				url: `/sep/${companyNo}/community/roomManagement`,
				data: { communityNo: communityNo },
				success: function(data) {
					// 방 편집 탭
					data.roomManagement.forEach(function(item) {
						document.querySelector('[name="communityNm"]').textContent = item.communityNm;
						document.querySelector('[name="communityContent"]').textContent = item.communityContent;
						document.querySelector('[name="roomMasterName"]').textContent = item.employee.empNm;
					});

					// 회원관리 탭
					let cnt = 0;
					data.roomManagement.forEach(function(item) {

						item.joinStatus.forEach(function(status) {
							const matchingMember = item.communityMember.find(m => m.employee.empId === status.empId);
							const memberRole = matchingMember ? matchingMember.memberRole : null;
							const memberNo = matchingMember ? matchingMember.memberNo : null;

							console.log("memberNo", memberNo);
							const tableContetn = `
								<tr class="post-row">
									<td><input type="checkbox"></td>
									<td>${cnt += 1}</td>
									<td>${status.empId}</td>
									<td>${status.employee.empNm}</td>
									<td>${status.requestDate}</td>
									<td>${status.userStatus}</td>
									<td>
										${
											memberRole === '방 주인'
												? ''
												: status.userStatus === '승인'
													? `<button class="btn btn-sm btn-danger expel-btn deleteStatusBtn"
														data-member-no="${memberNo}"
														data-request-no="${status.requestNo}">강퇴</button>`
													: status.userStatus === '대기'
														? `<select class="form-select form-select-sm status-select" data-emp-id="${status.empId}" style="width: 70px;">
																<option value disabled hidden selected>✔</option>
																<option value="승인">승인</option>
																<option value="거절">거절</option>
														   </select>`
														: ''
										}
									</td>
								</tr>
							`;
							tableBody.innerHTML += tableContetn;

							});
							initDataTables();


					});
					// 서버 전송을 위한 input 값 셋팅
					document.addEventListener('change',function(e){
						if(e.target.classList.contains('status-select')){
							const selectedValue = e.target.value;
							const empId = e.target.getAttribute('data-emp-id');

							userStatusInput = document.querySelector('[name="userStatus"]');
							communityNoInput = document.querySelector('[name="communityNo"]');
							empIdInput = document.querySelector('[name="empId"]');
							memberRoleInput = document.querySelector('[name="memberRole"]');
							memberActivityStatusInput = document.querySelector('[name="memberActivityStatus"]');

							// input 값 셋팅
							userStatusInput.value = selectedValue;
							communityNoInput.value = currentCommunityNo;
							empIdInput.value = empId;


							if(selectedValue === "승인"){
								memberRoleInput.value = "멤버";
								memberActivityStatusInput.value = "활동중";

							}else if(selectedValue === "거절"){
								memberRoleInput.value = "";
								memberActivityStatusInput.value = "";
							}else{
								userStatusInput.value = "";
								communityNoInput.value = "";
								empIdInput.value = "";
								memberRoleInput.value = "";
								memberActivityStatusInput.value = "";
							}

						}
					});
				}
			});
			const roomMasterBtn = document.querySelector('.roomMasterBtn');

			// 방장 권한 이전을 위한 조직도 팝업
			roomMasterBtn.addEventListener('click', function (e) {
				const icon = e.currentTarget;

				const popup = document.getElementById("organizationPopup");
				const detailPopup = document.getElementById("detailPopup");

				// 팝업 먼저 보여주기
				popup.style.display = "block";

				if (!document.getElementById('confirmOrgSelectionBtn')) {
					const confirmBtn = document.createElement('button');
					confirmBtn.id = 'confirmOrgSelectionBtn';
					confirmBtn.className = 'btn btn-sm btn-primary mt-2';
					confirmBtn.textContent = '확인';
					document.getElementById('organizationPopup').appendChild(confirmBtn);
				}

				const rect = icon.getBoundingClientRect();
				const scrollTop = window.scrollY || document.documentElement.scrollTop;
				const scrollLeft = window.scrollX || document.documentElement.scrollLeft;

				const top = rect.bottom + scrollTop + 5;
				const left = rect.left + scrollLeft;

				popup.style.position = "absolute";
				popup.style.top = `${top}px`;
				popup.style.left = `${left}px`;
				popup.style.zIndex = 9999;

				// 🔁 detailPopup 뜨는 걸 감지해서 위치 옮기기 (공통 코드 안 건드리고!)
				const observer = new MutationObserver(() => {
					if (detailPopup.style.display === 'block') {
						const popupRect = popup.getBoundingClientRect();

						detailPopup.style.position = "absolute";
						detailPopup.style.top = `${popupRect.top + scrollTop}px`;
						detailPopup.style.left = `${popupRect.right + scrollLeft + 10}px`;
						detailPopup.style.zIndex = 9999;

						observer.disconnect(); // 더 이상 감시 안 함
					}
				});

				observer.observe(detailPopup, { attributes: true, attributeFilter: ['style'] });

				// FancyTree 초기화 (한 번만)
				if (!$.ui.fancytree.getTree("#depTree")) {
					setTimeout(() => {
						initFancyTree();
					}, 50);
				}
			});
			document.addEventListener('click', function (e) {
				if (e.target && e.target.id === 'confirmOrgSelectionBtn') {
					const tree = $.ui.fancytree.getTree("#depTree");
					const selectedNode = tree.getActiveNode();

					if (selectedNode && !selectedNode.folder) {
						const empName = selectedNode.title;
						const empId = selectedNode.key;

						const masterNameSpan = document.querySelector('[name="roomMasterName"]');
						if (masterNameSpan) {
							masterNameSpan.textContent = empName;
						}

						let input = document.querySelector('[name="newRoomMasterId"]');
						if (!input) {
							input = document.createElement('input');
							input.type = 'text';
							input.name = 'newRoomMasterId';
							document.body.appendChild(input);
						}
						input.value = empId;

						document.getElementById("organizationPopup").style.display = "none";
						document.getElementById("detailPopup").style.display = "none";
					} else {
						alert("직원을 선택해주세요.");
					}
				}
			});


			// 방 관리 저장하기 버튼 이벤트
			const roomUpdateBtn = document.querySelector('#roomUpdateBtn');

			roomUpdateBtn.addEventListener('click', function(){
				const communityContent = document.querySelector('[name="communityContent"]');
				const communityNo = currentCommunityNo

				const newRoomMasterIdInput = document.querySelector('[name="newRoomMasterId"]');
				const newMasterEmpId = newRoomMasterIdInput ? newRoomMasterIdInput.value : null;


				console.log("communityContent", communityContent);
				console.log("communityNo", communityNo);

				$.ajax({
					url: `/sep/${companyNo}/community/roomDataUpdate`,
					type: 'POST',
					data: {
						communityContent: communityContent.value,
						communityNo: communityNo,
						newMasterEmpId: newMasterEmpId

					},
					success: function(){
						Swal.fire({
						  title: "커뮤니티방 편집 성공",
						  icon: "success",
						  draggable: true

					  }).then(() => {
		                  // ✅ 방장을 바꾼 경우만 새로고침 (접근 권한이 사라질 수 있으므로)
		                  if (newMasterEmpId) {
		                      location.reload(); // 또는 원하는 URL로 리다이렉트
		                  } else {
		                      // 그대로 현재 상태 유지 (데이터만 갱신하거나 필요하면 UI 갱신)
		                      console.log("방장 변경 없이 저장됨");
		                  }
		              });
					}
				});

			});

			// 회원관리 저장하기 버튼 이벤트
			const joinStatusBtn = document.querySelector('#joinStatusBtn');

			// 가입 승인 또는 거절 처리
			joinStatusBtn.addEventListener('click', function(){
				console.log("나 클릭함");

				$.ajax({
					url: `/sep/${companyNo}/community/joinStatus`,
					type: 'POST',
					data: {
						userStatus: userStatusInput.value,
						communityNo: communityNoInput.value,
						empId: empIdInput.value,
						memberRole: memberRoleInput.value,
						memberActivityStatus: memberActivityStatusInput.value
					},

					success: function (response) {
					    if (response.result === "success") {
					        const updated = response.updateStatus;

					        const row = [...document.querySelectorAll('.post-row')]
					            .find(tr => tr.children[2].textContent === updated.empId);

					        if (row) {
					            row.children[5].textContent = updated.userStatus;

					            const statusCell = row.children[6];
					            if (updated.userStatus === '승인') {
					                statusCell.innerHTML = `<button class="btn btn-sm btn-danger expel-btn" data-emp-id="${updated.empId}">강퇴</button>`;
					            } else if (updated.userStatus === '거절') {
					                row.remove();
					            }
					        }

							Swal.fire({
							  title: "정상적으로 처리 되었습니다.",
							  icon: "success",
							  draggable: true
							})
					    }
					}
				});
			});

			// 강퇴처리
			document.addEventListener('click', function(e){
				if(e.target.classList.contains('deleteStatusBtn')){
					const memberNo = e.target.getAttribute('data-member-no');
					const requestNo = e.target.getAttribute('data-request-no');

					requestNoInput = document.querySelector('[name="requestNo"]');
					memberNoInput = document.querySelector('[name="memberNo"]');
					requestNoInput.value = requestNo;
					memberNoInput.value = memberNo;

					const row = e.target.closest('tr');
					$.ajax({
						url: `/sep/${companyNo}/community/deleteMember`,
						type: 'POST',
						data: {
							requestNo : requestNoInput.value,
							memberNo : memberNoInput.value
						},
						success: function(response){
							Swal.fire({
							  title: "정상적으로 처리 되었습니다.",
							  icon: "success",
							  draggable: true
							});

							if (row) row.remove();
						}
					});
				}
			});

			// 폐홰하기 이벤트
			document.querySelector('#confirmClosureBtn').addEventListener('click', function(){
				const closureReason = document.querySelector('#closure-reason').value;
				const communityNo = this.getAttribute('data-community-no');

				if(!closureReason.trim()){
					Swal.fire({
					  icon: "error",
					  title: "폐쇄 처리 실패",
					  text: "폐쇠사유를 입력해주세요",
					});
					return;
				}

				$.ajax({
					url: `/sep/${companyNo}/community/shutDown`,
					type: 'POST',
					data: {
						closureReason: closureReason,
						communityNo: communityNo

					},
					success: function(response){
						Swal.fire({
						  title: "정상처리완료",
						  icon: "success",
						  draggable: true
						});
						location.reload();
					}
				});
			});
		});

	});


    // 좌측 가입커뮤니티 목록 클릭 시 오른쪽 내용 동적으로 변경 (AJAX를 통해 서버에서 데이터 가져오기)
    communityItems.forEach(function(item) {
        item.addEventListener('click', function() {
            const communityNo = this.getAttribute('data-community-no');
            loadCommunityBoard(communityNo); // 아래 이벤트 호출



        });
    });

    function loadCommunityBoard(communityNo, skipPush) {
		card.innerHTML = '';

        let cnt = 0;
        const tableHeader = `

            <div class="table-responsive">
                <table id="add-row" class="table tableData table-hover display">
                    <thead>
                        <tr>
                            <th><input type="checkbox" id="select-all" /></th>
                            <th>글번호</th>
                            <th>게시판명</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>조회수</th>
                            <th>좋아요</th>
                        </tr>
                    </thead>
                    <tbody id="tableBody">
                    </tbody>
                </table>
            </div>
        `;
        content.innerHTML = tableHeader;

		if (!skipPush) {
		    history.pushState({ page: "communityBoard", communityNo: communityNo }, "Community Board", `#communityBoard/${communityNo}`);
		}

        const tableBody = document.querySelector('#tableBody');
        // 우측에 게시글 목록 출력
        $.ajax({
            url: `/sep/${companyNo}/community/getPost`,
            data: { communityNo: communityNo },
            success: function(data) {
				let html = '';
				let noPosts = true; // 초기값을 true로 설정

				data.board.forEach(function(boards) {
				    boards.board.forEach(function(boardItem) {
				        if (boardItem.post.length > 0) {
				            noPosts = false; // 하나라도 게시글이 있다면 false로 변경
				            boardItem.post.forEach(function(postItem) {
				                html += `
				                    <tr class="post-row" data-post-id="${postItem.postNo}">
										<input type="hidden" data-member-no="${postItem.memberNo}" value="${postItem.memberNo}">
				                        <td><input type="checkbox"></td>
				                        <td>${cnt += 1}</td>
				                        <td>${boardItem.boardNm}</td>
				                        <td>${postItem.postTitle}</td>
				                        <td>${boards.employee.empNm}</td>
				                        <td>${postItem.postCreatedAt}</td>
				                        <td>${postItem.postViewCount}</td>
				                        <td>${postItem.postLikeCount}</td>
				                    </tr>
				                `;
				            });
				        }
				    });
				});

				if (noPosts) {
				    html = '<tr><td colspan="8" style="text-align: center;">등록된 게시글이 없습니다.</td></tr>';
				}
				tableBody.innerHTML = html;

				initDataTables();

            }
        });

        tableBody.addEventListener('click', function(e){
            card.innerHTML = '';
            const tr = e.target.closest('tr.post-row');
            const postNo = tr.getAttribute('data-post-id');

			const inputElement = tr.querySelector('input[data-member-no]');
			const memberNo = inputElement.getAttribute('data-member-no');
            postDetail(postNo, memberNo); // 아래 함수 호출
        });

        function postDetail(postNo, memberNo, skipPush){
			currentPostNo = postNo;   // ← 전역 변수에 저장
			currentMemberNo = memberNo;

			// 아이콘 스타일 반영 (선택사항)
			function updateLikeIconStyle() {
				const icon = document.querySelector('#likeIcon');
				icon.style.color = isLiked ? 'red' : '';
			}

            const detailPage = `
                <div class="notice-container">
                    <div class="button-group">
                        <button type="button" class="btn btn-sm btn-info">수정</button>
                    </div>

                    <div class="notice-info">
                        <strong name="boardNm"></strong>
                    </div>

                    <div class="notice-title" name="postTitle"></div>

					<div class="notice-info">
					    <span name="postEmpNm"></span>

						<div class="icon-view-count">
						<i class="fas fa-thumbs-up" id="likeIcon" style="cursor: pointer;"></i>
						    <span name="postLikeCount" id="postLikeCount"></span>

						    <i class="fas fa-eye"></i>
						    <span name="postViewCount"></span>
						</div>
					</div>

                    <div class="notice-container">
                        <div class="notice-content" name="postContent"></div>
                    </div>

					<div class="comment-section mt-4">

					    <div id="commentList" class="mb-3">
					        <p class="text-muted" id="noCommentsText">등록된 댓글이 없습니다.</p>
					    </div>

					    <div class="d-flex justify-content-between align-items-center mb-2">
					        <button class="btn btn-outline-secondary btn-sm" id="toggleCommentInput">
					            <i class="bi bi-chat-dots"></i> 댓글 작성
					        </button>
					    </div>

					    <div id="commentInputContainer" class="d-none">
					        <div class="mb-2">
					            <textarea id="commentText" class="form-control" rows="3" placeholder="댓글을 입력하세요..."></textarea>
					        </div>
					        <div class="d-flex justify-content-end">
					            <button class="btn btn-primary btn-sm" id="submitCommentBtn">
					                <i class="bi bi-send"></i> 댓글 작성
					            </button>
					        </div>
					    </div>
					</div>
                </div>
            `;

            content.innerHTML = detailPage;

			if (!skipPush) {
			    history.pushState({ page: "postDetail", postNo: postNo, memberNo: memberNo }, "Post Detail", `#postDetail/${postNo}`);
			}

            $.ajax({
                url: `/sep/${companyNo}/community/detailPost`,
                data: {postNo: postNo},
                success: function(data){
                    // 작성자
                    document.querySelector('[name="postEmpNm"]').textContent = '작성자 : ' + data.postDetail.employee.empNm;

                    // 제목, 내용, 작성일, 조회수, 좋아요수
                    data.postDetail.board.forEach(function(boardItem){
                        // 게시판 제목
                        document.querySelector('[name="boardNm"]').textContent = boardItem.boardNm

                        boardItem.post.forEach(function(postItem){
							document.querySelector('[name="postTitle"]').textContent = postItem.postTitle
							document.querySelector('[name="postViewCount"]').textContent = postItem.postViewCount
							document.querySelector('#postLikeCount').textContent = postItem.postLikeCount ?? 0;
							document.querySelector('[name="postContent"]').textContent = postItem.postContent.replace(/<[^>]*>/g, '');
                        });
                    });

					loadComments(postNo); // ← 여기에 추가

                }
            })

			// 좋아요 아이콘 색 부드럽게 전환
			let isLiked = false;
			$.ajax({
				url: `/sep/${companyNo}/community/postLiktStatus`,
				data: { postNo: postNo, memberNo: memberNo },
				success: function(data){
					isLiked = data.isLiked;

					updateLikeIconStyle(); // 초기 스타일 적용

			    }
			});
			document.querySelector('#likeIcon').addEventListener('click', function(){
				const url = `/sep/${companyNo}/community/postLike`;
				const method = 'POST';

				// 좋아요 취소
				if(isLiked){
					isLiked = false;
					$.ajax({
						url: url,
						method: method,
						data: { postNo: postNo, memberNo: memberNo, isLiked: false },
						success: function(data){
							document.querySelector('#postLikeCount').textContent = data.likeCount;
							updateLikeIconStyle();

						}
					})
				// 좋아요 등록
				}else{
					isLiked = true;
					$.ajax({
						url: url,
						method: method,
						data: { postNo: postNo, memberNo: memberNo, isLiked: true },
						success: function(data){
							document.querySelector('#postLikeCount').textContent = data.likeCount;
							updateLikeIconStyle();

						}
					})
				}

			});
        }
    }

	// 댓글 조회
	function loadComments(postNo) {
	    $.ajax({
	        url: `/sep/${companyNo}/community/selectComment`,
	        method: "GET",
	        data: { postNo },
	        success: function(response) {
	            const container = document.getElementById("commentList");
	            container.innerHTML = ""; // 기존 댓글 비우기

	            const comments = response.comments || []; // 혹시 null 방지

	            if (comments.length === 0) {
	                container.innerHTML = `<p class="text-muted">등록된 댓글이 없습니다.</p>`;
	                return;
	            }

	            comments.forEach(comment => {
	                const writerName = '이철희';
	                const writeDate = comment.replyCreateDate || "";
	                const content = comment.replyContent || "";

	                const commentHTML = `
	                    <div class="mb-2 border p-2 rounded bg-light">
	                        <div class="fw-bold">${writerName}</div>
	                        <div class="small text-muted">${writeDate}</div>
	                        <div>${content}</div>
	                    </div>
	                `;
	                container.insertAdjacentHTML("beforeend", commentHTML);
	            });
	        },
	    });
	}
	// 댓글 등록
	document.addEventListener("click", function (e) {

	    // 댓글 작성 아이콘 클릭 시
	    if (e.target.closest("#toggleCommentInput")) {
	        const inputBox = document.getElementById("commentInputContainer");
	        inputBox.classList.toggle("d-none");
	    }

	    // 댓글 작성 버튼 눌렀을 때
	    if (e.target.closest("#submitCommentBtn")) {
	        const text = document.getElementById("commentText").value.trim();
	        if (!text) {
				Swal.fire({
					toast: true,
					position: 'top',
				  title: "댓글 내용을 입력하세요.",
				  icon: "error",
				  draggable: true,
				  showConfirmButton: false,
				  timer: 2000
				});
	            return;
	        }

	        // postNo, memberNo는 외부 스코프에서 가져오도록 해야 함 (예: 전역 변수로 저장하거나 dataset 등으로 넘기기)
	        // 여기선 전역 변수로 저장했다고 가정할게
	        $.ajax({
	            url: `/sep/${companyNo}/community/insertComment`,
	            method: "POST",
	            data: {
	                postNo: currentPostNo, // <- 전역변수로 저장해놨다고 가정
	                memberNo: currentMemberNo,
	                replyContent: text
	            },
	            success: function(response) {
	                if (response.result === "success") {
						Swal.fire({
							toast: true,
							position: 'top',
						  title: "댓글 등록이 완료되었습니다.",
						  icon: "success",
						  draggable: true,
						  showConfirmButton: false,
						  timer: 2000
						});
	                    document.getElementById("commentText").value = ""; // 입력창 비우기
	                    document.getElementById("commentInputContainer").classList.add("d-none");

	                    // 👉 여기서 새로 댓글 리스트 불러오는 함수 호출 가능
						loadComments(currentPostNo);
	                }
	            },
	            error: function() {
	                alert("댓글 등록 중 오류가 발생했습니다.");
	            }
	        });
	    }
	});

    // 방 만들기
    const roomInsert = document.querySelector('#roomInsert');
    roomInsert.addEventListener('click', function() {
        roomInsertForm(); // 아래 코드 호출
    });

    function roomInsertForm() {
		card.innerHTML = '';
        const roomInsertForm = `
            <div class="community-create-form">
                <h3>커뮤니티 개설</h3>
                <form id="formData" action="/sep/${companyNo}/community/insertRoom" method="post">
                    <div class="form-group">
                        <label for="communityName">커뮤니티명</label>
                        <input type="text" class="form-control" name="communityNm" placeholder="커뮤니티명을 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="communityDescription">커뮤니티 설명</label>
                        <textarea class="form-control" name="communityContent" placeholder="커뮤니티에 대한 설명을 입력하세요" rows="4"></textarea>
                    </div>
                    <div style="text-align: center;">
                        <button type="submit" class="btn btn-sm btn-primary" id="formButton">커뮤니티 만들기</button>
                    </div>
                </form>
            </div>
        `;
        content.innerHTML = roomInsertForm;
		history.pushState({ page: "roomInsert" }, "Room Insert", "#roomInsert");

    }

    // 게시판 만들기 form
    function boardInsert() {
		card.innerHTML = '';

        const boardInsertForm = `
            <div class="community-create-form">
                <h3>게시판 만들기</h3>
                <div class="form-group">
                    <label for="boardName">게시판명</label>
                    <input type="text" class="form-control" name="boardNm" id="boardNm" placeholder="게시판명을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="boardDescription">게시판 설명</label>
                    <textarea class="form-control" name="boardDescription" id="boardContent" placeholder="게시판에 대한 설명을 입력하세요" rows="4"></textarea>
                </div>
                <div style="text-align: center;">
                    <button type="button" class="btn btn-sm btn-primary" id="createBoardButton">게시판 만들기</button>
                </div>
            </div>
        `;
        content.innerHTML = boardInsertForm;

        const createBoardButton = document.querySelector('#createBoardButton');
        createBoardButton.addEventListener('click', function() {
            const boardNm = document.querySelector('#boardNm').value;
            const boardContent = document.querySelector('#boardContent').value;
            const communityNo = currentCommunityNo;

            $.ajax({
                url: `/sep/${companyNo}/community/insertBoard`,
                type: 'POST',
                data: {
                    communityNo: communityNo,
                    boardContent: boardContent,
                    boardNm: boardNm
                },
                success: function() {
					Swal.fire({
					  title: "게시판 생성 완료",
					  icon: "success",
					  draggable: true
					}).then(() => {
						history.pushState({ page: "communityBoard", communityNo: communityNo }, "Community Board", `#communityBoard/${communityNo}`);
				          loadCommunityBoard(communityNo);
						  loadCommunityDetails(communityNo);

				      });

                }
            });
        });
    }

	document.addEventListener('click', function (e) {
	    if (e.target && e.target.classList.contains('leave-btn')) {
	        const memberNo = e.target.getAttribute('data-member-no');

	        if (!memberNo || memberNo === 'null') {
	            Swal.fire('에러', 'memberNo가 유효하지 않습니다.', 'error');
	            return;
	        }

	        currentMemberNo = memberNo; // 🔥 여기서 설정!
	        handleLeaveButtonClick();  // 탈퇴 처리 함수 호출
	    }
	});

	// 커뮤니티 가입하기
	const joinRoomBtn = document.querySelectorAll('.joinRoom');
	joinRoomBtn.forEach(function(icon){
		icon.addEventListener('click', function(){
			const communityNo = this.getAttribute('data-community-no');
			const buttonElement = this;
			console.log("zz", communityNo);
			console.log("가입하기");

			$.ajax({
				url: `/sep/${companyNo}/community/joinStatusInsert`,
				type: 'POST',
				data: { communityNo: communityNo },
				success: function(){
					Swal.fire({
					  title: "가입요청 완료",
					  icon: "success",
					  draggable: true
					});
					const badge = document.createElement('span');
					badge.className = "badge bg-warning text-dark";
					badge.innerText = "승인 대기중";

					// 버튼을 뱃지로 교체
					buttonElement.parentNode.replaceChild(badge, buttonElement);
				}
			});
		})
	});


	window.addEventListener("popstate", function(event) {
	    const state = event.state;
	    console.log("popstate 발생", state);

	    if (!state || !state.page) {
	        console.warn("popstate - state.page 없음", state);
			location.reload(); // ✅ 여기서 그냥 새로고침!

	        return;
	    }

	    switch (state.page) {
	        case "roomInsert":
	        case "roomManagement":
	        case "communityDetails":
				location.reload(); // 또는 강제 리다이렉트 방식도 가능
	            break;

	        case "communityBoard":
	            loadCommunityBoard(state.communityNo, true);
	            break;

	        case "postDetail":
	            postDetail(state.postNo, state.memberNo, true);
	            break;

	        case "boardPostList":
	            boardNumberPostList(state.boardNo, true);
	            break;

	        case "boardInsert":
	            boardInsert();
	            break;

	        default:
	            console.warn("정의되지 않은 상태:", state);
	            break;
	    }
	});

	// 커뮤니티 목록을 클릭했을 때 상태 기록 (pushState 사용)
	communityItems.forEach(function(item) {
	    item.addEventListener('click', function() {
	        const communityNo = this.getAttribute('data-community-no');

	        // 상태를 히스토리에 기록 (뒤로가기를 할 때 이전 상태로 돌아갈 수 있도록 pushState 사용)
	        history.pushState({ page: "communityBoard", communityNo: communityNo }, "Community Board", `#communityBoard/${communityNo}`);

	        // 커뮤니티 게시판 화면 로드
	        loadCommunityBoard(communityNo);
	    });
	});

	function initDataTables() {
	  $('.tableData').each(function () {
	    if (!$.fn.DataTable.isDataTable(this)) {
	      $(this).DataTable({
	        language: {
	          search: "검색:",
	          lengthMenu: "_MENU_개씩 보기",
	          zeroRecords: "검색 결과가 없습니다.",
	          paginate: {
	            next: "다음",
	            previous: "이전"
	          }
	        }
	      });
	    }
	  });
	}
});

