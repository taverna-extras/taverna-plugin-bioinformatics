package org.biomart.martservice.config.event;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
