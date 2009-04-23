/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.sf.taverna.t2.annotation.annotationbeans.MimeType;
import net.sf.taverna.t2.reference.ExternalReferenceSPI;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;


import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;
import org.biomoby.shared.Central;
import org.biomoby.shared.MobyDataType;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyRelationship;
import org.biomoby.shared.NoSuccessException;

/**
 * An Activity that breaks up a Moby datatype into its component parts minus all
 * the moby wrappings.
 * 
 * Copied from org.biomoby.client.taverna.plugin.MobyParseDatatypeActivityProcessor and
 * org.biomoby.client.taverna.plugin.MobyParseDatatypeActivityTask and converted to a Taverna 2
 * Activity.
 * 
 * @author Edward Kawas
 * @author David Withers
 */
public class MobyParseDatatypeActivity extends AbstractAsynchronousActivity<MobyParseDatatypeActivityConfigurationBean> {

	private static Logger logger = Logger.getLogger(MobyParseDatatypeActivity.class);
			
	private MobyParseDatatypeActivityConfigurationBean configurationBean = new MobyParseDatatypeActivityConfigurationBean();

	private Central central = null;

	private MobyDataType datatype = null;

	@Override
	public void configure(MobyParseDatatypeActivityConfigurationBean configurationBean) throws ActivityConfigurationException {
		this.configurationBean = configurationBean;
		init();
	}

