/*
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
