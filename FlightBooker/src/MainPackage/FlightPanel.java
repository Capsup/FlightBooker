package MainPackage;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The FlightPanel serves as a usable JPanel that can be implemented anywhere without further ado.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * 
 */
public class FlightPanel extends JPanel
{
	private Flight flight;
	private Plane plane;
	private Dimension panelSize;
	private Reservation currentReservation;
	private SeatButton[][] seatButtonArray;

	// Properties
	double scaleModifier;
	int panelToButtonScale;

	int squaredSize;
	int indent = ( int ) ( 10 / scaleModifier );

	int leftPadding;
	int topPadding;

	Insets insets = getInsets();

	// Other
	private boolean editable;

	/**
	 * The FlightPanel makes an easily accessible JPanel to use, whenever one need to display data about a flight.
	 * 
	 * @param the
	 *            flight to take data from
	 * @param the
	 *            current reservation to identify
	 * @param the
	 *            size of the panel
	 * @param whether
	 *            or not the panel is editable
	 */
	public FlightPanel( Flight flight, Reservation reservation, Dimension size, boolean editable )
	{
		this.flight = flight;
		this.plane = flight.getPlane();
		this.panelSize = size;
		this.currentReservation = reservation;
		this.editable = editable;

		// We setup the properties of any shown component in the container
		setupProperties();

		makeContent();
	}

	/**
	 * We make the content of the FlightPanel
	 */
	void makeContent()
	{
		JPanel mainPanel;

		try
		{
			// We try to find an image for the main panel to paint.
			String path = "/images/" + plane.getPlaneTypeString() + ".png";

			Rectangle rect = new Rectangle( 0, 0, panelSize.width, panelSize.height );

			mainPanel = new JPanelWithBackground( path, rect );
		}
		catch( IOException e )
		{
			// If no image is found, the panel will be initialized with no background image.
			mainPanel = new JPanel();
		}

		mainPanel.setPreferredSize( panelSize );
		mainPanel.setLayout( null );

		// We specifiy the string to use for the routeLabel
		String stringToUse = flight.getOrigin().getName() + " - " + flight.getDestination().getName();

		JLabel routeLabel = new JLabel( stringToUse );
		// We use absolute positioning to position the label, based on the size of the panel.
		routeLabel.setBounds( panelSize.width / 20, panelSize.height / 20, 250, 25 );

		// We specify the string to the flightIDLabel
		stringToUse = "Flight ID: " + flight.getID();

		JLabel flightIDPanel = new JLabel( stringToUse );
		// We use absolute positioning to position the label, based on the size of the panel.
		flightIDPanel.setBounds( ( panelSize.width / 20 ) * 10, panelSize.height / 20, 250, 25 );

		// We specify the string to the planeTypeLabel
		stringToUse = flight.getPlane().getPlaneTypeString();

		JLabel planeTypeLabel = new JLabel( stringToUse );
		// We use absolute positioning to position the label, based on the size of the panel.
		planeTypeLabel.setBounds( ( panelSize.width / 20 ) * 19 - 100, panelSize.height / 20, 250, 25 );

		mainPanel.add( routeLabel );
		mainPanel.add( flightIDPanel );
		mainPanel.add( planeTypeLabel );

		SeatButton seatButton;

		Passenger[] passengers;
		Dimension passengerSeatPosition;

		// Initialize seats for the current Reservation

		if( currentReservation != null )
		{
			passengers = currentReservation.getPassengers();

			if( passengers != null )
			{
				int count = 0;

				for( Passenger passenger : passengers )
				{
					if( passenger != null && passenger.getSeat() != null )
					{
						// If a passengers seat is not null we initialize its seat button.

						// We get the seat and its position through the passenger.
						passengerSeatPosition = passenger.getSeat().getPosition();

						// We set the position to use to the seats position
						int x = passengerSeatPosition.width;
						int y = passengerSeatPosition.height;

						// We initialize the seat button and save its returned value.
						seatButton = InitializeSeatButton( x, y, mainPanel );

						// Since a passengers seat will always be booked we set the seats booking information to be true.
						seatButton.setBooked( true );

						// We assign an index to the seat button so that it knows which passenger it belongs to.
						seatButton.setMyIndex( count );

						count += 1;
					}
				}
			}
		}

		// Initialize seats for any reservation already booked
		Reservation[] reservations = flight.getReservations();

		if( reservations != null )
		{
			for( int i = 0; i < reservations.length; i++ )
			{
				passengers = reservations[i].getPassengers();

				for( Passenger passenger : passengers )
				{
					// We initialize any seat button from any reservation associated with the flight, that has not already been initialized.
					passengerSeatPosition = passenger.getSeat().getPosition();

					int x = passengerSeatPosition.width;
					int y = passengerSeatPosition.height;

					if( seatButtonArray[x][y] == null )
					{
						seatButton = InitializeSeatButton( x, y, mainPanel );

						// We know that any reserved seat is actually booked, so we set the booking status to be true.
						seatButton.setBooked( true );
					}
				}
			}
		}

		// We initialize any seat button that is has not already been initialized.
		// All these seats are not occupied.
		for( int i = 0; i < seatButtonArray.length; i++ )
		{
			for( int j = 0; j < seatButtonArray[i].length; j++ )
			{
				if( seatButtonArray[i][j] == null )
				{
					InitializeSeatButton( i, j, mainPanel );
				}
			}
		}

		// We add the main panel to the frame.
		add( mainPanel );

		// We make sure that any seats are up to date.
		updateSeats();
	}

