<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<header>
    <security:authentication property="principal" var="principal"/> <!-- Provider 시큐리티 정보 -->
    <%-- <h1>${principal }</h1> --%>

    <%-- <c:set value="${principal.realUser.target}" var="userTarget"/> --%>
    <%-- <p>userTarget: ${userTarget}</p> --%>
    <%-- <h1>${isAuthenticated() }</h1> --%>

    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand" href="/sep">
                <img id="seplogo" alt="tree" src="${pageContext.request.contextPath }/resources/sepgruppe/images/favicon.png">
            </a>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-lg-5 me-lg-auto">
                    <li class="nav-item">
                        <c:choose>
                            <c:when test="${sessionScope.isAdmin}">
                                <a class="nav-link active" id="btnNonDrop" href="/sep/provider">대시보드</a>
                                <a class="nav-link active" id="btnNonDrop" href="/sep/company">고객사 목록</a>
                            </c:when>
                        </c:choose>
                    </li>
                    <li class="nav-item">
                        <c:choose>
                            <c:when test="${sessionScope.isAdmin}">
                                    <a class="nav-link active" id="navbarLightDropdownMenuLink" href="/sep/subscriptionPlan/manage">구독관리</a>
                            </c:when>
                            <c:otherwise>
                                <a class="nav-link active" href="/sep/subscriptionPlan">PRODUCT</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    <li class="nav-item">
                        <c:choose>
                            <c:when test="${sessionScope.isAdmin}">
                                    <a class="nav-link active"  id="navbarLightDropdownMenuLink" href="/sep/payment">자동결제내역</a>
                            </c:when>
                            <c:otherwise>
                                <a class="nav-link active" href="/sep">PROCESS</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    <li class="nav-item">
                        <c:choose>
                            <c:when test="${sessionScope.isAdmin}">
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </li>

<!-- 로그인 안되어있을때 -->

                <security:authorize access="isAnonymous()">
                    <li class="nav-item">
                        <a class="nav-link active" id="btnNonDrop" href="/sep/login">LOGIN</a>
                    </li>
                </security:authorize>
                </ul>


<!-- 로그인이 되어있을 때 -->
                <security:authorize access="isAuthenticated()">
                    <c:choose>
                        <c:when test="${principal.realUser.target eq 'COMPANY' }">
                            <div class="d-none d-lg-block dropdown">
                                <a class="navbar-icon bi-person" data-bs-toggle="dropdown" aria-expanded="false" id="navbarLightDropdownMenuLink"></a>
                                <ul class="dropdown-menu dropdown-menu-light" aria-labelledby="navbarLightDropdownMenuLink">
                                    <li><a class="dropdown-item" href="/sep/company/${principal.realUser.companyNo}/mypage">마이페이지</a></li> <!-- 시큐리티에 저장된 로그인한 ID 가져오기 -->
                                    <li><a class="dropdown-item" href="<c:url value='/login/logout'/>">로그아웃</a></li>
                                </ul>
                            </div>
                        </c:when>
                        <c:when test="${principal.realUser.target eq 'PROVIDER' }">
                            <div class="d-none d-lg-block dropdown">
                                <a class="navbar-icon bi-person" data-bs-toggle="dropdown" aria-expanded="false" id="navbarLightDropdownMenuLink"></a>
                                <ul class="dropdown-menu dropdown-menu-light" aria-labelledby="navbarLightDropdownMenuLink">
                                    <li><a class="dropdown-item" href="/sep/provider">관리자모드</a></li>
                                    <li><a class="dropdown-item" href="<c:url value='/login/logout'/>">로그아웃</a></li>
                                    <li><a class="dropdown-item" href="<c:url value='/alarm/send'/>" 
                                    		data-bs-toggle="modal" data-bs-target="#exampleModal1"/>알림작성</a></li>
                                </ul>
                            </div>
                        </c:when>
                    </c:choose>
                </security:authorize>

            </div>
        </div>
    </nav>
    
    <!-- Modal -->
<div class="modal fade" id="exampleModal1" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">알림작성</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
</header>

<script>
document.addEventListener("DOMContentLoaded", ()=>{
	const myModalEl = document.querySelector('#exampleModal');
	myModalEl.addEventListener('show.bs.modal', event => {
		let aTag = event.relatedTarget;
		let url = aTag.href; 
		
		$.ajax({
			url:url,
			dataType:"html",
			success:function(resp){
				$(myModalEl).find(".modal-body").html(resp);
			}
		});
	})
});
</script>
