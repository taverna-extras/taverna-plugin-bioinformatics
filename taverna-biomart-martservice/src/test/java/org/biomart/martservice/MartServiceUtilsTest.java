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
 * Filename           $RCSfile: MartServiceUtilsTest.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2007/02/22 18:31:56 $
 *               by   $Author: davidwithers $
 * Created on 22 Feb 2007
 *****************************************************************/
package org.biomart.martservice;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author David Withers
 */
public class MartServiceUtilsTest {

	/**
	 * Test method for {@link org.biomart.martservice.MartServiceUtils#isProxyHost(java.lang.String)}.
	 */
	@Test
	public void testIsProxyHost() {
		System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|*.mydomain.com|192.168.1.*");
		assertFalse(MartServiceUtils.isProxyHost("http://localhost/"));
		assertFalse(MartServiceUtils.isProxyHost("http://localhost:8080/"));
		assertFalse(MartServiceUtils.isProxyHost("http://127.0.0.1/"));
		assertFalse(MartServiceUtils.isProxyHost("http://www.mydomain.com/"));
		assertFalse(MartServiceUtils.isProxyHost("http://www.sub.mydomain.com/"));
		assertFalse(MartServiceUtils.isProxyHost("http://192.168.1.1/"));
		assertFalse(MartServiceUtils.isProxyHost("http://192.168.1.2/"));
		assertTrue(MartServiceUtils.isProxyHost("http://www.mydomain.co.uk/"));
		assertTrue(MartServiceUtils.isProxyHost("http://192.168.2.1/"));
		assertTrue(MartServiceUtils.isProxyHost("http://127.0.0.2/"));
		assertTrue(MartServiceUtils.isProxyHost("http://nonlocalhost/"));
	}

}
