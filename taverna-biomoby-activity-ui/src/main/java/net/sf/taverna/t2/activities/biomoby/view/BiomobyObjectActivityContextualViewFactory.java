/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.workbench.configuration.colour.ColourManager;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

/**
 * @author Stuart Owen
 *
 */
public class BiomobyObjectActivityContextualViewFactory implements
		ContextualViewFactory<BiomobyObjectActivity> {

	private EditManager editManager;
	private FileManager fileManager;
	private ColourManager colourManager;

	public boolean canHandle(Object activity) {
		return activity instanceof BiomobyObjectActivity;
	}

	public List<ContextualView> getViews(BiomobyObjectActivity activity) {
		return Arrays.asList(new ContextualView[] { new BiomobyObjectActivityContextualView(
				activity, editManager, fileManager, colourManager) });
	}

	public void setEditManager(EditManager editManager) {
		this.editManager = editManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setColourManager(ColourManager colourManager) {
		this.colourManager = colourManager;
	}

}
