package net.sf.taverna.t2.activities.biomoby.actions;
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
