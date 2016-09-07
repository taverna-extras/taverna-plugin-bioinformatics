package org.biomart.martservice;
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

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * The link between two MartDatasets and the list of possible the link IDs that
 * could join the source and target MartDataset.
 * 
 * @author David Withers
 */
public class DatasetLink {
	private static final Comparator<DatasetLink> displayComparator = new DatasetLinkComparator();

	private MartDataset sourceDataset;

	private MartDataset targetDataset;

	private Set<String> links = new HashSet<String>();

	/**
	 * Constructs an instance of an <code>DatasetLink</code> with the
	 * specified source and target.
	 * 
	 * @param sourceDataset
	 *            the link source
	 * @param targetDataset
	 *            the link target
	 */
	public DatasetLink(MartDataset sourceDataset, MartDataset targetDataset) {
		this.sourceDataset = sourceDataset;
		this.targetDataset = targetDataset;
	}

	/**
	 * Returns the source Dataset.
	 * 
	 * @return the source Dataset
	 */
	public MartDataset getSourceDataset() {
		return sourceDataset;
	}

	/**
	 * Sets the source Dataset.
	 * 
	 * @param sourceDataset
	 *            the sourceDataset to set
	 */
	public void setSourceDataset(MartDataset sourceDataset) {
		this.sourceDataset = sourceDataset;
	}

	/**
	 * Returns the target Dataset.
	 * 
	 * @return the target Dataset
	 */
	public MartDataset getTargetDataset() {
		return targetDataset;
	}

	/**
	 * Sets the target Dataset.
	 * 
	 * @param targetDataset
	 *            the target Dataset to set
	 */
	public void setTargetDataset(MartDataset targetDataset) {
		this.targetDataset = targetDataset;
	}

	/**
	 * Returns the link IDs that could join the source and target MartDatasets.
	 * 
	 * @return the link IDs that could join the source and target MartDatasets
	 */
	public String[] getLinks() {
		return links.toArray(new String[links.size()]);
	}

	/**
	 * Adds a link ID.
	 * 
	 * @param link
	 *            the link ID to add.
	 */
	public void addLink(String link) {
		links.add(link);
	}

	/**
	 * Returns true if this DatasetLink has link IDs.
	 * 
	 * @return true if this DatasetLink has link IDs
	 */
	public boolean hasLinks() {
		return !links.isEmpty();
	}

	public String toString() {
		return "[" + sourceDataset.getMartURLLocation().getDisplayName() + "] "
				+ sourceDataset.getDisplayName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((sourceDataset == null) ? 0 : sourceDataset.hashCode());
		result = PRIME * result + ((targetDataset == null) ? 0 : targetDataset.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DatasetLink other = (DatasetLink) obj;
		if (sourceDataset == null) {
			if (other.sourceDataset != null)
				return false;
		} else if (!sourceDataset.equals(other.sourceDataset))
			return false;
		if (targetDataset == null) {
			if (other.targetDataset != null)
				return false;
		} else if (!targetDataset.equals(other.targetDataset))
			return false;
		return true;
	}

	/**
	 * Returns a Comparator that compares DatasetLinks based on the display name
	 * of the source dataset and the display name of the MartURLLocation
	 * containing the source dataset.
	 * 
	 * @return the display comparator
	 */
	public static Comparator<DatasetLink> getDisplayComparator() {
		return displayComparator;
	}

}

/**
 * Comparator that compares DatasetLinks based on the display name of the source
 * MartDataset and the display name of the MartURLLocation containing the source
 * MartDataset.
 * 
 * @author David Withers
 */
class DatasetLinkComparator implements Comparator<DatasetLink> {

	/**
	 * Compares two DatasetLinks based on the display name of the source dataset
	 * and the display name of the MartURLLocation containing the source
	 * dataset.
	 * 
	 * @param o1
	 *            the first DatasetLink to be compared
	 * @param o2
	 *            the second DatasetLink to be compared
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second
	 */
	public int compare(DatasetLink o1, DatasetLink o2) {
		MartDataset ds1 = o1.getSourceDataset();
		MartDataset ds2 = o2.getSourceDataset();
		int result = ds1.getDisplayName().compareTo(ds2.getDisplayName());
		if (result == 0) {
			result = ds1.getMartURLLocation().getDisplayName().compareTo(
					ds2.getMartURLLocation().getDisplayName());
		}
		return result;
	}

}
