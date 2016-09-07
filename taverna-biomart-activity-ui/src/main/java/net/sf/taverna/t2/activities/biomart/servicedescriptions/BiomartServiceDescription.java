 ******************************************************************************/
package net.sf.taverna.t2.activities.biomart.servicedescriptions;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import org.apache.taverna.scufl2.api.configurations.Configuration;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author alanrw
 * @author David Withers
 */
public class BiomartServiceDescription extends ServiceDescription {

	public static final URI ACTIVITY_TYPE = URI.create("http://ns.taverna.org.uk/2010/activity/biomart");

	private String url;
	private String dataset;
	private String location;

	private static final String BIOMART = "Biomart @ ";
	private Element martQuery;

	public Element getMartQuery() {
		return martQuery;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the dataset
	 */
	public String getDataset() {
		return dataset;
	}

	/**
	 * @param dataset
	 *            the dataset to set
	 */
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	public void setMartQuery(Element martQuery) {
		this.martQuery = martQuery;
	}

	@Override
	public URI getActivityType() {
		return ACTIVITY_TYPE;
	}

	@Override
	public Configuration getActivityConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setType(ACTIVITY_TYPE.resolve("#Config"));
		String queryText = new XMLOutputter().outputString(new Document(martQuery));
		((ObjectNode) configuration.getJson()).put("martQuery", queryText);
		return configuration;
	}

	@Override
	public Icon getIcon() {
		return BiomartActivityIcon.getBiomartIcon();
	}

	@Override
	public String getName() {
		return dataset;
	}

	@Override
	public List<? extends Comparable<?>> getPath() {
		List<String> result;
		result = Arrays.asList(BIOMART + url, location);
		return result;
	}

	@Override
	protected List<Object> getIdentifyingData() {
		return Arrays.<Object> asList(getUrl(), getLocation(), getDataset());
	}

	@Override
	public boolean isTemplateService() {
		return true;
	}
}
