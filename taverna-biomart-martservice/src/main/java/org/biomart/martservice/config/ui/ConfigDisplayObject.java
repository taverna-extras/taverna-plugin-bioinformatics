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
