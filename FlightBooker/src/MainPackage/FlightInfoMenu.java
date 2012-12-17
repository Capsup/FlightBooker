package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import MainPackage.FlightManagerMenu.ButtonListener;

public class FlightInfoMenu extends JFrame
{
	private static final String X_AXIS = null;

	JPanel mainPanel;
	
	Flight currentFlight;
	Reservation currentReservation;
	FlightPanel planePanel;
	
	JTable reservationTable;
	
	
	public FlightInfoMenu(Flight flight)
	{
		currentFlight = flight;
		
		setupFrame();
		
		
		makeContent();
	}
	
	private class TableListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			if(reservationTable.getSelectedRow() >= 0 && reservationTable.getSelectedRow() < currentFlight.getReservations().length)
			{
				currentReservation = currentFlight.getReservations()[reservationTable.getSelectedRow()];
				
				planePanel.setCurrentReservation(currentReservation);
				
				planePanel.updateSeats();
			}
		}
		
		public void mouseEntered(MouseEvent e)
		{
			
		}
		
		public void mouseReleased(MouseEvent e)
		{
			
		}
		
		public void mousePressed(MouseEvent e)
		{
			
		}
		
		public void mouseExited(MouseEvent e)
		{
			
		}
	}
	
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getActionCommand() == "Inspect")
			{
				if(currentReservation != null)
					new ReservationInfoMenu(new JFrame(), currentReservation, false);
			}
		}
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
		
		TableModel reservationTableModel = new DefaultTableModel(reservationData,columns);
		reservationTable = new JTable(reservationTableModel);
		reservationLabel.setPreferredSize(new Dimension(400, 200));
		reservationLabel.setBackground(Color.WHITE);
		
		TableListener listener = new TableListener();
		
		reservationTable.addMouseListener(listener);
		
		
				//Inspect Reservation Button
		JButton inspectReservationButton = new JButton("Inspect Reservation");
		inspectReservationButton.setAlignmentX(CENTER_ALIGNMENT);
		inspectReservationButton.setActionCommand("Inspect");
		
		ButtonListener buttonListener = new ButtonListener();
		
		inspectReservationButton.addActionListener(buttonListener);
		
			//Top Panel Finish Up
		topPanel.add(reservationLabel);
		topPanel.add(reservationTable);
		topPanel.add(inspectReservationButton);
			//Top Panel Finished
		
			//Center Panel
		JPanel centerPanel = new JPanel();
		
				//Flight Panel
		planePanel = new FlightPanel(currentFlight, currentReservation, new Dimension(400,200), false);
		
			//Center Panel Finish Up
		centerPanel.add(planePanel);
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
		JLabel departureLabel = new JLabel("Departure: "+currentFlight.getOrigin());
		
						//Destination Label
		JLabel destinationLabel = new JLabel("Destination: "+currentFlight.getDestination());

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
		
		
		this.addWindowListener(new WindowAdapter() {
			
			public void windowActivated(WindowEvent e)
			{
				updateMenu();
			}
		});
		
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
		
		if(currentFlight.getReservations() != null)
		{
			returnArray = new Object[currentFlight.getReservations().length][];
			
			Reservation[] reservations = currentFlight.getReservations();
			
			if(reservations != null)
			{
				for(int i=0; i<reservations.length; i++)
				{
					String firstData = reservations[i].getOwner().getFirstName()+" "+reservations[i].getOwner().getSurName();
					String secondData = ""+reservations[i].getPassengers().length;
					String thirdData = ""+reservations[i].getReservationDate().getTime();
					
					returnArray[i] = new Object[]{firstData, secondData, thirdData};
				}
			}
			
			return returnArray;
		}
		else 
		{
			returnArray = new Object[0][0];
		}
		
		return returnArray;
	}
	
	void updateMenu()
	{
		currentFlight = Database.getInstance().Get(currentFlight.getID(), Flight.class);
		
		if(reservationTable.getSelectedRow() >= 0 && reservationTable.getSelectedRow() < currentFlight.getReservations().length)
		{
			currentReservation = currentFlight.getReservations()[reservationTable.getSelectedRow()];
		}
		
		planePanel.updateSeats();
	}
}