	/**
	 * Initialize a seat button.
	 * 
	 * @param The
	 *            x position that the button shall be initialized on
	 * @param The
	 *            y position that the button shall be initialized on
	 * @param The
	 *            panel for which the seat button shall be attatched.
	 * @return we return the seatbutton in order to access the seatbutton later.
	 */
	SeatButton InitializeSeatButton( int x, int y, JPanel mainPanel )
	{
		SeatButton seatButton = new SeatButton( flight.getSeat( x, y ), currentReservation );

		seatButtonArray[x][y] = seatButton;

		Dimension size = new Dimension( squaredSize, squaredSize );

		// Based on the calculated preferences for each button, we set the size and position of the button.
		// We do this through absolute position.
		seatButton.setBounds( leftPadding + y * ( squaredSize + indent ), topPadding + x * ( squaredSize + indent ), squaredSize, squaredSize );

		// We add the button to the main panel.
		mainPanel.add( seatButton );

		return seatButton;
	}

	/**
	 * We iterate over every seat associated with the FlightPanel and update it.
	 */
	public void updateSeats()
	{
		for( int i = 0; i < seatButtonArray.length; i++ )
		{
			for( int j = 0; j < seatButtonArray[i].length; j++ )
			{
				// We make sure that the seats has the current reservation selected
				seatButtonArray[i][j].setCurrentReservation( currentReservation );

				// We set the buttons editable status to be the current editable status of the FlightPanel.
				seatButtonArray[i][j].setButtonEnabled( editable );

				// We reverse all seats to a non-booked state.
				seatButtonArray[i][j].setBooked( false );
			}
		}

		Passenger[] passengers;
		Dimension passengerSeatPosition;

		// Initialize seats for any reservation already booked
		Reservation[] reservations = flight.getReservations();

		if( reservations != null )
		{
			// We iterate through all our reservations, in order to rebook their individual seats.
			for( int i = 0; i < reservations.length; i++ )
			{
				if( currentReservation == null )
				{
					// If the current reservation is null, we simply iterate through all reservations.

					passengers = reservations[i].getPassengers();

					for( Passenger passenger : passengers )
					{
						passengerSeatPosition = passenger.getSeat().getPosition();

						int x = passengerSeatPosition.width;
						int y = passengerSeatPosition.height;

						seatButtonArray[x][y].setBooked( true );
					}
				}
				else if( currentReservation.getCurrentFlightReservationIndex() != i )
				{
					// If the current reservation is not null, we iterate through all reservations
					// except the one that is also the current.

					passengers = reservations[i].getPassengers();

					for( Passenger passenger : passengers )
					{
						passengerSeatPosition = passenger.getSeat().getPosition();

						int x = passengerSeatPosition.width;
						int y = passengerSeatPosition.height;

						seatButtonArray[x][y].setBooked( true );
					}
				}
			}
		}

		if( currentReservation != null )
		{
			passengers = currentReservation.getPassengers();

			// We iterate through the passengers of the current reservation, and book any seat associated
			if( passengers != null )
			{
				for( Passenger passenger : passengers )
				{
					if( passenger != null && passenger.getSeat() != null )
					{
						passengerSeatPosition = passenger.getSeat().getPosition();

						int x = passengerSeatPosition.width;
						int y = passengerSeatPosition.height;

						seatButtonArray[x][y].setBooked( true );
					}
				}
			}
		}

		// We finally update any seat associated with the flight panel, to make sure
		// that they are showing the correct status of booking.
		for( int i = 0; i < seatButtonArray.length; i++ )
		{
			for( int j = 0; j < seatButtonArray[i].length; j++ )
			{
				seatButtonArray[i][j].update();
			}
		}
	}

	/**
	 * We calculate how we want to scale each individual component of the Container, based on the size.
	 */
	void setupProperties()
	{
		Seat[][] seatArray = plane.getSeatArray();

		// We calculate a scale modifer based on the total amount of seats associated with the Flight Panel
		// in order to fit any number of seats inside the same amount of space.
		scaleModifier = Math.sqrt( ( seatArray.length * seatArray[0].length ) / 30f );

		// We calculate a general panelToButtonScale.
		panelToButtonScale = panelSize.height / 10;

		// We calculate the squared size that each button should have.
		squaredSize = ( int ) ( panelToButtonScale / scaleModifier );

		// We calculate the space between every button.
		indent = ( int ) ( 10 / scaleModifier );

		// We calculate the amount of padding should be added, before the first button is added.
		leftPadding = panelSize.width / 2 - ( ( squaredSize + indent ) * seatArray[0].length ) / 2;
		topPadding = panelSize.height / 2 - ( ( squaredSize + indent ) * seatArray.length ) / 2;

		// We initialize our seatButtonArray according to the size of the seat array, had by the flight.
		seatButtonArray = new SeatButton[seatArray.length][seatArray[0].length];
	}

	/**
	 * setEditable reverses the editabilty of the FlightPanel.
	 */
	void setEditable()
	{
		editable = !editable;

		// To make sure any data is up to date when we change editability, we update our seats.
		updateSeats();
	}

	/**
	 * We use this method to set the reservation of the FlightPanel
	 * 
	 * @param the
	 *            new reservation
	 */
	public void setCurrentReservation( Reservation currentReservation )
	{
		this.currentReservation = currentReservation;
	}
}
