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
