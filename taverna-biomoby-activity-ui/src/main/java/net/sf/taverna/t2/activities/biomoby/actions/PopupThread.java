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

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;


public class PopupThread extends Thread {

	Object object = null;

	BiomobyObjectActivity objectActivity = null;

	BiomobyObjectActionHelper objectAction = null;

	boolean done = false;

	PopupThread(BiomobyObjectActivity bop, BiomobyObjectActionHelper boa) {
		super("Biomoby popup");
		this.objectAction = boa;
		this.objectActivity = bop;
		setDaemon(true);
	}

	public void run() {
		object = objectAction.getComponent(objectActivity);
		this.done = true;
	}

	// call after you check if done!
	public Component getComponent() {
		return (Component) object;
	}
}
