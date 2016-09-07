package org.biomart.martservice;

import java.util.ArrayList;
import java.util.List;

/**
 * The MartRegistry returned by a BioMart web service.
 *
 * @author David Withers
 */
public class MartRegistry {
	private List<MartURLLocation> martURLLocations = new ArrayList<MartURLLocation>();

	/**
	 * Returns the martURLLocations.
	 *
	 * @return the martURLLocations.
	 */
	public MartURLLocation[] getMartURLLocations() {
		return martURLLocations
				.toArray(new MartURLLocation[martURLLocations.size()]);
	}

	/**
	 * Adds a martURLLocation to the registry.
	 *
	 * @param martURLLocations
	 *            the martURLLocations to add.
	 */
	public void addMartURLLocation(MartURLLocation martURLLocation) {
		martURLLocations.add(martURLLocation);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((martURLLocations == null) ? 0 : martURLLocations.hashCode());
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
		final MartRegistry other = (MartRegistry) obj;
		if (martURLLocations == null) {
			if (other.martURLLocations != null)
				return false;
		} else if (!martURLLocations.equals(other.martURLLocations))
			return false;
		return true;
	}

}
