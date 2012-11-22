import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {


	public MainMenu()
	{
		setFrame();
		makeContent();
		this.setVisible(true);
		//this.pack();
		
		//Spawner rammen midt p� sk�rmen
		this.setLocationRelativeTo(null);
	}
	
	class NewReservationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("New reservation");
			//�bn CustomerWindow
		}
	}
	
	class FindReservationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("Find reservation");
			//�bn Find...Window
		}
	}
	
	class FindCustomerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("Find customer");
			//�bn CustomerWindow
		}
	}
	
	class FindFlightListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("Find flight");
			//�bn FlightWindow
		}
	}
	
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(200, 250);
		//this.setResizable(false);
		this.setTitle("Main menu");
	}
	
	private void makeContent()
	{
        Container contentPane = this.getContentPane();    
        this.setLayout(new BorderLayout());
        
        //Laver et JPanel kaldet buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(1,15,15));

        //Laver knapper
        JButton bNewReservation = new JButton("New reservation");
        JButton bFindReservation = new JButton("Find reservation");
        JButton bFindCustomer = new JButton("Find customer");
        JButton bFindFlight = new JButton("Find flight");
        
        //Tilf�jer listeners til knapperne
        bNewReservation.addActionListener(new NewReservationListener());
        bFindReservation.addActionListener(new FindReservationListener());
        bFindCustomer.addActionListener(new FindCustomerListener());
        bFindFlight.addActionListener(new FindFlightListener());
        
        //Tilf�jer knapperne til panelet buttons
        buttons.add(bNewReservation);
        buttons.add(bFindReservation);
        buttons.add(bFindCustomer);
        buttons.add(bFindFlight);
        
        
        contentPane.add(buttons, BorderLayout.CENTER);
	}



}
