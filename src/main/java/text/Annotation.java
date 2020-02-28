package text;

import org.xml.sax.Attributes;
import convertor.IDGenerator;

/**
 * ����<office:annotation> �� <��:��ע>��ת����
 * 
 * @author xie
 *
 */
public class Annotation {
	//
	private static String _chs = "";
	//
	private static String _result = "";
	//text content
	private static String _content = ""; 
	//<dc:creator>
	private static String _creator = "";
	//<dc:date>
	private static String _date = "";
	//tag for text content
	private static boolean _content_tag = false;

	
	private static void clear(){
		_chs = "";
		_content = "";		
		_creator = "";
		_date = "";
	}
	
	private static String get_one_anno(){
		String anno = "";
		String annoID = IDGenerator.get_annotation_id();
		
		anno = "<��:��ע";
		anno += " ��:��������=\"" + annoID + "\"";
		if(!_creator.equals("")){
			anno += " ��:����=\"" + _creator + "\"";
		}
		if(!_date.equals("")){
			anno += " ��:����=\"" + _date + "\"";
		}
		anno += ">";
		
		anno += _content;
		anno += "</��:��ע>";
		
		clear();
		return anno;
	}
	
	public static String get_result(){
		String rst = "";
		
		if(!_result.equals("")){
			rst += "<��:��ע��>";
			rst += _result;
			rst += "</��:��ע��>";
			
			_result = "";
		}
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		if(_content_tag){
			Text_Content.process_start(qName,atts);
		}
		else if(qName.equals("office:annotation")){
			_content_tag = true;
		}	
	}
	
	public static void process_chars(String chs){
		if(_content_tag){
			Text_Content.process_chars(chs);
		}
		
		_chs = chs;
	}
	
	public static void process_end(String qName){
		if(qName.equals("dc:creator")){
			_creator  = _chs;
		}
		else if(qName.equals("dc:date")){
			_date = _chs;
		}
		
		if(_content_tag){
			Text_Content.process_end(qName);
			if(qName.equals("office:annotation")){
				_content_tag = false;
				_content += Text_Content.get_result();
				
				_result += get_one_anno();
			}
		}
	}
}
