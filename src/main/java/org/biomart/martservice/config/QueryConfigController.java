/*
 * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 ****************************************************************
 * Source code information
 * -----------------------
 * Filename           $RCSfile: QueryConfigController.java,v $
 * Revision           $Revision: 1.4 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/12/13 11:38:57 $
 *               by   $Author: davidwithers $
 * Created on 27-Mar-2006
 *****************************************************************/
package org.biomart.martservice.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.biomart.martservice.MartQuery;
import org.biomart.martservice.config.event.QueryComponentEvent;
import org.biomart.martservice.config.event.QueryComponentListener;
import org.biomart.martservice.config.ui.QueryComponent;
import org.biomart.martservice.query.Attribute;
import org.biomart.martservice.query.Dataset;
import org.biomart.martservice.query.Filter;
import org.biomart.martservice.query.Query;
import org.biomart.martservice.query.QueryListener;
import org.ensembl.mart.lib.config.FilterDescription;

/**
 * Controls the interaction between graphical <code>QueryComponent</code>s
 * and <code>Query</code>s.
 * 
 * @author David Withers
 */
public class QueryConfigController {
	private static Logger logger = Logger
			.getLogger("org.biomart.martservice.config");

	private static QueryListener queryListener = new QueryHandler();

	private MartQuery martQuery;

	private Query query;

	private Map<String, Attribute> initialAttributeMap = new HashMap<String, Attribute>();

	private Map<String, Filter> initialFilterMap = new HashMap<String, Filter>();

	private Map<String, Attribute> nameToAttributeMap = new HashMap<String, Attribute>();

	private Map	<String, Filter> nameToFilterMap = new HashMap<String, Filter>();

	private QueryComponentHandler queryComponenHandler = new QueryComponentHandler();

