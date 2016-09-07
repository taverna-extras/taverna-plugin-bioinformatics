/*******************************************************************************
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.servicedescriptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityIcon;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyQueryHelper;
import net.sf.taverna.t2.activities.biomoby.ui.AddBiomobyDialogue;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.CustomizedConfigurePanelProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;

import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;

public class BiomobyServiceProvider extends
		AbstractConfigurableServiceProvider<BiomobyServiceProviderConfig> implements
		CustomizedConfigurePanelProvider<BiomobyServiceProviderConfig> {

	private static final URI providerId = URI
			.create("http://taverna.sf.net/2010/service-provider/biomoby");
	private ServiceDescriptionRegistry serviceDescriptionRegistry;

	public BiomobyServiceProvider() {
		super(new BiomobyServiceProviderConfig());
	}

	public void findServiceDescriptionsAsync(FindServiceDescriptionsCallBack callBack) {
		try {
			// constructor throws exception if it cannot communicate
			// with the registry
			BiomobyQueryHelper helper = new BiomobyQueryHelper(getConfiguration().getEndpoint()
					.toASCIIString(), getConfiguration().getNamespace().toASCIIString());
			helper.findServiceDescriptionsAsync(callBack);
		} catch (MobyException ex) {
			callBack.fail("Could not connect to Biomoby endpoint "
					+ getConfiguration().getEndpoint(), ex);
		}
	}

	@Override
	public List<BiomobyServiceProviderConfig> getDefaultConfigurations() {

		List<BiomobyServiceProviderConfig> defaults = new ArrayList<BiomobyServiceProviderConfig>();

		// If defaults have failed to load from a configuration file then load them here.
		if (!serviceDescriptionRegistry.isDefaultSystemConfigurableProvidersLoaded()) {
			defaults.add(new BiomobyServiceProviderConfig(CentralImpl.DEFAULT_ENDPOINT,
					CentralImpl.DEFAULT_NAMESPACE));
		} // else return an empty list

		return defaults;
	}

	public String getName() {
		return "Biomoby service";
	}

	public Icon getIcon() {
		return BiomobyActivityIcon.getBiomobyIcon();
	}

	@Override
	public String toString() {
		return getName() + " " + getConfiguration().getEndpoint();
	}

	@SuppressWarnings("serial")
	public void createCustomizedConfigurePanel(
			final CustomizedConfigureCallBack<BiomobyServiceProviderConfig> callBack) {
		AddBiomobyDialogue addBiomobyDialogue = new AddBiomobyDialogue() {
			@Override
			protected void addRegistry(String registryEndpoint, String registryURI) {
				BiomobyServiceProviderConfig providerConfig = new BiomobyServiceProviderConfig(
						registryEndpoint, registryURI);
				callBack.newProviderConfiguration(providerConfig);
			}
		};
		addBiomobyDialogue.setVisible(true);
	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		List<String> result;
		result = Arrays.asList(getConfiguration().getEndpoint().toString());
		return result;
	}

	public String getId() {
		return providerId.toString();
	}

	public void setServiceDescriptionRegistry(ServiceDescriptionRegistry serviceDescriptionRegistry) {
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
	}

}
