package main;

import ui.UICompressor;

/**
 * @author francesco
 *
 */
public class MainCompressor {


	public static void main(String[] args) {
		try {
			new UICompressor().setVisible(true);
		} 
		catch (Exception e) {}
	}

	
}
