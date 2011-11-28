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
	
	public void handle(ClassData data) {
		
		// if method footprints have been resolved from cache
		// TODO : cleanup & join as single loop
		if (data.hasResolvedMethods()) {
			for (String footprint : data.getMethodFootprints()) {
				if (footprint.contains(methodName)) {
					System.out.println("+ [" + data.getName() + "]  " + footprint);
				}
			}
			return;
		}
		
		Class loadedClass = data.getClassEntry();
		
		Method[] methods = loadedClass.getDeclaredMethods();
		for (Method method : methods) {
			String footprint = ClassUtils.getMethodFootprint(method);
			if (footprint.contains(methodName)) {
				System.out.println("+ [" + loadedClass.getName() + "]  " + footprint);
			}
		}
	}
	
}