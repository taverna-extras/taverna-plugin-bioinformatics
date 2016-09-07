/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.edits;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edits;

/**
 * @author Stuart Owen
 *
 */
public class AddBiomobyCollectionDataTypeEdit extends
		AddBiomobyDataTypeEdit {

	private final String theCollectionName;

	/**
	 * @param dataflow
	 * @param activity
	 * @param objectName
	 */
	public AddBiomobyCollectionDataTypeEdit(Dataflow dataflow,
			BiomobyActivity activity, String objectName, String theCollectionName, Edits edits) {
		super(dataflow, activity, objectName, edits);
		this.theCollectionName = theCollectionName;
	}

	@Override
	protected String determineInputPortName(String defaultName,String objectName) {
		return defaultName
		+ "(Collection - '"
		+ (theCollectionName.equals("") ? "MobyCollection"
				: theCollectionName)
		+ "')";
	}



}
