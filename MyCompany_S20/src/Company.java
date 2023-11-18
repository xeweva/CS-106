import java.util.ArrayList;

public class Company {
	private String name;
	private String location;
	public ArrayList<Employee> employees = new ArrayList<Employee>();

	public Company(String name, String location, ArrayList<Employee> employees) {
		super();
		this.name = name;
		this.location = location;
		this.employees = employees;
	}
	
	public void payEmployees() {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ArrayList<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
	}


}