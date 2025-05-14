<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/sepgruppe/css/indexSep.css" />
<script src="https://cdn.onesignal.com/sdks/web/v16/OneSignalSDK.page.js" defer></script>
<security:authorize access="isAuthenticated()">
<security:authentication property="principal" var="principal" />
  <script>
    window.OneSignalDeferred = window.OneSignalDeferred || [];
    OneSignalDeferred.push(async function(OneSignal) {
      // OneSignal 초기화
      await OneSignal.init({
        appId: "4313ee1a-1f37-4485-8164-0745af496050",  // 자신의 OneSignal 앱 ID로 변경
        serviceWorkerPath: "sep/OneSignalSDKWorker.js",
        serviceWorkerUpdaterPath: "sep/OneSignalSDKUpdaterWorker.js",
        allowLocalhostAsSecureOrigin: true  // 개발 환경(Localhost)에서 테스트할 경우 사용
      });

      // 그룹웨어 사용자의 고유 ID를 External ID로 등록
      var setExternalId = "${principal.realUser.userId}"; // 실제 사용자 ID로 변경
      try {
        await OneSignal.setExternalId(setExternalId);
        console.log("External ID 등록 성공: " + setExternalId);
      } catch (error) {
        console.error("External ID 등록 실패:", error);
      }
    });
  </script>
</security:authorize>
<section class="section-padding" id="section_main">
	<div class="container">
        <div class="row">
            <div class="col-lg-10 col-12 mx-auto">
                <div class="d-flex justify-content-center">
                     <div class="noto-sans-kr-sep">
                         <h2>SEP 과 함께 그룹웨어를 보다</h2>
                         <h2>편리하게 이용해 보세요.</h2>
						<div class="group-button-wrapper"> <!-- 추가된 div -->
					        <security:authorize access="isAuthenticated()">
					            <security:authentication property="principal" var="principal"/>
					            <button type="button" class="btn btn-outline-secondary btn-lg" id="group-buttons"
					                onclick="location.href='${pageContext.request.contextPath }/${principal.realUser.companyNo}/groupware'">
					                <i class="bi bi-exclude"></i> 그룹웨어 이용하기
					            </button>
					        </security:authorize>
					        <security:authorize access="isAnonymous()">
					            <button type="button" class="btn btn-outline-secondary btn-lg" id="group-buttons"
					                onclick="location.href='${pageContext.request.contextPath }/login'">
					                <i class="bi bi-exclude"></i> 그룹웨어 이용하기
					            </button>
					        </security:authorize>
					    </div>
                         <br><br><br>
                     </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="timeline-section section-padding" id="section_3">
    <div class="section-overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-12 text-center">
                <h2 class="text-white mb-4">How Do Apply?</h2>
            </div>
            <div class="col-lg-10 col-12 mx-auto">
                <div class="timeline-container">
                    <ul class="vertical-scrollable-timeline" id="vertical-scrollable-timeline">
                        <div class="list-progress">
                            <div class="inner"></div>
                        </div>
                        <li>
                            <h4 class="text-white mb-3">무료 데모 체험을 한다.</h4>
                            <p class="text-white">서비스를 사용하기 전에 무료 데모 체험을 통해 실제 기능을 경험해보세요.
                               데모 체험은 사용자가 서비스의 다양한 기능을 직접 사용해볼 수 있는 기회를 제공합니다.
                               데모 체험 후에는 서비스에 대한 이해도가 높아져, 구독 신청을 결정하는 데 큰 도움이 될 것입니다.</p>
                            <div class="icon-holder">
                                <i class="bi-search"></i>
                            </div>
                        </li>
                        <li>
                            <h4 class="text-white mb-3">원하는 옵션의 구독신청을 한다.</h4>
                            <p class="text-white">체험 후, 본인의 필요에 맞는 구독 옵션을 선택하여 신청합니다.
                               다양한 요금제와 기능이 준비되어 있어, 개인이나 팀의 요구에 맞춰 최적의 선택을 할 수 있습니다.
                               구독 신청 과정은 간단하며, 필요한 정보를 입력하고 결제를 진행하면 됩니다.</p>
                            <div class="icon-holder">
                                <i class="bi-bookmark"></i>
                            </div>
                        </li>
                        <li>
                            <h4 class="text-white mb-3">부여받은 계정으로 그룹웨어 접속을 한다.</h4>
                            <p class="text-white">구독 신청이 완료되면, 부여받은 계정으로 그룹웨어에 접속할 수 있습니다.
                               로그인 후에는 팀원들과의 협업을 위한 다양한 도구와 기능을 활용할 수 있습니다.
                               그룹웨어를 통해 팀원들과의 소통을 원활하게 하고, 효율적인 업무 환경을 구축해보세요.</p>
                            <div class="icon-holder">
                                <i class="bi-book"></i>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</section>



<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">제품명 가져오기</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        상품설명, 금액 가져오기
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" onclick="location.href='구독신청하는 form 이동'" class="btn btn-primary">결제하기</button>
      </div>
    </div>
  </div>
</div>