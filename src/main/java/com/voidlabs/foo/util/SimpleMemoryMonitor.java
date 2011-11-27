package com.voidlabs.foo.util;

import java.lang.management.ThreadMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryPoolMXBean;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

import com.voidlabs.foo.conf.FooConf;


/**
 * Simple Memory Monitoring Thread
 * TODO : switch list for bounded array
 */

public class SimpleMemoryMonitor extends Thread {

	MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
	
	// simple bounded-buffer 
	
	LinkedList<MemoryUsage> heapUsage = new LinkedList<MemoryUsage>();
	LinkedList<MemoryUsage> nonHeapUsage = new LinkedList<MemoryUsage>();
	
	HashMap<String, LinkedList<MemoryUsage>> poolUsage = new HashMap<String, LinkedList<MemoryUsage>>();

	private long SNAPSHOT_INTERVAL = 1000;
	
	/**
	 * set snapshot interval
	 */
	public void setSnapshotInterval(long interval) {
		SNAPSHOT_INTERVAL = interval;
	}

	/**
	 * snapshot current memory state
	 */
	public void snapshot() {
		
		heapUsage.add(mxb.getHeapMemoryUsage());
		nonHeapUsage.add(mxb.getNonHeapMemoryUsage());

		for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
			LinkedList<MemoryUsage> usage = poolUsage.get(pool.getName());
			if (usage == null) {
				usage = new LinkedList<MemoryUsage>();
			}
			usage.add(pool.getUsage());
			poolUsage.put(pool.getName(), usage);
		}

	}
	
	/**
	 * get historical data on heap usage
	 */
	public LinkedList<MemoryUsage> getHeapUsageHistory() {
		return heapUsage;
	}
	
	/**
	 * get historical data on non-heap usage
	 */
	public LinkedList<MemoryUsage> geNonHeapUsageHistory() {
		return nonHeapUsage;
	}
	
	/**
	 * get per-memory pool usage history
	 */
	public HashMap<String, LinkedList<MemoryUsage>> getPoolUsageHistory() {
		return poolUsage;
	}
	/**
	 * exec snapshot loop
	 */ 
	public void run() {
		while (true) {
			snapshot();
			if (FooConf.DEBUG_ENABLED && FooConf.VERBOSE_MEMORY) {
				
				System.out.println("---------------------------------------------");
				System.out.println("heap    | " + heapUsage.getLast());
				System.out.println("nonheap | " + nonHeapUsage.getLast());
				
				for (String pool : poolUsage.keySet()) {
					System.out.println(pool + " | " + poolUsage.get(pool).getLast());
				}
				System.out.println("---------------------------------------------");

			}
			try { Thread.sleep(SNAPSHOT_INTERVAL); } catch (Exception e) {}
		}
	}
	
}