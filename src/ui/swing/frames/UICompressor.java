package ui.swing.frames;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.compression.CompressorDelegate;
import engine.facade.CompressorFacade;
import ui.swing.material.MaterialButton;
import ui.utilities.UIConstants;
import ui.utilities.UISupport;

import javax.swing.JProgressBar;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;


public class UICompressor extends JFrame implements CompressorDelegate {
	private static final long serialVersionUID = 1L;
	// Logic
	private CompressorFacade facade;
	private String pathFile = "/esercizi/compression/aaa.txt";
	private boolean inProgress;
	private String[] methods;
	//UI elements
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private JTextArea txtDescription;


	public UICompressor() {
		// linking UI with logic
		facade = new CompressorFacade(this);
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
		txtDescription.setText("Select compression algorithm...");
		txtDescription.setBounds(12, 47, 230, 65);
		contentPane.add(txtDescription);
		// UI Compress
		MaterialButton btnCompress = new MaterialButton("");
		btnCompress.setIcon(UISupport.loadImage("/images/zipper.png"));
		btnCompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !inProgress ) {
					new Thread() {
						public void run() {
							inProgress = true;
							facade.compress(pathFile);
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
		contentPane.add(progressBar);
		// UI Progress label
		progressLabel = new JLabel(" -  %");
		progressLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		progressLabel.setBounds(518, 132, 61, 27);
		contentPane.add(progressLabel);
	}

	@Override
	public void notifyAdvancement(float percentage) {
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
