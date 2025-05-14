<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<security:authentication property="principal.realUser" var="realUser"/> <!-- Provider 시큐리티 정보 -->

<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/community/communityList.css" />

<%-- 커뮤니티 ${communityList } <br> --%>
<%--  커뮤니티 ${communityList } <br> --%>
<%-- 리얼유저 ${realUser } --%>
<%-- 커뮤니티 전체 ${allCommunityList } --%>

<div class="community-wrapper">
    <!-- 왼쪽 영역 -->
    <div class="community-left" id="community-left">
        <a class="btn btn-outline-primary btn-create" id="roomInsert">
            <i class="bi bi-door-open"></i> 방 만들기
        </a>

        <!-- 가입 커뮤니티 -->
        <ul class="list-unstyled">
            <li class="d-flex justify-content-between align-items-center">
                <h6 class="mb-0" style="font-weight: 500;">가입 커뮤니티</h6>
                <i class="bi bi-chevron-down toggle-communities" id="toggle-communities"></i>
            </li>

            <!-- 커뮤니티 목록 -->
            <ul class="community-list" id="community-list" style="display: block; list-style-type: none; padding-left: 0;">
<c:forEach var="community" items="${communityList}">
    <c:set var="isApproved" value="false" />
    <c:set var="isActive" value="false" />
    <c:set var="myMemberNo" value="null" />

    <!-- 승인 여부 체크 -->
    <c:forEach var="status" items="${community.joinStatus}">
        <c:if test="${status.userStatus eq '승인'}">
            <c:set var="isApproved" value="true" />
        </c:if>
    </c:forEach>

    <!-- 로그인한 사용자의 memberNo를 저장 -->
    <c:forEach var="member" items="${community.communityMember}">
        <c:if test="${member.empId eq loginEmpId and member.memberActivityStatus eq '활동중'}">
            <c:set var="isActive" value="true" />
            <c:set var="myMemberNo" value="${member.memberNo}" />
        </c:if>
    </c:forEach>

    <!-- 조건 만족 시 출력 -->
    <c:if test="${community.communityIsClosed eq 'N' and isApproved and isActive}">
        <li class="community-item d-flex justify-content-between align-items-center"
            data-community-no="${community.communityNo}"
            data-member-no="${myMemberNo}"
            style="padding: 8px 12px; border-bottom: 1px solid #ddd;">
            ${community.communityNm}

            <!-- 방 주인 아이콘 -->
            <c:forEach var="member" items="${community.communityMember}">
                <c:if test="${member.memberRole == '방 주인'}">
                    <i class="bi bi-gear roomManagement" style="float: right; cursor: pointer"
                       title="방관리"
                       data-member-no="${member.memberNo}"
                       data-community-no="${community.communityNo}"></i>
                </c:if>
            </c:forEach>
        </li>
    </c:if>
</c:forEach>
            </ul>

            <!-- 가입 멤버 목록 (처음엔 보이지 않음) -->
            <h6 class="mt-4" id="member-title" style="display: none;">가입 멤버</h6>
            <ul class="member-list list-unstyled" id="member-list" style="display: none;"></ul>
        </ul>
    </div>

    <!-- 오른쪽 영역 -->
    <div class="community-right" id="community-content">
        <!-- 모든 오른쪽 요소들이 그려질 div -->
        <div class="content" id="contentIds"></div>

        <!-- 초기화면 카드 시작 -->
        <div id="card">
            <!-- 최근 글과 전체 커뮤니티 메뉴 -->
            <div class="mb-3" id="mainCard">
                <div class="card-body">
                    <ul class="nav nav-tabs nav-tabs-bordered" id="community-tabs">
                        <li class="nav-item">
                            <button class="nav-link active" id="recent-articles-tab" data-bs-target="#recent-articles">
                                가입 커뮤니티
                            </button>
                        </li>
                        <li class="nav-item">
                            <button class="nav-link" id="all-communities-tab" data-bs-target="#all-communities">
                                전체 커뮤니티
                            </button>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="tab-content pt-2" id="tabContent">
                <div id="recent-articles" class="tab-pane fade show active">
                    <ul id="recent-articles-list" class="list-unstyled">
                        <div class="table-responsive">
                            <table id="add-row" class="table table-hover display">
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="select-all" /></th>
                                        <th>커뮤니티명</th>
                                        <th>마스터명</th>
                                        <th>회원수</th>
                                        <th>전체글수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="community" items="${communityList}">
                                        <%-- 승인 여부 체크 --%>
                                        <c:set var="isApproved" value="false" />
                                        <c:forEach var="status" items="${community.joinStatus}">
                                            <c:if test="${status.userStatus eq '승인'}">
                                                <c:set var="isApproved" value="true" />
                                            </c:if>
                                        </c:forEach>

                                        <c:if test="${community.communityIsClosed eq 'N' and isApproved}">
                                            <c:forEach var="communityMember" items="${community.communityMember}">

                                            	                <c:if test="${communityMember.memberActivityStatus ne '탈퇴'}">

                                                <tr class="community-item"
                                                    data-community-no="${community.communityNo}"
                                                    data-member-no="${communityMember.memberNo}">
                                                    <td><input type="checkbox"></td>
                                                    <td>${community.communityNm}</td>
                                                    <td>${community.employee.empNm}</td>
                                                    <td>${community.memberCount}</td>
                                                    <td>4</td>
                                                </tr>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </ul>
                </div>

                <div id="all-communities" class="tab-pane fade">
                    <ul id="all-communities-list" class="list-unstyled">
                        <div class="table-responsive">
                            <table id="add-row" class="table table-hover display">
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="select-all" /></th>
                                        <th>커뮤니티명</th>
                                        <th>마스터명</th>
                                        <th>개설일</th>
                                        <th>회원수</th>
                                        <th>가입</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="community" items="${allCommunityList}">
                                        <c:if test="${community.communityIsClosed eq 'N'}">
                                            <tr>
                                                <td><input type="checkbox"></td>
                                                <td>${community.communityNm}</td>
                                                <td>${community.employee.empNm}</td>
                                                <td>${community.communityCreatedAt}</td>
                                                <td>${community.memberCount}</td>
                                                <td>
                                                    <c:set var="statusValue" value="none" />
                                                    <c:forEach var="myCommunity" items="${communityList}">
                                                        <c:if test="${myCommunity.communityNo eq community.communityNo}">
                                                            <c:forEach var="status" items="${myCommunity.joinStatus}">
                                                                <c:if test="${status.userStatus eq '대기'}">
                                                                    <c:set var="statusValue" value="대기" />
                                                                </c:if>
                                                                <c:if test="${status.userStatus eq '승인'}">
                                                                    <c:set var="statusValue" value="승인" />
                                                                </c:if>
                                                                <c:if test="${status.userStatus eq '거절'}">
                                                                    <c:set var="statusValue" value="거절" />
                                                                </c:if>
                                                            </c:forEach>
                                                        </c:if>
                                                    </c:forEach>

