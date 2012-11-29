import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class FindFlightWindow extends JFrame {

	public FindFlightWindow()
	{
		setFrame();
		makeContent();
		this.setVisible(true);
		
		//Spawner rammen midt på skærmen
		this.setLocationRelativeTo(null);
	}
	
	/*
	 * Observerer om brugeren trykker på knappen New Reservation/Find flight
	 */
	class InspectFlightListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("Inspect flight");
			new FlightInfoWindow();
			
			//FlightInfoWindow.getInstance();
		}
	}
	
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200, 250);
		this.setResizable(false);
		this.setTitle("Find Flight");
	}
	
	private void makeContent()
	{
		Container contentPane = this.getContentPane();    
		this.setLayout(new BorderLayout());

		//Laver et JPanel kaldet buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER,5,15));

		//Laver knapper
		JButton bInspectFlight = new JButton("Inspect flight");
		//JButton bInspectReservations = new JButton("Inspect reservations");
		//JButton bOK = new JButton("OK");

		//Tilføjer listeners til knapperne
		bInspectFlight.addActionListener(new InspectFlightListener());
		//bInspectReservations.addActionListener(new InspectReservationsListener());
		//bOK.addActionListener(new OKListener());

		//Tilføjer knapperne til panelet buttons
		buttons.add(bInspectFlight);
		//buttons.add(bInspectReservations);
		//buttons.add(bOK);        

		contentPane.add(buttons, BorderLayout.CENTER);
	}
}
