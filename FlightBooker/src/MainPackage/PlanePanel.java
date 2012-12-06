package MainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.*;

import MainPackage.Plane.PlaneType;

public class PlanePanel extends JPanel 
{
	
	Plane plane;
	
	public PlanePanel(Plane plane)
	{
		this.plane = plane;
		
		makeContent();
	}
	
	void makeContent()
	{
		JPanel mainPanel;
		
		try
		{
			mainPanel = new JPanelWithBackground("/images/"+plane.getPlaneTypeString()+".jpg");
			
			mainPanel.setPreferredSize(new Dimension(400, 100));
		}
		catch(IOException e)
		{
			mainPanel = new JPanel();
		}
		
		
		
		add(mainPanel, BorderLayout.CENTER);
		
	}
}
