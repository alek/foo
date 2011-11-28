package com.voidlabs.foo.util;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;


/**
 * Collection of random filesystem-related utilities
 */

public class FSUtil {
	
	/**
	 * initialize file descriptor array corresponding 
	 * to given library path string
	 * 
	 * @libPathString - colon-separated library path string
	 * @throws Exception if any of entries is not a valid file
	 */
	public static File[] getLibraryPaths(String libPathString) throws Exception {
		
		String[] libPaths = libPathString.split(":");
		File[] libFD = new File[libPaths.length];

		for (int i=0; i<libPaths.length; i++) {
			libFD[i] = new File(libPaths[i]);
			if (!libFD[i].isDirectory()) {
				throw new Exception();
			}
		}
		
		return libFD;
		
	}
	
	
	/**
	 * get list of all files under defined set of rootDirs 
	 * matching specified extension
	 * 
	 * @rootDirs - set of root dirs
	 * @extension - file extension to be matched
	 */
	public static List<File> getFileList(File[] rootDirs, String extension) throws Exception {
		LinkedList<File> result = new LinkedList<File>();
		for (File dir : rootDirs) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.getCanonicalPath().endsWith(extension)) {
					result.add(file);
				}
			}
		}
		return result;
	}
	
	/**
	 * write list contents to file
	 */
	public static void writeListToFile(String filePath, List<? extends Serializable> entries) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		for (Serializable entry : entries) {
			out.write(entry.toString() + "\n");
		}
		out.close();
	}
	
	
	/** 
	 * load list contents from file
	 */
	public static LinkedList<String> loadListFromFile(File filePath) throws Exception {
		LinkedList<String> result = new LinkedList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String entry = null;
		while ((entry = reader.readLine()) != null) {
			result.add(entry);
		}
		reader.close();
		return result;
	}
	
	
	
}