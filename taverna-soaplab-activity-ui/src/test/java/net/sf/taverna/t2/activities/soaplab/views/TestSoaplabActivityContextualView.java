/*******************************************************************************
 ******************************************************************************/
package net.sf.taverna.t2.activities.soaplab.views;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.taverna.t2.activities.soaplab.actions.SoaplabActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.taverna.scufl2.api.activity.Activity;

public class TestSoaplabActivityContextualView {

	Activity a;

	@Before
	public void setup() throws Exception {
		a=new Activity();
	}

	@Test
	@Ignore("Integration test")
	public void testConfigureAction() throws Exception {
		ContextualView view = new SoaplabActivityContextualView(a, null, null, null, null, null);
		assertNotNull("the action should not be null",view.getConfigureAction(null));
		assertTrue("The action should be a SoaplabAcitivyConfigurationAction",view.getConfigureAction(null) instanceof SoaplabActivityConfigurationAction);
	}

	private void run() throws Exception
	{
		setup();
		ContextualView view = new SoaplabActivityContextualView(a, null, null, null, null, null);
		view.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new TestSoaplabActivityContextualView().run();
	}
}
