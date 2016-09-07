/*
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
