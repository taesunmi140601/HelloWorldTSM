<!-- Employee(그룹웨어) Mypage -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<security:authentication property="principal.realUser" var="realUser"/> <!-- Provider 시큐리티 정보 -->
<!-- myPage css 코드 -->
<link href="${pageContext.request.contextPath}/resources/sepgruppe/css/button.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/employee/myEmployee2.css" />
    <script src="https://unpkg.com/lottie-web@latest/build/player/lottie.min.js"></script>
    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>

<!-- 정보변경 탭 -->
<section class="section-padding profile2">
    <div class="row">
        <div class="col-xl-4">
            <div class="card">
                <div class="card-body profile2-card pt-4 d-flex flex-column align-items-center text-center">
                    <!-- 프로필 이미지 -->
                    <c:if test="${empty member.empImg }">
                        <spring:eval expression="@fileInfo.attachFiles" var="attachFiles"/>
                        <img src="<c:url value='${attachFiles }default/defaultImage.jpg'/>" class="rounded-circle" style="max-width: 250px; height: auto;">
                    </c:if>
                    <c:if test="${not empty member.empImg }">
                        <spring:eval expression="@fileInfo.attachFiles" var="attachFiles"/>
                        <img src="<c:url value='${attachFiles }${member.empImg}'/>" class="rounded-circle" style="max-width: 250px; height: auto;">
                    </c:if>
                    <h2>${member.empNm }</h2>
                </div>
            </div>
        </div>
        <div class="col-xl-8">
            <div class="card">
                <div class="card-body pt-3">
                    <!-- 사용자정보, 정보변경 탭 -->
                    <ul class="nav justify-content-start nav-tabs nav-tabs-bordered" id="mypageBtn">
                        <li class="nav-item">
                            <button class="nav-link active" data-bs-target="#profile2-overview">사용자정보</button>
                        </li>
                        <li class="nav-item">
                            <button class="nav-link" data-bs-target="#profile2-edit" id="changeModal">정보변경</button>
                        </li>
                    </ul>

                    <!-- 사용자정보 -->
                    <div class="tab-content pt-2">
                        <div class="tab-pane fade show active profile2-overview" id="profile2-overview">
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">Name</div>
                                <div class="col-lg-9 col-md-8">${member.empNm }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">ID</div>
                                <div class="col-lg-9 col-md-8">${member.empId }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">사원번호</div>
                                <div class="col-lg-9 col-md-8">${member.empNo }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">주소</div>
                                <div class="col-lg-9 col-md-8">${member.empAdd1 }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">상세주소</div>
                                <div class="col-lg-9 col-md-8">${member.empAdd2 }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">이메일주소</div>
                                <div class="col-lg-9 col-md-8">${member.empEmail }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">휴대폰번호</div>
                                <div class="col-lg-9 col-md-8">${member.empPhone }</div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3 col-md-4 label">입사년도</div>
                                <div class="col-lg-9 col-md-8">${member.empRegdate }</div>
                            </div>
                        </div>

                        <!-- 정보변경 탭 -->
                        <div class="tab-pane fade profile2-edit pt-3" id="profile2-edit">
                            <form name="frm" action="/sep/${member.companyNo}/employee/mypage/edit" method="post" enctype="multipart/form-data">
                                <div class="row mb-3">
                                    <label for="fileGroupNo" class="col-md-4 col-lg-3 col-form-label">profile Image</label>
                                    <div class="col-md-8 col-lg-9">
                                        <spring:eval expression="@fileInfo.attachFiles" var="attachFiles"/>
                                        <c:if test="${empty member.empImg }">
                                            <img src="<c:url value='${attachFiles }default/defaultImage.jpg'/>" id="profileImagePreview">
                                        </c:if>
                                        <c:if test="${not empty member.empImg }">
                                            <img src="<c:url value='${attachFiles }${member.empImg}'/>" class="profileImage" name="profileImage" id="profileImagePreview">
                                        </c:if>
                                        <div class="pt-2">
                                            <div class="file-upload">
                                                <input name="attachFile" type="file" class="fileInput" accept="image/jpeg, image/png, image/jpg" id="isFile" style="display: none;">
                                                <button type="button" class="btn button" onclick="document.getElementById('isFile').click();">
                                                    <i class="fas fa-upload"></i>
	                                                <span id="fileName" class="ml-2"></span>
                                                </button>
	                                            <a class="btn buttonDanger" id="deleteBtn">
	                                                <i class="fa fa-trash"></i>
	                                            </a>
                                            </div>
	                                            <span class="text-danger" id="fileError"></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empId" class="col-md-4 col-lg-3 col-form-label">아이디</label>
                                    <div class="col-md-8 col-lg-9">
                                        <div>${member.empId }</div>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empNm" class="col-md-4 col-lg-3 col-form-label">Name</label>
                                    <div class="col-md-8 col-lg-9">
                                        <div>${member.empNm}</div>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empPw" class="col-md-4 col-lg-3 col-form-label">Password</label>
                                    <div class="col-md-8 col-lg-9">
                                        <input id="empPw" name="empPw" type="password" class="form-control"/>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="pwErrorCk" class="col-md-4 col-lg-3 col-form-label">PasswordCheck</label>
                                    <div class="col-md-8 col-lg-9">
                                        <input id="empPwCk" type="password" class="form-control" />
                                        <span class="text-danger" id="pwErrorCk"></span>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empZip" class="col-md-4 col-lg-3 col-form-label">우편번호</label>
                                    <div class="col-md-8 col-lg-9">
                                        <div class="input-group" id="empGroup">
                                            <input type="text" id="sample6_postcode" name="empZip" class="form-control" value="${member.empZip}" readonly>
                                        	<button type="button" onclick="sample6_execDaumPostcode()" class="btn button">
                                        		<i class="fa fa-map-marker"></i>
                                        	</button>
                                        </div>
                                        <span class="text-danger" id="zipError"></span>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empAdd1" class="col-md-4 col-lg-3 col-form-label">주소</label>
                                    <div class="col-md-8 col-lg-9">
                                        <input type="text" id="sample6_address" name="empAdd1" class="form-control" value="${member.empAdd1 }" readonly><br>
                                        <span class="text-danger" id="addrError"></span>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empAdd2" class="col-md-4 col-lg-3 col-form-label">상세주소</label>
                                    <div class="col-md-8 col-lg-9">
                                        <input type="text" name="empAdd2" id="sample6_detailAddress" class="form-control" value="${member.empAdd2 }">
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empEmail" class="col-md-4 col-lg-3 col-form-label">이메일</label>
                                    <div class="col-md-8 col-lg-9">
                                        <input name="empEmail" type="text" class="form-control" value="${member.empEmail }">
                                        <span class="text-danger" id="emailError"></span> <!-- 이메일 오류 메시지 -->
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <label for="empPhone" class="col-md-4 col-lg-3 col-form-label">휴대폰번호</label>
                                    <div class="col-md-8 col-lg-9">
                                        <input name="empPhone" type="text" class="form-control" value="${member.empPhone }">
                                        <span class="text-danger" id="phoneError"></span> <!-- 휴대폰 번호 오류 메시지 -->
                                    </div>
                                </div>

                                <div class="d-grid gap-2 col-1 mx-auto">
                                    <button type="submit" class="btn button" id="button">변경</button>
                                </div>
                            </form><!-- End profile2 Edit Form -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="staticBackdropLabel">비밀번호 확인</h1>
            </div>
            <div class="modal-body">
                <div class="row mb-3">
                    <label for="confirmPw" class="col-md-4 col-lg-3 col-form-label">패스워드 확인</label>
                    <div class="col-md-8 col-lg-9">
                        <form>
                            <input id="confirmPw" name="confirmPw" type="password" class="form-control"/>
                            <span class="text-danger" id="pwErrorCk"></span>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="verifyBtn">확인</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<lottie-player
    id="background-lottie"
    src="${pageContext.request.contextPath}/resources/groupware/images/Animation - 1744694190711.json"
    background="transparent"
    speed="1"
    loop
    autoplay
    style="position: fixed; bottom: 0; left: 50%; transform: translateX(-50%); width: 300px; height: 300px; z-index: -1;">
</lottie-player>    

<!-- myPage.js 에서 사용하기 위한 스크립트 코드 -->
<script>
    var companyNo = "${realUser.companyNo}";
    var empId = "${realUser.empId}";
</script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/employee/myPage.js"></script>




