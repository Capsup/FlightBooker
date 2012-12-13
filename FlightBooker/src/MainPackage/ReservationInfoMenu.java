package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import MainPackage.NewReservationMenu.ButtonListener;
import MainPackage.Plane.PlaneType;

public class ReservationInfoMenu 
{
	private JFrame frame;
	private JPanel mainPanel;
	
	private Reservation currentReservation;

	private PlanePanel planePanel;
	
	public ReservationInfoMenu(JFrame frame, Reservation reservation)
	{
		this.frame = frame;
		currentReservation = reservation;
		
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
				if(canCommit())
				{
					planePanel.setEditable();
					planePanel.updateSeats();
				}
				break;
			case "Inspect Reservation": 	
				System.out.println("MEH!");
				break;			
			}
		}
	}
	
	void setupFrame()
	{
		frame.setSize(450, 600);
		frame.setResizable(false);
		
		frame.setLocationRelativeTo(null);
		
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
			personString = currentReservation.getOwner().getFirstName()+currentReservation.getOwner().getSurName()+", "+currentReservation.getOwner().getPhone();
		
		JLabel customerLabel = new JLabel(personString);
		
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
		
			//Info Panel finishup
		infoPanel.add(customerPanel);
		infoPanel.add(flightPanel);
			//Info Panel Finished
		
			//Passenger Panel
		JPanel passengerPanel = new JPanel();
		
				//Passenger Scrollview
		JList viewedList = new JList();
		
		JScrollPane scrollPane = new JScrollPane(viewedList);
		
				//NEEDS SCROLLPANE THINGS
		
			//Passenger Panel Finished
		passengerPanel.add(scrollPane);
			//Passenger Panel Finished
		
		
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
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
				//Edit Reservation Button
		JButton editReservationButton = new JButton("Edit Reservation");
		editReservationButton.setActionCommand("Edit Reservation");
		
				//Email Reservation Button
		JButton eMailReservationButton = new JButton("E-Mail Reservation");
		
				//Ok Button
		JButton okButton = new JButton("OK");
		
				//Cancel Button
		JButton cancelButton = new JButton("Cancel");
		
			//Button Panel Finishup
		buttonPanel.add(editReservationButton);
		buttonPanel.add(eMailReservationButton);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
			//Button Panel Finished
		
		//Bottom Panel Finishup
		bottomPanel.add(buttonPanel);
		//Bottom Panel FInished
		
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		contentPane.add(mainPanel);
		
		//Listeners
		ButtonListener listener = new ButtonListener();
		
		editReservationButton.addActionListener(listener);
		
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
}