	/**
	 * Constructs an instance of a <code>QueryConfigController</code>.
	 * 
	 * @param martQuery
	 */
	public QueryConfigController(MartQuery martQuery) {
		this.martQuery = martQuery;
		query = martQuery.getQuery();
		query.addQueryListener(queryListener);

		for (Attribute attribute : query.getAttributes()) {
			initialAttributeMap.put(attribute.getQualifiedName(), attribute);
			nameToAttributeMap.put(attribute.getQualifiedName(), attribute);
		}
		for (Filter filter : query.getFilters()) {
			initialFilterMap.put(filter.getQualifiedName(), filter);
			nameToFilterMap.put(filter.getQualifiedName(), filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		query.removeQueryListener(queryListener);
	}

	/**
	 * Returns the <code>MartQuery</code> under control.
	 * 
	 * @return the <code>MartQuery</code> under control
	 */
	public MartQuery getMartQuery() {
		return martQuery;
	}

	/**
	 * Removes a <code>QueryComponent</code> from the controller.
	 * 
	 * @param queryComponent
	 *            the <code>QueryComponent</code> to remove
	 */
	public void deregister(QueryComponent queryComponent) {
		queryComponent.removeQueryComponentListener(queryComponenHandler);
	}

	/**
	 * Registers a <code>QueryComponent</code> with the controller.
	 * 
	 * If the <code>Query</code> already contains an <code>Attribute</code>
	 * or <code>Filter</code> with the corresponding name the
	 * <code>QueryComponent</code> is set as selected.
	 * 
	 * @param queryComponent
	 *            the <code>QueryComponent</code> to register
	 */
	public void register(QueryComponent queryComponent) {
		if (queryComponent.getType() == QueryComponent.ATTRIBUTE) {
			getAttribute(queryComponent);

			// if query already contains attribute then set the component as
			// selected
			if (initialAttributeMap.containsKey(queryComponent
					.getQualifiedName())) {
				queryComponent.setSelected(true);
			}

		} else if (queryComponent.getType() == QueryComponent.FILTER) {
			Filter filter = getFilter(queryComponent);

			String value = filter.getValue();
			if (value != null) {
				if (filter.isBoolean()) {
					if ("excluded".equals(value)) {
						queryComponent.setValue("excluded");
					} else {
						queryComponent.setValue("only");
					}
				} else {
					queryComponent.setValue(value);
				}
			}

			if (initialFilterMap.containsKey(queryComponent.getQualifiedName())) {
				queryComponent.setSelected(true);
			}
		} else if (queryComponent.getType() == QueryComponent.LINK) {
			Iterator<String> linkedDatasets = martQuery.getLinkedDatasets().iterator();
			// only one linked dataset allowed for now
			if (linkedDatasets.hasNext()) {
				String dataset = linkedDatasets.next();
				queryComponent.setName(dataset);
				queryComponent.setValue(martQuery.getLink(dataset));
			}
		}

		queryComponent.addQueryComponentListener(queryComponenHandler);

	}

	/**
	 * Returns the <code>Attribute</code> mapped to the
	 * <code>QueryComponent</code>. If no <code>Attribute</code> is mapped
	 * a new <code>Attribute</code> is created and added to the map.
	 * 
	 * @param queryComponent
	 * @return
	 */
	private Attribute getAttribute(QueryComponent queryComponent) {
		String internalName = queryComponent.getQualifiedName();
		Attribute attribute = null;
		if (nameToAttributeMap.containsKey(internalName)) {
			attribute = (Attribute) nameToAttributeMap.get(internalName);
		} else {
			attribute = new Attribute(queryComponent.getName());
			if (queryComponent.getValue() != null) {
				attribute.setAttributes(queryComponent.getValue());
			}
			nameToAttributeMap.put(internalName, attribute);
		}
		return attribute;
	}

	/**
	 * Returns the <code>Filter</code> mapped to the
	 * <code>QueryComponent</code>. If no <code>Filter</code> is mapped a
	 * new <code>Filter</code> is created and added to the map.
	 * 
	 * @param queryComponent
	 * @return
	 */
	private Filter getFilter(QueryComponent queryComponent) {
		FilterDescription filterDescription = (FilterDescription) queryComponent
				.getConfigObject();
		String internalName = queryComponent.getQualifiedName();
		Filter filter;
		if (nameToFilterMap.containsKey(internalName)) {
			filter = (Filter) nameToFilterMap.get(internalName);
		} else {
			if ("boolean".equals(filterDescription.getType())) {
				if ("excluded".equals(queryComponent.getValue())) {
					filter = new Filter(queryComponent.getName(), "excluded", true);
				} else {
					filter = new Filter(queryComponent.getName(), "only", true);
				}
			} else {
				String defaultValue = filterDescription.getDefaultValue();
				if (defaultValue == null
						&& !QueryConfigUtils.isNestedList(filterDescription)) {
					// if there is no default value but there are options then
					// choose the first option as the filter value
//					Option[] options = filterDescription.getOptions();
//					if (options != null && options.length > 0) {
//						defaultValue = options[0].getValue();
//					} else {
						defaultValue = queryComponent.getValue();
//					}
				}
				filter = new Filter(queryComponent.getName(), defaultValue);
				if ("id_list".equals(filterDescription.getType())) {
					filter.setList(true);
				}
			}
			nameToFilterMap.put(internalName, filter);
		}
		return filter;
	}

	class QueryComponentHandler implements QueryComponentListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
		 */
		public void attributeAdded(QueryComponentEvent event) {
			Attribute attribute = (Attribute) nameToAttributeMap.get(event
					.getDataset().getName()
					+ "." + event.getName());
			synchronized (query) {
				martQuery.addAttribute(event.getDataset().getName(), attribute);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
		 */
		public void attributeRemoved(QueryComponentEvent event) {
			Attribute attribute = (Attribute) nameToAttributeMap.get(event
					.getDataset().getName()
					+ "." + event.getName());
			synchronized (query) {
				martQuery.removeAttribute(attribute);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
		 */
		public void filterAdded(QueryComponentEvent event) {
			Filter filter = (Filter) nameToFilterMap.get(event.getDataset()
					.getName()
					+ "." + event.getName());
			synchronized (query) {
				martQuery.addFilter(event.getDataset().getName(), filter);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
		 */
		public void filterRemoved(QueryComponentEvent event) {
			Filter filter = (Filter) nameToFilterMap.get(event.getDataset()
					.getName()
					+ "." + event.getName());
			synchronized (query) {
				martQuery.removeFilter(filter);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterChanged(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
		 */
		public void filterChanged(QueryComponentEvent event) {
			Filter filter = (Filter) nameToFilterMap.get(event.getDataset()
					.getName()
					+ "." + event.getName());
			synchronized (query) {
				filter.setValue(event.getValue());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.biomart.martservice.config.event.QueryComponentListener#linkAdded(org.biomart.martservice.config.event.QueryComponentEvent)
		 */
		public void linkAdded(QueryComponentEvent event) {
			martQuery.addLinkedDataset(event.getName(), event.getValue());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.biomart.martservice.config.event.QueryComponentListener#linkRemoved(org.biomart.martservice.config.event.QueryComponentEvent)
		 */
		public void linkRemoved(QueryComponentEvent event) {
			martQuery.removeLinkedDataset(event.getName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.biomart.martservice.config.QueryComponentListener#linkChanged(org.biomart.martservice.config.QueryComponentEvent)
		 */
		public void linkChanged(QueryComponentEvent event) {
			martQuery.changeLinkedDataset(event.getName(), event.getValue());
		}

	}

	static class QueryHandler implements QueryListener {

		public void attributeAdded(Attribute attribute, Dataset dataset) {
			logger.finer("Attribute Added " + attribute.getQualifiedName());
		}

		public void attributeRemoved(Attribute attribute, Dataset dataset) {
			logger.finer("Attribute Removed " + attribute.getQualifiedName());
		}

		public void filterAdded(Filter filter, Dataset dataset) {
			logger.finer("Filter Added " + filter.getQualifiedName() + " "
					+ filter.getValue());
		}

		public void filterRemoved(Filter filter, Dataset dataset) {
			logger.finer("Filter Removed " + filter.getQualifiedName());
		}

		public void filterChanged(Filter filter, Dataset dataset) {
			logger.finer("Filter Changed " + filter.getQualifiedName() + " "
					+ filter.getValue());
		}

		public void formatterAdded(String formatter) {
			logger.finer("Formatter Added " + formatter);
		}

		public void formatterRemoved(String formatter) {
			logger.finer("Formatter Removed " + formatter);
		}

		public void formatterChanged(String formatter) {
			logger.finer("Formatter Changed to " + formatter);
		}

	}

}