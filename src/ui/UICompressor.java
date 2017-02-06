package ui;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import compression.CompressorDelegate;
import facade.CompressorFacade;


public class UICompressor extends JFrame implements CompressorDelegate {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CompressorFacade facade;


	public UICompressor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}


	@Override
	public void notifyAdvancement(long percentage) {
		// TODO Auto-generated method stub
	}
	

}