	@Override
	public void executeAsynch(final Map<String, T2Reference> data,
			final AsynchronousActivityCallback callback) {
		callback.requestRun(new Runnable() {

			@SuppressWarnings("unchecked")
			public void run() {
				ReferenceService referenceService = callback.getContext().getReferenceService();

				Map<String, T2Reference> output = new HashMap<String, T2Reference>();
				
				try {

                    //cache ontology and namespace if not done so already. Immediately returns if already cached.
                    BiomobyCache.cacheForRegistryEndpoint(getConfiguration().getRegistryEndpoint());

					String inputMapKey = getInputPorts().iterator().next().getName();
					// inputMap wasnt as expected
					if (!data.containsKey(inputMapKey)) {
						callback.receiveResult(output, new int[0]);
						return;
					}
					
					T2Reference inputId = data.get(inputMapKey);
					
					Object input = referenceService.renderIdentifier(inputId, String.class, callback.getContext());
					
					if (input instanceof String) {
						//logger.error(inputMapKey + " is a string!\n");
						String inputXML = (String) input;
						for (OutputPort outPort : getOutputPorts()) {
							String outputPortName = outPort.getName();
							String[] invocations = XMLUtilities.getSingleInvokationsFromMultipleInvokations(inputXML);
							ArrayList<String> names = new ArrayList<String>();
							int type = 0;		
							// get the type, names list, etc
							if (outputPortName.equalsIgnoreCase("namespace")) {
								// extract the namespace from the top element
								names.add(configurationBean.getArticleNameUsedByService());
								type = ParseMobyXML.NAMESPACE;
							} else if (outputPortName.equalsIgnoreCase("id")) {
								// extract the id from the top element
								names.add(configurationBean.getArticleNameUsedByService());
								type = ParseMobyXML.ID;
							} else {
								names = getNames(outputPortName);
								if (outputPortName.endsWith("_ns")) {
									type = ParseMobyXML.NAMESPACE;
									if (names.size() > 1) // added nov15-2007
										names.remove(names.size()-1);
								} else if (outputPortName.endsWith("_id")) {
									type = ParseMobyXML.ID;
									if (names.size() > 1)//added nov15-2007
		                                                                names.remove(names.size()-1);
								} else {
									type = ParseMobyXML.VALUE;
								}
							}
							ArrayList<String> stuff = new ArrayList<String>();
							for (int i = 0; i < invocations.length; i++) {
								String invocation = invocations[i];
								if (XMLUtilities.isCollection(invocation)) {
									String[] simples = XMLUtilities.getAllSimplesByArticleName(configurationBean.getArticleNameUsedByService(), invocation);
									for (int j = 0; j < simples.length; j++) {
										ArrayList<String> content = ParseMobyXML.getContentForDataType(names, type, XMLUtilities.createMobyDataElementWrapper(simples[j],"a1", null), configurationBean.getRegistryEndpoint());
										stuff.addAll(content);
									}
								} else {
									ArrayList<String> content = ParseMobyXML.getContentForDataType(names, type, invocations[i],configurationBean.getRegistryEndpoint());
									stuff.addAll(content);
								}
							}
							output.put(outputPortName, referenceService.register(stuff, 1, true, callback.getContext()));
						}

					} else if (input instanceof List) {
						//logger.error(inputMapKey + " is a list!\n");
						List<String> list = (List) input;
						// holder contains a list of strings indexed by output port name
						// TODO put stuff in the map and in the end put it in the output map
						HashMap<String, ArrayList<String>> holder = new HashMap<String, ArrayList<String>>();
						for (Iterator<String> it = list.iterator(); it.hasNext();) {
							String inputXML = (String) it.next();
							for (OutputPort outPort : getOutputPorts()) {
								String outputPortName = outPort.getName();
								String[] invocations = XMLUtilities.getSingleInvokationsFromMultipleInvokations(inputXML);
								ArrayList<String> names = new ArrayList<String>();
								int type = 0;		
								// get the type, names list, etc
								if (outputPortName.equalsIgnoreCase("namespace")) {
									// extract the namespace from the top element
									names.add(configurationBean.getArticleNameUsedByService());
									type = ParseMobyXML.NAMESPACE;
								} else if (outputPortName.equalsIgnoreCase("id")) {
									// extract the id from the top element
									names.add(configurationBean.getArticleNameUsedByService());
									type = ParseMobyXML.ID;
								} else {
									names = getNames(outputPortName);
									if (outputPortName.endsWith("_ns")) {
										type = ParseMobyXML.NAMESPACE;
										if (names.size() > 1)//added nov-15-07
		                                                                	names.remove(names.size()-1);
									} else if (outputPortName.endsWith("_id")) {
										type = ParseMobyXML.ID;
										if (names.size() > 1)//added nov-15-07
		                                                                        names.remove(names.size()-1);
									} else {
										type = ParseMobyXML.VALUE;
									}
								}
								ArrayList<String> stuff = new ArrayList<String>();
								for (int i = 0; i < invocations.length; i++) {
									String invocation = invocations[i];
									if (XMLUtilities.isCollection(invocation)) {
										String[] simples = XMLUtilities.getAllSimplesByArticleName(configurationBean.getArticleNameUsedByService(), invocation);
										for (int j = 0; j < simples.length; j++) {
											ArrayList<String> content = ParseMobyXML.getContentForDataType(names, type, XMLUtilities.createMobyDataElementWrapper(simples[j],"a1", null),configurationBean.getRegistryEndpoint());
											stuff.addAll(content);
										}
									} else {
										ArrayList<String> content = ParseMobyXML.getContentForDataType(names, type, invocations[i],configurationBean.getRegistryEndpoint());
										stuff.addAll(content);
									}
								}
								if (holder.containsKey(outputPortName)) {
									ArrayList<String> al = holder.get(outputPortName);
									al.addAll(stuff);
									holder.put(outputPortName, al);
								} else {
									holder.put(outputPortName, stuff);
								}
							}
						}
						// fill output map
						for (Iterator<String> it = holder.keySet().iterator(); it.hasNext();) {
							String key = it.next();
							output.put(key, referenceService.register(holder.get(key), 1, true, callback.getContext()));
						}
					}
						
					callback.receiveResult(output, new int[0]);
				} catch (ReferenceServiceException e) {
					callback.fail("Error accessing input/output data", e);
				} catch (Exception e) {
					callback.fail("rror parsing moby data", e);
				}

			}
		});
	}

