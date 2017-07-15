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
	}
	public void delete(Person p) {
		staffs.remove(p);
	}
	public void delete(int i) {
		staffs.remove(i);
	}
	public void edit(int i, Person p) {
		staffs.set(i, p);
	}
	
	//saveAs file
	public boolean saveAs(String newPath) throws UnsupportedEncodingException, FileNotFoundException {
		Writer writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(newPath), "UTF-8"));
		TsvWriter tsvWriter = new TsvWriter(writer, new TsvWriterSettings());
		
		String[] content = {"123","张三","80","100","80.0%"};
		tsvWriter.writeHeaders(DEFAULT_FIELDS);
		tsvWriter.writeRow(content);
   
		
		Iterator<Person> Staffs = this.staffs.iterator();
		while(Staffs.hasNext()) {
			//System.out.println(Staffs.next().getName());
			Person aaa = Staffs.next();
			String[] temp = {aaa.getPosition(), aaa.getName(), aaa.getCurrent(), aaa.getTotal(), aaa.getPercent(), aaa.getRecord()};
			//System.out.println(temp.length);
			tsvWriter.writeRow(temp);
		}
		/**
		for (int i = 0; i < staffs.size(); i++) {
			String[] temp = {staffs.get(i).getPosition(), staffs.get(i).getName(), staffs.get(i).getCurrent(), staffs.get(i).getTotal(), staffs.get(i).getPercent(), staffs.get(i).getRecord()};
			tsvWriter.writeRow(temp);
		}
		**/
		tsvWriter.close();
		filePath = newPath;
		return true;
	}
	
	//Save, call Save as method
	public boolean save() throws UnsupportedEncodingException, FileNotFoundException {
		saveAs(filePath);
		return true;
	}
	
	public void readFile(String path) throws FileNotFoundException {
		Reader in = new FileReader(path);
		TsvParserSettings settings = new TsvParserSettings();
   		settings.getFormat().setLineSeparator("\n");
   		TsvParser parser = new TsvParser(settings);
   		List<String[]> allRows = parser.parseAll(in);
   		//System.out.println(allRows.get(0)[0]);
   		//System.out.println(allRows.get(1)[0]);
   		fields = new ArrayList<String>(Arrays.asList(allRows.remove(0)));
   		while (allRows.size() > 0) {
   			String[] temp = allRows.remove(0);
   			Person p = new Person();
   			p.setPosition(temp[0]);
   			p.setName(temp[1]);
   			p.setCurrent(Double.parseDouble(temp[2]));
   			p.setTotal(Double.parseDouble(temp[3]));
   			
   			
   			//System.out.println(allRows.remove(0)[1]);
   		}
   		
   		//String[] temp = allRows.remove(0)[0].split(",");
   		//List<String> temp = Arrays.asList(allRows.remove(0));
   		//System.out.println(temp.length);
	}
	
	public ArrayList<Person> getCurrentStaff(String pos){
		ArrayList<Person> temp = new ArrayList<Person>();
		for (int i = 0; i < staffs.size(); i++) {
			if (staffs.get(i).getPosition().equals(pos)) {
				temp.add(staffs.get(i));
			}
		}
		return temp;
	}
	
	
	
}
