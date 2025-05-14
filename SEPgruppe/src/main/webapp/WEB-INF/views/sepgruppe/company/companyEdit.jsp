<!-- Employee(그룹웨어) Mypage -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<security:authentication property="principal.realUser" var="realUser"/> <!-- Provider 시큐리티 정보 -->
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/sepgruppe/css/company/companyEdit.css" />
<!-- 정보변경 탭 -->
<!-- <section class="section-padding profile2"> -->
<!--     <div class="row"> -->
<!--         <div class="col-xl-4"> -->
<!--             <div class="card"> -->
<!--                 <div class="card-body profile2-card pt-4 d-flex flex-column align-items-center text-center"> -->
<!--                     프로필 이미지 -->
<!--                     <img src="assets/img/kaiadmin/logo_light.svg" alt="navbar brand" class="navbar-brand" height="20" /> -->
<%--                     <h2>${member.contactNm }</h2> --%>
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
<!--         <div class="col-xl-8"> -->
<!--             <div class="card"> -->
<!--                 <div class="card-body pt-3"> -->
<!--                     사용자정보, 정보변경 탭 -->
<!--                     <ul class="nav justify-content-start nav-tabs nav-tabs-bordered" id="mypageBtn"> -->
<!--                         <li class="nav-item"> -->
<!--                             <button class="nav-link active" data-bs-target="#profile2-overview">사용자정보</button> -->
<!--                         </li> -->
<!--                         <li class="nav-item"> -->
<!--                             <button class="nav-link" data-bs-target="#profile2-edit" id="changeModal">정보변경</button> -->
<!--                         </li> -->
<!--                         <li class="nav-item"> -->
<!--                             <button class="nav-link" data-bs-target="#profile2-subscription">구독정보</button> -->
<!--                         </li> -->
<!--                     </ul> -->

<!--                     사용자정보 -->
<!--                     <div class="tab-content pt-2"> -->
<!--                         <div class="tab-pane fade show active profile2-overview" id="profile2-overview"> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">Name</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.contactNm }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">ID</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.contactId }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">CompanyName</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.companyName }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">Phone</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.contactPhone }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">Email</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.contactEmail }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">사업자등록번호</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.businessRegNo }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">그룹웨어 ID</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${member.adminId }</div> --%>
<!--                             </div> -->
<!--                         </div> -->
                        
<!--                         구독 정보 -->
<!--                     <div class="tab-content pt-2"> -->
<!--                         <div class="tab-pane fade show profile2-subscription" id="profile2-subscription"> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">구독 플랜</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${subscription.planType}</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">월간 가격</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${subscription.subscriptionPlan.monthlyPrice }원</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">금월 결제상태</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${subscription.paymentStatus }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">서비스 시작일</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${subscription.subscriptionStart }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">서비스 종료일</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${subscription.subscriptionEnd }</div> --%>
<!--                             </div> -->
<!--                             <div class="row"> -->
<!--                                 <div class="col-lg-3 col-md-4 label">자동결제일</div> -->
<%--                                 <div class="col-lg-9 col-md-8">${subscription.billingDate }</div> --%>
<!--                              </div> -->
<!--                         </div> -->
<!--                     </div> -->

<!--                         정보변경 탭 -->
<!--                         <div class="tab-pane fade profile2-edit pt-3" id="profile2-edit"> -->
<%--                             <form name="frm" action="/sep/company/${member.companyNo}/edit" method="post"> --%>
<!--                                 <div class="row mb-3"> -->
<!--                                     <label for="contactId" class="col-md-4 col-lg-3 col-form-label">ID</label> -->
<!--                                     <div class="col-md-8 col-lg-9"> -->
<%--                                         <div>${member.contactId }</div> --%>
<!--                                     </div> -->
<!--                                 </div> -->

<!--                                 <div class="row mb-3"> -->
<!--                                     <label for="contactNm" class="col-md-4 col-lg-3 col-form-label">Name</label> -->
<!--                                     <div class="col-md-8 col-lg-9"> -->
<%--                                         <div>${member.contactNm}</div> --%>
<!--                                     </div> -->
<!--                                 </div> -->

