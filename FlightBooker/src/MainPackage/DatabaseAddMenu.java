package MainPackage;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import MainPackage.Airport.AirportType;

/**
 * The DatabaseAddMenu class is the class that manages our Database menu in the system. It allows the user to add valid and useful data to the
 * database for testing purposes only.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
public class DatabaseAddMenu extends JFrame
{
	JPanel mainPanel;

	// Create a random object so we can get some pseudo-random integers.
	Random random = new Random();

	// A buttload of string arrays so we can randomly generate people with some (real) data.
	String[] firstNames = new String[] { "Hans", "Grethe", "Leo", "Martin", "Jesper", "Jonas", "Han", "Luke", "Darth", "Mathias", "Gurlig" };
	String[] surNames = new String[] { "Hansen", "Kastberg", "Leonhardt", "Martinsen", "Jespersen", "Jonassen", "Hansi", "Lukesen", "Darthsen",
	        "Mathiassen", "Solo", "Vader", "Skywalker" };
				 //^ Speaking of butts... Here's one.
	String[] gender = new String[] { "Male", "Female", "Unknown" };
	String[] countries = new String[] { "Denmark", "Somalia", "Germany", "USA", "Japan", "Uganda", "Uranus", "Alderaan" };
	String[] nationalities = new String[] { "Denmark", "Somalia", "Germany", "USA", "Japan", "Uganda", "Uranus", "Alderaan" };
	String[] adress = new String[] { "Rådhuspladsen nr. 1, 9000 København", "Nederenvej 1337, 2650 Hvidovre", "Platanvej 42, 1650 Vesterbronx",
	        "Gadenavn n, 0000 place" };

	public DatabaseAddMenu()
	{
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
		boolean withPassengers = false;

		public ButtonListener( boolean withPassengers )
		{
			this.withPassengers = withPassengers;
		}

		/**
		 * When any action is fired on a component that has this listener added, this function is called.
		 */
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Add Person":
					// Add a person to the database with random generated information.
					Database.getInstance().Add(
					        new Person( ( String ) ( rand( firstNames ) ), ( String ) ( rand( surNames ) ), ( String ) ( rand( gender ) ),
					                calcBirthDate(), ( String ) ( rand( countries ) ), ( String ) ( rand( nationalities ) ),
					                ( String ) ( rand( adress ) ), "" + ( random.nextInt( 80000000 ) + 10000000 ), "" + random.nextInt(), Database
					                        .getInstance().GetID( Person.class ) ) );
					System.out.println( "Person Added" );
				break;

				case "Add Flight":
					// Defining Random Calendar
					Calendar calendarToUse = Calendar.getInstance();
					calendarToUse.set( 2012, random.nextInt( 12 ) + 1, random.nextInt( 28 ) + 1 );

					// Defining a list of Airports from which to choose a random
					ArrayList<AirportType> airportTypeArray = new ArrayList( Arrays.asList( Airport.getAirportTypes() ) );

					//Defining the random departure airport
					int randInt = random.nextInt( airportTypeArray.size() );
					Airport airport1 = new Airport( airportTypeArray.get( randInt ) );
					//We need to remove the airport from the array so we don't get a flight having the same departure and destination airport.
					airportTypeArray.remove( randInt );

					//Defining the random destination airport
					randInt = random.nextInt( airportTypeArray.size() );
					Airport airport2 = new Airport( airportTypeArray.get( randInt ) );

					//Here we initialize the flight for us to upload
					Flight newFlight = new Flight( calendarToUse, new Plane( Plane.planeTypes()[random.nextInt( Plane.planeTypes().length )] ),
					        airport1, airport2, Database.getInstance().GetID( Flight.class ) );

					//If we press the /With passengers we generate a series of reservations
					//for it to use.
					//Otherwise we leave it empty.
					if( withPassengers )
					{
						//We fetch the all available persons from the database, in order to avoid calling it too many times.
						ArrayList<Person> persons = Database.getInstance().Get(Person.class);
						
						//First we generate a random amount of reservations
						int randReservationAmount = random.nextInt( 3 );
						
						//We initialize an array for us to add the seats we wish to randomize between
						ArrayList<Seat> availableSeats = new ArrayList<>();
						
						//We access the seat array of the current flight, in order to randomize between the correct amount of seats in our reservations
						Seat[][] seatArray = newFlight.getSeats();

						for( int i = 0; i < seatArray.length; i++ )
						{
							for( int j = 0; j < seatArray[i].length; j++ )
							{
								availableSeats.add( seatArray[i][j] );
							}
						}
						
						//We iterate over the amount of reservations we want to add
						for( int i = 0; i < randReservationAmount; i++ )
						{
							//We initialize the reservation
							Reservation reservation = new Reservation();
							
							//We generate a random amount of passengers for the current reservation, insuring we always have atleast 1
							int randPassengerAmount = random.nextInt( 4 ) + 1;
							
							//We initialize the array of passengers to add to our reservation
							Passenger[] newPassengers = new Passenger[randPassengerAmount];

							//We iterate over the amount of passengers we want to add to the current reeservation
							for( int j = 0; j < randPassengerAmount; j++ )
							{
								//We get a randomPerson
								int personRand = random.nextInt(persons.size());

								//We get the random person from our person array.
								Person person = persons.get(personRand);

								//We get a random integer to access the random seat we want.
								int seatRand = random.nextInt( availableSeats.size() );
								
								//We get the random seat from our seat array.
								Seat seat = availableSeats.get( seatRand );
								
								//We remove the seat we randomed, so that we do not place two passengers on the same seat.
								availableSeats.remove( seatRand );

								//We initialize the new passenger that we want to add to our reservation.
								Passenger passenger = new Passenger( person, seat );
								
								//If this is the first passenger we add, we insure that the owner of the reservation is also this person.
								if( j == 0 )
								{
									reservation.setOwner( person );
								}

								//We put the new passenger into our passenger array.
								newPassengers[j] = passenger;
							}
							
							//We set the passengers of the reservation to be our generated passenger array.
							reservation.setPassengers( newPassengers );

							//We set the flight of the reservation to be the flight we wish to upload.
							reservation.setFlight( newFlight );

							//We set the reserved date of the reservation to be the same as of the flights lift off date.
							reservation.setReservedDate( calendarToUse );
							
							//We set the accessing index of the reservation, so we can access it later through the flight.
							reservation.setCurrentFlightReservationIndex( i );

							//We wish to add the newly initialized reservation to the owners array of reservations.
							//Thus we can access it for later use.
							
							//We make sure that the reservations of the owner is up to date with the database.
							reservation.getOwner().updateReservations();
							
							//We get the current reservation array of the owner.
							Reservation[] newReservations = reservation.getOwner().getReservations();

							//If there is no reservations so far we initialize the reservation array.
							if( newReservations == null )
								newReservations = new Reservation[1];
							else
							{
								//If there are already reservations in the array, we make a new array with the same data
								//but which is 1 index longer.
								newReservations = Arrays.copyOf( newReservations,
								        reservation.getOwner().getReservations().length + 1 );
							}
							
							//We add the new reservation to our reservation array.
							newReservations[newReservations.length - 1] = reservation;
							
							//We assign the new reservation array to that of the owner.
							reservation.getOwner().setReservations( newReservations );
							
							//We set the id of the reservation to the next available database index.
							reservation.setID( Database.getInstance().GetID( Reservation.class ) );
							
							//We replace the owner in the database with the updated owner.
							//We access the id at which we want to replace through the owner of the reservation.
							Database.getInstance().Replace( reservation.getOwner().getID(),
							        reservation.getOwner() );
							
							//We add the reservation to our database
							Database.getInstance().Add( reservation );

							//We add the reservation to the flights array of reservations.
							newFlight.addReservation( reservation );
						}
					}
					
					//We add the flight to our database.
					Database.getInstance().Add( newFlight );
				break;
				
				case "Delete Database":
					//We delete the whole database.
					Database.getInstance().executeQuery( "DELETE FROM person" );
					Database.getInstance().executeQuery( "DELETE FROM passenger" );
					Database.getInstance().executeQuery( "DELETE FROM flight" );
					Database.getInstance().executeQuery( "DELETE FROM reservation" );

					System.out.println( "Database Deleted!" );
				break;
			}
		}
	}

	void setupFrame()
	{
		this.setSize( 250, 300 );
		this.setResizable( false );
		this.setTitle( "Database" );
		this.setLocationRelativeTo( null );
		this.setVisible( true );
	}

	void makeContent()
	{
		Container contentPane = this.getContentPane();

		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.Y_AXIS ) );

			// Add Person Button
		JButton addPersonButton = new JButton( "Add Person" );
		addPersonButton.setActionCommand( "Add Person" );

		addPersonButton.setAlignmentX( CENTER_ALIGNMENT );

			// Add Flight Button
		JButton addFlightButton = new JButton( "Add Flight With Passengers" );
		addFlightButton.setActionCommand( "Add Flight" );

		addFlightButton.setAlignmentX( CENTER_ALIGNMENT );

			// Add Flight Without Button
		JButton addFlightWithoutButton = new JButton( "Add Flight Without Passengers" );
		addFlightWithoutButton.setActionCommand( "Add Flight" );

		addFlightWithoutButton.setAlignmentX( CENTER_ALIGNMENT );

			// Delete Database Button
		JButton deleteDatabaseButton = new JButton( "Delete Database" );
		deleteDatabaseButton.setActionCommand( "Delete Database" );

		deleteDatabaseButton.setAlignmentX( CENTER_ALIGNMENT );

		// Main Panel Finish Up
		mainPanel.add( Box.createVerticalGlue() );
		mainPanel.add( addPersonButton );
		mainPanel.add( Box.createVerticalGlue() );
		mainPanel.add( addFlightButton );
		mainPanel.add( Box.createVerticalGlue() );
		mainPanel.add( addFlightWithoutButton );
		mainPanel.add( Box.createVerticalGlue() );
		mainPanel.add( deleteDatabaseButton );
		mainPanel.add( Box.createVerticalGlue() );
		// Main Panel Finished

		//We initialize a buttonlistener for us to use.
		ButtonListener listener = new ButtonListener( false );
		
		//We add the button listener to the different buttons.
		addPersonButton.addActionListener( listener );
		addFlightButton.addActionListener( new ButtonListener( true ) );
		addFlightWithoutButton.addActionListener( new ButtonListener( false ) );
		deleteDatabaseButton.addActionListener( listener );

		contentPane.add( mainPanel );
	}

	/**
	 * We use this method to return a random object in the array we passed.
	 * 
	 * @param Object array to get a random object from
	 * @return the randomed object.
	 */
	Object rand( Object[] array )
	{
		Object returnObject;

		returnObject = array[random.nextInt( array.length )];

		return returnObject;
	}

	/**
	 *  We calculate the birth date of the generated passenger.
	 *  
	 * @return The calculated birth date.
	 */
	String calcBirthDate()
	{
		String finishedString = "";

		int randInt = random.nextInt( 28 ) + 1;
		if( randInt < 10 )
			finishedString += "0" + randInt;
		else
			finishedString += randInt;

		randInt = random.nextInt( 12 ) + 1;
		if( randInt < 10 )
			finishedString += "0" + randInt;
		else
			finishedString += randInt;

		finishedString += ( random.nextInt( 74 ) + 1920 );

		return finishedString;
	}
}
