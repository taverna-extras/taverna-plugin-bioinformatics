/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.partition.AbstractActivityItem;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * 
 * @author Alex Nenadic
 * @author Stuart Owen
 *
 */
public class BiomobyObjectActivityItem extends AbstractActivityItem {

	String registryUrl;
	String serviceName;
	String authorityName;
	String registryUri;
	
	public String getRegistryUri() {
		return registryUri;
	}

	public void setRegistryUri(String registryUri) {
		this.registryUri = registryUri;
	}

	public String getRegistryUrl() {
		return registryUrl;
	}

	public void setRegistryUrl(String registryUrl) {
		this.registryUrl = registryUrl;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
	
	@Override
	public Object getConfigBean() {
		BiomobyObjectActivityConfigurationBean bean = new BiomobyObjectActivityConfigurationBean();
		bean.setAuthorityName(getAuthorityName());
		bean.setServiceName(getServiceName());
		bean.setMobyEndpoint(getRegistryUrl());
		return bean;
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(BiomobyObjectActivityItem.class.getResource("/biomoby_object.png"));
	}

	@Override
	public Activity<?> getUnconfiguredActivity() {
		return new BiomobyObjectActivity();
	}
	
	public String getType() {
		return "Biomoby Object";
	}

}
