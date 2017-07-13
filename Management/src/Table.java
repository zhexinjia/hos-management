import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {
	public static final String DEFAULT_FIELDS[] = {"名字", "当前得分", "总分", "百分比" };
	public ArrayList<String> fields;
	public ArrayList<Person> staffs;
	public String fileName;
	public Group allStaffs;
	public ArrayList<Person> currentStaffs;
	
	public boolean hasChanged;
	
	public Table() {
		this.fields = new ArrayList<String>(Arrays.asList(DEFAULT_FIELDS));
		this.staffs = new ArrayList<Person>();
		this.fileName = null;
		this.hasChanged = false;
		this.allStaffs = null;
	}
	
	
}
