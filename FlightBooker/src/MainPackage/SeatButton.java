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
	
	private boolean isEnabled = true;
	
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
		
		if(!seat.isBooked())
		{
			int count = 0;
			
			for(int i=0; i< passengers.length; i++)
			{
				if(passengers[i].getSeat() == null)
				{
					break;
				}
				
				count += 1;
			}
			
			if(count < passengers.length)
			{
				reserve(passengers[count]);
				myIndex = count;
			}
		}
		else if(isMySeat)
		{
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
	
	public void setBooked(boolean booked)
	{
		seat.changeBookingStatus(booked);
	}
	
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
				//System.out.println("WAT");
				setBackground(Color.GREEN);
				setEnabled(isEnabled);
			}
			else
			{
				setBackground(Color.RED);
				setEnabled(false);
			}
		}
		else
		{
			setBackground(null);
			setEnabled(isEnabled);
		}
		
	}
	
	public void setCurrentReservation(Reservation currentReservation)
	{
		this.currentReservation = currentReservation;
	}
	
	void setMyIndex(int count)
	{
		myIndex = count;
	}
	
	public void setButtonEnabled(boolean status)
	{
		isEnabled = status;
	}
}

