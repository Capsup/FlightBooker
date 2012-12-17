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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * A menu that the user can use in order to access the information of a given reservation
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * 
 */
public class ReservationInfoMenu
{
	private JFrame frame;
	private JPanel mainPanel;

	private Reservation currentReservation;

	private FlightPanel planePanel;

	private boolean isNew;

	/**
	 * This class is initialized by passing it a frame to contain all components, aswell as a reservation which is used to display all the given
	 * information. the boolean passed determines whether the reservation shall be added to the database, or be updated in the database.
	 * 
	 * @param frame
	 * @param reservation
	 * @param isNew
	 */
	public ReservationInfoMenu( JFrame frame, Reservation reservation, boolean isNew )
	{
		this.frame = frame;
		this.isNew = isNew;
		currentReservation = reservation;

		setupFrame();

		makeContent();
	}

	/**
	 * 
	 * ButtonListener class that allows the listener to respond to events such as clicking on a button or other actions.
	 * 
	 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
	 * @version 1.0
	 * 
	 */
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Edit Reservation":

					frame.remove( mainPanel );

					// we rewind to the flight manager menu, which we pass the current reservation.
					new FlightManagerMenu( frame, currentReservation, isNew );

				break;

				case "OK":

					frame.dispose();

					Flight flightInstance = currentReservation.getFlight();

					if( isNew )
					{
						// Add Reservation to flight and set the current reservations flight index
						currentReservation.setCurrentFlightReservationIndex( flightInstance.addReservation( currentReservation ) );

						// Set the current date
						Calendar newCalendar = Calendar.getInstance();
						currentReservation.setReservedDate( newCalendar );

						// Set the id of the database instance
						currentReservation.setID( Database.getInstance().GetID( Reservation.class ) );

						// We make sure that all the reservations stored in the owner is up to date.
						currentReservation.getOwner().updateReservations();

						Reservation[] newReservations = currentReservation.getOwner().getReservations();

						if( newReservations == null )
						{
							// If the new reservation array is null we initialize it
							newReservations = new Reservation[1];
						}
						else
						{
							// If the new reservation is not null we extends it length by 1, maintaining any data in it.
							newReservations = Arrays.copyOf( newReservations,
							        currentReservation.getPassengers()[0].getPerson().getReservations().length + 1 );
						}

						// We set the last index of the new reservation array to the current reservation.
						newReservations[newReservations.length - 1] = currentReservation;

						// We set the reservations of the owner of the current reservation to be the new reservation array.
						currentReservation.getOwner().setReservations( newReservations );

						// We replace the owner in the database
						Database.getInstance().Replace( currentReservation.getOwner().getID(), currentReservation.getOwner() );

						// We add the reservation to the database
						Database.getInstance().Add( currentReservation );
					}
					else
					{
						// If the reservation is not new, we simply replace it in the database.
						Database.getInstance().Replace( currentReservation.getID(), currentReservation );
					}

					// We replace the flight that is associated with the reservation, in order to make sure that it is up to date in the database.
					Database.getInstance().Replace( flightInstance.getID(), flightInstance );

				break;

