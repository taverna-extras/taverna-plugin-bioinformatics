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
package net.sf.taverna.t2.activities.biomart.query;

import java.util.Arrays;

import javax.swing.JOptionPane;

import net.sf.taverna.t2.partition.ActivityQuery;

import org.apache.log4j.Logger;
import org.biomart.martservice.MartDataset;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartRegistry;
import org.biomart.martservice.MartService;
import org.biomart.martservice.MartURLLocation;

public class BiomartQuery extends ActivityQuery {
	
	private static Logger logger = Logger.getLogger(BiomartQuery.class);

	public BiomartQuery(String property) {
		super(property);
	}

	@Override
	public void doQuery() {
		try {
			MartService martService = MartService
					.getMartService(getBiomartServiceLocation(getProperty()));
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
							BiomartActivityItem item = new BiomartActivityItem();
							item.setUrl(martService.getLocation());
							item.setLocation(martURLLocation.getDisplayName());
							item.setDataset(dataset.getName());
							MartQuery biomartQuery = new MartQuery(martService,
									dataset, "taverna");
							item.setMartQuery(biomartQuery);
							add(item);
						}
					}
				}
			}
		} catch (Exception ex) {
			String message = "There was an error querying Biomart at: "+getProperty();
			logger.error(message,ex);
			JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE); 			
		}
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

}
