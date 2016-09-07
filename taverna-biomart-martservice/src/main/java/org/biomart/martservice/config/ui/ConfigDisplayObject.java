package org.biomart.martservice.config.ui;
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
