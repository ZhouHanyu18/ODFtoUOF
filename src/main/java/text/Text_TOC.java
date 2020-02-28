package text;

import org.xml.sax.Attributes;

/**
 * ���� Ŀ¼ ��ת����
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

			tocStart += "<��:����><��:��������/>";
			tocStart += "<��:��ʼ ��:����=\"ref\" ��:����=\"false\"/>";
			tocStart += "<��:�����><��:����>";
			tocStart += "<��:��><��:������/>";
			tocStart += "<��:�ı���>TOC /o\"1-3\" /h /z </��:�ı���>";
			tocStart += "</��:��>";
			tocStart += "</��:����></��:�����>";
			tocStart += "</��:����>";

			_result += tocStart;
		}

		else if(qName.equals("text:index-body")){
			String tocEnd = "";

			tocEnd += "<��:����><��:��������/>";
			tocEnd += "<��:�����/>";
			tocEnd += "</��:����>";

			_result += tocEnd;
		}
	}
}
