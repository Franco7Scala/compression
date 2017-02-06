package ui;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import compression.CompressorDelegate;
import facade.CompressorFacade;
import javax.swing.JButton;
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
	private String pathFile = "/esercizi/compression/test.tush";
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
		txtDescription.setBounds(12, 47, 210, 65);
		contentPane.add(txtDescription);
		// UI Compress
		JButton btnCompress = new JButton("Compress");
		btnCompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !inProgress ) {
					new Thread() {
						public void run() {
							facade.compress(pathFile);
							inProgress = true;
						};
					}.start();
					txtDescription.setText("Compressing...");
				}
				else {
					JOptionPane.showMessageDialog(null, "Working...");
				}
			}
		});
		btnCompress.setBounds(479, 18, 100, 100);
		contentPane.add(btnCompress);
		// UI Decompress
		JButton btnDecompress = new JButton("Decompress");
		btnDecompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !inProgress ) {
					new Thread() {
						public void run() {
							facade.decompress(pathFile);
							inProgress = true;
						};
					}.start();
					txtDescription.setText("Decompressing...");
				}
				else {
					JOptionPane.showMessageDialog(null, "Working...");
				}
			}
		});
		btnDecompress.setBounds(371, 18, 100, 100);
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
		if ( percentage >= 100 ) {
			inProgress = false;
			txtDescription.setText(facade.getStatiscticsCompression());
		}
		progressBar.setValue((int)(percentage*100));
		progressLabel.setText( (int)(percentage*100) + " %");
	}
}
