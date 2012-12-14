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
import MainPackage.MainMenu.ButtonListener;
import MainPackage.Plane.PlaneType;

public class DatabaseAddMenu extends JFrame
{
	JPanel mainPanel;
	
	Random random = new Random();
	
	String[] firstNames = new String[]{"Hans", "Grethe", "Leo", "Martin", "Jesper", "Jonas", "Han", "Luke", "Darth", "Mathias", "Gurlig"};
	String[] surNames = new String[]{"Hansen", "Kastberg", "Leonhardt", "Martinsen", "Jespersen", "Jonassen", "Hansi", "Lukesen", "Darthsen", "Mathiassen", "Solo", "Vader", "Skywalker"};
	String[] gender = new String[]{"Male", "Female", "Unknown"};
	String[] countries = new String[]{"Denmark", "Somalia", "Germany", "USA", "Japan", "Uganda", "Uranus", "Alderaan"};
	String[] nationalities = new String[]{"Denmark", "Somalia", "Germany", "USA", "Japan", "Uganda", "Uranus", "Alderaan"};
	String[] adress = new String[]{"Rådhuspladsen nr. 1, 9000 København", "Nederenvej 1337, 2650 Hvidovre", "Platanvej 42, 1650 Vesterbronx", "Gadenavn n, 0000 place"};
	
	
	
	
	public DatabaseAddMenu()
	{
		setupFrame();
		
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "Add Person": 	
				Database.getInstance().Add(new Person((String)(rand(firstNames)), (String)(rand(surNames)), (String)(rand(gender)), "", (String)(rand(countries)), (String)(rand(nationalities)), (String)(rand(adress)), ""+(random.nextInt(80000000)+10000000), ""+random.nextInt(), random.nextInt()));
				System.out.println("Person Added");
				break;

			case "Add Flight":
				//Defining Random Calendar
				Calendar calendarToUse = Calendar.getInstance();
				calendarToUse.set(2012, random.nextInt(12)+1, random.nextInt(28)+1);
				
				//Defining Random Airports
				ArrayList<AirportType> airportTypeArray = new ArrayList( Arrays.asList(Airport.getAirportTypes()));
				
				int randInt = random.nextInt(airportTypeArray.size());
				Airport airport1 = new Airport(airportTypeArray.get(randInt));
				airportTypeArray.remove(randInt);
				
				randInt = random.nextInt(airportTypeArray.size());
				Airport airport2 = new Airport(airportTypeArray.get(randInt));
				
				int airport2Index = random.nextInt(Airport.getAirportTypes().length);
				
				Database.getInstance().Add(new Flight(calendarToUse,
						new Plane(Plane.planeTypes()[random.nextInt(Plane.planeTypes().length)]),
						airport1,
						airport2,
						random.nextInt()));
				System.out.println("Flight Added");
				break;
				
			case "Delete Database":
				Database.getInstance().executeQuery("DELETE FROM person");
				Database.getInstance().executeQuery("DELETE FROM passenger");
				Database.getInstance().executeQuery("DELETE FROM flight");
				System.out.println("Database Deleted!");
				break;
			}
		}
	}
	
	void setupFrame()
	{
		this.setSize(250, 300);
		this.setResizable(false);
		this.setTitle("Database");
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	
	void makeContent()
	{
		Container contentPane = this.getContentPane();
		
		//Main Panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
			//Add Person Button
		JButton addPersonButton = new JButton("Add Person");
		addPersonButton.setActionCommand("Add Person");
		
		addPersonButton.setAlignmentX(CENTER_ALIGNMENT);
		
			//Add Flight Button
		JButton addFlightButton = new JButton("Add Flight");
		addFlightButton.setActionCommand("Add Flight");
		
		addFlightButton.setAlignmentX(CENTER_ALIGNMENT);
		
			//Delete Database Button
		JButton deleteDatabaseButton = new JButton("Delete Database");
		deleteDatabaseButton.setActionCommand("Delete Database");
		
		deleteDatabaseButton.setAlignmentX(CENTER_ALIGNMENT);
		
		
		//Main Panel Finish Up
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(addPersonButton);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(addFlightButton);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(deleteDatabaseButton);
		mainPanel.add(Box.createVerticalGlue());
		//Main Panel Finished
		
		ButtonListener listener = new ButtonListener();

		addPersonButton.addActionListener(listener);
		addFlightButton.addActionListener(listener);
		deleteDatabaseButton.addActionListener(listener);
		
		contentPane.add(mainPanel);
	}
	
	Object rand(Object[] array)
	{
		Object returnObject;
		
		returnObject = array[random.nextInt(array.length)];
		
		return returnObject;
	}
}
