document.addEventListener('DOMContentLoaded', function() {

    // 좌측 가입커뮤니티 목록 클릭 시 오른쪽 내용 동적으로 변경 (AJAX를 통해 서버에서 데이터 가져오기)
    const communityTabs = document.querySelector('#community-tabs');
    const articlesList = document.querySelector('#recent-articles');
    const communityContent = document.querySelector('#community-content');

    let currentCommunityNo = null; // 커뮤니티번호 여러곳에서 사용을 위한 전역변수
    let noPosts = true;            // 게시글 유무에 따라 다르게 출력하기 위한 전역변수

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

    // 가입커뮤니티, 전체커뮤니티 탭 클릭 시 콘텐츠 전환
    const tabs = document.querySelectorAll('.nav-link');
    const tabContents = document.querySelectorAll('.tab-pane');

    tabs.forEach(function(tab) {
        tab.addEventListener('click', function() {
            const targetId = event.target.getAttribute('data-bs-target');
            const targetContent = document.querySelector(`#${targetId}`);

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

    // 좌측 가입커뮤니티 목록 클릭 시 좌측 내용 동적으로 변경
    const communityItems = document.querySelectorAll('.community-item');
    const communityLeft = document.querySelector('#community-left');

    communityItems.forEach(function(item) {
        item.addEventListener('click', function() {
            const communityNo = this.getAttribute('data-community-no');
            currentCommunityNo = communityNo;
            loadCommunityDetails(communityNo); // 아래 이벤트 호출
        });
    });

    // 좌측 가입커뮤니티 목록 클릭 시 좌측 내용 동적으로 변경 (AJAX를 통해 서버에서 데이터 가져오기)
    function loadCommunityDetails(communityNo) {
        communityLeft.innerHTML = '';
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

                <button class="btn" id="leave-btn">
                    <i class="fas fa-user-minus fa-1x"></i> 탈퇴
                </button>
            </div>
        `;

        communityLeft.innerHTML = communityContent;

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

                function boardNumberPostList(boardNo) {
                    articlesList.innerHTML = '';
                    let cnt = 0;

                    const tableHeader = `
                        <div class="table-responsive">
                            <table id="add-row" class="table table-hover display">
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
                    articlesList.innerHTML = tableHeader;

                    // 우측에 게시글 목록 출력
                    $.ajax({
                        url: `/sep/${companyNo}/community/getBoardNoPost`,
                        data: { boardNo: boardNo },
                        success: function(data) {
							console.log("멤버넘버를 찾아라~~", data);
                            data.boardNoPost.forEach(function(boardNoPosts) {
                                boardNoPosts.board.forEach(function(boards) {

                                    if(boards.post.length === 0){
                                        noPosts = true;
                                    }else{
                                        noPosts = false;
                                        boards.post.forEach(function(boardList) {
											console.log("멤버넘버어어어", boardList.memberNo);
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
                        }
                    });

					// 게시글 목록 클릭 시 상세페이지 진입
                    tableBody.addEventListener('click', function(e){
                        articlesList.innerHTML = '';
                        const tr = e.target.closest('tr.post-item');
                        const postNo = tr.getAttribute('data-post-no');

						const inputElement = tr.querySelector('input[data-member-no]');
						const memberNo = inputElement.getAttribute('data-member-no');
                        boardPostDetail(postNo, memberNo); // 아래 함수 호출
                    });

                    function boardPostDetail(postNo, memberNo){
                        const boardPostPage = `
                            <div class="notice-container">
                                <div class="button-group">
                                    <button type="button" class="btn btn-sm btn-info">Update</button>
                                </div>

                                <div class="notice-info">
                                    <strong name="boardNm"></strong>
                                </div>

                                <div class="notice-title" name="postTitle"></div>

                                <div class="notice-info">
                                    <span name="postEmpNm"></span>

									<div class="icon-view-count">
									    <i class="fas fa-thumbs-up"></i>
									    <span name="postLikeCount" id="postLikeCount"></span>

									    <i class="fas fa-eye"></i>
									    <span name="postViewCount"></span>
									</div>
                                </div>

                                <div class="notice-container">
                                    <i class="bi bi-arrow-through-heart" id="heart-icon"></i>
                                    <i class="bi bi-arrow-through-heart-fill" id="heart-icon-fill"></i>

                                    <div class="notice-content" name="postContent"></div>
                                </div>

                                <div class="comment-section">
                                    <div class="comment-input-container">
                                        <div class="comment-input">
                                            <textarea id="commentText" class="form-control" rows="3" placeholder="댓글을 입력하세요..."></textarea>
                                        </div>
                                    </div>
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <button class="btn btn-sm btn-primary me-md-2" type="button" id="submitCommentBtn">댓글 작성</button>
                                    </div>
                                </div>
                            </div>
                        `;
                        articlesList.innerHTML = boardPostPage;

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
										console.log("아이템",postItem);
                                        document.querySelector('[name="postTitle"]').textContent = postItem.postTitle
                                        document.querySelector('[name="postViewCount"]').textContent = postItem.postViewCount
                                        document.querySelector('[name="postLikeCount"]').textContent = postItem.postLikeCount
                                        document.querySelector('[name="postContent"]').textContent = postItem.postContent
                                    });
                                });

                                document.querySelector('[name="postTitle"]').testContent = data.title
                            }
                        })

                        // 좋아요 아이콘 색 부드럽게 전환
						let isLiked = false;
						$.ajax({
							url: `/sep/${companyNo}/community/postLiktStatus`,
							data: { postNo: postNo, memberNo: memberNo },
							success: function(data){
								isLiked = data.isLiked;

						        if (isLiked) {
						            document.querySelector('#heart-icon').style.opacity = 0;
						            document.querySelector('#heart-icon-fill').style.opacity = 1;
						        } else {
						            document.querySelector('#heart-icon').style.opacity = 1;
						            document.querySelector('#heart-icon-fill').style.opacity = 0;
						        }
						    }
						});
                        document.querySelector('#heart-icon-fill').addEventListener('click', function(){
							const url = `/sep/${companyNo}/community/postLike`;
							const method = 'POST';

							// 좋아요 취소
							if(isLiked){
								document.querySelector('#heart-icon').style.opacity = 1;
								document.querySelector('#heart-icon-fill').style.opacity = 0;
								isLiked = false;
								$.ajax({
									url: url,
									method: method,
									data: { postNo: postNo, memberNo: memberNo, isLiked: false },
									success: function(data){
										console.log("data", data)
										document.querySelector('[name="postLikeCount"]').textContent = data.likeCount;

									}
								})
							// 좋아요 등록
							}else{
								document.querySelector('#heart-icon').style.opacity = 0;
								document.querySelector('#heart-icon-fill').style.opacity = 1;
								isLiked = true;
								$.ajax({
									url: url,
									method: method,
									data: { postNo: postNo, memberNo: memberNo, isLiked: true },
									success: function(data){
										console.log("data", data)
										document.querySelector('[name="postLikeCount"]').textContent = data.likeCount;

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
    }








    // 좌측 가입커뮤니티 목록 클릭 시 오른쪽 내용 동적으로 변경 (AJAX를 통해 서버에서 데이터 가져오기)
    const allCommunities = document.querySelector('#all-communities');

    communityItems.forEach(function(item) {
        item.addEventListener('click', function() {
            const communityNo = this.getAttribute('data-community-no');
            loadCommunityBoard(communityNo); // 아래 이벤트 호출
        });
    });

    function loadCommunityBoard(communityNo) {
        communityTabs.style.display = 'none';
        allCommunities.innerHTML = '';
        articlesList.innerHTML = '';
        let cnt = 0;
        const tableHeader = `
            <div class="table-responsive">
                <table id="add-row" class="table table-hover display">
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
        articlesList.innerHTML = tableHeader;
        const tableBody = document.querySelector('#tableBody');
        // 우측에 게시글 목록 출력
        $.ajax({
            url: `/sep/${companyNo}/community/getPost`,
            data: { communityNo: communityNo },
            success: function(data) {

                data.board.forEach(function(boards) {
                    boards.board.forEach(function(boardItem) {
                        if (boardItem.post.length === 0) {
                            noPosts = true;
                        } else {
                            noPosts = false;
                            boardItem.post.forEach(function(postItem) {
								console.log("포스트아이템", postItem.memberNo);
                                const communityContent = `
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
                                tableBody.innerHTML += communityContent;
                            });
                        }
                    });
                });
                // 등록된 게시글이 없을 경우 출력
                if (noPosts) {
                    tableBody.innerHTML = '<tr><td colspan="8" style="text-align: center;">등록된 게시글이 없습니다.</td></tr>';
                }
            }
        });

        tableBody.addEventListener('click', function(e){
            articlesList.innerHTML = '';
            const tr = e.target.closest('tr.post-row');
            const postNo = tr.getAttribute('data-post-id');

			const inputElement = tr.querySelector('input[data-member-no]');
			const memberNo = inputElement.getAttribute('data-member-no');
			console.log("나올까요~~~??", memberNo);
            postDetail(postNo, memberNo); // 아래 함수 호출
        });

        function postDetail(postNo, memberNo){
            const detailPage = `
                <div class="notice-container">
                    <div class="button-group">
                        <button type="button" class="btn btn-sm btn-info">Update</button>
                    </div>

                    <div class="notice-info">
                        <strong name="boardNm"></strong>
                    </div>

                    <div class="notice-title" name="postTitle"></div>

					<div class="notice-info">
					    <span name="postEmpNm"></span>

						<div class="icon-view-count">
						    <i class="fas fa-thumbs-up"></i>
						    <span name="postLikeCount" id="postLikeCount"></span>

						    <i class="fas fa-eye"></i>
						    <span name="postViewCount"></span>
						</div>
					</div>

                    <div class="notice-container">
                        <i class="bi bi-arrow-through-heart" id="heart-icon"></i>
                        <i class="bi bi-arrow-through-heart-fill" id="heart-icon-fill"></i>

                        <div class="notice-content" name="postContent"></div>
                    </div>

                    <div class="comment-section">
                        <div class="comment-input-container">
                            <div class="comment-input">
                                <textarea id="commentText" class="form-control" rows="3" placeholder="댓글을 입력하세요..."></textarea>
                            </div>
                        </div>
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <button class="btn btn-sm btn-primary me-md-2" type="button" id="submitCommentBtn">댓글 작성</button>
                        </div>
                    </div>
                </div>
            `;

            articlesList.innerHTML = detailPage;

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
							document.querySelector('[name="postLikeCount"]').textContent = postItem.postLikeCount
							document.querySelector('[name="postContent"]').textContent = postItem.postContent
                        });
                    });

                    document.querySelector('[name="postTitle"]').testContent = data.title
                }
            })

			// 좋아요 아이콘 색 부드럽게 전환
			let isLiked = false;
			$.ajax({
				url: `/sep/${companyNo}/community/postLiktStatus`,
				data: { postNo: postNo, memberNo: memberNo },
				success: function(data){
					isLiked = data.isLiked;

			        if (isLiked) {
			            document.querySelector('#heart-icon').style.opacity = 0;
			            document.querySelector('#heart-icon-fill').style.opacity = 1;
			        } else {
			            document.querySelector('#heart-icon').style.opacity = 1;
			            document.querySelector('#heart-icon-fill').style.opacity = 0;
			        }
			    }
			});
			document.querySelector('#heart-icon-fill').addEventListener('click', function(){
				const url = `/sep/${companyNo}/community/postLike`;
				const method = 'POST';

				// 좋아요 취소
				if(isLiked){
					document.querySelector('#heart-icon').style.opacity = 1;
					document.querySelector('#heart-icon-fill').style.opacity = 0;
					isLiked = false;
					$.ajax({
						url: url,
						method: method,
						data: { postNo: postNo, memberNo: memberNo, isLiked: false },
						success: function(data){
							console.log("data", data)
							document.querySelector('[name="postLikeCount"]').textContent = data.likeCount;

						}
					})
				// 좋아요 등록
				}else{
					document.querySelector('#heart-icon').style.opacity = 0;
					document.querySelector('#heart-icon-fill').style.opacity = 1;
					isLiked = true;
					$.ajax({
						url: url,
						method: method,
						data: { postNo: postNo, memberNo: memberNo, isLiked: true },
						success: function(data){
							console.log("data", data.likeCount);
							document.querySelector('[name="postLikeCount"]').textContent = data.likeCount;

						}
					})
				}
			});
        }
    }

    // 방 만들기
    const roomInsert = document.querySelector('#roomInsert');
    roomInsert.addEventListener('click', function() {
        roomInsertForm(); // 아래 코드 호출
    });

    function roomInsertForm() {
        communityTabs.style.display = 'none';
        allCommunities.innerHTML = '';
        articlesList.innerHTML = '';
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
        communityContent.innerHTML = roomInsertForm;
    }

    // 게시판 만들기 form
    function boardInsert() {
        communityTabs.style.display = 'none';
        allCommunities.innerHTML = '';
        articlesList.innerHTML = '';

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
        communityContent.innerHTML = boardInsertForm;

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
                    window.location.href = `/sep/${companyNo}/community`;
                }
            });
        });
    }

});
