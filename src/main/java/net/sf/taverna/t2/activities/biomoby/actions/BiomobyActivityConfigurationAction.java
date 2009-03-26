/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JComponent;

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
		JComponent component = getActivity().getParameterTable().scrollable();
		
		final HelpEnabledDialog dialog = new HelpEnabledDialog(owner, "BioMoby Activity Configuration", true, null);
		
		dialog.getContentPane().add(component);
		dialog.pack();
		dialog.setModal(true);
		dialog.setVisible(true);
		
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

}
