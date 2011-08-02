/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.view;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactoryRegistry;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.junit.Before;
import org.junit.Test;

public class BiomobyContextualViewFactoryTest {
	BiomobyActivity activity;
	@Before
	public void setup() throws ActivityConfigurationException {
		activity=new BiomobyActivity() { //need to prevent the activity trying to configure itself, but store a copy of the config bean

			@Override
			public void configure(
					BiomobyActivityConfigurationBean configurationBean)
					throws ActivityConfigurationException {
				this.configurationBean=configurationBean;
			}

		};
		BiomobyActivityConfigurationBean b = new BiomobyActivityConfigurationBean();
		b.setAuthorityName("a");
		b.setMobyEndpoint("e");
		b.setServiceName("s");
		activity.configure(b);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDisovery() throws Exception {
		List<ContextualViewFactory> viewFactoriesForBeanType = ContextualViewFactoryRegistry.getInstance().getViewFactoriesForObject(activity);
		assertTrue("The Biomoby view factory should not be empty", !viewFactoriesForBeanType.isEmpty());
		BiomobyActivityContextualViewFactory factory = null;
		for (ContextualViewFactory cvf : viewFactoriesForBeanType) {
			if (cvf instanceof BiomobyActivityContextualViewFactory) {
				factory = (BiomobyActivityContextualViewFactory) cvf;
			}
		}
		assertTrue("No biomoby view factory", factory != null);
	}

	@Test
	public void testGetConfigureAction() throws Exception {
		ContextualView view = new BiomobyActivityContextualView(activity, null, null);
		//will be null because its not a valid activity so therefore has no secondaries
		assertNull("The action should be null",view.getConfigureAction(null));
	}
}
