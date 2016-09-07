/*
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Edward Kawas, Martin Senger, The BioMoby Project
 */
package net.sf.taverna.t2.activities.biomoby.query;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.biomoby.shared.MobyException;

/**
 * An implementation of {@link org.biomoby.shared.CentralAll}, allowing to
 * cache locally results of the cumulative methods so it does not need to access
 * Moby registry all the time. The other methods of the Central interface do not
 * use the results of the cached cumulative results (their implementation is
 * just passed to the parent class).
 * <p>
 *
 * The caching is done in the file system, not in memory, so the results are
 * permanent (until someone removes the caching directory, or calls
 * {@link #removeFromCache}).
 * <p>
 *
 * This class can be used also without caching - just instantiate it with
 * 'cacheDir' set to null in the constructor.
 * <p>
 *
 * @author <A HREF="mailto:martin.senger@gmail.com">Martin Senger</A>
 * @version $Id: CacheImpl.java,v 1.2 2008/09/04 13:42:16 sowen70 Exp $
 */

public class CacheImpl {

	private static Logger log = Logger.getLogger(CacheImpl.class);

	// filename for a list of cached entities
	protected static final String LIST_FILE = "__L__S__I__D__S__";

	/** An ID used in {@link #removeFromCache} indicating data types part. */
	public static final String CACHE_PART_DATATYPES = "c1";

	/** An ID used in {@link #removeFromCache} indicating services part. */
	public static final String CACHE_PART_SERVICES = "c2";

	public static final String CACHE_PROP_NAME = "cache-name";

	public static final String CACHE_PROP_COUNT = "cache-count";

	public static final String CACHE_PROP_OLDEST = "cache-oldest";

	public static final String CACHE_PROP_YOUNGEST = "cache-youngest";

	public static final String CACHE_PROP_SIZE = "cache-size";

	public static final String CACHE_PROP_LOCATION = "cache-loc";

	// DEFAULT registry
	private String MOBYCENTRAL_REGISTRY_URL = "http://moby.ucalgary.ca/moby/MOBY-Central.pl";

	// Default Endpoint
	private String MOBYCENTRAL_REGISTRY_URI = "http://moby.ucalgary.ca/MOBY/Central";

	// cache location
	private String cacheDir; // as defined in the constructor

	protected File dataTypesCache;

	protected File servicesCache;

	public static final String SERVICE_INSTANCE_FILENAME = "SERVICE_INSTANCE.rdf";

	private String serviceInstanceRDFLocation = SERVICE_INSTANCE_FILENAME;

	public static final String DATATYPE_FILENAME = "DATATYPES.rdf";

	private String datatypeRDFLocation = DATATYPE_FILENAME;

	// for optimalization
	private String fileSeparator;

	/***************************************************************************
	 * Create an instance that will access a default Moby registry and will
	 * cache results in the 'cacheDir' directory.
	 * <p>
	 **************************************************************************/
	public CacheImpl(String cacheDir) throws MobyException {
		this(null, null, cacheDir);
	}

	/***************************************************************************
	 * Create an instance that will access a Moby registry defined by its
	 * 'endpoint' and 'namespace', and will cache results in the 'cacheDir'
	 * directory. Note that the same 'cacheDir' can be safely used for more Moby
	 * registries.
	 * <p>
	 **************************************************************************/
	public CacheImpl(String endpoint, String namespace, String cacheDir)
			throws MobyException {
		if (endpoint != null && !endpoint.trim().equals(""))
			this.MOBYCENTRAL_REGISTRY_URL = endpoint;
		if (namespace != null && !namespace.trim().equals(""))
			this.MOBYCENTRAL_REGISTRY_URI = namespace;
		fileSeparator = System.getProperty("file.separator");
		this.cacheDir = cacheDir;
		initCache();
	}

	// it makes all necessary directories for cache given in the
	// constructor (which is now in global 'cacheDir'); it is
	// separated here because it can be called either from the
	// constructor, or everytime a cache is going to be used but it is
	// not there (somebody removed it)
	protected void initCache() throws MobyException {
		if (cacheDir != null) {
			File cache = createCacheDir(cacheDir, MOBYCENTRAL_REGISTRY_URL);
			dataTypesCache = createSubCacheDir(cache, "datatype_rdf");
			servicesCache = createSubCacheDir(cache, "service_rdf");
			serviceInstanceRDFLocation = servicesCache.getPath()
					+ fileSeparator + SERVICE_INSTANCE_FILENAME;
			datatypeRDFLocation = dataTypesCache.getPath() + fileSeparator
					+ DATATYPE_FILENAME;
		}
	}

