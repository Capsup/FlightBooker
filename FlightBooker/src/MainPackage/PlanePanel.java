package MainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

public class PlanePanel extends JPanel 
{
	private Flight flight;
	private Plane plane;
	private Dimension panelSize;
	private Reservation currentReservation;
	private SeatButton[][] seatButtonArray;
	
	private HashMap<Dimension, SeatButton> seatMap;
	
	//Properties
	double scaleModifier;
	int panelToButtonScale;
	
	int squaredSize;
	int indent = (int)(10/scaleModifier);
	
	int leftPadding;
	int topPadding;		
	
	Insets insets = getInsets();
	
	//Other
	private boolean editable;
	
	public PlanePanel(Flight flight, Reservation reservation, Dimension size, boolean editable)
	{
		this.flight = flight;
		plane = flight.getPlane();
		this.panelSize = size;
		currentReservation = reservation;
		
		this.editable = editable;
		
		setupProperties();
		
		makeContent();
	}
	
	void makeContent()
	{
		JPanel mainPanel;
		
		try
		{
			String path = "/images/"+plane.getPlaneTypeString()+".png";
			
			//Needs automization
			Rectangle rect = new Rectangle(0,0, panelSize.width, panelSize.height);
			
			mainPanel = new JPanelWithBackground(path, rect);
			
			mainPanel.setPreferredSize(panelSize);
		}
		catch(IOException e)
		{
			mainPanel = new JPanel();
		}
		
		mainPanel.setLayout(null);
		
		String stringToUse = flight.getOrigin().getName()+" - "+flight.getDestination().getName();
		
		JLabel routeLabel = new JLabel(stringToUse);
		routeLabel.setBounds(panelSize.width/20, panelSize.height/20, 250, 25);
		
		stringToUse = flight.getPlane().getPlaneTypeString();
		
		JLabel planeTypeLabel = new JLabel(stringToUse);
		planeTypeLabel.setBounds((panelSize.width/20)*19-100, panelSize.height/20, 250, 25);
		
		
		mainPanel.add(routeLabel);
		mainPanel.add(planeTypeLabel);
				
		/*
		Insets insets = getInsets();
		Dimension size;
		
		Seat[][] seatArray = plane.getSeatArray();
		
		
		double scaleModifier = Math.sqrt((seatArray.length*seatArray[0].length)/30f);
		int panelToButtonScale = panelSize.height/10;
		
		int squaredSize = (int) (panelToButtonScale/scaleModifier);
		int indent = (int)(10/scaleModifier);
		
		int leftPadding = panelSize.width/2-((squaredSize+indent)*seatArray[0].length)/2;
		int topPadding = panelSize.height/2-((squaredSize+indent)*seatArray.length)/2;		
		*/
		
		SeatButton seatButton;
		
		//Initialize seats for the current Reservation
		Passenger[] passengers = currentReservation.getPassengers();
		Dimension passengerSeatPosition;
		
		if(passengers != null)
		{
			int count = 0;
			
			for (Passenger passenger : passengers) 
			{
				if(passenger != null && passenger.getSeat() != null)
				{
					passengerSeatPosition = passenger.getSeat().getPosition();
					
					int x = passengerSeatPosition.width;
					int y = passengerSeatPosition.height;
					
					seatButton = InitializeSeatButton(x, y, mainPanel);
					
					seatButton.setBooked(true);
					seatButton.setMyIndex(count);
					
					count += 1;
				}
			}
		}
		
		//Initialize seats for any reservation already booked
		Reservation[] reservations = flight.getReservations();
		
		if(reservations != null)
		{
			for (Reservation reservation : reservations) 
			{
				passengers = reservation.getPassengers();
				
				for (Passenger passenger : passengers) 
				{
					passengerSeatPosition = passenger.getSeat().getPosition();
					
					int x = passengerSeatPosition.width;
					int y = passengerSeatPosition.height;
					
					if(seatButtonArray[x][y] == null)
					{
						seatButton = InitializeSeatButton(x, y, mainPanel);
						
						seatButton.setBooked(true);
					}
				}
			}
		}
		
		for(int i=0; i<seatButtonArray.length; i++)
		{
			for(int j=0; j<seatButtonArray[i].length; j++)
			{
				if(seatButtonArray[i][j] == null)
				{
					InitializeSeatButton(i, j, mainPanel);
					
					/*
					seatButton = new SeatButton(flight.getSeat(i, j), currentReservation);
				
					seatButtonArray[i][j] = seatButton;
				
					size = new Dimension(squaredSize, squaredSize);
				
					seatButton.setBounds(leftPadding+j*(squaredSize+indent)+insets.left, topPadding+i*(squaredSize+indent)+insets.top, squaredSize, squaredSize);
				
					mainPanel.add(seatButton);
					*/
				}
			}
		}
		
		updateSeats();
		
		add(mainPanel);
		
	}
	
	SeatButton InitializeSeatButton(int x, int y, JPanel mainPanel)
	{
		SeatButton seatButton = new SeatButton(flight.getSeat(x, y), currentReservation);
		
		seatButtonArray[x][y] = seatButton;
	
		Dimension size = new Dimension(squaredSize, squaredSize);
	
		seatButton.setBounds(leftPadding+y*(squaredSize+indent)+insets.left, topPadding+x*(squaredSize+indent)+insets.top, squaredSize, squaredSize);
	
		mainPanel.add(seatButton);
		
		return seatButton;
	}
	
	public void updateSeats()
	{
		for(int i=0; i<seatButtonArray.length; i++)
		{
			for(int j=0; j<seatButtonArray[i].length; j++)
			{
				seatButtonArray[i][j].setButtonEnabled(editable);
				
				seatButtonArray[i][j].update();
				
				seatButtonArray[i][j].setBooked(false);
			}
		}
		
		Passenger[] passengers = currentReservation.getPassengers();
		Dimension passengerSeatPosition;
		
		if(passengers != null)
		{
			for (Passenger passenger : passengers) 
			{
				if(passenger != null && passenger.getSeat() != null)
				{
					passengerSeatPosition = passenger.getSeat().getPosition();
					
					int x = passengerSeatPosition.width;
					int y = passengerSeatPosition.height;
					
					seatButtonArray[x][y].setBooked(true);
				}
			}
		}
		
		//Initialize seats for any reservation already booked
		Reservation[] reservations = flight.getReservations();
		
		if(reservations != null)
		{
			for (Reservation reservation : reservations) 
			{
				passengers = reservation.getPassengers();
				
				for (Passenger passenger : passengers) 
				{
					passengerSeatPosition = passenger.getSeat().getPosition();
					
					int x = passengerSeatPosition.width;
					int y = passengerSeatPosition.height;
					
					seatButtonArray[x][y].setBooked(true);
				}
			}
		}
	}
	
	void setupProperties()
	{
		Seat[][] seatArray = plane.getSeatArray();
		
		scaleModifier = Math.sqrt((seatArray.length*seatArray[0].length)/30f);
		panelToButtonScale = panelSize.height/10;
		
		squaredSize = (int) (panelToButtonScale/scaleModifier);
		indent = (int)(10/scaleModifier);
		
		leftPadding = panelSize.width/2-((squaredSize+indent)*seatArray[0].length)/2;
		topPadding = panelSize.height/2-((squaredSize+indent)*seatArray.length)/2;		
		
		seatButtonArray = new SeatButton[seatArray.length][seatArray[0].length];
		
	}
	
	void setEditable()
	{
		editable = !editable;
		updateSeats();
	}
}
