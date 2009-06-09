/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester   
 * 
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *    
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *    
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.servicedescriptions;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.biomoby.actions.BiomobyAdvancedMenuAction;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyActivityIcon;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyQueryHelper;
import net.sf.taverna.t2.activities.biomoby.ui.AddBiomobyDialogue;
import net.sf.taverna.t2.lang.observer.Observable;
import net.sf.taverna.t2.lang.observer.Observer;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.CustomizedConfigurePanelProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionRegistry;
import net.sf.taverna.t2.servicedescriptions.events.RemovedProviderEvent;
import net.sf.taverna.t2.servicedescriptions.events.ServiceDescriptionRegistryEvent;
import net.sf.taverna.t2.servicedescriptions.impl.ServiceDescriptionRegistryImpl;

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
		
		
		BiomobyAdvancedMenuAction.addRegistry(
				getConfiguration().getEndpoint().toASCIIString(),
				getConfiguration().getNamespace().toASCIIString());
		
		// here we add a listener for events regarding BiomobyServiceProvider
		// TODO should find a better place for this so that we dont have to call addRegistry without
		// listening for the add provider event
		ServiceDescriptionRegistry serviceReg = ServiceDescriptionRegistryImpl.getInstance();
		serviceReg.addObserver(new Observer<ServiceDescriptionRegistryEvent>(){
			public void notify(
					Observable<ServiceDescriptionRegistryEvent> subject,
					ServiceDescriptionRegistryEvent event) throws Exception {
				// check for removed provider events
				if (event instanceof RemovedProviderEvent) {
					if (((RemovedProviderEvent) event).getProvider() instanceof BiomobyServiceProvider) {
						BiomobyServiceProvider bsp = (BiomobyServiceProvider) ((RemovedProviderEvent) event).getProvider();
						// remove our menu item
						BiomobyAdvancedMenuAction.removeRegistry(
								bsp.getConfiguration().getEndpoint().toASCIIString(),
								bsp.getConfiguration().getNamespace().toASCIIString());
					}
				}
				
			}});
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

	@Override
	protected List<? extends Object> getIdentifyingData() {
		List<String> result;
		result = Arrays.asList(getConfiguration().getEndpoint().toString());
		return result;
	}

}
