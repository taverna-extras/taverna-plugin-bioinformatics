/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import net.sf.taverna.t2.annotation.annotationbeans.MimeType;
import net.sf.taverna.t2.reference.ExternalReferenceSPI;
import net.sf.taverna.t2.reference.ReferenceContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;
import org.biomoby.service.dashboard.data.ParametersTable;
import org.biomoby.shared.Central;
import org.biomoby.shared.MobyData;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyPrimaryDataSet;
import org.biomoby.shared.MobyPrimaryDataSimple;
import org.biomoby.shared.MobySecondaryData;
import org.biomoby.shared.MobyService;
import org.biomoby.shared.Utils;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * An Activity based on the Biomoby compliant web services. This activity
 * implementation will contact Biomoby registry in order to find the list of
 * extant ports at creation time.
 * 
 * Copied from org.biomoby.client.taverna.plugin.BiomobyProcessor and
 * org.biomoby.client.taverna.plugin.BiomobyTask and converted to a Taverna 2
 * Activity.
 * 
 * @author Martin Senger
 * @author Edward Kawas
 * @author Jose Maria Fernandez, INB
 * @author David Withers
 */
public class BiomobyActivity extends
		AbstractAsynchronousActivity<BiomobyActivityConfigurationBean> {

	private static Logger logger = Logger.getLogger(BiomobyActivity.class);

	protected BiomobyActivityConfigurationBean configurationBean = new BiomobyActivityConfigurationBean();

	private URL endpoint;

	private Central worker = null;

	private MobyService mobyService = null;

	private boolean containSecondary = false;

	private ParametersTable parameterTable = null;



	private boolean doInit = true;

	@Override
	public void configure(BiomobyActivityConfigurationBean configurationBean)
			throws ActivityConfigurationException {
		this.configurationBean = configurationBean;
		if (doInit) {
			init();
			generatePorts();
			configureSecondaries();
			doInit = false;
		} else {
			configureSecondaries();
		}
	}

	@Override
	public BiomobyActivityConfigurationBean getConfiguration() {
		return configurationBean;
	}

	@Override
	public void executeAsynch(final Map<String, T2Reference> inputMap,
			final AsynchronousActivityCallback callback) {
		callback.requestRun(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				ReferenceService referenceService = callback.getContext()
						.getReferenceService();

				Map<String, T2Reference> outputMap = new HashMap<String, T2Reference>();

				if (logger.isDebugEnabled()) {
					logger.debug("Service " + mobyService.getUniqueName());
					for (Iterator it = inputMap.keySet().iterator(); it
							.hasNext();) {
						String key = (String) it.next();
						try {
							Object input = referenceService.renderIdentifier(
									inputMap.get(key), String.class, callback
											.getContext());
							if (input instanceof String) {
								logger.debug("key " + key + "has value of\n"
										+ input);
								continue;
							} else if (input instanceof List) {
								List list = (List) input;
								for (Iterator it2 = list.iterator(); it2
										.hasNext();) {
									logger.debug("List key " + key
											+ "has value of\n" + it2.next());
								}
							}
						} catch (ReferenceServiceException e) {
							logger.debug(
									"Error resolving data for port " + key, e);
						}
					}
					logger.debug("Printing of ports complete.");
				}
				// invoke services with no defined input (as per BioMOBY API)
				if (mobyService.getPrimaryInputs().length == 0) {
					try {
						String methodName = configurationBean.getServiceName();
						String serviceEndpoint = endpoint.toExternalForm();
						String serviceInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
								+ "<moby:MOBY xmlns:moby=\"http://www.biomoby.org/moby\">"
								+ "  <moby:mobyContent>"
								+ "    <moby:mobyData moby:queryID=\"sip_1_\" />"
								+ "  </moby:mobyContent>" + "</moby:MOBY>";
						String[] invocations = new String[] { serviceInput };
						// add secondaries
						if (containSecondary) {
							@SuppressWarnings("unused")
							ParametersTable pt = getParameterTable();
							Element[] parameters = null;
							parameters = getParameterTable().toXML();
							serviceInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
									+ "<moby:MOBY xmlns:moby=\"http://www.biomoby.org/moby\">"
									+ "  <moby:mobyContent>"
									+ "    <moby:mobyData moby:queryID=\"sip_1_\">";
							XMLOutputter out = new XMLOutputter(Format
									.getCompactFormat());
							Format format = out.getFormat();
							format.setOmitDeclaration(true);
							out.setFormat(format);
							for (int i = 0; i < parameters.length; i++) {
								serviceInput += out.outputString(parameters[i]);
							}
							serviceInput += "    </moby:mobyData>"
									+ "  </moby:mobyContent>" + "</moby:MOBY>";
							format = Format.getPrettyFormat();
							format.setOmitDeclaration(false);
							format.setIndent("   ");
							serviceInput = new XMLOutputter(format)
									.outputString(XMLUtilities
											.getDOMDocument(serviceInput));
							invocations = new String[] { serviceInput };
						}

						// execute the service that takes no Biomoby datatypes
						// as input
						for (int inCount = 0; inCount < invocations.length; inCount++) {
							if (logger.isDebugEnabled())
								logger.debug("input(" + inCount + "):\n"
										+ invocations[inCount]);
							// execute a 'moby' service
							invocations[inCount] = executeService(
									serviceEndpoint, methodName,
									invocations[inCount]);
							if (logger.isDebugEnabled())
								logger.debug("output(" + inCount + "):\n"
										+ invocations[inCount]);
						}
						String outputXML = XMLUtilities
								.createMultipleInvokations(invocations);
						// create the other ports
						processOutputPorts(outputXML, outputMap,
								referenceService, callback.getContext());

						callback.receiveResult(outputMap, new int[0]);
						return;
					} catch (ReferenceServiceException e) {
						callback.fail("Error accessing input/output data", e);
					} catch (MobyException ex) {
						// a MobyException should be already reasonably
						// formatted
						logger
								.error(
										"Error invoking biomoby service for biomoby. A MobyException caught",
										ex);
						callback.fail(
								"Service failed due to problem invoking remote biomoby service.\n"
										+ ex.getMessage(), ex);

					} catch (Exception ex) {
						logger.error(
								"Error invoking biomoby service for biomoby",
								ex);
						callback
								.fail(
										"Task failed due to problem invoking biomoby service (see details in log)",
										ex);
					}
				}

				{
					// now try other named ports
					try {
						String inputXML = null;
						Element root = new Element("MOBY", XMLUtilities.MOBY_NS);
						Element content = new Element("mobyContent",
								XMLUtilities.MOBY_NS);
						root.addContent(content);
						int totalMobyDatas = 0;
						Vector mobyDatas = new Vector(); // list of mobyData
						// element
						for (ActivityInputPort myInput : getInputPorts()) {
							if (myInput.getName().equalsIgnoreCase("input")) {
								continue;
							}
							// the port name
							String portName = myInput.getName();
							// the article name
							String articleName = "";
							String type = portName;
							if (portName.indexOf("(") >= 0
									&& portName.indexOf(")") > 0) {
								articleName = portName.substring(portName
										.indexOf("(") + 1, portName
										.indexOf(")"));

								if (articleName.indexOf("'") >= 0
										&& articleName.lastIndexOf("'") > 0)
									articleName = articleName.substring(
											articleName.indexOf("'") + 1,
											articleName.lastIndexOf("'"));

								type = portName.substring(0, portName
										.indexOf("("));
							}

							// String inputType = myInput.getSyntacticType();
							Object input = referenceService.renderIdentifier(
									inputMap.get(portName), myInput
											.getTranslatedElementClass(),
									callback.getContext());
							if (myInput.getDepth() == 0) {
								inputXML = (String) input;
								Element inputElement = null;
								try {
									inputElement = XMLUtilities.getDOMDocument(
											inputXML).getRootElement();

								} catch (MobyException e) {
									callback
											.fail(XMLUtilities.newline
													+ "There was an error parsing the input XML:"
													+ XMLUtilities.newline
													+ Utils.format(inputXML, 3)
													+ XMLUtilities.newline
													+ e.getLocalizedMessage());
									return;
								}
								// determine whether we have a multiple
								// invocation message
								if (XMLUtilities
										.isMultipleInvocationMessage(inputElement)) {
									// multiple invocations
									Element[] invocations = XMLUtilities
											.getSingleInvokationsFromMultipleInvokations(inputElement);
									ArrayList list = new ArrayList();
									for (int j = 0; j < invocations.length; j++) {
										Element[] elements = XMLUtilities
												.getListOfCollections(invocations[j]);
										if (elements.length == 0) {
											// single simple
											inputElement = XMLUtilities
													.renameSimple(articleName,
															type,
															invocations[j]);
											Element md = XMLUtilities
													.extractMobyData(inputElement);
											list.add(md);
										} else {
											// collection of simples => create
											// multiple
											// invocation message
											String queryID = XMLUtilities
													.getQueryID(invocations[j]);
											Element[] simples = XMLUtilities
													.getSimplesFromCollection(invocations[j]);
											for (int k = 0; k < simples.length; k++) {
												Element wrappedSimple = XMLUtilities
														.createMobyDataElementWrapper(simples[k]);
												wrappedSimple = XMLUtilities
														.renameSimple(
																articleName,
																type,
																wrappedSimple);
												wrappedSimple = XMLUtilities
														.setQueryID(
																wrappedSimple,
																queryID );
												list
														.add(XMLUtilities
																.extractMobyData(wrappedSimple));
											}
										}
									}
									if (list.isEmpty())
										continue;
									if (totalMobyDatas < 1)
										totalMobyDatas = 1;
									totalMobyDatas *= list.size();
									mobyDatas.add(list);
								} else {
									// single invocation
									// is this a collection
									Element[] elements = XMLUtilities
											.getListOfCollections(inputElement);
									if (elements.length == 0) {
										// single simple
										inputElement = XMLUtilities
												.renameSimple(articleName,
														type, inputElement);
										ArrayList list = new ArrayList();
										Element md = XMLUtilities
												.extractMobyData(inputElement);
										list.add(md);
										mobyDatas.add(list);
										if (totalMobyDatas < 1)
											totalMobyDatas = 1;
									} else {
										// collection of simples => create
										// multiple
										// invocation message
										String queryID = XMLUtilities
												.getQueryID(inputElement);
										Element[] simples = XMLUtilities
												.getSimplesFromCollection(inputElement);

										ArrayList list = new ArrayList();
										for (int j = 0; j < simples.length; j++) {
											Element wrappedSimple = XMLUtilities
													.createMobyDataElementWrapper(simples[j]);
											wrappedSimple = XMLUtilities
													.renameSimple(articleName,
															type, wrappedSimple);
											wrappedSimple = XMLUtilities
													.setQueryID(wrappedSimple,
															queryID );
											list
													.add(XMLUtilities
															.extractMobyData(wrappedSimple));
										}
										if (list.isEmpty())
											continue;
										mobyDatas.add(list);
										if (totalMobyDatas < 1)
											totalMobyDatas = 1 * list.size();
										else {
											totalMobyDatas *= list.size();
										}
									}

								}
							} else {
								// we have a collection!
								// inputThing is a list of Strings
								List list = (List) input;
								/*
								 * need this map in cases where simples are
								 * passed into a service that wants a
								 * collection. each simple is then added into
								 * the same collection
								 */
								Map collectionMap = new HashMap();
								for (Iterator it = list.iterator(); it
										.hasNext();) {
									Element inputElement = null;
									String next = (String) it.next();
									try {
										inputElement = XMLUtilities
												.getDOMDocument(next)
												.getRootElement();

									} catch (MobyException e) {
										callback
												.fail(XMLUtilities.newline
														+ "There was an error parsing the input XML:"
														+ XMLUtilities.newline
														+ Utils.format(
																inputXML, 3)
														+ XMLUtilities.newline
														+ e
																.getLocalizedMessage());
										return;
									}
									// determine whether we have a multiple
									// invocation message
									if (XMLUtilities
											.isMultipleInvocationMessage(inputElement)) {
										// multiple invocations (update
										// totalMobyDatas)
										Element[] invocations = XMLUtilities
												.getSingleInvokationsFromMultipleInvokations(inputElement);
										ArrayList mdList = new ArrayList();
										// this is here for mim messages of
										// simples
										Element mimCollection = null;
										String mimQueryID = "";
										for (int j = 0; j < invocations.length; j++) {
											Element[] elements = XMLUtilities
													.getListOfCollections(invocations[j]);
											mimQueryID = XMLUtilities
													.getQueryID(invocations[j]);
											if (elements.length == 0) {
												if (mimCollection == null)
													mimCollection = new Element(
															"Collection",
															XMLUtilities.MOBY_NS);

												Element theSimple = XMLUtilities
														.extractMobyData(invocations[j]);
												if (theSimple
														.getChild("Simple") != null)
													theSimple = theSimple
															.getChild("Simple");
												else if (theSimple.getChild(
														"Simple",
														XMLUtilities.MOBY_NS) != null)
													theSimple = theSimple
															.getChild(
																	"Simple",
																	XMLUtilities.MOBY_NS);
												mimCollection
														.addContent(theSimple
																.detach());
											} else {
												// collection passed in (always
												// 1 passed in)
												Element collection = invocations[j];
												collection = XMLUtilities
														.renameCollection(
																articleName,
																collection);
												collection = XMLUtilities
														.createMobyDataElementWrapper(
																collection,
																XMLUtilities
																		.getQueryID(invocations[j]),
																null);
												mdList
														.add(XMLUtilities
																.extractMobyData(collection));
											}
										}
										if (mimCollection != null) {
											mimCollection = XMLUtilities
													.createMobyDataElementWrapper(
															mimCollection,
															mimQueryID, null);
											mimCollection = XMLUtilities
													.renameCollection(
															articleName,
															mimCollection);
											mimCollection = XMLUtilities
													.createMobyDataElementWrapper(
															mimCollection,
															mimQueryID, null);
											mdList
													.add(XMLUtilities
															.extractMobyData(mimCollection));
										}

										if (mdList.isEmpty())
											continue;

										mobyDatas.add(mdList);
										if (totalMobyDatas < 1)
											totalMobyDatas = 1;
										totalMobyDatas *= mdList.size();
									} else {
										// single invocation
										Element[] elements = XMLUtilities
												.getListOfCollections(inputElement);
										if (elements.length == 0) {
											// simple was passed in so wrap it
											Element collection = new Element(
													"Collection",
													XMLUtilities.MOBY_NS);
											collection.addContent(XMLUtilities
													.extractMobyData(
															inputElement)
													.cloneContent());
											collection = XMLUtilities
													.createMobyDataElementWrapper(
															collection,
															XMLUtilities
																	.getQueryID(inputElement),
															null);
											collection = XMLUtilities
													.renameCollection(
															articleName,
															collection);
											collection = XMLUtilities
													.createMobyDataElementWrapper(
															collection,
															XMLUtilities
																	.getQueryID(inputElement),
															null);
											if (collectionMap
													.containsKey(articleName)) {
												// add the simple to a
												// pre-existing
												// collection
												ArrayList mdList = (ArrayList) collectionMap
														.remove(articleName);
												mdList
														.add(XMLUtilities
																.extractMobyData(collection));
												collectionMap.put(articleName,
														mdList);
											} else {
												// new collection - add element
												// and
												// increment count
												ArrayList mdList = new ArrayList();
												mdList
														.add(XMLUtilities
																.extractMobyData(collection));
												collectionMap.put(articleName,
														mdList);
												// totalMobyDatas++;
												if (totalMobyDatas < 1)
													totalMobyDatas = 1;
											}
										} else {
											// we have a collection
											Element collection = inputElement;
											collection = XMLUtilities
													.renameCollection(
															articleName,
															collection);
											ArrayList mdList = new ArrayList();
											collection = XMLUtilities
													.createMobyDataElementWrapper(
															collection,
															XMLUtilities
																	.getQueryID(inputElement),
															null);
											mdList
													.add(XMLUtilities
															.extractMobyData(collection));
											mobyDatas.add(mdList);
											if (totalMobyDatas < 1)
												totalMobyDatas = 1;

										}
									} // end if SIM
								} // end iteration over inputThing list
								Iterator collectionIterator = collectionMap
										.keySet().iterator();
								while (collectionIterator.hasNext()) {
									String key = (String) collectionIterator
											.next();
									List theList = (List) collectionMap
											.get(key);
									theList = XMLUtilities.mergeCollections(
											theList, key);
									List unwrappedList = new ArrayList();
									for (Iterator it = theList.iterator(); it
											.hasNext();) {
										Element e = (Element) it.next();
										if (XMLUtilities.isWrapped(e))
											unwrappedList.add(XMLUtilities
													.extractMobyData(e));
										else
											unwrappedList.add(e);
									}
									mobyDatas.add(unwrappedList);
								}
							}
						}

						if (logger.isDebugEnabled()) {
							logger.debug("Before MobyData aggregation");
							for (Iterator itr = mobyDatas.iterator(); itr
									.hasNext();) {
								List eList = (List) itr.next();
								for (int x = 0; x < eList.size(); x++) {
									logger.debug(new XMLOutputter(Format
											.getPrettyFormat())
											.outputString((Element) eList
													.get(x)));
								}
							}
							logger.debug("******* End ******");
						}
						/*
						 * ports have been processed -> vector contains a list
						 * of all the different types of inputs with their
						 * article names set correctly. The elements are from
						 * mobyData down. Moreover, there are totalMobyData
						 * number of invocations in the output moby message
						 */
						if (logger.isDebugEnabled()) {
							logger.debug("TotalMobyDatas: " + totalMobyDatas);
						}
						Element[] mds = new Element[totalMobyDatas];
						// initialize the mobydata blocks
						for (int x = 0; x < mds.length; x++) {
							mds[x] = new Element("mobyData",
									XMLUtilities.MOBY_NS);
							String queryID = "_";
							// add the content
							for (Iterator iter = mobyDatas.iterator(); iter
									.hasNext();) {
								ArrayList list = (ArrayList) iter.next();
								int index = x % list.size();
								Element next = ((Element) list.get(index));
								logger.debug(new XMLOutputter(Format
										.getPrettyFormat()).outputString(next));
								// queryID += "_" +
								// XMLUtilities.getQueryID(next);
								queryID = XMLUtilities.getQueryID(next);
								mds[x].addContent(next.cloneContent());

							}
							// remove the first _
							// if (queryID != null && queryID.length() > 1)
							// queryID = queryID.substring(1);
							mds[x].setAttribute("queryID", queryID,
									XMLUtilities.MOBY_NS);
							// if secondarys exist add them here
							if (containSecondary) {
								@SuppressWarnings("unused")
								ParametersTable pt = parameterTable;
								Element[] parameters = null;
								parameters = parameterTable.toXML();
								for (int i = 0; i < parameters.length; i++) {
									mds[x].addContent((parameters[i]).detach());
								}
							}
							content.addContent(mds[x].detach());
						}

						if (logger.isDebugEnabled()) {
							logger.debug("After MobyData aggregation");
							logger.debug(new XMLOutputter(Format
									.getPrettyFormat()).outputString(root));
							logger.debug("******* End ******");
						}
						// do the task and populate outputXML

						String methodName = configurationBean.getServiceName();
						String serviceEndpoint = endpoint.toExternalForm();

						String serviceInput = new XMLOutputter(Format
								.getPrettyFormat()).outputString(root);
						String[] invocations = XMLUtilities
								.getSingleInvokationsFromMultipleInvokations(serviceInput);
						// logger.debug(serviceInput);
						// going to iterate over all invocations so that
						// messages with
						// many mobyData blocks dont timeout.
						logger.debug("Total invocations " + invocations.length);
						if (invocations.length > 0)
							logger.debug("invocation 00");
						for (int inCount = 0; inCount < invocations.length; inCount++) {
							if (logger.isDebugEnabled())
								logger.info("input(" + inCount + "):\n"
										+ invocations[inCount]);
							if (!XMLUtilities.isEmpty(invocations[inCount]))
								invocations[inCount] = executeService(
										serviceEndpoint, methodName,
										invocations[inCount]);
							if (logger.isDebugEnabled())
								logger.info("output(" + inCount + "):\n"
										+ invocations[inCount]);
						}

						String outputXML = XMLUtilities
								.createMultipleInvokations(invocations);
						// create the other ports
						processOutputPorts(outputXML, outputMap,
								referenceService, callback.getContext());

						callback.receiveResult(outputMap, new int[0]);

					} catch (ReferenceServiceException e) {
						callback.fail("Error accessing input/output data", e);
					} catch (MobyException ex) {
						// a MobyException should be already reasonably
						// formatted
						logger
								.error(
										"Error invoking biomoby service for biomoby. A MobyException caught",
										ex);
						callback.fail(
								"Service failed due to exception invoking remote biomoby service.\n"
										+ ex.getMessage(), ex);

					} catch (Exception ex) {
						// details of other exceptions will appear only in a log
//						ex.();
						logger.error(
								"Error invoking biomoby service for biomoby",
								ex);
						callback
								.fail(
										"Task failed due to problem invoking biomoby service (see details in log)",
										ex);
					}

				}
			}

		});

	}

	private void init() throws ActivityConfigurationException {
		// Find the service endpoint (by calling Moby registry)
		try {
			if (mobyService == null) {
				worker = new CentralImpl(configurationBean.getMobyEndpoint());

				MobyService pattern = new MobyService(configurationBean
						.getServiceName());
				pattern.setAuthority(configurationBean.getAuthorityName());
				pattern.setCategory("");
				MobyService[] services = worker.findService(pattern);
				if (services == null || services.length == 0)
					throw new ActivityConfigurationException(
							formatError("I cannot find the service."));
				mobyService = services[0];
			}
			String serviceEndpoint = mobyService.getURL();
			if (serviceEndpoint == null || serviceEndpoint.equals(""))
				throw new ActivityConfigurationException(
						formatError("Service has an empty endpoint."));
			try {
				endpoint = new URL(serviceEndpoint);
			} catch (MalformedURLException e2) {
				throw new ActivityConfigurationException(
						formatError("Service has malformed endpoint: '"
								+ serviceEndpoint + "'."));
			}		

		} catch (Exception e) {
			if (e instanceof ActivityConfigurationException) {
				throw (ActivityConfigurationException) e;
			}
			throw new ActivityConfigurationException(formatError(e.toString()));
		}
		// here we make sure that we have downloaded the ontology for the
		// registry that we got this service from
		try {
			new GetOntologyThread(worker.getRegistryEndpoint()).start();
		} catch (Exception e) {
			/* don't care if an exception occurs here ... */
		}

	}

	/**
	 * Use the endpoint data to create new ports and attach them to the
	 * processor.
	 */
	private void generatePorts() {

		// inputs TODO - find a better way to deal with collections
		MobyData[] serviceInputs = this.mobyService.getPrimaryInputs();
		int inputDepth = 0;
		for (int x = 0; x < serviceInputs.length; x++) {
			if (serviceInputs[x] instanceof MobyPrimaryDataSimple) {
				MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) serviceInputs[x];

				// retrieve the simple article name
				String simpleName = simple.getName();
				if (simpleName.equals("")) {
					simpleName = "_ANON_";
				}
				simpleName = "(" + simpleName + ")";

				String portName = simple.getDataType().getName() + simpleName;
				addInput(portName, inputDepth, true,
						new ArrayList<Class<? extends ExternalReferenceSPI>>(),
						String.class);
			} else {
				// collection of items
				inputDepth = 1;
				MobyPrimaryDataSet collection = (MobyPrimaryDataSet) serviceInputs[x];
				String collectionName = collection.getName();
				if (collectionName.equals(""))
					collectionName = "MobyCollection";
				MobyPrimaryDataSimple[] simples = collection.getElements();
				for (int y = 0; y < simples.length; y++) {
					// collection port
					String portName = simples[y].getDataType().getName()
							+ "(Collection - '" + collectionName + "')";
					addInput(
							portName,
							inputDepth,
							true,
							new ArrayList<Class<? extends ExternalReferenceSPI>>(),
							String.class);

				}
			}
		}
		/*addInput("input", inputDepth, true,
				new ArrayList<Class<? extends ExternalReferenceSPI>>(),
				String.class);*/

		MobyData[] secondaries = this.mobyService.getSecondaryInputs();

		if (secondaries.length > 0) {
			MobySecondaryData[] msd = new MobySecondaryData[secondaries.length];
			for (int i = 0; i < secondaries.length; i++) {
				msd[i] = (MobySecondaryData) secondaries[i];
			}
			containSecondary = true;
			this.parameterTable = new org.biomoby.service.dashboard.data.ParametersTable(
					msd);
			updateConfigBeanSecondaries();
		}

		// outputs
		MobyData[] serviceOutputs = this.mobyService.getPrimaryOutputs();
		int outputDepth = 0;
		for (int x = 0; x < serviceOutputs.length; x++) {
			if (serviceOutputs[x] instanceof MobyPrimaryDataSimple) {
				MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) serviceOutputs[x];

				// retrieve the simple article name
				String simpleName = simple.getName();
				if (simpleName.equals("")) {
					simpleName = "_ANON_";
				}
				simpleName = "(" + simpleName + ")";

				String outputName = simple.getDataType().getName() + simpleName;
				addOutput(outputName, outputDepth, "text/xml");
			} else {
				outputDepth = 1;
				// collection of items
				MobyPrimaryDataSet collection = (MobyPrimaryDataSet) serviceOutputs[x];
				String collectionName = collection.getName();
				if (collectionName.equals(""))
					collectionName = "MobyCollection";
				MobyPrimaryDataSimple[] simples = collection.getElements();
				for (int y = 0; y < simples.length; y++) {
					String outputName = simples[y].getDataType().getName()
							+ "(Collection - '" + collectionName + "')";
					addOutput(outputName, outputDepth, "text/xml");

					outputName = simples[y].getDataType().getName()
							+ "(Collection - '" + collectionName
							+ "' As Simples)";
					addOutput(outputName, outputDepth, "text/xml");
				}
			}
		}

