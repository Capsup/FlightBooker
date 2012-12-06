package MainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import javax.swing.*;

public class PlanePanel extends JPanel 
{
	private Flight flight;
	private Plane plane;
	private Dimension panelSize;
	private Reservation currentReservation;
	private SeatButton[][] seatButtonArray;
	
	public PlanePanel(Flight flight, Reservation reservation, Dimension size)
	{
		this.flight = flight;
		plane = flight.getPlane();
		this.panelSize = size;
		currentReservation = reservation;
		
		makeContent();
	}
	
	void makeContent()
	{
		JPanel mainPanel;
		
		try
		{
			String path = "/images/"+plane.getPlaneTypeString()+".jpg";
			
			Rectangle rect = new Rectangle(-100, -125, 800, 600);
			
			mainPanel = new JPanelWithBackground(path, rect);
			
			mainPanel.setPreferredSize(panelSize);
		}
		catch(IOException e)
		{
			mainPanel = new JPanel();
		}
		
		mainPanel.setLayout(null);
		
		SeatButton seatButton;
		
		Insets insets = getInsets();
		Dimension size;
		
		Seat[][] seatArray = plane.getSeatArray();
		
		double scaleModifier = Math.sqrt((seatArray.length*seatArray[0].length)/30f);
		int panelToButtonScale = panelSize.height/10;
		
		int squaredSize = (int) (panelToButtonScale/scaleModifier);
		int indent = (int)(10/scaleModifier);
		
		seatButtonArray = new SeatButton[seatArray.length][];
		
		for(int i=0; i<seatArray.length; i++)
		{
			seatButtonArray[i] = new SeatButton[seatArray[i].length];
			
			for(int j=0; j<seatArray[i].length; j++)
			{
				seatButton = new SeatButton(flight.getSeat(i, j), currentReservation);
				
				seatButtonArray[i][j] = seatButton;
				
				size = new Dimension(squaredSize, squaredSize);
				
				seatButton.setBounds(35+j*(squaredSize+indent)+insets.left, 110+i*(squaredSize+indent)+insets.top, squaredSize, squaredSize);
				
				mainPanel.add(seatButton);
			}
		}
		
		updateSeats();
		
		add(mainPanel, BorderLayout.CENTER);
		
	}
	
	public void updateSeats()
	{
		for(int i=0; i<seatButtonArray.length; i++)
		{
			for(int j=0; j<seatButtonArray[i].length; j++)
			{
				seatButtonArray[i][j].update();
				
			}
		}
	}
}
