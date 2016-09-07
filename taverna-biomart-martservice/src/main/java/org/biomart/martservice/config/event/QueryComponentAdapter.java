package org.biomart.martservice.config.event;

/**
 * An abstract adapter class for receiving <code>QueryComponent</code> events.
 * The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 *
 * @author David Withers
 */
public class QueryComponentAdapter implements QueryComponentListener {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void attributeAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#attributeRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void attributeRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterAdded(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterRemoved(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentListener#filterChanged(org.embl.ebi.escience.scuflworkers.biomartservice.config.QueryComponentEvent)
	 */
	public void filterChanged(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.biomart.martservice.config.event.QueryComponentListener#linkAdded(org.biomart.martservice.config.event.QueryComponentEvent)
	 */
	public void linkAdded(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.biomart.martservice.config.event.QueryComponentListener#linkRemoved(org.biomart.martservice.config.event.QueryComponentEvent)
	 */
	public void linkRemoved(QueryComponentEvent event) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.biomart.martservice.config.QueryComponentListener#linkChanged(org.biomart.martservice.config.QueryComponentEvent)
	 */
	public void linkChanged(QueryComponentEvent event) {
	}

}
