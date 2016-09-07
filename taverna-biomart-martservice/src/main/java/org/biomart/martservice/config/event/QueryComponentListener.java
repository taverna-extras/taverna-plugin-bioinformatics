package org.biomart.martservice.config.event;

import java.util.EventListener;

/**
 * The listener interface for receiving QueryComponent events.
 *
 * @author David Withers
 */
public interface QueryComponentListener extends EventListener {

	/**
	 * Invoked when a <code>QueryComponent</code> for an attribute is
	 * selected.
	 *
	 * @param event the query component event
	 */
	public void attributeAdded(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for an attribute is
	 * deselected.
	 *
	 * @param event the query component event
	 */
	public void attributeRemoved(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a filter is selected.
	 *
	 * @param event the query component event
	 */
	public void filterAdded(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a filter is deselected.
	 *
	 * @param event the query component event
	 */
	public void filterRemoved(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a filter is changed.
	 *
	 * @param event the query component event
	 */
	public void filterChanged(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a link is selected.
	 *
	 * @param event the query component event
	 */
	public void linkAdded(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a link is deselected.
	 *
	 * @param event the query component event
	 */
	public void linkRemoved(QueryComponentEvent event);

	/**
	 * Invoked when a <code>QueryComponent</code> for a dataset link id is
	 * changed.
	 *
	 * @param event the query component event
	 */
	public void linkChanged(QueryComponentEvent event);

}
