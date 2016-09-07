package net.sf.taverna.t2.activities.biomoby.actions;
import java.util.ArrayList;

public class MobyServiceTreeNode {

    // name of the service == node name
    private String name = "";

    // list of objects that service produces
    @SuppressWarnings("unused")
	private ArrayList<MobyObjectTreeNode> mobyObjectTreeNodes = null;

    // description of object == tool tip text
    private String description = "";


    /**
     *
     * @param name - the name of the Moby Service
     * @param description - the description of the Moby Service
     */
    public MobyServiceTreeNode(String name, String description) {
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

    public String getDescription() {
        return this.description;
    }
}
