import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JFrame {

	public MainMenu()
	{
		setFrame();
		makeContent();
		this.setVisible(true);
		//this.pack();

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
				System.out.println("New Reservation/Find flight");
				//new FindFlightWindow();
				CustomerWindow.getInstance();
				break;

			case "FindRes":
				System.out.println("Find reservation/customer");
				new FindWindow();
				break;
				
			case "Exit":
				System.out.println("Exit program");
				System.exit(EXIT_ON_CLOSE);
				break;
			}
		}
	}


	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(220, 170);
		this.setResizable(false);
		this.setTitle("Main menu");
	}

	private void makeContent()
	{
		Container contentPane = this.getContentPane();    
		this.setLayout(new BorderLayout());

		//Laver et JPanel kaldet buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER,5,15));

		//Laver knapper
		JButton bNewResFindFlight = new JButton("New reservation/Find flight");
		JButton bFindResCus = new JButton("Find reservation or customer");
		JButton bExit = new JButton("Exit program");

		//Tilføjer listeners til knapperne
		ButtonListener listener = new ButtonListener();

		bNewResFindFlight.addActionListener(listener);
		bNewResFindFlight.setActionCommand("NewRes");

		bFindResCus.addActionListener(listener);
		bFindResCus.setActionCommand("FindRes");

		bExit.addActionListener(listener);
		bExit.setActionCommand("Exit");

		//Tilføjer knapperne til panelet buttons
		buttons.add(bNewResFindFlight);
		buttons.add(bFindResCus);
		buttons.add(bExit);        

		contentPane.add(buttons, BorderLayout.CENTER);
	}
}
