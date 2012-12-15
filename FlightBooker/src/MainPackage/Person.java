package MainPackage;

import java.io.Serializable;

/**
 * 
 * @author Martin Juul Pedersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
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

	public void updateReservations()
	{
		reservations = Database.getInstance().Get(id, Person.class).getReservations();
		
		
		//System.out.println(reservations.length);
	}
	
	public Reservation[] getReservations()
	{
		return reservations;
	}
	
	/*
	public Reservation[] getReservations()
    {
		//Database.getInstance().Get( reservations[], type )
		
		//reservations = Database.getInstance().Get(id, Person.class);
		
		if( reservations == null || reservations.length == 0 )
			return null;
		
		Reservation[] newReservations = new Reservation[reservations.length];
		
		int iCount = 0;
		for( Reservation reservation : reservations )
		{
			newReservations[iCount] = Database.getInstance().Get( reservation.getFlight().getID(), Flight.class ).getReservations()[reservation.getCurrentFlightReservationIndex()]; 
			
			iCount++;
		}
		
	    //return reservations;
		return newReservations;
    }
    */

	public void setReservations( Reservation[] reservations )
    {
	    this.reservations = reservations;
    }
	
	public void removeReservationAt(int index)
	{
		updateReservations();
		
		Reservation[] modifiedReservations = getReservations();
		
		//Flight Reservations
		for(int i=index; i<modifiedReservations.length-1; i++)
		{
			modifiedReservations[i] = modifiedReservations[i+1];
		}
		
		Reservation[] newReservations = new Reservation[getReservations().length-1];
		
		for(int j=0; j<newReservations.length; j++)
		{
			newReservations[j] = modifiedReservations[j];
			newReservations[j].setCurrentFlightReservationIndex(newReservations[j].getCurrentFlightReservationIndex()-1);
		}
		
		setReservations(newReservations);
	}
	
	/*public String toString()
	{
		return firstName;
	}*/
}
