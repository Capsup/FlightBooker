package MainPackage;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import MainPackage.MainMenu.ButtonListener;
import MainPackage.Plane.PlaneType;

public class NewReservationMenu
{
	private JFrame frame;
	private JPanel mainPanel;
	
	public NewReservationMenu(JFrame frame)
	{
		this.frame = frame;
		
		setupFrame();
		
		makeContent();
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
				Window[] allWindows = Window.getWindows();
				System.out.println(allWindows.length);
				break;
			
			}
		}
	}
	
	private void setupFrame()
	{
		frame.setSize(400, 600);
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
		topPanel.setBorder(BorderFactory.createEtchedBorder(1));
		
			//Top Title
		JPanel topTitlePanel = new JPanel();
		topTitlePanel.setLayout(new BoxLayout(topTitlePanel, BoxLayout.Y_AXIS));
		
		JLabel startDateTitleLabel = new JLabel("Start Date");
		JLabel endDateTitleLabel = new JLabel("Start Date");
		JLabel destinationTitleLabel = new JLabel("Destination");
		JLabel seatAmountTitleLabel = new JLabel("Seat Amount");
		
		topTitlePanel.add(startDateTitleLabel);
		topTitlePanel.add(endDateTitleLabel);
		topTitlePanel.add(destinationTitleLabel);
		topTitlePanel.add(seatAmountTitleLabel);
		
			//Top Parameter
		JPanel topParameterPanel = new JPanel();
		topParameterPanel.setLayout(new BoxLayout(topParameterPanel, BoxLayout.Y_AXIS));
		
		JTextField startDateLabel = new JTextField();
		startDateLabel.setPreferredSize(new Dimension(100,15));
		
		JTextField endDateLabel = new JTextField();
		endDateLabel.setPreferredSize(new Dimension(100,15));
		
		JTextField destinationLabel = new JTextField();
		destinationLabel.setPreferredSize(new Dimension(100,15));
		
		JTextField seatAmountLabel = new JTextField();
		seatAmountLabel.setPreferredSize(new Dimension(100,15));
		
		topParameterPanel.add(startDateLabel);
		topParameterPanel.add(endDateLabel);
		topParameterPanel.add(destinationLabel);
		topParameterPanel.add(seatAmountLabel);
		
		topPanel.add(topTitlePanel,BorderLayout.WEST);
		topPanel.add(topParameterPanel,BorderLayout.EAST);
		
		//Center
		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(BorderFactory.createEtchedBorder(1));
		
		JList viewedList = new JList();
		
		viewedList.setPreferredSize(new Dimension(370,1000));
		
		JScrollPane scrollPane = new JScrollPane(viewedList);
		scrollPane.setPreferredSize(new Dimension(390, 275));
		middlePanel.add(scrollPane);
		
		//Bottom
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createEtchedBorder(1));
		
			//Plane Panel
		PlanePanel planePanel = new PlanePanel(new Plane(PlaneType.BOEING747));
		
			//Button Panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
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
	}
	
	private void update()
	{
		frame.setVisible(true);
	}
	
}
