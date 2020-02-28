package text;

import java.util.Set;
import java.util.TreeSet;
import org.xml.sax.Attributes;

/**
 * ����<text:date>��<text:time>��<text:word-count>��<text:file-name>��<text:character-count>��
 * <text:editing-duration>��<text:page-number>��<text:page-count>�� �� ��:���ת����
 *
 * @author xie
 *
 */
public class Text_Field {
	//
	private static String _chs = "";
	//the result
	private static String _result = "";
	//@text:*-value
	//private static String _value = "";
	//@text:fixed
	private static String _fixed = "";
	//��:�����
	private static String _code = "";
	//the collection of text-field's names
	private static Set<String> _field_name_set = new TreeSet<String>();


	public static void create_set(){

		_field_name_set.add("text:date");				//����

		_field_name_set.add("text:time");				//ʱ��

		_field_name_set.add("text:word-count");			//����

		_field_name_set.add("text:file-name");			//·������

		_field_name_set.add("text:character-count");	//�ַ���

		_field_name_set.add("text:editing-duration");	//�༭ʱ��

		_field_name_set.add("text:page-number");		//ҳ��

		_field_name_set.add("text:page-count");			//ҳ��

		_field_name_set.add("text:initial-creator");	//����

		_field_name_set.add("text:editing-cycles");		//�޸Ĵ���

		_field_name_set.add("text:sequence");			//��ע
	}

	public static boolean is_field_name(String qName){
		return _field_name_set.contains(qName);
	}

	private static void clear(){
		_chs = "";
		_result = "";
		_fixed = "";
		_code = "";
	}

	public static String get_result(){
		String str = _result;
		clear();
		return str;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		_fixed = atts.getValue("text:fixed");
		_fixed = (_fixed==null) ? "false" : _fixed;

		if(qName.equals("text:sequence")){
			String format = atts.getValue("style:num-format");

			attVal = atts.getValue("text:name");
			if(attVal != null){
				_code = "Seq " + attVal + " /* " + conv_num_format(format);
			}
		}
	}

	public static void process_chars(String chs){
		_chs += chs;
	}

	public static void process_end(String qName){
		if(qName.equals("text:date")){
			_result += gen_a_field("Date",_fixed,_chs);
		}

		else if(qName.equals("text:time")){
			_result += gen_a_field("Time",_fixed,_chs);
		}

		else if(qName.equals("text:character-count")){
			_result += gen_a_field("NumChars",_fixed,_chs);
		}

		else if(qName.equals("text:word-count")){
			_result += gen_a_field("NumWords",_fixed,_chs);
		}

		else if(qName.equals("text:file-name")){
			_result += gen_a_field("FileName",_fixed,_chs);
		}

		else if(qName.equals("text:editing-duration")){
			_result += gen_a_field("EditTime",_fixed,_chs);
		}

		else if(qName.equals("text:page-number")){
			_result += gen_a_field("Page",_fixed,_chs);
		}

		else if(qName.equals("text:page-count")){
			_result += gen_a_field("NumPages",_fixed,_chs);
		}

		else if(qName.equals("text:initial-creator")){
			_result += gen_a_field("Author",_fixed,_chs);
		}

		else if(qName.equals("text:editing-cycles")){
			_result += gen_a_field("RevNum",_fixed,_chs);
		}

		else if(qName.equals("text:sequence")){
			_result += gen_sequence_field(_code,_fixed,_chs);
		}

		_chs = "";
	}

	private static String gen_a_field(String type,String fixed,String chs){
		String field = "";

		field = "<��:��ʼ ��:����=\"" + type + "\" ��:����=\"" + fixed + "\"/>";
		field += "<��:�����>";
		field += "<��:����><��:��><��:������/>";
		field += "<��:�ı���>" + get_field_code(type) + "</��:�ı���>";
		field += "</��:��></��:����>";
		field += "</��:�����>";
		field += "<��:��><��:������/>";
		field += "<��:�ı���>" + chs + "</��:�ı���>";
		field += "</��:��>";
		field += "<��:�����/>";
		field += "<��:��><��:������/></��:��>";

		return field;
	}

	private static String gen_sequence_field(String code,String fixed,String chs){
		String field = "";

		field = "<��:��ʼ ��:����=\"Seq\" ��:����=\"" + fixed + "\"/>";
		field += "<��:�����>";
		field += "<��:����><��:��><��:������/>";
		field += "<��:�ı���>" + _code + "</��:�ı���>";
		field += "</��:��></��:����>";
		field += "</��:�����>";
		field += "<��:��><��:������/>";
		field += "<��:�ı���>" + chs + "</��:�ı���>";
		field += "</��:��>";
		field += "<��:�����/>";
		field += "<��:��><��:������/></��:��>";

		return field;
	}

	private static String get_field_code(String type){
		String code = "";

		if(type.equals("Date")){
			code = "DATE /@ \"yyyy��M��d��\"";
		}
		else if(type.equals("Time")){
			code = "/@ \"h:mm:ss AM/PM\"";
		}
		else if(type.equals("NumChars")){
			code = "NUMCHARS /* MERGEFORMAT";
		}
		else if(type.equals("FileName")){
			code = "FileName /p /* MERGEFORMAT";
		}
		else if(type.equals("NumWords")){
			code = "NUMWORDS  /* MERGEFORMAT";
		}
		else if(type.equals("EditTime")){
			code = "EDITTIME  /* MERGEFORMAT";
		}
		else if(type.equals("Page")){
			code = "PAGE /* MERGEFORMAT";
		}
		else if(type.equals("NumPages")){
			code = "NUMPAGES  /* MERGEFORMAT";
		}
		else if(type.equals("Author")){
			code = "AUTHOR /* MERGEFORMAT";
		}
		else if(type.equals("RevNum")){
			code = "REVNUM  /* MERGEFORMAT";
		}

		return code;
	}

	private static String conv_num_format(String odfVal){
		String uofVal = "Arabic";

		if(odfVal == null){

		}
		else if(odfVal.equals("1")){
			uofVal = "Arabic";
		}
		else if(odfVal.equals("a")){
			uofVal = "alphabetic";
		}
		else if(odfVal.equals("A")){
			uofVal = "ALPHABETIC";
		}
		else if(odfVal.equals("i")){
			uofVal = "roman";
		}
		else if(odfVal.equals("I")){
			uofVal = "ROMAN";
		}
		else if(odfVal.equals("һ, ��, ��, ...")){
			uofVal = "CHINESENUM3";
		}
		else if(odfVal.equals("Ҽ, ��, ��, ...")){
			uofVal = "CHINESENUM2";
		}
		else if(odfVal.equals("��, ��, ��, ...")){
			uofVal = "ZODIAC1";
		}
		else if(odfVal.equals("��, ��, ��, ...")){
			uofVal = "ZODIAC2";
		}
		else if(odfVal.equals("��, ��, ��, ...")){
			uofVal = "GB3";
		}

		return uofVal;
	}
}
