package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.PrintGraphics;
import java.awt.Rectangle;
import java.awt.SecondaryLoop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RepaintManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import MainPackage.Airport.AirportType;
import MainPackage.Plane.PlaneType;

public class NewReservationMenu
{
	private JFrame frame;
	private JPanel mainPanel;
	
	private Flight[] displayedFlights;
	private JPanel[] flightPanels;
	
	private Dimension frameSize = new Dimension(1000, 600);
	
	private Reservation currentReservation;
	private Flight currentFlight;
	private Passenger[] currentPassengers;
	
	private JFormattedTextField seatAmountLabel;
	
	private PlanePanel planePanel;
	
	private ArrayList<Flight> flights;
	
	//Start parameters
	private int startSeatAmount = 0; 
	
	private JDateChooser startDateLabel;
	private JDateChooser endDateLabel;
	private DefaultListModel listModel;
	private JList viewedList;
	private JScrollPane scrollPane;
	private JComboBox departureList;
	private JComboBox destinationList;
	
	private JPanel bottomPanel;
	
	public NewReservationMenu(JFrame frame)
	{
		this.frame = frame;
		
		flights = Database.getInstance().Get( Flight.class );
		
		//Test
		displayedFlights = new Flight[flights.size()];
		
		/*displayedFlights[0] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.COPENHAGEN),  new Airport(AirportType.MALMÖ));
		displayedFlights[1] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.STOCKHOLM),  new Airport(AirportType.COPENHAGEN));
		displayedFlights[2] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.RØNNE),  new Airport(AirportType.COPENHAGEN));
		displayedFlights[3] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.COPENHAGEN),  new Airport(AirportType.RØNNE));
		displayedFlights[4] = new Flight(Calendar.getInstance(), new Plane(PlaneType.BOEING747), new Airport(AirportType.RØNNE),  new Airport(AirportType.STOCKHOLM));*/
		
		flightPanels = new JPanel[displayedFlights.length];
		
		/*for (int i=0; i<displayedFlights.length; i++) 
		{
			setupFlightPanel(i, flights.get( i ));
		}*/
		
		initializeReservation();
		
		setupFrame();
		
		makeContent();
		
		//Test
		currentPassengers = new Passenger[10];
		
		for(int i=0; i<currentPassengers.length; i++)
			currentPassengers[i] = new Passenger(null, null);
		
		calculateResults();
		updateReservation();
		
	}
	
	class ButtonListener implements ActionListener
	{
		int index;
		
