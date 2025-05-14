<!-- 
 * 전자결재 환경설정 / 부재 추가
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 24.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<table>
	<tr>
		<td>부재 시작일</td>
		<td><input type="date"></td>
	</tr>
	<tr>
		<td>부재 종료일</td>
		<td><input type="date"></td>
	</tr>
	<tr>
		<td>대결자</td>
		<td>	</td>
	</tr>
	<tr>	
		<td>부재 사유</td>	
		<td><textarea></textarea> </td>
	</tr>
</table>

<button class="btn btn-outline-primary apprbtn">등록</button>
<button class="btn btn-outline-danger apprbtn" onclick="history.back()" >취소</button>