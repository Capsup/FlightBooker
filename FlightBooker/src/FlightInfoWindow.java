import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FlightInfoWindow extends JFrame {
	
	private static FlightInfoWindow instance = null;

	public FlightInfoWindow(/* flight id */)
	{
		setFrame();
		makeContent();
		this.setVisible(true);

		//Spawner rammen midt på skærmen
		this.setLocationRelativeTo(null);
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			switch(event.getActionCommand())
			{
			case "NewRes": 	
				System.out.println("New reservation");
				CustomerWindow.getInstance();
				break;

			case "InspRes":
				System.out.println("Inspect reservations");
				break;
				
			case "OK":
				System.out.println("OK");
				close();
				break;
			}
		}
	}

	
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200, 250);
		this.setResizable(false);
		this.setTitle("Flight Info FLIGHTID");
		
		//Tilføjer listener til rammen, der tjekker om vinduet lukkes
		this.addWindowListener( 
				new WindowAdapter() { 
					public void windowClosing(WindowEvent e) { 
						close(); 
					} 
				} 
				);
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();    
		this.setLayout(new BorderLayout());

		//Laver et JPanel kaldet buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER,5,15));

		//Laver knapper
		JButton bNewReservation = new JButton("New reservation");
		JButton bInspectReservations = new JButton("Inspect reservations");
		JButton bOK = new JButton("OK");

		//Tilføjer listeners til knapperne
		ButtonListener listener = new ButtonListener();
		
		bNewReservation.addActionListener(listener);
		bNewReservation.setActionCommand("NewRes");
		
		bInspectReservations.addActionListener(listener);
		bInspectReservations.setActionCommand("InspRes");
		
		bOK.addActionListener(listener);
		bOK.setActionCommand("OK");	

		//Tilføjer knapperne til panelet buttons
		buttons.add(bNewReservation);
		buttons.add(bInspectReservations);
		buttons.add(bOK);        

		contentPane.add(buttons, BorderLayout.CENTER);
	}
	
	public static FlightInfoWindow getInstance() {
		if(instance == null) {
			instance = new FlightInfoWindow();
			
			return instance;
		}
		else
		{
			return null;
		}
		
		
	}
	
	
	/*
	 * Fjerner rammen og sætter instance til null
	 */
	private void close()
	{
		instance.dispose();
		instance = null;
	}

}
