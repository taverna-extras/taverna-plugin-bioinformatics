/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.edits;

import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.MobyParseDatatypeActivity;
import net.sf.taverna.t2.activities.biomoby.MobyParseDatatypeActivityConfigurationBean;
import net.sf.taverna.t2.workflowmodel.CompoundEdit;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EventForwardingOutputPort;
import net.sf.taverna.t2.workflowmodel.EventHandlingInputPort;
import net.sf.taverna.t2.workflowmodel.InputPort;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.Processor;
import net.sf.taverna.t2.workflowmodel.ProcessorInputPort;
import net.sf.taverna.t2.workflowmodel.ProcessorOutputPort;
import net.sf.taverna.t2.workflowmodel.impl.AbstractDataflowEdit;
import net.sf.taverna.t2.workflowmodel.impl.DataflowImpl;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

/**
 * @author Stuart Owen
 *
 */
public class AddMobyParseDatatypeEdit extends AbstractDataflowEdit {

	private final BiomobyActivity activity;
	private final String objectName;
	private final boolean isCollection;
	private final String potentialCollectionString;

	private Edit<?> compoundEdit = null;
	private Edit<?> linkEdit = null;

	private Edits edits;

	/**
	 * @param dataflow
	 */
	public AddMobyParseDatatypeEdit(Dataflow dataflow,
			BiomobyActivity activity, String objectName, boolean isCollection,
			String potentialCollectionString, Edits edits) {
		super(dataflow);
		this.activity = activity;
		this.objectName = objectName;
		this.isCollection = isCollection;
		this.potentialCollectionString = potentialCollectionString;
		this.edits = edits;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * net.sf.taverna.t2.workflowmodel.impl.AbstractEdit#doEditAction(java.lang
	 * .Object)
	 */
	@Override
	protected void doEditAction(DataflowImpl dataflow) throws EditException {

		List<Edit<?>> editList = new ArrayList<Edit<?>>();

		String defaultName = objectName;
		if (defaultName.indexOf("(") > 0)
			defaultName = defaultName.substring(0, defaultName.indexOf("("));

		String name = Tools
				.uniqueProcessorName("Parse Moby Data(" + defaultName + ")",
						dataflow);

		String articlename = "";
		if (isCollection) {
			articlename = potentialCollectionString.substring(
					potentialCollectionString.indexOf("('") + 2,
					potentialCollectionString.lastIndexOf("'"));
		} else {
			articlename = objectName.substring(objectName.indexOf("'") + 1,
					objectName.lastIndexOf("'"));
		}

		MobyParseDatatypeActivityConfigurationBean bean = new MobyParseDatatypeActivityConfigurationBean();
		bean.setArticleNameUsedByService(articlename);
		bean.setRegistryEndpoint(activity.getConfiguration().getMobyEndpoint());
		bean.setDatatypeName(defaultName);
		MobyParseDatatypeActivity mobyDatatypeActivity = new MobyParseDatatypeActivity();

		editList
				.add(edits.getConfigureActivityEdit(mobyDatatypeActivity, bean));

		net.sf.taverna.t2.workflowmodel.Processor sinkProcessor = edits
				.createProcessor(name);

		editList.add(edits.getDefaultDispatchStackEdit(sinkProcessor));

		Edit<?> addActivityToProcessorEdit = edits.getAddActivityEdit(
				sinkProcessor, mobyDatatypeActivity);
		editList.add(addActivityToProcessorEdit);

		editList.add(edits.getAddProcessorEdit(dataflow, sinkProcessor));

		compoundEdit = new CompoundEdit(editList);
		compoundEdit.doEdit();

		Processor sourceProcessor = Tools.getProcessorsWithActivity(dataflow,
				activity).iterator().next();

		List<Edit<?>> linkEditList = new ArrayList<Edit<?>>();

		String inputName = mobyDatatypeActivity.getInputPorts().iterator()
				.next().getName();
		EventHandlingInputPort sinkPort = getSinkPort(sinkProcessor,
				mobyDatatypeActivity, inputName, linkEditList);


		String outputPortName;
		if (isCollection) {
		outputPortName = defaultName + "(Collection - '"
				+ (articlename.equals("") ? "MobyCollection" : articlename)
				+ "' As Simples)";
		}
		else {
			outputPortName = defaultName +"(" + articlename + ")";
		}
		EventForwardingOutputPort sourcePort = getSourcePort(sourceProcessor,
				activity, outputPortName, linkEditList);
		linkEditList.add(Tools.getCreateAndConnectDatalinkEdit(dataflow,
				sourcePort, sinkPort, edits));
		linkEdit = new CompoundEdit(linkEditList);
		linkEdit.doEdit();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * net.sf.taverna.t2.workflowmodel.impl.AbstractEdit#undoEditAction(java
	 * .lang.Object)
	 */
	@Override
	protected void undoEditAction(DataflowImpl subjectImpl) {
		if (linkEdit != null && linkEdit.isApplied())
			linkEdit.undo();
		if (compoundEdit != null && compoundEdit.isApplied())
			compoundEdit.undo();
	}

	private EventHandlingInputPort getSinkPort(
			net.sf.taverna.t2.workflowmodel.Processor processor,
			Activity<?> activity, String portName, List<Edit<?>> editList) {
		InputPort activityPort = net.sf.taverna.t2.workflowmodel.utils.Tools
				.getActivityInputPort(activity, portName);
		// check if processor port exists
		EventHandlingInputPort input = net.sf.taverna.t2.workflowmodel.utils.Tools
				.getProcessorInputPort(processor, activity, activityPort);
		if (input == null) {
			// port doesn't exist so create a processor port and map it
			ProcessorInputPort processorInputPort = edits
					.createProcessorInputPort(processor,
							activityPort.getName(), activityPort.getDepth());
			editList.add(edits.getAddProcessorInputPortEdit(processor,
					processorInputPort));
			editList.add(edits.getAddActivityInputPortMappingEdit(activity,
					activityPort.getName(), activityPort.getName()));
			input = processorInputPort;
		}
		return input;
	}

	private EventForwardingOutputPort getSourcePort(
			net.sf.taverna.t2.workflowmodel.Processor processor,
			Activity<?> activity, String portName, List<Edit<?>> editList) {
		OutputPort activityPort = net.sf.taverna.t2.workflowmodel.utils.Tools
				.getActivityOutputPort(activity, portName);
		// check if processor port exists
		EventForwardingOutputPort output = net.sf.taverna.t2.workflowmodel.utils.Tools
				.getProcessorOutputPort(processor, activity, activityPort);
		if (output == null) {
			// port doesn't exist so create a processor port and map it
			ProcessorOutputPort processorOutputPort = edits
					.createProcessorOutputPort(processor, activityPort
							.getName(), activityPort.getDepth(), activityPort
							.getGranularDepth());
			editList.add(edits.getAddProcessorOutputPortEdit(processor,
					processorOutputPort));
			editList.add(edits.getAddActivityOutputPortMappingEdit(activity,
					activityPort.getName(), activityPort.getName()));
			output = processorOutputPort;
		}
		return output;
	}

}
