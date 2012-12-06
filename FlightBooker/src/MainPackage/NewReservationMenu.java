package MainPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import MainPackage.Plane.PlaneType;

public class NewReservationMenu
{
	private JFrame frame;
	private JPanel mainPanel;
	
	private Dimension frameSize = new Dimension(600, 1000);
	
	private Reservation currentReservation;
	private Flight currentFlight;
	private Passenger[] currentPassengers;
	
	private JFormattedTextField seatAmountLabel;
	
	private PlanePanel planePanel;
	
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
				new PassengerManagerMenu(frame);
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
		//topPanel.setBorder(BorderFactory.createEtchedBorder(1));
		topPanel.setPreferredSize(new Dimension(frameSize.width, (frameSize.height/6)));
		
			//Top Title
		JPanel topTitlePanel = new JPanel();
		//topTitlePanel.setLayout(new BoxLayout(topTitlePanel, BoxLayout.Y_AXIS));
		topTitlePanel.setLayout(null);
		topTitlePanel.setPreferredSize(new Dimension(frameSize.width/2, (frameSize.height/6)));
		
		Insets insets = topTitlePanel.getInsets();
		
		JLabel startDateTitleLabel = new JLabel("Start Date");
		//startDateTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		startDateTitleLabel.setBounds(insets.left+(frameSize.width/5), insets.top, 100, 25);
		
		JLabel endDateTitleLabel = new JLabel("End Date");
		//endDateTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		endDateTitleLabel.setBounds(insets.left+(frameSize.width/5), insets.top+((frameSize.height/6)/4), 100, 25);
		
		JLabel destinationTitleLabel = new JLabel("Origin / Destination");
		//destinationTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		destinationTitleLabel.setBounds(insets.left+(frameSize.width/5), insets.top+((frameSize.height/6)/4)*2, 100, 25);
		
		JLabel seatAmountTitleLabel = new JLabel("Seat Amount");
		//seatAmountTitleLabel.setPreferredSize(new Dimension(100,(frameWidth/6)/4));
		seatAmountTitleLabel.setBounds(insets.left+(frameSize.width/5), insets.top+((frameSize.height/6)/4)*3, 100, 25);
		
		topTitlePanel.add(startDateTitleLabel);
		topTitlePanel.add(endDateTitleLabel);
		topTitlePanel.add(destinationTitleLabel);
		topTitlePanel.add(seatAmountTitleLabel);
		
			//Top Parameter
		JPanel topParameterPanel = new JPanel();
		topParameterPanel.setLayout(new BoxLayout(topParameterPanel, BoxLayout.Y_AXIS));
		topParameterPanel.setPreferredSize(new Dimension(frameSize.width/4, (frameSize.height/6)));
		
		JTextField startDateLabel = new JTextField();
		startDateLabel.setPreferredSize(new Dimension(100,((frameSize.height/6)/4)));
		
		JTextField endDateLabel = new JTextField();
		endDateLabel.setPreferredSize(new Dimension(100,((frameSize.height/6)/4)));
		
		JTextField destinationLabel = new JTextField();
		destinationLabel.setPreferredSize(new Dimension(100,((frameSize.height/6)/4)));
		
		//SKAL KUN VÆRE TAL
		seatAmountLabel = new JFormattedTextField();
		
		try 
		{
			seatAmountLabel = new JFormattedTextField(new MaskFormatter("#"));
		} 
		catch (ParseException e) {
			System.out.println("FUCK TRY/CATCH");
		}
		
		seatAmountLabel.addPropertyChangeListener(new TextListener());
		
		seatAmountLabel.setPreferredSize(new Dimension(100,((frameSize.height/6)/4)));
		
		topParameterPanel.add(startDateLabel);
		topParameterPanel.add(endDateLabel);
		topParameterPanel.add(destinationLabel);
		topParameterPanel.add(seatAmountLabel);
		
		topPanel.add(topTitlePanel,BorderLayout.WEST);
		topPanel.add(topParameterPanel,BorderLayout.EAST);
		
		//Center
		JPanel middlePanel = new JPanel();
		//middlePanel.setBorder(BorderFactory.createEtchedBorder(1));
		middlePanel.setPreferredSize(new Dimension(frameSize.width, frameSize.height/3));
		
		JList viewedList = new JList();
		
		viewedList.setPreferredSize(new Dimension(frameSize.width-50,1000));
		
		JScrollPane scrollPane = new JScrollPane(viewedList);
		scrollPane.setPreferredSize(new Dimension(frameSize.width, frameSize.height/3));
		middlePanel.add(scrollPane);
		
		//Bottom
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		//bottomPanel.setBorder(BorderFactory.createEtchedBorder(1));
		bottomPanel.setPreferredSize(new Dimension(frameSize.width, frameSize.height/2));
		
			//Plane Panel
		//Test
		Flight testFlight = new Flight(new Date(), new Plane(PlaneType.BOEING747));
		
		planePanel = new PlanePanel(testFlight, currentReservation, new Dimension(frameSize.width, (frameSize.height/2/6*5)));
		
		//Final
		//PlanePanel planePanel = new PlanePanel(currentFlight, new Dimension(frameSize.width, frameSize.height/2));
		
			//Button Panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setPreferredSize(new Dimension(frameSize.width, frameSize.height/6/2*1));
		
		JButton makeReservationButton = new JButton("Make Reservation");
		JButton inspectReservationButton = new JButton("Inspect Reservation");
		JButton closeButton = new JButton("Close");		
		
		buttonPanel.add(makeReservationButton);
		buttonPanel.add(inspectReservationButton);
		
		bottomPanel.add(planePanel, BorderLayout.NORTH);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		//Finish up
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);
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
		currentReservation = new Reservation(null, null, null, new Passenger[0]);
		
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
