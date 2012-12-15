package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import MainPackage.NewReservationMenu.ButtonListener;
import MainPackage.Plane.PlaneType;

public class ReservationInfoMenu 
{
	private JFrame frame;
	private JPanel mainPanel;
	
	private Reservation currentReservation;

	private PlanePanel planePanel;
	
	private boolean isNew;
	
	public ReservationInfoMenu(JFrame frame, Reservation reservation, boolean isNew)
	{
		this.frame = frame;
		this.isNew = isNew;
		currentReservation = reservation;
		//currentReservation.setFlight(Database.getInstance().Get(currentReservation.getFlight().getID(), Flight.class));
		
		setupFrame();
		
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "Edit Reservation":
				System.out.println("Edit Reservation");
				if(canCommit())
				{
					frame.remove( mainPanel );
			        
					//currentReservation.getFlight().removeReservation(currentReservation.getCurrentFlightReservationIndex());
					
			        new NewReservationMenu( frame, currentReservation, isNew);
					
					//planePanel.setEditable();
					//planePanel.updateSeats();
					
					//mainPanel.revalidate();
					//mainPanel.repaint();
				}
				break;
				
			case "OK":
				System.out.println("OK - Commit changes");
				frame.dispose();
				
				
				//Flight flightInstance = Database.getInstance().Get(currentReservation.getFlight().getID(), Flight.class);
				
				Flight flightInstance = currentReservation.getFlight();
				
				if(isNew)
				{
					//Add Reservation to flight
					currentReservation.setCurrentFlightReservationIndex(flightInstance.addReservation(currentReservation));
					
					//Set the current date
					Calendar newCalendar = Calendar.getInstance();
					currentReservation.setReservedDate(newCalendar);
					
					//Set the id of the database instance
					currentReservation.setID(Database.getInstance().GetID(Reservation.class));
					
					Reservation[] newReservations = currentReservation.getPassengers()[0].getPerson().getReservations();
					
					if( newReservations == null )
						newReservations = new Reservation[1];
						else {
							Arrays.copyOf( newReservations, currentReservation.getPassengers()[0].getPerson().getReservations().length + 1 );
						}
					
					
					newReservations[ newReservations.length - 1 ] = currentReservation;
					
					currentReservation.getPassengers()[0].getPerson().setReservations( newReservations );
					
					Database.getInstance().Add(currentReservation);
					Database.getInstance().Replace(currentReservation.getPassengers()[0].getPerson().getID(), currentReservation.getPassengers()[0].getPerson());
				}
				else 
				{
					Database.getInstance().Replace(currentReservation.getID(), currentReservation);
				}
				
				Database.getInstance().Replace(flightInstance.getID(), flightInstance);
				
				//currentReservation.getFlight().addReservation(currentReservation);
				
				//Commit changes
				break;
				
			case "Cancel":
				System.out.println("Cancel");
				frame.dispose();
				break;
			case "Delete":
				System.out.println("Delete");
				deleteReservation();
				break;
			}
			
		}
	}
	
	void setupFrame()
	{
		frame.setSize(450, 600);
		frame.setResizable(false);
		frame.setTitle("Reservation Info");
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	void makeContent()
	{
		Container contentPane = frame.getContentPane();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
			//Info Panel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
				//Customer Panel
		JPanel customerPanel = new JPanel();
		customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.X_AXIS));
		
					//Customer Title Label
		JLabel customerTitleLabel = new JLabel("Customer:  ");
		
					//Customer Label
		String personString = "";
		
		if(currentReservation.getOwner() != null)
			personString = currentReservation.getOwner().getFirstName()+" "+currentReservation.getOwner().getSurName()+", "+currentReservation.getOwner().getPhone();
		
		JLabel customerLabel = new JLabel(personString);
		
				//Customer Panel Finish Up
		customerPanel.add(customerTitleLabel);
		customerPanel.add(customerLabel);
				//Customer Panel Finished
		
		
				//Flight Panel
		JPanel flightPanel = new JPanel();
		flightPanel.setLayout(new BoxLayout(flightPanel, BoxLayout.X_AXIS));
		
					//Flight Title Label
		JLabel flightTitleLabel = new JLabel("Flight:  ");
		
		String flightString = "";
		
		if(currentReservation.getFlight() != null)
			flightString = currentReservation.getFlight().getOrigin().getName()+" - "
					+ currentReservation.getFlight().getDestination().getName();
		
					//Flight Label
		JLabel flightLabel = new JLabel(flightString);
				
				//Flight Panel Finishup
		flightPanel.add(flightTitleLabel);
		flightPanel.add(flightLabel);
				//Flight Panel Finished
		
				//Time Panel
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
		
					//Time Title Label
		JLabel timeTitleLabel = new JLabel("Time:  ");
		
		String timeString = "";
		
		if(currentReservation.getFlight() != null)
			timeString = ""+currentReservation.getFlight().getDate().getTime();
		
					//Time Label
		JLabel timeLabel = new JLabel(timeString);
				
				//Time Panel Finishup
		timePanel.add(timeTitleLabel);
		timePanel.add(timeLabel);
				//Time Panel Finished
	
				//Passenger Panel
		JPanel passengerPanel = new JPanel();
		
					//Passenger Label
		JLabel passengerLabel = new JLabel("Passengers");
		
					//Passenger Table
		String[] columns = {"Passengers", "Seats"};
		
		Object[][] passengerData = makePassengerData();
		
		JTable passengerTable = new JTable(passengerData, columns);
		passengerTable.setEnabled(false);
		passengerTable.setBackground(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(passengerTable);
		scrollPane.setPreferredSize(new Dimension(450, 200));
		
				//Passenger Panel Finished
		passengerPanel.add(passengerLabel);
		passengerPanel.add(scrollPane);
				//Passenger Panel Finished
		
		
			//Info Panel finishup
		infoPanel.add(customerPanel);
		infoPanel.add(flightPanel);
		infoPanel.add(timePanel);
		infoPanel.add(passengerPanel);
			//Info Panel Finished
		
		//Top Panel Finishup
		topPanel.add(infoPanel);
		topPanel.add(passengerPanel);
		//Top Panel Finished
		
		
		//Center Panel
		JPanel centerPanel = new JPanel();
		
			//Flight Panel
		//Flight testFlight = new Flight(new Date(), new Plane(PlaneType.BOEING747));
		
		planePanel = new PlanePanel(currentReservation.getFlight(), currentReservation, new Dimension(400,200),false);
		
		
		//Center Panel Finishup
		centerPanel.add(planePanel);
		//Center Panel Finish
		
		
		//Bottom Panel
		JPanel bottomPanel = new JPanel();
		
		// MANGLER SPACING OG ALLIGNMENT
		
			//Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		ButtonListener listener = new ButtonListener();
				//Edit Reservation Button
		JButton editReservationButton = new JButton("Edit Reservation");
		editReservationButton.setActionCommand("Edit Reservation");
		editReservationButton.addActionListener(listener);
		
				//Ok Button
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		
				//Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(listener);
		
			//Button Panel Finishup
		buttonPanel.add(editReservationButton);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		if(!isNew)
		{
				//Delete Button
			JButton deleteButton = new JButton("Delete Reservation");
			deleteButton.setActionCommand("Delete");
			deleteButton.addActionListener(listener);
	
			buttonPanel.add(deleteButton);
		}
			//Button Panel Finished
		
		//Bottom Panel Finishup
		bottomPanel.add(buttonPanel);
		//Bottom Panel FInished
		
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		contentPane.add(mainPanel);
		
	}
	
	Object[][] makePassengerData()
	{
		Object[][] returnArray;
		
		returnArray = new Object[currentReservation.getPassengers().length][];
		
		Passenger[] passengers = currentReservation.getPassengers();
		
		if(passengers != null)
		{
			for(int i=0; i<passengers.length; i++)
			{
				String firstData = passengers[i].getPerson().getFirstName()+" "+passengers[i].getPerson().getSurName();
				
				String secondData = "("+passengers[i].getSeat().getPosition().width+","+passengers[i].getSeat().getPosition().height+")";
				
				returnArray[i] = new Object[]{firstData, secondData};
			}
		}
		
		return returnArray;
	}
	
	boolean canCommit()
	{
		boolean returnBool = true;
		
		Passenger[] passengers = currentReservation.getPassengers();
		
		for (Passenger passenger : passengers) 
		{
			if(passenger.getSeat() == null)
				returnBool = false;
		}
		
		return returnBool;
	}
	
	void deleteReservation()
	{
		currentReservation.getFlight().removeReservationAt(currentReservation.getCurrentFlightReservationIndex());
		
		System.out.println(currentReservation.getFlight().getReservations().length);
		
		Database.getInstance().Replace(currentReservation.getFlight().getID(), currentReservation.getFlight());
		
		frame.dispose();
	}
}
