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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;


/**
 * This class contains some methods that are useful in creating a consistent JPanel
 * for displaying information or actions for biomoby services and datatypes.
 *
 * @author Edward Kawas
 * @author Stuart Owen = initial port from T1 to T2
 *
 */
public class SimpleActionFrame extends JPanel {

		private static final long serialVersionUID = -6611234116434482238L;


		private String name = "";
		public SimpleActionFrame(Component c, String name) {
			super(new BorderLayout());
			add(c, BorderLayout.CENTER);

			this.name = name;
		}



		public String getName() {
			return name;
		}

		public void onDisplay() {

		}


	}
