package kr.or.ddit.works.widget.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class WidgetPositionVO {
	private String empId;
    private String widgetId;
    private int positionNo;
    private String columnName;
    
}
