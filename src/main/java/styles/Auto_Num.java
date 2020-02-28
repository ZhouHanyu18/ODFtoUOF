package styles;

import org.xml.sax.Attributes;
import stored_data.Common_Data;
import stored_data.Style_Data;
import tables.Numformat_Table;
import convertor.Unit_Converter;
import graphic_content.Media_Obj;

/** 
 * ����<text:list-style>��<text:outline-style> �� <uof:�Զ����>��ת����
 *  
 * @author xie
 *
 */
public class Auto_Num {
	//the result
	private static String _result = "";
	//
	private static String _list_name = "";
	//
	private static String _levels = "";
	//
	private static String _font = "";
	//
	private static String _ele_atts = "";				//<����>������
	private static String _symbol = "";   				//��Ŀ����
	private static String _symbol_font = "";   			//��������
	private static String _link_ref = "";   			//����ʽ������(�޶�Ӧ)
	private static String _num_format = "";   			//��Ÿ�ʽ														
	private static String _num_format_render = "";   	//��Ÿ�ʽ��ʾ
	private static String _image_ref = "";   			//ͼƬ��������														
	private static String _indent = "";   				//���� 
	private static String _tab_position = "";   		//�Ʊ��λ��														
	private static String _start_num = "";   			//��ʼ���
	private static String _regular_format = "";   		//�����ʽ
	private static int _level = 0;
	
	private static void clear(){
		_font = "";
		_ele_atts = "";
		_symbol = "";   
		_symbol_font = "";   
		_link_ref = "";   
		_num_format = "";  
		_num_format_render = "";  
		_image_ref = ""; 
		_indent = "";  
		_tab_position = "";  
		_start_num = ""; 
		_regular_format = ""; 
		_level = 0;
	}
	
	private static String get_one_level(){
		String oneLevel = "";
		
		if(_symbol_font.equals("")){
			_symbol_font += "<��:��������>";
		}
		_symbol_font += _font + "</��:��������>";
		
		oneLevel ="<��:����" + _ele_atts + ">";
		oneLevel += _symbol + _symbol_font + _link_ref 
			  + _num_format + _num_format_render
			  + _image_ref + _indent + _tab_position 
			  + _start_num + _regular_format;
		oneLevel += "</��:����>";

		//The level range in UOF is 0-8, so
		//level 9 is just dropped.
		if(_level == 9){
			oneLevel = "";
		}
		clear();		
		return oneLevel;
	}
	
	//return <�Զ���ż�>
	public static String get_result(){
		String rst = "";
		
		rst = "<uof:�Զ���ż�>";
		if(Common_Data.get_file_type().equals("presentation")){
			rst += add_present_list_style();
		}
		rst += _result;
		rst += "</uof:�Զ���ż�>";

		_result = "";
		return rst;
	}
	
	//set list name
	public static void set_list_name(String name){
		_list_name = name;
	}
	
	private static String skip_null(String val){
		return (val==null) ? "" : val;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("text:list-style")){
			if((attVal=atts.getValue("style:name")) != null){
				attVal = Style_Data.rename(attVal);
				_list_name = attVal;
			}
		}
		
		else if(qName.equals("text:outline-style")){
			_list_name = "outline";
		}
		
		else if (qName.equals("style:text-properties")) {
			//Here the element's only attribute is
			//style:font-name, and the default font-size
			//is 9pt
			attVal = atts.getValue("style:font-name");
			if(attVal == null){
				attVal = atts.getValue("fo:font-family");
				if(attVal != null){
					Font_Face.add_font(attVal,attVal);
				}
			}
			if(attVal != null){				
				_font = "<��:���� ��:�ֺ�=\"9\" ��:������������=\"" + attVal + "\"/>";
			}
		}
		
		else if(qName.equals("text:list-level-style-image")){
			attVal = atts.getValue("text:level");
			_level = Integer.valueOf(attVal) - 1;			
			_ele_atts += " ��:����ֵ=\"" + _level + "\"";
			
			attVal = atts.getValue("text:style-name");
			if (attVal != null) {
				_symbol_font = "<��:�������� ��:ʽ������=\"" + attVal + "\">";
			}
			
			if ((attVal = atts.getValue("xlink:href")) != null) {
				String objID = "";

				objID = Media_Obj.process_href(attVal);
				_image_ref = "<��:ͼƬ��������>" + objID + "</��:ͼƬ��������>";
			}
		}
		else if((qName.equals("text:list-level-style-number")
				||qName.equals("text:list-level-style-bullet")
				||qName.equals("text:outline-level-style"))){		

			attVal = atts.getValue("text:level");
			_level = Integer.valueOf(attVal) - 1;			
			_ele_atts += " ��:����ֵ=\"" + _level + "\"";
			
			attVal = atts.getValue("text:style-name");
			if (attVal != null) {
				_symbol_font = "<��:�������� ��:ʽ������=" + "\"" + attVal + "\">";
			}
			
			String suffix = skip_null(atts.getValue("style:num-suffix"));
			String prefix = skip_null(atts.getValue("style:num-prefix"));
			String displays = skip_null(atts.getValue("text:display-levels"));
			
			_num_format_render = "<��:��Ÿ�ʽ��ʾ>";
			_num_format_render += prefix + get_render_levels(displays,_level) + suffix;
			_num_format_render += "</��:��Ÿ�ʽ��ʾ>";
			
			attVal=atts.getValue("text:bullet-char");
			if (attVal != null) {
				_symbol = "<��:��Ŀ����>" + attVal + "</��:��Ŀ����>";
			}
			
			attVal = skip_null(atts.getValue("style:num-format"));
			if (!attVal.equals("")) {			
				_num_format = "<��:��Ÿ�ʽ>" + Numformat_Table.get_name(attVal)+ "</��:��Ÿ�ʽ>";
			}
			
			//��:��ʼ���
			attVal = atts.getValue("text:start-value");
			if (attVal != null) {
				_start_num += "<��:��ʼ���>" + attVal + "</��:��ʼ���>"; 
			}
		}
		
