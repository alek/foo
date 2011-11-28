package com.voidlabs.foo.tools;

import java.io.PrintStream;

/**
 * print foo usage
 */
public class HelpCommand {
	
	public static void main(String[] args) throws Exception {
		
		PrintStream out = System.out;
		
		out.println("usage ./foo [command] [args]");
		out.println("valid commands: ");
		out.println("\tclass [class name]- find class entries matching given pattern");
		out.println("\tmethod [method name] - find methods matching given pattern");
		out.println("\tdescribe [class name] - get detailed description of classes matching given pattern");
		out.println("\tjar [jar path] - get detailed description of jar entries");
		out.println("\thelp - print help");
		out.println("examples:");
		out.println("\tfoo class IndexWriter");
		out.println("\tfoo method isTokenChar");
		out.println("\tfoo jar lib/lucene-core-3.4.0.jar");
		out.println("\tfoo describe sun.management.ManagementFactory");
		out.println("\tfoo search LinkedList");

	}
	
}