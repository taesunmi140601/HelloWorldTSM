package kr.or.ddit.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *Interceptor를 사용하여 자동으로 처리
 * 
 * 모든 요청이 컨트롤러로 가기 전에 자동으로 companyNo를 가져와서 저장함.
 * 컨트롤러에서 @PathVariable("companyNo") 없이도 companyNo 값을 사용할 수 있음.
 * 
 * @author JYS
 * @since 2025. 3. 12.
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 *  2025. 3. 12.     	JYS	          최초 생성
 *
 * </pre>
 */
public class CompanyInterceptor implements HandlerInterceptor {
	
	// preHandle() : 요청이 컨트롤러로 가기 전에 실행되는 메서드
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		 // URL에서 'companyNo' 값을 가져옴
        String companyNo = request.getParameter("companyNo");
        
        // companyNo가 존재하면, 요청 객체에 companyNo를 저장
        if (companyNo != null) {
            request.setAttribute("companyNo", companyNo);  // 이후 다른 곳에서 사용될 수 있도록 request에 저장
            
            // true를 반환하면, 요청이 계속해서 컨트롤러로 넘어가게 됨
            return true;
        }
        // companyNo가 없으면 로그인 페이지로 리다이렉트
        response.sendRedirect("/login");
        return false;
    }

	// postHandle() : 컨트롤러의 메서드 실행 후, 뷰가 렌더링 되기 전에 실행되는 메서드
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	// ModelAndView 객체가 null이 아닐 경우에만
        if (modelAndView != null) {
            // 컨트롤러에서 처리된 뷰로 돌아가기 전에, 모델에 companyNo를 추가
            modelAndView.addObject("companyNo", request.getAttribute("companyNo"));
        }
    }
}
