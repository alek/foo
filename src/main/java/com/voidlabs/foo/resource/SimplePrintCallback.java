package com.voidlabs.foo.resource;

import java.util.List;
import java.util.LinkedList;
import java.util.TreeMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.voidlabs.foo.util.ClassUtils;

/**
 * Simple callback printing basic info about loaded class 
 */
public class SimplePrintCallback implements ClassloadCallback {
	
	/**
	 * print basic information about class being loaded
	 */ 
	public void handle(Class loadedClass, ClassData data) {
		
		System.out.println("+ " + loadedClass.getCanonicalName());

		Class[] interfaces = loadedClass.getInterfaces();
		
		if (interfaces.length > 0) {
			StringBuilder sb = new StringBuilder();
			String prefix = "";
			for (Class iff : interfaces) {
				sb.append(prefix).append(getInterface(iff.getName()));
				prefix = ",";
			}
			System.out.println(" \\- implements [" + sb + "]");
		}
		
		Method[] methods = loadedClass.getDeclaredMethods();
		TreeMap<String, List> uniqMethods = new TreeMap<String, List>();
		
		for (Method method : methods) {

			String footprint = ClassUtils.getMethodFootprint(method);

			List fprints;

			if (uniqMethods.containsKey(method.getName())) {
			 	fprints = uniqMethods.get(method.getName());
			} else {
				fprints = new LinkedList();
			}
			
			fprints.add(footprint);
			uniqMethods.put(method.getName(), fprints);
		}
		
		for (String methodName : uniqMethods.keySet()) {
			System.out.println("  - " + methodName);
			List impls = uniqMethods.get(methodName);
			for (Object impl : impls) {
				System.out.println("\t" + impl);
			}
 		}
		
	}
	
	/**
	 * get interface print representation
	 */
	public String getInterface(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}
	
	/**
	 * get method parameter print representation
	 */
	public String getMethodParam(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}
	
	
}