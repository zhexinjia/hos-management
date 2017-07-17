import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class Person {
	
	/**
	private String name = "";
	private String position = "";
	private double currentScore = 0;
	private double totalScore = 0;
	private double percent = 0;
	private String record = "";
	private String groupName = "";
	**/
	
	//position[0], name[1], currentScore[2], totalScore[3], percent[4], record[5]
	
	ArrayList<String> vals;
	
	public Person() {
		this.vals = new ArrayList<String>();
	}
	public Person(Iterator<String> iter) {
		this.vals = new ArrayList<String>();
		while (iter.hasNext()) {
			this.vals.add(iter.next());
		}
		updatePercent();
	}
	public Person(String[] fields) {
		this.vals = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			vals.add(fields[i]);
		}
		updatePercent();
	}
	
	//get and set Position
	public void setPosition(String s) {
		this.vals.set(0, s);
	}
	public String getPosition() {
		return this.vals.get(0);
	}
	
	//get name and set name
	public void setName(String s) {
		this.vals.set(1, s);
	}
	public String getName() {
		return this.vals.get(1);
	}
	
	//get, update, set currentScore
	public void setCurrent(String num) {
		this.vals.set(2, num);
		updatePercent();
	}
	public void updateCurrent(String num) {
		Double current = Double.parseDouble(this.vals.get(2));
		Double updateNum = Double.parseDouble(num);
		Double result = current += updateNum;
		this.vals.set(2, Double.toString(result));
		updatePercent();
	}
	public String getCurrent() {
		return this.vals.get(2);
	}
	
	//set, update, get totalScore
	public void setTotal(String num) {
		this.vals.set(3, num);
		updatePercent();
	}
	public void updateTotal(String num) {
		Double total = Double.parseDouble(this.vals.get(3));
		Double updateNum = Double.parseDouble(num);
		Double result = total += updateNum;
		this.vals.set(3, Double.toString(result));
		updatePercent();
	}
	public String getTotal() {
		return this.vals.get(3);
	}
	
	//get record and set record
	public void updateRecord(String s) {
		//currentScore += num;
		LocalDate localDate = LocalDate.now();
		String currentDate = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate);
		String record = this.vals.get(5) + currentDate + "\n得分：" + getCurrent() + "\n原因：" + s + "\n当前总得分：" + getTotal()
		+ ", " + getPercent() + "%\n\n";
	}
	public String getRecord() {
		return this.vals.get(5);
	}
	
	
	//get percent
	public String getPercent() {
		return this.vals.get(4);
	}
	
	public void updatePercent() {
		try {
			Double current = Double.parseDouble(this.vals.get(2));
			Double total = Double.parseDouble(this.vals.get(3));
			Double result = 100*current/total;
			this.vals.set(4, Double.toString(result)+"%");
		}catch(Exception e) {
			
		}
		
	}
	
	//get ArrayList
	public ArrayList<String> getArrayList(){
		return this.vals;
	}
	
	//getArray
	public String[] getFormattedArray() {
		String[] temp = new String[this.vals.size()];
		for (int i = 0; i < this.vals.size(); i++) {
			temp[i] = this.vals.get(i);
		}
		return temp;
	}
	
	
	
}
