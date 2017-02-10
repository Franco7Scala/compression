package ui.compressor.frames;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import engine.compression.CompressorDelegate;
import engine.facade.CompressorFacade;
import ui.compressor.material.MaterialButton;
import ui.compressor.utilities.UIConstants;
import ui.compressor.utilities.UISupport;

import javax.swing.JProgressBar;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;


public class UICompressor extends JFrame implements CompressorDelegate {
	private static final long serialVersionUID = 1L;
	// Logic
	private CompressorFacade facade;
	private String pathFile = "";
	private boolean inProgress;
	private String[] methods;
	//UI elements
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private JTextArea txtDescription;
	private List<File> files;


	public UICompressor() {
		// linking UI with logic
		facade = CompressorFacade.sharedInstance();
		facade.delegate = this;
		inProgress = false;
		methods = facade.getAllCompressionMethods();
		// UI main panel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 585, 185);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(UIConstants.defaultColor);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		// UI text description
		txtDescription = new JTextArea();
		txtDescription.setBackground(UIConstants.defaultColor);
		txtDescription.setForeground(Color.WHITE);
		txtDescription.setText("Select compression algorithm\n               And drag file here →");
		txtDescription.setBounds(12, 47, 230, 65);
		txtDescription.setEditable(false);
		contentPane.add(txtDescription);
		// UI dragNdrop
		JLabel myLabel = new JLabel();
		myLabel.setBackground(UIConstants.defaultColor);
		myLabel.setIcon(UISupport.loadImage("/images/stopped.png"));
		myLabel.setBounds(223, 0, 140, 140);
		contentPane.add(myLabel);
		DropTargetListener myDragDropListener = new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
			}
			@Override
			public void dragOver(DropTargetDragEvent dtde) {
			}
			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}
			@Override
			public void dragExit(DropTargetEvent dte) {
			}
			@Override
			public void drop(DropTargetDropEvent event) {
				// Accept copy drops
		        event.acceptDrop(DnDConstants.ACTION_COPY);
		        // Get the transfer which can provide the dropped item data
		        Transferable transferable = event.getTransferable();
		        // Get the data formats of the dropped item
		        DataFlavor[] flavors = transferable.getTransferDataFlavors();
		        // Loop through the flavors
		        for (DataFlavor flavor : flavors) {
		            try {
		                // If the drop items are files
		                if (flavor.isFlavorJavaFileListType()) {
		                    // Get all of the dropped files
		                    files = (List<File>) transferable.getTransferData(flavor);
		                    pathFile = files.get(files.size()-1).getPath();
		                    txtDescription.setText("File loaded: " + pathFile.substring(pathFile.lastIndexOf('/')+1, pathFile.length()));
		                    myLabel.setIcon(UISupport.loadImage("/images/animated.gif"));
		                    new Thread () {
		                    	public void run () {
				                    try {
										Thread.sleep(2200);
									} catch (InterruptedException e) {}
				                    myLabel.setIcon(UISupport.loadImage("/images/stopped.png"));
		                    	}
		                    }.start();
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		        // Inform that the drop is complete
		        event.dropComplete(true);
			}
		};
		// Connect the label with a drag and drop listener
		new DropTarget(myLabel, myDragDropListener);
		// UI Compress
		MaterialButton btnCompress = new MaterialButton("");
		btnCompress.setIcon(UISupport.loadImage("/images/zipper.png"));
		btnCompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !inProgress ) {
					new Thread() {
						public void run() {
							inProgress = true;
							try {
								facade.compress(pathFile);
							} catch (Exception e) {}
							completed();
						}
					}.start();
					txtDescription.setText("Compressing...");
				}
				else {
					JOptionPane.showMessageDialog(null, "Working...");
				}
			}
		});
		btnCompress.setBounds(490, 18, 80, 80);
		contentPane.add(btnCompress);
		// UI Decompress
		MaterialButton btnDecompress = new MaterialButton("");
		btnDecompress.setIcon(UISupport.loadImage("/images/unzipper.png"));
		btnDecompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !inProgress ) {
					new Thread() {
						public void run() {
							inProgress = true;
							facade.decompress(pathFile);
							completed();
						};
					}.start();
					txtDescription.setText("Decompressing...");
				}
				else {
					JOptionPane.showMessageDialog(null, "Working...");
				}
			}
		});
		btnDecompress.setBounds(400, 18, 80, 80);
		contentPane.add(btnDecompress);
		// Box to select mode
		JComboBox<String> comboBox = new JComboBox<>(methods); 
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facade.setCurrentCompressionMethod( (String)comboBox.getSelectedItem() );
			}
		});
		comboBox.setBounds(6, 18, 201, 27);
		contentPane.add(comboBox);
		// UI Progress bar
		progressBar = new JProgressBar();
		progressBar.setBounds(6, 137, 526, 20);
		progressBar.setForeground(Color.WHITE);
		contentPane.add(progressBar);
		// UI Progress label
		progressLabel = new JLabel(" -  %");
		progressLabel.setForeground(Color.WHITE);
		progressLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		progressLabel.setBounds(518, 132, 61, 27);
		contentPane.add(progressLabel);
	}

	@Override
	public void notifyAdvancementCompression(float percentage) {
		progressBar.setValue((int)(percentage*100));
		progressLabel.setText( (int)(percentage*100) + " %");
	}

	private void completed() {
		inProgress = false;
		txtDescription.setText(facade.getStatiscticsCompression());
		progressBar.setValue(100);
		progressLabel.setText("100 %");
	}


}
