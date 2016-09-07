package org.biomart.martservice.query;

/**
 * Class for creating link elements of mart queries.
 *
 * @author David Withers
 */
public class Link {
	private String source;

	private String target;

	private String defaultLink;

	private Query containingQuery;

	/**
	 * Constructs an instance of a <code>Link</code>.
	 *
	 * @param source
	 *            the source dataset of the <code>Link</code>
	 * @param target
	 *            the target dataset of the <code>Link</code>
	 * @param defaultLink
	 *            the ID the links the datasets
	 */
	public Link(String source, String target, String defaultLink) {
		this.source = source;
		this.target = target;
		this.defaultLink = defaultLink;
	}

	/**
	 * Constructs an instance of a <code>Link</code> which is a copy of
	 * another <code>Link</code>.
	 *
	 * @param filter
	 *            the <code>Link</code> to copy
	 */
	public Link(Link link) {
		this.source = link.source;
		this.target = link.target;
		this.defaultLink = link.defaultLink;
	}

	/**
	 * Returns the defaultLink.
	 *
	 * @return the defaultLink.
	 */
	public String getDefaultLink() {
		return defaultLink;
	}

	/**
	 * @param defaultLink
	 *            the defaultLink to set.
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}

	/**
	 * Returns the source dataset.
	 *
	 * @return the source dataset.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source dataset.
	 *
	 * @param source
	 *            the source dataset to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the target dataset.
	 *
	 * @return the target dataset.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target dataset.
	 *
	 * @param target
	 *            the target dataset to set.
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Returns the containingQuery.
	 *
	 * @return the containingQuery.
	 */
	public Query getContainingQuery() {
		return containingQuery;
	}

	/**
	 * Sets the containingQuery.
	 *
	 * @param containingQuery
	 *            the containingQuery to set.
	 */
	void setContainingQuery(Query containingQuery) {
		this.containingQuery = containingQuery;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null) {
			Link link = (Link) obj;
			result = ((source == null && link.source == null) || source
					.equals(link.source))
					&& ((target == null && link.target == null) || target
							.equals(link.target))
					&& ((defaultLink == null && link.defaultLink == null) || defaultLink
							.equals(link.defaultLink));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return source.hashCode() + target.hashCode() + defaultLink.hashCode();
	}

}
