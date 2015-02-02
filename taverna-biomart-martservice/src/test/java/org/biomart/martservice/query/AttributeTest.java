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
 * Filename           $RCSfile: AttributeTest.java,v $
 * Revision           $Revision: 1.2 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008/03/04 16:43:40 $
 *               by   $Author: davidwithers $
 * Created on 02-May-2006
 *****************************************************************/
package org.biomart.martservice.query;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 
 * @author David Withers
 */
public class AttributeTest {
	private String attributeName;

	private String attributes;

	private Dataset dataset;

	private Attribute attribute;

	@Before
	public void setUp() throws Exception {
		attributeName = "attribute name";
		attributes = "attributes";
		dataset = new Dataset("dataset name");
		attribute = new Attribute(attributeName);
	}

	@Test
	public void AttributeString() {
		Attribute attribute = new Attribute(attributeName);
		assertEquals("Name should be '" + attributeName + "'", attribute
				.getName(), attributeName);
	}

	@Test
	public void AttributeAttribute() {
		attribute.setContainingDataset(dataset);
		Attribute copy = new Attribute(attribute);
		assertEquals(attribute.getName(), copy.getName());
		assertNull(copy.getContainingDataset());
	}

	@Test
	public final void getName() {
		assertEquals("Name should be '" + attributeName + "'", attribute
				.getName(), attributeName);
	}

	@Test (expected=IllegalArgumentException.class)
	public final void setName() {
		String newName = "new attribute name";
		attribute.setName(newName);
		assertEquals("Name should be '" + newName + "'", attribute.getName(),
				newName);
		attribute.setName(null);
	}

	@Test
	public final void getQualifiedName() {
		assertEquals("Qualified name should be '" + attributeName + "'",
				attribute.getQualifiedName(), attributeName);

		String qualifiedName = dataset.getName() + "." + attributeName;
		attribute.setContainingDataset(dataset);
		assertEquals("Qualified name should be '" + qualifiedName + "'",
				attribute.getQualifiedName(), qualifiedName);

		dataset.setName("new dataset name");
		qualifiedName = dataset.getName() + "." + attributeName;
		attribute.setContainingDataset(dataset);
		assertEquals("Qualified name should be '" + qualifiedName + "'",
				attribute.getQualifiedName(), qualifiedName);

		attribute.setContainingDataset(null);
		assertEquals("Qualified name should be '" + attributeName + "'",
				attribute.getQualifiedName(), attributeName);
	}

	@Test
	public void getAttributes() {
		assertNull(attribute.getAttributes());
		attribute.setAttributes(attributes);
		assertEquals("Attributes should be '" + attributes + "'", attribute
				.getAttributes(), attributes);
	}

	@Test
	public void setAttributes() {
		String newAttributes = "new attributes";
		attribute.setAttributes(newAttributes);
		assertEquals("Attributes should be '" + newAttributes + "'", attribute.getAttributes(),
				newAttributes);
		attribute.setAttributes(null);
		assertNull(attribute.getAttributes());
	}
	
	@Test
	public void testGetAttributesCount() {
		assertEquals(attribute.getAttributesCount(), 0);
		attribute.setAttributes("a");
		assertEquals(attribute.getAttributesCount(), 1);
		attribute.setAttributes("a,b,c");
		assertEquals(attribute.getAttributesCount(), 3);
		attribute.setAttributes(null);
		assertEquals(attribute.getAttributesCount(), 0);
	}

	@Test
	public void getContainingDataset() {
		assertNull(attribute.getContainingDataset());
		dataset.addAttribute(attribute);
		assertEquals(attribute.getContainingDataset(), dataset);
	}

	@Test
	public void setContainingDataset() {
		attribute.setContainingDataset(dataset);
		assertEquals(attribute.getContainingDataset(), dataset);
		attribute.setContainingDataset(null);
		assertNull(attribute.getContainingDataset());
	}

	@Test
	public void hashCodeTest() {
		Attribute attribute2 = new Attribute(attributeName);
		assertEquals(attribute.hashCode(), attribute.hashCode());
		assertEquals(attribute.hashCode(), attribute2.hashCode());
	}

}
