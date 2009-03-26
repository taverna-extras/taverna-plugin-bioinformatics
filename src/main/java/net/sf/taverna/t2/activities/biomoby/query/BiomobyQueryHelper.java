/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.query;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.taverna.raven.appconfig.ApplicationRuntime;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralDigestCachedImpl;
import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyResourceRef;
import org.biomoby.shared.MobyService;
import org.w3c.dom.Document;


public class BiomobyQueryHelper {

	private static Logger log = Logger.getLogger(BiomobyQueryHelper.class);

	private String REGISTRY_URI;
	private String REGISTRY_URL;

	private CentralDigestCachedImpl central;

	private String registryUrl = CentralImpl.DEFAULT_ENDPOINT;

	private String registryUri = CentralImpl.DEFAULT_NAMESPACE;

	private String REMOTE_DATATYPE_RDF_URL = null;

	private String REMOTE_SERVICE_RDF_URL = null;
	
	private static final String CACHE_NAME = "moby-cache";

	public BiomobyQueryHelper(String url, String uri) throws MobyException {
		try {
			if (uri != null)
				this.REGISTRY_URI = uri;
			if (url != null)
				this.REGISTRY_URL = url;
			String tavernaHome=null;
			if (ApplicationRuntime.getInstance().getApplicationHomeDir()!=null) {
				tavernaHome=ApplicationRuntime.getInstance().getApplicationHomeDir().getAbsolutePath();
			}
			String cacheLoc = tavernaHome;
			if (cacheLoc == null || cacheLoc.trim().length() == 0)
				cacheLoc = "";
			if (!cacheLoc.endsWith(System.getProperty("file.separator")))
				cacheLoc += File.separator;
			
			central = new CentralDigestCachedImpl(registryUrl, registryUri, cacheLoc + CACHE_NAME);

		} catch (MobyException e) {
			// 
			log
					.warn(
							"There was a problem in initializing the caching agent, therefor caching is disabled.",
							e);
		}
		//getRDFLocations();
	}

	@SuppressWarnings("unused")
	private void getRDFLocations() {
		try {

			MobyResourceRef mrr[] = central.getResourceRefs();
			REMOTE_DATATYPE_RDF_URL = null;
			for (int x = 0; x < mrr.length; x++) {
				MobyResourceRef ref = mrr[x];
				if (ref.getResourceName().equals("Object")) {
					REMOTE_DATATYPE_RDF_URL = ref.getResourceLocation()
							.toExternalForm();
					continue;
				}
				if (ref.getResourceName().equals("ServiceInstance")) {
					REMOTE_SERVICE_RDF_URL = ref.getResourceLocation()
							.toExternalForm();
					continue;
				}
			}
		} catch (MobyException e) {
			return;
		}

		log.info("Service RDF @ "
				+ (REMOTE_SERVICE_RDF_URL == null ? "(not used)"
						: REMOTE_SERVICE_RDF_URL)
				+ ", "
				+ System.getProperty("line.separator")
				+ "\tObjects @ "
				+ (REMOTE_DATATYPE_RDF_URL == null ? "(not used)"
						: REMOTE_DATATYPE_RDF_URL));
		return;
	}

	/**
	 * 
	 * @return an ArrayList of BiomobyActivityItem
	 * @throws MobyException
	 *             if something goes wrong
	 */
	public synchronized ArrayList<BiomobyActivityItem> getServices() throws MobyException {
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
//					if (service.getStatus() != MobyService.UNCHECKED) {
//						f.setAlive((service.getStatus() & MobyService.ALIVE) == 2);
//					}
				BiomobyActivityItem item = makeActivityItem(REGISTRY_URL, REGISTRY_URI, authority_name, serviceName);
				item.setCategory(service.getCategory());
				item.setServiceType(service.getServiceType() == null ? "Service" : service.getServiceType().getName());
				authorityList.add(item);
			}
		}
		return authorityList;

	}

	private BiomobyActivityItem makeActivityItem(String url, String uri, String authorityName,String serviceName) {
		BiomobyActivityItem item = new BiomobyActivityItem();
		item.setAuthorityName(authorityName);
		item.setServiceName(serviceName);
		item.setRegistryUrl(url);
		item.setRegistryUri(uri);
		return item;
	}
	
	/**
	 * @return the rEMOTE_DATATYPE_RDF_URL
	 */
	public String getREMOTE_DATATYPE_RDF_URL() {
		return REMOTE_DATATYPE_RDF_URL;
	}

	/**
	 * @return the rEMOTE_SERVICE_RDF_URL
	 */
	public String getREMOTE_SERVICE_RDF_URL() {
		return REMOTE_SERVICE_RDF_URL;
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
}
