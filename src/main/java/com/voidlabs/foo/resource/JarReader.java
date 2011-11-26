package com.voidlabs.foo.resource;

import java.io.*;
import java.util.jar.*;
import java.util.*;

/**
 * Simple Jar file content reader
 */

public class JarReader {
	
	private JarFile file;

	/**
	 * Init JarReader
	 */
	public JarReader(File path) throws Exception {
		file = new JarFile(path);
	}
	
	/**
	 * fina all entries in the jar matching given clasName
	 */
	public TreeSet<String> find(String className) throws Exception {

		TreeSet<String> result = new TreeSet<String>();
		
		Enumeration entries = file.entries();
		while (entries.hasMoreElements()) {

			Object elem = entries.nextElement();
			String name = elem.toString();

			if (name.contains(className)) {
				if (name.indexOf("$") > 0) {
					name = name.substring(0,name.indexOf("$"));
				} 
				if (name.indexOf(".") > 0) {
					name = name.substring(0,name.indexOf("."));
				}
				name = name.replaceAll("/",".");
				
				result.add(name);
			}
		}
		
		return result;
		
	}
	
}