	@Override
	public MobyParseDatatypeActivityConfigurationBean getConfiguration() {
		return configurationBean;
	}

	@SuppressWarnings("unchecked")
	private void init() throws ActivityConfigurationException {
		try {
			central = new CentralImpl(configurationBean.getRegistryEndpoint());
		} catch (MobyException e) {
			throw new ActivityConfigurationException("Couldn't create MobyCentral client for endpoint "
					+ configurationBean.getRegistryEndpoint() + System.getProperty("line.separator")
					+ e.getLocalizedMessage());
		}
		if (this.datatype == null) {
			try {
				this.datatype = central.getDataType(configurationBean.getDatatypeName());
			} catch (MobyException e) {
				throw new ActivityConfigurationException(
						"There was a problem getting information from the MobyCentral registry at "
								+ configurationBean.getRegistryEndpoint() + System.getProperty("line.separator")
								+ e.getLocalizedMessage());
			} catch (NoSuccessException e) {
				throw new ActivityConfigurationException(
						"There was no success in getting information from the MobyCentral registry at "
								+ configurationBean.getRegistryEndpoint() + System.getProperty("line.separator")
								+ e.getLocalizedMessage());
			}
		}

//		setDescription("Processor to parse the datatype " + this.datatype.getName());
		
		ArrayList list = new ArrayList();
		if (isPrimitive(this.datatype.getName())) {
			list.add(configurationBean.getArticleNameUsedByService() + "_" + this.datatype.getName());
		} else if (this.datatype.getName().equals("Object")) {
			// dont do anything because object has no value
		} else {
			processDatatype(this.datatype, central, configurationBean.getArticleNameUsedByService(), list);
		}
		// add the input port called mobyData('datatypeName')
		addInput("mobyData('" + this.datatype.getName() + "')", 0, true,
				new ArrayList<Class<? extends ExternalReferenceSPI>>(),
				String.class);
		// add the namespace/id ports to the processor
		addOutput("namespace", 1, "text/xml");
		addOutput("id", 1, "text/xml");

		// list contains the output ports i have to create
		for (Iterator it = list.iterator(); it.hasNext();) {
			String portName = (String) it.next();
			if (portName.equals(configurationBean.getArticleNameUsedByService()+"_id") || portName.equals(configurationBean.getArticleNameUsedByService()+"_ns"))
				continue;
			addOutput(portName, 1, "text/xml");
		}
	}

