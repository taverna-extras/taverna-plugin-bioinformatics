/*
package org.biomart.martservice.config.ui;
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

import junit.framework.TestCase;

/**
 * 
 * @author witherd5
 */
public class MartServiceIconsTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test method for {@link org.biomart.martservice.config.ui.MartServiceIcons#getIcon(java.lang.String)}.
	 */
	public void testGetIcon() {
		assertNotNull(MartServiceIcons.getIcon("gene_schematic"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_3utr"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_5utr"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_cdna"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_coding_gene_flank"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_coding_transcript_flank"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_coding"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_gene_exon_intron"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_gene_exon"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_gene_flank"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_peptide"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_transcript_exon_intron"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_transcript_exon"));
		assertNotNull(MartServiceIcons.getIcon("gene_schematic_transcript_flank"));
		assertNotNull(MartServiceIcons.getIcon("expand"));
		assertNotNull(MartServiceIcons.getIcon("contract"));
		assertNull(MartServiceIcons.getIcon("something else"));
		assertNull(MartServiceIcons.getIcon(""));
		assertNull(MartServiceIcons.getIcon(null));
	}

}
