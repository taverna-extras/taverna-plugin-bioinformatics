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

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
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
public class AddUpstreamObjectEdit extends AbstractDataflowEdit {

	private final Processor sinkProcessor;
	private final BiomobyObjectActivity activity;
	private Edits edits = EditsRegistry.getEdits();

	private List<Edit<?>> compoundEdits = new ArrayList<Edit<?>>();
	private List<Edit<?>> linkEdits = new ArrayList<Edit<?>>();
	private Edit<?> upstreamObjectEdit;

	/**
	 * @param dataflow
	 */
	public AddUpstreamObjectEdit(Dataflow dataflow, Processor sinkProcessor,
			BiomobyObjectActivity activity) {
		super(dataflow);
		this.sinkProcessor = sinkProcessor;
		this.activity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.taverna.t2.workflowmodel.impl.AbstractDataflowEdit#doEditAction
	 * (net.sf.taverna.t2.workflowmodel.impl.DataflowImpl)
	 */
	@Override
	protected void doEditAction(DataflowImpl dataflow) throws EditException {

		for (InputPort inputPort : activity.getInputPorts()) {
			// ignore article name, id, namespace, value
			if (inputPort.getName().equals("namespace")
					|| inputPort.getName().equals("id")
					|| inputPort.getName().equals("article name")
					|| inputPort.getName().equals("value")) {
				continue;
			}
			List<Edit<?>> editList = new ArrayList<Edit<?>>();
			String defaultName = inputPort.getName().split("\\(")[0];

			String name = Tools
					.uniqueProcessorName(inputPort.getName(), dataflow);

			BiomobyObjectActivityConfigurationBean configBean = new BiomobyObjectActivityConfigurationBean();
			configBean.setMobyEndpoint(activity.getConfiguration()
					.getMobyEndpoint());
			configBean.setAuthorityName("");
			configBean.setServiceName(defaultName);

			net.sf.taverna.t2.workflowmodel.Processor sourceProcessor = edits
					.createProcessor(name);
			BiomobyObjectActivity boActivity = new BiomobyObjectActivity();
			Edit<?> configureActivityEdit = edits.getConfigureActivityEdit(
					boActivity, configBean);
			editList.add(configureActivityEdit);

			editList.add(edits.getDefaultDispatchStackEdit(sourceProcessor));

			Edit<?> addActivityToProcessorEdit = edits.getAddActivityEdit(
					sourceProcessor, boActivity);
			editList.add(addActivityToProcessorEdit);

			

			editList.add(edits.getAddProcessorEdit(dataflow, sourceProcessor));

			CompoundEdit compoundEdit = new CompoundEdit(editList);
			compoundEdits.add(compoundEdit);
			compoundEdit.doEdit();

			
			List<Edit<?>> linkEditList = new ArrayList<Edit<?>>();
			
			EventForwardingOutputPort sourcePort = getSourcePort(
					sourceProcessor, boActivity, "mobyData", linkEditList);
			EventHandlingInputPort sinkPort = getSinkPort(sinkProcessor, activity, inputPort.getName(), linkEditList);
			linkEditList.add(Tools.getCreateAndConnectDatalinkEdit(dataflow,
					sourcePort, sinkPort));
			CompoundEdit linkEdit = new CompoundEdit(linkEditList);
			linkEdits.add(linkEdit);
			linkEdit.doEdit();

			if (!(defaultName.equalsIgnoreCase("Object")
					|| name.equalsIgnoreCase("String")
					|| name.equalsIgnoreCase("Integer") || name
					.equalsIgnoreCase("DateTime"))) {
				upstreamObjectEdit = new AddUpstreamObjectEdit(dataflow,
						sourceProcessor, boActivity);
				upstreamObjectEdit.doEdit();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.taverna.t2.workflowmodel.impl.AbstractDataflowEdit#undoEditAction
	 * (net.sf.taverna.t2.workflowmodel.impl.DataflowImpl)
	 */
	@Override
	protected void undoEditAction(DataflowImpl dataflow) {
		if (linkEdits != null && linkEdits.size() > 0) {
			for (int i = linkEdits.size() - 1; i >= 0; i--) {
				Edit<?> edit = linkEdits.get(i);
				if (edit.isApplied())
					edit.undo();
			}
		}
		
		if (compoundEdits != null && compoundEdits.size() > 0) {
			for (int i = compoundEdits.size() - 1; i >= 0; i--) {
				Edit<?> edit = compoundEdits.get(i);
				if (edit.isApplied())
					edit.undo();
			}
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
