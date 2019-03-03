package server;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcurrentManager {
	//nested class
	public class Tuple<X, Y> { 
		public final X x; 
		public final Y y;
		
		public Tuple(X x, Y y) { 
			this.x = x; 
			this.y = y; 
		}
		public X getFirst(){
			return x;
		}
		public Y getSecond(){
			return y;
		}
	}
	//** Default value to initial semaphore**/
	public static final int DEFAULT_VALUE = 1;
	private Hashtable<String, Tuple<ArrayList<String>,ArrayList<Semaphore>>> concurrentFilesUsers;
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
	public boolean addSem(String user, String path){
		if(user == null || user.isEmpty()|| path == null || path.isEmpty()){
			logger.log(Level.SEVERE, "Error on parameters");
			return false;
		}
		
		if(concurrentFilesUsers.containsKey(user)){
			logger.log(Level.CONFIG, "Already exists for: " + user);
			Tuple<ArrayList<String>, ArrayList<Semaphore>> value = concurrentFilesUsers.get(user);
			if(value.x.contains(path)){
				logger.log(Level.CONFIG, "Already exists path for: " + path);
			}else{
				logger.log(Level.CONFIG, "Add semaphore to the list for path: " + path);
				value.x.add(path);
				value.y.add(new Semaphore(DEFAULT_VALUE));
			}
		}else{
			ArrayList<String> paths = new ArrayList<>();
			ArrayList<Semaphore> sem = new ArrayList<>();
			paths.add(path);
			sem.add(new Semaphore(DEFAULT_VALUE));
			concurrentFilesUsers.put(user, new Tuple<ArrayList<String>, ArrayList<Semaphore>>(paths, sem));
		}
		return true;
	}
	/**
	 * Get semaphore
	 * @param user
	 * @return Tuple with file and semaphore
	 * @requires user != null and path != null
	 */
	public Semaphore getSem(String user, String path){
		if(user == null || user.isEmpty()|| path == null || path.isEmpty()){
			logger.log(Level.SEVERE, "Error on parameters");
			return null;
		}
		Tuple<ArrayList<String>, ArrayList<Semaphore>> value = concurrentFilesUsers.get(user);
		if(value != null){
			for(int i = 0; i < value.x.size(); i++){
				if(value.x.get(i).equals(path)){
					return value.y.get(i);
				}
			}
		}
		logger.log(Level.CONFIG, "Semaphore not found!");
		return null;
	}
}


