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
 * Filename           $RCSfile: MinimalLayout.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/06/27 12:49:48 $
 *               by   $Author: davidwithers $
 * Created on 26 Jun 2007
 *****************************************************************/
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
