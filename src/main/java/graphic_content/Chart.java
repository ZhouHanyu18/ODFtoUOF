package graphic_content;

import java.util.HashMap;
import java.util.Map;

public class Chart {

	private static float _frameX = 0;   //��ǰframe��X����
	private static float _frameY = 0;
	
	private static String _begin_element = "<��:ͼ��";
	private static String _end_element = "</��:ͼ��>";
	private static String _chart_area = "";   //ͼ����
	private static String _plot_area = "";   //��ͼ��
	private static String _x_axis = "";   //������
	private static String _y_axis = "";   //��ֵ��
	private static String _legend = "";   //ͼ��
	private static String _data_table = "";   //���ݱ�
	private static String _data_series_set = "";   //����ϵ�м�
	private static String _data_point_set = "";   //���ݵ㼯
	private static String _gridline_set = "";   //�����߼�
	private static String _data_sources = "";   //����Դ
	private static String _title_set = "";   //���⼯
	
	private static String _type = "";
	private static boolean _3D = false;
	private static boolean _vertical = false;
	private static boolean _pie_offset = false;
	private static boolean _stacked = false;
	private static boolean _percentage = false;
	private static boolean _symbol = false;
	private static boolean _lines = false;
	
	private static Map<String,String> _data_style_set = new HashMap<String,String>();   //���ָ�ʽ�ļ��ϣ�Ŀǰֻ��ID�����ָ�ʽ�Ķ�
	
	public Chart() {
	}
	
	public static void add_data_serie(String string) 
	{
		_data_series_set += string;
	}
	
	public static void add_data_point(String string) 
	{
		_data_point_set += string;
	}
	
	public static void add_data_source(String string) 
	{
		_data_sources += string;
	}
	
	public static void add_data_style(String ID,String style) 
	{
		_data_style_set.put(ID,style);
	}
	
	public static String get_data_style(String ID)
	{
		return _data_style_set.get(ID);
	}
	
	public static void set_frameX(float svgX) 
	{
		_frameX = svgX;
	}
	
	public static void set_frameY(float svgY) 
	{
		_frameY = svgY;
	}
	
	public static void add_begin_element(String string)
	{
		_begin_element += string;
	}
	
	public static void add_chart_area(String string)
	{
		_chart_area += string;
	}
	
	public static void add_plot_area(String string)
{
		_plot_area += string;
	}
	
	public static void add_legend(String string)
	{
		_legend += string;
	}
	
	public static void add_title(String string) 
	{
		_title_set += string;
	}
	
	public static void add_x_axis(String string) 
	{
		_x_axis += string;
	}
	
	public static void add_y_axis(String string)
	{
		_y_axis += string;
	}
	
	public static void add_gridline(String string) 
	{
		_gridline_set += string;
	}
	
	public static String get_chart_string() 
	{
		get_type(_type);
		_begin_element += (" ��:x����=\"" + _frameX + "\" ��:y����=\"" + _frameY + "\">");
		return _begin_element + _chart_area + _plot_area + _x_axis + _y_axis + _legend
		+ _data_table + "<��:����ϵ�м�>" + _data_series_set + "</��:����ϵ�м�>" 
		+ "<��:���ݵ㼯>" + _data_point_set + "</��:���ݵ㼯>" 
		+ "<��:�����߼�>" + _gridline_set + "</��:�����߼�>" + "<��:����Դ" + _data_sources + "</��:����Դ>"
		+ "<��:���⼯>" + _title_set  + "</��:���⼯>" + _end_element;
	}
	
	public static void clear() 
	{
		_begin_element = "<��:ͼ��";
		_end_element = "</��:ͼ��>";
		_chart_area = "";
		_plot_area = "";
		_x_axis = "";
		_y_axis = "";
		_legend = "";
		_data_table = "";
		_data_series_set = "";
		_data_point_set = "";
		_gridline_set = "";
		_data_sources = "";
		_title_set = "";
		_type = "";
		_3D = false;
		_vertical = false;
		_pie_offset = false;
		_stacked = false;
		_percentage = false;
		_symbol = false;
		_lines = false;
	}
	
	public static void set_type(String type) {
		_type = type;
	}
	
	public static String get_type() {
		return _type;
	}
	
	public static void set_stacked(boolean bool) {
		_stacked = bool;
	}
	
	public static void set_percentage(boolean bool) {
		_percentage = bool;
	}
	
	public static void set_3D(boolean bool) {
		_3D = bool;
	}
	
	public static void set_vertical(boolean bool) {
		_vertical = bool;
	}
	
	public static void set_pieOffset(boolean bool) {
		_pie_offset = bool;
	}
	
	public static void set_symbol(boolean bool) {
		_symbol = bool;
	}
	
	public static void set_lines(boolean bool) {
		_lines = bool;
	}
	
	private static void get_type(String Type) {
		String type = "", subtype = "";
		
		if (Type.equals("chart:bar")) {
			if (!_vertical) {
				type = "column";
				if (_3D) {
					if (_stacked)
						subtype = "column_stacked_3D";
					else if (_percentage)
						subtype = "column_100%_stacked_3D";
					else
						subtype = "column_clustered_3D";
				}
				else {
					if (_stacked)
						subtype = "column_stacked";
					else if (_percentage)
						subtype = "column_100%_stacked";
					else
						subtype = "column_clustered";
				}
			}
			else {
				type = "bar";
				if (_3D) {
					if (_stacked)
						subtype = "bar_stacked_3D";
					else if (_percentage)
						subtype = "bar_100%_stacked_3D";
					else
						subtype = "bar_clustered_3D";
				}
				else {
					if (_stacked)
						subtype = "bar_stacked";
					else if (_percentage)
						subtype = "bar_100%_stacked";
					else
						subtype = "bar_clustered";
				}
			}
		}
		else if (Type.equals("chart:circle")) {
			type = "pie";
			if (_3D)
				subtype = "pie_3D";
			else {
				if (_pie_offset) 
					subtype = "pie_exploded";
				else 
					subtype = "pie_standard";
			}
		}
		else if (Type.equals("chart:ring")) {
			type = "column";
			subtype = "doughnut_standard";
		}
		else if (Type.equals("chart:line")) {
			type = "line";
			if (_symbol) {
				if (_stacked)
					subtype = "line_stcked_marker";
				else if (_percentage)
					subtype = "line_100%_stacked_marker";
				else
					subtype = "line_standard_marker";
			}
			else {
				if (_stacked)
					subtype = "line_stacked";
				else if (_percentage)
					subtype = "line_100%_stacked";
				else
					subtype = "line_standard";
			}
		}
		else if (Type.equals("chart:area")) {
			type = "column";
			
			if (_stacked)
				subtype = "area_stcked";
			else if (_percentage)
				subtype = "area_100%_stacked";
			else
				subtype = "area_standard";
		}
		else if (Type.equals("chart:scatter")) {
			type = "column";
			
			if (_lines) {
				if (_symbol)
					subtype = "scatter_line_marker";
				else
					subtype = "scatter_line";
			}
			else
				subtype = "scatter_marker";
		}
/*		else if (Type.equals("chart:stock")) {
			type = "line";
			subtype = "line_stacked";
			//���еĹɼ�ͼ��ʾ�����⣬��Ϊuof�ļ����ٴ򿪲���������ʾ����������ͼ�����ͺ������Ͷ���ͬ��Ӧ����bug
		}*/
		
		_begin_element += " ��:����=\"" + type + "\" ��:������=\"" + subtype + "\"";
	}
}