<!--                                 <div class="row mb-3"> -->
<!--                                     <label for="adminId" class="col-md-4 col-lg-3 col-form-label">그룹웨어 ID</label> -->
<!--                                     <div class="col-md-8 col-lg-9"> -->
<%--                                         <div>${member.adminId}</div> --%>
<!--                                     </div> -->
<!--                                 </div> -->

<!--                                 <div class="row mb-3"> -->
<!--                                     <label for="companyName" class="col-md-4 col-lg-3 col-form-label">회사명</label> -->
<!--                                     <div class="col-md-8 col-lg-9"> -->
<%--                                         <div>${member.companyName}</div> --%>
<!--                                     </div> -->
<!--                                 </div> -->

<!-- 								<div class="row mb-3"> -->
<!-- 								    <label for="contactPhone" class="col-md-4 col-lg-3 col-form-label">휴대폰번호</label> -->
<!-- 								    <div class="col-md-8 col-lg-9"> -->
<%-- 								        <input name="contactPhone" type="text" class="form-control" value="${member.contactPhone }"> --%>
<!-- 								        <span class="text-danger" id="phoneError"></span> 휴대폰 번호 오류 메시지 -->
<!-- 								    </div> -->
<!-- 								</div> -->

<!-- 								<div class="row mb-3"> -->
<!-- 								    <label for="contactEmail" class="col-md-4 col-lg-3 col-form-label">이메일</label> -->
<!-- 								    <div class="col-md-8 col-lg-9"> -->
<%-- 								        <input name="contactEmail" type="text" class="form-control" value="${member.contactEmail }"> --%>
<!-- 								        <span class="text-danger" id="emailError"></span> 이메일 오류 메시지 -->
<!-- 								    </div> -->
<!-- 								</div> -->

<!--                                 <div class="row mb-3"> -->
<!--                                     <label for="contactPw" class="col-md-4 col-lg-3 col-form-label">Password</label> -->
<!--                                     <div class="col-md-8 col-lg-9"> -->
<!--                                         <input id="contactPw" name="contactPw" type="password" class="form-control"/> -->
<!--                                     </div> -->
<!--                                 </div> -->

<!--                                 <div class="row mb-3"> -->
<!--                                     <label for="pwErrorCk" class="col-md-4 col-lg-3 col-form-label">PasswordCheck</label> -->
<!--                                     <div class="col-md-8 col-lg-9"> -->
<!--                                         <input id="contactPwCk" type="password" class="form-control" /> -->
<!--                                         <span class="text-danger" id="pwErrorCk"></span> -->
<!--                                     </div> -->
<!--                                 </div> -->

<!--                                 <div class="d-grid gap-2 col-1 mx-auto"> -->
<!-- 									<button type="submit" class="btn button" id="button">변경</button> -->
<!--                                 </div> -->
<!--                             </form>End profile2 Edit Form -->
<!--                         </div> -->
<!--                     </div> -->
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
<!-- </section> -->

