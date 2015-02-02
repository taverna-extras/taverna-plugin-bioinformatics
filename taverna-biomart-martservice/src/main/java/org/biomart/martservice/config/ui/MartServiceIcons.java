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
 * Filename           $RCSfile: MartServiceIcons.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/12/13 11:38:56 $
 *               by   $Author: davidwithers $
 * Created on 24-Aug-2006
 *****************************************************************/
package org.biomart.martservice.config.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 * @author witherd5
 */
public class MartServiceIcons {
	private static Map<String, Icon> nameToIcon = new HashMap<String, Icon>();

	private static final String imageLocation = "org/biomart/martservice/config/ui/";

	static {
		ClassLoader loader = MartServiceQueryConfigUIFactory.class
				.getClassLoader();
		if (loader == null) {
			loader = Thread.currentThread().getContextClassLoader();
		}

		nameToIcon.put("gene_schematic", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic.gif")));
		nameToIcon.put("gene_schematic_3utr", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_3utr.gif")));
		nameToIcon.put("gene_schematic_5utr", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_5utr.gif")));
		nameToIcon.put("gene_schematic_cdna", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_cdna.gif")));
		nameToIcon.put("gene_schematic_coding_gene_flank", new ImageIcon(loader
				.getResource(imageLocation
						+ "gene_schematic_coding_gene_flank.gif")));
		nameToIcon.put("gene_schematic_coding_transcript_flank", new ImageIcon(
				loader.getResource(imageLocation
						+ "gene_schematic_coding_transcript_flank.gif")));
		nameToIcon.put("gene_schematic_coding", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_coding.gif")));
		nameToIcon.put("gene_schematic_gene_exon_intron", new ImageIcon(loader
				.getResource(imageLocation
						+ "gene_schematic_gene_exon_intron.gif")));
		nameToIcon.put("gene_schematic_gene_exon", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_gene_exon.gif")));
		nameToIcon.put("gene_schematic_gene_flank", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_gene_flank.gif")));
		nameToIcon.put("gene_schematic_peptide", new ImageIcon(loader
				.getResource(imageLocation + "gene_schematic_peptide.gif")));
		nameToIcon.put("gene_schematic_transcript_exon_intron", new ImageIcon(
				loader.getResource(imageLocation
						+ "gene_schematic_transcript_exon_intron.gif")));
		nameToIcon.put("gene_schematic_transcript_exon", new ImageIcon(
				loader.getResource(imageLocation
						+ "gene_schematic_transcript_exon.gif")));
		nameToIcon.put("gene_schematic_transcript_flank", new ImageIcon(loader
				.getResource(imageLocation
						+ "gene_schematic_transcript_flank.gif")));
		nameToIcon.put("contract", new ImageIcon(loader
				.getResource(imageLocation
						+ "contract.gif")));
		nameToIcon.put("expand", new ImageIcon(loader
				.getResource(imageLocation
						+ "expand.gif")));
	}

	public static Icon getIcon(String name) {
		return (Icon) nameToIcon.get(name);
	}

}
