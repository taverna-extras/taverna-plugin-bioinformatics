package net.sf.taverna.t2.activities.soaplab;

import java.io.IOException;
import java.net.URI;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import net.sf.taverna.t2.annotation.annotationbeans.MimeType;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityFactory;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityOutputPort;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An {@link ActivityFactory} for creating <code>SoaplabActivity</code>.
 *
 * @author David Withers
 */
public class SoaplabActivityFactory implements ActivityFactory {

	private static final Logger logger = Logger.getLogger(SoaplabActivityFactory.class);

	private Edits edits;

	@Override
	public SoaplabActivity createActivity() {
		return new SoaplabActivity();
	}

	@Override
	public URI getActivityType() {
		return URI.create(SoaplabActivity.URI);
	}

	@Override
	public JsonNode getActivityConfigurationSchema() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
 			return objectMapper.readTree(getClass().getResource("/schema.json"));
		} catch (IOException e) {
			return objectMapper.createObjectNode();
		}
	}

	@Override
	public Set<ActivityInputPort> getInputPorts(JsonNode json) throws ActivityConfigurationException {
		Set<ActivityInputPort> inputPorts = new HashSet<>();
		try {
			// Do web service type stuff[tm]
			Map<String, String>[] inputs = (Map<String, String>[]) Soap
					.callWebService(json.get("endpoint").textValue(),
							"getInputSpec");
			// Iterate over the inputs
			for (int i = 0; i < inputs.length; i++) {
				Map<String, String> input_spec = inputs[i];
				String input_name = input_spec.get("name");
				String input_type = input_spec.get("type").toLowerCase();
				// Could get other properties such as defaults here
				// but at the moment we've got nowhere to put them
				// so we don't bother.
				if (input_type.equals("string")) {
					inputPorts.add(edits.createActivityInputPort(input_name, 0, true,
							null, String.class));
				} else if (input_type.equals("string[]")) {
					inputPorts.add(edits.createActivityInputPort(input_name, 1, true,
							null, String.class));
				} else if (input_type.equals("byte[]")) {
					inputPorts.add(edits.createActivityInputPort(input_name, 0, true,
							null, byte[].class));
				} else if (input_type.equals("byte[][]")) {
					inputPorts.add(edits.createActivityInputPort(input_name, 1, true,
							null, byte[].class));
				} else {
					// Count number of [] to get the arrays right
					int depth = (input_type.split("\\[\\]", -1).length) -1 ;
					logger.info("Soaplab input type '" + input_type
							+ "' unknown for input '" + input_name + "' in "
							+ json.get("endpoint").textValue()
							+ ", will attempt to add as String depth " + depth);
					inputPorts.add(edits.createActivityInputPort(input_name, depth, true, null, String.class));
				}
			}
		} catch (ServiceException se) {
			throw new ActivityConfigurationException(
					json.get("endpoint").textValue()
							+ ": Unable to create a new call to connect\n   to soaplab, error was : "
							+ se.getMessage());
		} catch (RemoteException re) {
			throw new ActivityConfigurationException(
					": Unable to call the get spec method for\n   endpoint : "
							+ json.get("endpoint").textValue()
							+ "\n   Remote exception message "
							+ re.getMessage());
		} catch (NullPointerException npe) {
			// If we had a null pointer exception, go around again - this is a
			// bug somewhere between axis and soaplab
			// that occasionally causes NPEs to happen in the first call or two
			// to a given soaplab installation. It also
			// manifests in the Talisman soaplab clients.
			return getInputPorts(json);
		}
		return inputPorts;
	}

	@Override
	public Set<ActivityOutputPort> getOutputPorts(JsonNode json) throws ActivityConfigurationException {
		Set<ActivityOutputPort> outputPorts = new HashSet<>();
		try {
			// Get outputs
			Map<String, String>[] results = (Map<String, String>[]) Soap
					.callWebService(json.get("endpoint").textValue(),
							"getResultSpec");
			// Iterate over the outputs
			for (int i = 0; i < results.length; i++) {
				Map<String, String> output_spec = results[i];
				String output_name = output_spec.get("name");
				String output_type = output_spec.get("type").toLowerCase();
				// Check to see whether the output is either report or
				// detailed_status, in
				// which cases we ignore it, this is soaplab metadata rather
				// than application data.
				if ((!output_name.equalsIgnoreCase("detailed_status"))) {

					// && (!output_name.equalsIgnoreCase("report"))) {
					if (output_type.equals("string")) {
						outputPorts.add(createOutput(output_name, 0, "text/plain"));
					} else if (output_type.equals("string[]")) {
						outputPorts.add(createOutput(output_name, 1, "text/plain"));
					} else if (output_type.equals("byte[]")) {
						outputPorts.add(createOutput(output_name, 0, "application/octet-stream"));
					} else if (output_type.equals("byte[][]")) {
						outputPorts.add(createOutput(output_name, 1, "application/octet-stream"));
					} else {
						// Count number of [] to get the arrays right
						int depth = (output_type.split("\\[\\]", -1).length) -1 ;
						logger.info("Soaplab output type '" + output_type
								+ "' unknown for output '" + output_name + "' in "
								+ json.get("endpoint").textValue()
								+ ", will add as depth " + depth);
						outputPorts.add(createOutput(output_name, depth, null));
					}
				}
			}

		} catch (ServiceException se) {
			throw new ActivityConfigurationException(
					json.get("endpoint").textValue()
							+ ": Unable to create a new call to connect\n   to soaplab, error was : "
							+ se.getMessage());
		} catch (RemoteException re) {
			throw new ActivityConfigurationException(
					": Unable to call the get spec method for\n   endpoint : "
							+ json.get("endpoint").textValue()
							+ "\n   Remote exception message "
							+ re.getMessage());
		} catch (NullPointerException npe) {
			// If we had a null pointer exception, go around again - this is a
			// bug somewhere between axis and soaplab
			// that occasionally causes NPEs to happen in the first call or two
			// to a given soaplab installation. It also
			// manifests in the Talisman soaplab clients.
			return getOutputPorts(json);
		}
		return outputPorts;
	}

	public void setEdits(Edits edits) {
		this.edits = edits;
	}

	public ActivityOutputPort createOutput(String portName, int portDepth, String type) {
		ActivityOutputPort port = edits.createActivityOutputPort(portName, portDepth, portDepth);
		if (type != null) {
			MimeType mimeType = new MimeType();
			mimeType.setText(type);
			try {
				edits.getAddAnnotationChainEdit(port, mimeType).doEdit();
			} catch (EditException e) {
				logger.debug("Error adding MimeType annotation to port", e);
			}
		}
		return port;
	}

}
