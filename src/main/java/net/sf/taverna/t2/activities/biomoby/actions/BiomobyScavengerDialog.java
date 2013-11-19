/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.biomoby.registry.meta.Registries;
import org.biomoby.registry.meta.RegistriesList;
import org.biomoby.shared.MobyException;

import net.sf.taverna.t2.lang.ui.ShadedLabel;

/**
 * a dialog for helping create scavengers for BioMoby registries that are not
 * the default registry.
 * 
 */
public class BiomobyScavengerDialog extends JPanel {

	private static final long serialVersionUID = -57047613557546674L;
	private JTextField registryEndpoint = new JTextField(
			"http://www.inab.org/cgi-bin/MOBY-Central.pl");
	private JTextField registryURI = new JTextField(
			"http://www.inab.org/MOBY/Central");

	/**
	 * Default constructor.
	 * 
	 */
	/**
	 * Default constructor.
	 * 
	 */
	public BiomobyScavengerDialog() {
		super();
		GridLayout layout = new GridLayout(3, 2);
		setLayout(layout);
		
		add(new ShadedLabel(
				"Location (URL) of your BioMoby central registry: ",
				ShadedLabel.BLUE, true));
		registryEndpoint
				.setToolTipText("BioMoby Services will be retrieved from the endpoint that you specify here!");
		add(registryEndpoint);
		add(new ShadedLabel(
				"Namespace (URI) of your BioMoby central registry: ",
				ShadedLabel.BLUE, true));
		registryURI
				.setToolTipText("BioMoby Services will be retrieved from the endpoint/URI that you specify here!");
		add(registryURI);
		// add(Box.createHorizontalGlue());add(Box.createHorizontalGlue());
		setPreferredSize(this.getPreferredSize());
		setMinimumSize(this.getPreferredSize());
		setMaximumSize(this.getPreferredSize());

	}

	/**
	 * 
	 * @return the string representation of the BioMoby Registry endpoint
	 */
	public String getRegistryEndpoint() {
		return registryEndpoint.getText();
	}

	/**
	 * 
	 * @return the string representation of the BioMoby Registry endpoint
	 */
	public String getRegistryURI() {
		return registryURI.getText();
	}
}
