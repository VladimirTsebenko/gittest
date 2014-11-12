package ca.mikelaud;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * Program for display XML
 */
public class XmlDomViewer {

	public static void main(String[] args) {
		EventQueue.invokeLater(
			new Runnable() {
				public void run() {
					JFrame frame = new DomTreeFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}			
			}
		);
	}

}
