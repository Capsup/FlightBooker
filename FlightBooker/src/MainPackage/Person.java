package MainPackage;

import java.io.Serializable;

public class Person implements Serializable, Uploadable
{
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
	
	private Reservation[] reservations;
	
	private int id;
	
	@Override
    public int getID()
    {
	    return id;
    }

	@Override
    public void setID( int iID )
    {
	    id = iID;
    }

	/*
	 * Opretter en ny Person og initialiserer alle dets felter.
	 */
	public Person(String firstName, String surName, String gender, String dateOfBirth, String country,
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
		this.id = customerID;
	}

	public String getFirstName()
	{
		return firstName;
	}
	
	public String getSurName()
	{
		return surName;
	}
	
	public String getGender()
	{
		return gender;
	}
	
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public String getNationality()
	{
		return nationality;
	}
	
	public String getAdress()
	{
		return adress;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getPassportNumber()
	{
		return passportNumber;
	}
	
	public int getCustomerID()
	{
		return customerID;
	}
	
	/*public String toString()
	{
		return firstName;
	}*/
}
