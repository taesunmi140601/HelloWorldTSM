/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 4. 11.     	JSW            최초 생성
 *
 * </pre>
 */
$(function() {
  $.ajax({
    url: `${contextPath}/${companyNo}/dclz/main-panel`,
    method: "GET",
    success: function(html) {
      $("#main-dclz-panel").html(html);
    },
    error: function() {
      console.error("근태 위젯 로딩 실패");
    }
  });
});

