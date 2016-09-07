package org.biomart.martservice.config;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.EtchedBorder;

import org.biomart.martservice.MartDataset;
import org.biomart.martservice.MartService;
import org.biomart.martservice.MartServiceException;
import org.biomart.martservice.config.ui.QueryComponent;
import org.ensembl.mart.lib.config.AttributeDescription;
import org.ensembl.mart.lib.config.AttributePage;
import org.ensembl.mart.lib.config.BaseNamedConfigurationObject;
import org.ensembl.mart.lib.config.DatasetConfig;
import org.ensembl.mart.lib.config.FilterDescription;
import org.ensembl.mart.lib.config.Option;

/**
 * Utility class for configuration objects.
 *
 * @author David Withers
 */
public abstract class QueryConfigUtils {
	public static final String LINE_END = System.getProperty("line.separator");

	private static int DISPLAY_WIDTH = 35;

	public static String splitSentence(String sentence) {
		return splitSentence(sentence, DISPLAY_WIDTH);
	}

	public static String splitSentence(String sentence, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");

		int width = 0;
		String[] split = sentence.split(" ");
		for (int i = 0; i < split.length; i++) {
			if (width == 0) {
				sb.append(split[i]);
				width += split[i].length();
			} else if (width + split[i].length() + 1 > limit) {
				sb.append("<br>");
				sb.append(split[i]);
				width = split[i].length();
			} else {
				sb.append(" ");
				sb.append(split[i]);
				width += split[i].length() + 1;
			}
		}

		return sb.toString();
	}

	/**
	 * Returns name truncated to DISPLAY_WIDTH.
	 *
	 * @param name
	 * @return
	 */
	public static String truncateName(String name) {
		if (name.length() > DISPLAY_WIDTH) {
			return name.substring(0, DISPLAY_WIDTH);
		} else {
			return name;
		}
	}

	public static List<String> getOutputFormats(AttributePage attributePage) {
		List<String> outputFormats = new ArrayList<String>();

		String[] formats = attributePage.getOutFormats().split(",");
		for (int i = 0; i < formats.length; i++) {
			outputFormats.add(formats[i]);
		}
		return outputFormats;
	}

	/**
	 * Returns true if filterDescription has no options.
	 *
	 * @param filterDescription
	 * @return true if filterDescription has no options
	 */
	public static boolean isList(FilterDescription filterDescription) {
		return filterDescription.getOptions().length > 0;
	}

