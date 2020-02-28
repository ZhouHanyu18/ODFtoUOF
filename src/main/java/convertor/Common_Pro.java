package convertor;

import org.xml.sax.Attributes;
/**
 * ���� ֽ�ŷ���ֽ�š�ҳ�߾ࡢ�߿� ��ת����
 * 
 * @author xie
 *
 */
public class Common_Pro {
	
	private static String get_pre(String fileType){
		String pre = "��:";
		
		if(fileType.equals("text")){
			pre = "��:";
		}
		else if(fileType.equals("spreadsheet")){
			pre = "��:";
		}
		else if(fileType.equals("presentation")){
			pre = "��:";
		}
		
		return pre;
	}
	
	//����<ֽ�ŷ���>��ת��
	protected static String get_orientation(String fileType, Attributes atts){
		String str = "";
		String attVal = "";
		
		if((attVal=atts.getValue("style:print-orientation")) != null){
			str =  attVal;
		}
		
		if(str.length() != 0){
			str = "<" + get_pre(fileType) + "ֽ�ŷ���>" + str + "</" + get_pre(fileType) + "ֽ�ŷ���>";
		}
		
		return str;
	}
	
	//����<ֽ��>��ת��
	protected static String get_page(String fileType, Attributes atts){
		String str = "";
		String attVal = "";

		if((attVal=atts.getValue("fo:page-width")) != null){
			String width = Unit_Converter.convert(attVal);
			if (fileType.equals("presentation")){ //������ʾ�ĸ��ֽ�ų������Ϊ����
				str += " uof:���=" + "\"" + Math.round(Float.valueOf(width)) + "\"";
			}
			else{
				str += " uof:���=" + "\"" + width + "\"";
			}
		}
		if((attVal=atts.getValue("fo:page-height")) != null){
			String height = Unit_Converter.convert(attVal);
			if (fileType.equals("presentation")){
				str += " uof:�߶�=" + "\"" + Math.round(Float.valueOf(height)) + "\"";
			}
			else{
				str += " uof:�߶�=" + "\"" + height + "\"";
			}
		}
		if((attVal=atts.getValue("style:paper-tray-name")) != null){
			str += " uof:ֽ��=" + "\"" + attVal + "\"";
		}
		
		if(str.length() != 0){
			str = "<" + get_pre(fileType) + "ֽ��" + str + "/>";
		}
		
		return str;
	}
	
	//����<ҳ�߾�>��ת��
	protected static String get_margins(String fileType, Attributes atts){
		String str = "";
		String attVal = "";
		
		if((attVal=atts.getValue("fo:margin-top")) != null){
			str += " uof:��=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("fo:margin-left")) != null){
			str += " uof:��=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("fo:margin-bottom")) != null){
			str += " uof:��=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("fo:margin-right")) != null){
			str += " uof:��=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		
		if(str.length() != 0){
			str = "<" + get_pre(fileType) + "ҳ�߾�" + str + "/>";
		}

		return str;
	}
	
	//����߿򣺴�"0.002cm solid #000000"��ʽ���ַ�����ȡ������ֵ 
	public static String tranBorderValue(String border)
	{
		int index1 = border.indexOf(' ');
		int index2 = border.lastIndexOf(' ');
		String str = "";
		
		if(border.equals("none")){
			str = " uof:����=\"none\"";
		}
		else {
			String width = border.substring(0,index1);
			String type = border.substring(index1+1,index2);
			String color = border.substring(index2+1);
			
			if(type.equals("solid")){
				str = " uof:���=\"" + Unit_Converter.convert(width) + 
					"\" uof:����=\"single\" uof:��ɫ=\"" + color + "\"";
			}
			else if(type.equals("double")){
				float widVal = Float.valueOf(Unit_Converter.convert(width));
				
				widVal = (widVal > 4) ? widVal/2 : widVal;
				str = " uof:���=\"" + widVal + "\" uof:����=\"double\" uof:��ɫ=\"" + color + "\"";
			}
		}
		
		return str;	
	}
	
	//����<�߿�>��ת��
	protected static String get_borders(String fileType, Attributes atts){
		String border = "";
		String value = "";
		
		value = atts.getValue("fo:border");
		if(value != null && !value.equals("none")) { 
			border += "<uof:��" + tranBorderValue(value) + "/>";
			border += "<uof:��" + tranBorderValue(value) + "/>";
			border += "<uof:��" + tranBorderValue(value) + "/>";
			border += "<uof:��" + tranBorderValue(value) + "/>";
		}
		else{
			if((value = atts.getValue("fo:border-left")) != null){
				border += "<uof:��" + tranBorderValue(value) + "/>";
			}
			if((value = atts.getValue("fo:border-top")) != null){
				border += "<uof:��" + tranBorderValue(value) + "/>";
			}
			if((value = atts.getValue("fo:border-right")) != null){
				border += "<uof:��" + tranBorderValue(value) + "/>";
			}
			if((value = atts.getValue("fo:border-bottom")) != null){
				border += "<uof:��" + tranBorderValue(value) + "/>";
			}
		}
		if((value = atts.getValue("style:diagonal-tl-br")) != null){   //��Ԫ���жԽ���
			border += "<uof:�Խ���1" + tranBorderValue(value) + "/>";
		}
		if((value = atts.getValue("style:diagonal-bl-tr")) != null){
			border += "<uof:�Խ���2" + tranBorderValue(value) + "/>";
		}
		
		if (border.length() != 0){
			if(fileType.equals("text")){
				border = "<��:�߿� uof:locID=\"t0065\">" + border + "</��:�߿�>";
			}
			else if(fileType.equals("spreadsheet")){
				border = "<��:�߿� uof:locID=\"s0022\">" + border + "</��:�߿�>";
			}
			else {
				border = "<��:�߿�>" + border + "</��:�߿�>";
			}
		}
		
		return border;
	}
}
