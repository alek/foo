package com.voidlabs.foo.resource;

/**
 * simple classload callback interface
 */

public interface ClassloadCallback {
	
	/**
	 * Handle loading of class 
	 *
	 * @loadedClass - class loaded using appropriate classloader
	 * @data - original class bytecode and related metadata
	 */
	public void handle(Class loadedClass, ClassData data);
	
}