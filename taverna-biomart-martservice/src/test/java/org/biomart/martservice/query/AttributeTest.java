/*
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
