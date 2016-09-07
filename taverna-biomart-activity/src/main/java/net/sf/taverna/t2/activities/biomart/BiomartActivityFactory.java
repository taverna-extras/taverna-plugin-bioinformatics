package net.sf.taverna.t2.activities.biomart;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.taverna.t2.reference.ExternalReferenceSPI;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityFactory;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityOutputPort;

import org.apache.log4j.Logger;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartServiceXMLHandler;
import org.biomart.martservice.query.Attribute;
import org.biomart.martservice.query.Filter;
import org.biomart.martservice.query.Query;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An {@link ActivityFactory} for creating <code>BiomartActivity</code>.
 *
 * @author David Withers
 */
public class BiomartActivityFactory implements ActivityFactory {

	private static Logger logger = Logger.getLogger(BiomartActivityFactory.class);

	private Edits edits;
	private SAXBuilder builder = new SAXBuilder();

	@Override
	public BiomartActivity createActivity() {
		return new BiomartActivity();
	}

	@Override
	public URI getActivityType() {
		return URI.create(BiomartActivity.URI);
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
	public Set<ActivityInputPort> getInputPorts(JsonNode configuration) {
		Set<ActivityInputPort> inputPorts = new HashSet<>();
		try {
			Query query = getQuery(configuration);
			for (Filter filter : query.getFilters()) {
				String name = filter.getQualifiedName() + "_filter";
				inputPorts.add(edits.createActivityInputPort(name, filter.isList() ? 1 : 0, true,
						new ArrayList<Class<? extends ExternalReferenceSPI>>(), String.class));
			}
		} catch (JDOMException | IOException e) {
			logger.warn("Error caluculating input ports from BioMart configuration", e);
		}
		return inputPorts;
	}

	@Override
	public Set<ActivityOutputPort> getOutputPorts(JsonNode configuration) {
		Set<ActivityOutputPort> outputPorts = new HashSet<>();
		try {
			Query query = getQuery(configuration);
			List<Attribute> attributes = query.getAttributes();
			if (query.getFormatter() == null) {
				// Create new output ports corresponding to attributes
				for (Attribute attribute : attributes) {
					String name = attribute.getQualifiedName();
					ActivityOutputPort outputPort = null;
					if (attribute.getAttributes() != null) {
						outputPorts.add(edits.createActivityOutputPort(name, 2,
								BiomartActivity.STREAM_RESULTS ? 1 : 2));
					} else {
						outputPorts.add(edits.createActivityOutputPort(name, 1,
								BiomartActivity.STREAM_RESULTS ? 0 : 1));
					}
				}
			} else if (attributes.size() > 0) {
				// create one port using the dataset name
				Attribute attribute = attributes.get(0);
				String name = attribute.getContainingDataset().getName();
				outputPorts.add(edits.createActivityOutputPort(name, 0, 0));
			}
		} catch (JDOMException | IOException e) {
			logger.warn("Error caluculating output ports from BioMart configuration", e);
		}
		return outputPorts;
	}

	public void setEdits(Edits edits) {
		this.edits = edits;
	}

	private Query getQuery(JsonNode configuration) throws JDOMException, IOException {
		String martQueryText = configuration.get("martQuery").asText();
		Document document = builder.build(new StringReader(martQueryText));
		MartQuery martQuery = MartServiceXMLHandler.elementToMartQuery(document.getRootElement(),
				null);
		return martQuery.getQuery();
	}

}
