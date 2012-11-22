
public class Customer {

	private String firstName;
	private String lastName;
	private String sex;
	private String CPRnumber;
	private String passportNumber;
	private String birthDate;
	private int customerID;
	private boolean isGay;	
	
	public Customer(String firstName, String lastName, boolean male, String CPRnumber, String passportNumber, String birthDate, int customerID, boolean isGay)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		
		if(male)
			sex = "Male";
		else 
			sex = "Female";
		
		this.CPRnumber = CPRnumber;
		this.passportNumber = passportNumber;
		this.birthDate = birthDate;
		this.customerID = customerID;			
	}
	
}
