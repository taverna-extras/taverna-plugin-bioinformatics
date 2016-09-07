package org.biomart.martservice.config.event;

import java.util.EventObject;

import org.biomart.martservice.MartDataset;

/**
 * An event which indicates that a <code>QueryComponent</code> has been
 * selected, deselected or its value has been modified.
 *
 * @author David Withers
 */
public class QueryComponentEvent extends EventObject {
	private static final long serialVersionUID = -7576317475836030298L;

	private String name;

	private MartDataset dataset;

	private String value;

	/**
	 * Constructs a new <code>QueryComponentEvent</code> instance.
	 *
	 * @param source
	 *            the source of the event
	 * @param name
	 *            the name of the attribute or filter affected by this event
	 * @param dataset
	 *            the dataset containing the attribute or filter affected by
	 *            this event
	 */
	public QueryComponentEvent(Object source, String name, MartDataset dataset) {
		this(source, name, dataset, null);
	}

	/**
	 * Constructs a new <code>QueryComponentEvent</code> instance.
	 *
	 * @param source
	 *            the source of the event
	 * @param name
	 *            the name of the attribute or filter affected by this event
	 * @param dataset
	 *            the dataset containing the attribute or filter affected by
	 *            this event
	 * @param value
	 *            the value of the filter affected by this event
	 */
	public QueryComponentEvent(Object source, String name, MartDataset dataset,
			String value) {
		super(source);
		this.name = name;
		this.dataset = dataset;
		this.value = value;
	}

	/**
	 * Returns the name of the attribute or filter affected by this event.
	 *
	 * @return the name of the attribute or filter affected by this event.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the dataset containing the attribute or filter affected by this
	 * event.
	 *
	 * @return the dataset containing the attribute or filter affected by this
	 *         event.
	 */
	public MartDataset getDataset() {
		return dataset;
	}

	/**
	 * Returns the value of the filter affected by this event.
	 *
	 * @return the value of the filter affected by this event.
	 */
	public String getValue() {
		return value;
	}

}
