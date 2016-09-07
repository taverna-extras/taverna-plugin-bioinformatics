package net.sf.taverna.t2.activities.biomart.servicedescriptions;
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

import java.net.URI;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;

/**
 *
 * @author Alex Nenadic
 * @author Alan R Williams
 *
 */
public class BiomartActivityIcon implements ActivityIconSPI {

	private static final URI BIOMART_ACTIVITY = URI.create("http://ns.taverna.org.uk/2010/activity/biomart");

	static Icon icon = null;

	public int canProvideIconScore(URI activityType) {
		if (BIOMART_ACTIVITY.equals(activityType))
			return DEFAULT_ICON + 1;
		else
			return NO_ICON;
	}

	public Icon getIcon(URI activityType) {
		return getBiomartIcon();
	}

	public static Icon getBiomartIcon() {
		if (icon == null) {
			icon = new ImageIcon(BiomartActivityIcon.class.getResource("/biomart.png"));
		}
		return icon;
	}
}
