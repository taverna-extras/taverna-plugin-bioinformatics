/**
 * Copyright (C) 2007 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 */
package net.sf.taverna.t2.activities.biomoby.edits;

import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.workflowmodel.CompoundEdit;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
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
public class AddBiomobyConsumingServiceEdit extends AbstractDataflowEdit {

	private final BiomobyObjectActivity activity;
	private final String serviceName;
	private Edits edits = EditsRegistry.getEdits();
	Edit<?> compoundEdit = null;
	Edit<?> linkEdit = null;
	private final String authority;
	private final OutputPort outputPort;

	/**
	 * @param dataflow
	 */
	public AddBiomobyConsumingServiceEdit(Dataflow dataflow,
			BiomobyObjectActivity activity, String serviceName,String authority,OutputPort outputPort) {
		super(dataflow);

		this.activity = activity;
		this.serviceName = serviceName;
		this.authority = authority;
		this.outputPort = outputPort;
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
		String name = net.sf.taverna.t2.workflowmodel.impl.Tools
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
		EventHandlingInputPort sinkPort = getSinkPort(sinkProcessor, boActivity, "input", linkEditList);
		if (sinkPort==null) {
			throw new EditException("No input called 'input' found for Biomoby consuming service");
		}
		linkEditList.add(Tools
				.getCreateAndConnectDatalinkEdit(
						dataflow,
						sourcePort, sinkPort));
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
