package org.biomart.martservice.query;
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

/**
 * The listener interface for receiving Query events.
 *
 * @author David Withers
 */
public interface QueryListener {

	/**
	 * Invoked when an <code>Attribute</code> is added to a <code>Query</code>.
	 *
	 * @param attribute
	 *            the <code>Attribute</code> added.
	 */
	public void attributeAdded(Attribute attribute, Dataset dataset);

	/**
	 * Invoked when an <code>Attribute</code> is removed from a
	 * <code>Query</code>.
	 *
	 * @param attribute
	 *            the <code>Attribute</code> removed.
	 */
	public void attributeRemoved(Attribute attribute, Dataset dataset);

	/**
	 * Invoked when a <code>Filter</code> is added to a <code>Query</code>.
	 *
	 * @param filter
	 *            the <code>Filter</code> added.
	 */
	public void filterAdded(Filter filter, Dataset dataset);

	/**
	 * Invoked when a <code>Filter</code> is removed from a <code>Query</code>.
	 *
	 * @param filter
	 *            the <code>Filter</code> removed.
	 */
	public void filterRemoved(Filter filter, Dataset dataset);

	/**
	 * Invoked when the value of a <code>Filter</code> is changed.
	 *
	 * @param filter
	 *            the <code>Filter</code> whose value has changed.
	 */
	public void filterChanged(Filter filter, Dataset dataset);

	/**
	 * Invoked when a formatter is added to a <code>Query</code>.
	 *
	 * @param formatter
	 *            the formatter added.
	 */
	public void formatterAdded(String formatter);

	/**
	 * Invoked when a formatter is removed from a <code>Query</code>.
	 *
	 * @param formatter
	 *            the formatter removed.
	 */
	public void formatterRemoved(String formatter);

	/**
	 * Invoked when the value of the formatter is changed.
	 *
	 * @param filter
	 *            the new value of the formatter.
	 */
	public void formatterChanged(String formatter);

}
