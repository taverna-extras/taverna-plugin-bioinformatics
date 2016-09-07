package net.sf.taverna.t2.activities.biomoby.edits;

import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.workflowmodel.CompoundEdit;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EventForwardingOutputPort;
import net.sf.taverna.t2.workflowmodel.EventHandlingInputPort;
import net.sf.taverna.t2.workflowmodel.InputPort;
import net.sf.taverna.t2.workflowmodel.OutputPort;
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
public class AddBiomobyDataTypeEdit extends AbstractDataflowEdit {

	private final BiomobyActivity activity;
	private Edits edits;
	private final String objectName;

	private Edit<?> compoundEdit = null;
	private Edit<?> linkEdit = null;
	private Edit<?> upstreamObjectEdit = null;


	public AddBiomobyDataTypeEdit(Dataflow dataflow,BiomobyActivity activity,String objectName, Edits edits) {
		super(dataflow);
		this.activity = activity;
		this.objectName = objectName;
		this.edits = edits;

	}





	/* (non-Javadoc)
	 * @see net.sf.taverna.t2.workflowmodel.impl.AbstractDataflowEdit#doEditAction(net.sf.taverna.t2.workflowmodel.impl.DataflowImpl)
	 */
	@Override
	protected void doEditAction(DataflowImpl dataflow) throws EditException {
		List<Edit<?>> editList = new ArrayList<Edit<?>>();


		String defaultName = objectName;
		defaultName = defaultName.split("\\(")[0];
		String name = Tools.uniqueProcessorName(objectName, dataflow);

		BiomobyObjectActivityConfigurationBean configBean = new BiomobyObjectActivityConfigurationBean();
		configBean.setMobyEndpoint(activity
				.getConfiguration()
				.getMobyEndpoint());
		configBean.setAuthorityName("");
		configBean.setServiceName(defaultName);


		net.sf.taverna.t2.workflowmodel.Processor sourceProcessor = edits
				.createProcessor(name);
		BiomobyObjectActivity boActivity = new BiomobyObjectActivity();
		Edit<?> configureActivityEdit = edits
				.getConfigureActivityEdit(
						boActivity, configBean);
		editList.add(configureActivityEdit);

		editList
				.add(edits
						.getDefaultDispatchStackEdit(sourceProcessor));

		Edit<?> addActivityToProcessorEdit = edits
				.getAddActivityEdit(
						sourceProcessor,
						boActivity);
		editList
				.add(addActivityToProcessorEdit);

		String inputPortName = determineInputPortName(defaultName,objectName);

		editList.add(edits.getAddProcessorEdit(
				dataflow,
				sourceProcessor));

		compoundEdit = new CompoundEdit(
				editList);
		compoundEdit.doEdit();

		net.sf.taverna.t2.workflowmodel.Processor sinkProcessor = Tools
				.getProcessorsWithActivity(
						dataflow,
						activity).iterator()
				.next();

		List<Edit<?>> linkEditList = new ArrayList<Edit<?>>();
		EventHandlingInputPort sinkPort = getSinkPort(
				sinkProcessor, activity,
				inputPortName, linkEditList);
		EventForwardingOutputPort sourcePort = getSourcePort(
				sourceProcessor, boActivity,
				"mobyData", linkEditList);

		linkEditList.add(Tools
				.getCreateAndConnectDatalinkEdit(
						dataflow,
						sourcePort, sinkPort, edits));
		linkEdit = new CompoundEdit(linkEditList);
		linkEdit.doEdit();

		if (!(defaultName.equalsIgnoreCase("Object")
				|| defaultName.equalsIgnoreCase("String")
                || defaultName.equalsIgnoreCase("Integer")
                || defaultName.equalsIgnoreCase("Float")
                || defaultName.equalsIgnoreCase("DateTime"))) {
			upstreamObjectEdit=new AddUpstreamObjectEdit(dataflow,sourceProcessor,boActivity, edits);
			upstreamObjectEdit.doEdit();

		}
	}

	protected String determineInputPortName(String defaultName,String objectName) {
		String inputPortName = objectName
				.replaceAll("'", "");
		if (inputPortName.indexOf("()") > 0)
			inputPortName = inputPortName
					.replaceAll("\\(\\)",
							"\\(_ANON_\\)");
		return inputPortName;
	}

	/* (non-Javadoc)
	 * @see net.sf.taverna.t2.workflowmodel.impl.AbstractDataflowEdit#undoEditAction(net.sf.taverna.t2.workflowmodel.impl.DataflowImpl)
	 */
	@Override
	protected void undoEditAction(DataflowImpl dataflow) {
		if (linkEdit!=null && linkEdit.isApplied())
			linkEdit.undo();
		if (compoundEdit!=null && compoundEdit.isApplied())
			compoundEdit.undo();
		if (upstreamObjectEdit!=null && upstreamObjectEdit.isApplied()) {
			upstreamObjectEdit.undo();
		}

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