				case "Cancel":
					frame.dispose();
				break;
				case "Delete":
					deleteReservation();
				break;
			}

		}
	}

	void setupFrame()
	{
		frame.setSize( 450, 600 );
		frame.setResizable( false );
		frame.setTitle( "Reservation Info Menu" );

		frame.setLocationRelativeTo( null );
		frame.setVisible( true );

	}

	void makeContent()
	{
		Container contentPane = frame.getContentPane();

		mainPanel = new JPanel();
		mainPanel.setLayout( new BorderLayout() );

		// Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BoxLayout( topPanel, BoxLayout.Y_AXIS ) );

		// Info Panel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout( new BoxLayout( infoPanel, BoxLayout.Y_AXIS ) );

		// Customer Panel
		JPanel customerPanel = new JPanel();
		customerPanel.setLayout( new BoxLayout( customerPanel, BoxLayout.X_AXIS ) );

		// Customer Title Label
		JLabel customerTitleLabel = new JLabel( "Customer:  " );

		// Customer Label
		String personString = "";

		if( currentReservation.getOwner() != null )
			personString = currentReservation.getOwner().getFirstName() + " " + currentReservation.getOwner().getSurName() + ", "
			        + currentReservation.getOwner().getPhone();

		JLabel customerLabel = new JLabel( personString );

		// Customer Panel Finish Up
		customerPanel.add( customerTitleLabel );
		customerPanel.add( customerLabel );
		// Customer Panel Finished

		// Flight Panel
		JPanel flightPanel = new JPanel();
		flightPanel.setLayout( new BoxLayout( flightPanel, BoxLayout.X_AXIS ) );

		// Flight Title Label
		JLabel flightTitleLabel = new JLabel( "Flight:  " );

		String flightString = "";

		if( currentReservation.getFlight() != null )
			flightString = currentReservation.getFlight().getOrigin().getName() + " - " + currentReservation.getFlight().getDestination().getName();

		// Flight Label
		JLabel flightLabel = new JLabel( flightString );

		// Flight Panel Finishup
		flightPanel.add( flightTitleLabel );
		flightPanel.add( flightLabel );
		// Flight Panel Finished

		// Time Panel
		JPanel timePanel = new JPanel();
		timePanel.setLayout( new BoxLayout( timePanel, BoxLayout.X_AXIS ) );

		// Time Title Label
		JLabel timeTitleLabel = new JLabel( "Time:  " );

		String timeString = "";

		if( currentReservation.getFlight() != null )
			timeString = "" + currentReservation.getFlight().getDate().getTime();

		// Time Label
		JLabel timeLabel = new JLabel( timeString );

		// Time Panel Finishup
		timePanel.add( timeTitleLabel );
		timePanel.add( timeLabel );
		// Time Panel Finished

		// Passenger Panel
		JPanel passengerPanel = new JPanel();

		// Passenger Label
		JLabel passengerLabel = new JLabel( "Passengers" );

		// Passenger Table
		String[] columns = { "Passengers", "Seats" };

		Object[][] passengerData = makePassengerData();

		JTable passengerTable = new JTable( passengerData, columns );
		passengerTable.setEnabled( false );
		passengerTable.setBackground( Color.WHITE );

		JScrollPane scrollPane = new JScrollPane( passengerTable );
		scrollPane.setPreferredSize( new Dimension( 450, 200 ) );

		// Passenger Panel Finished
		passengerPanel.add( passengerLabel );
		passengerPanel.add( scrollPane );
		// Passenger Panel Finished

		// Info Panel finishup
		infoPanel.add( customerPanel );
		infoPanel.add( flightPanel );
		infoPanel.add( timePanel );
		infoPanel.add( passengerPanel );
		// Info Panel Finished

		// Top Panel Finishup
		topPanel.add( infoPanel );
		topPanel.add( passengerPanel );
		// Top Panel Finished

		// Center Panel
		JPanel centerPanel = new JPanel();

		// Flight Panel
		planePanel = new FlightPanel( currentReservation.getFlight(), currentReservation, new Dimension( 400, 200 ), false );

		// Center Panel Finishup
		centerPanel.add( planePanel );
		// Center Panel Finish

		// Bottom Panel
		JPanel bottomPanel = new JPanel();

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new FlowLayout() );

		ButtonListener listener = new ButtonListener();
		// Edit Reservation Button
		JButton editReservationButton = new JButton( "Edit Reservation" );
		editReservationButton.setActionCommand( "Edit Reservation" );
		editReservationButton.addActionListener( listener );

		// Ok Button
		JButton okButton = new JButton( "OK" );
		okButton.setActionCommand( "OK" );
		okButton.addActionListener( listener );

		// Cancel Button
		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.setActionCommand( "Cancel" );
		cancelButton.addActionListener( listener );

		// Button Panel Finishup
		buttonPanel.add( editReservationButton );
		buttonPanel.add( okButton );
		buttonPanel.add( cancelButton );

		if( !isNew )
		{
			// Delete Button
			JButton deleteButton = new JButton( "Delete Reservation" );
			deleteButton.setActionCommand( "Delete" );
			deleteButton.addActionListener( listener );

			buttonPanel.add( deleteButton );
		}
		// Button Panel Finished

		// Bottom Panel Finishup
		bottomPanel.add( buttonPanel );
		// Bottom Panel FInished

		mainPanel.add( topPanel, BorderLayout.NORTH );
		mainPanel.add( centerPanel, BorderLayout.CENTER );
		mainPanel.add( bottomPanel, BorderLayout.SOUTH );

		contentPane.add( mainPanel );

	}

	/**
	 * This method returns the data of the passengers associated with the current reservation.
	 * 
	 * @return the data of the passenger table
	 */
	Object[][] makePassengerData()
	{
		Object[][] returnArray;

		returnArray = new Object[currentReservation.getPassengers().length][];

		Passenger[] passengers = currentReservation.getPassengers();

		if( passengers != null )
		{
			for( int i = 0; i < passengers.length; i++ )
			{
				// We iterate through our passengers in order to assign each data label.

				// the first data string is assigned the persons first name and sur name.
				String firstData = passengers[i].getPerson().getFirstName() + " " + passengers[i].getPerson().getSurName();

				// The second data string is assigned the persons seat position in the plane.
				String secondData = "(" + ( passengers[i].getSeat().getPosition().height + 1 ) + ","
				        + ( passengers[i].getSeat().getPosition().width + 1 ) + ")";

				// We store the data in the return array
				returnArray[i] = new Object[] { firstData, secondData };
			}
		}

		// We return the array
		return returnArray;
	}

	/**
	 * delete the current reservation, removing it from the database. updates anything associated with the reservation. closes the current reservation
	 * info menu.
	 */
	void deleteReservation()
	{
		// We remove the current reservation from the flight.
		currentReservation.getFlight().removeReservationAt( currentReservation.getCurrentFlightReservationIndex() );

		int count = 0;

		for( Reservation reservation : currentReservation.getOwner().getReservations() )
		{
			// We iterate through the owner of the reservations reservation array, in order to find the current reservation in the array.
			if( reservation.getID() == currentReservation.getID() )
			{
				// We remove the reservation from the owners reservation array.
				currentReservation.getOwner().removeReservationAt( count );
			}

			count += 1;
		}

		// We replace the owner and the flight in the database.
		Database.getInstance().Replace( currentReservation.getOwner().getID(), currentReservation.getOwner() );
		Database.getInstance().Replace( currentReservation.getFlight().getID(), currentReservation.getFlight() );

		frame.dispose();
	}
}
