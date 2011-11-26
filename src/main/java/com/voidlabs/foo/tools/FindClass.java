package com.voidlabs.foo.tools;

import java.util.*;
import java.io.*;
import com.voidlabs.foo.resource.*;
import com.voidlabs.foo.util.FSUtil;

public class FindClass {

	/**
	 * find all instances of class described by given pattern
	 * in all libraries under given directory
	 */
	public static TreeSet<String> find(File libPath, String className) throws Exception {
		return find(new File[] {libPath}, className);
	}
	
	/**
	 * find all instances of class described by given pattern
	 * in all libraries under given set of directories
	 */
	public static TreeSet<String> find(File[] libPaths, String className) throws Exception {

		TreeSet<String> result = new TreeSet<String>();
		
		for (File libPath : libPaths) {

			File[] files = libPath.listFiles();
		
			for (File file : files) {
				if (file.getCanonicalPath().endsWith(".jar")) {
					JarReader reader = new JarReader(file);
					result.addAll(reader.find(className));
				}
			}
	
		}
		
		return result;
	}
	
	/**
      * find classpath matching given string
	  */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("usage : FieldDefinition [library path] [class name]");
			System.exit(1);
		}

		File[] libFD = FSUtil.getLibraryPaths(args[0]);
		
		TreeSet<String> results = FindClass.find(libFD,args[1]);

		if (results.size() == 0) {
			System.out.println("no classes found matching given name");
		} else {
			System.out.println("found total of " + results.size() + " matching classes : ");
		}
 		
		for (String result : results) {
			System.out.println("+ " + result);
		}
		
	}
	
}