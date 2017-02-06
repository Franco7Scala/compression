package ui;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * @author francesco
 *
 */
public class UISupport {

	
	public static ImageIcon loadImage(String path) {
		URL url = UISupport.class.getClass().getResource(path);
		return new ImageIcon(url);
	}
	
	public BufferedImage loadImageBuffered(String fileName){

	    BufferedImage buff = null;
	    try {
	        buff = ImageIO.read(getClass().getResourceAsStream(fileName));
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return null;
	    }
	    return buff;

	}
	
	
}
