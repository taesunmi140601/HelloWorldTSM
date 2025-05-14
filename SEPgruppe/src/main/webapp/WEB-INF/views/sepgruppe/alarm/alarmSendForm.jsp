<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 28.     	손현진            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>OneSignal 알림 발송 폼</title>
  <style>
    label {
      display: block;
      margin-top: 15px;
      font-weight: bold;
    }
    input[type="text"],
    input[type="url"],
    textarea {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    button {
      display: block;
      width: 100%;
      padding: 12px;
      margin-top: 20px;
      background-color: #007BFF;
      color: #fff;
      border: none;
      border-radius: 4px;
      font-size: 16px;
      cursor: pointer;
    }
    button:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>
    <form action="/sendNotification" method="post">
      <label for="title">알림 제목</label>
      <input type="text" id="title" name="title" placeholder="알림 제목을 입력하세요" required>
      
      <label for="message">알림 메시지</label>
      <textarea id="message" name="message" placeholder="메시지 내용을 입력하세요" rows="4" required></textarea>
      
      <input type="url" id="url" name="url" value="/alarm" hidden>
      
      <label for="target">대상 태그 또는 사용자 그룹 (선택)</label>
      <input type="text" id="target" name="target" placeholder="예: premium, trial 등">
      
      <button type="submit">알림 발송</button>
    </form>
</body>
</html>