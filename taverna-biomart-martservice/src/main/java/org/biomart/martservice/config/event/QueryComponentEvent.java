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
 * Filename           $RCSfile: QueryComponentEvent.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:06 $
 *               by   $Author: davidwithers $
 * Created on 03-Apr-2006
 *****************************************************************/
package org.biomart.martservice.config.event;

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
