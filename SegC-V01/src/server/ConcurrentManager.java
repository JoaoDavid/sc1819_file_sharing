package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcurrentManager {
	//inner class
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
	private Hashtable<String, Tuple<File,Semaphore>> concurrentMsg;
	private Hashtable<String, Tuple<File,Semaphore>> concurrentTrusted;
	private Hashtable<String, Tuple<ArrayList<File>,ArrayList<Semaphore>>> concurrentFilesUsers;
	//singleton
	private ConcurrentManager INSTANCE;
	private static final String CLASS_NAME = ConcurrentManager.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);
	
	private ConcurrentManager(){
		concurrentMsg = new Hashtable<>();
		concurrentTrusted = new Hashtable<>();
		concurrentFilesUsers = new Hashtable<>();
	}
	/**
	 * SINGLETON
	 * @return ConcurrentManager
	 */
	public ConcurrentManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ConcurrentManager();
		}
		return INSTANCE;
	}
	/**
	 * Add file to hashMsg
	 * @param user
	 * @param path
	 */
	public void addMsg(String user, String path){
		if(concurrentMsg.containsKey(user)){
			logger.log(Level.CONFIG, "Already exists for: " + user);
		}else{
			concurrentMsg.put(user, new Tuple<File, Semaphore>(new File(path), new Semaphore(DEFAULT_VALUE)));
		}
	}
	/**
	 * Get File and semaphore
	 * @param user
	 * @return
	 */
	public Tuple<File, Semaphore> getMsg(String user){
		return concurrentMsg.get(user);
	}
	/**
	 * Add file to hashTrusted
	 * @param user
	 * @param path
	 */
	public void addTrusted(String user, String path){
		if(concurrentTrusted.containsKey(user)){
			logger.log(Level.CONFIG, "Already exists for: " + user);
		}else{
			concurrentTrusted.put(user, new Tuple<File, Semaphore>(new File(path), new Semaphore(DEFAULT_VALUE)));
		}
	}
	/**
	 * Get File and semaphore
	 * @param user
	 * @return Tuple with file and semaphore
	 */
	public Tuple<File, Semaphore> getTrusted(String user){
		return concurrentTrusted.get(user);
	}
	/**
	 * Add file to hashFile
	 * @param user
	 * @param path
	 * @requires (new file(path)).exists()
	 */
	public void addUser(String user, String path){
		if(concurrentFilesUsers.containsKey(user)){
			logger.log(Level.CONFIG, "Already exists for: " + user);
			Tuple<ArrayList<File>, ArrayList<Semaphore>> value = concurrentFilesUsers.get(user);
			
		}else{
			ArrayList<File> files = new ArrayList<>();
			ArrayList<Semaphore> sem = new ArrayList<>();
			files.add(new File(path));
			sem.add(new Semaphore(DEFAULT_VALUE));
			concurrentFilesUsers.put(user, new Tuple<ArrayList<File>, ArrayList<Semaphore>>(files, sem));
		}
	}
	/**
	 * Get File and semaphore
	 * @param user
	 * @return Tuple with file and semaphore
	 */
	public Tuple<File, Semaphore> getUser(String user){
		//to complete...
		return null;
	}
}


