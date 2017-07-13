import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Person {
	private String name;
	private double currentScore = 0;
	private double totalScore = 0;
	//private double percent = 0;
	private String record = "";
	private String groupName = "";
	
	//get name and set name
	public void setName(String s) {
		name = s;
	}
	public String getName() {
		return name;
	}
	
	//get scores and set scores
	public void setCurrent(double num) {
		currentScore = num;
	}
	public double getCurrent() {
		return currentScore;
	}
	public void setTotal(double num) {
		totalScore = num;
	}
	public double getTotal() {
		return totalScore;
	}
	
	//get record and set record
	public void updateRecord(double num, String s) {
		currentScore += num;
		LocalDate localDate = LocalDate.now();
		String currentDate = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate);
		record += currentDate + "\n得分：" + num + "\n原因：" + s + "\n当前总得分：" + getCurrent()
		+ ", " + getPercent() + "%\n\n";
		
	}
	public String getRecord() {
		return record;
	}
	
	//get and set groupName
	public void setGroupName(String s) {
		groupName = s;
	}
	public String getGroupName() {
		return groupName;
	}
	
	//get percent
	public double getPercent() {
		return 100*currentScore/totalScore;
	}
	
	
	
}
