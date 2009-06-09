/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.Component;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityIcon;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyObjectActivityItem;
import net.sf.taverna.t2.activities.biomoby.ui.DatatypeMenuItem;
import net.sf.taverna.t2.ui.menu.AbstractMenuCustom;

/**
 * This class encapsulates the advanced Menu encountered in the menu bar for the
 * workbench.
 * <p>
 * Currently, the menu only has the ability for showing the Datatype tree for
 * any specified registry.
 * <p>
 * In the future, we hope to be able to add more functionality through this
 * menu.
 * 
 * @author Edward Kawas
 * 
 */
public class BiomobyAdvancedMenuAction extends AbstractMenuCustom {

	// main menu
	private static JMenu menu = null;
	// menu for listing registries that we can view dt trees
	private static JMenu datatypeViewers = null;

	// a name for our empty menu item
	private static String EMPTY_NAME = "EMPTY";

	public BiomobyAdvancedMenuAction() {
		super(
				URI
						.create("http://taverna.sf.net/2008/t2workbench/menu#advanced"),
				100);
	}

	@Override
	protected Component createCustomComponent() {
		if (menu == null) {
			menu = new JMenu("Biomoby");
			menu.setIcon(BiomobyActivityIcon.getBiomobyIcon());
			datatypeViewers = new JMenu("Datatype Browser");
			datatypeViewers.setIcon(new ImageIcon(
					BiomobyObjectActivityItem.class
							.getResource("/biomoby_object.png")));
			JMenuItem empty = new JMenuItem("empty");
			empty.setName(EMPTY_NAME);
			empty.setEnabled(false);
			datatypeViewers.add(empty);
			menu.add(datatypeViewers);
		}
		return menu;
	}

	/**
	 * This method is called each time a new BiomobyServiceProvider is added to
	 * the workbench. Each added registry causes modifications to our menu, and
	 * those modifications are done here.
	 * 
	 * @param endpoint
	 *            the endpoint of the registry that we are adding
	 * @param namespace
	 *            the namespace of the registry that we are adding
	 */
	public static void addRegistry(String endpoint, String namespace) {
		// check for the 'empty' label
		for (int x = 0; x < datatypeViewers.getItemCount(); x++) {
			if (datatypeViewers.getItem(x).getName().equals(EMPTY_NAME))
				datatypeViewers.remove(datatypeViewers.getItem(x));
		}
		addDatatypeViewerMenuItem(endpoint, namespace);
	}

	/**
	 * This method is called each time a BiomobyServiceProvider is removed from
	 * the workbench. Each registry removal may cause the menu to require
	 * updating itself and that updating occurs here.
	 * 
	 * @param endpoint
	 *            the endpoint of the registry that we are removing
	 * @param namespace
	 *            the namespace of the registry that we are removing
	 */
	public static void removeRegistry(String endpoint, String namespace) {
		// process datatype tree menu items
		removeDatatypeViewerMenuItem(endpoint);
		// check to see if any items remain
		if (datatypeViewers.getItemCount() == 0) {
			JMenuItem empty = new JMenuItem("empty");
			empty.setName(EMPTY_NAME);
			empty.setEnabled(false);
			datatypeViewers.add(empty);
		}
	}

	// add a datatype viewer to the menu
	private static void addDatatypeViewerMenuItem(String endpoint,
			String namespace) {
		// check for duplicates
		for (int x = 0; x < datatypeViewers.getItemCount(); x++) {
			if (datatypeViewers.getItem(x).getName().equals(endpoint))
				return;
		}
		// add the item
		JMenuItem item = new DatatypeMenuItem(endpoint, namespace);
		datatypeViewers.add(item);
	}
	
	// remove a datatype viewer from the menu
	private static void removeDatatypeViewerMenuItem(String endpoint) {
		// look for our item and remove it
		for (int x = 0; x < datatypeViewers.getItemCount(); x++)
			if (datatypeViewers.getItem(x).getName().equals(endpoint)) {
				datatypeViewers.remove(datatypeViewers.getItem(x));
				break;
			}
	}
}
