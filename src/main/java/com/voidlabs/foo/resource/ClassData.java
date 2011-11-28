package com.voidlabs.foo.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import com.voidlabs.foo.util.FSUtil;
import com.voidlabs.foo.util.ClassUtils;



/**
 * Simple representation of raw class data
 */

public class ClassData {
	
	String className;	// class name
	byte[] classData;	// binary bytecode content
	File jarFile;		// fd representing jar given class belongs to
	
	Class classEntry;	// resolved class entry 
	LinkedList<String> methods;	// method footprints
	
	public ClassData(String className, byte[] classData) {
		this.className = className;
		this.classData = classData;
	}
	
	public String getName() {
		return className;
	}
	
	public byte[] getData() {
		return classData;
	}
	
	/**
	 * return true of class info has been resolved from raw byte data
	 */
	public boolean isResolved() {
		return (classEntry != null) || (methods != null);
	}
	
	/**
	 * set class entry for given object
	 */
	public void setClass(Class classEntry) {
		this.classEntry = classEntry;
		methods = ClassUtils.getMethodFootprints(classEntry);
	}
	
	/**
	 * get class instance (if available)
	 */
	public Class getClassEntry() {
		return classEntry;
	}
	
	/** 
	 * set jar file given class belongs to
	 */
	public void setJarFile(File jarFile) {
		this.jarFile = jarFile;
	}
	
	/**
	 * get jar file given class belongs to
	 */
	public File getJarFile() {
		return jarFile;
	}
	
	/**
	 * get list of methods footprints for given class
	 */
	public LinkedList<String> getMethodFootprints() {
		return methods;
	}

	/**
	 *  set method footprints for given class
	 */
	protected void setMethodFootprints(LinkedList<String> methods) {
		this.methods = methods;
	}
	
	/**
	 * return true if methods have been resolved for given class
	 */
	public boolean hasResolvedMethods() {
		return methods != null;
	}

	/**
	 * get canonical data file path
	 */
	public static String getDataFile(File classFile) throws Exception {
		return getDataFile(classFile.getCanonicalPath());
	}

	/**
	 * get canonical data file path
	 */
	public static String getDataFile(String classFile) {
		return classFile + ".data";
	}

	/**
	 * get canonical description file path
	 */
	public static String getDescriptionFile(File classFile) throws Exception {
		return getDescriptionFile(classFile.getCanonicalPath());
	}

	/**
	 * get canonical description file path
	 */
	public static String getDescriptionFile(String classFile) {
		return classFile + ".desc";
	}

	/**
	 * serialize file contents to given file
	 */
	public void serialize(String outFile) throws Exception {
		
		// write class data
		String classDataFile = getDataFile(outFile);
		OutputStream out = new FileOutputStream(new File(classDataFile));
		out.write(classData);
		out.close();
		
		// write class resolution data
		
		String descriptionDataFile = getDescriptionFile(outFile);
		if (methods == null) {
			LinkedList<String> methods = ClassUtils.getMethodFootprints(classEntry);
		}
		FSUtil.writeListToFile(descriptionDataFile, methods);
		
	}
	
	/**
	 * deserialize class data entry from given file
	 */
	public static ClassData deserialize(File classFile) throws Exception {
		
		// deserialize class content
		
		FileInputStream is = new FileInputStream(classFile);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
				
	    byte[] buff = new byte[4096*8];
		int cnt;
		
		while ((cnt = is.read(buff)) != -1) {
			data.write(buff,0,cnt);
		}
		
		String fileName = classFile.getName();
		String className = fileName.substring(0, fileName.indexOf(".data"));

		ClassData cdata = new ClassData(className,data.toByteArray());

		// check if resolved class data exists | TODO : cleanup this

		String filePath = classFile.getCanonicalPath();
		String descFilePath = getDescriptionFile(filePath.substring(0, filePath.indexOf(".data")));

		if ((new File(descFilePath)).exists()) {
			cdata.setMethodFootprints(FSUtil.loadListFromFile(new File(descFilePath)));
		} 
		
		return cdata;
	}
	
}