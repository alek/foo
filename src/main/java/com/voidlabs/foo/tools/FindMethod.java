package com.voidlabs.foo.tools;

import java.io.File;
import java.util.List;
import com.voidlabs.foo.util.FSUtil;
import com.voidlabs.foo.resource.ClassloadCallback;
import com.voidlabs.foo.resource.MethodSearchCallback;
import com.voidlabs.foo.resource.ClassData;
import com.voidlabs.foo.resource.JarLoader;
import com.voidlabs.foo.conf.FooConf;
import com.voidlabs.foo.util.SimpleMemoryMonitor;

/**
 * Simple method-search class
 * TODO : merge this with FindClass tool
 */

public class FindMethod {
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.out.println("usage: FindMethod [library path] [method name]");
			System.exit(1);
		}
		
		if (FooConf.DEBUG_ENABLED) {
			(new SimpleMemoryMonitor()).start();	// start memory tracking
		}
		
		File[] libPaths = FSUtil.getLibraryPaths(args[0]);
		List<File> jarFiles = FSUtil.getFileList(libPaths,".jar");
		
		for (File file : jarFiles) {
			(new JarLoader()).load(file, new MethodSearchCallback(args[1]));
		}
		
	}
	
}