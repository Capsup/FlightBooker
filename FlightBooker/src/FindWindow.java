import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;

public class FindWindow extends JFrame {

	public FindWindow()
	{
		setFrame();
		makeContent();
		this.setVisible(true);
		
		//Spawner rammen midt på skærmen
		this.setLocationRelativeTo(null);
	}
	
	private void setFrame()
	{
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200, 250);
		this.setResizable(false);
		this.setTitle("Find...");
	}
	
	private void makeContent()
	{
		Container contentPane = this.getContentPane();    
		this.setLayout(new BorderLayout());
		
		//Laver textfields
		JTextField firstName = new JTextField("ReservationID");
		
		
		
	}
}
