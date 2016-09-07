package net.sf.taverna.t2.activities.biomoby.edits;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;

import org.biomoby.registry.meta.Registry;
import org.biomoby.shared.MobyDataType;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
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
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

/**
 * @author Stuart Owen
 *
 */
public class AddBiomobyConsumingServiceEdit extends AbstractDataflowEdit {

	private final BiomobyObjectActivity activity;
	private final String serviceName;
	private Edits edits;
	Edit<?> compoundEdit = null;
	Edit<?> linkEdit = null;
	private final String authority;
	private final OutputPort outputPort;

	/**
	 * @param dataflow
	 */
	public AddBiomobyConsumingServiceEdit(Dataflow dataflow,
			BiomobyObjectActivity activity, String serviceName,String authority,OutputPort outputPort, Edits edits) {
		super(dataflow);

		this.activity = activity;
		this.serviceName = serviceName;
		this.authority = authority;
		this.outputPort = outputPort;
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
		String defaultName = serviceName;
		String name = Tools
				.uniqueProcessorName(defaultName, dataflow);

		List<Edit<?>> editList = new ArrayList<Edit<?>>();

		net.sf.taverna.t2.workflowmodel.Processor sinkProcessor = edits
				.createProcessor(name);

		BiomobyActivityConfigurationBean bean = new BiomobyActivityConfigurationBean();
		bean.setAuthorityName(authority);
		bean.setServiceName(serviceName);
		bean.setMobyEndpoint(activity.getConfiguration().getMobyEndpoint());
		BiomobyActivity boActivity = new BiomobyActivity();
		editList.add(edits.getConfigureActivityEdit(boActivity, bean));

		editList.add(edits.getDefaultDispatchStackEdit(sinkProcessor));

		Edit<?> addActivityToProcessorEdit = edits.getAddActivityEdit(
				sinkProcessor, boActivity);
		editList.add(addActivityToProcessorEdit);

		editList.add(edits.getAddProcessorEdit(dataflow, sinkProcessor));

		compoundEdit = new CompoundEdit(editList);
		compoundEdit.doEdit();

		net.sf.taverna.t2.workflowmodel.Processor sourceProcessor = Tools.getProcessorsWithActivityOutputPort(dataflow, outputPort).iterator().next();

		List<Edit<?>> linkEditList = new ArrayList<Edit<?>>();

		EventForwardingOutputPort sourcePort = null;
		//FIXME: there is an assumption here that the processor will contain only 1 activity.
		if (outputPort != null) {
			sourcePort = getSourcePort(sourceProcessor, sourceProcessor.getActivityList().get(0), outputPort.getName(), linkEditList);
		}
		else {
			sourcePort = getSourcePort(sourceProcessor, sourceProcessor.getActivityList().get(0), sourceProcessor.getOutputPorts().get(0).getName(), linkEditList);
		}
		// get the input port that isnt called 'input'
		String inputPortName = "";
		for (ActivityInputPort aip : sinkProcessor.getActivityList().get(0).getInputPorts()) {
			if (!aip.getName().equalsIgnoreCase("input")) {
				// try to match the datatype to an input port
				String dtName = activity.getMobyObject().getName();
				String sinkDtname = aip.getName();
				if (sinkDtname.indexOf("(") > 0)
					sinkDtname = sinkDtname.substring(0, sinkDtname.indexOf("("));
				// are the datatype names exactly the same?
				if (dtName.equals(sinkDtname)) {
					inputPortName = aip.getName();
					break;
				}
				// check for the name in the datatypes lineage
				MobyDataType sinkDt = MobyDataType.getDataType(dtName,
						new Registry(
								activity.getCentral().getRegistryEndpoint(),
								activity.getCentral().getRegistryEndpoint(),
								activity.getCentral().getRegistryNamespace())
				);
				// check the lineage of the sinkdt for dtname
				for (MobyDataType lineage : sinkDt.getLineage()) {
					if (lineage.getName().equals(sinkDtname)) {
						inputPortName = aip.getName();
						break;
					}
				}
				// are we done?
				if (!inputPortName.trim().equals(""))
					break;
			}
		}
		// if inputPortName is not set, then just pick the first one
		if (inputPortName.trim().equals("")) {
			inputPortName = sinkProcessor.getActivityList().get(0).getInputPorts().iterator().next().getName();
		}
		EventHandlingInputPort sinkPort = getSinkPort(sinkProcessor, boActivity, inputPortName, linkEditList);
		if (sinkPort==null) {
			throw new EditException("No valid input called '"+inputPortName+"' found for Biomoby consuming service");
		}
		linkEditList.add(Tools
				.getCreateAndConnectDatalinkEdit(
						dataflow,
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
		if (linkEdit!=null && linkEdit.isApplied())
			linkEdit.undo();
		if (compoundEdit!=null && compoundEdit.isApplied())
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
