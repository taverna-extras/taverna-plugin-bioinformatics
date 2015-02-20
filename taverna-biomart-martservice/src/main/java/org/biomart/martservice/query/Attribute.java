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
 * Filename           $RCSfile: Attribute.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008/03/04 16:43:40 $
 *               by   $Author: davidwithers $
 * Created on 26-Apr-2006
 *****************************************************************/
package org.biomart.martservice.query;

/**
 * Class for creating attribute elements of mart queries.
 * 
 * @author David Withers
 */
public class Attribute {
	private String name;

	private String attributes;
	
	private int attributesCount;

	private Dataset containingDataset;

	/**
	 * Constructs an instance of an <code>Attribute</code> with the specified name.
	 * 
	 * @param name
	 *            the name of the <code>Attribute</code>; must not be <code>null</code>
	 */
	public Attribute(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Parameter 'name' must not be null");
		}
		this.name = name;
	}

	/**
	 * Constructs an instance of an <code>Attribute</code> which is a copy of
	 * another <code>Attribute</code>.
	 * 
	 * @param attribute
	 *            the <code>Attribute</code> to copy; must not be <code>null</code>
	 */
	public Attribute(Attribute attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException("Parameter 'attribute' must not be null");
		}
		setName(attribute.getName());
		setAttributes(attribute.getAttributes());
	}

	/**
	 * Returns the name of the Attribute.
	 * 
	 * @return the name of the Attribute
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Attribute.
	 * 
	 * @param name
	 *            the name of the Attribute; must not be <code>null</code>
	 */
	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Parameter 'name' must not be null");
		}
		this.name = name;
	}

	/**
	 * Returns the qualified name of this <code>Attribute</code>.
	 * 
	 * The qualified name is <code>containingDatasetName.attributeName</code>
	 * or just <code>attributeName</code> if the <code>Attribute</code> is
	 * not in a <code>Dataset</code>.
	 * 
	 * @return the qualified name of this <code>Attribute</code>
	 */
	public String getQualifiedName() {
		if (containingDataset == null) {
			return name;
		} else {
			return containingDataset.getName() + "." + getName();
		}
	}

	/**
	 * Returns the component attributes as a comma separated list.
	 *
	 * @return the attributes as a comma separated list or null if there are no component attributes
	 */
	public String getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 */
	public void setAttributes(String attributes) {
		this.attributes = attributes;
		if (attributes == null) {
			attributesCount = 0;
		} else {
			attributesCount = attributes.split(",").length;
		}
	}
	
	/**
	 * Returns the number of component attributes.
	 * 
	 * @return the number of component attributes
	 */
	public int getAttributesCount() {
		return attributesCount;
	}

	/**
	 * Returns the Dataset containing this Attribute or null if it is not in a
	 * Dataset.
	 * 
	 * @return the Dataset containing this Attribute or null if it is not in a
	 *         Dataset
	 */
	public Dataset getContainingDataset() {
		return containingDataset;
	}

	/**
	 * Sets the containing Dataset.
	 * 
	 * @param dataset
	 *            the containing Dataset
	 */
	void setContainingDataset(Dataset dataset) {
		this.containingDataset = dataset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null) {
			Attribute attribute = (Attribute) obj;
			String qualifiedName = getQualifiedName();
			if (qualifiedName == null) {
				result = attribute.getQualifiedName() == null;
			} else {
				result =  qualifiedName.equals(attribute.getQualifiedName());
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getQualifiedName().hashCode();
	}

}
