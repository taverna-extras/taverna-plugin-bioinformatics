/*
 * Created on Sep 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.lang.ui.DialogTextArea;

/**
 * @author Eddie Kawas
 *
 * 
 */
public class MobyPanel extends JPanel {

	
	private static final long serialVersionUID = 1L;
	private DialogTextArea textArea = null;
	private String text = "";
	private String name = "";
	private JLabel jLabel = new JLabel();
	
	/**
	 * This is the default constructor
	 */
	public MobyPanel(String label, String name, String text) {
		super(new BorderLayout());
		jLabel.setText(label);
		this.text = text;
		this.name = name;
		initialize();
	}
	
	public MobyPanel(String label) {
		super(new BorderLayout());
		jLabel.setText(label);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(450, 450);
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		add(jLabel, BorderLayout.NORTH);
		add(getTextArea(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return DialogTextArea	
	 */    
	private DialogTextArea getTextArea() {
		if (textArea == null) {
			textArea = new DialogTextArea();
		}
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(this.text);
		textArea.setEditable(false);
		textArea.setEnabled(true);
		textArea.setAutoscrolls(true);
		textArea.setCaretPosition(0);
		return textArea;
	}
	
	public void setText(String text) {
	    this.text = text;
	    if (textArea == null) {
			textArea = new DialogTextArea(this.text);
		}
	    textArea.setText(text);
	}

    /* (non-Javadoc)
     * @see org.embl.ebi.escience.scuflui.ScuflUIComponent#getIcon()
     */
    public ImageIcon getIcon() {
    	return new ImageIcon(MobyPanel.class.getResource("/moby_small.png"));
    }
    
    /**
     * 
     * @param icon a relative path to an icon to get
     * @return the ImageIcon at icon 
     */
    public static ImageIcon getIcon(String icon) {
    	return new ImageIcon(MobyPanel.class.getResource(icon));
    }
    
    public String getName(){
    	if (name==null) return "";
    	else return name;
    }

	public void onDisplay() {
		// TODO Auto-generated method stub
		
	}

	public void onDispose() {
		// TODO Auto-generated method stub
		
	}

	public static Frame CreateFrame(String title) {
		// Create a frame
	    Frame frame = new Frame(title);
	    // Add a listener for the close event
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent evt) {
	            Frame frame = (Frame)evt.getSource();
	            // Hide the frame
	            frame.setVisible(false);
	            // If the frame is no longer needed, call dispose
	            frame.dispose();
	        }
	    });
	    return frame;
	}
  }