<!-- <!-- Modal --> -->
<!-- <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true"> -->
<!--     <div class="modal-dialog"> -->
<!--         <div class="modal-content"> -->
<!--             <div class="modal-header"> -->
<!--                 <h1 class="modal-title fs-5" id="staticBackdropLabel">비밀번호 확인</h1> -->
<!--             </div> -->
<!--             <div class="modal-body"> -->
<!--                 <div class="row mb-3"> -->
<!--                     <label for="confirmPw" class="col-md-4 col-lg-3 col-form-label">패스워드</label> -->
<!--                     <div class="col-md-8 col-lg-9"> -->
<!--                     <form> -->
<!--                         <input id="confirmPw" name="confirmPw" type="password" class="form-control"/> -->
<!--                         <span class="text-danger" id="pwErrorCk"></span> -->
<!--                     </form> -->
<!--                     </div> -->
<!--                 </div> -->
<!--             </div> -->
<!--             <div class="modal-footer"> -->
<!--                 <button type="button" class="btn btn-primary" id="verifyBtn">확인</button> -->
<!--                 <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
<!-- </div> -->
<div class="mypageContainer">
    <div class="mypageHeader">
        <div class="time"></div>
        <div class="mypageRight">
        <i class="fa-solid fa-wifi"></i>
        <i class="fa-solid fa-battery-full"></i>
        </div>
    </div>
    <div class="profile">
        <img src="${pageContext.request.contextPath }/resources/sepgruppe/images/변요한.jpg" alt="profile" class="profile-image">
        <div class="profile-info">
            <div class="name">${member.contactNm }</div>
            <div class="description">●접속중</div>
        </div>
    </div>
    <ul class="tabs nav nav-tabs nav-tabs-bordered" id="mypageBtn">
        <li class="nav-item">
            <button class="tab nav-link active" data-bs-target="#profile2-overview">사용자정보</button>
        </li>
        <li class="nav-item">
            <button class="tab nav-link" data-bs-target="#profile2-edit" id="changeModal">정보변경</button>
        </li>
        <li class="nav-item">
            <button class="tab nav-link" data-bs-target="#profile2-subscription">구독정보</button>
        </li>
    </ul>
    <div class="mycontent tab-content pt-2">
        <div class="tab-pane fade show active profile2-overview" id="profile2-overview">
            <div class="row">
                <div class="col-lg-3 col-md-4 label">Name</div>
                <div class="col-lg-9 col-md-8">${member.contactNm }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">ID</div>
                <div class="col-lg-9 col-md-8">${member.contactId }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">CompanyName</div>
                <div class="col-lg-9 col-md-8">${member.companyName }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">Phone</div>
                <div class="col-lg-9 col-md-8">${member.contactPhone }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">Email</div>
                <div class="col-lg-9 col-md-8">${member.contactEmail }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">사업자등록번호</div>
                <div class="col-lg-9 col-md-8">${member.businessRegNo }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">그룹웨어 ID</div>
                <div class="col-lg-9 col-md-8">${member.adminId }</div>
            </div>
        </div>

        <div class="tab-pane fade profile2-subscription" id="profile2-subscription">
            <div class="row">
                <div class="col-lg-3 col-md-4 label">구독 플랜</div>
                <div class="col-lg-9 col-md-8">${subscription.planType}</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">월간 가격</div>
                <div class="col-lg-9 col-md-8">${subscription.subscriptionPlan.monthlyPrice }원</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">금월 결제상태</div>
                <div class="col-lg-9 col-md-8">${subscription.paymentStatus }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">서비스 시작일</div>
                <div class="col-lg-9 col-md-8">${subscription.subscriptionStart }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">서비스 종료일</div>
                <div class="col-lg-9 col-md-8">${subscription.subscriptionEnd }</div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-4 label">자동결제일</div>
                <div class="col-lg-9 col-md-8">${subscription.billingDate }</div>
            </div>
        </div>

        <div class="tab-pane fade profile2-edit pt-3" id="profile2-edit">
            <form name="frm" action="/sep/company/${member.companyNo}/edit" method="post">
                <div class="row mb-3">
                    <label for="contactId" class="col-md-4 col-lg-3 col-form-label">ID</label>
                    <div class="col-md-8 col-lg-9">
                        <div>${member.contactId }</div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="contactNm" class="col-md-4 col-lg-3 col-form-label">Name</label>
                    <div class="col-md-8 col-lg-9">
                        <div>${member.contactNm}</div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="adminId" class="col-md-4 col-lg-3 col-form-label">그룹웨어 ID</label>
                    <div class="col-md-8 col-lg-9">
                        <div>${member.adminId}</div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="companyName" class="col-md-4 col-lg-3 col-form-label">회사명</label>
                    <div class="col-md-8 col-lg-9">
                        <div>${member.companyName}</div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="contactPhone" class="col-md-4 col-lg-3 col-form-label">휴대폰번호</label>
                    <div class="col-md-8 col-lg-9">
                        <input name="contactPhone" type="text" class="form-control" value="${member.contactPhone }">
                        <span class="text-danger" id="phoneError"></span>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="contactEmail" class="col-md-4 col-lg-3 col-form-label">이메일</label>
                    <div class="col-md-8 col-lg-9">
                        <input name="contactEmail" type="text" class="form-control" value="${member.contactEmail }">
                        <span class="text-danger" id="emailError"></span>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="contactPw" class="col-md-4 col-lg-3 col-form-label">Password</label>
                    <div class="col-md-8 col-lg-9">
                        <input id="contactPw" name="contactPw" type="password" class="form-control"/>
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="pwErrorCk" class="col-md-4 col-lg-3 col-form-label">PasswordCheck</label>
                    <div class="col-md-8 col-lg-9">
                        <input id="contactPwCk" type="password" class="form-control" />
                        <span class="text-danger" id="pwErrorCk"></span>
                    </div>
                </div>
                <div class="text-center">
				    <button type="submit" class="btn button" id="button">변경</button>
				</div>
            </form>
        </div>
    </div>
