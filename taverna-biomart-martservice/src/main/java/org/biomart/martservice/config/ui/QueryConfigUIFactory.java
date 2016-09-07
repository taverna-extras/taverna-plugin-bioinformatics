package org.biomart.martservice.config.ui;

import java.awt.Component;

import org.biomart.martservice.MartServiceException;
import org.ensembl.mart.lib.config.AttributeCollection;
import org.ensembl.mart.lib.config.AttributeDescription;
import org.ensembl.mart.lib.config.AttributeGroup;
import org.ensembl.mart.lib.config.AttributeList;
import org.ensembl.mart.lib.config.AttributePage;
import org.ensembl.mart.lib.config.FilterCollection;
import org.ensembl.mart.lib.config.FilterDescription;
import org.ensembl.mart.lib.config.FilterGroup;
import org.ensembl.mart.lib.config.FilterPage;

/**
 * Interface for generating graphical components from <code>DatasetConfig</code>
 * objects.
 *
 * @author David Withers
 */
public interface QueryConfigUIFactory {

	public final static String SINGLE_SELECTION = "SINGLE";

	public final static String MULTIPLE_SELECTION = "MULTIPLE";

	public final static String LIST_SELECTION = "LIST";

	/**
	 * Generates a <code>Component</code> for a <code>DatasetConfig</code>.
	 *
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getDatasetConfigUI() throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>AttributePage</code>.
	 *
	 * @param attributePages
	 *            an array of <code>AttributePage</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributePagesUI(AttributePage[] attributePages,
			Object data) throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an <code>AttributePage</code>.
	 *
	 * @param attributePage
	 *            an <code>AttributePage</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributePageUI(AttributePage attributePage, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>AttributeGroup</code>.
	 *
	 * @param attributeGroups
	 *            an array of <code>AttributeGroup</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeGroupsUI(AttributeGroup[] attributeGroups,
			Object data) throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an <code>AttributeGroup</code>.
	 *
	 * @param attributeGroup
	 *            an <code>AttributeGroup</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeGroupUI(AttributeGroup attributeGroup,
			Object data) throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>AttributeCollection</code>.
	 *
	 * @param attributeCollections
	 *            an array of <code>AttributeCollection</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeCollectionsUI(
			AttributeCollection[] attributeCollections, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an
	 * <code>AttributeCollection</code>.
	 *
	 * @param attributeCollection
	 *            an <code>AttributeCollection</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeCollectionUI(
			AttributeCollection attributeCollection, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>AttributeDescription</code>.
	 *
	 * @param attributeDescriptions
	 *            an array of <code>AttributeDescription</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeDescriptionsUI(
			AttributeDescription[] attributeDescriptions, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an
	 * <code>AttributeDescription</code>.
	 *
	 * @param attributeDescription
	 *            an <code>AttributeDescription</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeDescriptionUI(
			AttributeDescription attributeDescription, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>AttributeList</code>.
	 *
	 * @param attributeLists
	 *            an array of <code>AttributeList</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeListsUI(
			AttributeList[] attributeLists, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an
	 * <code>AttributeList</code>.
	 *
	 * @param attributeList
	 *            an <code>AttributeList</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getAttributeListUI(
			AttributeList attributeList, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>FilterPage</code>.
	 *
	 * @param filterPages
	 *            an array of <code>FilterPage</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterPagesUI(FilterPage[] filterPages, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for a <code>FilterPage</code>.
	 *
	 * @param filterPage
	 *            a <code>FilterPage</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterPageUI(FilterPage filterPage, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>FilterGroup</code>.
	 *
	 * @param filterGroups
	 *            an array of <code>FilterGroup</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterGroupsUI(FilterGroup[] filterGroups, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for a <code>FilterGroup</code>.
	 *
	 * @param filterGroup
	 *            a <code>FilterGroup</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterGroupUI(FilterGroup filterGroup, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>FilterCollection</code>.
	 *
	 * @param filterCollections
	 *            an array of <code>FilterCollection</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterCollectionsUI(
			FilterCollection[] filterCollections, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for a <code>FilterCollection</code>.
	 *
	 * @param filterCollection
	 *            a <code>FilterCollection</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterCollectionUI(FilterCollection filterCollection,
			Object data) throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for an array of
	 * <code>FilterDescription</code>.
	 *
	 * @param filterDescriptions
	 *            an array of <code>FilterDescription</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterDescriptionsUI(
			FilterDescription[] filterDescriptions, Object data)
			throws MartServiceException;

	/**
	 * Generates a <code>Component</code> for a <code>FilterDescription</code>.
	 *
	 * @param filterDescription
	 *            a <code>FilterDescription</code>
	 * @param data
	 *            extra context information
	 * @return the generated <code>Component</code>
	 * @throws MartServiceException
	 *             if the MartService returns an error or is unavailable
	 */
	public Component getFilterDescriptionUI(
			FilterDescription filterDescription, Object data)
			throws MartServiceException;

}
