package MainPackage;

import java.io.Serializable;

/**
 * This class represents our datatype for the Persons in our system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 *
 */
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

	/**
	 * Creates a new Person
	 * @param firstName the first name of the Person
	 * @param surName the surname of the Person
	 * @param gender the gender of the Person
	 * @param dateOfBirth the date of birth of the Person
	 * @param country the country of the Person
	 * @param nationality the nationality of the Person
	 * @param adress the adress of the Person
	 * @param phone the phone number of the Person
	 * @param passportNumber the passport number of the Person
	 * @param customerID the customerID of the Person
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

	/**
	 * Gets the first name of the Person
	 * @return the first name of the Person
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * Gets the surname of the Person
	 * @return the surname of the Person
	 */
	public String getSurName()
	{
		return surName;
	}

	/**
	 * Gets the gender of the Person
	 * @return the gender of the Person
	 */
	public String getGender()
	{
		return gender;
	}

	/**
	 * Gets the date of birth of the Person
	 * @return the date of birth of the Person
	 */
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	/**
	 * Gets the country of the Person
	 * @return the country of the Person
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * Gets the nationality of the Person
	 * @return the nationality of the Person
	 */
	public String getNationality()
	{
		return nationality;
	}

	/**
	 * Gets the address of the Person
	 * @return the address of the Person
	 */
	public String getAdress()
	{
		return adress;
	}

	/**
	 * Gets the phone number of the Person
	 * @return the phone number of the Person
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * Gets the passport number of the Person
	 * @return the passport number of the Person
	 */
	public String getPassportNumber()
	{
		return passportNumber;
	}

	/**
	 * Gets the customerID of the Person
	 * @return the customerID of the Person
	 */
	public int getCustomerID()
	{
		return customerID;
	}

	/**
	 * Updates a Person's reservations
	 */
	public void updateReservations()
	{
		reservations = Database.getInstance().Get(id, Person.class).getReservations();

	}

	/**
	 * Gets a Person's reservations
	 * @return a Reservation[] of all the Person's reservations
	 */
	public Reservation[] getReservations()
	{
		return reservations;
	}

	
	/**
	 * Gets a Person's reservations
	 * @param reservations a Reservation[] of all the Person's reservations
	 */
	public void setReservations( Reservation[] reservations )
	{
		this.reservations = reservations;
	}

	/**
	 * Removes one of the Person's Reservations, specified by the index
	 * @param index the index of the Reservation to be removed
	 */
	public void removeReservationAt(int index)
	{
		updateReservations();

		//Gets all of the Person's current reservations
		Reservation[] modifiedReservations = getReservations();
		
		//Moves all of the Person's Reservations in the array to overwrite the Reservation to be removed and compensating for the lack of the removed Reservation
		for(int i=index; i<modifiedReservations.length-1; i++)
		{
			modifiedReservations[i] = modifiedReservations[i+1];
		}

		
		//Makes a new array with the updated Reservations, and each of the Reservations Indices.
		Reservation[] newReservations = new Reservation[getReservations().length-1];
		for(int j=0; j<newReservations.length; j++)
		{
			newReservations[j] = modifiedReservations[j];
			newReservations[j].setCurrentFlightReservationIndex(newReservations[j].getCurrentFlightReservationIndex()-1);
		}

		setReservations(newReservations);
	}
}
