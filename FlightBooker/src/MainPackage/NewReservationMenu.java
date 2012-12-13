package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.PrintGraphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import MainPackage.Airport.AirportType;
import MainPackage.Plane.PlaneType;

public class NewReservationMenu
{
	private JFrame frame;
	private JPanel mainPanel;
	
	private Dimension frameSize = new Dimension(1000, 600);
	
	private Reservation currentReservation;
	private Flight currentFlight;
	private Passenger[] currentPassengers;
	
	private JFormattedTextField seatAmountLabel;
	
	private PlanePanel planePanel;
	
	//Start parameters
	private int startSeatAmount = 2;
	
	public NewReservationMenu(JFrame frame)
	{
		this.frame = frame;
		
		initializeReservation();
		
		setupFrame();
		
		makeContent();
		
		//Test
		currentPassengers = new Passenger[10];
		
		for(int i=0; i<currentPassengers.length; i++)
			currentPassengers[i] = new Passenger(null, null);
				
		updateReservation();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "Make Reservation": 	
				frame.remove(mainPanel);
				new PassengerManagerMenu(frame, currentReservation);
				//new ReservationInfoMenu(frame, currentReservation);
				break;
			case "Inspect Reservation": 	
				System.out.println("MEH!");
				break;			
			}
		}
	}
	
	class TextListener implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt) 
		{
			updateReservation();
			
			updateFlightPanel();
		}
	}
	
	private void setupFrame()
	{
		frame.setSize(frameSize.width, frameSize.height);
		frame.setResizable(false);
		frame.setTitle("Passenger Manager");
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
		JDateChooser startDateLabel = new JDateChooser();
		startDateLabel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		
		JDateChooser endDateLabel = new JDateChooser();
		endDateLabel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		
		JPanel destinationPanel = new JPanel();
		destinationPanel.setPreferredSize(new Dimension(frameSize.width/8,((frameSize.height/6)/4)));
		destinationPanel.setLayout(new BoxLayout(destinationPanel, BoxLayout.X_AXIS));
		
		String[] availableAirports = Airport.getDestinations();
		
		JComboBox departureList = new JComboBox(availableAirports);
		JComboBox destinationList = new JComboBox(availableAirports);
		
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
		
		seatAmountLabel.addPropertyChangeListener(new TextListener());
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
		//middlePanel.setBorder(BorderFactory.createEtchedBorder(1));
		topRightPanel.setPreferredSize(new Dimension(frameSize.width/2, frameSize.height/3));
		
		JList viewedList = new JList();
		
		viewedList.setPreferredSize(new Dimension(frameSize.width-50,1000));
		
		JScrollPane scrollPane = new JScrollPane(viewedList);
		scrollPane.setPreferredSize(new Dimension(frameSize.width/2, frameSize.height/3));
		topRightPanel.add(scrollPane);
		
		topPanel.add(topLeftPanel, BorderLayout.WEST);
		topPanel.add(topRightPanel, BorderLayout.CENTER);
		
		//Bottom
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		//bottomPanel.setBorder(BorderFactory.createEtchedBorder(1));
		bottomPanel.setPreferredSize(new Dimension(frameSize.width, frameSize.height/3*2));
		
			//Plane Panel
		//Test
		//testFlight = new Flight(new Date(), new Plane(PlaneType.BOEING747), new Airport(AirportType.COPENHAGEN), new Airport(AirportType.STOCKHOLM));
		
		System.out.println(currentReservation.getFlight());
		
		planePanel = new PlanePanel(currentReservation.getFlight(), currentReservation, new Dimension(frameSize.width,(((frameSize.height/3*2)/4)*3)), true);
		
		//Final
		//PlanePanel planePanel = new PlanePanel(currentFlight, new Dimension(frameSize.width, frameSize.height/2));
		
			//Button Panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setPreferredSize(new Dimension(frameSize.width, (frameSize.height/3*2/4)));
		
		JButton makeReservationButton = new JButton("Make Reservation");
		JButton inspectReservationButton = new JButton("Inspect Reservation");
		JButton closeButton = new JButton("Close");		
		
		buttonPanel.add(makeReservationButton);
		buttonPanel.add(inspectReservationButton);
		
		bottomPanel.add(planePanel, BorderLayout.CENTER);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		//Finish up
		mainPanel.add(topPanel, BorderLayout.NORTH);
		//mainPanel.add(middlePanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		contentPane.add(mainPanel);
		
		//Listeners
		ButtonListener listener = new ButtonListener();
		
		makeReservationButton.addActionListener(listener);
		inspectReservationButton.addActionListener(listener);
		
		update();
		updateReservation();
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
						new Passenger[startSeatAmount]);
		
		currentFlight = new Flight(new Date(),  new Plane(PlaneType.BOEING737), new Airport(AirportType.COPENHAGEN), new Airport(AirportType.STOCKHOLM));
		
		Passenger[] passengerArray = currentReservation.getPassengers();
		
		for(int i=0; i<passengerArray.length; i++)
		{
			passengerArray[i] = new Passenger(null, null);
		}
		
		currentReservation.setPassengers(passengerArray);
	}
	
	private void updateReservation()
	{
		currentReservation.setFlight(currentFlight);
		
		updatePassengers();
	}
	
	private void updatePassengers()
	{
		int amount = 0;
		
		if(seatAmountLabel.getValue() != null)
		{
			amount = Integer.parseInt((String)seatAmountLabel.getValue());
		}
		
		if( amount == 0 )
			return;
		
		Passenger[] passengerArray = new Passenger[amount];
		
		for(int i=0; i<passengerArray.length; i++)
		{
			if(i < currentReservation.getPassengers().length)
			{
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
	}
	
	void updateFlightPanel()
	{
		if(planePanel != null)
			planePanel.updateSeats();
	}
}
