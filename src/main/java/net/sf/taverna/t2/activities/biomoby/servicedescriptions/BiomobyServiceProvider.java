package net.sf.taverna.t2.activities.biomoby.servicedescriptions;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityIcon;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyQueryHelper;
import net.sf.taverna.t2.activities.biomoby.ui.AddBiomobyDialogue;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.CustomizedConfigurePanelProvider;

import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;

public class BiomobyServiceProvider extends
		AbstractConfigurableServiceProvider<BiomobyServiceProviderConfig>
		implements
		CustomizedConfigurePanelProvider<BiomobyServiceProviderConfig> {

	public BiomobyServiceProvider() {
		super(new BiomobyServiceProviderConfig());
	}

	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		try {
			BiomobyQueryHelper helper = new BiomobyQueryHelper(
					getConfiguration().getEndpoint().toASCIIString(),
					getConfiguration().getNamespace().toASCIIString());
			helper.findServiceDescriptionsAsync(callBack);
		} catch (MobyException ex) {
			callBack.fail("Could not connect to Biomoby endpoint "
					+ getConfiguration().getEndpoint(), ex);
		}
	}

	@Override
	public List<BiomobyServiceProviderConfig> getDefaultConfigurations() {
		return Arrays.asList(new BiomobyServiceProviderConfig(
				CentralImpl.DEFAULT_ENDPOINT,
				CentralImpl.DEFAULT_NAMESPACE));
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
			protected void addRegistry(String registryEndpoint,
					String registryURI) {
				BiomobyServiceProviderConfig providerConfig = new BiomobyServiceProviderConfig(
						registryEndpoint, registryURI);
				callBack.newProviderConfiguration(providerConfig);
			}
		};
		addBiomobyDialogue.setVisible(true);
	}

}
