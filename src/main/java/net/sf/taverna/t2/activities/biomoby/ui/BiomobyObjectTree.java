/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.actions.MobyPanel;
import net.sf.taverna.t2.activities.biomoby.datatypedescriptions.BiomobyDatatypeDescription;
import net.sf.taverna.t2.activities.biomoby.edits.AddUpstreamObjectEdit;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyObjectActivityItem;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyQueryHelper;
import net.sf.taverna.t2.workbench.edits.EditManager;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workflowmodel.CompoundEdit;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edit;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

import org.biomoby.client.CentralImpl;
import org.biomoby.shared.MobyException;

/**
 * Creates a Datatype tree for any BioMOBY registry. The tree allows the user to
 * add nodes to the workflow. Includes the ability to search for datatypes too.
 * 
 * @author Eddie Kawas, The BioMoby Project
 * 
 */
public class BiomobyObjectTree {

	private JTree tree;
	private String registryEndpoint = "";
	private String registryNamespace = "";
	private static String SEARCH_DATATYPE_TEXT = "Type to search!";
	private FilterTreeModel model;

	/**
	 * Default constructor. Creates a BiomobyObjectTree for the default Biomoby
	 * registry
	 */
	public BiomobyObjectTree() {
		this(CentralImpl.DEFAULT_ENDPOINT, CentralImpl.DEFAULT_NAMESPACE);
	}

	/**
	 * 
	 * @param url
	 *            the Biomoby registry endpoint URL to build a tree for
	 * @param uri
	 *            the Biomoby registry namespace URI to build a tree for
	 */
	public BiomobyObjectTree(String url, String uri) {
		this.registryEndpoint = url;
		this.registryNamespace = uri;
	}

	/*
	 * method that inserts our BiomobyDatatypeDescription object into our tree
	 */
	private void insertDescriptionIntoTree(
			BiomobyDatatypeDescription description,
			HashMap<String, FilterTreeNode> nodeMap,
			HashMap<String, BiomobyDatatypeDescription> descriptionMap) {
		FilterTreeNode node = nodeMap.containsKey(description.getName()) ? nodeMap
				.get(description.getName())
				: new FilterTreeNode(description);

		String parent = description.getParent();
		if (parent.equals(""))
			parent = "Object";
		FilterTreeNode pNode = nodeMap.containsKey(parent) ? nodeMap
				.get(parent) : new FilterTreeNode(descriptionMap.get(parent));
		pNode.add(node);
		nodeMap.put(description.getName(), node);
		nodeMap.put(parent, pNode);
	}

