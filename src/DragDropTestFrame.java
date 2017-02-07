import java.awt.BorderLayout;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import ui.utilities.FileDragDropListener;


public class DragDropTestFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private List<File> files;

	public static void main(String[] args) {

	    // Create our frame
	    new DragDropTestFrame();
	    
	}

	public DragDropTestFrame() {

	    // Set the frame title
	    super("Drag and drop test");

	    // Set the size
	    this.setSize(250, 150);

	    // Create the label
	    JLabel myLabel = new JLabel("Drag something here!", SwingConstants.CENTER);

	    // Create the drag and drop listener
	    FileDragDropListener myDragDropListener = new FileDragDropListener();
	    files = myDragDropListener.getFiles();

	    // Connect the label with a drag and drop listener
	    new DropTarget(myLabel, myDragDropListener);

	    // Add the label to the content
	    this.getContentPane().add(BorderLayout.CENTER, myLabel);

	    // Show the frame
	    this.setVisible(true);

	}
	
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

}
