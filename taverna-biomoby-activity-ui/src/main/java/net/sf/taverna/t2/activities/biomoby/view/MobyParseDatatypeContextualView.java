/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.view;

import net.sf.taverna.t2.activities.biomoby.MobyParseDatatypeActivityConfigurationBean;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * @author Stuart Owen
 *
 */
@SuppressWarnings("serial")
public class MobyParseDatatypeContextualView extends
		HTMLBasedActivityContextualView<MobyParseDatatypeActivityConfigurationBean> {

	public MobyParseDatatypeContextualView(Activity<?> activity, ColourManager colourManager) {
		super(activity, colourManager);
	}

	@Override
	protected String getRawTableRowsHtml() {
		String html = "<tr><td>Article name used by service</td><td>"
				+ getConfigBean().getArticleNameUsedByService() + "</td></tr>";
		html += "<tr><td>Datatype</td><td>" + getConfigBean().getDatatypeName() + "</td></tr>";
		html += "<tr><td>Registry endpoint</td><td>" + getConfigBean().getRegistryEndpoint()
				+ "</td></tr>";
		return html;
	}

	@Override
	public String getViewTitle() {
		return "Moby parse datatype service";
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

}
