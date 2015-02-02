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
 * Filename           $RCSfile: QueryComponent.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/12/13 11:38:56 $
 *               by   $Author: davidwithers $
 * Created on 03-Apr-2006
 *****************************************************************/
package org.biomart.martservice.config.ui;

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
