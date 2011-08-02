package net.sf.taverna.t2.activities.biomoby.datatypedescriptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivity;
import net.sf.taverna.t2.activities.biomoby.BiomobyObjectActivityConfigurationBean;
import net.sf.taverna.t2.activities.biomoby.query.BiomobyObjectActivityItem;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

public class BiomobyDatatypeDescription extends
		ServiceDescription<BiomobyObjectActivityConfigurationBean> {

	private String authorityName;
	private String emailContact;
	private String datatypeName;
	private URI lsid;
	private String parent;
	private String[] lineage;

	private URI endpoint;
	private URI namespace;

	@Override
	public Class<? extends Activity<BiomobyObjectActivityConfigurationBean>> getActivityClass() {
		return BiomobyObjectActivity.class;
	}

	@Override
	public BiomobyObjectActivityConfigurationBean getActivityConfiguration() {
		BiomobyObjectActivityConfigurationBean bean = new BiomobyObjectActivityConfigurationBean();
		bean.setAuthorityName(getAuthorityName());
		bean.setServiceName(getDatatypeName());
		bean.setMobyEndpoint(getEndpoint().toASCIIString());
		return bean;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getEmailContact() {
		return emailContact;
	}

	public void setEmailContact(String emailContact) {
		this.emailContact = emailContact;
	}

	public String getDatatypeName() {
		return datatypeName;
	}

	public void setDatatypeName(String datatypeName) {
		this.datatypeName = datatypeName;
	}

	public URI getLsid() {
		return lsid;
	}

	public void setLsid(URI lsid) {
		this.lsid = lsid;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}

	public URI getNamespace() {
		return namespace;
	}

	public void setNamespace(URI namespace) {
		this.namespace = namespace;
	}

	public String[] getLineage() {
		return lineage;
	}

	public void setLineage(String[] lineage) {
		this.lineage = lineage;
	}

	@Override
	public String getName() {
		return getDatatypeName();
	}

	@Override
	protected List<Object> getIdentifyingData() {
		return Arrays.<Object>asList(getNamespace(), getEndpoint(), getAuthorityName(), getDatatypeName());
	}

	@Override
	public List<String> getPath() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Biomoby @ "  + getEndpoint());
		list.add("MOBY Objects");
		list.addAll(Arrays.asList(getLineage()));
		return list;
	}

	@Override
	public Icon getIcon() {
		return new BiomobyObjectActivityItem().getIcon();
	}
	@Override
	public String toString() {
		return getName();
	}

//	public Edit getInsertionEdit(Dataflow dataflow, Processor p, Activity a) {
//		if (a instanceof BiomobyObjectActivity) {
//			return new AddUpstreamObjectEdit(dataflow, p, (BiomobyObjectActivity) a);
//		}
//		return null;
//	}
}
