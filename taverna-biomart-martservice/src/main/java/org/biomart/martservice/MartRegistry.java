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
