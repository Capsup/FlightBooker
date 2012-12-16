package MainPackage;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JPanelWithBackground extends JPanel
{
	private BufferedImage backgroundImage;
	private Rectangle rect;

	public JPanelWithBackground( String fileName, Rectangle rect ) throws IOException
	{
		backgroundImage = ImageIO.read( getClass().getResource( fileName ) );

		this.rect = rect;
	}

	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		// Draw the background image.
		g.drawImage( backgroundImage, rect.x, rect.y, rect.width, rect.height, null );
	}
}
