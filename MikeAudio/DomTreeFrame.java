package ca.mikelaud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Frame for display XML
 */
public class DomTreeFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 400;
	
	private DocumentBuilder builder;

	/**
	 * Open and load XML file
	 */
	private void openFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileFilter(
			new FileFilter() {
				public boolean accept(File file) {
					return (
						file.isDirectory()
						||
						file.getName().toLowerCase().endsWith(".xml")
					);
				}

				public String getDescription() {
					return "XML files";
				}
			}
		);
		int ret = chooser.showOpenDialog(this);
		if (ret != JFileChooser.APPROVE_OPTION) return;
		final File file = chooser.getSelectedFile();
		
		new SwingWorker<Document, Void>() {
			protected Document doInBackground() throws Exception {
				if (builder == null) {
					DocumentBuilderFactory factory =
						DocumentBuilderFactory.newInstance();
					builder = factory.newDocumentBuilder();
				}
				return builder.parse(file);
			}
			protected void done() {
				try {
					Document doc = get();
					JTree tree = new JTree(new DomTreeModel(doc));
					tree.setCellRenderer(new DomTreeCellRenderer());
					setContentPane(new JScrollPane(tree));
					validate();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(DomTreeFrame.this, e);
				}
			}
		}.execute();
	}

	public DomTreeFrame() {
		setTitle("XmlDomViewer");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					openFile();
				}				
			}
		);
		fileMenu.add(openItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					System.exit(0);
				}				
			}
		);
		fileMenu.add(exitItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	
}
