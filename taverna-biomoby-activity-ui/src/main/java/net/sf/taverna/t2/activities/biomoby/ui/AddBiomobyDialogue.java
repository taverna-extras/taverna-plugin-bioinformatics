package net.sf.taverna.t2.activities.biomoby.ui;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import net.sf.taverna.t2.activities.biomoby.actions.BiomobyScavengerDialog;
import net.sf.taverna.t2.workbench.MainWindow;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;

@SuppressWarnings("serial")
public abstract class AddBiomobyDialogue extends HelpEnabledDialog {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AddBiomobyDialogue.class);

	private String endpoint = CentralImpl.DEFAULT_ENDPOINT;

	private String uri = CentralImpl.DEFAULT_NAMESPACE;

	public AddBiomobyDialogue() {
		super(MainWindow.getMainWindow(), "Add BioMoby Registry", true, null);
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
					addRegistry(registryEndpoint.trim(), registryNamespace.trim());
				} finally {
					setVisible(false);
					dispose();
				}
			}
		}
	}

}
