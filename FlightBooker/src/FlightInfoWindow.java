import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class FlightInfoWindow extends JFrame {
	
	private FlightInfoWindow instance = null;

	private BufferedImage img; 
	
	public FlightInfoWindow(/* flight id */)
	{
		loadImage(img, "pictures/747-test.jpg");
		
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
		this.setSize(700, 120);
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
		
		this.setLayout(new FlowLayout());
		
		JPanel west = new JPanel();
		
		try{
			west = new JPanelWithBackground("/images/aww yeah2.png");
			
			west.setPreferredSize(new Dimension(100,100));
			
			west.paintComponents(west.getGraphics());
			
		}
		catch(IOException e){
		}
		
		//Laver et JPanel kaldet buttons
		JPanel buttons = new JPanel();
		
		//JPanel buttons = new JPanelWithBackground("/images/aww yeah.png");
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));

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
		
		JPanel east = new JPanel();
		
		try{
			east = new JPanelWithBackground("/images/aww yeah2.png");
			
			east.setPreferredSize(new Dimension(100,100));
			
			east.paintComponents(east.getGraphics());
			
		}
		catch(IOException e){
		}
		
		contentPane.add(west, FlowLayout.LEFT);
		
		contentPane.add(buttons, FlowLayout.CENTER);
		
		contentPane.add(east, FlowLayout.RIGHT);
	}
	
	public FlightInfoWindow getInstance() {
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
	
	private void loadImage(Image img, String path)
	{
		
		
	}

}
