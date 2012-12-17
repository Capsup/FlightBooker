package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

/**
 * A menu that the user can use in order to edit the passenger amount, the selected flight aswell as the passengers seats of a selected flight.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * 
 */
public class FlightManagerMenu
{
	private JFrame frame;
	private JPanel mainPanel;

	private Flight[] displayedFlights;
	private JPanel[] flightPanels;

	private Dimension frameSize;

	private Reservation currentReservation;

	private JFormattedTextField seatAmountLabel;

	private FlightPanel flightPanel;

	private ArrayList<Flight> flights;

	// Start parameters
	private int startSeatAmount;

	private JDateChooser startDateLabel;
	private JDateChooser endDateLabel;
	private DefaultListModel listModel;
	private JList viewedList;
	private JScrollPane scrollPane;
	private JComboBox departureList;
	private JComboBox destinationList;

	private JPanel bottomPanel;

	private boolean isNew;

	/**
	 * This class is initialized by passing it a frame to use, since it is not always created with a new JFrame. Since this menu is used in order to
	 * create new reserations aswell as editing exsiting ones, we also need to pass it the reservation to use. The boolean passed defines whether the
	 * reservation should be uploaded to the database as a new or update an exsisting one.
	 * 
	 * @param the
	 *            frame that everything will be using
	 * @param the
	 *            reservation to create or edit
	 * @param whether
	 *            or not the reservation is new or not
	 */
	public FlightManagerMenu( JFrame frame, Reservation reservation, boolean isNew )
	{
		// We save the passed parameters in our fields.
		this.frame = frame;
		this.currentReservation = reservation;
		this.isNew = isNew;

		// We get every single instance of flights contained in our database, and save them.
		flights = Database.getInstance().Get( Flight.class );

		// We initialize an array of displayedFlights, with the same size as the flights gotten.
		displayedFlights = new Flight[flights.size()];

		// We initialize the panels for which we wish to display the displayedFlights.
		flightPanels = new JPanel[displayedFlights.length];

		if( currentReservation == null )
		{
			// If the current reservation is a new one, we set the start seat amount
			// to 0, and initialize the reservation.

			startSeatAmount = 0;

			initializeReservation();
		}
		else
		{
			// We set the starting seat amount to the number of passangers in the current reservation.

			startSeatAmount = currentReservation.getPassengers().length;
		}

		// We setup the frame
		setupFrame();

		// We initialize the user interface of the menu
		makeContent();

		// We calculate the results of the search inputs.
		calculateResults();
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
		int index;

		public ButtonListener( int i )
		{
			// If we press a flight panel we pass the index of the flight to the class.

			index = i;
		}

		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Make Reservation":

					// We check whether we can commit the changes, or there are still parameters left untouched.
					if( canCommit() )
					{
						// If this is not a new reservation we make sure to replace the previous reservation in saved in the flight
						// with the updated one
						if( !isNew )
							currentReservation.getFlight().replaceReservation( currentReservation.getCurrentFlightReservationIndex(),
							        currentReservation );

						// We remove the main panel from the frame, in order to have a clean frame for the new menu to use.
						frame.remove( mainPanel );

						// We initialize a new passenger manager menu, passing the frame, the
						// current reservation aswell as if this is a new reservation or not
						new PassengerManagerMenu( frame, currentReservation, isNew );
					}
				break;
				case "Flight Panel":

					// We start out by removing the plane panel from the bottom panel, in order to make space for the updated panel
					bottomPanel.remove( flightPanel );

					// We wipe the passengers of the current reservation, since they might not fit in the new flight.
					wipePassengers();

					// We set the flight assigned to our current reservation to be the flight we pressed.
					currentReservation.setFlight( flights.get( index ) );

					// We initialize a new FlightPanel with the new flight.
					flightPanel = new FlightPanel( currentReservation.getFlight(), currentReservation, new Dimension( frameSize.width,
					        ( ( ( frameSize.height / 3 * 2 ) / 4 ) * 3 ) ), true );

					// We add the new flightPanel to the bottom panel
					bottomPanel.add( flightPanel );

					// We make sure that the main panel is up to date.
					mainPanel.invalidate();
					mainPanel.validate();
					mainPanel.repaint();
				break;
			}
		}
	}

	/**
	 * 
	 * TextListener class that allows the listener to respond to events in which a property has changed.
	 * 
	 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
	 * @version 1.0
	 * 
	 */
	class TextListener implements PropertyChangeListener
	{
		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			// Whenever a property is changed, we wish to calculate the new search results, as well us update our reservation.
			calculateResults();
			updatePassengers();
		}
	}

	/**
	 * 
	 * TextActionListener class that allows the listener to respond to events in which a text has been clicked.
	 * 
	 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
	 * @version 1.0
	 * 
	 */
	class TextActionListener implements ActionListener
	{
		@Override
		public void actionPerformed( ActionEvent e )
		{
			// Whenever a text concerning the calculated results is changed, we
			// wish to calculate the new search results, as well us update our reservation.
			calculateResults();
			updatePassengers();
		}
	}

	/**
	 * calculateResults method that allows the user to calculate the flights shown compared to the search criteria
	 */
	private void calculateResults()
	{
		// Since the listModel of the scrollview in which the flights are shown is not initialized
		// everytime this method is called we need stop the code here.
		if( listModel == null )
			return;

		int height = 0;

		// We remove everything already stored in our viewed list in order to make space for the new search results
		viewedList.removeAll();

		// We iterate through our flight array in order to check if the flight matches with the search criteria
		for( int i = 0; i < flights.size(); i++ )
		{
			Flight currentFlight = flights.get( i );

			boolean bounce = true;

			// Check if the startDate time is smaller than the actual flight time.
			if( !( currentFlight.getDate().getTimeInMillis() >= startDateLabel.getCalendar().getTimeInMillis() ) )
			{
				bounce = false;
			}

			// Check if the endDate time is greater than the actual flight time.
			if( !( currentFlight.getDate().getTimeInMillis() <= endDateLabel.getCalendar().getTimeInMillis() ) )
			{
				bounce = false;
			}

			// Check if the departure selected is the same as the actual flight.
			if( departureList.getSelectedIndex() > 0 && !departureList.getSelectedItem().toString().equals( currentFlight.getOrigin().getName() ) )
			{
				bounce = false;
			}

			// Check if the destination selected is the same as the actual flight.
			if( destinationList.getSelectedIndex() > 0
			        && !destinationList.getSelectedItem().toString().equals( currentFlight.getDestination().getName() ) )
			{
				bounce = false;
			}

			int amount = 0;

			// if the value of the seatAmount label is not null we retrieve the value
			if( seatAmountLabel.getValue() != null )
				amount = Integer.parseInt( ( String ) seatAmountLabel.getValue() );

			// Check if the amount of seats left on the flight is greater than the amount of seats you wish to reserve
			if( currentFlight.getSeatsLeft() < amount )
			{
				bounce = false;
			}

			// If any of the search criteria didnt return false we let the flight be displayed.
			if( bounce )
			{
				// we initialize a new JPanel with the informations of the current flight
				JPanel flightPanel = setupFlightPanel( currentFlight );

				JButton flightPanelButton = new JButton();
				flightPanelButton.setActionCommand( "Flight Panel" );

				flightPanelButton.add( flightPanel );

				flightPanelButton.addActionListener( new ButtonListener( i ) );

				// We add the height of the new button to the collected height of the new list.
				height += flightPanelButton.getPreferredSize().height;

				// We add the flight button to our list.
				viewedList.add( flightPanelButton );
			}
		}

		// We add a last margin for the list to look nice
		height += 5;

		// We set the new height of the viewed list to the calculated size.
		viewedList.setPreferredSize( new Dimension( 200, height ) );

		// We make sure that the main panel is up to date.
		mainPanel.invalidate();
		mainPanel.validate();
		mainPanel.repaint();

	}

	private void setupFrame()
	{
		frameSize = new Dimension( 1000, 600 );

		frame.setSize( frameSize.width, frameSize.height );
		frame.setResizable( false );
		frame.setTitle( "Flight Manager Menu" );
		frame.setLocationRelativeTo( null );
	}

	private void makeContent()
	{
		Container contentPane = frame.getContentPane();
		contentPane.setLayout( new BorderLayout() );

		mainPanel = new JPanel();

		// Top
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		topPanel.setPreferredSize( new Dimension( frameSize.width, frameSize.height / 3 ) );

		// Top Left Panel
		JPanel topLeftPanel = new JPanel();
		topLeftPanel.setLayout( new BoxLayout( topLeftPanel, BoxLayout.Y_AXIS ) );
		topLeftPanel.setPreferredSize( new Dimension( frameSize.width / 2, frameSize.height / 3 ) );

		// Start Date Panel
		JPanel startDatePanel = new JPanel();
		startDatePanel.setLayout( new BoxLayout( startDatePanel, BoxLayout.X_AXIS ) );

		// Start Date Title Label
		JLabel startDateTitleLabel = new JLabel( "Start Date:" );
		startDateTitleLabel.setPreferredSize( new Dimension( frameSize.width / 4, ( frameSize.height / 2 ) / 4 ) );
		startDateTitleLabel.setHorizontalAlignment( startDateTitleLabel.CENTER );

		// Start Date Label
		TextListener textListener = new TextListener();

		Calendar calendarToUse = Calendar.getInstance();

		startDateLabel = new JDateChooser();
		calendarToUse.set( 2013, 0, 1 );
		startDateLabel.setDate( calendarToUse.getTime() );
		startDateLabel.addPropertyChangeListener( textListener );

		// Start Date Panel Finish Up
		startDatePanel.add( startDateTitleLabel );
		startDatePanel.add( startDateLabel );
		// Start Date Panel Finished

		// End Date Panel
		JPanel endDatePanel = new JPanel();
		endDatePanel.setLayout( new BoxLayout( endDatePanel, BoxLayout.X_AXIS ) );

		// End Date Title Label
		JLabel endDateTitleLabel = new JLabel( "End Date:" );
		endDateTitleLabel.setPreferredSize( new Dimension( frameSize.width / 4, ( frameSize.height / 2 ) / 4 ) );
		endDateTitleLabel.setHorizontalAlignment( endDateTitleLabel.CENTER );

		// End Date Label
		endDateLabel = new JDateChooser();
		endDateLabel.setPreferredSize( new Dimension( frameSize.width / 8, ( ( frameSize.height / 6 ) / 4 ) ) );
		calendarToUse.set( 2013, 11, 31 );
		endDateLabel.setDate( calendarToUse.getTime() );
		endDateLabel.addPropertyChangeListener( textListener );

		// End Date Panel Finish Up
		endDatePanel.add( endDateTitleLabel );
		endDatePanel.add( endDateLabel );
		// End Date Panel Finished

		// Destination Panel
		JPanel destinationPanel = new JPanel();
		destinationPanel.setLayout( new BoxLayout( destinationPanel, BoxLayout.X_AXIS ) );

		// Destination Title Label
		JLabel destinationTitleLabel = new JLabel( "Departure / Destination:" );
		destinationTitleLabel.setPreferredSize( new Dimension( frameSize.width / 4, ( frameSize.height / 2 ) / 4 ) );
		destinationTitleLabel.setHorizontalAlignment( destinationTitleLabel.CENTER );

		// Destination Choose Panel
		JPanel destinationChoosePanel = new JPanel();
		destinationChoosePanel.setLayout( new BoxLayout( destinationChoosePanel, BoxLayout.X_AXIS ) );

		String[] availableAirports = new String[Airport.getDestinations().length + 1];

		availableAirports[0] = "- Unknown -";

		for( int i = 1; i < Airport.getDestinations().length + 1; i++ )
		{
			availableAirports[i] = Airport.getDestinations()[i - 1];
		}

		departureList = new JComboBox( availableAirports );
		departureList.addActionListener( new TextActionListener() );
		destinationList = new JComboBox( availableAirports );
		destinationList.addActionListener( new TextActionListener() );

		// Destination Choose Panel Finish Up
		destinationChoosePanel.add( departureList );
		destinationChoosePanel.add( destinationList );
		// Destination Choose Panel Finsihed

		// Destination Panel Finish Up
		destinationPanel.add( destinationTitleLabel );
		destinationPanel.add( destinationChoosePanel );
		// Destination Panel Finished

		// Seat Amount Panel
		JPanel seatAmountPanel = new JPanel();
		seatAmountPanel.setLayout( new BoxLayout( seatAmountPanel, BoxLayout.X_AXIS ) );

		// Seat Amount Title Label
		JLabel seatAmountTitleLabel = new JLabel( "Seat Amount:" );
		seatAmountTitleLabel.setPreferredSize( new Dimension( frameSize.width / 4, ( frameSize.height / 2 ) / 4 ) );
		seatAmountTitleLabel.setHorizontalAlignment( seatAmountTitleLabel.CENTER );

		// Seat Amount Label
		seatAmountLabel = new JFormattedTextField();

		try
		{
			seatAmountLabel = new JFormattedTextField( new MaskFormatter( "#" ) );
		}
		catch( ParseException e )
		{

		}

		seatAmountLabel.setText( "" + startSeatAmount );

		seatAmountLabel.addPropertyChangeListener( textListener );

		// Seat Amount Panel Finish Up
		seatAmountPanel.add( seatAmountTitleLabel );
		seatAmountPanel.add( seatAmountLabel );
		// Seat Amount Panel Finished

		// Top Left Panel Finish Up
		topLeftPanel.add( startDatePanel );
		topLeftPanel.add( endDatePanel );
		topLeftPanel.add( destinationPanel );
		topLeftPanel.add( seatAmountPanel );
		// Top Left Panel Finished

		// Top Right
		JPanel topRightPanel = new JPanel();
		// topRightPanel.setPreferredSize(new Dimension(frameSize.width/2, frameSize.height/3));

		listModel = new DefaultListModel<>();
		viewedList = new JList<>( listModel );
		viewedList.setLayout( new BoxLayout( viewedList, BoxLayout.Y_AXIS ) );
		viewedList.setPreferredSize( new Dimension( 200, 1000 ) );

		for( JPanel flightPanel : flightPanels )
		{
			if( flightPanel != null )
			{
				JButton test = new JButton();
				test.add( flightPanel );

				viewedList.add( test );
			}
		}

		scrollPane = new JScrollPane( viewedList );
		scrollPane.setPreferredSize( new Dimension( frameSize.width / 2, frameSize.height / 3 ) );
		topRightPanel.add( scrollPane );

		topPanel.add( topLeftPanel, BorderLayout.CENTER );
		topPanel.add( topRightPanel, BorderLayout.EAST );

		// Bottom
		bottomPanel = new JPanel();
		bottomPanel.setLayout( new BorderLayout() );
		bottomPanel.setPreferredSize( new Dimension( frameSize.width, frameSize.height / 3 * 2 ) );

		// Plane Panel
		flightPanel = new FlightPanel( currentReservation.getFlight(), currentReservation, new Dimension( frameSize.width,
		        ( ( ( frameSize.height / 3 * 2 ) / 4 ) * 3 ) ), true );

		// Button Panel
		JPanel buttonPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		buttonPanel.setPreferredSize( new Dimension( frameSize.width, ( frameSize.height / 3 * 2 / 4 ) ) );

		JButton makeReservationButton = new JButton( "Make Reservation" );
		JButton closeButton = new JButton( "Close" );

		buttonPanel.add( makeReservationButton );

		// bottomPanel.add(planePanel, BorderLayout.CENTER);
		bottomPanel.add( buttonPanel, BorderLayout.SOUTH );
		bottomPanel.add( flightPanel );

		// Finish up
		mainPanel.add( topPanel, BorderLayout.NORTH );
		// mainPanel.add(middlePanel, BorderLayout.CENTER);
		mainPanel.add( bottomPanel, BorderLayout.SOUTH );

		contentPane.add( mainPanel );

		// Listeners
		ButtonListener listener = new ButtonListener( 0 );

		makeReservationButton.addActionListener( listener );

		update();
	}

	/**
	 * The setupFlightPanel class setups a flight panel with any valid data to display taken from the passed flight
	 * 
	 * @param flight
	 *            to make the panel from
	 * @return A JPanel that has been setup according to the passed flight.
	 */
	JPanel setupFlightPanel( Flight currentFlight )
	{
		// Flight Panel
		JPanel newFlightPanel = new JPanel();

		newFlightPanel.setLayout( new BoxLayout( newFlightPanel, BoxLayout.X_AXIS ) );
		newFlightPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		newFlightPanel.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );
		newFlightPanel.setBackground( Color.WHITE );

		// First Panel
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout( new BoxLayout( firstPanel, BoxLayout.Y_AXIS ) );
		firstPanel.setBackground( Color.WHITE );

		// Departure Label
		String stringToUse = "Departure: " + currentFlight.getOrigin().getName();

		JLabel departureLabel = new JLabel( stringToUse );

		// Destination Label
		stringToUse = "Destination: " + currentFlight.getDestination().getName();

		JLabel destinationLabel = new JLabel( stringToUse );

		// First Panel Finish Up
		firstPanel.add( departureLabel );
		firstPanel.add( destinationLabel );
		// First Panel Finished

		// Second Panel
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout( new BoxLayout( secondPanel, BoxLayout.Y_AXIS ) );
		secondPanel.setBackground( Color.WHITE );

		// Date Label
		stringToUse = "" + currentFlight.getDate().get( Calendar.DATE ) + "/" + ( currentFlight.getDate().get( Calendar.MONTH ) + 1 ) + " - "
		        + currentFlight.getDate().get( Calendar.YEAR );

		JLabel dateLabel = new JLabel( stringToUse );

		// Time Label
		String minute = "";

		if( currentFlight.getDate().get( Calendar.MINUTE ) < 10 )
		{
			minute = "0" + currentFlight.getDate().get( Calendar.MINUTE );
		}
		else
		{
			minute = "" + currentFlight.getDate().get( Calendar.MINUTE );
		}

		stringToUse = "" + currentFlight.getDate().get( Calendar.HOUR_OF_DAY ) + ":" + minute;

		JLabel timeLabel = new JLabel( stringToUse );

		// Second Panel Finish Up
		secondPanel.add( dateLabel );
		secondPanel.add( timeLabel );
		// Second Panel Finished

		// Third Panel
		JPanel thirdPanel = new JPanel();
		thirdPanel.setLayout( new BoxLayout( thirdPanel, BoxLayout.Y_AXIS ) );
		thirdPanel.setBackground( Color.WHITE );

		// Seat Label
		stringToUse = "Available Seats: " + currentFlight.getSeatsLeft();

		JLabel seatLabel = new JLabel( stringToUse );

		// Plane Type Label
		stringToUse = "Plane: " + currentFlight.getPlane().getPlaneTypeString();

		JLabel priceLabel = new JLabel( stringToUse );

		// Third Panel Finish Up
		thirdPanel.add( seatLabel );
		thirdPanel.add( priceLabel );
		// Third Panel Finished

		// Flight Panel Finish Up
		newFlightPanel.add( firstPanel );
		newFlightPanel.add( Box.createHorizontalGlue() );
		newFlightPanel.add( secondPanel );
		newFlightPanel.add( Box.createHorizontalGlue() );
		newFlightPanel.add( thirdPanel );
		// Flight Panel Finished

		return newFlightPanel;
	}

	private void update()
	{
		frame.setVisible( true );
	}

	/**
	 * initializeReservation method initializes the currentReservation. This is done since many of the methods used to change the reservation requires
	 * non-null instances of the different fields.
	 */
	private void initializeReservation()
	{
		currentReservation = new Reservation();

		currentReservation.setPassengers( new Passenger[startSeatAmount] );

		Flight currentFlight = flights.get( 0 );

		currentReservation.setFlight( currentFlight );
	}

	/**
	 * the updatePassenger method updates the passenger array accordingly to the amount of seats chosen.
	 */
	private void updatePassengers()
	{
		int amount = 0;

		// We will only update our passengers if the value of the seat amount label is not null
		if( seatAmountLabel.getValue() != null )
		{
			// The length of the new passenger array is defined by the seat amount label.
			amount = Integer.parseInt( ( String ) seatAmountLabel.getValue() );

			// We initialize the new array with the correct amount.
			Passenger[] newPassengerArray = new Passenger[amount];

			// We initialize an array that holds the current reservations passengers.
			Passenger[] currentPassengerArray = currentReservation.getPassengers();

			// We iterate through the new passenger array to make sure the new array contains the previously
			// selected seats and passengers.
			for( int i = 0; i < newPassengerArray.length; i++ )
			{
				// As long as the index is less that the current passenger array we
				// set the passenger of the new passenger array to the one of the current passenger array.
				if( i < currentPassengerArray.length )
				{
					int index = i;

					// We go through the current passenger array until we find a passenger that is not null
					while( currentPassengerArray[index] == null && index < currentPassengerArray.length )
					{
						index += 1;
					}

					if( currentPassengerArray[index] != null )
					{
						// We assign the passenger from the current passenger array to the one of the new passenger array.
						newPassengerArray[i] = currentPassengerArray[index];
					}
					else
					{
						// If we did not find any passenger in the current passenger array we initialize a new one for the new passenger array.
						newPassengerArray[i] = new Passenger( null, null );
					}
				}
				else
				{
					// If we exceed the length that the current passenger array had, we initialize null passengers for the remained indexes.
					newPassengerArray[i] = new Passenger( null, null );
				}
			}

			// If we reduce the length of the passenger array we make sure that none of the
			// exceeding indexes seats are still left as occupied.
			if( currentReservation.getPassengers().length > newPassengerArray.length )
			{
				int start = newPassengerArray.length;

				// We iterate through any seat that is not a part of the new passenger array.
				for( int i = start; i < currentReservation.getPassengers().length; i++ )
				{
					// If the seat was occupied we make sure that it changes its booking status to false.
					if( currentReservation.getPassengers()[i].getSeat() != null )
						currentReservation.getPassengers()[i].getSeat().changeBookingStatus( false );
				}
			}

			// We assign the new passenger array to the current reservation.
			currentReservation.setPassengers( newPassengerArray );

			// We update the flight panel, to make sure that the data shown corresponds to that which is saved
			// in the current reservation.
			updateFlightPanel();
		}
	}

	/**
	 * The wipePassengers method is used to remove any seat from the current reservation.
	 */
	void wipePassengers()
	{
		Passenger[] passengers = currentReservation.getPassengers();

		for( Passenger passenger : passengers )
		{
			passenger.setSeat( null );
		}
	}

	/**
	 * The updateFlightPanel method is used to update the flight panel seats.
	 */
	void updateFlightPanel()
	{
		if( flightPanel != null )
			flightPanel.updateSeats();
	}

	/**
	 * The canCommit method returns whether or not you have filled out all viable data of the menu.
	 * 
	 * @return
	 */
	boolean canCommit()
	{
		boolean returnBool = true;

		Passenger[] passengers = currentReservation.getPassengers();

		if( passengers.length > 0 )
		{
			// We iterate through every passenger of the reservtion and check that they have a specified seat
			for( Passenger passenger : passengers )
			{
				if( passenger.getSeat() == null )
				{
					returnBool = false;

					// if one of the passenger has not selected a seat we show an error message, telling the user to select one.
					showErrorDialog();
				}
			}
		}
		else
		{
			returnBool = false;
		}

		return returnBool;
	}

	/**
	 * The showErrorDialog displays a dialog telling the user to fill out any given information, before continuing.
	 */
	private void showErrorDialog()
	{
		JOptionPane.showMessageDialog( frame, "Please choose enough seats", "Error", JOptionPane.ERROR_MESSAGE );
	}
}
