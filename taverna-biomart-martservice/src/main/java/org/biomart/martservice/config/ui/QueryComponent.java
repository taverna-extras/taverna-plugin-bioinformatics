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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import org.biomart.martservice.MartDataset;
import org.biomart.martservice.config.event.QueryComponentEvent;
import org.biomart.martservice.config.event.QueryComponentListener;
import org.ensembl.mart.lib.config.BaseNamedConfigurationObject;

/**
 * Abstract class for creating query configuration UI components.
 *
 * @author David Withers
 */
public abstract class QueryComponent extends JPanel {
	public static final int ATTRIBUTE = 0;

	public static final int FILTER = 1;

	public static final int LINK = 2;

	private Vector<QueryComponentListener> queryComponentListeners = new Vector<QueryComponentListener>();

	private MartDataset dataset;

	private String pointerDataset;

	private String name;

	private String value;

	private BaseNamedConfigurationObject configObject;

	protected AbstractButton selectorButton;

	public abstract int getType();

	/**
	 * Returns the dataset.
	 *
	 * @return the dataset.
	 */
	public MartDataset getDataset() {
		return dataset;
	}

	/**
	 * Sets the dataset.
	 *
	 * @param dataset
	 *            the dataset to set.
	 */
	public void setDataset(MartDataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Returns the pointerDataset.
	 *
	 * @return the pointerDataset
	 */
	public String getPointerDataset() {
		return pointerDataset;
	}

	/**
	 * Sets the pointerDataset.
	 *
	 * @param pointerDataset the new pointerDataset
	 */
	public void setPointerDataset(String pointerDataset) {
		this.pointerDataset = pointerDataset;
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
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getQualifiedName() {
		return getDataset().getName() + "." + getName();
	}

	public String getOldQualifiedName() {
		if (pointerDataset == null) {
			return getDataset().getName() + "." + getName();
		} else {
			return pointerDataset + "." + getName();
		}
	}

	/**
	 * Returns the value.
	 *
	 * @return the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the configObject.
	 *
	 * @return the configObject
	 */
	public BaseNamedConfigurationObject getConfigObject() {
		return configObject;
	}

	/**
	 * Sets the configObject.
	 *
	 * @param configObject
	 *            the configObject to set.
	 */
	public void setConfigObject(BaseNamedConfigurationObject configObject) {
		this.configObject = configObject;
	}

	/**
	 * Sets the selected state of this component.
	 *
	 * @param selected
	 *            <code>true</code> if this component is selected,
	 *            <code>false</code> otherwise
	 */
	public void setSelected(boolean selected) {
		if (selectorButton != null) {
			selectorButton.setSelected(selected);
		}
	}

	/**
	 * Sets the button used to select/deselect this QueryComponent.
	 *
	 * This function adds an ItemListener to the button and fires the
	 * appropriate QueryComponentEvent when the button is selected or
	 * deselected.
	 *
	 * @param button
	 *            the button used to select/deselect this QueryComponent.
	 */
	public void setSelectorButton(AbstractButton button) {
		selectorButton = button;
		button.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (getType() == ATTRIBUTE) {
						fireAttributeAdded(new QueryComponentEvent(
								QueryComponent.this, getName(), getDataset()));
					} else {
						fireFilterAdded(new QueryComponentEvent(
								QueryComponent.this, getName(), getDataset()));
					}
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					if (getType() == ATTRIBUTE) {
						fireAttributeRemoved(new QueryComponentEvent(
								QueryComponent.this, getName(), getDataset()));
					} else {
						fireFilterRemoved(new QueryComponentEvent(
								QueryComponent.this, getName(), getDataset()));
					}
				}
			}
		});
	}

	/**
	 * Adds the specified query component listener to receive query component
	 * events. If <code>listener</code> is null, no exception is thrown and no
	 * action is performed.
	 *
	 * @param listener
	 *            the query component listener
	 */
	public void addQueryComponentListener(QueryComponentListener listener) {
		queryComponentListeners.add(listener);
	}

	/**
	 * Removes the specified query component listener so that it no longer
	 * receives component query events. This method performs no function, nor
	 * does it throw an exception, if <code>listener</code> was not previously
	 * added to this component. If <code>listener</code> is null, no exception
	 * is thrown and no action is performed.
	 *
	 * @param listener
	 *            the query component listener
	 */
	public void removeQueryComponentListener(QueryComponentListener listener) {
		queryComponentListeners.remove(listener);
	}

	/**
	 * Fires an attribute added event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireAttributeAdded(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.attributeAdded(event);
		}
	}

	/**
	 * Fires an attribute removed event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireAttributeRemoved(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.attributeRemoved(event);
		}
	}

	/**
	 * Fires an filter added event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireFilterAdded(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.filterAdded(event);
		}
	}

	/**
	 * Fires an filter removed event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireFilterRemoved(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.filterRemoved(event);
		}
	}

	/**
	 * Fires an filter changed event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireFilterChanged(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.filterChanged(event);
		}
	}

	/**
	 * Fires an link added event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireLinkAdded(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.linkAdded(event);
		}
	}

	/**
	 * Fires an link removed event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireLinkRemoved(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.linkRemoved(event);
		}
	}

	/**
	 * Fires an link changed event.
	 *
	 * @param event the event to be fired
	 */
	protected void fireLinkChanged(QueryComponentEvent event) {
		for (QueryComponentListener listener : queryComponentListeners) {
			listener.linkChanged(event);
		}
	}

}
