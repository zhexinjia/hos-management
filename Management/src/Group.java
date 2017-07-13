import java.util.ArrayList;

public class Group {
	private String groupName;
	private ArrayList<Person> group;
	
	public void add(Person person) {
		group.add(person);
	}
	
	public boolean delete(Person person) {
		return group.remove(person);
	}
	
	public ArrayList<Person> getGroup(){
		return group;
	}
	

}
