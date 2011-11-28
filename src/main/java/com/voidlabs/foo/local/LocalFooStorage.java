package com.voidlabs.foo.local;

import java.io.File;

/**
 * Contains description of local foo storage data
 */

public class LocalFooStorage {
	
	private static String ROOT_DIR_PREFIX = ".foo";

	private File rootDir;
	private JarContentCache jarCache;

	/**
	 * create instance of local foo storage
	 */ 
	private LocalFooStorage() throws Exception {

		rootDir = new File(getCanonicalRootPath());
		
		if (!rootDir.exists()) {
			initRootStructure();
		}
		
		jarCache = JarContentCache.init(rootDir);
		
	}
	
	/**
	 * init local storage structure
	 */
	public void initRootStructure() throws Exception {
		
		rootDir.mkdir();
		
		// double-ckeck
		if (!rootDir.exists()) { throw new Exception(); }

	}
	
	/**
	 * get instance of local storage
	 */
	public static LocalFooStorage init() throws Exception {
		return new LocalFooStorage();
	}
	
	/** 
	 * get instance of local jar content cache
	 */
	public JarContentCache getJarCache() {
		return jarCache;
	}
	
	/** 
	 * get canonical path to storage root dir
	 */
	public String getCanonicalRootPath() {
		return System.getProperty("user.home") + "/" + ROOT_DIR_PREFIX + "/";
	}
	
}