<c:choose>
    <c:when test="${statusValue eq 'none'}">
        <a class="btn btn-sm btn-primary joinRoom" data-community-no="${community.communityNo}">가입하기</a>
    </c:when>

    <c:when test="${statusValue eq '대기'}">
        <span class="badge bg-warning text-dark">승인 대기중</span>
    </c:when>

    <c:when test="${statusValue eq '승인'}">
        <c:set var="isWithdrawn" value="false" />

        <!-- 해당 커뮤니티의 멤버 리스트에서 로그인 사용자가 '탈퇴' 상태인지 확인 -->
        <c:forEach var="myCommunity" items="${communityList}">
            <c:if test="${myCommunity.communityNo eq community.communityNo}">
                <c:forEach var="member" items="${myCommunity.communityMember}">
                    <c:if test="${member.empId eq loginEmpId and member.memberActivityStatus eq '탈퇴'}">
                        <c:set var="isWithdrawn" value="true" />
                    </c:if>
                </c:forEach>
            </c:if>
        </c:forEach>

        <c:choose>
            <c:when test="${isWithdrawn}">
                <!-- 탈퇴 상태라면 다시 가입 가능 -->
                <a class="btn btn-sm btn-primary joinRoom" data-community-no="${community.communityNo}">가입하기</a>
            </c:when>
            <c:otherwise>
                <!-- 정상 승인 상태 -->
<c:set var="myMemberNo" value="null" />

<c:forEach var="myCommunity" items="${communityList}">
    <c:if test="${myCommunity.communityNo eq community.communityNo}">
        <c:forEach var="member" items="${myCommunity.communityMember}">
            <c:if test="${member.empId eq loginEmpId and member.memberActivityStatus eq '활동중'}">
                <c:set var="myMemberNo" value="${member.memberNo}" />
            </c:if>
        </c:forEach>
    </c:if>
</c:forEach>

<a class="btn btn-sm btn-danger leaveRoom leave-btn"
   data-community-no="${community.communityNo}"
   data-member-no="${myMemberNo}">탈퇴하기</a>
            </c:otherwise>
        </c:choose>
    </c:when>

    <c:when test="${statusValue eq '거절'}">
        <a class="btn btn-sm btn-secondary rejoinRoom" data-community-no="${community.communityNo}">재가입 요청</a>
    </c:when>
</c:choose>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<%-- <script src="${pageContext.request.contextPath }/resources/groupware/js/notice/ckEditor.js"></script> --%>
<script src="${pageContext.request.contextPath }/resources/groupware/js/community/communityList.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/sidepopup.js"></script>
<script>
    var companyNo = '${companyNo}';
</script>
<script>
    $(function () {
        $('#add-row').DataTable({
            language: {
                search: "검색:",
                lengthMenu: "_MENU_개씩 보기",
                info: "_TOTAL_건 중 _START_부터 _END_까지 표시 중",
                infoEmpty: "메일 없음",
                zeroRecords: "검색 결과가 없습니다.",
                paginate: {
                    next: "다음",
                    previous: "이전"
                }
            }
        });
    });
</script>

<!-- 커뮤니티 폐쇄 모달 -->
<div class="modal fade" id="closureModal" tabindex="-1" aria-labelledby="closureModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="closureModalLabel">커뮤니티 폐쇄</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="mb-3">
                        <label for="closure-reason" class="col-form-label">폐쇄 사유:</label>
                        <textarea class="form-control" id="closure-reason" placeholder="폐쇄 사유를 입력하세요."></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                <c:forEach var="community" items="${communityList}" varStatus="status">
                    <c:if test="${status.first}">
                        <button type="button" class="btn btn-danger" id="confirmClosureBtn" data-community-no="${community.communityNo}">
                            폐쇄 확정
                        </button>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
