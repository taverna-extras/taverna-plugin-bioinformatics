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
 * Filename           $RCSfile: Link.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:09 $
 *               by   $Author: davidwithers $
 * Created on 26-Apr-2006
 *****************************************************************/
package org.biomart.martservice.query;

/**
 * Class for creating link elements of mart queries.
 * 
 * @author David Withers
 */
public class Link {
	private String source;

	private String target;

	private String defaultLink;

	private Query containingQuery;

	/**
	 * Constructs an instance of a <code>Link</code>.
	 * 
	 * @param source
	 *            the source dataset of the <code>Link</code>
	 * @param target
	 *            the target dataset of the <code>Link</code>
	 * @param defaultLink
	 *            the ID the links the datasets
	 */
	public Link(String source, String target, String defaultLink) {
		this.source = source;
		this.target = target;
		this.defaultLink = defaultLink;
	}

	/**
	 * Constructs an instance of a <code>Link</code> which is a copy of
	 * another <code>Link</code>.
	 * 
	 * @param filter
	 *            the <code>Link</code> to copy
	 */
	public Link(Link link) {
		this.source = link.source;
		this.target = link.target;
		this.defaultLink = link.defaultLink;
	}

	/**
	 * Returns the defaultLink.
	 * 
	 * @return the defaultLink.
	 */
	public String getDefaultLink() {
		return defaultLink;
	}

	/**
	 * @param defaultLink
	 *            the defaultLink to set.
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}

	/**
	 * Returns the source dataset.
	 * 
	 * @return the source dataset.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source dataset.
	 * 
	 * @param source
	 *            the source dataset to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the target dataset.
	 * 
	 * @return the target dataset.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target dataset.
	 * 
	 * @param target
	 *            the target dataset to set.
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Returns the containingQuery.
	 * 
	 * @return the containingQuery.
	 */
	public Query getContainingQuery() {
		return containingQuery;
	}

	/**
	 * Sets the containingQuery.
	 * 
	 * @param containingQuery
	 *            the containingQuery to set.
	 */
	void setContainingQuery(Query containingQuery) {
		this.containingQuery = containingQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null) {
			Link link = (Link) obj;
			result = ((source == null && link.source == null) || source
					.equals(link.source))
					&& ((target == null && link.target == null) || target
							.equals(link.target))
					&& ((defaultLink == null && link.defaultLink == null) || defaultLink
							.equals(link.defaultLink));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return source.hashCode() + target.hashCode() + defaultLink.hashCode();
	}

}
