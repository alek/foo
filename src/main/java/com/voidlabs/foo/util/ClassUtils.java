package com.voidlabs.foo.util;

import java.lang.reflect.Method;

import java.util.List;
import java.util.LinkedList;

/**
 * Collection of random class-related utils
 */ 

public class ClassUtils {
	
	/**
	 * get list of method footprints for given class
	 */
	public static LinkedList<String> getMethodFootprints(Class classEntry) {
		Method[] methods = classEntry.getDeclaredMethods();
		LinkedList<String> result = new LinkedList<String>();
		for (Method method : methods) {
			result.add(getMethodFootprint(method));
		}
		return result;
	}
	
	/** 
	 * get print representation of method footprint
	 */
	public static String getMethodFootprint(Method method) {
		StringBuilder sb = new StringBuilder();

		Class returnType = method.getReturnType();
		Class[] paramTypes = method.getParameterTypes();
		
		sb.append(returnType.getName()).append(" ").append(method.getName()).append("(");
		
		String prefix = "";
		for (Class ptype : paramTypes) {
			sb.append(prefix).append(ptype.getName());
			prefix = ",";
		}
		sb.append(")");
		
		return sb.toString();
	}
	
	
}