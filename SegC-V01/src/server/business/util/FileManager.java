package server.business.util;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;


public class FileManager {
	
	private class FileCounter{
		
		private File file;
		private int counter;
		
		private FileCounter(File file, Integer counter) {
			this.file = file;
			this.counter = (int) counter;
		}
		
		private void incrementCounter() {
			this.counter++;
		}
		
		private void decrementCounter() {
			this.counter--;
		}

		private java.io.File getFile() {
			return this.file;
		}
		
		private int getCounter() {
			return this.counter;
		}
	}
	
	private static FileManager INSTANCE;
	/**
	 * Map of filePaths and files that are currently being used
	 */
	private ConcurrentHashMap<String, FileCounter> map;
	
	
	private FileManager() {
		map = new ConcurrentHashMap<String, FileCounter>();
	}
	
	public static FileManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new FileManager();
		}
		return INSTANCE;
	}
	
	public File acquireFile(String filePath) {
		FileCounter fc = map.get(filePath);
		if(fc == null) {
			File file = new File(filePath);
			fc = new FileCounter(file,1);
			map.put(filePath, fc);
		}else {
			fc.incrementCounter();
		}
		return fc.getFile();
	}
	
	public void releaseFile(String filePath) {
		FileCounter fc = map.get(filePath);
		if(fc != null) {
			fc.decrementCounter();
			if(fc.getCounter() == 0) {
				map.remove(filePath);
			}
		}
	}

	public boolean removeFile(String filePath) {
		FileCounter fc = map.get(filePath);
		if(fc == null) {//nobody is using the file
			File file = new File(filePath);
			return file.delete();
		}else { //someone is using the file
			File file = fc.getFile();
			synchronized(file) {
	            boolean deleted = file.delete();
	            if(deleted) {
	            	map.remove(filePath);
	            }	            
	            return deleted;
	        }	
		}
		
	}
	

}
