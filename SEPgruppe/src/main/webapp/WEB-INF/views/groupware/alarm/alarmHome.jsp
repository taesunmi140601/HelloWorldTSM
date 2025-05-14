<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      		수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 15.     손현진            최초 생성 + 메뉴 수정
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link href="${pageContext.request.contextPath }/resources/groupware/css/mail/mailList.css" rel="stylesheet">

<div class="mail-container">
    <!-- 왼쪽 사이드 메뉴 -->
    <div class="mailSidebar">
        <h3>🔔 알림함</h3>
        <a href="#" class="menu-item" onclick="filterAlarm('all')">전체 알림</a>
        <a href="#" class="menu-item" onclick="filterAlarm('unread')">읽지 않은 알림</a>
        <a href="#" class="menu-item" onclick="filterAlarm('trash')">휴지통</a>
    </div>

    <!-- 오른쪽 알림 리스트 -->
    <div class="content-area">
        <c:if test="${empty alarmList}">
            <div class="no-mail">📭 받은 알림이 없습니다.</div>
        </c:if>

        <c:if test="${not empty alarmList}">
            <table class="table table-head-bg-primary mt-4" id="customDatatable">
                <thead>
                    <tr>
                        <th scope="col">선택</th>
                        <th scope="col">종류</th>
                        <th scope="col">내용</th>
                        <th scope="col">날짜</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="alarm" items="${alarmList}">
                        <tr class="alarm-row ${alarm.readYn == 'N' ? 'unread' : ''} ${alarm.deletedYn == 'Y' ? 'trashed' : ''}">
                            <td><input type="checkbox" name="deleteAlarm"></td>
                            <td>${alarm.alarmCategory}</td>
                            <td>
                                <span class="mail-link"
                                      onclick="moveToTarget('${alarm.alarmCategory}', '${alarm.alarmCategoryNo}')"
                                      style="cursor:pointer; color:#2980b9; text-decoration:underline;">
                                    ${alarm.alarmNm}
                                </span>
                            </td>
                            <td>
                                <fmt:formatDate value="${alarm.alarmDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>

<script>
    // 필터링 함수
    function filterAlarm(type) {
        const rows = document.querySelectorAll('.alarm-row');
        rows.forEach(row => {
            const isUnread = row.classList.contains('unread');
            const isTrashed = row.classList.contains('trashed');

            if (type === 'all') {
                row.style.display = '';
            } else if (type === 'unread') {
                row.style.display = isUnread ? '' : 'none';
            } else if (type === 'trash') {
                row.style.display = isTrashed ? '' : 'none';
            }
        });
    }

    // 알림 클릭 시 페이지 이동
    function moveToTarget(category, id) {
        let url = "#";
        // 경로 정의는 필요 시 확장
        if (category && id) {
            url = '/' + category + '/detail/' + id;
            window.open(url, '_blank');
        }
    }

    // DataTable 초기화
    $(function () {
        $('#customDatatable').DataTable({
            language: {
                search: "검색:",
                lengthMenu: "_MENU_개씩 보기",
                info: "_TOTAL_건 중 _START_부터 _END_까지 표시 중",
                infoEmpty: "알림 없음",
                zeroRecords: "검색 결과가 없습니다.",
                paginate: {
                    next: "다음",
                    previous: "이전"
                }
            }
        });
    });
</script>