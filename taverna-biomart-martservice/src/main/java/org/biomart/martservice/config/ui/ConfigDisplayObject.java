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
 * Filename           $RCSfile: ConfigDisplayObject.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:09 $
 *               by   $Author: davidwithers $
 * Created on 17-Mar-2006
 *****************************************************************/
package org.biomart.martservice.config.ui;

import java.awt.Component;

import org.ensembl.mart.lib.config.BaseNamedConfigurationObject;

/**
 * An object containing a configuration object and it's graphical component.
 * 
 * @author David Withers
 */
public class ConfigDisplayObject {
	private BaseNamedConfigurationObject configObject;

	private Component component;

	/**
	 * Constructs an instance of a <code>ConfigDisplayObject</code> with the
	 * specified configuration object and a <code>null</code> component.
	 * 
	 * @param configObject
	 *            the configuration object; must not be <code>null</code>
	 */
	public ConfigDisplayObject(BaseNamedConfigurationObject configObject) {
		this(configObject, null);
	}

	/**
	 * Constructs an instance of a <code>ConfigDisplayObject</code> with the
	 * specified configuration object and component.
	 * 
	 * @param configObject
	 *            the configuration object; must not be <code>null</code>
	 * @param component
	 *            the component
	 */
	public ConfigDisplayObject(BaseNamedConfigurationObject configObject,
			Component component) {
		if (configObject == null) {
			throw new IllegalArgumentException(
					"Parameter 'configObject' must not be null");
		}
		this.configObject = configObject;
		this.component = component;
	}

	/**
	 * Returns the display name.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return configObject.getDisplayName();
	}

	/**
	 * Returns the internal name.
	 * 
	 * @return the internal name
	 */
	public String getInternalName() {
		return configObject.getInternalName();
	}

	/**
	 * Returns the display name.
	 * 
	 * @return the display name
	 */
	public String toString() {
		return getDisplayName();
	}

	/**
	 * Returns the configuration object.
	 * 
	 * @return the configuration object
	 */
	public BaseNamedConfigurationObject getConfigObject() {
		return configObject;
	}

	/**
	 * Returns the component.
	 * 
	 * @return the component.
	 */
	public Component getComponent() {
		return component;
	}

}
