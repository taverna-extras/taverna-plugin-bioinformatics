/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import net.sf.taverna.t2.activities.biomoby.actions.AddBiomobyRegistryActionHandler;

import org.junit.Test;
import static org.junit.Assert.*;

public class AddBiomobyActionHandlerTest {

	@Test
	public void testIcon() {
		AddBiomobyRegistryActionHandler handler = new AddBiomobyRegistryActionHandler();
		assertNotNull("The icon must not be null",handler.getIcon());
		assertNotNull("The text must not be null",handler.getText());
		assertEquals("The text is wrong","Biomoby...",handler.getText());
	}
}
