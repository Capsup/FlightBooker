package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * 
 * This class is where the Flight Info Menu window is from. It opens up a window where an overview of the specified flights reservations are
 * presented.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */

public class FlightInfoMenu extends JFrame
{
	JPanel mainPanel;

	Flight currentFlight;
	Reservation currentReservation;
	FlightPanel planePanel;

	JTable reservationTable;

	/**
	 * Constructor for the class.
	 * 
	 * @param flight
	 *            - a reference to the flight object that this window is supposed to display information for
	 */
	public FlightInfoMenu( Flight flight )
	{
		currentFlight = flight;

		setupFrame();

		makeContent();
	}

	/**
	 * 
	 * This class is the class responsible for allowing the JTable to respond to mouse clicks.
	 * 
	 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
	 * @version 1.0
	 * 
	 */
	private class TableListener implements MouseListener
	{
		public void mouseClicked( MouseEvent e )
		{
			if( reservationTable.getSelectedRow() >= 0 && reservationTable.getSelectedRow() < currentFlight.getReservations().length )
			{
				currentReservation = currentFlight.getReservations()[reservationTable.getSelectedRow()];

				planePanel.setCurrentReservation( currentReservation );

				planePanel.updateSeats();
			}
		}

		public void mouseEntered( MouseEvent e )
		{

		}

		public void mouseReleased( MouseEvent e )
		{

		}

		public void mousePressed( MouseEvent e )
		{

		}

