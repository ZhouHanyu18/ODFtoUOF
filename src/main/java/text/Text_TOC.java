package text;

import org.xml.sax.Attributes;

/**
 * 处理 目录 的转换。
 *
 * @author xie
 *
 */
public class Text_TOC {
	private static String _result = "";
	private static boolean _para_tag = false;

	public static String get_result(){
		String rst = _result;
		_result = "";
		return rst;
	}

	public static void process_start(String qName,Attributes atts){

		if(_para_tag){
			Text_P.process_start(qName,atts);
		}
		else if(qName.equals("text:p")){
			_para_tag = true;
			Text_P.process_start(qName,atts);
		}
	}

	public static void process_chars(String chs){
		if(_para_tag){
			Text_P.process_chars(chs);
		}
	}

	public static void process_end(String qName){
		if(qName.equals("text:p")){
			_para_tag = false;
			Text_P.process_end(qName);
			_result += Text_P.get_result();
		}
		else if(_para_tag){
			Text_P.process_end(qName);
		}

		else if(qName.equals("text:index-title")){
			String tocStart = "";

			tocStart += "<字:段落><字:段落属性/>";
			tocStart += "<字:域开始 字:类型=\"ref\" 字:锁定=\"false\"/>";
			tocStart += "<字:域代码><字:段落>";
			tocStart += "<字:句><字:句属性/>";
			tocStart += "<字:文本串>TOC \\o\"1-3\" \\h \\z </字:文本串>";
			tocStart += "</字:句>";
			tocStart += "</字:段落></字:域代码>";
			tocStart += "</字:段落>";

			_result += tocStart;
		}

		else if(qName.equals("text:index-body")){
			String tocEnd = "";

			tocEnd += "<字:段落><字:段落属性/>";
			tocEnd += "<字:域结束/>";
			tocEnd += "</字:段落>";

			_result += tocEnd;
		}
	}
}
