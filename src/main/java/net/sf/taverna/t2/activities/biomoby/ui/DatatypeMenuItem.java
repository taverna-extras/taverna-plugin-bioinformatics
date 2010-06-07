/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.sf.taverna.t2.activities.biomoby.actions.MobyPanel;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyObjectActivityItem;
import net.sf.taverna.t2.workbench.MainWindow;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;

import com.sun.java.help.impl.SwingWorker;

/**
 * DatatypeMenuItem is a JMenuItem that onClick produces a biomoby datatype tree
 * that workbench users can utilize to add datatypes to any workflow.
 * 
 * @author Edward Kawas
 * 
 */
public class DatatypeMenuItem extends JMenuItem {

	private static Logger logger = Logger.getLogger(DatatypeMenuItem.class);
	private static final long serialVersionUID = -1010828167358361441L;

	private String endpoint;
	private String namespace;

	/**
	 * Default constructor; Creates a menu item for the default registry
	 */
	public DatatypeMenuItem() {
		this(CentralImpl.DEFAULT_ENDPOINT, CentralImpl.DEFAULT_NAMESPACE);
	}

	/**
	 * Create a Datatype menu item for a biomoby registry given a specific
	 * endpoint and namespace
	 * 
	 * @param endpoint
	 *            the registry endpoint
	 * @param namespace
	 *            the registry namespace
	 */
	public DatatypeMenuItem(String endpoint, String namespace) {
		this(endpoint, namespace, endpoint);
	}

	/*
	 * A private constructor. Every constructor ends up here
	 */
	private DatatypeMenuItem(String endpoint, String namespace, String label) {
		// set up some specifics
		this.endpoint = endpoint;
		this.namespace = namespace;
		// set up the name, label and icon for this menu item
		setName(label);
		setText(label);
		setIcon(new ImageIcon(BiomobyObjectActivityItem.class
				.getResource("/biomoby_object.png")));
		// enable the item
		setEnabled(true);
		// create an action listener to catch clicks
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof DatatypeMenuItem) {
					final DatatypeMenuItem item = (DatatypeMenuItem) e
							.getSource();
					// create a swing worker that creates our tree
					SwingWorker worker = new SwingWorker() {
						@Override
						public Object construct() {
							// create a progress bar ...
							JProgressBar bar = new JProgressBar();
							bar.setIndeterminate(true);
							bar.setString("Creating datatype tree ...");
							bar.setStringPainted(true);
							// a dialog frame hold the bar
							String title = "Datatype Tree Builder";
							JDialog frame = new HelpEnabledDialog(MainWindow.getMainWindow(), title, false, null);							
							JLabel label = new JLabel(
									"Constructing tree for:\n\t"
											+ item.getEndpoint());
							JPanel panel = new JPanel();
							panel.add(bar);
							// the panel that holds the label and bar
							JPanel panel1 = new JPanel();
							panel1.setLayout(new BorderLayout());
							panel1.add(panel, BorderLayout.NORTH);
							panel1.add(label, BorderLayout.CENTER);
							panel1.setBorder(BorderFactory.createEmptyBorder(
									20, 20, 20, 20));
							frame.setContentPane(panel1);
							frame.setResizable(false);
							frame.pack();
							frame.setVisible(true);
							// do our task
							getTreeForRegistry(item.getEndpoint(), item
									.getNamespace());
							// hide the progress bar ...
							frame.setVisible(false);
							frame.removeAll();
							frame = null;
							return null;
						}
					};
					worker.start();
				}
			}
		});
	}

	/**
	 * Set the registry namespace
	 * 
	 * @param namespace
	 *            the registry namespace that this menu item will use
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Set the registry endpoint
	 * 
	 * @param endpoint
	 *            the registry endpoint that this menu item will use
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Get the registry endpoint
	 * 
	 * @return the registry endpoint that this menu item is using
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * Get the registry namespace
	 * 
	 * @return the registry namespace that this menu item is using
	 */
	public String getNamespace() {
		return namespace;
	}

	/*
	 * Creates a tree for a given registry
	 */
	private void getTreeForRegistry(String endpoint, String namespace) {
		Frame f = MobyPanel.CreateFrame("Datatype Viewer for " + endpoint);
		try {
			Component c = new BiomobyObjectTree(endpoint, namespace)
					.getDatatypeTree();
			f.add(c);
			f.setPreferredSize(c.getPreferredSize());
			f.setMinimumSize(c.getPreferredSize());
			f.pack();
		} catch (MobyException e) {
			logger.error(
					"Error encountered while constructing datatype viewer:\n",
					e);
		}
		f.setVisible(true);
	}
}
