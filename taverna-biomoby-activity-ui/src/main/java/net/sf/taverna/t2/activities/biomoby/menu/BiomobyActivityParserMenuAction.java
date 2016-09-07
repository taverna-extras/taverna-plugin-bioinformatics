package net.sf.taverna.t2.activities.biomoby.menu;
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

import javax.swing.Action;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.actions.MobyParserAction;
import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;

public class BiomobyActivityParserMenuAction extends
		AbstractConfigureActivityMenuAction<BiomobyActivity> {

	private static final String ADD_BIOMOBY_PARSER = "Add Biomoby parser";

	private EditManager editManager;

	private FileManager fileManager;

	public BiomobyActivityParserMenuAction() {
		super(BiomobyActivity.class);
	}

	@Override
	protected Action createAction() {
		MobyParserAction configAction = new MobyParserAction(
				findActivity(), getParentFrame(), editManager, fileManager);
		configAction.putValue(Action.NAME, ADD_BIOMOBY_PARSER);
		addMenuDots(configAction);
		return configAction;
	}

	public void setEditManager(EditManager editManager) {
		this.editManager = editManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

}
