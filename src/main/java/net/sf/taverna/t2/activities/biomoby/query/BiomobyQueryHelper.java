/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.taverna.raven.appconfig.ApplicationRuntime;
import net.sf.taverna.t2.activities.biomoby.GetOntologyThread;
import net.sf.taverna.t2.activities.biomoby.servicedescriptions.BiomobyServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider.FindServiceDescriptionsCallBack;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralDigestCachedImpl;
import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyResourceRef;
import org.biomoby.shared.MobyService;
import org.w3c.dom.Document;

public class BiomobyQueryHelper {

	private static Logger log = Logger.getLogger(BiomobyQueryHelper.class);

	private String registryNamespace;
	private String registryEndpoint;

	private CentralDigestCachedImpl central;

	private String DEFAULT_REGISTRY_ENDPOINT = CentralImpl.DEFAULT_ENDPOINT;

	private String DEFAULT_REGISTRY_NAMESPACE = CentralImpl.DEFAULT_NAMESPACE;

	private String remoteDatatypeRdfUrl = null;

	private String remoteServiceRdfUrl = null;

	private static final String CACHE_NAME = "moby-cache";

	private ApplicationRuntime applicationRuntime = ApplicationRuntime
			.getInstance();

	public BiomobyQueryHelper(String registryEndpoint, String registryNamespace)
			throws MobyException {
		try {
			if (registryNamespace != null)
				this.registryNamespace = registryNamespace;
			else
				this.registryNamespace = DEFAULT_REGISTRY_NAMESPACE;
			if (registryEndpoint != null)
				this.registryEndpoint = registryEndpoint;
			else
				this.registryEndpoint = DEFAULT_REGISTRY_ENDPOINT;
			String tavernaHome = null;
			if (applicationRuntime.getApplicationHomeDir() != null) {
				tavernaHome = applicationRuntime.getApplicationHomeDir()
						.getAbsolutePath();
			}
			String cacheLoc = tavernaHome;
			if (cacheLoc == null || cacheLoc.trim().length() == 0)
				cacheLoc = "";
			if (!cacheLoc.endsWith(System.getProperty("file.separator")))
				cacheLoc += File.separator;

			central = new CentralDigestCachedImpl(this.registryEndpoint,
					this.registryNamespace, cacheLoc + CACHE_NAME);

		} catch (MobyException e) {
			// 
			log.warn("There was a problem in initializing the caching agent, therefor caching is disabled.",
							e);
		}
		// getRDFLocations();
		// now we try to speed up the loading of the datatypes ontology
		try {
			new GetOntologyThread(central.getRegistryEndpoint()).start();
		} catch (Exception e) {
			/* don't care if an exception occurs here ... */
		}
	}

	@SuppressWarnings("unused")
	private void getRDFLocations() {
		try {

			MobyResourceRef mrr[] = central.getResourceRefs();
			remoteDatatypeRdfUrl = null;
			for (int x = 0; x < mrr.length; x++) {
				MobyResourceRef ref = mrr[x];
				if (ref.getResourceName().equals("Object")) {
					remoteDatatypeRdfUrl = ref.getResourceLocation()
							.toExternalForm();
					continue;
				}
				if (ref.getResourceName().equals("ServiceInstance")) {
					remoteServiceRdfUrl = ref.getResourceLocation()
							.toExternalForm();
					continue;
				}
			}
		} catch (MobyException e) {
			return;
		}

		log.info("Service RDF @ "
				+ (remoteServiceRdfUrl == null ? "(not used)"
						: remoteServiceRdfUrl)
				+ ", "
				+ System.getProperty("line.separator")
				+ "\tObjects @ "
				+ (remoteDatatypeRdfUrl == null ? "(not used)"
						: remoteDatatypeRdfUrl));
		return;
	}

