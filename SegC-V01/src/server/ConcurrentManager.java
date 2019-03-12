package server;

import java.util.Hashtable;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcurrentManager {
	private Hashtable<String,StampedLock> concurrentFilesUsers;
	//** Log **//
	private static final String CLASS_NAME = ConcurrentManager.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);
	
	public ConcurrentManager(){
		concurrentFilesUsers = new Hashtable<>();
	}
	/**
	 * Add file to hashtable
	 * @param user
	 * @param path
	 * @requires user != null and path != null
	 */
	public boolean addSem(String path){
		if(path == null || path.isEmpty()){
			logger.log(Level.SEVERE, "Error on parameters");
			return false;
		}else if(concurrentFilesUsers.containsKey(path)){
			logger.log(Level.CONFIG, "Already exists: " + path);
			return false;
		}
		logger.log(Level.CONFIG, "Semaphore ADDED: " + path);
		concurrentFilesUsers.put(path, new StampedLock());
		return true;
	}
	/**
	 * Get semaphore
	 * @param user
	 * @return Tuple with file and semaphore
	 * @requires user != null and path != null
	 */
	public StampedLock getSem(String path){
		if(path == null || path.isEmpty()){
			logger.log(Level.SEVERE, "Error on parameters");
			return null;
		} else{
			StampedLock locker = concurrentFilesUsers.get(path);
			if(locker != null ){
				logger.log(Level.CONFIG, "Semaphore found! path: "+ path);
				return locker;
			}
		}
		logger.log(Level.CONFIG, "Semaphore not found! path: "+ path);
		return null;
	}
	public void delSem(String user, String path) {
		logger.log(Level.CONFIG, "Delete Semaphore path: "+ path);
		if(path == null || path.isEmpty()){
			logger.log(Level.SEVERE, "Error on parameters");
			return;
		}
		if(concurrentFilesUsers.remove(path) != null){
			logger.log(Level.CONFIG, "Semaphore deleted to path: "+ path);
		}else{
			logger.log(Level.CONFIG, "Semaphore NOT deleted to path: "+ path);
		}
	}
}


