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