		public void mouseExited( MouseEvent e )
		{

		}
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			if( e.getActionCommand() == "Inspect" )
			{
				if( currentReservation != null )
					new ReservationInfoMenu( new JFrame(), currentReservation, false );
			}
		}
	}

	void setupFrame()
	{
		setSize( 400, 600 );
		setResizable( false );
		setTitle( "Flight Info Menu" );
		setLocationRelativeTo( null );

	}

	void makeContent()
	{
		Container contentPane = this.getContentPane();

		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setLayout( new BorderLayout() );

		// Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BoxLayout( topPanel, BoxLayout.Y_AXIS ) );

		// ReservationLabel
		JLabel reservationLabel = new JLabel( "Reservations overview" );

		// Reservation Table
		String[] columns = { "Name", "Seats", "Date of Reservation" };

		Object[][] reservationData = makeReservationData();

		// Creates a DefaultTableModel for this JTable. This allows us to add and remove data at runtime.
		TableModel reservationTableModel = new DefaultTableModel( reservationData, columns );
		reservationTable = new JTable( reservationTableModel );
		reservationLabel.setPreferredSize( new Dimension( 400, 200 ) );
		reservationLabel.setBackground( Color.WHITE );

		TableListener listener = new TableListener();

		reservationTable.addMouseListener( listener );

		// Inspect Reservation Button
		JButton inspectReservationButton = new JButton( "Inspect Reservation" );
		inspectReservationButton.setAlignmentX( CENTER_ALIGNMENT );
		inspectReservationButton.setActionCommand( "Inspect" );

		ButtonListener buttonListener = new ButtonListener();

		inspectReservationButton.addActionListener( buttonListener );

		// Top Panel Finish Up
		topPanel.add( reservationLabel );
		topPanel.add( reservationTable );
		topPanel.add( inspectReservationButton );
		// Top Panel Finished

		// Center Panel
		JPanel centerPanel = new JPanel();

		// Flight Panel
		planePanel = new FlightPanel( currentFlight, currentReservation, new Dimension( 400, 200 ), false );

		// Center Panel Finish Up
		centerPanel.add( planePanel );
		// Center Panel Finished

		// Bottom Panel
		JPanel bottomPanel = new JPanel();

		// Column Panel
		JPanel columnPanel = new JPanel();
		columnPanel.setLayout( new BoxLayout( columnPanel, BoxLayout.X_AXIS ) );

		// Left Column Panel
		JPanel leftColumnPanel = new JPanel();
		leftColumnPanel.setLayout( new BoxLayout( leftColumnPanel, BoxLayout.Y_AXIS ) );

		// Plane Label
		JLabel planeLabel = new JLabel( "Plane: " + currentFlight.getPlane().getPlaneTypeString() );

		// Capacity Label
		JLabel capacityLabel = new JLabel( "Capacity: " + currentFlight.getSeatAmount() + " Seats" );

		// Left Column Panel Finish Up
		leftColumnPanel.add( planeLabel );
		leftColumnPanel.add( capacityLabel );
		// Left Column Panel Finished

		// Right Column Panel
		JPanel rightColumnPanel = new JPanel();
		rightColumnPanel.setLayout( new BoxLayout( rightColumnPanel, BoxLayout.Y_AXIS ) );

		// Departure Label
		JLabel departureLabel = new JLabel( "Departure: " + currentFlight.getPlane().getPlaneTypeString() );

		// Destination Label
		JLabel destinationLabel = new JLabel( "Destination: " + currentFlight.getSeatAmount() );

		// Departure Time Label
		String minute = "";

		// Need to add a 0 before minute counts who are less than 10 to make sure it looks right.
		if( currentFlight.getDate().get( Calendar.MINUTE ) < 10 )
		{
			minute = "0" + currentFlight.getDate().get( Calendar.MINUTE );
		}
		else
		{
			minute = "" + currentFlight.getDate().get( Calendar.MINUTE );
		}

		String stringToUse = "" + currentFlight.getDate().get( Calendar.HOUR_OF_DAY ) + ":" + minute;

		JLabel departureTimeLabel = new JLabel( "Departure Time: " + stringToUse );

		// Right Column Panel Finish Up
		rightColumnPanel.add( departureLabel );
		rightColumnPanel.add( destinationLabel );
		rightColumnPanel.add( departureTimeLabel );
		// Right Column Panel Finished

		// Column Panel Finish Up
		columnPanel.add( leftColumnPanel );
		columnPanel.add( rightColumnPanel );
		// Column Panel Finished

		// Bottom Panel Finish Up
		bottomPanel.add( columnPanel );
		// Bottom Panel Finished

		// Main Panel Finish Up
		mainPanel.add( topPanel, BorderLayout.NORTH );
		mainPanel.add( centerPanel, BorderLayout.CENTER );
		mainPanel.add( bottomPanel, BorderLayout.SOUTH );
		// Main Panel Finished

		this.addWindowListener( new WindowAdapter()
		{

			public void windowActivated( WindowEvent e )
			{
				updateMenu();
			}
		} );

		contentPane.add( mainPanel );

		update();
	}

	private void update()
	{
		this.setVisible( true );
	}

	/**
	 * Creates data for the JTable to display based on the reservations the current flight has.
	 * 
	 * @return a 2D array for the JTable to display.
	 */
	Object[][] makeReservationData()
	{
		Object[][] returnArray;

		// If the current flight has any reservations, we loop through em to display their data.
		if( currentFlight.getReservations() != null )
		{
			returnArray = new Object[currentFlight.getReservations().length][];

			Reservation[] reservations = currentFlight.getReservations();

			if( reservations != null )
			{
				for( int i = 0; i < reservations.length; i++ )
				{
					String firstData = reservations[i].getOwner().getFirstName() + " " + reservations[i].getOwner().getSurName();
					String secondData = "" + reservations[i].getPassengers().length;
					String thirdData = "" + reservations[i].getReservationDate().getTime();

					// JTable can only display Objects, therefore we need to create a object with the relevant data.
					returnArray[i] = new Object[] { firstData, secondData, thirdData };
				}
			}

			return returnArray;
		}
		// That's one empty flight. Better create an empty array AKA null.
		else
		{
			returnArray = null;
		}

		return returnArray;
	}

	/**
	 * Update the menu so that it contains the proper, up to date information.
	 */
	void updateMenu()
	{
		//
		currentFlight = Database.getInstance().Get( currentFlight.getID(), Flight.class );

		if( reservationTable.getSelectedRow() >= 0 && reservationTable.getSelectedRow() < currentFlight.getReservations().length )
		{
			currentReservation = currentFlight.getReservations()[reservationTable.getSelectedRow()];
		}

		planePanel.updateSeats();
	}
}
