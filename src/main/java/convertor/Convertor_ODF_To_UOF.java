package convertor;

import graphic_content.Graphic_Handler;
import graphic_content.Media_Obj;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

import tables.*;
import text.*;
import spreadsheet.*;
import stored_data.*;
import styles.*;
import presentation.*;

/**
 * 锟斤拷锟斤拷锟津，革拷锟斤拷锟绞硷拷锟斤拷锟教拷锟斤拷锟斤拷锟斤拷锟絊AX锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟捷达拷锟斤拷锟斤拷锟斤拷锟斤拷牡锟斤拷锟斤拷薪锟斤拷锟斤拷锟�
 *
 * @author xie
 *
 */
public class Convertor_ODF_To_UOF extends JFrame implements ActionListener{
	private static final long serialVersionUID = 200611111102L;

	private static JTextField _src_path_field;
	private static JTextField _rst_path_field;
	private static JTextArea _source_area;
	private static JTextArea _result_area;
	private static JButton convertButton;
	private static JTextField _state_field;

	private JPanel create_src_panel(JComponent comp1, JComponent comp2){
		JPanel srcPl = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		srcPl.add(comp1, c);
		c.gridwidth = 2;
		srcPl.add(comp2, c);

		return srcPl;
	}

	private void add_content(JComponent comp,Insets is, int gridx, int gridy, int gridwidth){
		GridBagConstraints c = new GridBagConstraints();

		c.insets = is;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;

		getContentPane().add(comp,c);
	}

	public Convertor_ODF_To_UOF(String title){
		super(title);
		getContentPane().setLayout(new GridBagLayout());

	    JLabel pathLb = new JLabel("锟斤拷锟斤拷锟斤拷ODF源锟侥硷拷锟斤拷: ");
	    pathLb.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
	    pathLb.setFont(new Font(" ", Font.BOLD, 14));
	    _src_path_field = new JTextField(System.getProperty("user.dir"));
	    _src_path_field.setFont(new Font(" ", Font.PLAIN, 13));
	    _src_path_field.setColumns(37);
	    add_content(create_src_panel(pathLb, _src_path_field),new Insets(25,5,5,5), 0, 0, 2);

	    convertButton = new JButton("转锟斤拷锟斤拷");
	    convertButton.setBorder(BorderFactory.createEmptyBorder(3,4,3,4));
	    convertButton.setActionCommand("CONVERT");
	    convertButton.addActionListener(this);
	    JPanel butPn = new JPanel();
	    butPn.add(convertButton);
	    butPn.setBorder(BorderFactory.createEmptyBorder(0,0,0,37));
	    _rst_path_field = new JTextField(System.getProperty("user.dir") + "/");
	    _rst_path_field.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
	    _rst_path_field.setBackground(Color.LIGHT_GRAY);
	    _rst_path_field.setColumns(32);
	    add_content(create_src_panel(butPn, _rst_path_field),new Insets(5,5,5,5), 0, 1, 2);

	    add_content(new JLabel("锟斤拷锟斤拷源锟侥硷拷: "),new Insets(5,5,0,35), 0, 2, 1);

	    add_content(new JLabel("锟斤拷锟斤拷锟斤拷:   "),new Insets(5,0,0,55), 1, 2, 1);

	    _source_area = new JTextArea("",20,25);
	    _source_area.setEditable(false);
	    _source_area.setLineWrap(true);
	    _source_area.setForeground(Color.gray);
	    add_content(new JScrollPane(_source_area),new Insets(0,25,5,0), 0, 3, 1);

	    _result_area = new JTextArea("",20,25);
	    _result_area.setEditable(false);
	    _result_area.setLineWrap(true);
	    _result_area.setForeground(Color.blue);
	    add_content(new JScrollPane(_result_area),new Insets(0,0,5,25), 1, 3, 1);

	    _state_field = new JTextField("");
	    _state_field.setColumns(30);
	    _state_field.setBorder(BorderFactory.createEmptyBorder());
	    _state_field.setForeground(Color.RED);
	    _state_field.setBackground(Color.LIGHT_GRAY);
	    _state_field.setFont(new Font("",Font.BOLD,14));
	    add_content(_state_field, new Insets(5,5,10,5),0,4,2);
	}

	//do some initiations
	private static void global_init(){
		UOF_LocID_Table.create_map();
		Meta_Table.create_map();
		Extension_Ele_Set.createSet();
		Text_Field.create_set();
		Draw_Type_Table.create_map();
		Numformat_Table.create_map();
		Draw_Page_Style.init_effect_table();
		Anim_Par.init_effect_table();
	}

	private void loc_init(){
		Page_Layout.init();
		Text_P.init();
		Text_Table.init();
		Tracked_Change.init();
		Graphic_Style.init();
		Table_Column.init();
		Table_Row.init();
		Table_Style.init();

		Chart_Data.init();
		Content_Data.init();
		Spreadsheet_Data.init();
		Style_Data.init();

		Anno_In_Cell.init();
		Cell_Range.init();
		Table_Cell.init();
		Validation.init();

		Anim_Par.init();
		Draw_Padding.init();
		Draw_Page_Style.init();
		Draw_Page.init();

		Styles.init();
		Media_Obj.init();
		IDGenerator.restart();
		Graphic_Handler.init();
	}

