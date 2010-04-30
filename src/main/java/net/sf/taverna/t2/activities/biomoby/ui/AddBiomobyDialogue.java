package net.sf.taverna.t2.activities.biomoby.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import net.sf.taverna.t2.activities.biomoby.actions.BiomobyScavengerDialog;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;

@SuppressWarnings("serial")
public abstract class AddBiomobyDialogue extends HelpEnabledDialog {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AddBiomobyDialogue.class);

	private String endpoint = CentralImpl.DEFAULT_ENDPOINT;

	private String uri = CentralImpl.DEFAULT_NAMESPACE;

	public AddBiomobyDialogue() {
		super(Workbench.getInstance(), "Add BioMoby Registry", true, null);
		initialize();
		setLocationRelativeTo(getParent());
	}
	public void initialize() {
		final BiomobyScavengerDialog msp = new BiomobyScavengerDialog();
		getContentPane().add(msp);
		JButton accept = new JButton(new OKAction(msp));
		JButton cancel = new JButton(new CancelAction());
		msp.add(accept);
		msp.add(cancel);
		setResizable(false);
		getContentPane().add(msp);
		setLocationRelativeTo(null);
		pack();
	}

	protected abstract void addRegistry(String registryEndpoint,
			String registryURI);

	public class CancelAction extends AbstractAction {
		public CancelAction() {
			super("Cancel");
		}
		
		public void actionPerformed(ActionEvent ae2) {
			if (isVisible()) {
				setVisible(false);
				dispose();
			}
		}
	}

	public class OKAction extends AbstractAction {
		private final BiomobyScavengerDialog scavengerDialogue;

		private OKAction(BiomobyScavengerDialog msp) {
			super("OK");
			this.scavengerDialogue = msp;
		}

		public void actionPerformed(ActionEvent ae2) {
			if (isVisible()) {
				String registryEndpoint = "";
				String registryNamespace = "";

				if (scavengerDialogue.getRegistryEndpoint().equals("")) {
					registryEndpoint = endpoint;
				} else {
					registryEndpoint = scavengerDialogue.getRegistryEndpoint();
				}

				if (scavengerDialogue.getRegistryEndpoint().equals("")) {
					registryNamespace = uri;
				} else {
					registryNamespace = scavengerDialogue.getRegistryURI();
				}

				try {
					addRegistry(registryEndpoint, registryNamespace);
				} finally {
					setVisible(false);
					dispose();
				}
			}
		}
	}

}