	/***************************************************************************
	 * Return a directory name representing the current cache. This is the same
	 * name as given in constructors.
	 * <p>
	 *
	 * @return current cache directory name
	 **************************************************************************/
	public String getCacheDir() {
		return cacheDir;
	}

	/***************************************************************************
	 * Removes object groups from the cache. If 'id' is null it removes the
	 * whole cache (for that Moby registry this instance was initiated for).
	 * Otherwise 'id' indicates which part of the cache that will be removed.
	 * <p>
	 *
	 * @param id
	 *            should be either null, or one of the following:
	 *            {@link #CACHE_PART_DATATYPES}, {@link #CACHE_PART_SERVICES},
	 *            {@link #CACHE_PART_SERVICETYPES}, and {@link
	 *            #CACHE_PART_NAMESPACES}.
	 **************************************************************************/
	public void removeFromCache(String id) {
		try {
			if (cacheDir != null) {
				String[] parts = null;
				if (id == null)
					parts = new String[] { "datatype_rdf", "service_rdf" };
				else if (id.equals(CACHE_PART_SERVICES))
					parts = new String[] { "service_rdf" };
				else if (id.equals(CACHE_PART_DATATYPES))
					parts = new String[] { "datatype_rdf" };
				if (parts != null) {
					removeCacheDir(cacheDir, MOBYCENTRAL_REGISTRY_URL, parts);
				}
			}
		} catch (MobyException e) {
			log.error("Removing cache failed: " + e.getMessage());
		}
	}

	/**
	 * Create a cache directory from 'cacheDirectory' and 'registryId' if it
	 * does not exist yet. Make sure that it is writable. Return a File
	 * representing created directory.
	 *
	 * 'registryId' (which may be null) denotes what registry this cache is
	 * going to be created for. If null, an endpoint of a default Moby registry
	 * is used.
	 */
	protected File createCacheDir(String cacheDirectory, String registryId)
			throws MobyException {
		if (registryId == null || registryId.equals(""))
			registryId = MOBYCENTRAL_REGISTRY_URL;
		File cache = new File(cacheDirectory + fileSeparator
				+ clean(registryId));
		try {
			if (!cache.exists())
				if (!cache.mkdirs())
					throw new MobyException("Cannot create '"
							+ cache.getAbsolutePath() + "'.");
			if (!cache.isDirectory())
				throw new MobyException("Cache location '"
						+ cache.getAbsolutePath()
						+ "' exists but it is not a directory.");
			if (!cache.canWrite())
				throw new MobyException("Cache location '"
						+ cache.getAbsolutePath() + "' is not writable for me.");
			return cache;
		} catch (SecurityException e) {
			throw new MobyException("Cannot handle cache location '"
					+ cache.getAbsolutePath() + "'. " + e.toString());
		}
	}

	/**
	 * Remove cache and all (but given in 'subCacheDirNames') its
	 * subdirectories.
	 */
	protected void removeCacheDir(String cacheDirectory, String registryId,
			String[] subCacheDirNames) throws MobyException {
		if (registryId == null || registryId.equals(""))
			registryId = MOBYCENTRAL_REGISTRY_URL;
		File cache = new File(cacheDirectory + fileSeparator
				+ clean(registryId));
		try {
			if (!cache.exists())
				return;
			if (!cache.isDirectory())
				throw new MobyException("Cache location '"
						+ cache.getAbsolutePath()
						+ "' exists but it is not a directory.");
			if (!cache.canWrite())
				throw new MobyException("Cache location '"
						+ cache.getAbsolutePath() + "' is not writable for me.");
			for (int i = 0; i < subCacheDirNames.length; i++) {
				File cacheSubDir = new File(cache.getAbsolutePath()
						+ fileSeparator + clean(subCacheDirNames[i]));
				File[] files = cacheSubDir.listFiles();
				for (int f = 0; f < files.length; f++) {
					if (files[f].isDirectory())
						throw new MobyException("Found a directory '"
								+ files[f].getAbsolutePath()
								+ "' where no directory should be");
					if (!files[f].delete())
						log.error("Can't delete file '" + files[f] + "'.");
				}
				cacheSubDir.delete();
			}
			cache.delete();

		} catch (SecurityException e) {
			throw new MobyException("Cannot handle cache location '"
					+ cache.getAbsolutePath() + "'. " + e.toString());
		}
	}

	//
	protected File createSubCacheDir(File mainCache, String subCacheDirName)
			throws MobyException {
		File cache = new File(mainCache.getAbsolutePath() + fileSeparator
				+ clean(subCacheDirName));
		try {
			if (!cache.exists())
				if (!cache.mkdirs())
					throw new MobyException("Cannot create '"
							+ cache.getAbsolutePath() + "'.");
			return cache;
		} catch (SecurityException e) {
			throw new MobyException("Cannot handle cache location '"
					+ cache.getAbsolutePath() + "'. " + e.toString());
		}
	}

