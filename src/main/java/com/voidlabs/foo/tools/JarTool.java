package com.voidlabs.foo.tools;

import java.io.File;

import com.voidlabs.foo.resource.JarLoader;
import com.voidlabs.foo.resource.SimplePrintCallback;

/**
 * Simple command-line tool
 */

public class JarTool {

	/**
	 * simple cmdline exec
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("usage : JarTool [lib path] [jar file]");
			System.exit(1);
		}

		File file = new File(args[1]);

		if (!(file.exists() && file.getCanonicalPath().endsWith(".jar"))) {
			System.out.println("invalid file : " + file.getCanonicalPath());
		}

		System.out.println("processing content of jar file : " + file.getCanonicalPath());

		(new JarLoader()).load(file, new SimplePrintCallback());

	}
	
}



