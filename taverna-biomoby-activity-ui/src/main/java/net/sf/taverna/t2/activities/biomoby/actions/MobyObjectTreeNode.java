package net.sf.taverna.t2.activities.biomoby.actions;

import org.biomoby.shared.MobyNamespace;

public class MobyObjectTreeNode {

    //  name of the object == node name
    private String name = "";

    // description of object == tool tip text
    private String description = "";

	private MobyNamespace[] ns = null;
    /**
     *
     * @param name - the name of the Moby Object
     * @param description - the description of the Moby Service
     */
    public MobyObjectTreeNode(String name, String description) {
        this.name = name;
        this.description = description;
    }
    /*
     * over-ride the toString method in order to print node values
     * that make sense.
     */
    public String toString() {
        return name;
    }

    public void setNamespaces(MobyNamespace[] namespaces) {
    	if (namespaces != null && namespaces.length == 0)
    		this.ns = null;
    	else
    		this.ns = namespaces;
    }

    public MobyNamespace[] getNamespaces() {
    	return this.ns;
    }

    public String getDescription() {
        return this.description;
    }
}