	/***************************************************************************
	 * Replace non digit/letter characters in 'toBeCleaned' by their numeric
	 * value. If there are more such numeric values side by side, put a dot
	 * between them. Return the cleaned string.
	 **************************************************************************/
	protected static String clean(String toBeCleaned) {

		char[] chars = toBeCleaned.toCharArray();
		int len = chars.length;
		int i = -1;
		while (++i < len) {
			char c = chars[i];
			if (!Character.isLetterOrDigit(c) && c != '_')
				break;
		}
		if (i < len) {
			StringBuffer buf = new StringBuffer(len * 2);
			for (int j = 0; j < i; j++) {
				buf.append(chars[j]);
			}
			boolean lastOneWasDigitalized = false;
			while (i < len) {
				char c = chars[i];
				if (Character.isLetterOrDigit(c) || c == '_') {
					buf.append(c);
					lastOneWasDigitalized = false;
				} else {
					if (lastOneWasDigitalized)
						buf.append('.');
					buf.append((int) c);
					lastOneWasDigitalized = true;
				}
				i++;
			}
			return new String(buf);
		}
		return toBeCleaned;
	}

	// create a file and put into it data to be cached
	protected static void store(File cache, String name, String data)
			throws MobyException {
		// File outputFile = new File (cache.getAbsolutePath() + fileSeparator +
		// clean (name));
		File outputFile = new File(cache.getAbsolutePath()
				+ System.getProperty("file.separator") + name);
		try {
			PrintWriter fileout = new PrintWriter(new BufferedOutputStream(
					new FileOutputStream(outputFile)));
			fileout.write(data);
			fileout.close();
		} catch (IOException e) {
			throw new MobyException("Cannot write to '"
					+ outputFile.getAbsolutePath() + ". " + e.toString());
		}
	}

	// create a file from a url
	protected void storeURL(File cache, String name, String url)
			throws MobyException {
		File outputFile = new File(cache.getAbsolutePath() + fileSeparator
				+ name);
		try {
			StringBuffer sb = new StringBuffer();
			String newline = System.getProperty("line.separator");
			HttpURLConnection urlConnection = null;

			URL rdf_url = new URL(url);
			urlConnection = (HttpURLConnection) rdf_url.openConnection();
			urlConnection.setDefaultUseCaches(false);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty ( "User-agent", "Taverna_BioMOBY/1.5");
			urlConnection.setConnectTimeout(1000*60*5);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				sb.append(inputLine + newline);
			in.close();

			PrintWriter fileout = new PrintWriter(new BufferedOutputStream(
					new FileOutputStream(outputFile)));
			fileout.write(sb.toString());
			fileout.close();
		} catch (ConnectException e) {
			throw new MobyException("Cannot read from url '"
					+ url + "'. " + e.toString());
		} catch (IOException e) {
			throw new MobyException("Cannot write to '"
					+ outputFile.getAbsolutePath() + "'. " + e.toString());
		} catch (Exception e) {
			throw new MobyException("Unexpected Exception caught: " + e.toString());
		}
	}

	// remove a file from a cache
	protected void remove(File cache, String name) {
		File file = new File(cache, name);
		// do not throw here an exception because a missing file
		// can be a legitimate status (e.g. for LIST_FILE when we
		// are updating)
		file.delete();
	}

	/***************************************************************************
	 * Read a cached file
	 **************************************************************************/
	protected static String load(File file) throws MobyException {
		try {
			StringBuffer buf = new StringBuffer();
			BufferedReader in = new BufferedReader(new FileReader(file));
			char[] buffer = new char[1024];
			int charsRead;

			while ((charsRead = in.read(buffer, 0, 1024)) != -1) {
				buf.append(buffer, 0, charsRead);
			}

			return new String(buf);

		} catch (Throwable e) { // be prepare for "out-of-memory" error
			throw new MobyException("Serious error when reading from cache. "
					+ e.toString());

		}
	}

	/***************************************************************************
	 * Is the given cache empty (meaning: cache directory does not exist, is
	 * empty, or contains only files to be ignored)?
	 **************************************************************************/
	protected boolean isCacheEmpty(File cache) throws MobyException {
		if (cache == null)
			return true;
		String[] list = cache.list();
		if (list == null || list.length == 0)
			return true;
		for (int i = 0; i < list.length; i++) {
			if (!ignoredForEmptiness(new File(list[i])))
				return false;
		}
		return true;
	}

