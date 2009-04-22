/**
 * 
 */
package net.sf.taverna.t2.activities.biomart.servicedescriptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.activities.biomart.query.BiomartActivityItem;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;

import org.biomart.martservice.MartDataset;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartRegistry;
import org.biomart.martservice.MartService;
import org.biomart.martservice.MartServiceException;
import org.biomart.martservice.MartURLLocation;
import org.jdom.Element;

/**
 * @author alanrw
 *
 */
public class BiomartServiceProvider extends AbstractConfigurableServiceProvider<BiomartServiceProviderConfig>{

	private static final String BIOMART_SERVICE = "Biomart service";
	
	public BiomartServiceProvider() {
		super(new BiomartServiceProviderConfig("http://somehost/biomart"));
	}

	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		List<ServiceDescription<Element>> descriptions = new ArrayList<ServiceDescription<Element>>();
		String url = serviceProviderConfig.getUrl();
		callBack.status("About to parse biomart:" + url);
		try {
		MartService martService = MartService
				.getMartService(getBiomartServiceLocation(url));
		martService.setRequestId("taverna");
		MartRegistry registry = martService.getRegistry();
		MartURLLocation[] martURLLocations = registry.getMartURLLocations();
		for (MartURLLocation martURLLocation : martURLLocations) {
			if (martURLLocation.isVisible()) {

				MartDataset[] datasets = martService
						.getDatasets(martURLLocation);
				Arrays.sort(datasets, MartDataset.getDisplayComparator());
				for (MartDataset dataset : datasets) {
					if (dataset.isVisible()) {
						BiomartServiceDescription item = new BiomartServiceDescription();
						item.setUrl(martService.getLocation());
						item.setLocation(martURLLocation.getDisplayName());
						item.setDataset(dataset.getName());
						MartQuery biomartQuery = new MartQuery(martService,
								dataset, "taverna");
						item.setBiomartQuery(biomartQuery);
						descriptions.add(item);
					}
				}
			}
		}
		callBack.partialResults(descriptions);
		callBack.finished();
		}
		catch (MartServiceException e) {
			callBack.fail("Failed to load Biomart from " + url, e);
		}
	}

	public Icon getIcon() {
		return new ImageIcon(BiomartActivityItem.class.getResource("/biomart.png"));
	}

	public String getName() {
		return BIOMART_SERVICE;
	}

	/**
	 * Attempts to construct a valid MartService URL from the location given.
	 * 
	 * @param biomartLocation
	 * @return a (hopefully) valid MartService URL
	 */
	private String getBiomartServiceLocation(String biomartLocation) {
		StringBuffer sb = new StringBuffer();
		if (biomartLocation.endsWith("martservice")) {
			sb.append(biomartLocation);
		} else if (biomartLocation.endsWith("martview")) {
			sb.append(biomartLocation.substring(0, biomartLocation
					.lastIndexOf("martview")));
			sb.append("martservice");
		} else if (biomartLocation.endsWith("/")) {
			sb.append(biomartLocation);
			sb.append("martservice");
		} else {
			sb.append(biomartLocation);
			sb.append("/martservice");
		}
		return sb.toString();
	}
	
	public List<BiomartServiceProviderConfig> getDefaultConfigurations() {
		List<BiomartServiceProviderConfig> defaults = new ArrayList<BiomartServiceProviderConfig>();
		// TODO: Defaults should come from a config/resource file
		defaults.add(new BiomartServiceProviderConfig(
				"http://www.biomart.org/biomart/martservice"));
		return defaults;
	}

}
