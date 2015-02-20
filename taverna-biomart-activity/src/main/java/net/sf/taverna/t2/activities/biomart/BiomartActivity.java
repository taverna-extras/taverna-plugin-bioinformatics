/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester
 *
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomart;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityOutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.apache.log4j.Logger;
import org.biomart.martservice.MartQuery;
import org.biomart.martservice.MartServiceException;
import org.biomart.martservice.MartServiceXMLHandler;
import org.biomart.martservice.ResultReceiver;
import org.biomart.martservice.ResultReceiverException;
import org.biomart.martservice.config.QueryConfigUtils;
import org.biomart.martservice.query.Attribute;
import org.biomart.martservice.query.Dataset;
import org.biomart.martservice.query.Filter;
import org.biomart.martservice.query.Query;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * An Activity providing Biomart functionality.
 *
 * @author David Withers
 */
public class BiomartActivity extends AbstractAsynchronousActivity<JsonNode> {

	public static final String URI = "http://ns.taverna.org.uk/2010/activity/biomart";

	static boolean STREAM_RESULTS = true;

	private static Logger logger = Logger.getLogger(BiomartActivity.class);
	
	private JsonNode json;

	private MartQuery biomartQuery;

	@Override
	public void configure(JsonNode json)
			throws ActivityConfigurationException {
		this.json = json;
		String martQueryText = json.get("martQuery").asText();
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new StringReader(martQueryText));
			biomartQuery = MartServiceXMLHandler.elementToMartQuery(document.getRootElement(), null);
		} catch (JDOMException | IOException e) {
			throw new ActivityConfigurationException(e);
		}
	}

	@Override
	public JsonNode getConfiguration() {
		return json;
	}

	@Override
	public void executeAsynch(final Map<String, T2Reference> data,
			final AsynchronousActivityCallback callback) {
		callback.requestRun(new Runnable() {

			public void run() {
				final ReferenceService referenceService = callback.getContext().getReferenceService();

				final Map<String, T2Reference> outputData = new HashMap<String, T2Reference>();

				try {
					// Get a query including data source etc, creating
					// a copy so that any filter value settings are not
					// overwritten by input values
					biomartQuery.calculateLinks();
					Query query = new Query(biomartQuery.getQuery());

					// Configure any filters
					List<Filter> filters = query.getFilters();
					for (Filter filter : filters) {
						String name = filter.getQualifiedName();
						if (data.containsKey(name + "_filter")) {
							Object filterValue = referenceService.renderIdentifier(data
									.get(name + "_filter"), String.class, callback.getContext());
							if (filterValue instanceof String) {
								filter.setValue((String) filterValue);
							} else if (filterValue instanceof List) {
								List<?> idList = (List<?>) filterValue;
								filter.setValue(QueryConfigUtils
										.listToCsv(idList));
							}
						}
					}

					if (biomartQuery.getQuery().getFormatter() == null) {
						if (STREAM_RESULTS) {
							final List<Attribute> attributes = biomartQuery
									.getAttributesInLinkOrder();
							final Map<String, List<T2Reference>> outputLists = new HashMap<String, List<T2Reference>>();
							for (Attribute attribute : attributes) {
								outputLists.put(attribute.getQualifiedName(),
										new ArrayList<T2Reference>());
							}

							biomartQuery.getMartService().executeQuery(query,
									new ResultReceiver() {

										public void receiveResult(
												Object[] resultLine, long index) throws ResultReceiverException {
											Map<String, T2Reference> partialOutputData = new HashMap<String, T2Reference>();
											for (int i = 0; i < resultLine.length; i++) {
												Attribute attribute = attributes
														.get(i);
												String outputName = attribute
														.getQualifiedName();
												Integer outputDepth = getOutputPortDepth(outputName);
                                                if (outputDepth == null) {
                                                    logger.warn("Skipping unknown output port " + outputName);
                                                    continue;
                                                }
												try {
													T2Reference data = referenceService
															.register(resultLine[i], outputDepth - 1, true, callback.getContext());
													partialOutputData.put(
															outputName, data);
													outputLists.get(outputName)
															.add((int) index, data);
												} catch (ReferenceServiceException e) {
													throw new ResultReceiverException(e);
//													callback.fail("Failure when calling the reference service", e);
												}
											}
											callback.receiveResult(
													partialOutputData,
													new int[] { (int) index });
										}

										public void receiveError(String message,
												long index) throws ResultReceiverException {
											Map<String, T2Reference> partialOutputData = new HashMap<String, T2Reference>();
											for (Attribute attribute : attributes) {
												String outputName = attribute
														.getQualifiedName();
    				                                Integer outputDepth = getOutputPortDepth(outputName);
    				                                if (outputDepth == null) {
    				                                    logger.warn("Skipping unknown output port " + outputName);
    				                                    continue;
    				                                }
    				                                try {
													T2Reference error = referenceService.getErrorDocumentService()
															.registerError(message, outputDepth - 1, callback.getContext()).getId();
													partialOutputData.put(
															outputName, error);
													outputLists.get(outputName)
															.add((int) index, error);
												} catch (ReferenceServiceException e) {
													throw new ResultReceiverException(e);
//													callback.fail("Failure when calling the reference service", e);
												}
											}
											callback.receiveResult(
													partialOutputData,
													new int[] { (int) index });
										}

									});

							for (Attribute attribute : attributes) {
								String outputName = attribute
										.getQualifiedName();
								Integer outputDepth = getOutputPortDepth(outputName);
								if (outputDepth == null) {
								    logger.warn("Skipping unknown output port " + outputName);
								    continue;
								}
								outputData.put(outputName, referenceService.register(
										outputLists.get(outputName),
										outputDepth, true, callback.getContext()));
							}

						} else {
							// shouldn't need to reorder attributes for MartJ
							// 0.5
							Object[] resultList = biomartQuery.getMartService()
									.executeQuery(query);
							List<Attribute> attributes = biomartQuery
									.getAttributesInLinkOrder();
							assert resultList.length == attributes.size();
							for (int i = 0; i < resultList.length; i++) {
								Attribute attribute = attributes.get(i);
								String outputName = attribute
										.getQualifiedName();
                                Integer outputDepth = getOutputPortDepth(outputName);
                                if (outputDepth == null) {
                                    logger.warn("Skipping unknown output port " + outputName);
                                } else {
                                    outputData.put(outputName, referenceService.register(
										resultList[i], outputDepth, true, callback.getContext()));
                                }
							}
						}
					} else {
						Object[] resultList = biomartQuery.getMartService()
								.executeQuery(query);
						assert resultList.length == 1;
						Dataset dataset = biomartQuery.getQuery().getDatasets()
								.get(0);
						String outputName = dataset.getName();
                        Integer outputDepth = getOutputPortDepth(outputName);
                        if (outputDepth == null) {
                            logger.warn("Skipping unknown output port " + outputName);
                        } else {
                            outputData.put(outputName, referenceService.register(
								resultList[0], outputDepth, true, callback.getContext()));
                        }
					}

					callback.receiveResult(outputData, new int[0]);
				} catch (MartServiceException e) {
					callback.fail("Failure calling biomart", e);
				} catch (ReferenceServiceException e) {
					callback.fail("Failure when calling the reference service", e);
				} catch (ResultReceiverException e) {
					callback.fail("Failure when receiving a result from biomart", e);
				}
			}

		});

	}

	private Integer getOutputPortDepth(String portName) {
		for (ActivityOutputPort port : getOutputPorts()) {
			if (port.getName().equals(portName)) {
				return port.getDepth();
			}
		}
		return null;
	}