		public ButtonListener(int i)
		{
			index = i;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "Make Reservation": 	
				if(canCommit())
				{
					frame.remove(mainPanel);
					new PassengerManagerMenu(frame, currentReservation);
					
					System.out.println(currentReservation.getFlight().getPlane().getPlaneTypeString());
					//new ReservationInfoMenu(frame, currentReservation);
				}
				break;
			case "Inspect Reservation": 	
				System.out.println("MEH!");
				break;		
			case "Flight Panel":
				bottomPanel.remove(planePanel);
				wipePassengers();
				
				currentReservation.setFlight(flights.get(index));
				planePanel = new PlanePanel(currentReservation.getFlight(), currentReservation, new Dimension(frameSize.width,(((frameSize.height/3*2)/4)*3)), true);
				
				bottomPanel.add(planePanel); 
				
				updateFlightPanel();
				
				mainPanel.invalidate();
				mainPanel.validate();
				mainPanel.repaint();
				break;
			}
		}
	}
	
	class TextListener implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt) 
		{
			//System.out.println(evt.getPropertyName());
			//if( evt.getPropertyName().equals( "value" ) )
			calculateResults();
			updateReservation();
		}
	}
	
	class TextActionListener implements ActionListener
	{
		@Override
        public void actionPerformed( ActionEvent e )
        {
			calculateResults();
			updateReservation();
        }
	}
	
	private void calculateResults()
	{
		//flightPanels = new JPanel[flights.size()];
		if( listModel == null )
			return;
		
		//listModel.clear();
		viewedList.removeAll();
		for( int i = 0; i < flights.size(); i++ )
		{
			Flight currentFlight = flights.get( i );
			boolean bSuccess = true;
			/*System.out.println( "Selected: " + departureList.getSelectedItem().toString());
			System.out.println( "Currentflight: " + currentFlight.getOrigin().getName());
			
			System.out.println( "Selected2: " + destinationList.getSelectedItem().toString());
			System.out.println( "Currentflight2: " + currentFlight.getDestination().getName());*/
			
			boolean bounce = true;
			
			//Check if the startDate time is smaller than the actual flight time.
			if(!(currentFlight.getDate().getTimeInMillis() >= startDateLabel.getCalendar().getTimeInMillis()))
			{
				bounce = false;
			}
			
			//Check if the endDate time is greater than the actual flight time.
			if(!(currentFlight.getDate().getTimeInMillis() <= endDateLabel.getCalendar().getTimeInMillis()))
			{
				bounce = false;
			}
			
			//Check if the departure selected is the same as the actual flight.
			if(departureList.getSelectedIndex() > 0 && !departureList.getSelectedItem().toString().equals( currentFlight.getOrigin().getName()))
			{
				bounce = false;
			}
			
			//Check if the destination selected is the same as the actual flight.
			if(destinationList.getSelectedIndex() > 0 && !destinationList.getSelectedItem().toString().equals( currentFlight.getDestination().getName()))
			{
				bounce = false;
			}
			
			
			//Martins lange satan
			//currentFlight.getDate().getTimeInMillis() >= startDateLabel.getCalendar().getTimeInMillis() && currentFlight.getDate().getTimeInMillis() <= endDateLabel.getCalendar().getTimeInMillis() && departureList.getSelectedItem().toString().equals( currentFlight.getOrigin().getName() ) && destinationList.getSelectedItem().toString().equals( currentFlight.getDestination().getName() ) 
			
			if(bounce)
			{
				JPanel flightPanel = setupFlightPanel( currentFlight );
				
				JButton flightPanelButton = new JButton();
				flightPanelButton.setActionCommand("Flight Panel");
				
				flightPanelButton.add(flightPanel);
				
				flightPanelButton.addActionListener(new ButtonListener(i));
				
				viewedList.add(flightPanelButton);
				
//				if( flightPanel != null )
//				{
//					JButton test = new JButton();
//					test.add(flightPanel);
//					
//					viewedList.add(test);
//					//listModel.addElement( test );
//				}
				
				//viewedList.repaint();
				//scrollPane.repaint();
				
			}
		}
		
//		for( Component component : viewedList.getComponents() )
//			System.out.println("wat");
		//System.out.println("VALIDATED");
//		if( viewedList.getComponentCount() > 0 )
//		{
//			mainPanel.validate();
//			viewedList.validate();
//		}
//		else 
//		{
//			mainPanel.invalidate();
//			viewedList.invalidate();
//		}
		
		mainPanel.invalidate();
		mainPanel.validate();
		mainPanel.repaint();
		
	}
	
	private void setupFrame()
	{
		frame.setSize(frameSize.width, frameSize.height);
		frame.setResizable(false);
		frame.setTitle("New Reservation Menu");
		frame.setLocationRelativeTo(null);
	}
	
	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		mainPanel = new JPanel();
		
		//Top
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(frameSize.width, frameSize.height/3));
		
		//Top Left Panel
		JPanel topLeftPanel = new JPanel();
		
		topLeftPanel.setLayout(new BorderLayout());
		//topPanel.setBorder(BorderFactory.createEtchedBorder(1));
		topLeftPanel.setPreferredSize(new Dimension(frameSize.width/2, frameSize.height/3));
		
			//Top Title
		JPanel topTitlePanel = new JPanel();
		//topTitlePanel.setLayout(new BoxLayout(topTitlePanel, BoxLayout.Y_AXIS));
		topTitlePanel.setLayout(null);
		topTitlePanel.setPreferredSize(new Dimension(frameSize.width/4, frameSize.height/3));
		
		Insets insets = topTitlePanel.getInsets();
		
		JLabel startDateTitleLabel = new JLabel("Start Date");
		//startDateTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		startDateTitleLabel.setBounds(insets.left+20, insets.top, 100, 25);
		
		JLabel endDateTitleLabel = new JLabel("End Date");
		//endDateTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		endDateTitleLabel.setBounds(insets.left+20, insets.top+((frameSize.height/6)/4), 100, 25);
		
		JLabel destinationTitleLabel = new JLabel("Origin / Destination");
		//destinationTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		destinationTitleLabel.setBounds(insets.left+20, insets.top+((frameSize.height/6)/4)*2, 100, 25);
		
		JLabel seatAmountTitleLabel = new JLabel("Seat Amount");
		//seatAmountTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		seatAmountTitleLabel.setBounds(insets.left+20, insets.top+((frameSize.height/6)/4)*3, 100, 25);
		
		topTitlePanel.add(startDateTitleLabel);
		topTitlePanel.add(endDateTitleLabel);
		topTitlePanel.add(destinationTitleLabel);
		topTitlePanel.add(seatAmountTitleLabel);
		
			//Top Parameter
		JPanel topParameterPanel = new JPanel();
		topParameterPanel.setLayout(new BoxLayout(topParameterPanel, BoxLayout.Y_AXIS));
		topParameterPanel.setPreferredSize(new Dimension(frameSize.width/4, frameSize.height/2));
		
		/*
		JTextField startDateLabel = new JTextField();
		startDateLabel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		
		*/
		
		TextListener textListener = new TextListener();
		
		Calendar calendarToUse = Calendar.getInstance();
		
		startDateLabel = new JDateChooser();
		startDateLabel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		calendarToUse.set(2012, 0, 1);
		startDateLabel.setDate(calendarToUse.getTime());
		startDateLabel.addPropertyChangeListener( textListener );
		
		endDateLabel = new JDateChooser();
		endDateLabel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		calendarToUse.set(2012, 11, 31);
		endDateLabel.setDate(calendarToUse.getTime());
		endDateLabel.addPropertyChangeListener( textListener );
		
		JPanel destinationPanel = new JPanel();
		destinationPanel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		destinationPanel.setLayout(new BoxLayout(destinationPanel, BoxLayout.X_AXIS));
		
		String[] availableAirports = new String[Airport.getDestinations().length+1];
		
		availableAirports[0] = "- Unknown -";
		
		for(int i=1; i<Airport.getDestinations().length+1; i++)
		{
			availableAirports[i] = Airport.getDestinations()[i-1];
		}
			
		departureList = new JComboBox(availableAirports);
		//departureList.addPropertyChangeListener( textListener );
		departureList.addActionListener( new TextActionListener() );
		destinationList = new JComboBox(availableAirports);
		//destinationList.addPropertyChangeListener( textListener );
		destinationList.addActionListener( new TextActionListener() );
		
		destinationPanel.add(departureList);
		destinationPanel.add(destinationList);
		
		//SKAL KUN VÆRE TAL
		seatAmountLabel = new JFormattedTextField();
		
		try 
		{
			seatAmountLabel = new JFormattedTextField(new MaskFormatter("#"));
		} 
		catch (ParseException e) {
			System.out.println("Parsing error");
		}
		
		seatAmountLabel.addPropertyChangeListener(textListener);
		seatAmountLabel.setText(""+startSeatAmount);
		
		seatAmountLabel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		
		topParameterPanel.add(startDateLabel);
		topParameterPanel.add(endDateLabel);
		topParameterPanel.add(destinationPanel);
		topParameterPanel.add(seatAmountLabel);
		
		topLeftPanel.add(topTitlePanel,BorderLayout.WEST);
		topLeftPanel.add(topParameterPanel,BorderLayout.CENTER);
		
		//Top Right
		JPanel topRightPanel = new JPanel();
		//topRightPanel.setPreferredSize(new Dimension(frameSize.width/2, frameSize.height/3));
		
		listModel = new DefaultListModel<>();
		viewedList = new JList<>();
		viewedList.setLayout(new BoxLayout(viewedList, BoxLayout.Y_AXIS));
		viewedList.setPreferredSize(new Dimension(200,1000));
		
		for (JPanel flightPanel : flightPanels) 
		{
			if( flightPanel != null )
			{
				JButton test = new JButton();
				test.add(flightPanel);
				
				viewedList.add(test);
			}
		}
		
		scrollPane = new JScrollPane(viewedList);
		scrollPane.setPreferredSize(new Dimension(frameSize.width/2, frameSize.height/3));
		topRightPanel.add(scrollPane);
		
		topPanel.add(topLeftPanel, BorderLayout.WEST);
		topPanel.add(topRightPanel, BorderLayout.CENTER);
		
		//Bottom
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(frameSize.width, frameSize.height/3*2));
		
			//Plane Panel
		planePanel = new PlanePanel(currentReservation.getFlight(), currentReservation, new Dimension(frameSize.width,(((frameSize.height/3*2)/4)*3)), true);
		
			//Button Panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setPreferredSize(new Dimension(frameSize.width, (frameSize.height/3*2/4)));
		
		JButton makeReservationButton = new JButton("Make Reservation");
		JButton inspectReservationButton = new JButton("Inspect Reservation");
		JButton closeButton = new JButton("Close");		
		
		buttonPanel.add(makeReservationButton);
		buttonPanel.add(inspectReservationButton);
		
		//bottomPanel.add(planePanel, BorderLayout.CENTER);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		//Finish up
		mainPanel.add(topPanel, BorderLayout.NORTH);
		//mainPanel.add(middlePanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		contentPane.add(mainPanel);
		
		//Listeners
		ButtonListener listener = new ButtonListener(0);
		
		makeReservationButton.addActionListener(listener);
		inspectReservationButton.addActionListener(listener);
		
		update();
		updateReservation();
	}
	
	JPanel setupFlightPanel(Flight currentFlight)
	{
		//Flight Panel
		JPanel newFlightPanel = new JPanel();
		
		newFlightPanel.setLayout(new BoxLayout(newFlightPanel, BoxLayout.X_AXIS));
		newFlightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		newFlightPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		newFlightPanel.setBackground(Color.WHITE);
		
			//First Panel
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.Y_AXIS));
		firstPanel.setBackground(Color.WHITE);
		
				//Departure Label
		String stringToUse = "Departure: "+currentFlight.getOrigin().getName();
		
		JLabel departureLabel = new JLabel(stringToUse);
				
				//Destination Label
		stringToUse = "Destination: "+currentFlight.getDestination().getName();
		
		JLabel destinationLabel = new JLabel(stringToUse);
		
			//First Panel Finish Up
		firstPanel.add(departureLabel);
		firstPanel.add(destinationLabel);
			//First Panel Finished
		
			//Second Panel
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.Y_AXIS));
		secondPanel.setBackground(Color.WHITE);
		
				//Date Label
		stringToUse = ""+currentFlight.getDate().get(Calendar.DATE)+
				"/"+(currentFlight.getDate().get(Calendar.MONTH)+1)+
				" - "+currentFlight.getDate().get(Calendar.YEAR);
		
		JLabel dateLabel = new JLabel(stringToUse);
		
				//Time Label
		String minute = "";
		
		if(currentFlight.getDate().get(Calendar.MINUTE) < 10)
		{
			minute = "0"+currentFlight.getDate().get(Calendar.MINUTE);
		}
		else 
		{
			minute = ""+currentFlight.getDate().get(Calendar.MINUTE);
		}
		
		stringToUse = ""+currentFlight.getDate().get(Calendar.HOUR_OF_DAY)+":"+minute;
		
		JLabel timeLabel = new JLabel(stringToUse);
		
			//Second Panel Finish Up
		secondPanel.add(dateLabel);
		secondPanel.add(timeLabel);
			//Second Panel Finished
		
			//Third Panel
		JPanel thirdPanel = new JPanel();
		thirdPanel.setLayout(new BoxLayout(thirdPanel, BoxLayout.Y_AXIS));
		thirdPanel.setBackground(Color.WHITE);
		
				//Seat Label
		stringToUse = "Available Seats: "+currentFlight.getSeatsLeft();
		
		JLabel seatLabel = new JLabel(stringToUse);
		
				//Price Label
		stringToUse = "Price Per Seat: "+"100$";
		
		JLabel priceLabel = new JLabel(stringToUse);
		
			//Third Panel Finish Up
		thirdPanel.add(seatLabel);
		thirdPanel.add(priceLabel);
			//Third Panel Finished
		
		//Flight Panel Finish Up
		newFlightPanel.add(firstPanel);
		newFlightPanel.add(Box.createHorizontalGlue());
		newFlightPanel.add(secondPanel);
		newFlightPanel.add(Box.createHorizontalGlue());
		newFlightPanel.add(thirdPanel);
		//Flight Panel Finished
		
		return newFlightPanel;
	}
	
	private void update()
	{
		frame.setVisible(true);
	}
	
	private void initializeReservation()
	{
		currentReservation = new Reservation(null,  
						null, 
						null, 
						new Passenger[startSeatAmount],
						0);
		
		currentFlight = new Flight(Calendar.getInstance(),  new Plane(PlaneType.BOEING737), new Airport(AirportType.COPENHAGEN), new Airport(AirportType.STOCKHOLM), 0);
		
		Passenger[] passengerArray = currentReservation.getPassengers();
		
		for(int i=0; i<passengerArray.length; i++)
		{
			passengerArray[i] = new Passenger(null, null);
		}
		
		currentReservation.setPassengers(passengerArray);
		currentReservation.setFlight(currentFlight);
		
	}
	
	private void updateReservation()
	{
		//currentReservation.setFlight(currentFlight);
		
		updatePassengers();
	}
	
	private void updatePassengers()
	{
		int amount = 0;
		
		if(seatAmountLabel.getValue() != null)
		{
			amount = Integer.parseInt((String)seatAmountLabel.getValue());
		}
		/*
		if( amount == 0 )
			return;
		*/
		Passenger[] passengerArray = new Passenger[amount];
		
		Passenger[] currentPassengerArray = currentReservation.getPassengers();
		
		for(int i=0; i<passengerArray.length; i++)
		{
			if(i<currentPassengerArray.length)
			{
				int index = i;
				
				while(currentPassengerArray[index] == null && index < currentPassengerArray.length)
				{
					index += 1;
				}
				
				if(currentPassengerArray[index] != null)
				{
					passengerArray[i] = currentPassengerArray[index];
				}
				else 
				{
					passengerArray[i] = new Passenger(null, null);
				}
			}
			else 
			{
				passengerArray[i] = new Passenger(null, null);
			}
			
			/*
			if(i < currentPassengerArray.length)
			{
				
				int index = i;
				
				while(currentPassengerArray[index] == null && i < currentPassengerArray.length)
				{
					index += 1;
				}
				
				
				if(currentReservation.getPassengers()[i] != null)
				{
					passengerArray[i] = currentReservation.getPassengers()[i];
				}
				else
				{
					passengerArray[i] = new Passenger(null, null);
				}
			}
			else 
			{
				passengerArray[i] = new Passenger(null, null);
			}
			*/
		}
		
		//Skal opdateres og fixes
		if(currentReservation.getPassengers().length > passengerArray.length)
		{
			int start = passengerArray.length;
			
			for(int i=start; i < currentReservation.getPassengers().length; i++)
			{	
				if(currentReservation.getPassengers()[i].getSeat() != null)
					currentReservation.getPassengers()[i].getSeat().changeBookingStatus(false);
			}
		}
		
		currentReservation.setPassengers(passengerArray);
		
		updateFlightPanel();
	}
	
	void wipePassengers()
	{
		Passenger[] passengers = currentReservation.getPassengers();
		
		for (Passenger passenger : passengers) 
		{
			passenger.setSeat(null);
		}
	}
	
	void updateFlightPanel()
	{
		if(planePanel != null)
			planePanel.updateSeats();
	}
	
	boolean canCommit()
	{
		boolean returnBool = true;
		
		
		Passenger[] passengers = currentReservation.getPassengers();
		
		if(passengers.length > 0)
		{
			for (Passenger passenger : passengers) 
			{
				if(passenger.getSeat() == null)
					returnBool = false;
			}
		}
		else 
		{
			returnBool = false;
		}
			
		return returnBool;
	}
}
