import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ReservationInfoWindow extends JFrame {
	
	private static ReservationInfoWindow instance = null;
	private JButton bCancel;
	
	private ReservationInfoWindow(/*reservationID*/)
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
			case "Edit": 	
				System.out.println("Edit reservation");
				//KODE der gør det muligt at ændre i boksene
				bCancel.setEnabled(true);
				EditReservation();
				break;
				
			case "OK":
				System.out.println("OK");
				//Hvis den ikke er blevet ændret
				close();				
				//Hvis den er blevet ændret, skal ændringerne gemmes og sendes til databasen
				//KODE
				break;
				
			case "Cancel":
				System.out.println("Cancel");
				
				//Skal være greyed out, medmindre edit er trykket.
				
				bCancel.setEnabled(false);
				
			}
		}
	}
	
	
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200, 250);
		this.setResizable(false);
		this.setTitle("Reservation: " /* Name and id */);
		
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
		JButton bEdit = new JButton("Edit reservation");
		JButton bOK = new JButton("OK");
		bCancel = new JButton("Cancel");

		//Tilføjer listeners til knapperne
		ButtonListener listener = new ButtonListener();
		
		bEdit.addActionListener(listener);
		bEdit.setActionCommand("Edit");
				
		bOK.addActionListener(listener);
		bOK.setActionCommand("OK");	
		
		bCancel.addActionListener(listener);
		bCancel.setActionCommand("Cancel");	
		bCancel.setEnabled(false);

		//Tilføjer knapperne til panelet buttons
		buttons.add(bEdit);
		buttons.add(bOK);  
		buttons.add(bCancel);

		contentPane.add(buttons, BorderLayout.CENTER);
	}
	
	/*
	 * Returnerer en instans af CustomerWindow, hvis et ikke allerede findes
	 */
	public static ReservationInfoWindow getInstance() {
		if(instance == null) {
			instance = new ReservationInfoWindow();
		}
		return instance;
	}

	/*
	 * Fjerner rammen og sætter instance til null
	 */
	private void close()
	{
		instance.dispose();
		instance = null;
	}
	
	private void EditReservation()
	{
	}

}
