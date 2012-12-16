package MainPackage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * MainMenu creates the first window of the application.
 * The window contains a menu consisting of three buttons: "New Reservation", "Find" and "Database".
 * 
 * Clicking "New reservation" opens a new menu, through which the user can make a new reservation.
 * Clicking "Find" opens a new menu, through which the user can find reservations, persons and flights.
 * Clicking "Database" opens a new menu, through which the user can interact with database.
 * 
 * @author Martin Juul Pedersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0 
 * 
 */
public class MainMenu extends JFrame {

	private Dimension buttonSize;

	public MainMenu()
	{
		setupFrame();
		makeContent();
		
	}

	/*
	 * ButtonListener makes it possible to do something, when a button is clicked.
	 * It is notified when a button with an added ButtonListener is clicked, and the button's ActionCommand decides what action to take.
	 * 
	 */
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "New Reservation": 	
				new FlightManagerMenu(new JFrame(), null, true);
				break;

			case "Find":
				new FindMenu();
				break;
				
			case "Database":
				new DatabaseAddMenu();
				break;

			case "Exit":
				exitDialog();
				break;
			}
		}
	}

	/**
	 * setupFrame sets the frames properties: 
	 * The size of the frame, whether the frame is resizable or not, the title of the frame, where on the screen, the frame should appear and
	 * what to do, when user tries to close the frame
	 * 
	 */
	private void setupFrame()
	{
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(250, 300);
		this.setResizable(false);
		this.setTitle("Main menu");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
 
		buttonSize = new Dimension(250,50);

		//Tilføjer listener til rammen, der tjekker om vinduet lukkes
		this.addWindowListener( 
				new WindowAdapter() { 
					public void windowClosing(WindowEvent e) {
						exitDialog(); 
					}
				} 
				);
	}

	/**
	 * makeContent creates and places all of the menu's content.
	 * 
	 */
	private void makeContent()
	{
		Container contentPane = this.getContentPane();

		//New Reservation Button
		JButton newReservationButton = new JButton("New Reservation");
		newReservationButton.setActionCommand("New Reservation");
		newReservationButton.setPreferredSize(buttonSize);
		newReservationButton.setAlignmentX(CENTER_ALIGNMENT);

		//Find Button
		JButton findButton = new JButton("Find...");
		findButton.setActionCommand("Find");
		findButton.setPreferredSize(buttonSize);
		findButton.setAlignmentX(CENTER_ALIGNMENT);

		//Database Button
		JButton databaseButton = new JButton("Database");
		databaseButton.setActionCommand("Database");
		databaseButton.setPreferredSize(buttonSize);
		databaseButton.setAlignmentX(CENTER_ALIGNMENT);

		//Exit Button
		JButton exitButton = new JButton("Exit");
		exitButton.setActionCommand("Exit");
		exitButton.setPreferredSize(buttonSize);
		exitButton.setAlignmentX(CENTER_ALIGNMENT);

		//Adds a button listener to every single button
		ButtonListener listener = new ButtonListener();

		newReservationButton.addActionListener(listener);
		findButton.addActionListener(listener);
		databaseButton.addActionListener(listener);
		exitButton.addActionListener(listener);

		//Creates a panel for the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));	
		
		//Add buttons to the buttonPanel
		buttonPanel.add(Box.createRigidArea(new Dimension(0,20)));
		buttonPanel.add(newReservationButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,20)));
		buttonPanel.add(findButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,20)));
		buttonPanel.add(databaseButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,40)));
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,20)));

		//Add everything to the contentPane
		contentPane.add(buttonPanel);
		
		this.setVisible(true);
		 
	}

	/**
	 * If any another windows than MainMenu is open, exitDialog creates a dialog to warn the user about possible loss of information.
	 * If no other windows are open, the window is closed without prompting the user.
	 */
	private void exitDialog()
	{

		Window[] allWindows = Window.getWindows();
		int openWindows = 0;
		for(Window currentWindow : allWindows)
			if(currentWindow.isVisible())
				openWindows++;

		if(openWindows == 1)
			System.exit(0);
		else {
			int n = JOptionPane.showConfirmDialog(this.getContentPane(), "This program will now close. Any work in progress will be discarded. Exit?",
					"Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			//If "Yes" is chosen
			if(n == 0){
				System.exit(0);
			}
		}

	}
}
