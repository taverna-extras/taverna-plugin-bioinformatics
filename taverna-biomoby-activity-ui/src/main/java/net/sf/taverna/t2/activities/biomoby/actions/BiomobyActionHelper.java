package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.edits.AddBiomobyCollectionDataTypeEdit;
import net.sf.taverna.t2.activities.biomoby.edits.AddBiomobyDataTypeEdit;
import net.sf.taverna.t2.activities.biomoby.edits.AddMobyParseDatatypeEdit;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;
import org.biomoby.shared.Central;
import org.biomoby.shared.MobyData;
import org.biomoby.shared.MobyDataType;
import org.biomoby.shared.MobyException;
import org.biomoby.shared.MobyNamespace;
import org.biomoby.shared.MobyPrimaryDataSet;
import org.biomoby.shared.MobyPrimaryDataSimple;
import org.biomoby.shared.NoSuccessException;


/**
 *
 * @author Eddie An action that for BioMobyProcessors
 * @auther Stuart Owen - helped port to T2 - but with the minimum code changes possible!
 */
public class BiomobyActionHelper {

	private static Logger logger = Logger
	.getLogger(BiomobyActionHelper.class);

	JProgressBar progressBar = new JProgressBar();

    private EditManager editManager;

	private final FileManager fileManager;

