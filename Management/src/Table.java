import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;


public class Table {
	public static final String DEFAULT_FIELDS[] = {"职位", "姓名", "当前得分", "总分", "百分比","记录" };
	public ArrayList<String> fields;
	public ArrayList<Person> staffs; //所有员工
	public String filePath;
	public Group allStaffs;
	public ArrayList<Person> currentStaffs;
	
	public boolean hasChanged;
	
	//default constructor
	public Table() {
		this.fields = new ArrayList<String>(Arrays.asList(DEFAULT_FIELDS));
		this.staffs = new ArrayList<Person>();
		this.filePath = null;
		this.hasChanged = false;
		this.allStaffs = null;
	}
	
	public void add(Person p) {
		staffs.add(p);
		hasChanged = true;
	}
	public void delete(Person p) {
		staffs.remove(p);
		hasChanged = true;
	}
	public void delete(int i) {
		staffs.remove(i);
		hasChanged = true;
	}
	public void edit(int i, Person p) {
		staffs.set(i, p);
		hasChanged = true;
	}
	
	public void replace(int i, Person p) {
		staffs.set(i, p);
		hasChanged = true;
	}
	
	//saveAs file
	public boolean saveAs(String newPath) throws UnsupportedEncodingException, FileNotFoundException {
		Writer writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(newPath), "UTF-8"));
		CsvWriter tsvWriter = new CsvWriter(writer, new CsvWriterSettings());
		
		//String[] content = {"123","张三","80","100","80.0%"};
		//tsvWriter.writeHeaders(DEFAULT_FIELDS);
		//tsvWriter.writeRow(content);
		
		Iterator<Person> Staffs = this.staffs.iterator();
		
		while(Staffs.hasNext()) {
			//System.out.println(Staffs.next().getName());
			//Person aaa = Staffs.next();
			//String[] temp = {aaa.getPosition(), aaa.getName(), aaa.getCurrent(), aaa.getTotal(), aaa.getPercent(), aaa.getRecord()};
			//System.out.println(temp.length);
			Person p = Staffs.next();
			tsvWriter.writeRow(p.getFormattedArray());
		}
		/**
		for (int i = 0; i < staffs.size(); i++) {
			String[] temp = {staffs.get(i).getPosition(), staffs.get(i).getName(), staffs.get(i).getCurrent(), staffs.get(i).getTotal(), staffs.get(i).getPercent(), staffs.get(i).getRecord()};
			tsvWriter.writeRow(temp);
		}
		**/
		tsvWriter.close();
		filePath = newPath;
		hasChanged = false;
		return true;
	}
	
	//Save, call Save as method
	public boolean save() throws UnsupportedEncodingException, FileNotFoundException {
		saveAs(filePath);
		return true;
	}
	
	//读指定文件
	//FIXME:每行必须有6个值，不然无法读取
	public void readFile(String path) throws FileNotFoundException {
		Reader in = new FileReader(path);
		filePath = path;
		TsvParserSettings settings = new TsvParserSettings();
   		settings.getFormat().setLineSeparator("\n");
   		TsvParser parser = new TsvParser(settings);
   		List<String[]> allRows = parser.parseAll(in);
   		
   		while (allRows.size() > 0) {
   			String[] temp = allRows.remove(0);
   			System.out.println("before add P");
   			Person p = new Person(temp[0].split(",|，"));
   			System.out.println("list大小：" + p.getArrayList().size());
   			System.out.println("after add P");
   			staffs.add(p);
   		}
   		hasChanged = false;
	}
	
	public ArrayList<Person> getCurrentStaff(String pos){
		ArrayList<Person> temp = new ArrayList<Person>();
		for (int i = 0; i < staffs.size(); i++) {
			if (staffs.get(i).getPosition().equals(pos)) {
				temp.add(staffs.get(i));
			}
		}
		currentStaffs = temp;
		return temp;
	}
	
	
	
}
