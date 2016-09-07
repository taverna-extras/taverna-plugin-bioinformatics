/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.actions.MobyObjectDetailsAction;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * @author Stuart Owen
 *
 */
@SuppressWarnings("serial")
public class BiomobyObjectActivityContextualView extends
		HTMLBasedActivityContextualView<BiomobyObjectActivityConfigurationBean> {

	private EditManager editManager;
	private final FileManager fileManager;

	public BiomobyObjectActivityContextualView(Activity<?> activity, EditManager editManager,
			FileManager fileManager, ColourManager colourManager) {
		super(activity, colourManager);
		this.editManager = editManager;
		this.fileManager = fileManager;
	}

	@Override
	protected String getRawTableRowsHtml() {
		String html = "<tr><td>Endpoint</td><td>" + getConfigBean().getMobyEndpoint()
				+ "</td></tr>";
		html += "<tr><td>Authority</td><td>" + getConfigBean().getAuthorityName() + "</td></tr>";
		html += "<tr><td>Datatype</td><td>" + getConfigBean().getServiceName() + "</td></tr>";
		return html;
	}

	@Override
	public String getViewTitle() {
		return "Biomoby Object service";
	}

	/**
	 * Gets the component from the {@link HTMLBasedActivityContextualView} and adds buttons to it
	 * allowing Moby object details
	 */
	@Override
	public JComponent getMainFrame() {
		final JComponent mainFrame = super.getMainFrame();
		BiomobyObjectActivity activity = (BiomobyObjectActivity) getActivity();
		if (activity.getMobyObject() != null) {
			JPanel flowPanel = new JPanel(new FlowLayout());
			JButton button = new JButton(new MobyObjectDetailsAction(activity, null, editManager,
					fileManager));
			flowPanel.add(button);
			mainFrame.add(flowPanel, BorderLayout.SOUTH);
		}
		return mainFrame;
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}
}
