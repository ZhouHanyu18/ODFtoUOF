package tables;

import java.util.HashMap;
import java.util.Map;

public class Numformat_Table {

	private static Map<String,String>
		_hashmap = new HashMap<String,String>();

	public static void create_map() {
		_hashmap.put("1","decimal.1 2 3 4 5 6 7 8 9 ");
		_hashmap.put("i","lower-roman.i ii iii iv v vi vii viii ix ");
		_hashmap.put("I","upper-roman.I II III IV V VI VII VIII IX ");
		_hashmap.put("a","lower-letter.a b c d e f g h i ");
		_hashmap.put("A","upper-letter.A B C D E F G H I ");
		_hashmap.put("子, 丑, 寅, ...","ideograph-zodiac.子 丑 寅 卯 辰 巳 午 未 申 ");
		_hashmap.put("一, 二, 三, ...","chinese-counting.一 二 三 四 五 六 七 八 九 ");
		_hashmap.put("壹, 贰, 叁, ...","chinese-legal-simplified.壹 贰 叁 肆 伍 陆 柒 捌 玖 ");
		_hashmap.put("甲, 乙, 丙, ...","ideograph-traditional.甲 乙 丙 丁 戊 己 庚 辛 壬 ");
		_hashmap.put("①, ②, ③, ...","decimal-enclosed-circle-chinese.");
		_hashmap.put("１, ２, ３, ...","decimal-full-width.");
		_hashmap.put("Native Numbering","chinese-counting.");
		_hashmap.put("","");
		//to be continued.
	}

	public static String get_name(String ODFname) {
		String name = _hashmap.get(ODFname);
		if(name == null){
			name = "decimal.";
		}

		int index = name.indexOf(".");

		return name.substring(0,index);
	}

	//
	public static String get_level_rep(String ODFname,int level){
		String string = _hashmap.get(ODFname);
		int index = string.indexOf(".");
		String reps = string.substring(index + 1);
		String rep = "";

		for (int i = 1; i < level; i++) {
			index = reps.indexOf(" ");
			reps = reps.substring(index + 1);
		}
		index = reps.indexOf(" ");
		rep = reps.substring(0,index);

		return rep;
	}
}
