/*
 * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 ****************************************************************
 * Source code information
 * -----------------------
 * Filename           $RCSfile: MartServiceIconsTest.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/01/31 14:12:16 $
 *               by   $Author: davidwithers $
 * Created on 24-Aug-2006
 *****************************************************************/
package org.biomart.martservice.config.ui;

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
