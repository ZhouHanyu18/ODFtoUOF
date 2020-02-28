package text;

import java.util.Set;
import java.util.TreeSet;
import org.xml.sax.Attributes;

/**
 * 处理<text:date>、<text:time>、<text:word-count>、<text:file-name>、<text:character-count>、
 * <text:editing-duration>、<text:page-number>、<text:page-count>等 到 字:域的转换。
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
	//字:域代码
	private static String _code = "";
	//the collection of text-field's names
	private static Set<String> _field_name_set = new TreeSet<String>();


	public static void create_set(){

		_field_name_set.add("text:date");				//日期

		_field_name_set.add("text:time");				//时间

		_field_name_set.add("text:word-count");			//字数

		_field_name_set.add("text:file-name");			//路径名称

		_field_name_set.add("text:character-count");	//字符数

		_field_name_set.add("text:editing-duration");	//编辑时间

		_field_name_set.add("text:page-number");		//页码

		_field_name_set.add("text:page-count");			//页数

		_field_name_set.add("text:initial-creator");	//作者

		_field_name_set.add("text:editing-cycles");		//修改次数

		_field_name_set.add("text:sequence");			//题注
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
				_code = "Seq " + attVal + " \\* " + conv_num_format(format);
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

		field = "<字:域开始 字:类型=\"" + type + "\" 字:锁定=\"" + fixed + "\"/>";
		field += "<字:域代码>";
		field += "<字:段落><字:句><字:句属性/>";
		field += "<字:文本串>" + get_field_code(type) + "</字:文本串>";
		field += "</字:句></字:段落>";
		field += "</字:域代码>";
		field += "<字:句><字:句属性/>";
		field += "<字:文本串>" + chs + "</字:文本串>";
		field += "</字:句>";
		field += "<字:域结束/>";
		field += "<字:句><字:句属性/></字:句>";

		return field;
	}

	private static String gen_sequence_field(String code,String fixed,String chs){
		String field = "";

		field = "<字:域开始 字:类型=\"Seq\" 字:锁定=\"" + fixed + "\"/>";
		field += "<字:域代码>";
		field += "<字:段落><字:句><字:句属性/>";
		field += "<字:文本串>" + _code + "</字:文本串>";
		field += "</字:句></字:段落>";
		field += "</字:域代码>";
		field += "<字:句><字:句属性/>";
		field += "<字:文本串>" + chs + "</字:文本串>";
		field += "</字:句>";
		field += "<字:域结束/>";
		field += "<字:句><字:句属性/></字:句>";

		return field;
	}

	private static String get_field_code(String type){
		String code = "";

		if(type.equals("Date")){
			code = "DATE \\@ \"yyyy年M月d日\"";
		}
		else if(type.equals("Time")){
			code = "\\@ \"h:mm:ss AM/PM\"";
		}
		else if(type.equals("NumChars")){
			code = "NUMCHARS \\* MERGEFORMAT";
		}
		else if(type.equals("FileName")){
			code = "FileName \\p \\* MERGEFORMAT";
		}
		else if(type.equals("NumWords")){
			code = "NUMWORDS  \\* MERGEFORMAT";
		}
		else if(type.equals("EditTime")){
			code = "EDITTIME  \\* MERGEFORMAT";
		}
		else if(type.equals("Page")){
			code = "PAGE \\* MERGEFORMAT";
		}
		else if(type.equals("NumPages")){
			code = "NUMPAGES  \\* MERGEFORMAT";
		}
		else if(type.equals("Author")){
			code = "AUTHOR \\* MERGEFORMAT";
		}
		else if(type.equals("RevNum")){
			code = "REVNUM  \\* MERGEFORMAT";
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
		else if(odfVal.equals("一, 二, 三, ...")){
			uofVal = "CHINESENUM3";
		}
		else if(odfVal.equals("壹, 贰, 叁, ...")){
			uofVal = "CHINESENUM2";
		}
		else if(odfVal.equals("甲, 乙, 丙, ...")){
			uofVal = "ZODIAC1";
		}
		else if(odfVal.equals("子, 丑, 寅, ...")){
			uofVal = "ZODIAC2";
		}
		else if(odfVal.equals("①, ②, ③, ...")){
			uofVal = "GB3";
		}

		return uofVal;
	}
}
