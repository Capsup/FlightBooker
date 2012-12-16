package MainPackage;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents our datatype for the Flights in our system.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0 
 */
public class Flight implements Serializable, Uploadable
{
	private Calendar flyDate;
	private Plane plane;
	private Airport origin;
	private Airport destination;
	
	private Seat[][] seatArray;
	
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
	 * Creates an instance of Flight
	 * 
	 * @param flyDate The date of depature
	 * @param plane The type of airplane
	 * @param origin The location of depature
	 * @param destination The destination
	 * @param ID The ID of the Flight
	 */
	public Flight(Calendar flyDate, Plane plane, Airport origin, Airport destination, int ID)
	{
		this.flyDate = flyDate;
		
		this.plane = plane;
		
		this.seatArray = plane.getSeatArray();
		
		this.origin = origin;
		
		this.destination = destination;
		
		this.id = ID;
	}
	
	/**
	 * Checks whether the Seat is booked or not
	 * 
	 * @param x The x coordinate of the Seat
	 * @param y The y coordinate of the Seat
	 * @return If the Seat is booked
	 */
	public boolean checkSeatReservation(int x, int y)
	{
		return seatArray[x][y].isBooked();
	}
	
	/**
	 * Returns the Seat of a given position
	 * @param x The x coordinate of the Seat
	 * @param y The y coordinate of the Seat
	 * @return a Seat at the x,y position
	 */
	public Seat getSeat(int x, int y)
	{
		if(seatArray[x][y] != null)
		{
			return seatArray[x][y];
		}
		else 
		{			
			return null;
		}
		
	}
	
	/**
	 * Gets the date of depature of the Flight
	 * @return a Calendar representing the date of depature
	 */
	public Calendar getDate()
	{
		return flyDate;
	}
	
	/**
	 * Gets the Plane of the Flight
	 * @return the Plane of the Flight
	 */
	public Plane getPlane()
	{
		return plane;
	}
	
	/**
	 * Gets the reservations of the Flight
	 * @return a Reservation[] of the Flights reservations
	 */
	public Reservation[] getReservations()
	{
		return reservations;
	}
	
	/**
	 * Gets the location of depature of the Flight
	 * @return the Airport of depature
	 */
	public Airport getOrigin()
	{
		return origin;
	}
	
	/**
	 * Gets the destination of the Flight
	 * @return the destination Airport
	 */
	public Airport getDestination()
	{
		return destination;
	}
	
	/**
	 * Gets the amount of seats available on the Flight
	 * @return an integer representing the available amount of Seats left
	 */
	public int getSeatsLeft()
	{
		int seatsLeft = getSeatAmount();
		
		if(reservations != null)
		{
			int seatsTaken = 0;
			
			for (Reservation reservation : reservations) 
			{
				seatsTaken += reservation.getPassengers().length;
			}
			
			seatsLeft -= seatsTaken;
		}
		
		return seatsLeft;
	}
	
	/**
	 * Gets the seats of the Flight
	 * @return a Seat[][] of all the Seats of the Flight
	 */
	public Seat[][] getSeats()
	{
		return seatArray;
	}
	
	/**
	 * Gets the amount of Seats on the Flight
	 * @return an integer, representing the amount of Seats on the Flight
	 */
	public int getSeatAmount()
	{
		return seatArray.length*seatArray[0].length;
	}
	
	/**
	 * Sets the Flight's reservations
	 * @param reservations the reservations to be set for the Flight
	 */
	public void setReservations(Reservation[] reservations)
	{
		this.reservations = reservations;
	}
	
	/**
	 * Adds a Reservation to the Flight
	 * 
	 * @param reservation the Reservation to be added
	 * @return an integer, representing the index of the Reservation
	 */
	public int addReservation(Reservation reservation)
	{
		Reservation[] newReservations;
		
		int newIndex = 0;
		
		//If this isn't the plane's first Reservation, add the new Reservations an array 
		if(reservations != null)
		{
			//Creates a new array of reservations the length of the Flight's current Reservations plus one
			newReservations = new Reservation[reservations.length+1];
			
			//Put all of the Plane's old Reservations into this new array
			for (int i = 0; i < reservations.length; i++) 
			{
				newReservations[i] = reservations[i];
			}
			
			//Add the new Reservation to the array
			newReservations[reservations.length] = reservation;
			
			newIndex = reservations.length;
		}
		//If this is the Plane's first Reservation
		else 
		{
			newReservations = new Reservation[1];	
			
			newReservations[0] = reservation;
			
			newIndex = 0;
		}
		//Saves the new Reservations array 
		reservations = newReservations;
		
		return newIndex;
	}
	
	/**
	 * Replaces a Reservation of the Flight
	 * @param index The index of the Reservation to be replaced
	 * @param reservation The Reservation to replace the current Reservation
	 */
	public void replaceReservation(int index, Reservation reservation)
	{	
		Reservation[] newReservations = getReservations();
		
		newReservations[index] = reservation;
		
		setReservations(newReservations);
	}
	
	/**
	 * Removes a Reservation of the Flight at a given index
	 * @param index the index of the Reservation to remove.
	 */
	public void removeReservationAt(int index)
	{
		//Gets all of the Flight's current reservations
		Reservation[] modifiedReservations = getReservations();
		
		//Moves all of the Flight's Reservations in the array to overwrite the Reservation to be removed and compensating for the lack of the removed Reservation
		for(int i=index; i<modifiedReservations.length-1; i++)
		{
			modifiedReservations[i] = modifiedReservations[i+1];
		}
		
		Reservation[] newReservations = new Reservation[getReservations().length-1];
		
		//Makes a new array with the updated Reservations, and each of the Reservations Indices.
		for(int j=0; j<newReservations.length; j++)
		{
			newReservations[j] = modifiedReservations[j];
			newReservations[j].setCurrentFlightReservationIndex(j);
		}
		
		setReservations(newReservations);
	}
}