    public BiomobyActionHelper(EditManager editManager, FileManager fileManager) {
		this.editManager = editManager;
		this.fileManager = fileManager;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.embl.ebi.escience.scuflui.processoractions.AbstractProcessorAction
	 * #getComponent(org.embl.ebi.escience.scufl.Processor)
	 */

	public JComponent getComponent(final BiomobyActivity activity) {
		// variables i need

		final String endpoint = activity.getConfiguration().getMobyEndpoint();
		// set up the root node
		String serviceName = activity.getMobyService().getName();
		String description = activity.getMobyService().getDescription();

		MobyServiceTreeNode service = new MobyServiceTreeNode(serviceName,
				description);
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(service);

		// now add the child nodes containing useful information about the
		// service
		DefaultMutableTreeNode input = new DefaultMutableTreeNode("Inputs");
		DefaultMutableTreeNode output = new DefaultMutableTreeNode("Outputs");
		rootNode.add(input);
		rootNode.add(output);

		// process inputs
		MobyData[] inputs = activity.getMobyService().getPrimaryInputs();
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i] instanceof MobyPrimaryDataSimple) {
				MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) inputs[i];
				StringBuffer sb = new StringBuffer(
						"Namespaces used by this object: ");
				MobyNamespace[] namespaces = simple.getNamespaces();
				for (int j = 0; j < namespaces.length; j++) {
					sb.append(namespaces[j].getName() + " ");
				}
				if (namespaces.length == 0)
					sb.append(" ANY ");
				MobyObjectTreeNode mobyObjectTreeNode = new MobyObjectTreeNode(
						simple.getDataType().getName() + "('"
								+ simple.getName() + "')", sb.toString());
				input.insert(new DefaultMutableTreeNode(mobyObjectTreeNode),
						input.getChildCount());
			} else {
				// we have a collection
				MobyPrimaryDataSet collection = (MobyPrimaryDataSet) inputs[i];
				DefaultMutableTreeNode collectionNode = new DefaultMutableTreeNode(
						"Collection('" + collection.getName() + "')");
				input.insert(collectionNode, input.getChildCount());
				MobyPrimaryDataSimple[] simples = collection.getElements();
				for (int j = 0; j < simples.length; j++) {
					MobyPrimaryDataSimple simple = simples[j];
					StringBuffer sb = new StringBuffer(
							"Namespaces used by this object: ");
					MobyNamespace[] namespaces = simple.getNamespaces();
					for (int k = 0; k < namespaces.length; k++) {
						sb.append(namespaces[k].getName() + " ");
					}
					if (namespaces.length == 0)
						sb.append(" ANY ");
					MobyObjectTreeNode mobyObjectTreeNode = new MobyObjectTreeNode(
							simple.getDataType().getName() + "('"
									+ simple.getName() + "')", sb.toString());
					collectionNode
							.insert(new DefaultMutableTreeNode(
									mobyObjectTreeNode), collectionNode
									.getChildCount());
				}

			}
		}
		if (inputs.length == 0) {
			input.add(new DefaultMutableTreeNode(" None "));
		}

		// process outputs
		MobyData[] outputs = activity.getMobyService().getPrimaryOutputs();
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] instanceof MobyPrimaryDataSimple) {
				MobyPrimaryDataSimple simple = (MobyPrimaryDataSimple) outputs[i];
				StringBuffer sb = new StringBuffer(
						"Namespaces used by this object: ");
				MobyNamespace[] namespaces = simple.getNamespaces();
				for (int j = 0; j < namespaces.length; j++) {
					sb.append(namespaces[j].getName() + " ");
				}
				if (namespaces.length == 0)
					sb.append(" ANY ");
				MobyObjectTreeNode mobyObjectTreeNode = new MobyObjectTreeNode(
						simple.getDataType().getName() + "('"
								+ simple.getName() + "')", sb.toString());
				mobyObjectTreeNode.setNamespaces(simple.getNamespaces());
				output.insert(new DefaultMutableTreeNode(mobyObjectTreeNode),
						output.getChildCount());
			} else {
				// we have a collection
				MobyPrimaryDataSet collection = (MobyPrimaryDataSet) outputs[i];
				DefaultMutableTreeNode collectionNode = new DefaultMutableTreeNode(
						"Collection('" + collection.getName() + "')");
				output.insert(collectionNode, output.getChildCount());
				MobyPrimaryDataSimple[] simples = collection.getElements();
				for (int j = 0; j < simples.length; j++) {
					MobyPrimaryDataSimple simple = simples[j];
					StringBuffer sb = new StringBuffer(
							"Namespaces used by this object: ");
					MobyNamespace[] namespaces = simple.getNamespaces();
					for (int k = 0; k < namespaces.length; k++) {
						sb.append(namespaces[k].getName() + " ");
					}
					if (namespaces.length == 0)
						sb.append("ANY ");
					MobyObjectTreeNode mobyObjectTreeNode = new MobyObjectTreeNode(
							simple.getDataType().getName() + "('"
									+ simple.getName() + "')", sb.toString());
					mobyObjectTreeNode.setNamespaces(simple.getNamespaces());
					collectionNode
							.insert(new DefaultMutableTreeNode(
									mobyObjectTreeNode), collectionNode
									.getChildCount());
				}

			}
		}
		if (outputs.length == 0) {
			output.add(new DefaultMutableTreeNode(" None "));
		}

		// finally return a tree describing the object
		final JTree tree = new JTree(rootNode);
		tree.setCellRenderer(new BioMobyServiceTreeCustomRenderer());
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
					TreePath path = tree.getPathForLocation(me.getX(), me
							.getY());
					if (path == null)
						return;
					if (path.getPathCount() >= 3) {
						if (path.getPathCount() == 4
								&& path.getParentPath().getLastPathComponent()
										.toString().startsWith("Collection(")
								&& (path.getParentPath().toString())
										.indexOf("Inputs") > 0) {
							// we have a collection input
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
									.getLastSelectedPathComponent();
							final String selectedObject = node.toString();
							// ensure that the last selected item is an object!
							if (!selectedObject.equals(path
									.getLastPathComponent().toString()))
								return;
							String collectionName = "";
							if (path.getParentPath().getLastPathComponent()
									.toString().indexOf("('") > 0
									&& path.getParentPath()
											.getLastPathComponent().toString()
											.indexOf("')") > 0) {
								collectionName = path.getParentPath()
										.getLastPathComponent().toString()
										.substring(
												path.getParentPath()
														.getLastPathComponent()
														.toString().indexOf(
																"('") + 2,
												path.getParentPath()
														.getLastPathComponent()
														.toString().indexOf(
																"')"));
							}
							final String theCollectionName = collectionName;
							final JPopupMenu menu = new JPopupMenu();
							// Create and add a menu item for adding to the item
							// to the workflow
							JMenuItem item = new JMenuItem("Add Datatype - "
									+ selectedObject + " to the workflow?");
							item
									.setIcon(MobyPanel.getIcon("/Add24.gif"));
							item.addActionListener(new ActionListener() {
								// private boolean added = false;

								public void actionPerformed(ActionEvent ae) {

									try {
										Dataflow currentDataflow = fileManager.getCurrentDataflow();
										Edit<?> edit = new AddBiomobyCollectionDataTypeEdit(
												currentDataflow, activity,
												selectedObject,
												theCollectionName, editManager.getEdits());
										editManager.doDataflowEdit(
												currentDataflow, edit);

									} catch (Exception e) {
										logger.error("", e);
									}
								}
							});
							// Create and add a menu item for service details
							JMenuItem details = new JMenuItem("Find out about "
									+ selectedObject);
							details
									.setIcon(MobyPanel.getIcon("/Information24.gif"));
							details.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {

									// TODO Create a frame
	    						    Frame frame = MobyPanel.CreateFrame("A BioMoby Object Description");
									JPanel panel = new MobyPanel(
											selectedObject,
											"A BioMoby Object Description", "");

									frame.add(panel);
									frame.setSize(getFrameSize());
	    							frame.pack();
	    							frame.setVisible(true);
								}
							});
							// add the components to the menu
							menu.add(new JLabel("Add to workflow ... ",
									JLabel.CENTER));
							menu.add(new JSeparator());
							menu.add(item);
							menu.add(new JSeparator());
							menu.add(new JLabel("Datatype Details ... ",
									JLabel.CENTER));
							menu.add(new JSeparator());
							menu.add(details);
							// show the window
							menu.show(me.getComponent(), me.getX(), me.getY());
						} else if (path.getPathCount() == 3
								&& path.getParentPath().getLastPathComponent()
										.toString().startsWith("Inputs")
								&& !path.getLastPathComponent().toString()
										.startsWith("Collection(")
								&& !path.getLastPathComponent().toString()
										.equals(" None ")) {
							// we have a simple input
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
									.getLastSelectedPathComponent();
							if (node == null)
								return;
							final String selectedObject = node.toString();
							// ensure that the last selected item is an object!
							if (!selectedObject.equals(path
									.getLastPathComponent().toString()))
								return;

							final JPopupMenu menu = new JPopupMenu();
							// Create and add a menu item for adding to the item
							// to the workflow
							JMenuItem item = new JMenuItem("Add Datatype - "
									+ selectedObject + " to the workflow?");
							item
									.setIcon(MobyPanel.getIcon("/Add24.gif"));
							item.addActionListener(new ActionListener() {
								// private boolean added = false;

								public void actionPerformed(ActionEvent ae) {

									try {
										Dataflow currentDataflow = fileManager.getCurrentDataflow();
										Edit<?> edit = new AddBiomobyDataTypeEdit(
												currentDataflow, activity,
												selectedObject, editManager.getEdits());
										editManager.doDataflowEdit(
												currentDataflow, edit);

									} catch (Exception e) {
										logger.error("Could not perform action", e);
									}
								}
							});

							// Create and add a menu item for service details
							JMenuItem details = new JMenuItem(
									"Find out about 1 " + selectedObject);
							details
									.setIcon(MobyPanel.getIcon("/Information24.gif"));
							details.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									// TODO Create a frame
	    						    Frame frame = MobyPanel.CreateFrame("A BioMoby Object Description");
									JPanel panel = new MobyPanel(
											// TODO create a valid description
											selectedObject,
											"A BioMoby Object Description",
											createDataDescription(
													selectedObject.split("\\(")[0],
													activity.getConfiguration()
															.getMobyEndpoint()));
									frame.add(panel);
									frame.setSize(getFrameSize());
	    							frame.pack();
	    							frame.setVisible(true);
								}

								private String createDataDescription(
										String dataName, String mobyEndpoint) {
									MobyDataType data;
									try {
										Central central = new CentralImpl(
												mobyEndpoint);
										data = central.getDataType(dataName);

									} catch (MobyException e) {
										return "Couldn't retrieve a description on the BioMoby service '"
												+ dataName + "'";
									} catch (NoSuccessException e) {
										return "Couldn't retrieve a description on the BioMoby service '"
												+ dataName + "'";
									}
									return data.toString();
								}
							});
							// add the components to the menu
							menu.add(new JLabel("Add to workflow ... ",
									JLabel.CENTER));
							menu.add(new JSeparator());
							menu.add(item);
							menu.add(new JSeparator());
							menu.add(new JLabel("Datatype Details ... ",
									JLabel.CENTER));
							menu.add(new JSeparator());
							menu.add(details);
							// show the window
							menu.show(me.getComponent(), me.getX(), me.getY());

						} else if (path.getParentPath().toString().indexOf(
								"Outputs") >= 0
								&& path.getLastPathComponent().toString()
										.indexOf(" None ") == -1) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
									.getLastSelectedPathComponent();
							if (node == null)
								return;
							final String selectedObject = node.toString();
							if (!selectedObject.equals(path
									.getLastPathComponent().toString()))
								return;

							if ((path.getPathCount() == 4
									&& path.getParentPath()
											.getLastPathComponent().toString()
											.startsWith("Collection(") && (path
									.getParentPath().toString())
									.indexOf("Outputs") > 0)
									|| (path.toString().indexOf("Collection(") < 0)) {
								final JPopupMenu menu = new JPopupMenu();
								JMenuItem item = new JMenuItem(
										"Find Services that Consume "
												+ selectedObject
												+ " - brief search");
								item
										.setIcon(MobyPanel.getIcon("/Information24.gif"));
								final String potentialCollectionString = path
										.getParentPath().getLastPathComponent()
										.toString();
								final boolean isCollection = potentialCollectionString
										.indexOf("Collection('") >= 0;
								final Object selectedMobyObjectTreeNodeHolder = (DefaultMutableTreeNode) tree
										.getLastSelectedPathComponent();
								item.addActionListener(new ActionListener() {

									public void actionPerformed(ActionEvent ae) {
										// you would like to search for
										// selectedObject
										new Thread("Finding biomoby services") {
											public void run() {
												try {
													// FIXME: ignored for now -
													// Stuart
													String name = selectedObject;
													if (name.indexOf("(") > 0)
														name = name
																.substring(
																		0,
																		name
																				.indexOf("("));
													String articlename = "";
													if (isCollection) {
														articlename = potentialCollectionString
																.substring(
																		potentialCollectionString
																				.indexOf("('") + 2,
																		potentialCollectionString
																				.lastIndexOf("'"));
													} else {
														articlename = selectedObject
																.substring(
																		selectedObject
																				.indexOf("'") + 1,
																		selectedObject
																				.lastIndexOf("'"));
													}

													BiomobyObjectActivity boAct = new BiomobyObjectActivity();
													BiomobyObjectActivityConfigurationBean bean = new BiomobyObjectActivityConfigurationBean();
													MobyDataType dataType = new MobyDataType(
															name);
													bean
															.setAuthorityName(dataType
																	.getAuthority());
													bean
															.setServiceName(dataType
																	.getName());
													bean
															.setMobyEndpoint(endpoint);
													boAct.configure(bean);

													OutputPort theServiceport = null;

													try {
														if (isCollection)
															theServiceport = Tools
																	.getActivityOutputPort(
																			activity,
																			name
																					+ "(Collection - '"
																					+ (articlename
																							.equals("") ? "MobyCollection"
																							: articlename)
																					+ "' As Simples)");

														else
															theServiceport = Tools
																	.getActivityOutputPort(
																			activity,
																			name
																					+ "("
																					+ articlename
																					+ ")");
													} catch (Exception except) {
													}
													BiomobyObjectActionHelper boa = null;

													if (theServiceport == null) {
														boa = new BiomobyObjectActionHelper(
																false, editManager, fileManager);
													} else {
														boa = new BiomobyObjectActionHelper(
																theServiceport,
																false, editManager, fileManager);
													}

													if (selectedMobyObjectTreeNodeHolder instanceof DefaultMutableTreeNode
															&& ((DefaultMutableTreeNode) selectedMobyObjectTreeNodeHolder)
																	.getUserObject() instanceof MobyObjectTreeNode) {
														DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) selectedMobyObjectTreeNodeHolder;
														MobyObjectTreeNode motn = (MobyObjectTreeNode) dmtn
																.getUserObject();
														boa
																.setNamespaces(motn
																		.getNamespaces());
													}
													PopupThread popupthread = new PopupThread(
															boAct, boa);
													progressBar
															.setStringPainted(true);
													progressBar
															.setVisible(true);
													popupthread.start();

													while (popupthread
															.isAlive()) {
														Thread.sleep(4000);
													}

													progressBar
															.setVisible(false);
													Component c = popupthread
															.getComponent();
													Dimension loc = getFrameLocation();
													Dimension size = getFrameSize();
													JPanel frame = new SimpleActionFrame(
															c,
															"Moby Object Details");
													createFrame(
															frame,
															(int) loc
																	.getWidth(),
															(int) loc
																	.getHeight(),
															(int) size
																	.getWidth(),
															(int) size
																	.getHeight());
												} catch (Exception e) {
												}
											}
										}.start();

									}
								});

								JMenuItem item2 = new JMenuItem(
										"Find Services that Consume "
												+ selectedObject
												+ " - semantic search");
								item2
										.setIcon(MobyPanel.getIcon("/Search24.gif"));
								item2.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent ae) {
										// you would like to search for
										// selectedObject
										new Thread("Finding BioMoby services") {

											public void run() {
												try {
													String name = selectedObject;
													if (name.indexOf("(") > 0)
														name = name
																.substring(
																		0,
																		name
																				.indexOf("("));
													String articlename = "";
													if (isCollection) {
														articlename = potentialCollectionString
																.substring(
																		potentialCollectionString
																				.indexOf("('") + 2,
																		potentialCollectionString
																				.lastIndexOf("'"));
													} else {
														articlename = selectedObject
																.substring(
																		selectedObject
																				.indexOf("'") + 1,
																		selectedObject
																				.lastIndexOf("'"));
													}
													BiomobyObjectActivity boAct = new BiomobyObjectActivity();
													BiomobyObjectActivityConfigurationBean bean = new BiomobyObjectActivityConfigurationBean();
													MobyDataType dataType = new MobyDataType(
															name);
													bean
															.setAuthorityName(dataType
																	.getAuthority());
													bean
															.setServiceName(dataType
																	.getName());
													bean
															.setMobyEndpoint(endpoint);
													boAct.configure(bean);

													OutputPort theServiceport = null;

													try {

														if (isCollection)
															theServiceport = Tools
																	.getActivityOutputPort(
																			activity,
																			name
																					+ "(Collection - '"
																					+ (articlename
																							.equals("") ? "MobyCollection"
																							: articlename)
																					+ "' As Simples)");

														else
															theServiceport = Tools
																	.getActivityOutputPort(
																			activity,
																			name
																					+ "("
																					+ articlename
																					+ ")");
													} catch (Exception except) {
													}
													BiomobyObjectActionHelper boa = null;

													if (theServiceport == null)
														boa = new BiomobyObjectActionHelper(
																true, editManager, fileManager);
													else
														boa = new BiomobyObjectActionHelper(
																theServiceport,
																true, editManager, fileManager);
													if (selectedMobyObjectTreeNodeHolder instanceof DefaultMutableTreeNode
															&& ((DefaultMutableTreeNode) selectedMobyObjectTreeNodeHolder)
																	.getUserObject() instanceof MobyObjectTreeNode) {
														DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) selectedMobyObjectTreeNodeHolder;
														MobyObjectTreeNode motn = (MobyObjectTreeNode) dmtn
																.getUserObject();
														boa
																.setNamespaces(motn
																		.getNamespaces());
													}

													PopupThread popupthread = new PopupThread(
															boAct, boa);
													progressBar
															.setStringPainted(true);
													progressBar
															.setVisible(true);
													popupthread.start();

													while (popupthread
															.isAlive()) {
														Thread.sleep(4000);
													}

													progressBar
															.setVisible(false);
													Component c = popupthread
															.getComponent();
													Dimension loc = getFrameLocation();
													Dimension size = getFrameSize();
													JPanel frame = new SimpleActionFrame(
															c,
															"Moby Object Details");
													createFrame(
															frame,
															(int) loc
																	.getWidth(),
															(int) loc
																	.getHeight(),
															(int) size
																	.getWidth(),
															(int) size
																	.getHeight());
												} catch (Exception e) {
												}
											}
										}.start();
									}
								});

								// string may be needed to extract the
								// collection article name
								// final String potentialCollectionString =
								// path.getParentPath()
								// .getLastPathComponent().toString();
								// final boolean isCollection =
								// potentialCollectionString
								// .indexOf("Collection('") >= 0;

								JMenuItem item3 = new JMenuItem(
										"Add parser for " + selectedObject
												+ " to the workflow");
								item3
										.setIcon(MobyPanel.getIcon("/Cut24.gif"));
								item3.addActionListener(new ActionListener() {

									public void actionPerformed(ActionEvent ae) {

										try {
											Dataflow currentDataflow = fileManager.getCurrentDataflow();
											Edit<?> edit = new AddMobyParseDatatypeEdit(
													currentDataflow, activity,
													selectedObject,isCollection, potentialCollectionString, editManager.getEdits());
											editManager.doDataflowEdit(
													currentDataflow, edit);

										} catch (Exception e) {
											logger.error("Could not perform action", e);
										}

									}
								});

								menu.add(new JLabel(
										"Moby Service Discovery ... ",
										JLabel.CENTER));
								menu.add(new JSeparator());
								menu.add(item);
								menu.add(new JSeparator());
								menu.add(item2);
								menu.add(new JLabel("Parse Moby Data ... ",
										JLabel.CENTER));
								menu.add(new JSeparator());
								menu.add(item3);

								menu.show(me.getComponent(), me.getX(), me
										.getY());
							}
						}
					}
				}
			}

			public void mouseEntered(MouseEvent me) {
			}

			public void mouseExited(MouseEvent me) {
			}
		});

		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane jsp = new JScrollPane(tree);
		JPanel thePanel = new JPanel(new BorderLayout());
		thePanel.add(jsp, BorderLayout.CENTER);
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setString("Finding Services ... ");
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		thePanel.add(progressBar, BorderLayout.PAGE_END);
		return thePanel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.embl.ebi.escience.scuflui.processoractions.ProcessorActionSPI#
	 * getDescription()
	 */
	public String getDescription() {
		return "Moby Service Details";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.embl.ebi.escience.scuflui.processoractions.ProcessorActionSPI#getIcon
	 * ()
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

	/**
	 * Return an Icon to represent this action
	 *
	 * @param loc
	 *            the location of the image to use as an icon
	 */
	public ImageIcon getIcon(String loc) {
		return MobyPanel.getIcon(loc);
	}

	/**
	 * Where should the frame open?
	 */
	public Dimension getFrameLocation() {
		return new Dimension(100, 100);
	}

	public void createFrame(JPanel targetComponent,
			int posX, int posY, int sizeX, int sizeY) {
		final JPanel component = targetComponent;
		JFrame newFrame = new JFrame(component.getName());
		newFrame.getContentPane().setLayout(new BorderLayout());
		newFrame.getContentPane().add(
				new JScrollPane((JComponent) targetComponent),
				BorderLayout.CENTER);
		newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newFrame.setSize(sizeX, sizeY);
		newFrame.setLocation(posX, posY);
		newFrame.setVisible(true);
	}

}
