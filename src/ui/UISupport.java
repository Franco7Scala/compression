package ui;


import java.net.URL;
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
	
	
}
