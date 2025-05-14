
    // 서버에서 전달한 값이 없으면 기본값 설정
    var roomId = null;
    const userName = $('#myId').data('id');
    const empName = $('#myId').data('name');
	const chatStateMap = {};
    var stompClient = null;

	// 서버에서 전달한 subscribedRooms가 없으면 빈 배열로 초기화
	var subscribedRooms = (typeof subscribedRooms !== 'undefined') ? subscribedRooms : [];
	// 각 roomId별 구독 객체를 저장할 객체
	var subscriptions = {};

	function chatApp(){
		// 엔터키로 메시지 전송 (Shift+Enter는 줄바꿈)
       $("#messageInput").on("keypress", function(event){
           if(event.key === "Enter" && !event.shiftKey){
               event.preventDefault();
               sendMessage();
           }
       });
       // 전송 버튼 클릭 시 메시지 전송
       $("#sendBtn").click(function(){
           sendMessage();
       });
	}

    // STOMP 연결 함수
	function connect() {
		var socket = new SockJS("/sep/stomp");
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log("Connected: " + frame);
			// 채팅방 구독: 서버에서 /topic/{roomId}로 메시지 브로드캐스트
			if (subscribedRooms && subscribedRooms.length > 0) {
				subscribedRooms.forEach(function(room) {
					subscribeToChatRoom(room.roomId);
				});
			}

			// 만약 현재 활성화된 roomId가 있으면 (예: 새로 선택한 채팅방)
			if (roomId) {
				subscribeToChatRoom(roomId);
			}
		}, function(error) {
	            console.error("STOMP 연결 에러: ", error);
	        });
	    }

    // 메시지 전송 함수
    function sendMessage() {
        var messageInput = document.getElementById("messageInput");
        var messageText = messageInput.value.trim();
        if(messageText !== "" && stompClient && stompClient.connected) {
            var chatMessage = {
                msgId: Date.now(),
                senderEmpId: userName,
                senderName: empName,
                msgContent: messageText,
				sendTime:new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' }),
				sendDate:new Date().toISOString().split('T')[0],
                roomId: roomId
            };
            // 서버의 메시지 핸들러로 전송 (/app/{roomId} 경로 사용)
            stompClient.send("/app/" + roomId, {}, JSON.stringify(chatMessage));
            messageInput.value = "";
        }
    }

    // 수신한 메시지를 채팅창에 추가
    function showMessage(message) {
        var chatBox = document.getElementById("chatBox");
        var li = document.createElement("li");

		if (!chatStateMap[message.roomId]) {
	        chatStateMap[message.roomId] = {
	            lastDisplayedDate: "",
	            lastDisplayedTime: ""
	        };
	    }

		const state = chatStateMap[message.roomId];
        // 본인이 보낸 메시지와 타인의 메시지 구분
        if(message.senderEmpId === userName){
            li.className = "chat-right";
        } else {
            li.className = "chat-left";
        }
		var timeHTML = "";

		if (message.sendDate !== state.lastDisplayedDate) {
	        timeHTML = message.sendDate + " " + message.sendTime + ' <span class="fa fa-check-circle"></span>';
	        state.lastDisplayedDate = message.sendDate;
	        state.lastDisplayedTime = message.sendTime;
	    } else if (message.sendTime !== state.lastDisplayedTime) {
	        timeHTML = message.sendTime + ' <span class="fa fa-check-circle"></span>';
	        state.lastDisplayedTime = message.sendTime;
	    }

		li.innerHTML = '<div class="chat-hour">' + timeHTML + '</div>' +'<div class="chat-avatar">' +
		            '<img src="/sep/resources/groupware/images/profile.png" alt="Avatar" />' +
		            '<div class="chat-name">(' + (message.organization.deptName || message.senderEmpId) +') '
					+ message.organization.empNm + ' ' + message.organization.positionName +'</div>' + '</div>' +
		        '<div class="chat-text">' + message.msgContent + '</div>';

        chatBox.appendChild(li);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

	// 개별 채팅방 구독 함수
	function subscribeToChatRoom(roomId) {
	    if (stompClient && stompClient.connected) {
	        // 이미 해당 채팅방에 대해 구독 중이면 재구독하지 않음
	        if (subscriptions[roomId]) {
	            console.log("이미 구독 중인 채팅방:", roomId);
	            return;
	        }
	        // 구독하고 subscription 객체를 저장
	        subscriptions[roomId] = stompClient.subscribe("/topic/" + roomId, function(message) {
				const parsedMessage = JSON.parse(message.body); // JSON 파싱

			    showMessage(parsedMessage);

	        });
	        console.log("Subscribed to chat room:", roomId);
	    }
	}

    // 페이지 로드 시 STOMP 연결 및 이벤트 바인딩
    $(document).ready(function(){
        connect();
		chatApp();


    });

	$(document).on("click", "li.chat-room-row", function(){
	    var selectedRoomId = $(this).data("roomid");
		const toName = $(this).find(".chat-room-name").data("roomname");
	    roomId = selectedRoomId;  // 전역 roomId 변수에 선택된 값을 할당
	    subscribeToChatRoom(roomId);

	    // AJAX 요청을 통해 해당 채팅방 UI(JSP)를 로드합니다.
	    $.ajax({
	        type: "GET",
	        url: "/sep/chat/" + roomId + "?toName=" + encodeURIComponent(toName),
	        success: function(jspContent) {
	            // 채팅 UI를 #depTree 영역에 삽입
	            $("#depTree").empty().html(jspContent);
	            chatApp(); // 채팅 전송 이벤트 재바인딩
	            $(document).off("click", "#sendBtn").on("click", "#sendBtn", function(){
	                sendMessage();
	            });
				$("#chatBox").scrollTop($("#chatBox").prop("scrollHeight"));
	        },
	        error: function(xhr, status, error) {
	            console.error("채팅 UI 로드 실패:", error);
	        }
	    });
	});

    // 페이지 종료 시 연결 해제
    window.onbeforeunload = function(){
        if(stompClient) {
            stompClient.disconnect();
        }
    };



// ===================================================================================================



	$(document).ready(function(){
	$("#depTree").fancytree({
	    source: {
	        url:"/sep/testnum001" + "/organization/admin/parentDep",
	        cache: false
	    },
	    lazyLoad: function(event, data) {
	        var node = data.node;
	        let mode = "employee";
	        if (!node.data.parentDeptCd) {
	            mode = "department";
	        }
	        // Load child nodes via Ajax
	        data.result = {
	            url:  "/sep/testnum001" + "/organization/admin/childeDep",
	            data: {mode: mode, parent: node.key},
	            cache: false
	        };
	    },
	    renderNode: function(event, data) {
	        var node = data.node;
	        var $span = $(node.span);

	     // 기존 아이콘 제거 (중복 방지)
	     $span.find(".fancytree-icon").remove();
	        if (node.data.empNm) {
	            // 사원 노드
	        const isManager = node.parent && node.parent.data.managerEmpId === node.data.empId;

	          const iconClass = isManager ? "fas fa-user-tie" : "fas fa-user";
	          const iconHtml = `<i class="${iconClass} fancytree-icon"></i>`;

	          $span.find(".fancytree-title").html(
	              `${iconHtml} ${node.data.empNm} (${node.data.positionName}) (${node.data.empId})`
	          );

	     } else {
	           // 부서 노드
	           $span.find(".fancytree-title").prepend(
	               `<i class="fas fa-building fancytree-icon"></i> `
	           );
	        }
	    },
		activate: function(event, data) {
			var node = data.node;
			if (node.data.empNm) {
				// 사원 정보 표시
				var myUsername = userName; // 전역 변수 (현재 로그인 사용자명)
				var empId = node.data.empId;
				var ids = [myUsername, empId].sort();
				roomId = ids.join("_");
				subscribeToChatRoom(roomId);

				$.ajax({
					type: "POST",
					url: "/sep/testnum001/chat/register",
					contentType: "application/json",
					data: JSON.stringify({
						createEmpId: myUsername,
						roomId: roomId
					}),
					success: function(response) {
						console.log("채팅방 참여 등록 성공:", response);

						$.ajax({
							type: "GET",  // 필요에 따라 POST로 변경 가능
							url: "/sep/chat/"+ roomId + "?toName=" + encodeURIComponent(response.toName),  // 받아올 JSP 페이지의 URL로 변경하세요
							success: function(jspContent) {
								// 기존 조직도 제거 후, 받아온 JSP 내용을 #depTree에 삽입
								$("#depTree").empty().html(jspContent);
								chatApp();
								$(document).on("click", "#sendBtn", function(){
								    sendMessage();
								});
							},
							error: function(xhr, status, error) {
								console.error("JSP 페이지 로딩 실패:", error);
							}
						});
					},
					error: function(xhr, status, error) {
						console.error("채팅방 참여 등록 실패:", error);
					}

				});
	        }
	    }
	});

	});
	$(document).ready(function(){
	        $('#listLink').on('click', function(event) {
	            event.preventDefault();  // 기본 페이지 이동 막기

	            var url = $(this).attr('href');

	            $.ajax({
	                url: url,
	                method: 'GET',
	                success: function(response) {
	                    // AJAX 요청 성공 시 처리할 로직
	                    // 예를 들어, 응답 데이터를 특정 영역에 출력한다.
	                    console.log('응답:', response);
	                    // 예: $('#resultContainer').html(response);
	                    $("#depTree").empty().html(response);
						setTimeout(updateChatTime, 0);
	                },
	                error: function(xhr, status, error) {
	                    // 에러 처리 로직
	                    console.error('AJAX 요청 에러:', error);
	                }
	            });
	        });
	    });

		$(document).on("click", ".btn.btn-outline-info", function () {
		    $("#listLink").trigger("click");
		});
		function updateChatTime() {
		    const timeElements = document.querySelectorAll(".chat-last-time");
		    timeElements.forEach(el => {
		        const dateStr = el.dataset.senddate;
		        const timeStr = el.dataset.sendtime;

		        if (!dateStr || !timeStr) {
		            el.textContent = "--";
		            return;
		        }

		        const [ampm, hm] = timeStr.split(" ");
		        const [hourStr, minuteStr] = hm.split(":");
		        let hour = Number(hourStr);
		        const minute = Number(minuteStr);

		        if (ampm === "오후" && hour < 12) hour += 12;
		        if (ampm === "오전" && hour === 12) hour = 0;

		        const fullDateTimeStr = `${dateStr}T${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:00`;
		        const messageTime = new Date(fullDateTimeStr);
		        if (isNaN(messageTime)) {
		            el.textContent = "--";
		            return;
		        }

		        const now = new Date();
		        const diffMillis = now - messageTime;

		        const diffMinutes = Math.floor(diffMillis / (1000 * 60));
		        const diffHours = Math.floor(diffMinutes / 60);
		        const diffDays = Math.floor(diffHours / 24);

		        let display = "";

		        if (diffMinutes < 1) {
		            display = "방금 전";
		        } else if (diffMinutes < 60) {
		            display = `${diffMinutes}분 전`;
		        } else if (diffHours < 24) {
		            display = `${diffHours}시간 전`;
		        } else {
		            display = `${diffDays}일 전`;
		        }

		        el.textContent = display;
		    });
		}

		$(document).on('click', '#leaveRoomBtn', function(event) {
		    event.preventDefault();

		    if (!roomId) {
		        alert("방 정보가 없습니다.");
		        return;
		    }

		    if (!confirm("정말 방을 나가시겠습니까?")) return;

		    $.ajax({
		        url: "/sep/chat/" + roomId, // 예시 URL, 실제 컨트롤러 URL로 바꾸세요
		        type: "DELETE",
		        success: function(response) {
		            alert("채팅방에서 나갔습니다.");
		            // 뒤로가기 동작 수행
		            $("#listLink").trigger("click");
		        },
		        error: function(xhr, status, error) {
		            console.error("방 나가기 실패:", error);
		            alert("방 나가기 중 오류가 발생했습니다.");
		        }
		    });
		});




