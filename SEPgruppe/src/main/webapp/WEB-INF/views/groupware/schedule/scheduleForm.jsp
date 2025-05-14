<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 20.     	KMJ            최초 생성
 *
-->
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@taglib uri ="http://www.springframework.org/tags/form"  prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.realUser" var="realUser"/>

<button type="button" class="btn btn-outline-secondary btn-sm" id="mecro">자동완성</button>

<c:url value="/${companyNo}/schedule" var="Cschedule"></c:url>
<form:form id="scheduleForm" method="post" modelAttribute="schedule">
  <table class="table">
    <tr>
      <th style="display: none;">일정 순번</th>
      <td style="display: none;"><input type="text" id="schdlNo" name="schdlNo" class="form-control" 
      		value="${schedule.schdlNo}" readonly />
          <form:errors path="schdlNo" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>아이디</th>
      <td><input type="text" id="empId" name="empId" class="form-control" 
      		value="${schedule.empId}" readonly />
          <form:errors path="empId" class="text-danger" element="span"/></td>
     </tr>
     <tr>
       <th>일정 유형</th>
       <td>    
       	<select id="scheduleTypeNo" name="scheduleTypeNo" class="form-control">
		<option value=0>--항목선택--</option>
		<option value=1>개인</option>
		<option value=2>부서</option>
		<option value=3>사내</option>
		<option value=4>프로젝트</option>
		</select>
           <form:errors path="scheduleTypeNo" class="text-danger" element="span"/></td>
    </tr>
    <!-- schdlTypeNm 저장 필드 -->
	<input type="hidden" id="schdlTypeNm" name="schdlTypeNm">
    <tr>
      <th>일정명</th>
      <td><input type="text" id="schdlNm" name="schdlNm" class="form-control" 
      		value="${schedule.schdlNm}" />
          <form:errors path="schdlNm" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정설명</th>
      <td><input type="text" id="schdlCn" name="schdlCn" class="form-control" 
      		value="${schedule.schdlCn}" />
          <form:errors path="schdlCn" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정 시작일</th>
      <td><input type="date" name="schdlBgngYmd" id="sschdlBgngYmd" class="form-control" required />
          <form:errors path="schdlBgngYmd" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정 종료일</th>
      <td><input type="date" name="schdlEndYmd" id="sschdlEndYmd" class="form-control" required />
          <form:errors path="schdlEndYmd" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>일정 장소</th>
      <td><input type="text" id="schdlLocation" name="schdlLocation" class="form-control" 
      		value="${schedule.schdlLocation}" />
          <form:errors path="schdlLocation" class="text-danger" element="span"/></td>
    </tr>
    <tr>
      <th>최초 등록 일시</th>
      <td><input type="date" id="schdlCreateDate" name="sschdlCreateDate" class="form-control" 
      		readonly />
          <form:errors path="schdlCreateDate" class="text-danger" element="span"/></td>
    </tr>
    <tr>
<!--       <th>알람 시간</th> -->
<!--       <td><input type="date" id="notifyTime" name="notifyTime" class="form-control"  -->
<%--       		value="${schedule.notifyTime}" /> --%>
<%--           <form:errors path="notifyTime" class="text-danger" element="span"/></td> --%>
<!--      </tr> -->
     <tr>
       <th>참석 여부</th>
       <td>    
       	<select id="schdlStatus" name="schdlStatus" class="form-control">
		<option value="0">--항목선택--</option>
		<option value="참석">참석</option>
		<option value="불참">불참</option>
		<option value="미정">미정</option>
</select>
           <form:errors path="schdlStatus" class="text-danger" element="span"/></td>
    </tr>
  </table>
  <div class="modal-footer">
    <button type="button" id="schdlSubmit" onclick="saveSchedule()" class="btn btn-outline-success">일정 등록</button>
    <button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">닫기</button>
  </div>
</form:form>

<script>
document.getElementById("empId").value = "${realUser.empId}";
document.getElementById("schdlCreateDate").value = new Date().toISOString().slice(0, 10);
document.getElementById("schdlNo").value = "${schdl_seq.NEXTVAL}";

function updateScheduleType() {
    // select 요소에서 선택된 값
    var scheduleTypeNo = document.getElementById("scheduleTypeNo").value;
    var scheduleTypeNm = "";

    // 선택된 값에 따라 schdlTypeNm 설정
    if (scheduleTypeNo == "1") {
    	schdlTypeNm = "개인";
    } else if (scheduleTypeNo == "2") {
    	schdlTypeNm = "부서";
    } else if (scheduleTypeNo == "3") {
    	schdlTypeNm = "사내";
    } else if (scheduleTypeNo == "4") {
    	schdlTypeNm = "프로젝트";
    } else {
    	schdlTypeNm = "";
    }
 }
 
 
function saveSchedule() {
	console.log("=============================================================== mjjjjj");
    let formData = $("#scheduleForm").serialize(); // 폼 데이터 직렬화
	
	console.log("=====================================================mj 폼nmkdnknsdf 데이터:", formData); // 폼 데이터 확인용
	console.log(`${pageContext.request.contextPath}/${companyNo}/schedule/createSchedule.ajax`); // 폼 데이터 확인용
	console.log(`${contextPath}/${companyNo}/schedule/createSchedule.ajax`); // 폼 데이터 확인용
	
    $.ajax({
        url: `${pageContext.request.contextPath}/${companyNo}/schedule/createSchedule.ajax`, // 일정 저장 API URL
        type: "POST",
        data: formData,
        dataType: "json",
        success: function(resp) {
			console.log(resp.cnt);
			
			if (resp.cnt == 1) {
			    alert("일정이 성공적으로 저장되었습니다!");
			    $("#scheduleModal").modal("hide"); // 모달 닫기
				
				//clist.push는 갑자기 등록해도 화면에 안떠서 추가함;
				clist.push({
//                    title: resp.schedule.schdlCn,
                    start: resp.schedule.schdlBgngYmd,
                    end: resp.schedule.schdlEndYmd,
                    schdlNm: resp.schedule.schdlNm,
                    schdlCn: resp.schedule.schdlCn,
                    schdlLocation: resp.schedule.schdlLocation,
                    createDate: resp.schedule.schdlCreateDate,
                    schdlStatus: resp.schedule.schdlStatus,
                    scheduleTypeNo: resp.schedule.scheduleTypeNo,
                    url: `${contextPath}/${companyNo}/schedule/scheduleDetail.do?what=${resp.schedule.schdlNo}`,
                    groupId: clist.length // 새로운 groupId 생성
                });

			    location.reload(); // 캘린더 새로고침
			} else {
			    alert("일정 저장에 실패했습니다. 다시 시도해주세요.");
			}
			
        },
        error: function(xhr, status, error) {
            console.error("AJAX 오류:", status, error);
            alert("일정 저장 중 오류가 발생했습니다.");
        }
    });
	
}
</script>
<script src="${pageContext.request.contextPath }/resources/groupware/js/schedule/scheduleForm.js"></script>