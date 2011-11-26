package com.voidlabs.foo.resource;

import java.lang.reflect.Method;
import com.voidlabs.foo.util.ClassUtils;

/**
 * Simple callback rendering results if methods are found
 * corresponding to the method passed in constructor
 */

public class MethodSearchCallback implements ClassloadCallback {
	
	private String methodName;
	
	public MethodSearchCallback(String methodName) {
		this.methodName = methodName;
	}
	
	public void handle(Class loadedClass, ClassData data) {
		Method[] methods = loadedClass.getDeclaredMethods();
		for (Method method : methods) {
			String footprint = ClassUtils.getMethodFootprint(method);
			if (footprint.contains(methodName)) {
				System.out.println("+ [" + loadedClass.getName() + "]  " + footprint);
			}
		}
	}
	
}