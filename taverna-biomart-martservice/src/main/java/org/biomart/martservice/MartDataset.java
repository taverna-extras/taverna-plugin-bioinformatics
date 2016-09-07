package org.biomart.martservice;

import java.util.Comparator;

/**
 * The dataset returned by a BioMart web service.
 * 
 * @author David Withers
 */
public class MartDataset {
	private static final Comparator<MartDataset> displayComparator = new MartDatasetComparator();

	private String type;

	private String name;

	private String displayName;

	private boolean visible;

	private long initialBatchSize;

	private long maximumBatchSize;

	private String interfaceValue;

	private String modified;

	private MartURLLocation martURLLocation;

	/**
	 * Returns the displayName.
	 * 
	 * @return the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the displayName.
	 * 
	 * @param displayName
	 *            the displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns the initialBatchSize.
	 * 
	 * @return the initialBatchSize.
	 */
	public long getInitialBatchSize() {
		return initialBatchSize;
	}

	/**
	 * Sets the initialBatchSize.
	 * 
	 * @param initialBatchSize
	 *            the initialBatchSize to set.
	 */
	public void setInitialBatchSize(long initialBatchSize) {
		this.initialBatchSize = initialBatchSize;
	}

	/**
	 * Returns the maximumBatchSize.
	 * 
	 * @return the maximumBatchSize.
	 */
	public long getMaximumBatchSize() {
		return maximumBatchSize;
	}

	/**
	 * Sets the maximumBatchSize.
	 * 
	 * @param maximumBatchSize
	 *            the maximumBatchSize to set.
	 */
	public void setMaximumBatchSize(long maximumBatchSize) {
		this.maximumBatchSize = maximumBatchSize;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the type.
	 * 
	 * @return the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the visible flag.
	 * 
	 * @return the visible flag
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible flag.
	 * 
	 * @param visible
	 *            the visible flag
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns the modified date.
	 *
	 * @return the modified date
	 */
	public String getModified() {
		return modified;
	}

	/**
	 * Sets the modified date.
	 *
	 * @param modified the new modified date
	 */
	public void setModified(String modified) {
		this.modified = modified;
	}

	/**
	 * Returns the interface.
	 *
	 * @return the interface
	 */
	public String getInterface() {
		return interfaceValue;
	}

	/**
	 * Sets the interface.
	 *
	 * @param interfaceValue the new interface
	 */
	public void setInterface(String interfaceValue) {
		this.interfaceValue = interfaceValue;
	}

	/**
	 * Returns the martURLLocation.
	 * 
	 * @return the martURLLocation
	 */
	public MartURLLocation getMartURLLocation() {
		return martURLLocation;
	}

	/**
	 * Sets the martURLLocation.
	 * 
	 * @param martURLLocation
	 *            the martURLLocation to set.
	 */
	public void setMartURLLocation(MartURLLocation martURLLocation) {
		this.martURLLocation = martURLLocation;
	}

	/**
	 * Returns the virtualSchema of the martURLLocation. If the martURLLocation
	 * is null, null is returned.
	 * 
	 * @return the virtualSchema of the martURLLocation
	 */
	public String getVirtualSchema() {
		MartURLLocation martURLLocation = getMartURLLocation();
		if (martURLLocation != null) {
			return martURLLocation.getVirtualSchema();
		} else {
			return null;
		}
	}

	/**
	 * Returns the qualified name of this dataset in the format
	 * 'virtualSchemaName.datasetName'. If there is no virtualSchema the
	 * datasetName is returned.
	 * 
	 * @return the qualified name of this dataset
	 */
	public String getQualifiedName() {
		String schema = getVirtualSchema();
		if (schema != null) {
			return schema + "." + getName();
		} else {
			return getName();
		}
	}

	/**
	 * Returns the display name.
	 * 
	 * @return the display name
	 */
	public String toString() {
		return getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME
				* result
				+ ((getQualifiedName() == null) ? 0 : getQualifiedName()
						.hashCode());
		return result;
	}

	/**
	 * Tests equality based on the qualified name.
	 * 
	 * @return true if the objects are equal based on the qualified name
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MartDataset other = (MartDataset) obj;
		if (getQualifiedName() == null) {
			if (other.getQualifiedName() != null)
				return false;
		} else if (!getQualifiedName().equals(other.getQualifiedName()))
			return false;
		return true;
	}

	/**
	 * Returns a Comparator that compares MartDatasets based on their display
	 * names.
	 * 
	 * @return the display comparator
	 */
	public static Comparator<MartDataset> getDisplayComparator() {
		return displayComparator;
	}

}

/**
 * Comparator that compares MartDatasets based on their display names.
 * 
 * @author David Withers
 */
class MartDatasetComparator implements Comparator<MartDataset> {

	/**
	 * Compares two MartDatasets based on their display names.
	 * 
	 * @param martDataset1
	 *            the first MartDataset to be compared
	 * @param martDataset2
	 *            the second MartDataset to be compared
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second
	 */
	public int compare(MartDataset martDataset1, MartDataset martDataset2) {
		return martDataset1.getDisplayName().compareTo(martDataset2.getDisplayName());
	}

}
