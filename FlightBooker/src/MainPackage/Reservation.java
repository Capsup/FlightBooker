package MainPackage;
import java.util.Date;

public class Reservation 
{
	private Person owner;
	private Passenger[] passengerArray;
	private Flight flight;
	private Date reservedDate;
	
	public Reservation(Person customer, Flight flight, Date reservedDate, Passenger[] passengers)
	{
		owner = customer;
		
		passengerArray = passengers;
		
		this.flight = flight;
		this.reservedDate = reservedDate;
	}
	
	//Returns the owner of the reservation.
	public Person getOwner()
	{
		return owner;
	}
	
	//Returns an array of Persons containing the passengers of the reservation.
	public Passenger[] getPassengers()
	{
		return passengerArray;
	}
	
	//Returns the Date object of the flight.
	public Date getFlyDate()
	{
		return flight.getDate();
	}
}
