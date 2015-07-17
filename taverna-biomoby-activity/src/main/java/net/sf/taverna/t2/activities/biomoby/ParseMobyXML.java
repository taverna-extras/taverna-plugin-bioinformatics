/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.sf.taverna.t2.activities.biomoby;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.biomoby.shared.MobyNamespace;
import org.biomoby.shared.data.MobyContentInstance;
import org.biomoby.shared.data.MobyDataBoolean;
import org.biomoby.shared.data.MobyDataComposite;
import org.biomoby.shared.data.MobyDataFloat;
import org.biomoby.shared.data.MobyDataInstance;
import org.biomoby.shared.data.MobyDataInt;
import org.biomoby.shared.data.MobyDataJob;
import org.biomoby.shared.data.MobyDataObject;
import org.biomoby.shared.data.MobyDataObjectVector;
import org.biomoby.shared.data.MobyDataString;
import org.biomoby.shared.data.MobyDataUtils;
import org.biomoby.registry.meta.Registry;

/**
 * This class is used to help parse BioMOBY messages.
 *
 * @author Edward Kawas
 *
 */
public class ParseMobyXML {

    private static Logger logger = Logger.getLogger(ParseMobyXML.class);

    public static final int NAMESPACE = -10;

    public static final int ID = -20;

    public static final int VALUE = -30;

    private static final List<Integer> allowables;

    static {
	allowables = new ArrayList<Integer>();
	allowables.add(NAMESPACE);
	allowables.add(ID);
	allowables.add(VALUE);
    }

    /**
     * PRECONDITION: XML is valid MOBY xml and contains exactly 1 invocation
     * message containing our simple element
     *
     * @param names
     *                an array of article names in the order that we will
     *                extract our information
     * @param type
     *                one of {NAMESPACE | ID | VALUE} denoting what exactly it
     *                is that you would like returned
     * @param xml
     *                the MOBY xml containing the data to extract
     * @param endpoint
     *                the BioMOBY registry endpoint to use
     * @return a list of strings representing what it is you asked for
     */
    public static ArrayList<String> getContentForDataType(
	    ArrayList<String> names, int type, String xml, String endpoint) {
	if (!allowables.contains(type) || names == null || names.size() == 0
		|| xml == null || xml.trim().length() == 0) {
	    // nothing to return
	    logger.warn("Parser invoked on an empty message ...");
	    return new ArrayList<String>();
	}
	MobyContentInstance contents;
	try {
	    contents = MobyDataUtils.fromXMLDocument(new ByteArrayInputStream(
		    xml.getBytes("UTF8")), new Registry(endpoint, endpoint,
		    "http://domain.com/MOBY/Central"));
	} catch (Exception e) {
	    logger.error("There was a problem parsing the input XML:\n" + xml
		    + "\n", e);
	    return new ArrayList<String>();
	}
	if (contents.keySet().size() != 1) {
	    return new ArrayList<String>();
	}

	ArrayList<String> clone = new ArrayList<String>();
	clone.addAll(names);
	ArrayList<String> output = new ArrayList<String>();
	// should be exactly 1 job!
	Iterator<String> jobIDs = contents.keySet().iterator();
	while (jobIDs.hasNext()) {
	    MobyDataJob job = (MobyDataJob) contents.get(jobIDs.next());
	    // get the instance
	    MobyDataInstance data = job.get(clone.remove(0));
	    if (data == null)
		return output;
	    recurse(clone, data, output, type);
	}
	return output;
    }

    @SuppressWarnings("unchecked")
    private static void recurse(ArrayList<String> names, MobyDataInstance data,
	    ArrayList<String> output, int type) {
	// base case => we have finally found the element of interest
	if (names.isEmpty()) {
	    baseCase(data, output, type);
	    return;
	}
	if (data instanceof MobyDataObjectVector) {
	    // recurse on the children -- the recursion will extract by
	    // articlename
	    MobyDataObjectVector vector = (MobyDataObjectVector) data;
	    // recurse on the has relationship
	    for (Iterator i = vector.iterator(); i.hasNext();) {
		recurse((ArrayList) (names.clone()), (MobyDataInstance) i
			.next(), output, type);
	    }

	} else if (data instanceof MobyDataComposite) {
	    // recurse on the child given by name.get(0)
	    MobyDataInstance d = ((MobyDataComposite) data).remove(names
		    .remove(0));
	    recurse((ArrayList) (names.clone()), d, output, type);
	} else if (data instanceof MobyDataBoolean) {
	    baseCase(data, output, type);
	} else if (data instanceof MobyDataFloat) {
	    baseCase(data, output, type);
	} else if (data instanceof MobyDataInt) {
	    baseCase(data, output, type);
	} else if (data instanceof MobyDataString) {
	    baseCase(data, output, type);
	}

    }

    private static void baseCase(MobyDataInstance data,
	    ArrayList<String> output, int type) {
	if (data == null)
	    return;
	switch (type) {
	case NAMESPACE: {
	    if (data instanceof MobyDataObjectVector) {
		MobyDataObjectVector vector = (MobyDataObjectVector) data;
		for (Iterator i = vector.iterator(); i.hasNext();) {
		    MobyNamespace[] namespaces = ((MobyDataObject) i.next())
			    .getNamespaces();
		    for (int j = 0; j < namespaces.length; j++) {
			output.add(namespaces[j].getName());
		    }
		}
	    } else {
		MobyNamespace[] namespaces = ((MobyDataObject) data)
			.getNamespaces();
		for (int j = 0; j < namespaces.length; j++) {
		    output.add(namespaces[j].getName());
		}
	    }
	}
	    break;
	case ID: {
	    if (data instanceof MobyDataObjectVector) {
		MobyDataObjectVector vector = (MobyDataObjectVector) data;
		for (Iterator i = vector.iterator(); i.hasNext();) {
		    output.add(((MobyDataObject) i.next()).getId());
		}
	    } else {
		output.add(((MobyDataObject) data).getId());
	    }
	}
	    break;
	case VALUE: {
	    if (data instanceof MobyDataObjectVector) {
		MobyDataObjectVector vector = (MobyDataObjectVector) data;
		for (Iterator i = vector.iterator(); i.hasNext();) {
		    output.add(((MobyDataObject) i.next()).getValue());
		}
	    } else {
		output.add(((MobyDataObject) data).getValue());
	    }
	}
	    break;
	default:
	    break;
	}
    }
}
