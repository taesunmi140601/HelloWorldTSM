<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 24.     	SJH            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설문 등록</title>
    <link href="${pageContext.request.contextPath}/resources/groupware/css/survey/surveyCreate.css" rel="stylesheet">
</head>
<body>
<div class="container">
	
	<!-- ✅ 목록으로 돌아가기 버튼 추가 -->
    <div class="text-right mb-3">
        <a href="${pageContext.request.contextPath}/${companyNo}/surveyApi/list" class="btn btn-outline-secondary btn-sm">
            <h3>← 목록으로</h3>
        </a>
    </div>
    
    <h2>설문 등록</h2>
    <form id="surveyForm">
        <label>설문 제목</label>
        <input type="text" id="title" required>

        <label>설문 설명</label>
        <textarea id="description" required></textarea>

        <label>설문 유형</label>
        <select id="surveyType" required>
            <option value="default">기본 설문</option>
            <option value="candidate">후보 설문</option>
            <option value="yesno">찬반 설문</option>
            <option value="multiple">복수 설문</option>
        </select>

        <!-- 기본 설문 -->
        <div id="defaultForm" class="form-type active">
            <button type="button" onclick="addQuestion()">기본 질문 추가</button>
        </div>

        <!-- 후보 설문 -->
		<div id="candidateForm" class="form-type">
		    <label>후보자 설문</label>
		
		    <!-- 후보 주제 입력 영역 추가 -->
		    <input type="text" id="candidateTopic" placeholder="어떤 후보에 대한 것인지 입력하세요." class="form-control mb-2">
		
		    <div id="candidateList">
		        <input type="text" placeholder="후보자 이름" class="form-control mb-2">
		    </div>
		    <button type="button" class="btn btn-outline-primary btn-sm" onclick="addCandidate()">후보 추가</button>
		</div>

        <!-- 찬반 설문 -->
        <div id="yesnoForm" class="form-type">
            <label>찬반 주제</label>
            <input type="text" id="yesnoTopic" placeholder="예: 사내 카페 운영 찬반">
        </div>

        <!-- 복수 설문 -->
        <div id="multipleForm" class="form-type">
            <button type="button" onclick="addMultipleQuestion()">복수 질문 추가</button>
        </div>

        <button type="submit">설문 생성</button>
    </form>
</div>

