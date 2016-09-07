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