//		addOutput("output", outputDepth, "text/xml");

	}

	/**
	 * Updates config bean secondaries for those that aren't already defined so
	 * this only records new ones, but doesn't overwrite existing ones.
	 */
	private void updateConfigBeanSecondaries() {
		ParametersTable table = getParameterTable();
		int rows = table.getModel().getRowCount();
		Map<String, String> secondaries = getConfiguration().getSecondaries();
		for (int i = 0; i < rows; i++) {
			String key = (String) table.getModel().getValueAt(i, 0);
			String value = table.getModel().getValueAt(i, 1).toString();
			if (!secondaries.containsKey(key)) {
				secondaries.put(key, value);
			}
		}

	}

	public ParametersTable getParameterTable() {
		return parameterTable;
	}

	public boolean containsSecondaries() {
		return containSecondary;
	}

	public MobyService getMobyService() {
		return mobyService;
	}

	@SuppressWarnings("unchecked")
	private void processOutputPorts(String outputXML, Map outputMap,
			ReferenceService referenceService, ReferenceContext context)
			throws MobyException, ReferenceServiceException {
		boolean isMIM = XMLUtilities.isMultipleInvocationMessage(outputXML);
		for (OutputPort outputPort : getOutputPorts()) {
			String name = outputPort.getName();
			if (!name.equalsIgnoreCase("output")) {
				if (outputPort.getDepth() == 1) {
					// collection - list of strings
					String articleName = "";
					if (name.indexOf("MobyCollection") > 0) {
						// un-named collection -> ignore it as it is illegal
						// in the api
						// TODO could throw exception

						List innerList = new ArrayList();
						outputMap.put(name, referenceService
								.register(innerList, outputPort.getDepth(),
										true, context));
						continue;
					} else {
						articleName = name.substring(name.indexOf("'") + 1,
								name.lastIndexOf("'"));
						if (name.indexOf("' As Simples)") > 0) {
							// list of simples wanted
							if (isMIM) {
								String[] invocations = XMLUtilities
										.getSingleInvokationsFromMultipleInvokations(outputXML);

								List innerList = new ArrayList();
								Element serviceNotesElement = XMLUtilities
										.getServiceNotesAsElement(outputXML);
								for (int i = 0; i < invocations.length; i++) {
									try {
										String collection = XMLUtilities
												.getWrappedCollection(
														articleName,
														invocations[i]);
										String[] simples = XMLUtilities
												.getSimplesFromCollection(
														articleName, collection);
										for (int j = 0; j < simples.length; j++) {
											innerList
													.add(XMLUtilities
															.createMobyDataElementWrapper(
																	simples[j],
																	XMLUtilities
																			.getQueryID(collection)
																	/*
																	 * + "_+_s"
																	 * +
																	 * qCounter
																	 * ++
																	 */,
																	serviceNotesElement));
										}
									} catch (MobyException e) {
										// collection didnt exist, so put an
										// empty
										// mobyData
										// TODO keep the original wrapper
										/*
										 * String qID =
										 * XMLUtilities.getQueryID(invocations
										 * [i]); Element empty =
										 * XMLUtilities.createMobyDataWrapper
										 * (qID,
										 * XMLUtilities.getServiceNotesAsElement
										 * (outputXML)); XMLOutputter output =
										 * new XMLOutputter(Format
										 * .getPrettyFormat());
										 * innerList.add(output
										 * .outputString(empty));
										 */
									}
								}
								outputMap.put(name, referenceService.register(
										innerList, outputPort.getDepth(), true,
										context));
							} else {
								// process the single invocation and put string
								// into
								// a
								// list
								try {

									List innerList = new ArrayList();
									String collection = XMLUtilities
											.getWrappedCollection(articleName,
													outputXML);

									String[] simples = XMLUtilities
											.getSimplesFromCollection(
													articleName, collection);
									Element serviceNotesElement = XMLUtilities
											.getServiceNotesAsElement(outputXML);
									for (int i = 0; i < simples.length; i++) {
										innerList
												.add(XMLUtilities
														.createMobyDataElementWrapper(
																simples[i],
																XMLUtilities
																		.getQueryID(collection),
																serviceNotesElement));
									}

									outputMap
											.put(
													name,
													referenceService
															.register(
																	innerList,
																	outputPort
																			.getDepth(),
																	true,
																	context));
								} catch (MobyException e) {
									List innerList = new ArrayList();
									outputMap
											.put(
													name,
													referenceService
															.register(
																	innerList,
																	outputPort
																			.getDepth(),
																	true,
																	context));
								}
							}
						} else {
							if (isMIM) {
								// process each invocation and then merge them
								// into
								// a
								// single string
								String[] invocations = XMLUtilities
										.getSingleInvokationsFromMultipleInvokations(outputXML);

								List innerList = new ArrayList();
								for (int i = 0; i < invocations.length; i++) {
									try {
										String collection = XMLUtilities
												.getWrappedCollection(
														articleName,
														invocations[i]);
										innerList.add(collection);
									} catch (MobyException e) {
										
									}
								}

								outputMap.put(name, referenceService.register(
										innerList, outputPort.getDepth(), true,
										context));
							} else {

								try {

									List innerList = new ArrayList();
									String collection = XMLUtilities
											.getWrappedCollection(articleName,
													outputXML);
									innerList.add(collection);
									outputMap
											.put(
													name,
													referenceService
															.register(
																	innerList,
																	outputPort
																			.getDepth(),
																	true,
																	context));
								} catch (MobyException e) {
									List innerList = new ArrayList();
									outputMap
											.put(
													name,
													referenceService
															.register(
																	innerList,
																	outputPort
																			.getDepth(),
																	true,
																	context));
								}
							}
						}
					}
				} else {
					// simple - single string
					if (name.indexOf("_ANON_") > 0) {
						// un-named simple -> ignore it as it is illegal in the
						// api
						// TODO could throw exception

						String empty = new XMLOutputter()
								.outputString(XMLUtilities
										.createMobyDataWrapper(
												XMLUtilities
														.getQueryID(outputXML),
												XMLUtilities
														.getServiceNotesAsElement(outputXML)));
						List innerList = new ArrayList();
						innerList.add(empty);
						outputMap.put(name, referenceService.register(empty,
								outputPort.getDepth(), true, context));
						// FIXME outputMap.put(name, new
						// DataThing((Object)null));
						continue;
					} else {
						// TODO what if you make mim messages a single string
						// and then simples always output 'text/xml'?
						String articleName = name.substring(
								name.indexOf("(") + 1, name.indexOf(")"));
						if (isMIM) {

							String[] invocations = XMLUtilities
									.getSingleInvokationsFromMultipleInvokations(outputXML);

							ArrayList innerList = new ArrayList();

							for (int i = 0; i < invocations.length; i++) {
								try {
									String simple = XMLUtilities
											.getWrappedSimple(articleName,
													invocations[i]);
									innerList.add(simple);
								} catch (MobyException e) {
									// simple didnt exist, so put an empty
									// mobyData
									// TODO keep the original wrapper
									String qID = XMLUtilities
											.getQueryID(invocations[i]);

									Element empty = XMLUtilities
											.createMobyDataWrapper(
													qID,
													XMLUtilities
															.getServiceNotesAsElement(outputXML));
									XMLOutputter output = new XMLOutputter(
											Format.getPrettyFormat());
									// invocations[i] =
									// output.outputString(empty);
									innerList.add(output.outputString(empty));
									// FIXME outputMap.put(name, new
									// DataThing(""));
								}
							}
							String[] s = new String[innerList.size()];
							s = (String[]) innerList.toArray(s);
							try {
								outputMap.put(name, referenceService.register(
										XMLUtilities
												.createMultipleInvokations(s),
										outputPort.getDepth(), true, context));
							} catch (MobyException e) {
								logger
										.error("Error creating output for service: "
												+ "."
												+ System
														.getProperty("line.separator")
												+ e.getMessage());
								outputMap.put(name, referenceService.register(
										"", outputPort.getDepth(), true,
										context));
							}
						} else {
							// process the single invocation and put into a
							// string
							try {
								String simple = XMLUtilities.getWrappedSimple(
										articleName, outputXML);
								ArrayList innerList = new ArrayList();
								innerList.add(simple);
								outputMap.put(name, referenceService.register(
										simple, outputPort.getDepth(), true,
										context));
							} catch (MobyException e) {
								// simple didnt exist, so put an empty mobyData
								// TODO keep the original wrapper
								String qID = XMLUtilities.getQueryID(outputXML);
								Element empty = XMLUtilities
										.createMobyDataWrapper(
												qID,
												XMLUtilities
														.getServiceNotesAsElement(outputXML));
								XMLOutputter output = new XMLOutputter(Format
										.getPrettyFormat());
								ArrayList innerList = new ArrayList();
								innerList.add(output.outputString(empty));
								outputMap.put(name, referenceService.register(
										output.outputString(empty), outputPort
												.getDepth(), true, context));
								// FIXME outputMap.put(name, new DataThing(""));

							}
						}
					}
				}
			}
		}
	}

	protected String formatError(String msg) {
		// Removed references to the authority, some errors
		// were causing it to be null which in turn threw
		// a NPE from here, breaking Taverna's error handlers
		return ("Problems with service '" + configurationBean.getServiceName()
				+ "' provided by authority '"
				+ configurationBean.getAuthorityName()
				+ "'\nfrom Moby registry at "
				+ configurationBean.getMobyEndpoint() + ":\n\n" + msg);
	}

	protected ActivityInputPort getInputPort(String name) {
		for (ActivityInputPort port : getInputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	protected OutputPort getOutputPort(String name) {
		for (OutputPort port : getOutputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	protected void addOutput(String portName, int portDepth, String type) {
		OutputPort port = EditsRegistry.getEdits().createActivityOutputPort(
				portName, portDepth, portDepth);
		MimeType mimeType = new MimeType();
		mimeType.setText(type);
		try {
			EditsRegistry.getEdits().getAddAnnotationChainEdit(port, mimeType)
					.doEdit();
		} catch (EditException e) {
			logger.debug("Error adding MimeType annotation to port", e);
		}
		outputPorts.add(port);
	}

	private void configureSecondaries() {
		if (configurationBean.getSecondaries().size() > 0
				&& containsSecondaries()) {
			MobyData[] datas = getMobyService().getSecondaryInputs();
			for (Entry<String, String> entry : configurationBean
					.getSecondaries().entrySet()) {
				String name = entry.getKey();
				String value = entry.getValue();
				for (int i = 0; i < datas.length; i++) {
					if (datas[i].getName().equals(name)) {
						((MobySecondaryData) datas[i]).setDefaultValue(value);
						break;
					}
				}
			}
			MobySecondaryData[] msd = new MobySecondaryData[datas.length];
			for (int i = 0; i < datas.length; i++) {
				msd[i] = (MobySecondaryData) datas[i];
			}
			setParameterTable(new ParametersTable(msd));
		}
	}

	private void setParameterTable(ParametersTable table) {
		parameterTable = table;
	}

	private String executeService(String url, String serviceName, String xml)
			throws MobyException {
		// here we get the wsdl before calling the service, as the biomoby api assumes ...
		try {
			new RetrieveWsdlThread(worker, mobyService).start();
		} catch (Exception e) {
			/* don't care if an exception occurs here ... */
			logger.info("Problem getting the biomoby wsdl for " + mobyService.getUniqueName() + ".\n" + e.getLocalizedMessage());
		}
		String serviceCategory = mobyService.getCategory();
		if (serviceCategory.equalsIgnoreCase(MobyService.CATEGORY_MOBY)) {
			return ExecuteMobyService.executeMobyService(url, serviceName, xml);
		} else if (serviceCategory.equalsIgnoreCase("cgi")) {
			return ExecuteCgiService.executeCgiService(url, xml);
		} else if (serviceCategory.equalsIgnoreCase(MobyService.CATEGORY_MOBY_ASYNC)) {
			return ExecuteAsyncMobyService.executeMobyAsyncService(url, serviceName, xml);
		} else if (serviceCategory.equalsIgnoreCase("cgi-async")) {
			return ExecuteAsyncCgiService.executeMobyCgiAsyncService(url, serviceName, xml);
		}
		// TODO should we throw an exception here?
		return "";
	}
}