	public void actionPerformed(ActionEvent event){
		if (event.getActionCommand().equals("CONVERT")){
			String srcFile = _src_path_field.getText().trim();
			String rstFile = _rst_path_field.getText().trim();

			if(!srcFile.endsWith(".odt") && !srcFile.endsWith(".ods") && !srcFile.endsWith(".odp")){
				_source_area.setText("锟斤拷锟斤拷! 源锟侥硷拷锟斤拷锟斤拷锟斤拷odf锟斤拷锟酵ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫！");
			}
			else {
				try{
					int inds = srcFile.lastIndexOf("/");
					int inde = srcFile.lastIndexOf(".");
					String srcName = srcFile.substring(inds+1, inde);

					if(!rstFile.contains(".")){
						new File(rstFile).mkdirs();
						if(!rstFile.endsWith("/")){
							rstFile += "/";
						}
						rstFile += srcName + "_result.uof";;
					}
					else if(!rstFile.endsWith(".uof")){
						rstFile = System.getProperty("user.dir") + "/" + srcName + "_result.uof";
					}

					_rst_path_field.setText(rstFile);
					_source_area.setText("");
					_result_area.setText("");

					do_convert(srcFile, rstFile);
					loc_init();
					//convertButton.setEnabled(false);
				}catch (Exception e){
					System.err.println(e.getMessage());
				}
			}
		}
	}

	public static void write_source_ta(String str){
		_source_area.append(str);
	}

	public static void write_result_ta(String element){
		_result_area.append(element);
	}

	private void do_convert(String srcFileName, String rstFileName){
		String state = "";

		try {
			String tmpPath = Unzip.get_temp_path();
			String tmpFileName = Results_Processor.initialize(rstFileName);

			//unzip the source file
			Unzip.unzip(srcFileName);
			XMLReader xmlReader = null;

			//锟斤拷mimetype锟侥硷拷锟斤拷取锟侥碉拷锟斤拷锟斤拷.
			BufferedReader typeReader = new BufferedReader(new FileReader(tmpPath + "mimetype"));
			String fileType = "";

			while ((fileType = typeReader.readLine()) != null) {
				if (fileType.contains("text")){
					Common_Data.set_file_type("text");
				}
				else if (fileType.contains("spreadsheet")){
					Common_Data.set_file_type("spreadsheet");
				}
				else if (fileType.contains("presentation")){
					Common_Data.set_file_type("presentation");
				}
			}

			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setValidating(false);    //锟斤拷锟斤拷证锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟节革拷式锟斤拷锟矫碉拷锟侥碉拷.
			SAXParser saxParser = spfactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();

			InputSource metaSource = new InputSource(tmpPath + "meta.xml");
			InputSource styleSource = new InputSource(tmpPath + "styles.xml");
			InputSource contentSource = new InputSource(tmpPath + "content.xml");

			//锟斤拷一锟斤拷扫锟斤拷源锟侥碉拷锟斤拷锟斤拷取锟斤拷锟斤拷要锟芥储锟斤拷锟斤拷锟斤拷.
			//first parse of meta.xml
			DefaultHandler firstMetaHandler = new First_Meta_Handler();
			xmlReader.setContentHandler(firstMetaHandler);
			xmlReader.setErrorHandler(firstMetaHandler);
			xmlReader.parse(metaSource);

			//first parse of style.xml
			DefaultHandler firstStyleHandler = new First_Style_Handler();
			xmlReader.setContentHandler(firstStyleHandler);
			xmlReader.setErrorHandler(firstStyleHandler);
			xmlReader.parse(styleSource);

			//first parse of content.xml
			DefaultHandler firstContentHandler = new First_Content_Handler();
			xmlReader.setContentHandler(firstContentHandler);
			xmlReader.setErrorHandler(firstContentHandler);
			xmlReader.parse(contentSource);

			//锟节讹拷锟斤拷扫锟斤拷源锟侥碉拷锟斤拷锟斤拷锟斤拷展锟斤拷之锟斤拷锟斤拷锟斤拷锟叫达拷锟斤拷锟斤拷牡锟絩esult.xml.
			IDGenerator.restart();
			Text_P.set_parsenum(false);
			//second parse of meta.xml
			DefaultHandler secondMetaHandler = new Second_Meta_Handler();
			xmlReader.setContentHandler(secondMetaHandler);
			xmlReader.setErrorHandler(secondMetaHandler);
			xmlReader.parse(metaSource);

			//second parse of style.xml
			DefaultHandler secondStyleHandler = new Second_Style_Handler();
			xmlReader.setContentHandler(secondStyleHandler);
			xmlReader.setErrorHandler(secondStyleHandler);
			xmlReader.parse(styleSource);

			//second parse of content.xml
			DefaultHandler secondContentHandler = new Second_Content_Handler();
			xmlReader.setContentHandler(secondContentHandler);
			xmlReader.setErrorHandler(secondContentHandler);
			xmlReader.parse(contentSource);

			//扫锟斤拷锟斤拷锟侥碉拷锟斤拷锟斤拷每锟斤拷元锟斤拷追锟斤拷uof:locID锟斤拷uof:AttList锟斤拷锟斤拷.
			InputSource result = new InputSource(tmpFileName);
			DefaultHandler thirdHandler = new Third_Handler();
			xmlReader.setContentHandler(thirdHandler);
			xmlReader.setErrorHandler(thirdHandler);
			xmlReader.parse(result);

			Results_Processor.close();
			state = "                Convert successfully!!!";
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();

			if(state.equals("")){
				state = "Something wrong happened in this conversion!!!";
			}
		}

		_state_field.setText(state);
	}

	public static void main(String args[]){
		global_init();

		Convertor_ODF_To_UOF convApp = new Convertor_ODF_To_UOF("ODF-UOF Converter");

		convApp.pack();
		convApp.setLocation(450,230);
		convApp.setVisible(true);
		convApp.setResizable(false);
		convApp.setDefaultCloseOperation(EXIT_ON_CLOSE);
		System.setErr(new PrintStream(new JTextAreaStream(_result_area)));
	}
}