<script>
        document.getElementById('surveyType').addEventListener('change', function() {
            // 모든 설문 폼 숨기기
            document.querySelectorAll('.form-type').forEach(form => {
                form.style.display = "none";
            });

            // 선택된 폼 보이기
            let selectedForm = document.getElementById(this.value + 'Form');
            if (selectedForm) {
                selectedForm.style.display = "block";
            }
        });

        // 기본 설문 질문 추가 (객관식 + 주관식)
        function addQuestion() {
            const container = document.createElement('div');
            container.className = 'question-container';
            container.innerHTML = `
                <select class="question-type" onchange="toggleQuestionType(this)">
                    <option value="single_choice">객관식</option>
                    <option value="open_ended">주관식</option>
                </select>
                <input type="text" class="question-text" placeholder="질문을 입력하세요" required>
                <div class="options">
                    <div class="option-item">
                        <input type="text" placeholder="옵션">
                        <button type="button" class="delete" onclick="removeElement(this)">✖</button>
                    </div>
                </div>
                <button type="button" class="btn-add" onclick="addOption(this)">옵션 추가</button>
                <button type="button" class="delete" onclick="removeElement(this)">질문 삭제</button>
            `;
            document.getElementById('defaultForm').appendChild(container);
            toggleQuestionType(container.querySelector('.question-type'));
        }

        // 질문 유형에 따라 .options, 옵션 추가 버튼 표시/숨김
        function toggleQuestionType(selectElement) {
            const container = selectElement.parentElement;
            const optionsDiv = container.querySelector('.options');
            const optionButton = container.querySelector('.btn-add');

            if (selectElement.value === 'open_ended') {
                optionsDiv.style.display = 'none';
                optionButton.style.display = 'none';
            } else {
                optionsDiv.style.display = 'block';
                optionButton.style.display = 'inline-block';
            }
        }

        // 옵션 추가
        function addOption(btn) {
            const optionsDiv = btn.previousElementSibling;
            const optionCount = optionsDiv.children.length + 1;
            const optionItem = document.createElement('div');
            optionItem.className = 'option-item';
            optionItem.innerHTML = `
                <input type="text" placeholder="옵션 ${optionCount}">
                <button type="button" class="delete" onclick="removeElement(this)">✖</button>
            `;
            optionsDiv.appendChild(optionItem);
        }

        // 후보자 추가
        function addCandidate() {
            const candidateDiv = document.createElement('div');
            candidateDiv.className = 'question-box';
            candidateDiv.innerHTML = `
                <div class="options">
                    <div class="option-item">
                        <input type="text" placeholder="후보자 이름" required>
                        <button type="button" class="delete" onclick="removeElement(this)">✖</button>
                    </div>
                </div>
            `;
            document.getElementById('candidateList').appendChild(candidateDiv);
        }

        // 복수 설문 질문 추가
        function addMultipleQuestion() {
            const container = document.createElement('div');
            container.className = 'multiple-question';
            container.innerHTML = `
                <input type="text" placeholder="질문을 입력하세요" required>
                <input type="number" min="1" placeholder="선택 가능한 개수" required>
                <div class="multiple-options">
                    <div class="option-item">
                        <input type="text" placeholder="옵션">
                        <button type="button" class="delete" onclick="removeElement(this)">✖</button>
                    </div>
                </div>
                <button type="button" class="btn-add" onclick="addMultipleOption(this)">옵션 추가</button>
                <button type="button" class="delete" onclick="removeElement(this)">질문 삭제</button>
            `;
            document.getElementById('multipleForm').appendChild(container);
        }

        // 복수 설문 옵션 추가
        function addMultipleOption(btn) {
            const optionsDiv = btn.previousElementSibling;
            const optionCount = optionsDiv.children.length + 1;
            const optionItem = document.createElement('div');
            optionItem.className = 'option-item';
            optionItem.innerHTML = `
                <input type="text" placeholder="옵션 ${optionCount}">
                <button type="button" class="delete" onclick="removeElement(this)">✖</button>
            `;
            optionsDiv.appendChild(optionItem);
        }

        // 요소 삭제
        function removeElement(btn) {
            btn.closest('.question-container, .option-item, .question-box, .multiple-question').remove();
        }
    </script> 
    
    <script>
    // ✅ 백엔드 프록시를 통한 설문 생성 방식으로 변경
    document.getElementById("surveyForm").addEventListener("submit", function(event) {
        event.preventDefault();
        submitSurvey();
    });

    async function submitSurvey() {
        const title = document.getElementById("title").value;
        const description = document.getElementById("description").value;
        const surveyType = document.getElementById("surveyType").value;

        let questions = [];

        if (surveyType === "default") {
            questions = Array.from(document.querySelectorAll("#defaultForm .question-container")).map(q => {
                const qType = q.querySelector(".question-type")?.value || "single_choice";
                const questionText = (q.querySelector(".question-text")?.value || "").trim();
                if (!questionText) return null;

                if (qType === "single_choice") {
                    return {
                        headings: [{ heading: questionText }],
                        family: "single_choice",
                        subtype: "vertical",
                        answers: {
                            choices: Array.from(q.querySelectorAll(".options input")).map(opt => ({ text: opt.value })),
                            other: []
                        }
                    };
                } else {
                    return {
                        headings: [{ heading: questionText }],
                        family: "open_ended",
                        subtype: "single",
                        answers: {}
                    };
                }
            }).filter(q => q !== null);

            if (questions.length === 0) return;

        } else if (surveyType === "candidate") {
            const candidateTopic = document.getElementById("candidateTopic").value;

            questions = [{
                headings: [{ heading: candidateTopic || "후보자 선택" }],
                family: "single_choice",
                subtype: "vertical",
                answers: {
                    choices: Array.from(document.querySelectorAll("#candidateList input")).map(input => ({ text: input.value }))
                }
            }];
        } else if (surveyType === "yesno") {
            questions = [{
                headings: [{ heading: document.querySelector("#yesnoForm input[type='text']").value }],
                family: "single_choice",
                subtype: "vertical",
                answers: {
                    choices: [{ text: "찬성" }, { text: "반대" }]
                }
            }];

        } else if (surveyType === "multiple") {
            questions = Array.from(document.querySelectorAll("#multipleForm .multiple-question")).map(q => ({
                headings: [{ heading: q.querySelector("input[type='text']").value }],
                family: "multiple_choice",
                subtype: "vertical",
                answers: {
                    choices: Array.from(q.querySelectorAll(".multiple-options input")).map(opt => ({ text: opt.value })),
                    other: []
                }
            }));
        }

        const surveyData = {
            title,
            language: "ko",
            pages: [{
                title: "설문 페이지",
                description,
                questions
            }]
        };

        try {
            const response = await fetch("${pageContext.request.contextPath}/{companyNo}/surveyApi/surveyCreate", {
            	  method: "POST",
            	  headers: { "Content-Type": "application/json" },
            	  body: JSON.stringify(surveyData)
            	});

            const data = await response.json();
            if (!response.ok) throw new Error(JSON.stringify(data));

            alert("✅ 설문 생성 완료!");
            location.href = "${pageContext.request.contextPath}/{companyNo}/surveyApi/list"; // 실제 회사번호 바인딩 필요
        } catch (err) {
            console.error("🚨 설문 생성 실패:", err);
            alert("❌ 오류 발생: " + err.message);
        }
    }
</script>
</body>
</html>
