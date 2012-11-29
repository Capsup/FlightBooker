package MainPackage;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainMenu extends JFrame {

	private Dimension buttonSize;
	
	public MainMenu()
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
			case "New Reservation": 	
				new NewReservationMenu(new JFrame());
				break;

			case "Find...":
				System.out.println("Find...");
				break;
				
			case "Exit":
				System.out.println("Exit program");
				System.exit(EXIT_ON_CLOSE);
				break;
			}
		}
	}
	
	private void setupFrame()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(250, 300);
		this.setResizable(false);
		this.setTitle("Main menu");
		this.setLocationRelativeTo(null);
		
		buttonSize = new Dimension(250,50);
	}
	
	private void makeContent()
	{
		Container contentPane = this.getContentPane();
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		//New Reservation Button
		JButton newReservationButton = new JButton("New Reservation");
		newReservationButton.setPreferredSize(buttonSize);
		newReservationButton.setAlignmentX(CENTER_ALIGNMENT);
		
		//Find Button
		JButton findButton = new JButton("Find...");
		findButton.setPreferredSize(buttonSize);
		findButton.setAlignmentX(CENTER_ALIGNMENT);
		
		//Adds a button listener to every single button
		ButtonListener listener = new ButtonListener();
	
		newReservationButton.addActionListener(listener);
		findButton.addActionListener(listener);
		
		//Add buttons
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(newReservationButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,50)));
		buttonPanel.add(findButton);
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		contentPane.add(buttonPanel);
		
		update();
	}
	
	private void update()
	{
		this.setVisible(true);
	}
}
