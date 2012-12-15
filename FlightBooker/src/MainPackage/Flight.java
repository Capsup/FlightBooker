package MainPackage;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
	
	public Flight(Calendar flyDate, Plane plane, Airport origin, Airport destination, int ID)
	{
		this.flyDate = flyDate;
		
		this.plane = plane;
		
		this.seatArray = plane.getSeatArray();
		
		this.origin = origin;
		
		this.destination = destination;
		
		this.id = ID;
	}
	
	public boolean checkSeatReservation(int x, int y)
	{
		return seatArray[x][y].isBooked();
	}
	
	public void changePlane(Plane newPlane)
	{
		if(newPlane.getSeatAmount() >= plane.getSeatAmount())
			plane = newPlane;
		else
			System.out.println("The new plane needs to be of same size or bigger");
	}
	
	public Seat getSeat(int x, int y)
	{
		if(seatArray[x][y] != null)
		{
			return seatArray[x][y];
		}
		else 
		{
			System.out.println("Seat does not exist");
			
			return null;
		}
		
	}
	
	public Calendar getDate()
	{
		return flyDate;
	}
	
	public Plane getPlane()
	{
		return plane;
	}
	
	public Reservation[] getReservations()
	{
		return reservations;
	}
	
	public Airport getOrigin()
	{
		return origin;
	}
	
	public Airport getDestination()
	{
		return destination;
	}
	
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
	
	public int getSeatAmount()
	{
		return seatArray.length*seatArray[0].length;
	}
	
	public void setReservations(Reservation[] reservations)
	{
		this.reservations = reservations;
	}
	
	public int addReservation(Reservation reservation)
	{
		Reservation[] newReservations;
		
		int newIndex = 0;
		
		if(reservations != null)
		{
			newReservations = new Reservation[reservations.length+1];
			
			for (int i = 0; i < reservations.length; i++) 
			{
				newReservations[i] = reservations[i];
			}
			
			newReservations[reservations.length] = reservation;
			
			newIndex = reservations.length;
		}
		else 
		{
			newReservations = new Reservation[1];	
			
			newReservations[0] = reservation;
			
			newIndex = 0;
		}
		
		reservations = newReservations;
		
		return newIndex;
	}
	
	public void replaceReservation(int index, Reservation reservation)
	{	
		Reservation[] newReservations = getReservations();
		
		newReservations[index] = reservation;
		
		setReservations(newReservations);
	}
	
	public void removeReservationAt(int index)
	{
		Reservation[] modifiedReservations = getReservations();
		
		for(int i=index; i<modifiedReservations.length-1; i++)
		{
			modifiedReservations[i] = modifiedReservations[i+1];
		}
		
		Reservation[] newReservations = new Reservation[getReservations().length-1];
		
		for(int j=0; j<newReservations.length; j++)
		{
			newReservations[j] = modifiedReservations[j];
			newReservations[j].setCurrentFlightReservationIndex(j);
		}
		
		
		setReservations(newReservations);
	}
}
