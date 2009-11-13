/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityIcon;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyQuery;
import net.sf.taverna.t2.activities.biomoby.ui.AddBiomobyDialogue;
import net.sf.taverna.t2.partition.AddQueryActionHandler;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class AddBiomobyRegistryActionHandler extends AddQueryActionHandler {

	private static Logger logger = Logger
			.getLogger(AddBiomobyRegistryActionHandler.class);

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {
		AddBiomobyDialogue addBiomobyDialogue = new AddBiomobyDialogue() {
			@Override
			protected void addRegistry(String registryEndpoint,
					String registryNamespace) {
				try {
					addQuery(new BiomobyQuery(registryEndpoint,
							registryNamespace));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Unable to create service provider!\n" + ex.getMessage(),
							"Exception!", JOptionPane.ERROR_MESSAGE);
					logger.error("Exception thrown:", ex);
				}
			}
		};
		addBiomobyDialogue.setVisible(true);
	}

	@Override
	protected Icon getIcon() {
		return BiomobyActivityIcon.getBiomobyIcon();
	}

	@Override
	protected String getText() {
		return "Biomoby...";
	}

}
