import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Person {
	private String name = "";
	private String position = "";
	private double currentScore = 0;
	private double totalScore = 0;
	private double percent = 0;
	private String record = "";
	private String groupName = "";
	
	//get and set Position
	public void setPosition(String s) {
		position = s;
	}
	public String getPosition() {
		return position;
	}
	
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
	public void updateCurrent(double num) {
		currentScore += num;
	}
	public String getCurrent() {
		return Double.toString(currentScore);
	}
	public void setTotal(double num) {
		totalScore = num;
	}
	public void updateTotal(double num) {
		totalScore += num;
	}
	public String getTotal() {
		return Double.toString(totalScore);
	}
	
	//get record and set record
	public void updateRecord(String s) {
		//currentScore += num;
		LocalDate localDate = LocalDate.now();
		String currentDate = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate);
		record += currentDate + "\n得分：" + currentScore + "\n原因：" + s + "\n当前总得分：" + getCurrent()
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
	public String getPercent() {
		return Double.toString(100*currentScore/totalScore);
	}
	
	
	
}