	/**
	 * 
	 * @return an ArrayList of BiomobyActivityItem
	 * @throws MobyException
	 *             if something goes wrong
	 */
	public synchronized ArrayList<BiomobyActivityItem> getServices()
			throws MobyException {
		central.updateCache(CentralDigestCachedImpl.CACHE_PART_SERVICES);
		MobyService[] services = central.getServices();
		SortedMap<String, SortedSet<MobyService>> map = new TreeMap<String, SortedSet<MobyService>>();
		for (MobyService service : services) {
			String authority = service.getAuthority();
			SortedSet<MobyService> set;
			if (map.containsKey(authority)) {
				set = map.remove(authority);
				set.add(service);
				map.put(authority, set);
			} else {
				set = new TreeSet<MobyService>(new Comparator<MobyService>() {
					public int compare(MobyService o1, MobyService o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				set.add(service);
				map.put(authority, set);
			}
		}
		ArrayList<BiomobyActivityItem> authorityList = new ArrayList<BiomobyActivityItem>();
		for (String authority_name : map.keySet()) {
			for (MobyService service : map.get(authority_name)) {
				String serviceName = service.getName();
				// if (service.getStatus() != MobyService.UNCHECKED) {
				// f.setAlive((service.getStatus() & MobyService.ALIVE) == 2);
				// }
				BiomobyActivityItem item = makeActivityItem(registryEndpoint,
						registryNamespace, authority_name, serviceName);
				item.setCategory(service.getCategory());
				item.setServiceType(service.getServiceType() == null ? "Service"
								: service.getServiceType().getName());
				authorityList.add(item);
			}
		}
		return authorityList;

	}

	private BiomobyActivityItem makeActivityItem(String url, String uri,
			String authorityName, String serviceName) {
		BiomobyActivityItem item = new BiomobyActivityItem();
		item.setAuthorityName(authorityName);
		item.setServiceName(serviceName);
		item.setRegistryUrl(url);
		item.setRegistryUri(uri);
		return item;
	}

	public String getRemoteDatatypeRdfUrl() {
		return remoteDatatypeRdfUrl;
	}

	public String getRemoteServiceRdfUrl() {
		return remoteServiceRdfUrl;
	}

	public static Document loadDocument(InputStream input) throws MobyException {
		try {
			DocumentBuilderFactory dbf = (DocumentBuilderFactory) DOCUMENT_BUILDER_FACTORIES
					.get();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return (db.parse(input));
		} catch (Exception e) {
			throw new MobyException("Problem with reading XML input: "
					+ e.toString(), e);
		}
	}

	public static ThreadLocal<DocumentBuilderFactory> DOCUMENT_BUILDER_FACTORIES = new ThreadLocal<DocumentBuilderFactory>() {
		protected synchronized DocumentBuilderFactory initialValue() {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			return dbf;
		}
	};

	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		try {
			central.updateCache(CentralDigestCachedImpl.CACHE_PART_SERVICES);
		} catch (MobyException ex) {
			callBack.fail("Can't update the Biomoby cache", ex);
			return;
		}
		MobyService[] services;
		try {
			services = central.getServices();
		} catch (MobyException ex) {
			callBack.fail("Can't get BioMoby services", ex);
			return;
		}
		List<BiomobyServiceDescription> serviceDescriptions = new ArrayList<BiomobyServiceDescription>();
		for (MobyService service : services) {
			BiomobyServiceDescription serviceDesc = new BiomobyServiceDescription();
			serviceDesc.setEndpoint(URI.create(registryEndpoint));
			serviceDesc.setNamespace(URI.create(registryNamespace));
			serviceDesc.setAuthorityName(service.getAuthority());
			serviceDesc.setServiceName(service.getName());
			serviceDesc.setCategory(service.getCategory());
			serviceDesc.setDescription(service.getDescription());
			String lsid = service.getLSID();
			if (lsid != null && lsid.length() > 0) {
				serviceDesc.setLSID(URI.create(lsid));
			}
			serviceDesc.setEmailContact(service.getEmailContact());
			serviceDesc.setServiceType(service.getServiceType() == null ? "Service"
							: service.getServiceType().getName());
			String signatureURL = service.getSignatureURL();
			if (signatureURL != null && signatureURL.length() > 0) {
				serviceDesc.setSignatureURI(URI.create(signatureURL));
			}
			serviceDescriptions.add(serviceDesc);
		}
		callBack.partialResults(serviceDescriptions);
		callBack.finished();
	}
}
