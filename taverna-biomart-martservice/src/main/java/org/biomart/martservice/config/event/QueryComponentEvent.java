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

import java.util.EventObject;

import org.biomart.martservice.MartDataset;

/**
 * An event which indicates that a <code>QueryComponent</code> has been
 * selected, deselected or its value has been modified.
 *
 * @author David Withers
 */
public class QueryComponentEvent extends EventObject {
	private static final long serialVersionUID = -7576317475836030298L;

	private String name;

	private MartDataset dataset;

	private String value;

	/**
	 * Constructs a new <code>QueryComponentEvent</code> instance.
	 *
	 * @param source
	 *            the source of the event
	 * @param name
	 *            the name of the attribute or filter affected by this event
	 * @param dataset
	 *            the dataset containing the attribute or filter affected by
	 *            this event
	 */
	public QueryComponentEvent(Object source, String name, MartDataset dataset) {
		this(source, name, dataset, null);
	}

	/**
	 * Constructs a new <code>QueryComponentEvent</code> instance.
	 *
	 * @param source
	 *            the source of the event
	 * @param name
	 *            the name of the attribute or filter affected by this event
	 * @param dataset
	 *            the dataset containing the attribute or filter affected by
	 *            this event
	 * @param value
	 *            the value of the filter affected by this event
	 */
	public QueryComponentEvent(Object source, String name, MartDataset dataset,
			String value) {
		super(source);
		this.name = name;
		this.dataset = dataset;
		this.value = value;
	}

	/**
	 * Returns the name of the attribute or filter affected by this event.
	 *
	 * @return the name of the attribute or filter affected by this event.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the dataset containing the attribute or filter affected by this
	 * event.
	 *
	 * @return the dataset containing the attribute or filter affected by this
	 *         event.
	 */
	public MartDataset getDataset() {
		return dataset;
	}

	/**
	 * Returns the value of the filter affected by this event.
	 *
	 * @return the value of the filter affected by this event.
	 */
	public String getValue() {
		return value;
	}

}
