package net.sf.taverna.t2.activities.biomoby.actions;
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

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;

/**
 * @author Stuart Owen
 *
 */
@SuppressWarnings("serial")
public class MobyParserAction extends AbstractAction {

	private final BiomobyActivity activity;
	private final Frame owner;
	private EditManager editManager;
	private final FileManager fileManager;

	public MobyParserAction(BiomobyActivity activity, Frame owner, EditManager editManager, FileManager fileManager) {
		this.activity = activity;
		this.owner = owner;
		this.editManager = editManager;
		this.fileManager = fileManager;
		putValue(NAME, "Add Biomoby parser");

	}
	public void actionPerformed(ActionEvent e) {
		AddParserActionHelper helper = new AddParserActionHelper(editManager, fileManager);
		Component component = helper.getComponent(activity);

		final JDialog dialog = new HelpEnabledDialog(owner, helper.getDescription(), false, null);
		dialog.getContentPane().add(component);
		dialog.pack();
//		dialog.setSize(helper.getFrameSize());
		dialog.setTitle(helper.getDescription());
//		dialog.setModal(false);
		dialog.setVisible(true);
	}

}
