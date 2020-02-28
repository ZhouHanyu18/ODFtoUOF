package text;

import org.xml.sax.Attributes;
import text.Sec_Style;

/**
 * 处理<style:master-page> 到 <字:分节>的转换。
 *
 * @author xie
 *
 */
public class Master_Page {
	private static String _left_header_content = "";			//偶数页眉的内容
	private static String _header_content = "";					//标准页眉的内容
	private static String _left_footer_content = "";			//偶数页脚的内容
	private static String _footer_content = "";					//标准页脚的内容
	//tag for text content
	private static boolean _text_content_tag = false;
	//name of first page-layout used by master-page
	private static String _first_PLname = "";
	//
	private static Sec_Style _cur_sec_style = null;				//对应的SecStyle引用


	private static void clear(){
		_footer_content =  "";
		_left_footer_content = "";
		_header_content =  "";
		_left_header_content = "";
	}

	public static String get_result(){
		String rst = "";
		Sec_Style first = Page_Layout.get_sec_style(_first_PLname);

		rst = "<字:分节>";
		if(first != null){
			rst += first.get_result();
		}
		rst += "</字:分节>";

		return rst;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(_text_content_tag){
			Text_Content.process_start(qName,atts);
		}
		else if(qName.equals("style:header")||qName.equals("style:header-left")
				||qName.equals("style:footer")||qName.equals("style:footer-left")){
			_text_content_tag = true;
		}
		else if(qName.equals("style:master-page")){
			attVal = atts.getValue("style:page-layout-name");
			attVal = (attVal==null) ? "" : attVal;

			if(_first_PLname.equals("")){
				_first_PLname = attVal;
			}

			_cur_sec_style = Page_Layout.get_sec_style(attVal);
		}
	}

	public static void process_chars(String chs){
		if(_text_content_tag){
			Text_Content.process_chars(chs);
		}
	}


	public static void process_end(String qName){
		if(qName.equals("style:header")){
			_text_content_tag = false;
			_header_content = Text_Content.get_result();
		}
		else if(qName.equals("style:header-left")){
			_text_content_tag = false;
			_left_header_content = Text_Content.get_result();
		}
		else if(qName.equals("style:footer")){
			_text_content_tag = false;
			_footer_content = Text_Content.get_result();
		}
		else if(qName.equals("style:footer-left")){
			_text_content_tag = false;
			_left_footer_content = Text_Content.get_result();
			if(_left_footer_content.length() != 0){
				String str = "<字:奇偶页页眉页脚不同 字:值=\"true\"/>";
				_cur_sec_style.set_differ_even_odd(str);
			}
		}
		else if(qName.equals("style:master-page")){
			_cur_sec_style.set_footer(get_footer());
			_cur_sec_style.set_header(get_header());
			clear();
		}
		else if(_text_content_tag){
			Text_Content.process_end(qName);
		}
	}

	//取得<字:页眉>的转换结果
	private static String get_header(){
		String header = "";

		if(!_header_content.equals("")||!_left_header_content.equals("")){
			header += "<字:页眉>";

			header += "<字:奇数页页眉>" + _header_content + "</字:奇数页页眉>";

			if(_left_header_content.equals("")){
				header += "<字:偶数页页眉>" + _header_content + "</字:偶数页页眉>";
			}else{
				header += "<字:偶数页页眉>" + _left_header_content + "</字:偶数页页眉>";
			}
			header += "</字:页眉>";
		}

		return header;
	}

	//取得<字:页脚>的转换结果
	private static String get_footer(){
		String footer = "";

		if(!_footer_content.equals("")||!_left_footer_content.equals("")){
			footer += "<字:页脚>";

			footer += "<字:奇数页页脚>" + _footer_content + "</字:奇数页页脚>";

			if(_left_footer_content.equals("")){
				footer += "<字:偶数页页脚>" + _footer_content + "</字:偶数页页脚>";
			}else{
				footer += "<字:偶数页页脚>" + _left_footer_content + "</字:偶数页页脚>";
			}
			footer += "</字:页脚>";
		}

		return footer;
	}
}
