<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 18.     	손현진            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Login Form -->
<section class="section-padding">
<div class="container">
    <form id="findIdForm">
        <div class="sepimg">
            <img src="${pageContext.request.contextPath}/resources/sepgruppe/images/LOGO.png" class="img-thumbnail" id="sep">
            <h5>회원가입 시 입력한 이름과 이메일 주소를 입력해주세요.</h4>
        </div>
        
        <div class="form-floating col-6 mx-auto mb-3">
        <input type="text" class="form-control" id="floatingName" name="contactNm" placeholder="이름">
        <label for="floatingName">이름</label>
    </div>
    <div class="form-floating col-6 mx-auto mb-3">
        <input type="email" class="form-control" id="floatingEmail" name="contactEmail" placeholder="이메일">
        <label for="floatingEmail">이메일</label>
    </div>
    <div class="d-grid gap-2 col-6 mx-auto mb-3">
        <button class="btn btn-success" type="button" id="findIdBtn">확인</button>
    </div>
         
    </form>
    <div id="resultMessage" class="text-center mt-3"></div>
</div>
</section>

<script>
document.getElementById("findIdBtn").addEventListener("click", function () {
    const name = document.getElementById("floatingName").value.trim();
    const email = document.getElementById("floatingEmail").value.trim();
    const resultMessage = document.getElementById("resultMessage");

    if (!name || !email) {
        resultMessage.textContent = "이름과 이메일을 입력해주세요.";
        resultMessage.style.color = "red";
        return;
    }

    fetch("/sep/login/findId", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ contactNm: name, contactEmail: email })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) { 
            resultMessage.innerHTML = `
						                ✅ 찾은 아이디: <strong>\${data.contactId}</strong> <br>
						                <a href="<c:url value='/login'/>" class="btn btn-success">로그인하러 가기</a>
						            `;
            resultMessage.style.color = "green";
        } else {
            resultMessage.textContent = `❌ \${data.message}`;
            resultMessage.style.color = "red";
        }
    })
    .catch(error => {
        console.error("Error:", error);
        resultMessage.textContent = "⚠️ 서버 오류가 발생했습니다.";
        resultMessage.style.color = "red";
    });
});
</script>
