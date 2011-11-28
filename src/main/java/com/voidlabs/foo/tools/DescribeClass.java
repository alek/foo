package com.voidlabs.foo.tools;

import java.io.File;
import java.util.List;
import com.voidlabs.foo.util.FSUtil;
import com.voidlabs.foo.resource.ClassloadCallback;
import com.voidlabs.foo.resource.ClassData;
import com.voidlabs.foo.resource.JarLoader;
import java.lang.reflect.Method;
import com.voidlabs.foo.util.ClassUtils;

/**
 *	Get full description of given class
 */
public class DescribeClass {
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.out.println("usage: DescribeClass [library path] [class name]");
			System.exit(1);
		}
		
		final String queryClass = args[1];
		
		File[] libPaths = FSUtil.getLibraryPaths(args[0]);
		List<File> jarFiles = FSUtil.getFileList(libPaths,".jar");
		
		for (File file : jarFiles) {
			(new JarLoader()).load(file, new ClassloadCallback() {
				public void handle(ClassData data) {
					Class loadedClass = data.getClassEntry();
					if (loadedClass.getName().indexOf(queryClass) > -1) {
						System.out.println("+ " + loadedClass.getName());
						Method[] methods = loadedClass.getDeclaredMethods();
						for (Method method : methods) {
							System.out.println("\t" + ClassUtils.getMethodFootprint(method));
						}
					}
				}
			});
		}
		
	}
	
}
