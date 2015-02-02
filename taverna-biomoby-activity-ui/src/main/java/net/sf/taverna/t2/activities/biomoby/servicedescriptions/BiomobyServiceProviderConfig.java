package net.sf.taverna.t2.activities.biomoby.servicedescriptions;

import java.net.URI;

import net.sf.taverna.t2.lang.beans.PropertyAnnotated;

public class BiomobyServiceProviderConfig extends PropertyAnnotated {

	private URI endpoint;

	private URI namespace;

	public BiomobyServiceProviderConfig() {
	}

	public BiomobyServiceProviderConfig(String endpoint, String namespace) {
		this.endpoint = URI.create(endpoint.trim());
		this.namespace = URI.create(namespace.trim());
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public URI getNamespace() {
		return namespace;
	}

	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}

	public void setNamespace(URI namespace) {
		this.namespace = namespace;
	}

}
