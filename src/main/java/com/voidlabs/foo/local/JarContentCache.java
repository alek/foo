package com.voidlabs.foo.local;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.io.FileFilter;

import com.voidlabs.foo.resource.ClassData;

/**
 * simple filesystem-based cache of jar entries
 */

public class JarContentCache {
	
	private static String CACHE_DIR_PREFIX = "cache";
	
	File cacheDir;
	
	private JarContentCache(File rootDir) throws Exception {
		
		cacheDir = new File(getCanonicalPath(rootDir));
		
		if (!cacheDir.exists()) {
			initCache();
		}
		
	}
	
	/**
	 * initialize cache directory entries
	 */
	private void initCache() throws Exception {
		cacheDir.mkdir();
		
		//doublecheck
		if (!cacheDir.exists()) { throw new Exception(); }
	}
	
	/** 
	 * return initalized instance of jar cache 
	 * with storage relative to given root dir
	 */ 
	public static JarContentCache init(File rootDir) throws Exception {
		return new JarContentCache(rootDir);
	}
	
	/**
	 * get filesystem path to cache root dir
	 */
	public String getCanonicalPath(File rootDir) throws Exception {
		return rootDir.getCanonicalPath() + "/" + CACHE_DIR_PREFIX;
	}
	
	/**
	 * get canonical cache path for given jar
	 */
	public String getJarCachePath(File jarFile) throws Exception {
		return cacheDir.getCanonicalPath() + "/" + jarFile.getName();
	}
	
	/**
	 * get canonical cache path for given class in defined jar
	 */
	public String getJarClassCachePath(File jarFile, ClassData classEntry) throws Exception {
		return getJarCachePath(jarFile) + "/" + classEntry.getName();
	}

	/**
	 * return true if cached entry exists for given file
	 */
	public boolean contains(File jarFile) throws Exception {
		return (new File(getJarCachePath(jarFile))).exists();
	}
	
	/**
	 * add cache entry
	 */
	public void add(ClassData classEntry) throws Exception {
		add(classEntry.getJarFile(), classEntry);
	}
	
	/**
	 * add cache entry
	 */
	public void add(File jarFile, ClassData classEntry) throws Exception {
		if (!(new File(getJarCachePath(jarFile))).exists()) {
			(new File(getJarCachePath(jarFile))).mkdir();
		}
		classEntry.serialize(getJarClassCachePath(jarFile, classEntry));
	}
	
	/**
	 * get all class entries corresponding to given jar
	 */
	public List<ClassData> getEntries(File jarFile) throws Exception {
		File[] classFiles = (new File(getJarCachePath(jarFile))).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".data");
			}
		});
		LinkedList<ClassData> result = new LinkedList<ClassData>();
		for (File file : classFiles) {
			result.add(ClassData.deserialize(file));
		}
		return result;
	}
	
}