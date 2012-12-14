package MainPackage;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Reservation implements Serializable, Uploadable
{
	private Person owner;
	private Passenger[] passengerArray;
	private Flight flight;
    private Calendar reservedDate;
    
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
	
	public Reservation(Person customer, Flight flight, Calendar reservedDate, Passenger[] passengers, int ID)
	{
		owner = customer;
		
		passengerArray = passengers;
		
		this.flight = flight;
		this.reservedDate = reservedDate;
		
		this.id = ID;
	}
	
	//Returns the owner of the reservation.
	public Person getOwner()
	{
		return owner;
	}
	
	public void setOwner( Person person )
	{
		this.owner = person;
	}
	
	//Returns an array of Persons containing the passengers of the reservation.
	public Passenger[] getPassengers()
	{
		if(passengerArray != null)
		{
			return passengerArray;
		}
		else
		{
			return null;	
		}
	}
	
	public void setPassengers(Passenger[] passengers)
	{
		passengerArray = passengers;
	}
	
	//Returns the Date object of the flight.
	public Calendar getFlyDate()
	{
		return flight.getDate();
	}
	
	public Calendar getReservationDate()
	{
		return reservedDate;
	}
	
	public void setReservedDate(Calendar newDate)
	{
		reservedDate = newDate;
	}
	
	public Flight getFlight()
	{
		if(flight != null)
			return flight;
		else 
			return null;
	}
	
	public void setFlight(Flight flight)
	{
		this.flight = flight;
	}
	
}
