package MainPackage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SeatButton extends JButton
{
	private Seat seat;
	private Reservation currentReservation;
	private boolean isMySeat;
	
	private int myIndex = -1;
	
	class ButtonListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event)
		{
			attemptReserve();
		}
	}
	
	public SeatButton(Seat seat, Reservation currentReservation)
	{
		this.seat = seat;
		this.currentReservation = currentReservation;
		
		setup();
	}
	
	void setup()
	{
		ButtonListener listener = new ButtonListener();
		
		addActionListener(listener);
		
		update();
	}
	
	void attemptReserve()
	{
		Passenger[] passengers = currentReservation.getPassengers();
		
		int count = 0;
		
		for(int i=0; i< passengers.length; i++)
		{
			if(passengers[i].getSeat() == null)
			{
				break;
			}
			
			count += 1;
		}
		
		if(!seat.isBooked())
		{
			if(count < passengers.length)
			{
				reserve(passengers[count]);
				myIndex = count;
			}
		}
		else if(isMySeat)
		{
			//FIX
			unreserve(passengers[myIndex]);
		}
		
		update();
	}
	
	void reserve( Passenger passenger)
	{
		seat.changeBookingStatus(true);
		
		passenger.setSeat(seat);
	}
	
	void unreserve(Passenger passenger)
	{
		seat.changeBookingStatus(false);
		
		passenger.setSeat(null);
		
		myIndex = -1;
	}
	
	public void setBooked()
	{
		seat.changeBookingStatus(true);
	}
	
	void update()
	{
		Passenger[] passengerArray = currentReservation.getPassengers();
		
		isMySeat = false;
		
		if(currentReservation.getPassengers() != null)
		{
			for(int i = 0; i < passengerArray.length; i++)
			{
				if(passengerArray[i].getSeat() != null && passengerArray[i].getSeat().getPosition().equals(seat.getPosition()))
				{
					isMySeat = true;
				}
			}
		}
		
		if(seat.isBooked())
		{
			if(isMySeat == true)
				setBackground(Color.GREEN);
			else
				setBackground(Color.RED);
		}
		else
		{
			setBackground(null);
		}
	}
}
