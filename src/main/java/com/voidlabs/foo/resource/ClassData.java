package com.voidlabs.foo.resource;

/**
 * Simple representation of raw class data
 */

public class ClassData {
	
	String className;
	byte[] classData;
	
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
}