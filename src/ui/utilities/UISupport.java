package ui.utilities;


import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import ui.swing.material.GUITheme;


/**
 * @author francesco
 *
 */
public class UISupport {


	public static ImageIcon loadImage(String path) {
		URL url = UISupport.class.getClass().getResource(path);
		return new ImageIcon(url);
	}
	
	public static ImageIcon loadImageWithSize(String path, int height, int width) {
		return new ImageIcon( loadImage(path).getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH) );  
	}

	public static void configureUI() {
		UIManager.put ("Button.font", GUITheme.LIGHT_THEME.getBold ());
		UIManager.put("RadioButton.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("CheckBox.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("ComboBox.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("Label.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("MenuBar.font", GUITheme.LIGHT_THEME.getBold ());
		UIManager.put("MenuItem.font", GUITheme.LIGHT_THEME.getRegular ());
		UIManager.put("Menu.font", GUITheme.LIGHT_THEME.getBold ());
		UIManager.put("OptionPane.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("Panel.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("ScrollPane.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("Table.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("TableHeader.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("TextField.font", GUITheme.LIGHT_THEME.getLight ());
		UIManager.put("TextArea.font", GUITheme.LIGHT_THEME.getRegular ());
		UIManager.put ("Panel.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put ("Panel.border", BorderFactory.createEmptyBorder ());
		UIManager.put ("MenuItem.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put ("MenuItem.border", BorderFactory.createEmptyBorder ());
		UIManager.put ("MenuItem.disabledForeground", GUITheme.LIGHT_THEME.getMenuDisabledText ());
		UIManager.put ("MenuItem.selectionBackground", GUITheme.LIGHT_THEME.getMenuSelectionBackground ());
		UIManager.put("MenuItem.selectionForeground", GUITheme.LIGHT_THEME.getMenuSelectionText ());
		UIManager.put ("MenuItem.foreground", GUITheme.LIGHT_THEME.getMenuSelectionText ());
		UIManager.put("PopupMenu.border", BorderFactory.createLineBorder (GUITheme.LIGHT_THEME.getMenuSelectionBackground (), 1));
		UIManager.put("PopupMenu.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put("Menu.border", BorderFactory.createEmptyBorder ());
		UIManager.put("Menu.selectionBackground", GUITheme.LIGHT_THEME.getMenuSelectionBackground ());
		UIManager.put("Menu.selectionForeground", GUITheme.LIGHT_THEME.getMenuSelectionText ());
		UIManager.put("Menu.disabledForeground", GUITheme.LIGHT_THEME.getMenuDisabledText ());
		UIManager.put ("Menu.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put("Menu.foreground", GUITheme.LIGHT_THEME.getMenuSelectionText ());
		UIManager.put("Menu.opaque", true);
		UIManager.put("Menu.menuPopupOffsetY", 10);  
		UIManager.put("MenuBar.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put ("MenuBar.border", GUITheme.LIGHT_THEME.getMenuBorder ());
		UIManager.put ("SplitPane.border", BorderFactory.createEmptyBorder ());
		UIManager.put ("SplitPane.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put ("SplitPane.dividerSize", 5);
		UIManager.put ("SplitPaneDivider.border", BorderFactory.createEmptyBorder ());
		UIManager.put ("ScrollPane.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put ("ScrollPane.border", BorderFactory.createEmptyBorder ());
		UIManager.put ("TextArea.background", GUITheme.LIGHT_THEME.getInactiveTextbox ());
		UIManager.put ("TextArea.border", BorderFactory.createEmptyBorder ());
		UIManager.put ("TextArea.foreground", GUITheme.LIGHT_THEME.getTextboxText ());
		UIManager.put ("OptionPane.background", GUITheme.LIGHT_THEME.getCard ());
		UIManager.put ("OptionPane.border", GUITheme.LIGHT_THEME.getDefaultBorder ());
		UIManager.put ("Button.background", GUITheme.LIGHT_THEME.getInactiveBorderlessButtonBackground ());
		UIManager.put ("Button.foreground", GUITheme.LIGHT_THEME.getBorderlessButtonText ());
		UIManager.put ("Button.highlight", GUITheme.LIGHT_THEME.getActiveBorderlessButtonBackground ());
		UIManager.put ("Button.border", GUITheme.LIGHT_THEME.getDefaultBorder ()); 
	}


}
