/*
 * This file is a component of the Taverna project, and is licensed under the
 * GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.edits.AddBiomobyConsumingServiceEdit;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.OutputPort;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;
import org.biomoby.registry.meta.Registry;
import org.biomoby.shared.Central;
import org.biomoby.shared.MobyData;
import org.biomoby.shared.MobyDataType;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyNamespace;
import org.biomoby.shared.MobyPrimaryDataSet;
import org.biomoby.shared.MobyPrimaryDataSimple;
import org.biomoby.shared.MobyService;
import org.biomoby.shared.data.MobyDataInstance;
import org.biomoby.shared.data.MobyDataObject;
import org.biomoby.shared.data.MobyDataObjectSet;

public class BiomobyObjectActionHelper  {

	private static Logger logger = Logger
	.getLogger(BiomobyObjectActionHelper.class);

	private boolean searchParentTypes = false;

	private OutputPort outputPort = null;

	private MobyNamespace[] namespaces = null;

	public BiomobyObjectActionHelper(boolean searchParentTypes) {
		super();
		this.searchParentTypes = searchParentTypes;
	}

	public BiomobyObjectActionHelper(OutputPort outputPort, boolean searchParentTypes) {
		super();
		this.searchParentTypes = searchParentTypes;
		this.outputPort = outputPort;
	}

	public BiomobyObjectActionHelper() {
		super();
	}

	private class Worker extends Thread {
		private BiomobyObjectActivity activity;

		private BiomobyObjectActionHelper action;

		private JPanel panel = new JPanel(new BorderLayout());

		private JPanel namespacePanel = new JPanel(new BorderLayout());
		
		private JProgressBar bar = new JProgressBar();

		private JTree namespaceList = null;
		
		public Worker(BiomobyObjectActivity activity, BiomobyObjectActionHelper object) {
			super("Biomoby object action worker");
			this.activity = activity;
			this.action = object;			
		}

		/*
		 * method to show the progress bar
		 */
		private void setUpProgressBar(String text) {
		    bar = new JProgressBar();
		    bar.setIndeterminate(true);
		    bar.setValue(0);
		    bar.setStringPainted(true);
		    bar.setVisible(true);
		    bar.setString(text);
		    if (panel != null){
			panel.add(bar, BorderLayout.PAGE_END);
			panel.updateUI();
		    }
		}
		
		private void takeDownProgressBar() {
		    if (panel != null) {
			panel.remove(bar);
			panel.updateUI();
		    }
		    
		}
		
		public JPanel getPanel() {
			return this.panel;
		}

		public void run() {
			Central central = activity.getCentral();
			// ask if we should restrict query by namespace
			// only do this if we dont have an output port
			if (action.outputPort == null) {
			    if (JOptionPane.YES_OPTION == 
				JOptionPane.showConfirmDialog(null, "Would you like to restrict your search by based upon a namespace?\n" +
						"This can allow you to find only those services that operate on a specific kind of data.",
						"Restrict query space", JOptionPane.YES_NO_OPTION)) {
				// create a JList chooser with a done button here				
				setUpProgressBar("Getting namespaces list");
				try {
				    namespacePanel = new JPanel(new BorderLayout());
				    createNamespaceList(central.getFullNamespaces());				    
				    JButton button = new JButton("Done");
				    button.addActionListener(new ActionListener(){
					@SuppressWarnings("unchecked")
					public void actionPerformed(
						ActionEvent e) {
					    ArrayList<MobyNamespace> chosen = new ArrayList<MobyNamespace>();
					    TreePath[] paths = namespaceList.getSelectionPaths();
					    for (TreePath p : paths) {
						if (p.getLastPathComponent() instanceof DefaultMutableTreeNode) {
						    DefaultMutableTreeNode node = (DefaultMutableTreeNode)p.getLastPathComponent();
						    if (node.isRoot()) {
							chosen = new ArrayList<MobyNamespace>();
							break;
						    } 
						    if (!node.isLeaf()) {
							// get the children and add them to chosen
							Enumeration<DefaultMutableTreeNode> children = node.children();
							while (children.hasMoreElements() )
							    chosen.add(new MobyNamespace(children.nextElement().toString()));
						    } else {
							// is a leaf ... add to chosen
							chosen.add(new MobyNamespace(node.toString()));
						    }
						}
					    }
					    // set the namespaces that were selected
					    setNamespaces(chosen.toArray(new MobyNamespace[]{}));
					    // get the tree - in a new thread
					   Thread t = new Thread(){
					    public void run() {
						 namespacePanel.setVisible(false);
						 setUpProgressBar("Getting BioMOBY details for " + activity.getConfiguration().getServiceName() + " ...");
						 getSemanticServiceTree();
					    }};
					    t.setDaemon(true);
					    t.start();
					    
					}});
				    // add the list and button to the panel
				    namespacePanel.add(new JScrollPane(namespaceList), BorderLayout.CENTER);
				    namespacePanel.add(button, BorderLayout.PAGE_END);
				    panel.add(namespacePanel, BorderLayout.CENTER);
				    panel.updateUI();
				} catch (MobyException e) {
				    logger.error("", e);
				    takeDownProgressBar();
				}
				takeDownProgressBar();
				// once done is pressed, insert selected namespace into the namespaces array
				// show the progress bar
				
			    } else {
			    	// start our search
			    	setNamespaces(null);
			    	setUpProgressBar("Getting BioMOBY details for " + activity.getConfiguration().getServiceName() + " ...");
			    	getSemanticServiceTree();
			    }
			} else {
				// search only for those services that consume the correct namespaces
				if (this.action != null && this.action.getNamespaces() != null) {
					setNamespaces(this.action.getNamespaces());
				} else {
					setNamespaces(null);
				}
			    setUpProgressBar("Getting BioMOBY details for " + activity.getConfiguration().getServiceName() + " ...");
			    // start our search
			    getSemanticServiceTree();
			}
			
			
		}

		/**
		 * @param central
		 */
		private void getSemanticServiceTree() {
		    Central central = activity.getCentral();
		    MobyDataType object = activity.getMobyObject();
		    MobyService template = new MobyService("dummy");

		    // strip the lsid portion of the name
		    String name = object.getName();
		    if (name.indexOf(":") > 0) {
		    	name = name.substring(name.lastIndexOf(":") + 1);
		    }
		    // initialize a data object to pass into the service template
		    Registry mRegistry = new Registry(central.getRegistryEndpoint(),central.getRegistryEndpoint(),"http://domain.com/MOBY/Central");
		    MobyDataObject data = new MobyDataObject("", mRegistry);
		    data.setDataType(new MobyDataType(name));
		    data.setXmlMode(MobyDataInstance.CENTRAL_XML_MODE);
		    if (action.namespaces != null)
		    	data.setNamespaces(action.namespaces);
		    // create the nodes for the tree
		    MutableTreeNode parent = new DefaultMutableTreeNode(name);
		    MutableTreeNode inputNode = new DefaultMutableTreeNode("Feeds into");
		    MutableTreeNode outputNode = new DefaultMutableTreeNode("Produced by");

		    // what services does this object feed into?
		    template.setInputs(new MobyData[] { data });
		    template.setCategory("");
		    MobyService[] services = null;
		    Set<MobyService> theServices = new TreeSet<MobyService>();
		    try {
		    	services = central.findService(template, null, true, action.searchParentTypes);
		    	
		    	theServices.addAll(Arrays.asList(services));
		    	MobyDataObjectSet set = new MobyDataObjectSet("");
		    	set.add(data);
		    	template.setInputs(null);
		    	template.setInputs(new MobyData[]{set});
		    	services = central.findService(template, null, true, action.searchParentTypes);
		    	theServices.addAll(Arrays.asList(services));
		    } catch (MobyException e) {
		    	panel.add(new JTree(new String[] { "Error finding services",
		    			"TODO: create a better Error" }), BorderLayout.CENTER);
		    	panel.updateUI();
		    	return;
		    }
		    createTreeNodes(inputNode, theServices.toArray(new MobyService[]{}));
		    if (inputNode.getChildCount() == 0)
		    	inputNode.insert(new DefaultMutableTreeNode(
		    			"Object Doesn't Currently Feed Into Any Services"), 0);

		    // what services return this object?
		    template = null;
		    template = new MobyService("dummy");
		    template.setCategory("");
		    template.setOutputs(new MobyData[] { data });
		    services = null;
		    theServices = new TreeSet<MobyService>();
		    try {
		    	services = central.findService(template, null, true, action.searchParentTypes);
		    	theServices.addAll(Arrays.asList(services));
		    	MobyDataObjectSet set = new MobyDataObjectSet("");
		    	set.add(data);
		    	template.setOutputs(null);
		    	template.setOutputs(new MobyData[]{set});
		    	services = central.findService(template, null, true, action.searchParentTypes);
		    	theServices.addAll(Arrays.asList(services));
		    } catch (MobyException e) {
		    	panel.add(new JTree(new String[] { "Error finding services",
		    			"TODO: create a better Error" }), BorderLayout.CENTER);
		    	panel.updateUI();
		    	return;
		    }
		    createTreeNodes(outputNode, theServices.toArray(new MobyService[]{}));
		    if (outputNode.getChildCount() == 0)
		    	outputNode.insert(new DefaultMutableTreeNode(
		    			"Object Isn't Produced By Any Services"), 0);
		    // what kind of object is this?

		    // set up the nodes
		    parent.insert(inputNode, 0);
		    parent.insert(outputNode, 1);

		    // finally return a tree describing the object
		    final JTree tree = new JTree(parent);
		    tree.setCellRenderer(new BioMobyObjectTreeCustomRenderer());
		    ToolTipManager.sharedInstance().registerComponent(tree);
		    tree.addMouseListener(new MouseListener() {
		    	public void mouseClicked(MouseEvent me) {
		    	}

		    	public void mousePressed(MouseEvent me) {
		    		mouseReleased(me);
		    	}

		    	public void mouseReleased(MouseEvent me) {
		    		if (me.isPopupTrigger()) // right click, show popup menu
		    		{
		    			TreePath path = tree.getPathForLocation(me.getX(), me.getY());
		    			if (path == null)
		    				return;
		    			if (path.getPathCount() == 4) {
		    				if (path.getParentPath().getParentPath().getLastPathComponent()
		    						.toString().equals("Feeds into")) {

		    					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		    							.getLastSelectedPathComponent();
		    					if (node == null)
		    						return;
		    					final String selectedService = node.toString();
		    					// ensure that the last selected item is a
		    					// service!
		    					if (!selectedService.equals(path.getLastPathComponent().toString()))
		    						return;
		    					final String selectedAuthority = path.getParentPath()
		    							.getLastPathComponent().toString();
		    					final JPopupMenu menu = new JPopupMenu();
		    					// Create and add a menu item for adding to the
		    					// item
		    					// to the workflow
		    					JMenuItem item = new JMenuItem("Add service - " + selectedService
		    							+ " to the workflow?");
		    					item
		    							.setIcon(MobyPanel.getIcon("/Add24.gif"));
		    					item.addActionListener(new ActionListener() {
		    						public void actionPerformed(ActionEvent ae) {
		    							
		    							try {
		    								if (outputPort==null) {
		    									outputPort = activity.getOutputPorts().iterator().next();
		    								}
											Dataflow currentDataflow = FileManager
													.getInstance()
													.getCurrentDataflow();
											Edit<?> edit = new AddBiomobyConsumingServiceEdit(
													currentDataflow, activity,
													selectedService,selectedAuthority,outputPort);
											EditManager editManager = EditManager
													.getInstance();
											editManager.doDataflowEdit(
													currentDataflow, edit);

										} catch (Exception e) {
											logger.error("Could not perform action", e);
										}
		    						}
		    					});
		    					// Create and add a menu item for service
		    					// details
		    					JMenuItem details = new JMenuItem("Find out about "
		    							+ selectedService);
		    					details
		    							.setIcon(MobyPanel.getIcon("/Information24.gif"));
		    					details.addActionListener(new ActionListener() {
		    						public void actionPerformed(ActionEvent ae) {
		    							// Create a frame
		    						    Frame frame = MobyPanel.CreateFrame("A BioMoby Service Description");
		    						    frame.setSize(getFrameSize());
		    							JPanel panel = new MobyPanel(selectedService,"A BioMoby Service Description",
		    									createServiceDescription(selectedService,
		    											selectedAuthority, activity.getConfiguration().getMobyEndpoint()));
		    							frame.add(panel);
		    							frame.pack();
		    							frame.setVisible(true);
		    						}

		    						@SuppressWarnings("unchecked")
		    						private String createServiceDescription(String selectedService,
		    								String selectedAuthority, String endpoint) {
		    							StringBuffer sb = new StringBuffer();
		    							String newline = System.getProperty("line.separator");
		    							MobyService service = new MobyService(selectedService);
		    							try {
		    								Central central = new CentralImpl(endpoint);
		    								service.setAuthority(selectedAuthority);
		    								service.setCategory("");
		    								MobyService[] services = central.findService(service);
		    								if (services == null || services.length != 1) {
		    									return "Couldn't retrieve a description on the BioMoby service '"
		    											+ selectedService + "'";
		    								}
		    								service = services[0];

		    							} catch (MobyException e) {
		    								logger.error("Could not retrieve a description on the BioMoby service "
		    										+ selectedService, e);
		    								return "Couldn't retrieve a description on the BioMoby service '"
		    										+ selectedService + "'";
		    							}
		    							sb.append("Service Contact: " + newline + "\t"
		    									+ service.getEmailContact() + newline);
		    							sb.append("Service Category: " + newline + "\t"
		    									+ service.getCategory() + newline);
		    							sb.append("Service Authority: " + newline + "\t"
		    									+ service.getAuthority() + newline);
		    							sb.append("Service Type: " + newline + "\t"
		    									+ service.getType() + newline);
		    							sb.append("Service Description: " + newline + "\t"
		    									+ service.getDescription() + newline);
		    							sb.append("Location of Service: " + newline + "\t"
		    									+ service.getURL() + newline);
		    							sb.append("Service Signature RDF Document is located at: "
		    									+ newline + "\t" + service.getSignatureURL()
		    									+ newline);
		    							MobyData[] data = service.getPrimaryInputs();
		    							Vector primaryDataSimples = new Vector();
		    							Vector primaryDataSets = new Vector();
		    							for (int x = 0; x < data.length; x++) {
		    								if (data[x] instanceof MobyPrimaryDataSimple)
		    									primaryDataSimples.add(data[x]);
		    								else
		    									primaryDataSets.add(data[x]);
		    							}
		    							// describe inputs simple then
		    							// collections
		    							sb.append("Inputs:" + newline);
		    							if (primaryDataSimples.size() == 0) {
		    								sb.append("\t\tNo Simple input datatypes consumed."
		    										+ newline);
		    							} else {
		    								Iterator it = primaryDataSimples.iterator();
		    								sb
		    										.append("\t\tService consumes the following Simple(s):"
		    												+ newline);
		    								while (it.hasNext()) {
		    									MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) it
		    											.next();
		    									MobyNamespace[] namespaces = simple.getNamespaces();
		    									sb.append("\t\tData type: "
		    											+ simple.getDataType().getName() + newline);
		    									sb.append("\t\t\tArticle name: " + simple.getName()
		    											+ newline);
		    									if (namespaces.length == 0) {
		    										sb.append("\t\t\tValid Namespaces: ANY"
		    												+ newline);
		    									} else {
		    										sb.append("\t\t\tValid Namespaces: ");
		    										for (int x = 0; x < namespaces.length; x++)
		    											sb.append(namespaces[x].getName() + " ");
		    										sb.append(newline);
		    									}
		    								}
		    							}
		    							if (primaryDataSets.size() == 0) {
		    								sb.append(newline
		    										+ "\t\tNo Collection input datatypes consumed."
		    										+ newline);
		    							} else {
		    								Iterator it = primaryDataSets.iterator();
		    								sb
		    										.append(newline
		    												+ "\t\tService consumes the following collection(s) of datatypes:"
		    												+ newline);
		    								while (it.hasNext()) {
		    									MobyPrimaryDataSet set = (MobyPrimaryDataSet) it
		    											.next();
		    									MobyPrimaryDataSimple simple = null;
		    									sb.append("\t\tCollection Name:" + set.getName()
		    											+ newline);
		    									MobyPrimaryDataSimple[] simples = set.getElements();
		    									for (int i = 0; i < simples.length; i++) {
		    										simple = simples[i];
		    										MobyNamespace[] namespaces = simple
		    												.getNamespaces();
		    										// iterate through set and
		    										// do
		    										// the following
		    										sb.append("\t\tData type: "
		    												+ simple.getDataType().getName()
		    												+ newline);
		    										sb.append("\t\t\tArticle name: "
		    												+ simple.getName() + newline);
		    										if (namespaces.length == 0) {
		    											sb.append("\t\t\tValid Namespaces: ANY"
		    													+ newline);
		    										} else {
		    											sb.append("\t\t\tValid Namespaces: ");
		    											for (int x = 0; x < namespaces.length; x++)
		    												sb
		    														.append(namespaces[x].getName()
		    																+ " ");
		    											sb.append(newline);
		    										}
		    									}
		    								}
		    							}
		    							// describe secondary inputs
		    							// describe outputs simple then
		    							// collections
		    							data = service.getPrimaryOutputs();
		    							primaryDataSimples = new Vector();
		    							primaryDataSets = new Vector();
		    							for (int x = 0; x < data.length; x++) {
		    								if (data[x] instanceof MobyPrimaryDataSimple)
		    									primaryDataSimples.add(data[x]);
		    								else
		    									primaryDataSets.add(data[x]);
		    							}
		    							sb.append("Outputs:" + newline);
		    							if (primaryDataSimples.size() == 0) {
		    								sb.append("\t\tNo Simple output datatypes produced."
		    										+ newline);
		    							} else {
		    								Iterator it = primaryDataSimples.iterator();
		    								sb
		    										.append("\t\tService produces the following Simple(s):"
		    												+ newline);
		    								while (it.hasNext()) {
		    									MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) it
		    											.next();
		    									MobyNamespace[] namespaces = simple.getNamespaces();
		    									sb.append("\t\tData type: "
		    											+ simple.getDataType().getName() + newline);
		    									sb.append("\t\t\tArticle name: " + simple.getName()
		    											+ newline);
		    									if (namespaces.length == 0) {
		    										sb.append("\t\t\tValid Namespaces: ANY"
		    												+ newline);
		    									} else {
		    										sb.append("\t\t\tValid Namespaces: ");
		    										for (int x = 0; x < namespaces.length; x++)
		    											sb.append(namespaces[x].getName() + " ");
		    										sb.append(newline);
		    									}
		    								}
		    							}
		    							if (primaryDataSets.size() == 0) {
		    								sb
		    										.append(newline
		    												+ "\t\tNo Collection output datatypes produced."
		    												+ newline);
		    							} else {
		    								Iterator it = primaryDataSets.iterator();
		    								sb
		    										.append(newline
		    												+ "\t\tService produces the following collection(s) of datatypes:"
		    												+ newline);
		    								while (it.hasNext()) {
		    									MobyPrimaryDataSet set = (MobyPrimaryDataSet) it
		    											.next();
		    									MobyPrimaryDataSimple simple = null;
		    									sb.append("\t\tCollection Name:" + set.getName()
		    											+ newline);
		    									MobyPrimaryDataSimple[] simples = set.getElements();
		    									for (int i = 0; i < simples.length; i++) {
		    										simple = simples[i];
		    										MobyNamespace[] namespaces = simple
		    												.getNamespaces();
		    										// iterate through set and
		    										// do
		    										// the following
		    										sb.append("\t\tData type: "
		    												+ simple.getDataType().getName()
		    												+ newline);
		    										sb.append("\t\t\tArticle name: "
		    												+ simple.getName() + newline);
		    										if (namespaces.length == 0) {
		    											sb.append("\t\t\tValid Namespaces: ANY"
		    													+ newline);
		    										} else {
		    											sb.append("\t\t\tValid Namespaces: ");
		    											for (int x = 0; x < namespaces.length; x++)
		    												sb
		    														.append(namespaces[x].getName()
		    																+ " ");
		    											sb.append(newline);
		    										}
		    									}
		    								}
		    							}
		    							sb.append((service.isAuthoritative()) ? newline
		    									+ "The service belongs to this author." + newline
		    									: newline
		    											+ "The service was wrapped by it's author."
		    											+ newline);
		    							return sb.toString();
		    						}
		    					});
		    					// add the components to the menus
		    					menu.add(new JLabel("Add to workflow ... ", JLabel.CENTER));
		    					menu.add(new JSeparator());
		    					menu.add(item);
		    					menu.add(new JSeparator());
		    					menu.add(new JLabel("Service Details ... ", JLabel.CENTER));
		    					menu.add(new JSeparator());
		    					menu.add(details);
		    					// show the window
		    					menu.show(me.getComponent(), me.getX(), me.getY());
		    				}
		    			}
		    		}
		    	}

		    	public void mouseEntered(MouseEvent me) {
		    	}

		    	public void mouseExited(MouseEvent me) {
		    	}
		    });
		    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		    panel.add(new JScrollPane(tree), BorderLayout.CENTER);
		    takeDownProgressBar();
		}

		private void createNamespaceList(MobyNamespace[] fullNamespaces) {
		    // sort the namespaces alphabetically
		    DefaultMutableTreeNode root = new DefaultMutableTreeNode("ANY");
		    // assuming that they increment by one ...
		    TreeMap<String, TreeSet<String>> sorted = new TreeMap<String, TreeSet<String>>();
		    for (MobyNamespace n : fullNamespaces) {
			String name = n.getName();
			String key = name.toUpperCase().substring(0, 1);
			if (sorted.get(key) == null) {
			    sorted.put(key, new TreeSet<String>());
			}
			sorted.get(key).add(name);
		    }
		    for (String o : sorted.keySet()) {
			if (sorted.get(o) == null)
			    continue;
			TreeSet<String> set = sorted.get(o);
			String first = set.first().toUpperCase().charAt(0) + "";
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(first);
			for (String s : set) {
			    node.add(new DefaultMutableTreeNode(s));
			}
			root.add(node);
		    }
		    namespaceList = new JTree(root);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflui.processoractions.AbstractProcessorAction#getComponent(org.embl.ebi.escience.scufl.Processor)
	 */
	public JComponent getComponent(BiomobyObjectActivity activity) {
		
		// this was done so that for longer requests, something is shown visually and the user then wont think that nothing happened.
		Worker worker = new Worker(activity, this);
		worker.start();
		return worker.getPanel();
	}

	/*
	 * method that processes the services returned by findService and adds them
	 * to the TreeNode parentNode, sorted by authority
	 */
	@SuppressWarnings("unchecked")
	private void createTreeNodes(MutableTreeNode parentNode, MobyService[] services) {
		HashMap inputHash;
		inputHash = new HashMap();
		for (int x = 0; x < services.length; x++) {
			DefaultMutableTreeNode authorityNode = null;
			if (!inputHash.containsKey(services[x].getAuthority())) {
				authorityNode = new DefaultMutableTreeNode(services[x].getAuthority());
			} else {
				authorityNode = (DefaultMutableTreeNode) inputHash.get(services[x].getAuthority());
			}
			MobyServiceTreeNode serv = new MobyServiceTreeNode(services[x].getName(), services[x]
					.getDescription());
			MutableTreeNode temp = new DefaultMutableTreeNode(serv);
			DefaultMutableTreeNode objects = new DefaultMutableTreeNode("Produces");
			// add to this node the MobyObjectTreeNodes that it produces!
			MobyData[] outputs = services[x].getPrimaryOutputs();
			for (int i = 0; i < outputs.length; i++) {
				if (outputs[i] instanceof MobyPrimaryDataSimple) {
					MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) outputs[i];
					StringBuffer sb = new StringBuffer("Namespaces used by this object: ");
					MobyNamespace[] namespaces = simple.getNamespaces();
					for (int j = 0; j < namespaces.length; j++) {
						sb.append(namespaces[j].getName() + " ");
					}
					if (namespaces.length == 0)
						sb.append("ANY ");
					MobyObjectTreeNode mobyObjectTreeNode = new MobyObjectTreeNode(simple
							.getDataType().getName()
							+ "('" + simple.getName() + "')", sb.toString());
					objects.insert(new DefaultMutableTreeNode(mobyObjectTreeNode), objects
							.getChildCount());
				} else {
					// we have a collection
					MobyPrimaryDataSet collection = (MobyPrimaryDataSet) outputs[i];
					DefaultMutableTreeNode collectionNode = new DefaultMutableTreeNode(
							"Collection('" + collection.getName() + "')");
					objects.insert(collectionNode, objects.getChildCount());
					MobyPrimaryDataSimple[] simples = collection.getElements();
					for (int j = 0; j < simples.length; j++) {
						MobyPrimaryDataSimple simple = simples[j];
						StringBuffer sb = new StringBuffer("Namespaces used by this object: ");
						MobyNamespace[] namespaces = simple.getNamespaces();
						for (int k = 0; k < namespaces.length; k++) {
							sb.append(namespaces[k].getName() + " ");
						}
						if (namespaces.length == 0)
							sb.append("ANY ");
						MobyObjectTreeNode mobyObjectTreeNode = new MobyObjectTreeNode(simple
								.getDataType().getName()
								+ "('" + simple.getName() + "')", sb.toString());
						collectionNode.insert(new DefaultMutableTreeNode(mobyObjectTreeNode),
								collectionNode.getChildCount());
					}

				}
			}

			temp.insert(objects, temp.getChildCount());

			authorityNode.insert(temp, authorityNode.getChildCount());
			inputHash.put(services[x].getAuthority(), authorityNode);

		}
		Set set = inputHash.keySet();
		SortedSet sortedset = new TreeSet(set);
		for (Iterator it = sortedset.iterator(); it.hasNext();) {
			parentNode.insert((DefaultMutableTreeNode) inputHash.get((String) it.next()),
					parentNode.getChildCount());
		}
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.embl.ebi.escience.scuflui.processoractions.ProcessorActionSPI#getDescription()
	 */
	public String getDescription() {
		return "Moby Object Details";
	}

	/*
	 * 
	 */
	public ImageIcon getIcon() {
		return MobyPanel.getIcon("/moby_small.gif");
	}

	/**
	 * returns the frame size as a dimension for the content pane housing this
	 * action
	 */
	public Dimension getFrameSize() {
		return new Dimension(450, 450);
	}

	public void setNamespaces(MobyNamespace[] namespaces) {
		if (namespaces != null && namespaces.length == 0)
			this.namespaces = null;
		else
			this.namespaces = namespaces;
	}
	public MobyNamespace[] getNamespaces() {
		return this.namespaces == null ? new MobyNamespace[]{} : this.namespaces;
	}
}