/*******************************************************************************
 ******************************************************************************/
/*
 *****************************************************************/
package net.sf.taverna.t2.activities.soaplab.servicedescriptions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

/**
 * An agent to query Soaplab server to determine the available categories and services.
 * @author sowen
 *
 */

public class SoaplabScavengerAgent {
	
	private static Logger logger = Logger.getLogger(SoaplabScavengerAgent.class);
		
	/**
	 * Returns a list of soaplab categories, containing a list of their services.
	 * Throws MissingSoaplabException if an installation cannot be found.
	 */	
	public static List<SoaplabCategory> load(String base) throws MissingSoaplabException{
		List<SoaplabCategory> categories=new ArrayList<SoaplabCategory>();
		
		// Get the categories for this installation
		boolean foundAnInstallation = loadCategories(base + "AnalysisFactory",categories);
		
		// Yes, bitwise OR is on purpose, to make sure the second
		// loadCategories() is always run. Do NOT replace with
		// foundInstallation = foundInstallation || getCategories(..)
		foundAnInstallation |= loadCategories(base + "GowlabFactory",categories);
		if (!foundAnInstallation) {
			// Neither Soaplab nor Gowlab were found, probably a fault
			throw new MissingSoaplabException("Unable to locate a soaplab installation at \n" + base);
		}
		
		return categories;
		
	}
	
	
	private static boolean loadCategories(String categoryBase, List<SoaplabCategory>cats) {
		boolean foundSome = false;
		String[] categories;
		try {
			categories = (String[]) callWebService(categoryBase, "getAvailableCategories", new Object[0]);
		} catch (Exception e) {
			logger.debug("Missing category: "+categoryBase, e);
			return false;
		}
		// Iterate over all the categories, creating new child nodes
		for (int i = 0; i < categories.length; i++) {
			String[] services;
			try {
				services = (String[]) callWebService(categoryBase, "getAvailableAnalysesInCategory", new Object[] {categories[i]});
			} catch (Exception e) {
				logger.info("Skipping category " + categories[i], e);
				continue;
			}
			if (services.length == 0) {
				// Avoid creating empty treenodes
				continue;
			}
			
			SoaplabCategory category=new SoaplabCategory(categories[i]);
			cats.add(category);
			
			foundSome = true;
			// Iterate over the services
			for (int j = 0; j < services.length; j++) {
				category.addService(services[j]);
			}			
		}
		return foundSome;
	}	

	public static Object callWebService(String target, String operation,
			Object[] parameters) throws ServiceException, RemoteException {
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(target);
		// No need to do new Qname(operation) with unspecified namespaces
		call.setOperationName(operation);
		return call.invoke(parameters);
	}
}