	/**
	 * Returns true if filterDescription has options and at least one option
	 * also has options.
	 *
	 * @param filterDescription
	 * @return true if filterDescription has options and at least one option
	 *         also has options
	 */
	public static boolean isNestedList(FilterDescription filterDescription) {
		Option[] options = filterDescription.getOptions();
		for (int i = 0; i < options.length; i++) {
			if (options[i].getOptions().length > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if filterDescription has options and the first option has a
	 * type equal to "boolean".
	 *
	 * @param filterDescription
	 * @return true if filterDescription has options and the first option has a
	 *         type equal to "boolean"
	 */
	public static boolean isBooleanList(FilterDescription filterDescription) {
		Option[] options = filterDescription.getOptions();
		if (options.length > 0) {
			if ("boolean".equals(options[0].getType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if filterDescription has options and the first option has a
	 * value equal to null.
	 *
	 * @param filterDescription
	 * @return true if filterDescription has options and the first option has a
	 *         value equal to null
	 */
	public static boolean isIdList(FilterDescription filterDescription) {
		Option[] options = filterDescription.getOptions();
		if (options.length > 0) {
			if (options[0].getValue() == null) {
				return true;
			}
		}
		return false;
	}

	public static Option[] fixOptionLength(Option[] options, int length) {
		if (options.length > length) {
			Option[] firstOptions = new Option[length];
			Option[] otherOptions = new Option[options.length - (length - 1)];
			for (int i = 0; i < length - 1; i++) {
				firstOptions[i] = options[i];
				fixOptionLength(options[i].getOptions(), length);
			}
			for (int i = length - 1; i < options.length; i++) {
				otherOptions[i - (length - 1)] = options[i];
			}
			Option newOption = new Option();
			newOption.setInternalName("more");
			newOption.setDisplayName("more");
			newOption.addOptions(fixOptionLength(otherOptions, length));
			firstOptions[length - 1] = newOption;
			return firstOptions;
		} else {
			return options;
		}
	}

	public static Component getOptionButton(
			FilterDescription filterDescription, QueryComponent queryComponent) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(new EtchedBorder());
		JMenu menu = new JMenu("browse");
		menu.setFont(menu.getFont().deriveFont(Font.PLAIN));
		menuBar.add(menu);
		Option[] options = fixOptionLength(filterDescription.getOptions(), 20);
		for (int i = 0; i < options.length; i++) {
			menu.add(getMenuItem(options[i], queryComponent));
		}
		return menuBar;
	}

	public static JMenuItem getMenuItem(final Option option,
			final QueryComponent queryComponent) {
		JMenuItem menuItem;

		Option[] options = option.getOptions();
		if (options.length > 0) {
			JMenu menu = new JMenu(option.getDisplayName());
			menu.setFont(menu.getFont().deriveFont(Font.PLAIN));
			for (int i = 0; i < options.length; i++) {
				menu.add(getMenuItem(options[i], queryComponent));
			}
			menuItem = menu;
		} else {
			menuItem = new JMenuItem(option.getDisplayName());
			menuItem.setFont(menuItem.getFont().deriveFont(Font.PLAIN));
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					queryComponent.setValue(option.getValue());
				}

			});
		}

		return menuItem;
	}

	/**
	 * Returns the dataset referenced by a configuration object or null if the
	 * configuration object does not reference a dataset.
	 *
	 * @param martService
	 *            the MartService to fetch the referenced dataset from
	 * @param referencedFromDataset
	 *            the datset containing the configuration object
	 * @param bnco
	 *            the configuration object
	 * @return the dataset referenced by a configuration object or null if the
	 *         configuration object does not reference a dataset.
	 * @throws MartServiceException
	 *             if and exception occurs while fetching the dataset
	 */
	public static MartDataset getReferencedDataset(MartService martService,
			MartDataset referencedFromDataset,
			BaseNamedConfigurationObject bnco, String softwareVersion)
			throws MartServiceException {
		if ("0.5".equals(softwareVersion)) {
			String pointerDataset = bnco.getAttribute("pointerDataset");
			if (pointerDataset != null) {
				return martService.getDataset(referencedFromDataset
						.getVirtualSchema(), pointerDataset);
			} else {
				return null;
			}
		} else {
			String[] splitName = bnco.getInternalName().split("\\.");
			if (splitName.length > 1) {
				return martService.getDataset(referencedFromDataset
						.getVirtualSchema(), splitName[0]);
			} else {
				return null;
			}
		}
	}

	/**
	 * Returns the filter description referenced by the filter description.
	 *
	 * @param martService
	 *            the MartService to fetch the referenced filter description
	 *            from
	 * @param dataset
	 *            the datset containing the referenced filter description
	 * @param filterDescription
	 *            the filter description
	 * @return the filter description referenced by the filter description
	 * @throws MartServiceException
	 *             if and exception occurs while fetching the filter description
	 */
	public static FilterDescription getReferencedFilterDescription(
			MartService martService, MartDataset dataset,
			FilterDescription filterDescription, String softwareVersion)
			throws MartServiceException {
		if ("0.5".equals(softwareVersion)) {
			String pointerFilter = filterDescription
					.getAttribute("pointerFilter");
			if (pointerFilter != null) {
				return getReferencedFilterDescription(martService, dataset,
						pointerFilter);
			} else {
				return null;
			}
		} else {
			String[] splitName = filterDescription.getInternalName().split(
					"\\.");
			FilterDescription ref = getReferencedFilterDescription(martService,
					dataset, splitName[1]);
			return ref;
		}
	}

	/**
	 * Returns the filter description referenced by the attribute description.
	 *
	 * @param martService
	 *            the MartService to fetch the referenced filter description
	 *            from
	 * @param dataset
	 *            the datset containing the referenced filter description
	 * @param attributeDescription
	 *            the attribute description
	 * @return the filter description referenced by the attribute description
	 * @throws MartServiceException
	 *             if and exception occurs while fetching the filter description
	 */
	public static FilterDescription getReferencedFilterDescription(
			MartService martService, MartDataset dataset,
			AttributeDescription attributeDescription, String softwareVersion)
			throws MartServiceException {
		if ("0.5".equals(softwareVersion)) {
			String pointerFilter = attributeDescription
					.getAttribute("pointerFilter");
			if (pointerFilter != null) {
				return getReferencedFilterDescription(martService, dataset,
						pointerFilter);
			} else {
				return null;
			}
		} else {
			String[] splitName = attributeDescription.getInternalName().split(
					"\\.");
			return getReferencedFilterDescription(martService, dataset,
					splitName[2]);
		}
	}

	public static FilterDescription getReferencedFilterDescription(
			MartService martService, MartDataset dataset, String filterName)
			throws MartServiceException {
		FilterDescription referencedFilter = null;
		DatasetConfig datasetConfig = martService.getDatasetConfig(dataset);
		List<FilterDescription> filterDescriptions = datasetConfig.getAllFilterDescriptions();
		for (FilterDescription filterDescription : filterDescriptions) {
			if (filterName.equals(filterDescription.getInternalName())) {
				if (!"true".equals(filterDescription.getHidden())) {
					referencedFilter = filterDescription;
					break;
				}
			}
		}
		return referencedFilter;
	}

	public static FilterDescription getReferencedFilterDescription(
			AttributeDescription attributeDescription, String softwareVersion) {
		FilterDescription filterDescription = new FilterDescription();
		if ("0.5".equals(softwareVersion)) {
			filterDescription.setInternalName(attributeDescription
					.getInternalName());
			filterDescription.setAttribute(attributeDescription
					.getAttribute("pointerDataset"));
			filterDescription.setAttribute(attributeDescription
					.getAttribute("pointerFilter"));
			filterDescription.setAttribute(attributeDescription
					.getAttribute("pointerInterface"));
		} else {
			String[] splitName = attributeDescription.getInternalName().split(
					"\\.");
			filterDescription
					.setInternalName(splitName[0] + "." + splitName[2]);
		}
		return filterDescription;
	}

	public static AttributeDescription getReferencedAttributeDescription(
			MartService martService, MartDataset dataset,
			AttributeDescription attributeDescription, String softwareVersion)
			throws MartServiceException {
		AttributeDescription referencedAttributeDescription = null;
		String attributeName = null;
		if ("0.5".equals(softwareVersion)) {
			attributeName = attributeDescription
					.getAttribute("pointerAttribute");
		} else {
			String internalName = attributeDescription.getInternalName();
			String[] splitName = internalName.split("\\.");
			if (splitName.length == 2) {
				attributeName = splitName[1];
			}
		}
		if (attributeName != null) {
			DatasetConfig datasetConfig = martService.getDatasetConfig(dataset);
			if (datasetConfig.containsAttributeDescription(attributeName)) {
				referencedAttributeDescription = datasetConfig
						.getAttributeDescriptionByInternalName(attributeName);
			}
		}
		return referencedAttributeDescription;
	}

	/**
	 * Returns true if the internal name of the configuration object contains a
	 * '.' character.
	 *
	 * @param bnco
	 *            the configuration object
	 * @return true if the internal name of the configuration object contains a
	 *         '.' character
	 */
	public static boolean isReference(BaseNamedConfigurationObject bnco,
			String softwareVersion) {
		if ("0.5".equals(softwareVersion)) {
			return bnco.getAttribute("pointerDataset") != null
			&& (bnco.getAttribute("pointerAttribute") != null || bnco.getAttribute("pointerFilter") != null);
		} else {
			return bnco.getInternalName().indexOf(".") != -1;
		}
	}

	/**
	 * Returns true if the internal name of the AttributeDescription has the
	 * format "[datasetName].[attributeName]".
	 *
	 * @param attributeDescription
	 * @return true if the internal name of the AttributeDescription has the
	 *         format "[datasetName].[attributeName]"
	 */
	public static boolean isAttributeReference(
			AttributeDescription attributeDescription, String softwareVersion) {
		if ("0.5".equals(softwareVersion)) {
			return attributeDescription.getAttribute("pointerAttribute") != null;
		} else {
			return attributeDescription.getInternalName().split("\\.").length == 2;
		}
	}

	/**
	 * Returns true if the internal name of the AttributeDescription has the
	 * format "[datasetName].filter.[filterName]".
	 *
	 * @param attributeDescription
	 * @return true if the internal name of the AttributeDescription has the
	 *         format "[datasetName].filter.[filterName]"
	 */
	public static boolean isFilterReference(
			AttributeDescription attributeDescription, String softwareVersion) {
		if ("0.5".equals(softwareVersion)) {
			return attributeDescription.getAttribute("pointerFilter") != null;
		} else {
			return attributeDescription.getInternalName().split("\\.").length == 3;
		}
	}

//	/**
//	 * Returns the qualified name of the AttributeDescription in the format
//	 * "[datasetName].[attributeName]".
//	 *
//	 * @param dataset
//	 * @param attributeDescription
//	 * @return true if the qualified name of the AttributeDescription in the
//	 *         format "[datasetName].[attributeName]"
//	 */
//	public static String getQualifiedName(MartDataset dataset,
//			AttributeDescription attributeDescription, String softwareVersion) {
//		if ("0.5".equals(softwareVersion)) {
//			if (isAttributeReference(attributeDescription, softwareVersion)) {
//				return attributeDescription.getAttribute("pointerDataset")
//						+ "."
//						+ attributeDescription.getAttribute("pointerAttribute");
//			} else if (isFilterReference(attributeDescription, softwareVersion)) {
//				return attributeDescription.getAttribute("pointerDataset")
//						+ "."
//						+ attributeDescription.getAttribute("pointerFilter");
//			} else {
//				return dataset.getName()
//						+ attributeDescription.getInternalName();
//			}
//		} else {
//			if (isAttributeReference(attributeDescription, softwareVersion)) {
//				return attributeDescription.getInternalName();
//			} else if (isFilterReference(attributeDescription, softwareVersion)) {
//				String[] splitName = attributeDescription.getInternalName()
//						.split("\\.");
//				return splitName[0] + "." + splitName[2];
//			} else {
//				return dataset.getName()
//						+ attributeDescription.getInternalName();
//			}
//		}
//	}

	public static String csvToValuePerLine(String csv) {
		StringBuffer list = new StringBuffer();

		String[] splitString = csv.split(",");
		for (int i = 0; i < splitString.length; i++) {
			if (i > 0) {
				list.append(LINE_END);
			}
			list.append(splitString[i].trim());
		}

		return list.toString();
	}

	public static String valuePerLineToCsv(String list) {
		return list.trim().replaceAll("\\s", ",");
	}

	public static List<String> csvToList(String csv) {
		List<String> list = new ArrayList<String>();

		String[] splitString = csv.split(",");
		for (int i = 0; i < splitString.length; i++) {
			list.add(splitString[i].trim());
		}

		return list;
	}

	/**
	 * Converts a List of objects to a comma separated string of the objects'
	 * string representations in the order given by the List's iterator. For
	 * example:
	 *
	 * <blockquote>
	 *
	 * <pre>
	 *          List list = Arrays.toList(new String[] {&quot;one&quot;, &quot;two&quot;, &quot;three&quot;};
	 *          System.out.println(listToCsv(list));
	 * </pre>
	 *
	 * </blockquote> would return the string "one,two,three".
	 *
	 * @param list
	 * @return a List of objects to a comma separated string of the object's
	 *         string representations
	 */
	public static String listToCsv(List<?> list) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			sb.append(iter.next().toString());
			if (iter.hasNext()) {
				sb.append(',');
			}
		}
		return sb.toString();
	}

	/**
	 * Returns true iff the 'display' or 'hidden' value of the configuration
	 * object is not "true".
	 *
	 * @param bnco
	 *            the configuration object
	 * @return true iff the 'display' or 'hidden' value of configuration object
	 *         is "true"
	 */
	public static boolean display(BaseNamedConfigurationObject bnco) {
		boolean display = true;
		String hideDisplay = bnco.getDisplay();
		if (hideDisplay != null) {
			if (hideDisplay.equals("true")) {
				display = false;
			}
		} else {
			String hidden = bnco.getHidden();
			if (hidden != null) {
				if (hidden.equals("true")) {
					display = false;
				}
			}
		}
		return display;
	}

}
