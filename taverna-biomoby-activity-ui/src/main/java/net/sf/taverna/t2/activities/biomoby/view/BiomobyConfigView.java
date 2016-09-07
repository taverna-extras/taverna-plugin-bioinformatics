/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.view;
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
import java.util.Map;

import javax.swing.JComponent;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import org.apache.log4j.Logger;
import org.biomoby.service.dashboard.data.ParametersTable;

/**
 * @author alanrw
 *
 */
public class BiomobyConfigView extends ActivityConfigurationPanel<BiomobyActivity, BiomobyActivityConfigurationBean> {

	private BiomobyActivity activity;
	private BiomobyActivityConfigurationBean configuration;
	private boolean changed = false;

	private static Logger logger = Logger
	.getLogger(BiomobyConfigView.class);
	private ParametersTable parameterTable;

	public BiomobyConfigView(BiomobyActivity activity) {
		this.activity = activity;
		initialise();
	}

	private void initialise() {
		configuration = activity.getConfiguration();
		this.setLayout(new BorderLayout());
		parameterTable = activity.getParameterTable();
		JComponent component = parameterTable.scrollable();
		this.add(component, BorderLayout.NORTH);
		validate();
	}

	/* (non-Javadoc)
	 * @see net.sf.taverna.t2.workbench.ui.views.contextualviews.ActivityConfigurationPanel#getConfiguration()
	 */
	@Override
	public BiomobyActivityConfigurationBean getConfiguration() {
		return configuration;
	}

	/* (non-Javadoc)
	 * @see net.sf.taverna.t2.workbench.ui.views.contextualviews.ActivityConfigurationPanel#isConfigurationChanged()
	 */
	@Override
	public boolean isConfigurationChanged() {
		Map<String,String> secondaries = configuration.getSecondaries();
		int rows = parameterTable.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			String key = (String)parameterTable.getModel().getValueAt(i,0);
			String newValue = parameterTable.getModel().getValueAt(i,1).toString();
			String currentValue = secondaries.get(key);
			if (!currentValue.equals(newValue)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.taverna.t2.workbench.ui.views.contextualviews.ActivityConfigurationPanel#noteConfiguration()
	 */
	@Override
	public void noteConfiguration() {
		BiomobyActivityConfigurationBean newConfiguration =
			(BiomobyActivityConfigurationBean) cloneBean(configuration);
		Map<String,String> secondaries = newConfiguration.getSecondaries();
		int rows = parameterTable.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			String key = (String)parameterTable.getModel().getValueAt(i,0);
			String value = parameterTable.getModel().getValueAt(i,1).toString();
			secondaries.put(key, value);
		}
//		logger.info(convertBeanToString(configuration));
//		logger.info("COnfiguration was " + configuration.hashCode());
//		logger.info(convertBeanToString(newConfiguration));
//		logger.info("New configuration is " + newConfiguration.hashCode());
		configuration = newConfiguration;
	}

	@Override
	public void refreshConfiguration() {
		logger.info(convertBeanToString(activity.getConfiguration()));
		removeAll();
		initialise();
	}

	@Override
	public boolean checkValues() {
		// TODO Not yet implemented
		return true;
	}



}
