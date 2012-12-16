package MainPackage;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents our datatype for the Reservations in our system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 */
public class Reservation implements Serializable, Uploadable
{
	private Person owner;
	private Passenger[] passengerArray;
	private Flight flight;
    private Calendar reservedDate;
    
	private int id;
	
	//The index of Reservation's position in the Flight's Reservation array
	private int currentFlightReservationIndex = -1;
	
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
	
	public Reservation()
	{
	}
	
	/**
	 * Gets the owner of the Reservation
	 * @return a Person, who is the owner of the Reservation
	 */
	public Person getOwner()
	{
		return owner;
	}
	
	/**
	 * Sets the owner of the Reservation
	 * @param person the owner of the Reservation
	 */
	public void setOwner( Person person )
	{
		this.owner = person;
	}
	
	/**
	 * Gets an array of Persons containing the passengers of the reservation.
	 * @return an array of Persons containing the passengers of the reservation.
	 */
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
	
	/**
	 * Sets an array of Persons containing the passengers of the Reservation.
	 * @param an array of Persons containing the passengers of the Reservation.
	 */
	public void setPassengers(Passenger[] passengers)
	{
		passengerArray = passengers;
	}
	
	/**
	 * Returns the date of departure of the Reservation's flight.
	 * @return a Calendar, which represents the date of departure
	 */
	public Calendar getFlyDate()
	{
		return flight.getDate();
	}
	
	/**
	 * Returns the date the Reservation was created
	 * @return a Calendar, which represents the date the Reservation was created
	 */
	public Calendar getReservationDate()
	{
		return reservedDate;
	}
	
	/**
	 * Sets the date the Reservation was created
	 * @param a Calendar, which represents the date the Reservation was created
	 */
	public void setReservedDate(Calendar newDate)
	{
		reservedDate = newDate;
	}
	
	/**
	 * Gets the Flight of the Reservation
	 * @return the Flight of the Reservation
	 */
	public Flight getFlight()
	{
		if(flight != null)
			return flight;
		else 
			return null;
	}
	
	/**
	 * Sets the Flight of the Reservation
	 * @param the Flight of the Reservation
	 */
	public void setFlight(Flight flight)
	{
		this.flight = flight;
	}
	
	/**
	 * Sets the index of Reservation's position in the Flight's Reservation array
	 * @param index The index of Reservation's position in the Flight's Reservation array
	 */
	public void setCurrentFlightReservationIndex(int index)
	{
		currentFlightReservationIndex = index;
	}
	
	/**
	 * Gets the index of Reservation's position in the Flight's Reservation array
	 * @return index The index of Reservation's position in the Flight's Reservation array
	 */
	public int getCurrentFlightReservationIndex()
	{
		return currentFlightReservationIndex;
	}
}
