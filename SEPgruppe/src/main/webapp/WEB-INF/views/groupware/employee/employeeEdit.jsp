<!--
 * == 개정이력(Modification Information) ==
 *
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 13.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<security:authentication property="principal.realUser" var="realUser"/> <!-- Provider 시큐리티 정보 -->

 <h1>리얼유저 ${realUser}</h1>
 <h1>관리자 인사정보 수정 폼</h1>
<%-- <h1>멤버 ${member }</h1> --%>