//	private void buildInputPorts(List<Edit<?>> editList) {
//		Map<String, ActivityInputPort> newInputMap = new HashMap<String, ActivityInputPort>();
//		List<Filter> filters = biomartQuery.getQuery().getFilters();
//		// Create new input ports corresponding to filters
//		for (Filter filter : filters) {
//			String name = filter.getQualifiedName() + "_filter";
//			if (inputMap.containsKey(name)) {
//				newInputMap.put(name, inputMap.remove(name));
//			} else {
//				ActivityInputPort inputPort = null;
//				if (filter.isList()) {
//					inputPort = edits.createActivityInputPort(name, 1, true,
//							new ArrayList<Class<? extends ExternalReferenceSPI>>(),
//							String.class);
//				} else {
//					inputPort = edits.createActivityInputPort(name, 0, true,
//							new ArrayList<Class<? extends ExternalReferenceSPI>>(),
//							String.class);
//				}
//				newInputMap.put(name, inputPort);
//				editList.add(edits.getAddActivityInputPortEdit(this, inputPort));
//				editList.add(createAddMimeTypeAnnotationEdit(inputPort, "text/plain"));
//			}
//		}
//		//remove any ports still left in the map
//		for (ActivityInputPort inputPort : inputMap.values()) {
//			editList.add(edits.getRemoveActivityInputPortEdit(this, inputPort));
//		}
//		inputMap = newInputMap;
//	}

//	private void buildOutputPorts(List<Edit<?>> editList) {
//		Map<String, ActivityOutputPort> newOutputMap = new HashMap<String, ActivityOutputPort>();
//		Query query = biomartQuery.getQuery();
//		List<Attribute> attributes = query.getAttributes();
//		String formatter = query.getFormatter();
//		if (formatter == null) {
//			// Create new output ports corresponding to attributes
//			for (Attribute attribute : attributes) {
//				String name = attribute.getQualifiedName();
//				if (outputMap.containsKey(name)) {
//					newOutputMap.put(name, outputMap.remove(name));
//				} else {
//					ActivityOutputPort outputPort = null;
//					if (attribute.getAttributes() != null) {
//						outputPort = edits.createActivityOutputPort(name, 2, STREAM_RESULTS?1:2);
//					} else {
//						outputPort = edits.createActivityOutputPort(name, 1, STREAM_RESULTS?0:1);
//					}
//					newOutputMap.put(name, outputPort);
//					editList.add(edits.getAddActivityOutputPortEdit(this, outputPort));
//					editList.add(createAddMimeTypeAnnotationEdit(outputPort, "text/plain"));
//				}
//			}
//		} else if (attributes.size() > 0) {
//			// create one port using the dataset name
//			Attribute attribute = attributes.get(0);
//			String name = attribute.getContainingDataset().getName();
//			if (outputMap.containsKey(name)) {
//				newOutputMap.put(name, outputMap.remove(name));
//			} else {
//				ActivityOutputPort outputPort = edits.createActivityOutputPort(name, 0, 0);
//				newOutputMap.put(name, outputPort);
//				editList.add(edits.getAddActivityOutputPortEdit(this, outputPort));
//				editList.add(createAddMimeTypeAnnotationEdit(outputPort, "text/plain"));
//			}
//		}
//		//remove any ports still left in the map
//		for (ActivityOutputPort outputPort : outputMap.values()) {
//			editList.add(edits.getRemoveActivityOutputPortEdit(this, outputPort));
//		}
//		outputMap = newOutputMap;
//	}

//	private Edit<?> createAddMimeTypeAnnotationEdit(Annotated<?> annotated, String type) {
//		MimeType mimeType = new MimeType();
//		mimeType.setText(type);
//		return edits.getAddAnnotationChainEdit(annotated, mimeType);
//	}

}
