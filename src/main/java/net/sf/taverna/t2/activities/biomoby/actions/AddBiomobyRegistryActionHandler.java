/*******************************************************************************
 * This file is a component of the Taverna project, and is licensed  under the
 *  GNU LGPL. Copyright Edward Kawas, The BioMoby Project
 ******************************************************************************/
package net.sf.taverna.t2.activities.biomoby.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sf.taverna.t2.activities.biomoby.query.BiomobyQuery;
import net.sf.taverna.t2.partition.AddQueryActionHandler;
import net.sf.taverna.t2.workbench.helper.HelpEnabledDialog;

import org.apache.log4j.Logger;
import org.biomoby.client.CentralImpl;

import net.sf.taverna.t2.activities.biomoby.actions.BiomobyScavengerDialog;

@SuppressWarnings("serial")
public class AddBiomobyRegistryActionHandler extends AddQueryActionHandler {
	
	private JFrame parentFrame = null;

	private String endpoint = CentralImpl.DEFAULT_ENDPOINT;
	private String uri = CentralImpl.DEFAULT_NAMESPACE;
	
	private static Logger logger = Logger
			.getLogger(AddBiomobyRegistryActionHandler.class);
	@Override
	public void actionPerformed(final ActionEvent actionEvent) {
		
		final HelpEnabledDialog dialog = new HelpEnabledDialog(parentFrame,
                "Add Your Custom BioMoby Registry", true, null);
        final BiomobyScavengerDialog msp = new BiomobyScavengerDialog();
        dialog.getContentPane().add(msp);
        JButton accept = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        msp.add(accept);
        msp.add(cancel);
        accept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae2) {
                if (dialog.isVisible()) {
                    String registryEndpoint = "";
                    String registryURI = "";
                    
                    if (msp.getRegistryEndpoint().equals(""))
                        registryEndpoint = endpoint;
                    else
                        registryEndpoint = msp.getRegistryEndpoint();
                    
                    if (msp.getRegistryEndpoint().equals(""))
                        registryURI = uri;
                    else
                        registryURI = msp.getRegistryURI();
                    
                    try {
                    	final String url = registryEndpoint;
                    	final String uri = registryURI;
                    	addQuery(new BiomobyQuery(url,uri));
                    } catch (Exception e) {
                        JOptionPane
                                .showMessageDialog(parentFrame,
                                        "Unable to create scavenger!\n"
                                                + e.getMessage(),
                                        "Exception!",
                                        JOptionPane.ERROR_MESSAGE);
                        logger.error("Exception thrown:", e);
                    } finally {
                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae2) {
                if (dialog.isVisible()) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        });
        dialog.setResizable(false);
        dialog.getContentPane().add(msp);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
	}

	@Override
	protected Icon getIcon() {
		return new ImageIcon(AddBiomobyRegistryActionHandler.class.getResource("/registry.gif"));
	}

	@Override
	protected String getText() {
		return "Biomoby...";
	}

}