	private boolean isPrimitive(String name) {
		if (name.equals("Integer") || name.equals("String") || name.equals("Float")
				|| name.equals("DateTime") || name.equals("Boolean")
		)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	private void processDatatype(MobyDataType dt, Central central, String currentName, List list) throws ActivityConfigurationException {

		if (dt.getParentName() == null || dt.getParentName().trim().equals("")) {
			//TODO should we throw an error or just return ...
			return;
		}

		if (!dt.getParentName().equals("Object")) {
			flattenChildType(dt.getParentName(), central, currentName, list);
		} else {
			list.add(currentName + "_id");
			list.add(currentName + "_ns");
		}

		MobyRelationship[] relations = dt.getChildren();
		for (int i = 0; i < relations.length; i++) {
			MobyRelationship relation = relations[i];
			switch (relation.getRelationshipType()) {
			case CentralImpl.iHAS: {
				// check for object or primitives
				if (isPrimitive(relation.getDataTypeName()) || relation.getDataTypeName().equals("Object")) {
					// object has no value ... only primitives do
					if (!relation.getDataTypeName().equals("Object"))
						list.add(currentName + (currentName.equals("") ? "" : "_'")
								+ relation.getName() + (currentName.equals("") ? "" : "'"));
					list.add(currentName + (currentName.equals("") ? "" : "_'")
							+ relation.getName() + (currentName.equals("") ? "" : "'")+"_id");
					list.add(currentName + (currentName.equals("") ? "" : "_'")
							+ relation.getName() + (currentName.equals("") ? "" : "'")+"_ns");
				}
				else {
					flattenChildType(relation.getDataTypeName(), central, currentName
							+ (currentName.equals("") ? "" : "_'") + relation.getName() + (currentName.equals("") ? "" : "'"), list);
				}
			}
			break;
			case CentralImpl.iHASA: {
				// check for object or primitives ... 
				if (isPrimitive(relation.getDataTypeName()) || relation.getDataTypeName().equals("Object")) {
					// object has no value ... only primitives do
					if (!relation.getDataTypeName().equals("Object"))
						list.add(currentName + (currentName.equals("") ? "" : "_'")
								+ relation.getName()+ (currentName.equals("") ? "" : "'"));
					list.add(currentName + (currentName.equals("") ? "" : "_'")
							+ relation.getName() + (currentName.equals("") ? "" : "'")+"_id");
					list.add(currentName + (currentName.equals("") ? "" : "_'")
							+ relation.getName() + (currentName.equals("") ? "" : "'")+"_ns");
				}
				else {

					flattenChildType(relation.getDataTypeName(), central, currentName
							+ (currentName.equals("") ? "" : "_'") + relation.getName() + (currentName.equals("") ? "" : "'"), list);
				}
			}
			break;
			default:
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void flattenChildType(String name, Central central, String current, List list) throws ActivityConfigurationException {
		MobyDataType dt = null;
		try {
			dt = central.getDataType(name);
		} catch (MobyException e) {
			throw new ActivityConfigurationException(
					"There was a problem getting information from the MobyCentral registry at "
					+ configurationBean.getRegistryEndpoint() + System.getProperty("line.separator")
					+ e.getLocalizedMessage());
		} catch (NoSuccessException e) {
			throw new ActivityConfigurationException(
					"There was no success in getting information from the MobyCentral registry at "
					+ configurationBean.getRegistryEndpoint() + System.getProperty("line.separator")
					+ e.getLocalizedMessage());
		}
		processDatatype(dt, central, current, list);
	}

	private ArrayList<String> getNames(String names) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		if (names == null || names.trim().length() == 0)
			return list;
		Scanner s = new Scanner(names).useDelimiter("_'");
		while (s.hasNext()) {
			temp.add(s.next());
		}
		s.close();
		
		for (String str : temp) {
			if (str.indexOf("'_") >= 0) {
				String[] strings = str.split("'_");
				for (int i = 0; i < strings.length; i++) {
					list.add(strings[i].replaceAll("'", ""));
				}
			} else {
				list.add(str.replaceAll("'", ""));
			}
		}
		
		if (list.size() == 1) {
			if (endsWithPrimitive(list.get(0))) {
				String name = list.remove(0);
				int i = name.lastIndexOf("_");
				name = name.substring(0, i);
				list.add(name);
			}
		} else if (isPrimitive(list.get(list.size()-1))) {
			// remove the last entry if its a primitive ... legacy reasons
			list.remove(list.size()-1);
		}
		return list;
	}
	
	private static boolean endsWithPrimitive(String name) {
		if (name.endsWith("_Integer") || name.endsWith("_String") || name.endsWith("_Float")
				|| name.endsWith("_DateTime") || name.endsWith("_Boolean"))
			return true;
		return false;
	}
	
	protected void addOutput(String portName, int portDepth, String type) {
		OutputPort port = EditsRegistry.getEdits().createActivityOutputPort(
				portName, portDepth, portDepth);
		MimeType mimeType = new MimeType();
		mimeType.setText(type);
		try {
			EditsRegistry.getEdits().getAddAnnotationChainEdit(port, mimeType).doEdit();
		} catch (EditException e) {
			logger.debug("Error adding MimeType annotation to port", e);
		}
		outputPorts.add(port);
	}

}
