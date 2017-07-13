
public class main {
	public static void main(String[] args) {
		Person test = new Person();
		test.setTotal(5);
		test.updateRecord(5, "no reason");
		System.out.println(test.getRecord());
	}
}
