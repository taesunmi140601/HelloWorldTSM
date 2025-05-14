<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 3. 17.     	KMJ            최초 생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
	<link href="${pageContext.request.contextPath }/resources/groupware/css/approval/aprvlLine.css" rel="stylesheet">
</head>
	<h2>전자결재</h2>
	<h2>전자결재</h2>
	<h2>전자결재</h2>
	<h2>전자결재</h2>
	<h2>전자결재</h2>
	<h2>전자결재</h2>	

<div class="card-body">
	<ul class="nav nav-tabs nav-line nav-color-secondary" id="line-tab" role="tablist">
		<li class="nav-item">
			<a class="nav-link active" id="line-home-tab" data-bs-toggle="pill" href="#line-home" role="tab" aria-controls="pills-home" aria-selected="true">새 결재</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" id="line-bookmark-tab" data-bs-toggle="pill" href="#line-bookmark" role="tab" aria-controls="pills-bookmark" aria-selected="false">자주 쓰는 양식</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" id="line-approve-tab" data-bs-toggle="pill" href="#line-approve" role="tab" aria-controls="pills-approve" aria-selected="false">결재하기</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" id="line-reference-tab" data-bs-toggle="pill" href="#line-reference" role="tab" aria-controls="pills-reference" aria-selected="false">참조/열람 대기 문서</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" id="line-tbd-tab" data-bs-toggle="pill" href="#line-tbd" role="tab" aria-controls="pills-tbd" aria-selected="false">결제 예정 문서</a>
		</li>
	</ul>
	<div class="tab-content mt-3 mb-3" id="line-tabContent">
		<div class="tab-pane fade show active" id="line-home" role="tabpanel" aria-labelledby="line-home-tab">
			<p>전자결재 홈</p>

		</div>
		<div class="tab-pane fade" id="line-bookmark" role="tabpanel" aria-labelledby="line-bookmark-tab">
			<p>자주 쓰는 양식</p>
		</div>
		<div class="tab-pane fade" id="line-approve" role="tabpanel" aria-labelledby="line-approve-tab">
			<p>결재하기</p>
		</div>
		<div class="tab-pane fade" id="line-reference" role="tabpanel" aria-labelledby="line-reference-tab">
			<p>참조열람</p>
		</div>
		<div class="tab-pane fade" id="line-tbd" role="tabpanel" aria-labelledby="line-tbd-tab">
			<p>참조열람</p>
		</div>
	</div>
</div>
<body>
<div class="biggestContainer">
    <div class="aprvl-main">
        <!-- 왼쪽 근태관리 -->
        <div class="aprvl-left">
            <div class="aprvl-container">
                <div class="aprvl">
                    <h3>전자결재</h3>
                    <button class="btn btn-primary btn-border">결재하기</button>
                    <h5>계속 수정 예정....</h5>
                </div>
            </div>
        </div>

        <!-- 오른쪽 진행중 문서 + 결재할 문서 + 완료 문서 -->
        <div class="aprvl-right">
            <!-- 진행중 문서 -->
            <div class="col-md-4">
		     <div class="card card-post card-round">
			    <div class="card-body">
		       <div class="d-flex">
		         <div class="avatar avatar-lg">
					<img src="${pageContext.request.contextPath }/resources/groupware/kaiadmin/assets/img/jm_denis.jpg" alt="..." class="avatar-img rounded-circle">
				 </div>
		         <div class="info-post ms-2">
		           <p class="username">이철희</p>
		           <p class="date text-muted">2025년 3월 17일</p>
		         </div>
		       </div>
		       <div class="separator-solid"></div>
		       <p class="card-category text-info mb-1">
		         <a href="#"><span class="badge badge-success">진행중</span></a>
		       </p>
		       <h3 class="card-title">
		         <a href="#"> 업무협조 </a>
		       </h3>
		       <p class="card-text">
		         <strong>기안자 :</strong> 강명지<br>
            	<strong>기안일 :</strong> 2025-03-17<br>
            	<strong>결재양식 :</strong> 업무협조(모바일)
		       </p>
		       <a href="#" class="btn btn-primary btn-rounded btn-sm"
		         >결재하기</a
		       >
		     </div>
		   </div>
		 </div>	
            <!-- 결재할 문서 -->
            <div class="content aprvlIng-container">
                <div class="card">
                    <div class="card-header">
                        <div class="card-title">결재할 문서</div>
                    </div>
                    <div class="aprvlIng">
                        <div class="card-body">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th scope="col">기안일</th>
                                        <th scope="col">결재양식</th>
                                        <th scope="col">긴급</th>
                                        <th scope="col">제목</th>
                                        <th scope="col">첨부</th>
                                        <th scope="col">결재상태</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>2025-03-17</td>
                                        <td>휴가신청</td>
                                        <td><span class="badge badge-danger">긴급</span></td>
                                        <td>휴가신청서</td>
                                        <td>-</td>
                                        <td><span class="badge badge-success">진행중</span></td>
                                    </tr>
                                    <tr>
                                        <td>2025-03-17</td>
                                        <td>업무협조</td>
                                        <td></td>
                                        <td>---</td>
                                        <td>---</td>
                                        <td></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 완료 문서 -->
            <div class="content aprvlDone-container">
                <div class="card">
                    <div class="card-header">
                        <div class="card-title">완료 문서</div>
                    </div>
                    <div class="aprvlDone">
                        <div class="card-body">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th scope="col">기안일</th>
                                        <th scope="col">결재양식</th>
                                        <th scope="col">긴급</th>
                                        <th scope="col">제목</th>
                                        <th scope="col">첨부</th>
                                        <th scope="col">결재상태</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>2025-03-17</td>
                                        <td>휴가신청</td>
                                        <td><span class="badge badge-danger">긴급</span></td>
                                        <td>휴가신청서</td>
                                        <td>-</td>
                                        <td><span class="badge badge-count">완료</span></td>
                                    </tr>
                                    <tr>
                                        <td>2025-03-17</td>
                                        <td>업무협조</td>
                                        <td></td>
                                        <td>-----</td>
                                        <td>-----</td>
                                        <td><span class="badge badge-count">완료</span></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>    
        </div>
    </div>
</div>
</body>