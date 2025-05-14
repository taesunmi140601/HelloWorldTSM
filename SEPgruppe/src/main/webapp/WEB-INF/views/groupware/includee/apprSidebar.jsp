<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 4. 15.     	JYS            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/approval/apprHome.css" >
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/groupware/css/approval/apprSidebar.css" >

<!-- Sidebar Menu -->
<div class="col-md-3" id="apprSidebar">
    <div class="apprSidebar-wrapper">
        <div class="apprSidebar-content">
            <ul class="nav nav-secondary">
                 <li class="nav-section">
                   <h4 class="text-section">📑&nbsp;전자결재</h4>
                </li>

                <!-- 새 문서 작성 -->
                <li class="nav-item apprBtn-li">
                    <div>
                        <button id="newApprDocBtn" type="button" class="btn btn-primary " 
                            onclick="location.href='<c:url value="/${companyNo}/approval/new"/>'"
                            data-company-no="${companyNo}" id="loadAllDocFormButton">
                            새 문서 작성
                        </button>
                    </div>
                </li>

               <!-- 결재하기 -->
                <li class="nav-item appr-item">
                    <a data-bs-toggle="collapse" href="#apprDoc" class="d-flex"> 
                        <span class="apprMenu-text"><i class="fas fa-file-signature"></i>결재하기</span><span class="caret"></span>
                    </a>
                    <div class="collapse show" id="apprDoc">
                        <ul class="nav nav-collapse">
                            <li><a href="<c:url value='/${companyNo}/box/awaitDocs'/>">
                                <p class="sub-item">결재 대기 문서</p>
                            </a></li>
                            <li><a href="<c:url value='/${companyNo}/box/pendingDocs'/>">
                                <p class="sub-item">결재 예정 문서</p>
                            </a></li>
                        </ul>
                    </div>
                </li>

                <!-- 개인 문서함 -->
                <li class="nav-item appr-item">
                  	<a data-bs-toggle="collapse" href="#my"> 
                        <span class="apprMenu-text"><i class="fas fa-folder"></i>개인 문서함</span><span class="caret"></span>
                    </a>
                    <div class="collapse show" id="my">
                        <ul class="nav nav-collapse">
                            <li><a href="<c:url value='/${companyNo}/box/draftDocs'/>">
                                <p class="sub-item">기안 문서함</p>
                            </a></li>
                            <li><a href="<c:url value='/${companyNo}/box/tempDocs'/>">
                                <p class="sub-item">임시 저장함</p>
                            </a></li>
                            <li><a href="<c:url value='/${companyNo}/box/apprDocs'/>">
                                <p class="sub-item">결재 문서함</p>
                            </a></li>
                            <li><a href="<c:url value='/${companyNo}/box/refDocs'/>">
                                <p class="sub-item">참조/열람 문서함</p>
                            </a></li>
                        </ul>
                    </div>
                </li>
                
                <!-- 부서 문서함 -->
                <li class="nav-item appr-item">
                    <a href="<c:url value='/${companyNo}/box/depDocs'/>"> 
                        <i class="fas fa-building"></i>
                        <span class="apprMenu-text">부서 문서함</span>
                    </a>
                </li>

                <!-- 전자결재 환경설정 -->
                <li class="nav-item appr-item">
                    <a href="<c:url value='/${companyNo}/approval/admin/setting'/>">
                        <i class="fas fa-cog"></i>
                        <span class="apprMenu-text">전자결재 환경설정</span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
