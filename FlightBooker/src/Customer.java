
public class Customer {

	private String firstName;
	private String surName;
	private String gender;
	private String dateOfBirth;
	private String country;
	private String nationality;
	private String adress;
	private String phone;
	private String passportNumber;
	private int customerID;

	/*
	 * Opretter en ny Customer og initialiserer alle dets felter.
	 */
	public Customer(String firstName, String surName, String gender, String dateOfBirth, String country,
			String nationality, String adress, String phone, String passportNumber, int customerID)
	{
		this.firstName = firstName;
		this.surName = surName;
		this.gender = gender;		
		this.dateOfBirth = dateOfBirth;
		this.country = country;
		this.nationality = nationality;
		this.adress = adress;
		this.phone = phone;
		this.passportNumber = passportNumber;
		this.customerID = customerID;			
	}



}
