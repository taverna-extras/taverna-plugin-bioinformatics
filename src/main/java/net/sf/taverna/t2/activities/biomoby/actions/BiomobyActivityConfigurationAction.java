/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;
import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;

import org.apache.log4j.Logger;
import org.biomoby.service.dashboard.data.ParametersTable;

@SuppressWarnings("serial")
public class BiomobyActivityConfigurationAction
		extends
		ActivityConfigurationAction<BiomobyActivity, BiomobyActivityConfigurationBean> {

	private final Frame owner;
	private static Logger logger = Logger
			.getLogger(BiomobyActivityConfigurationAction.class);

	public BiomobyActivityConfigurationAction(BiomobyActivity activity,
			Frame owner) {
		super(activity);
		this.owner = owner;
	}

	public void actionPerformed(ActionEvent arg0) {
		JDialog currentDialog = ActivityConfigurationAction.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		JPanel panel = new JPanel(new BorderLayout());
		if (getActivity().getParameterTable() == null) {
			return;
		}
		JComponent component = getActivity().getParameterTable().scrollable();
		
		final HelpEnabledDialog dialog = new HelpEnabledDialog((Frame) null, getRelativeName(), false, null);
		
		panel.add(component, BorderLayout.NORTH);
		
		JButton closeButton = new JButton(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				BiomobyActivity activity = getActivity();
				configureActivity(activity);
				ActivityConfigurationAction.clearDialog(activity);
			}
		});

		closeButton.setText("Close");
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(closeButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		dialog.getContentPane().add(panel);
		dialog.pack();
		dialog.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				configureActivity(getActivity());
				ActivityConfigurationAction.clearDialog(dialog);
			}
		});
		ActivityConfigurationAction.setDialog(getActivity(), dialog);		
	}

	private void configureActivity(BiomobyActivity activity) {
		BiomobyActivityConfigurationBean bean = getActivity().getConfiguration();
		Map<String,String> secondaries = bean.getSecondaries();
		ParametersTable table = getActivity().getParameterTable();
		int rows = table.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			String key = (String)table.getModel().getValueAt(i,0);
			String value = table.getModel().getValueAt(i,1).toString();
			secondaries.put(key, value);
		}
		
		//FIXME: A dummy edit to trigger updating of the contextual view. Because of the way
		//Biomoby is configured, the activity ParameterTable gets modified directly and is then applied to
		//the ConfigBean. I'm sure this is fixable but require significant changes to Eddies code, which I'm trying
		//to avoid.
		Edit<?> dummyEdit = EditsRegistry.getEdits().getConfigureActivityEdit(getActivity(), bean);
		Dataflow dataflow = FileManager.getInstance()
		.getCurrentDataflow();
		try {
			EditManager.getInstance().doDataflowEdit(dataflow, dummyEdit);
		} catch (IllegalStateException e) {
			logger.warn("An error occurred applying the Biomoby configuration edit",e);
			
		} catch (EditException e) {
			logger.warn("An error occurred applying the Biomoby configuration edit",e);
		}
		
	}
	
	public boolean isEnabled() {
		BiomobyActivity activity = (BiomobyActivity)getActivity();
		return (activity.getMobyService() != null && activity.containsSecondaries());
	}
}
