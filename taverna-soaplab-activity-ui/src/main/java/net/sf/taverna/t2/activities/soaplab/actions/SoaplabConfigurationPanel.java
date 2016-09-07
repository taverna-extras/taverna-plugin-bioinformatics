package net.sf.taverna.t2.activities.soaplab.actions;
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
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import org.apache.taverna.scufl2.api.activity.Activity;

@SuppressWarnings("serial")
public class SoaplabConfigurationPanel extends ActivityConfigurationPanel {

//	ActionListener closeClicked;
//	ActionListener applyClicked;

	private JTextField intervalMaxField;
	private JTextField intervalField;
	private JTextField backoffField;
	private JCheckBox allowPolling;

	public SoaplabConfigurationPanel(Activity activity) {
		super(activity);
		initialise();
	}

	public boolean isAllowPolling() {
		return allowPolling.isSelected();
	}

	public int getInterval() {
		return Integer.parseInt(intervalField.getText());
	}

	public int getIntervalMax() {
		return Integer.parseInt(intervalMaxField.getText());
	}

	public double getBackoff() {
		return Double.parseDouble(backoffField.getText());
	}

	@Override
	protected void initialise() {
		super.initialise();
		removeAll();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel interval = new JPanel();
		interval.setLayout(new BorderLayout());
		interval.setBorder(new TitledBorder("Interval"));

		JPanel intervalMax = new JPanel();
		intervalMax.setLayout(new BorderLayout());
		intervalMax.setBorder(new TitledBorder("Max interval"));

		JPanel backoff = new JPanel();
		backoff.setLayout(new BorderLayout());
		backoff.setBorder(new TitledBorder("Backoff"));

		intervalField = new JTextField(getJson().get("pollingInterval").asText());
		intervalMaxField = new JTextField(getJson().get("pollingIntervalMax").asText());
		backoffField = new JTextField(getJson().get("pollingBackoff").asText());

		interval.add(intervalField, BorderLayout.CENTER);
		intervalMax.add(intervalMaxField);
		backoff.add(backoffField);

		allowPolling = new JCheckBox("Polling?", getJson().get("pollingInterval").intValue() != 0);
		allowPolling.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateEnableForPollingFlag();
			}
		});

		updateEnableForPollingFlag();
		JPanel allowPollingPanel = new JPanel();
		allowPollingPanel.setLayout(new BorderLayout());
		allowPollingPanel.add(allowPolling, BorderLayout.WEST);
		add(allowPollingPanel);
		add(interval);
		add(intervalMax);
		add(backoff);
		add(Box.createGlue());
		validate();
	}

	@Override
	public void noteConfiguration() {
		if (validateValues()) {
			int interval = 0;
			int intervalMax = 0;
			double backoff = 1.1;

			if (isAllowPolling()) {
				interval = getInterval();
				intervalMax = getIntervalMax();
				backoff = getBackoff();
			}

			getJson().put("pollingBackoff", backoff);
			getJson().put("pollingInterval", interval);
			getJson().put("pollingIntervalMax", intervalMax);
		}
	}

	@Override
	public boolean checkValues() {
		// TODO Not yet implemented
		return true;
	}

	private void updateEnableForPollingFlag() {
		boolean enabled = allowPolling.isSelected();
		intervalField.setEnabled(enabled);
		intervalMaxField.setEnabled(enabled);
		backoffField.setEnabled(enabled);
	}

	public boolean validateValues() {
		if (allowPolling.isSelected()) {
			try {
				new Integer(intervalField.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "The interval field must be a valid integer",
						"Invalid value", JOptionPane.ERROR_MESSAGE);
				return false;

			}

			try {
				new Integer(intervalMaxField.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"The maximum interval field must be a valid integer", "Invalid value",
						JOptionPane.ERROR_MESSAGE);
				return false;

			}

			try {
				new Double(backoffField.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "The backoff field must be a valid float",
						"Invalid value", JOptionPane.ERROR_MESSAGE);
				return false;

			}
		}

		return true;
	}

}
