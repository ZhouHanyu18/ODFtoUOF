package stored_data;

import java.util.HashMap;
import java.util.Map;

import graphic_content.ChartStyle;

public class Chart_Data {
	private static String _ole_type = "";   //���ڴ洢OLE��������ࣨĿǰֻ���chart��
	private static Map<String,ChartStyle> _chart_style_set = new HashMap<String,ChartStyle>();   //�洢chart�е�ʽ��
	
	
	//initialize
	public static void init(){
		_ole_type = "";
		_chart_style_set.clear();
	}
	
//	*******************����OLEType****************************
//	*		
	public static void set_ole_type(String type) {
		_ole_type = type;
	}
	
	public static String get_ole_type() {	
		return _ole_type;
	}	
//	*	
//	***************************************
	
//	*******************����chartStyleSet****************************
//	*		
	public static void add_chart_style(String ID,ChartStyle chartstyle) {
		_chart_style_set.put(ID,chartstyle);
	}
	
	public static ChartStyle get_chart_style(String ID) {	
		return _chart_style_set.get(ID);
	}	
//	*	
//	***************************************
}