		else if (qName.equals("style:list-level-properties")){
			//@��:��Ŷ��뷽ʽ
			attVal=atts.getValue("fo:text-align");
			if (attVal != null) {
				_ele_atts += " ��:��Ŷ��뷽ʽ=\"" + conv_align_val(attVal) + "\"";
			}else {
				_ele_atts += " ��:��Ŷ��뷽ʽ=\"left\"";
			}
			
			//����/�Ʊ��λ��
			attVal = atts.getValue("text:space-before");
			attVal = (attVal==null||attVal.equals("")) ? "0" : Unit_Converter.convert(attVal);
			float space = Float.parseFloat(attVal);
			
			attVal = atts.getValue("text:min-label-width");
			attVal = (attVal==null||attVal.equals("")) ? "0" : Unit_Converter.convert(attVal);
			
			attVal = atts.getValue("text:min-label-distance");
			attVal = (attVal==null||attVal.equals("")) ? "0" : Unit_Converter.convert(attVal);
			float labelDis = Float.parseFloat(attVal);
			
			_indent = "<��:���� uof:locID=\"t0165\">";
			_indent += "<��:��><��:���� uof:locID=\"t0185\" uof:attrList=\"ֵ\""
				+ " ��:ֵ=\"" + space + "\"/></��:��>";
			//The default value is -21.0pt
			_indent += "<��:����><��:���� uof:locID=\"t0189\" uof:attrList=\"ֵ\""
				+ " ��:ֵ=\"-21.0\"/></��:����>";
			_indent += "</��:����>";
			
			_tab_position  = "<��:�Ʊ��λ��>";
			_tab_position += labelDis;
			//_tab_position  += (space + labelWid + labelDis);
			_tab_position  += "</��:�Ʊ��λ��>";
		}
	}
	
	public static void process_end(String qName){
		if(qName.contains("text:list-level-style")
			||qName.equals("text:outline-level-style")){
			
			_levels += get_one_level();
		}
		
		else if(qName.equals("text:list-style")
				||qName.equals("text:outline-style")){
			String style = "";
			
			style = "<��:�Զ����";
			style += " ��:��ʶ��=\"" + _list_name + "\"";
			style += " ��:����=\"" + _list_name + "\">";
			style += _levels;
			style += "</��:�Զ����>";
			
			//list name connot be empty
			if(!_list_name.equals("")){
				_result += style;
			}
			
			_levels = "";
			_list_name = "";
		}
	}
	
	private static String get_render_levels(String displays, int level){
		String rls = "";
		int dis = 1;
		
		if(!displays.equals("")){
			dis = Integer.parseInt(displays);
		}
		
		for(int i = level+1; dis > 0; dis--,i--){
			rls = "%" + i + "." + rls;
		}
		rls = rls.substring(0,rls.length()-1);
		
		return rls;
	}
	
	private static String conv_align_val(String val){
		String convVal = "left";
		
		if (val.equals("left")){
			convVal = "left";
		}
		else if(val.equals("start")){
			convVal = "left";
		}
		else if (val.equals("right")){
			convVal = "right";
		}
		else if(val.equals("end")){
			convVal = "right";
		}
		else if (val.equals("center")){
			convVal = "center";
		}
		else if (val.equals("justify")){
			convVal = "justify";
		}
		
		return convVal;
	}
	
	private static String add_present_list_style(){
		String presenls = "";
		
		presenls = 
			"<��:�Զ���� ��:��ʶ��=\"bn0\" ��:����=\"bn0\" ��:�༶���=\"false\">"
				+ "<��:���� ��:����ֵ=\"0\" ��:��Ŷ��뷽ʽ=\"left\" ��:β���ַ�=\"none\">"
				+	"<��:��Ŀ����>�C</��:��Ŀ����>"
				+	"<��:��������>"
				+ "<��:���� ��:�ֺ�=\"13\" ��:������������=\"StarSymbol\"/>"
				//+		"<��:���� ��:������������=\"font_2\" ��:������������=\"font_2\"/>"
				+	"</��:��������>"
				+	"<��:�Ʊ��λ��>0.0</��:�Ʊ��λ��>"
				+	"<��:��ʼ���>1</��:��ʼ���>"
				+"</��:����>"
			+"</��:�Զ����>"
			+"<��:�Զ���� ��:��ʶ��=\"bn1\" ��:����=\"bn1\" ��:�༶���=\"false\">"
				+"<��:���� ��:����ֵ=\"0\" ��:��Ŷ��뷽ʽ=\"left\" ��:β���ַ�=\"none\">"
				+	"<��:��Ŀ����>��</��:��Ŀ����>"
				+	"<��:��������>"
				+ "<��:���� ��:�ֺ�=\"13\" ��:������������=\"StarSymbol\"/>"
				//+		"<��:���� ��:������������=\"font_1\" ��:������������=\"font_1\"/>"
				+	"</��:��������>"
				+	"<��:�Ʊ��λ��>0.0</��:�Ʊ��λ��>"
				+	"<��:��ʼ���>1</��:��ʼ���>"
				+"</��:����>"
			+"</��:�Զ����>";
		
		return presenls;
	}
}