	/**
	 * 
	 * @return a Tree containing the datatype ontology for the specified biomoby
	 *         registry
	 * @throws MobyException
	 *             if there is a problem comunicating with the specified biomoby
	 *             registry
	 */
	public Component getDatatypeTree() throws MobyException {
		BiomobyQueryHelper bqh = new BiomobyQueryHelper(getRegistryEndpoint(),
				getRegistryNamespace());
		List<BiomobyDatatypeDescription> descriptions = bqh
				.findDatatypeDescriptions();

		// create a tree from all of the nodes
		HashMap<String, BiomobyDatatypeDescription> descriptionMap = new HashMap<String, BiomobyDatatypeDescription>();
		HashMap<String, FilterTreeNode> nodeMap = new HashMap<String, FilterTreeNode>();
		for (BiomobyDatatypeDescription d : descriptions) {
			// PRECONDITION: datatype names are unique across the ontology
			descriptionMap.put(d.getDatatypeName(), d);
		}

		nodeMap.put("Object", new FilterTreeNode(descriptionMap.get("Object")));
		for (BiomobyDatatypeDescription d : descriptions) {
			if (!d.getName().equals("Object"))
				insertDescriptionIntoTree(d, nodeMap, descriptionMap);
		}
		// construct a new tree with our root node
		tree = new JTree(nodeMap.get("Object"));

		// only allow one node to be selected at once
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		model = new FilterTreeModel((FilterTreeNode) tree.getModel().getRoot());
		tree.setModel(model);

		// set up the icon and tooltips for the nodes in the tree
		ImageIcon icon = new ImageIcon(BiomobyObjectActivityItem.class
				.getResource("/biomoby_object.png"));
		if (icon != null) {
			DefaultTreeCellRenderer renderer = new DatatypeTreeRenderer();
			renderer.setLeafIcon(icon);
			renderer.setOpenIcon(icon);
			renderer.setClosedIcon(icon);
			renderer.setIcon(icon);
			tree.setCellRenderer(renderer);
		}

		// add a mouse listener to catch context clicks
		// the listener adds the selected datatype to the workflow
		// it also adds the datatype's container relationships
		tree.addMouseListener(new BiomobyObjectTreeMouseListener());
		// clear the hashmaps to clear some memory
		nodeMap.clear();
		descriptionMap.clear();
		// register our tree for tool tips
		ToolTipManager.sharedInstance().registerComponent(tree);
		// insert the tree into a scrollpane
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setSize(getFrameSize());

		// create a new panel to hold the scrollpane and a search box
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(treeView, BorderLayout.CENTER);
		JTextField search = new JTextField(SEARCH_DATATYPE_TEXT);
		panel.add(search, BorderLayout.PAGE_END);
		search.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField field = (JTextField) e.getSource();
					if (field.getText().trim().equals(
							BiomobyObjectTree.SEARCH_DATATYPE_TEXT)) {
						field.setText("");
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField field = (JTextField) e.getSource();
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						field.setText(SEARCH_DATATYPE_TEXT);
						model.setFilter(null);
						return;
					}
					// filter our tree
					if (!field.getText().trim().equals(
							BiomobyObjectTree.SEARCH_DATATYPE_TEXT)) {
						// does our filter tree model exist yet?
						model.setFilter(field.getText().trim());
					}
				}
			}

			public void keyTyped(KeyEvent e) {

			}
		});
		search.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField field = (JTextField) e.getSource();
					if (field.getText().trim().equals(
							BiomobyObjectTree.SEARCH_DATATYPE_TEXT)) {
						field.setText("");
					}
				}
			}

			public void focusLost(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField field = (JTextField) e.getSource();
					if (field.getText().trim().equals("")) {
						field.setText(SEARCH_DATATYPE_TEXT);
					}
				}
			}
		});
		// done
		panel.setToolTipText("Datatype Viewer for " + getRegistryEndpoint().toString());
		return panel;
	}

	/**
	 * 
	 * @param registryEndpoint
	 *            the endpoint to set
	 */
	public void setRegistryEndpoint(String registryEndpoint) {
		this.registryEndpoint = registryEndpoint;
	}

	/**
	 * 
	 * @param registryNamespace
	 *            the namespace to set
	 */
	public void setRegistryNamespace(String registryNamespace) {
		this.registryNamespace = registryNamespace;
	}

	/**
	 * 
	 * @return the registry endpoint that this tree is using
	 */
	public String getRegistryEndpoint() {
		return registryEndpoint;
	}

	/**
	 * 
	 * @return the registry namespace that this tree is using
	 */
	public String getRegistryNamespace() {
		return registryNamespace;
	}

	/**
	 * returns the frame size as a dimension for the content pane housing this
	 * action
	 */
	public Dimension getFrameSize() {
		return new Dimension(550, 450);
	}

	/*
	 * A mouse listener for our datatype tree
	 */
	private class BiomobyObjectTreeMouseListener implements MouseListener {
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

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (node == null)
					return;

				final String selectedObject = node.toString();
				final BiomobyDatatypeDescription bdd = (BiomobyDatatypeDescription) node
						.getUserObject();
				final JPopupMenu menu = new JPopupMenu();
				// Create and add a menu item for adding to the
				// item to the workflow
				JMenuItem item = new JMenuItem("Add Datatype - '"
						+ selectedObject + "' to the workflow?");

				item.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent ae) {

						try {
							Dataflow dataflow = FileManager.getInstance()
									.getCurrentDataflow();
							Edits edits = EditsRegistry.getEdits();
							List<Edit<?>> compoundEdits = new ArrayList<Edit<?>>();
							List<Edit<?>> editList = new ArrayList<Edit<?>>();

							String name = Tools.uniqueProcessorName(
									selectedObject, dataflow);

							BiomobyObjectActivityConfigurationBean configBean = new BiomobyObjectActivityConfigurationBean();
							configBean.setMobyEndpoint(bdd
									.getActivityConfiguration()
									.getMobyEndpoint());
							configBean.setAuthorityName("");
							configBean.setServiceName(selectedObject);

							net.sf.taverna.t2.workflowmodel.Processor sourceProcessor = edits
									.createProcessor(name);
							BiomobyObjectActivity boActivity = new BiomobyObjectActivity();
							Edit<?> configureActivityEdit = edits
									.getConfigureActivityEdit(boActivity,
											configBean);
							editList.add(configureActivityEdit);

							editList
									.add(edits
											.getDefaultDispatchStackEdit(sourceProcessor));

							Edit<?> addActivityToProcessorEdit = edits
									.getAddActivityEdit(sourceProcessor,
											boActivity);
							editList.add(addActivityToProcessorEdit);

							editList.add(edits.getAddProcessorEdit(dataflow,
									sourceProcessor));

							CompoundEdit compoundEdit = new CompoundEdit(
									editList);
							compoundEdits.add(compoundEdit);
							compoundEdit.doEdit();

							// process relationships
							Edit<?> edit = new AddUpstreamObjectEdit(dataflow,
									sourceProcessor, boActivity);
							EditManager editManager = EditManager.getInstance();
							editManager.doDataflowEdit(dataflow, edit);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				item.setIcon(MobyPanel.getIcon("/Add24.gif"));

				// add the components to the menus
				menu.add(new JLabel("Add to workflow ... ", JLabel.CENTER));
				menu.add(new JSeparator());
				menu.add(item);
				// show the window
				menu.show(me.getComponent(), me.getX(), me.getY());
			}
		}

		public void mouseEntered(MouseEvent me) {
		}

		public void mouseExited(MouseEvent me) {
		}
	}

	private static class DatatypeTreeRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 7287097980554656834L;

		// the max tool tip length
		private static int MAX_TOOLTIP_LENGTH = 300; 

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			if (value instanceof DefaultMutableTreeNode) {
				if (((DefaultMutableTreeNode) value).getUserObject() instanceof BiomobyDatatypeDescription) {
					BiomobyDatatypeDescription desc = (BiomobyDatatypeDescription) ((DefaultMutableTreeNode) value)
							.getUserObject();
					String d = desc.getDescription().trim();
					// we only keep MAX_TOOLTIP_LENGTH characters of the string
					if (d.length() > MAX_TOOLTIP_LENGTH)
						d = d.substring(0, MAX_TOOLTIP_LENGTH) + "...";
					setToolTipText("<html><body><div style='width:200px;'><span>"
							+ d + "</span></div></body></html>");
					
					ToolTipManager.sharedInstance().setDismissDelay(
							Integer.MAX_VALUE);
				}
			}
			return super.getTreeCellRendererComponent(tree, value, sel,
					expanded, leaf, row, hasFocus);
		}
	}

	/*
	 * Shamelessly stolen from t2. Made the Filter a simple string filter and
	 * modified the code a bit to make it relevant to my tree
	 */
	private final class FilterTreeModel extends DefaultTreeModel {

		private static final long serialVersionUID = 8446366558654481274L;
		String currentFilter;

		/**
		 * 
		 * @param node
		 *            the node to apply filtering to
		 */
		public FilterTreeModel(FilterTreeNode node) {
			this(node, null);
		}

		/**
		 * 
		 * @param node
		 *            the node to apply filtering to
		 * @param filter
		 *            the actual filter we will apply
		 */
		public FilterTreeModel(FilterTreeNode node, String filter) {
			super(node);
			currentFilter = filter;
			node.setFilter(filter);
		}

		/**
		 * 
		 * @param filter
		 *            the filter to set and apply to our node
		 */
		public void setFilter(String filter) {
			if (root != null) {
				currentFilter = filter;
				((FilterTreeNode) root).setFilter(filter);
				Object[] path = { root };
				fireTreeStructureChanged(this, path, null, null);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.tree.DefaultTreeModel#getChildCount(java.lang.Object)
		 */
		public int getChildCount(Object parent) {
			if (parent instanceof FilterTreeNode) {
				return (((FilterTreeNode) parent).getChildCount());
			}
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.DefaultTreeModel#getChild(java.lang.Object,
		 * int)
		 */
		public Object getChild(Object parent, int index) {
			if (parent instanceof FilterTreeNode) {
				return (((FilterTreeNode) parent).getChildAt(index));
			}
			return null;
		}

		/**
		 * Getter
		 * 
		 * @return the filter that we are currently using
		 */
		public String getCurrentFilter() {
			return currentFilter;
		}
	}

	private class FilterTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -5269485070471940445L;
		private String filter;
		private boolean passed = true;
		private List<FilterTreeNode> filteredChildren = new ArrayList<FilterTreeNode>();

		public FilterTreeNode(Object userObject) {
			super(userObject);
		}

		public String getFilter() {
			return filter;
		}

		public void setFilter(String filter) {
			this.filter = filter == null ? "" : filter;
			passed = false;
			filteredChildren.clear();
			if (filter == null) {
				passed = true;
				passFilterDown(null);
			} else if (pass(this)) {
				passed = true;
				passFilterDown(filter);
			} else {
				passFilterDown(filter);
				passed = filteredChildren.size() != 0;
			}
		}

		private boolean pass(FilterTreeNode node) {
			if (getFilter().trim().equals("")) {
				return true;
			}
			return node.getUserObject().toString().toLowerCase().trim()
					.contains(getFilter().toLowerCase().trim());
		}

		private void passFilterDown(String filter) {
			int realChildCount = super.getChildCount();
			for (int i = 0; i < realChildCount; i++) {
				FilterTreeNode realChild = (FilterTreeNode) super.getChildAt(i);
				realChild.setFilter(filter);
				if (realChild.isPassed()) {
					filteredChildren.add(realChild);
				}
			}
		}

		public void add(FilterTreeNode node) {
			super.add(node);
			node.setFilter(filter);
			if (node.isPassed()) {
				filteredChildren.add(node);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.DefaultMutableTreeNode#remove(int)
		 */
		public void remove(int childIndex) {
			if (filter != null) {
				// as child indexes might be inconsistent..
				throw new IllegalStateException(
						"Can't remove while the filter is active");
			}
			super.remove(childIndex);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.DefaultMutableTreeNode#getChildCount()
		 */
		public int getChildCount() {
			if (filter == null) {
				return super.getChildCount();
			}
			return (filteredChildren.size());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.DefaultMutableTreeNode#getChildAt(int)
		 */
		public FilterTreeNode getChildAt(int index) {
			if (filter == null) {
				return (FilterTreeNode) super.getChildAt(index);
			}
			return filteredChildren.get(index);
		}

		/**
		 * 
		 * @return
		 */
		public boolean isPassed() {
			return passed;
		}
	}

	public static void main(String[] args) throws Exception {
		// Create a frame
		String title = "TeST";
		JFrame frame = new JFrame(title);

		// Create a component to add to the frame
/*		Component comp = new BiomobyObjectTree(CentralImpl.DEFAULT_ENDPOINT,
				CentralImpl.DEFAULT_NAMESPACE).getDatatypeTree();*/
		
		Component comp = new BiomobyObjectTree("http://cropwiki.irri.org/cgi-bin/MOBY-Central.pl",
				CentralImpl.DEFAULT_NAMESPACE).getDatatypeTree();

		// Add the component to the frame's content pane;
		// by default, the content pane has a border layout
		frame.getContentPane().add(comp, BorderLayout.CENTER);

		// Show the frame
		int width = 300;
		int height = 300;
		frame.setSize(width, height);
		frame.setVisible(true);

		// Set to exit on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