<!--       <div class="keyboard"> -->
<!--         <div class="row"> -->
<!--             <div class="key">1</div> -->
<!--             <div class="key">2</div> -->
<!--             <div class="key">3</div> -->
<!--             <div class="key">4</div> -->
<!--             <div class="key">5</div> -->
<!--             <div class="key">6</div> -->
<!--             <div class="key">7</div> -->
<!--             <div class="key">8</div> -->
<!--             <div class="key">9</div> -->
<!--             <div class="key">0</div> -->
<!--         </div> -->
<!--         <div class="row"> -->
<!--             <div class="key">ㅂ</div> -->
<!--             <div class="key">ㅈ</div> -->
<!--             <div class="key">ㄷ</div> -->
<!--             <div class="key">ㄱ</div> -->
<!--             <div class="key">ㄴ</div> -->
<!--             <div class="key">ㅅ</div> -->
<!--             <div class="key">ㅛ</div> -->
<!--             <div class="key">ㅠ</div> -->
<!--             <div class="key">ㅕ</div> -->
<!--             <div class="key">ㅔ</div> -->
<!--         </div> -->
<!--         <div class="row"> -->
<!--             <div class="key">ㅁ</div> -->
<!--             <div class="key">ㄴ</div> -->
<!--             <div class="key">ㅇ</div> -->
<!--             <div class="key">ㄹ</div> -->
<!--             <div class="key">ㅎ</div> -->
<!--             <div class="key">ㅗ</div> -->
<!--             <div class="key">ㅓ</div> -->
<!--             <div class="key">ㅏ</div> -->
<!--             <div class="key">ㅣ</div> -->
<!--         </div> -->
<!--         <div class="row"> -->
<!--             <div class="key icon">↑</div> -->
<!--             <div class="key">ㅋ</div> -->
<!--             <div class="key">ㅌ</div> -->
<!--             <div class="key">ㅊ</div> -->
<!--             <div class="key">ㅍ</div> -->
<!--             <div class="key">ㅠ</div> -->
<!--             <div class="key">ㅜ</div> -->
<!--             <div class="key">ㅡ</div> -->
<!--             <div class="key">ㅣ</div> -->
<!--             <div class="key icon">ⓧ</div> -->
<!--         </div> -->
<!--         <div class="row"> -->
<!--             <div class="key">!#1</div> -->
<!--             <div class="key">한/영</div> -->
<!--             <div class="key large">,</div> -->
<!--             <div class="key large">_</div> -->
<!--             <div class="key large">.</div> -->
<!--             <div class="key icon">↵</div> -->
<!--         </div> -->
<!--     </div> -->
</div>

<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="staticBackdropLabel">비밀번호 확인</h1>
            </div>
            <div class="modal-body">
                <div class="row mb-3">
                    <label for="confirmPw" class="col-md-4 col-lg-3 col-form-label">패스워드</label>
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

<!-- companyEdit css 코드 -->
<link href="${pageContext.request.contextPath}/resources/sepgruppe/css/mypage.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/employee/myEmployee2.css" />
<link href="${pageContext.request.contextPath}/resources/sepgruppe/css/button.css" rel="stylesheet">

<!-- companyEdit js 코드 -->
<script src="${pageContext.request.contextPath}/resources/sepgruppe/js/company/companyEdit.js"></script>
