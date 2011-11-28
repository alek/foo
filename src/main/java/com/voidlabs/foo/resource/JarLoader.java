package com.voidlabs.foo.resource;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarEntry;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;
import java.util.LinkedList;

import com.voidlabs.foo.conf.FooConf;
import com.voidlabs.foo.local.JarContentCache;

/**
 * Simple jar file analysis tool
 */
public class JarLoader extends ClassLoader {
	
	private LinkedBlockingQueue<ClassData> classQueue = new LinkedBlockingQueue<ClassData>();
	private static long bytesLoaded = 0;
	
	private JarContentCache cache;
	
	// init jar loader with no cache supported
	public JarLoader() {}
	
	/**
	 * initialize jar loader with cache support
	 */
	public JarLoader(JarContentCache cache) {
		this.cache = cache;
	}
	
	/** 
	 * get total bytes loaded by this loader
	 */ 
	public long getBytesLoaded() {
		return bytesLoaded;
	}
	
	/**
	 * process jar and dump info
	 */
	public void load(File jarFile, ClassloadCallback callback) throws Exception {
		
		List<ClassData> classEntries = new LinkedList<ClassData>();
		
		if ((cache != null) && (cache.contains(jarFile))) {
			classEntries = cache.getEntries(jarFile);
		} else {
			classEntries = loadClassData(jarFile);
		}

		// load class entries into resolve queue
		for (ClassData entry : classEntries) {
			classQueue.add(entry);
		}
		
		if (FooConf.DEBUG_ENABLED) {
 			System.out.println(jarFile.getCanonicalPath() + " total loaded : " + (double)bytesLoaded/(1024*1024) + " Mb");
		}
		
		int numClasses = classQueue.size();
		int loadCounter = 0;	
				
		while(!classQueue.isEmpty()) {
			ClassData _entry = classQueue.remove();
			
			if (_entry.hasResolvedMethods()) {	// if methods have been resolved from cache - continue
				callback.handle(_entry);		// just invoke callback
				continue;
			}
			
			try {
				Class c = defineClass(_entry.getName(), _entry.getData(), 0, _entry.getData().length);
				_entry.setClass(c);
				callback.handle(_entry);
				
				if (cache != null) {
					cache.add(_entry);
				}
				
			} catch (java.lang.NoClassDefFoundError e1) {
				// dependency not found -> return class to queue for resolve retry
				classQueue.add(_entry);
			} catch (java.lang.IllegalAccessError e2) {
				// skip entries with access problems
			} catch (java.lang.LinkageError e3) {
				// report-resolve
			} catch (Exception e) {
				if (FooConf.DEBUG_ENABLED) {
					// report exception - do not reattempt resolution
					e.printStackTrace();
				}
			}
			if (loadCounter++ > 1.2*numClasses) {
				break;		// simple tiebreaker for curcular-dependency resolving (TODO : fix)
							// + report failure to load
			}
		}
		
	}			
	
	
	/**
	 * load classes from given jar
	 */
	public List<ClassData> loadClassData(File jarFile) throws Exception {
		
		LinkedList<ClassData> result = new LinkedList<ClassData>();
		
		JarInputStream is = new JarInputStream(new FileInputStream(jarFile));
		
		JarEntry entry = null;
		byte[] buffer = new byte[4096];
		
		// load classQueue
		
		while ((entry = is.getNextJarEntry()) != null) {
		
			if (entry.getName().endsWith(".class")) {
		
				String className = entry.getName().substring(0,entry.getName().length() - 6).replaceAll("/",".");
				int nread;
				
				ByteArrayOutputStream buff = new ByteArrayOutputStream();
				
				while ((nread = is.read(buffer)) != -1) {
					buff.write(buffer,0,nread);
				}
				
				byte[] classData = buff.toByteArray();
				bytesLoaded += classData.length;
				
				ClassData classEntry = new ClassData(className, classData);
				classEntry.setJarFile(jarFile);
				
				result.add(classEntry);
				
			}
		}
		
		return result;
	}	
	
}