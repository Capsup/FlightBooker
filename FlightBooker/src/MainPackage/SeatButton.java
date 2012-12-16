package MainPackage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * A button, which represents a Seat in a FlightPanel.
 * 
 * If the Seat is already booked, the SeatButton will be red. Non-booked seats will be the default color of a JButton.
 * If the user clicks on a SeatButton, it will become green, unless it is already booked.
 * 
 * @author Martin Juul Pedersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0 
 *
 */
public class SeatButton extends JButton
{
	private Seat seat;
	private Reservation currentReservation;
	private boolean isMySeat;
	
	private int myIndex = -1;
	
	private boolean isEnabled = true;
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			attemptReserve();
		}
	}
	
	/**
	 * 
	 * @param The Seat for which to make a button
	 * @param the reservation of which the seat should check if it belongs
	 */
	public SeatButton(Seat seat, Reservation currentReservation)
	{
		this.seat = seat;
		this.currentReservation = currentReservation;
		
		//We make any setup needed for the button to work.
		setup();
	}
	
	/**
	 * This method sets up the button listener of the seat button.
	 */
	void setup()
	{
		ButtonListener listener = new ButtonListener();
		
		addActionListener(listener);
	}
	
	/**
	 * attempt to reserve the seat. Based on the seats current state, it will reserve or unreserve.
	 */
	void attemptReserve()
	{
		Passenger[] passengers = currentReservation.getPassengers();
		
		if(!seat.isBooked())
		{
			int count = 0;
			
			for(int i=0; i< passengers.length; i++)
			{
				//If the seat is not booked we iterate through our passenger array and find the first passenger which seat is not reserved.
				
				if(passengers[i].getSeat() == null)
				{
					break;
				}
				
				count += 1;
			}
			
			//If the index found is still inside the bounds of the passenger array we reserve the seat.
			if(count < passengers.length)
			{
				reserve(passengers[count]);
				myIndex = count;
			}
		}
		else if(isMySeat)
		{
			//if the seat is reserved and it is part of the currently edited reservation.
			//we will unreserve it. myIndex holds the index of the passenger to remove the seat from.
			unreserve(passengers[myIndex]);
		}
		
		//In order to keep the seat display up to date, we update.
		update();
	}
	
	/**
	 * reserve this seat for the passenger
	 * @param the passenger to recieve the seat from
	 */
	void reserve( Passenger passenger)
	{
		seat.changeBookingStatus(true);
		
		passenger.setSeat(seat);
	}
	
	/**
	 * remove the seat from the given passenger
	 * @param the passenger to remove the seat from
	 */
	void unreserve(Passenger passenger)
	{
		seat.changeBookingStatus(false);
		
		passenger.setSeat(null);
		
		myIndex = -1;
	}
	
	/**
	 * set the booking status of the seat associated with the button.
	 * @param booking status
	 */
	public void setBooked(boolean booked)
	{
		seat.changeBookingStatus(booked);
	}
	
	/**
	 * Update the visual display of the seat.
	 */
	void update()
	{
		if(currentReservation != null)
		{
			Passenger[] passengerArray = currentReservation.getPassengers();
			
			isMySeat = false;
			
			if(currentReservation.getPassengers() != null)
			{
				for(int i = 0; i < passengerArray.length; i++)
				{
					//We iterate through the passenger array of the current reservation.
					//If the seat exists in the reservation, we know that the seat is part of this reservation.
					if(passengerArray[i].getSeat() != null && passengerArray[i].getSeat().getPosition().equals(seat.getPosition()))
					{
						isMySeat = true;
					}
				}
			}
		}
		
		if(seat.isBooked())
		{		
			if(isMySeat)
			{
				//If the seat is booked and it is ours, we enabled editing, aswell as change the color to green
				setBackground(Color.GREEN);
				setEnabled(isEnabled);
			}
			else
			{
				//If the seat is booked and it is not ours, we disabled editing, aswell as change the color to red
				setBackground(Color.RED);
				setEnabled(false);
			}
		}
		else
		{
			//If the seat is not booked, we change the background color to the default color and enable editing
			setBackground(null);
			setEnabled(isEnabled);
		}
	}
	
	/**
	 * Assign the current reservation of the seat to check from
	 * @param currentReservation
	 */
	public void setCurrentReservation(Reservation currentReservation)
	{
		this.currentReservation = currentReservation;
	}
	
	/**
	 * set the association index between the passenger of the current reservation and the seatbutton.
	 * @param count
	 */
	void setMyIndex(int count)
	{
		myIndex = count;
	}
	
	/**
	 * Set whether or not the button can be edited.
	 * @param status
	 */
	public void setButtonEnabled(boolean status)
	{
		isEnabled = status;
	}
}

