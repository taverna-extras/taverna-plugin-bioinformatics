/**
 *
 */
package net.sf.taverna.t2.activities.biomoby.edits;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.sf.taverna.t2.activities.biomoby.BiomobyActivity;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Edits;

/**
 * @author Stuart Owen
 *
 */
public class AddBiomobyCollectionDataTypeEdit extends
		AddBiomobyDataTypeEdit {

	private final String theCollectionName;

	/**
	 * @param dataflow
	 * @param activity
	 * @param objectName
	 */
	public AddBiomobyCollectionDataTypeEdit(Dataflow dataflow,
			BiomobyActivity activity, String objectName, String theCollectionName, Edits edits) {
		super(dataflow, activity, objectName, edits);
		this.theCollectionName = theCollectionName;
	}

	@Override
	protected String determineInputPortName(String defaultName,String objectName) {
		return defaultName
		+ "(Collection - '"
		+ (theCollectionName.equals("") ? "MobyCollection"
				: theCollectionName)
		+ "')";
	}



}
