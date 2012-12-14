package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

public class FlightInfoMenu extends JFrame
{
	private static final String X_AXIS = null;

	JPanel mainPanel;
	
	Flight currentFlight;
	Reservation currentReservation;
	
	public FlightInfoMenu(Flight flight)
	{
		currentFlight = flight;
		
		//Test
		currentFlight.setReservations(new Reservation[0]);
		//currentReservation = new Reservation(null, flight, null, null, 0);
		
		setupFrame();
		
		makeContent();
	}
	
	void setupFrame()
	{
		setSize(400, 600);
		setResizable(false);
		setTitle("Flight Info Window");
		setLocationRelativeTo(null);
	
	}
	
	void makeContent()
	{
		Container contentPane = this.getContentPane();
		
		//Main Panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
			//Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
				//ReservationLabel
		JLabel reservationLabel = new JLabel("Reservations overview");
		
				//Reservation Table
		String[] columns = {"Name", "Seats", "Date of Reservation"};
		
		Object[][] reservationData = makeReservationData();
		
		JTable reservationTable = new JTable(reservationData, columns);
		reservationLabel.setPreferredSize(new Dimension(400, 200));
		reservationLabel.setBackground(Color.WHITE);
		
			//Top Panel Finish Up
		topPanel.add(reservationLabel);
		topPanel.add(reservationTable);
			//Top Panel Finished
		
			//Center Panel
		JPanel centerPanel = new JPanel();
		
				//Flight Panel
		PlanePanel flightPanel = new PlanePanel(currentFlight, currentReservation, new Dimension(400,200), false);
		
			//Center Panel Finish Up
		centerPanel.add(flightPanel);
			//Center Panel Finished
		
			//Bottom Panel
		JPanel bottomPanel = new JPanel();
		
				//Column Panel
		JPanel columnPanel = new JPanel();
		columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.X_AXIS));
	
					//Left Column Panel
		JPanel leftColumnPanel = new JPanel();
		leftColumnPanel.setLayout(new BoxLayout(leftColumnPanel, BoxLayout.Y_AXIS));
		
						//Plane Label
		JLabel planeLabel = new JLabel("Plane: "+currentFlight.getPlane().getPlaneTypeString());
		
						//Capacity Label
		JLabel capacityLabel = new JLabel("Capacity: "+currentFlight.getSeatAmount()+" Seats");
		
					//Left Column Panel Finish Up
		leftColumnPanel.add(planeLabel);
		leftColumnPanel.add(capacityLabel);
					//Left Column Panel Finished
		
					//Right Column Panel
		JPanel rightColumnPanel = new JPanel();
		rightColumnPanel.setLayout(new BoxLayout(rightColumnPanel, BoxLayout.Y_AXIS));
		
						//Departure Label
		JLabel departureLabel = new JLabel("Departure: "+currentFlight.getPlane().getPlaneTypeString());
		
						//Destination Label
		JLabel destinationLabel = new JLabel("Destination: "+currentFlight.getSeatAmount());

						//Departure Time Label
		String minute = "";
		
		if(currentFlight.getDate().get(Calendar.MINUTE) < 10)
		{
			minute = "0"+currentFlight.getDate().get(Calendar.MINUTE);
		}
		else 
		{
			minute = ""+currentFlight.getDate().get(Calendar.MINUTE);
		}
		
		String stringToUse = ""+currentFlight.getDate().get(Calendar.HOUR_OF_DAY)+":"+minute;
		
		JLabel departureTimeLabel = new JLabel("Departure Time: "+stringToUse);
		
					//Right Column Panel Finish Up
		rightColumnPanel.add(departureLabel);
		rightColumnPanel.add(destinationLabel);
		rightColumnPanel.add(departureTimeLabel);
					//Right Column Panel Finished
		
				//Column Panel Finish Up
		columnPanel.add(leftColumnPanel);
		columnPanel.add(rightColumnPanel);
				//Column Panel Finished
		
			//Bottom Panel Finish Up
		bottomPanel.add(columnPanel);
			//Bottom Panel Finished
		
		//Main Panel Finish Up
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		//Main Panel Finished
		
		contentPane.add(mainPanel);
		
		update();
	}
	
	private void update()
	{
		this.setVisible(true);
	}
	
	Object[][] makeReservationData()
	{
		Object[][] returnArray;
		
		returnArray = new Object[currentFlight.getReservations().length][];
		
		Reservation[] reservations = currentFlight.getReservations();
		
		if(reservations != null)
		{
			for(int i=0; i<reservations.length; i++)
			{
				String firstData = reservations[i].getOwner().getFirstName()+" "+reservations[i].getOwner().getSurName();
				String secondData = ""+reservations[i].getPassengers().length;
				String thirdData = ""+reservations[i].getReservationDate();
				
				returnArray[i] = new Object[]{firstData, secondData};
			}
		}
		
		return returnArray;
	}
}
