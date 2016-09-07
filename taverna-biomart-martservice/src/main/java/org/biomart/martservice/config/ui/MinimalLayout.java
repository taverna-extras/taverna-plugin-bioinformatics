package org.biomart.martservice.config.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A layout manager that lays out components, either horizontally or vertically,
 * according to their minimum size.
 *
 * @author David Withers
 */
class MinimalLayout implements LayoutManager {
	public static final int HORIZONTAL = 0;

	public static final int VERTICAL = 1;

	private static final int gap = 5;

	private int type;

	public MinimalLayout() {
		type = HORIZONTAL;
	}

	public MinimalLayout(int type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component comp) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int x = insets.left;
		int y = insets.top;
		if (type == HORIZONTAL) {
			Component[] components = parent.getComponents();
			for (int i = 0; i < components.length; i++) {
				components[i].setLocation(x, y);
				components[i].setSize(getSize(components[i]));
				x = x + gap + components[i].getWidth();
			}
		} else {
			Component[] components = parent.getComponents();
			for (int i = 0; i < components.length; i++) {
				components[i].setLocation(x, y);
				components[i].setSize(getSize(components[i]));
				y = y + gap + components[i].getHeight();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
	 *      java.awt.Component)
	 */
	public void addLayoutComponent(String name, Component comp) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	public Dimension minimumLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		Dimension minimumSize = new Dimension(insets.left, insets.top);
		if (type == HORIZONTAL) {
			int x = insets.left;
			Component[] components = parent.getComponents();
			for (int i = 0; i < components.length; i++) {
				Dimension size = getSize(components[i]);
				if (insets.top + size.height > minimumSize.height) {
					minimumSize.height = insets.top + size.height;
				}
				minimumSize.width = x + size.width;
				x = x + size.width + gap;
			}
		} else {
			int y = insets.top;
			Component[] components = parent.getComponents();
			for (int i = 0; i < components.length; i++) {
				Dimension size = getSize(components[i]);
				if (insets.left + size.width > minimumSize.width) {
					minimumSize.width = insets.left + size.width;
				}
				minimumSize.height = y + size.height;
				y = y + size.height + gap;
			}
		}
		minimumSize.width = minimumSize.width + insets.right;
		minimumSize.height = minimumSize.height + insets.bottom;

		return (minimumSize);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(Container parent) {
		return minimumLayoutSize(parent);
	}

	private Dimension getSize(Component component) {
		return component.getPreferredSize();
	}

}