	/***************************************************************************
	 * A LIST_FILE is a TOC of a cache object (each cache part has its own
	 * LIST_FILE). Read it and return it. If it does not exist, return null.
	 **************************************************************************/
	protected static String getListFile(File cache) throws MobyException {
		File listFile = new File(cache, LIST_FILE);
		if (!listFile.exists())
			return null;
		return load(listFile);
	}

	/***************************************************************************
	 * A LIST_FILE is a TOC of a cache object (each cache part has its own
	 * LIST_FILE). Read it and return it. If it does not exist, return null.
	 **************************************************************************/
	protected static void storeListFile(File cache, String data)
			throws MobyException {
		CacheImpl.store(cache, LIST_FILE, data);
	}

	/***************************************************************************
	 * Return a comparator for Files that compares in case-insensitive way.
	 **************************************************************************/
	protected static Comparator getFileComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				return o1.toString().compareToIgnoreCase(o2.toString());
			}
		};
	}

	/***************************************************************************
	 * Some file (when being read from a cache directory) are ignored.
	 **************************************************************************/
	protected static boolean ignored(File file) {
		String path = file.getPath();
		return path.endsWith("~") || path.endsWith(LIST_FILE);
	}

	/***************************************************************************
	 * Some file (when a cache is being tested fir emptyness) are ignored.
	 **************************************************************************/
	protected static boolean ignoredForEmptiness(File file) {
		String path = file.getPath();
		return path.endsWith("~");
	}

	/***************************************************************************
	 *
	 **************************************************************************/
	protected static String MSG_CACHE_NOT_DIR(File cache) {
		return "Surprisingly, '" + cache.getAbsolutePath()
				+ "' is not a directory. Strange...";
	}

	/***************************************************************************
	 *
	 **************************************************************************/
	protected static String MSG_CACHE_BAD_FILE(File file, Exception e) {
		return "Ignoring '" + file.getPath()
				+ "'. It should not be in the cache directory:"
				+ e.getMessage();
	}

	/***************************************************************************
	 * Return age of the current (whole) cache in millis from the beginning of
	 * the Epoch; or -1 if cache is empty, or the age is unknown.
	 * <p>
	 *
	 * @return the cache age which is taken as the oldest (but filled) cache
	 *         part (part is considered e.g. 'services', or 'data types', not
	 *         their individual entities)
	 **************************************************************************/
	public long getCacheAge() {
		try {
			long dataTypesCacheAge = (isCacheEmpty(dataTypesCache) ? Long.MAX_VALUE
					: dataTypesCache.lastModified());
			long servicesCacheAge = (isCacheEmpty(servicesCache) ? Long.MAX_VALUE
					: servicesCache.lastModified());
			long age = Math.min(dataTypesCacheAge, servicesCacheAge);
			return (age == Long.MAX_VALUE ? -1 : age);
		} catch (MobyException e) {
			return -1;
		}
	}

	/**
	 * @return the serviceInstanceRDFLocation
	 */
	public String getServiceInstanceRDFLocation() {
		return serviceInstanceRDFLocation;
	}

	/**
	 * @param serviceInstanceRDFLocation
	 *            the serviceInstanceRDFLocation to set
	 */
	public void setServiceInstanceRDFLocation(String serviceInstanceRDFLocation) {
		this.serviceInstanceRDFLocation = serviceInstanceRDFLocation;
	}

	/**
	 * @return the mOBYCENTRAL_REGISTRY_URI
	 */
	public String getMOBYCENTRAL_REGISTRY_URI() {
		return MOBYCENTRAL_REGISTRY_URI;
	}

	/**
	 * @param mobycentral_registry_uri
	 *            the mOBYCENTRAL_REGISTRY_URI to set
	 */
	public void setMOBYCENTRAL_REGISTRY_URI(String mobycentral_registry_uri) {
		MOBYCENTRAL_REGISTRY_URI = mobycentral_registry_uri;
	}

	/**
	 * @return the MOBYCENTRAL_REGISTRY_URL
	 */
	public String getMOBYCENTRAL_REGISTRY_URL() {
		return MOBYCENTRAL_REGISTRY_URL;
	}

	/**
	 * @param mobycentral_registry_url
	 *            the MOBYCENTRAL_REGISTRY_URL to set
	 */
	public void setMOBYCENTRAL_REGISTRY_URL(String mobycentral_registry_url) {
		MOBYCENTRAL_REGISTRY_URL = mobycentral_registry_url;
	}

	/**
	 * @return the datatypeRDFLocation
	 */
	public String getDatatypeRDFLocation() {
		return datatypeRDFLocation;
	}

	/**
	 * @param datatypeRDFLocation the datatypeRDFLocation to set
	 */
	public void setDatatypeRDFLocation(String datatypeRDFLocation) {
		this.datatypeRDFLocation = datatypeRDFLocation;
	}

}
