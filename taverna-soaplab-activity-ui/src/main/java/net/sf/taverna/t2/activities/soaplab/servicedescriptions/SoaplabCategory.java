/*
package net.sf.taverna.t2.activities.soaplab.servicedescriptions;

import java.util.ArrayList;
import java.util.List;

public class SoaplabCategory {
	
	private String category;
	private List<String> services = new ArrayList<String>();
	
	public SoaplabCategory(String category) {
		this.category=category;
	}		
	
	public boolean addService(String service) {
		return services.add(service);
	}

	public String getCategory() {
		return category;
	}

	public List<String> getServices() {
		return services;
	}
	
}
