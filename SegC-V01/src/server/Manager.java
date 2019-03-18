package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.OpCode;

public class Manager {
	//** Log **//
	private static final String CLASS_NAME = Manager.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);
	//** File of passwords **//
	private File usersFile;
	//** Object to Singleton **//
	private static Manager INSTANCE;
	//** ConcurrentManager **//
	private ConcurrentManager sempManager;
	private HashMap<String, String> users;

	private Manager() {
		sempManager = new ConcurrentManager();
		users = new HashMap<>();
		usersFile = new File(ServerConst.FILE_USERS_PASSWORDS);
		if(!usersFile.exists()) {
			try {
				usersFile.getParentFile().mkdirs();
				usersFile.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "ERROR to open the file " , e);
			}
		}else{
			try {
				synchronized (usersFile) {
					BufferedReader br = new BufferedReader(new FileReader(usersFile));
					String st; 
					while ((st = br.readLine()) != null) {
						String[] usersPass = st.split(":");
						users.put(usersPass[0], usersPass[1]);
					}
					br.close();
				}
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "File of users not found", e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "IO Exception", e);
			}
		}
		File file = new File(ServerConst.FOLDER_SERVER_USERS);
		File temp;
		if(file.exists()){
			for(String paths: file.list()){
				temp = new File(file.getPath() + File.separator + paths);
				if(file.exists() && file.isDirectory() && users.containsKey(paths)){
					createSemaphore(temp, paths);
				}
			}
		}
	}
	/**
	 * Recursion to add all semaphore for each file
	 * ignore all folders only add semaphore only to files 
	 * @param file
	 * @param user
	 * @requires file.exists() and user != null
	 */
	private void createSemaphore(File file, String user){
		File temp;
		for(String path: file.list()){
			temp = new File(file.getPath() + File.separator +path);
			if(temp.exists()){
				if(ServerConst.FILE_NAME_TRUSTED.equals(temp.getName())){
					continue;
				}else if(temp.isFile()){
					sempManager.addSem(temp.getPath());
				}else{
					createSemaphore(temp, user);
				}
			}
		}
	}

	/**
	 * Singleton
	 * @return Manager
	 */
	public static Manager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Manager();
		}
		return INSTANCE;
	}

	/**
	 * Create Account
	 * @param username
	 * @param password
	 * @return true if got success
	 */
	public boolean createAccount(String username, String password) {
		logger.log(Level.INFO, "Creating account");
		try {
			synchronized (usersFile) {
				FileWriter fileWriter = new FileWriter(usersFile,true);
				String newLine = System.getProperty("line.separator");
				fileWriter.write(username + ":" + password + newLine);
				users.put(username, password);
				fileWriter.close();
			}
			String path = ServerConst.FOLDER_SERVER_USERS + File.separator 
					+ username + File.separator + ServerConst.FOLDER_FILES;
			File userFiles = new File(path);
			userFiles.getParentFile().mkdirs(); 
			userFiles.mkdir();
			//it is a folder do not need semaphore
			//sempManager.addSem(username, path);
			path = ServerConst.FOLDER_SERVER_USERS + File.separator 
					+ username + File.separator + ServerConst.FILE_NAME_TRUSTED;
			File userTrusted = new File(path);
			userTrusted.getParentFile().mkdirs(); 
			userTrusted.createNewFile();
			//sempManager.addSem(username, path);
			path = ServerConst.FOLDER_SERVER_USERS + File.separator 
					+ username + File.separator + ServerConst.FILE_NAME_MSG;
			File userMsg = new File(path);
			userMsg.getParentFile().mkdirs(); 
			userMsg.createNewFile();
			sempManager.addSem(path);
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error creating account", e);
		}
		return false;
	}

	/**
	 * Login
	 * @param username
	 * @param password
	 * @return true was a user with password valid
	 */
	public boolean login(String username, String password) {
		try {
			synchronized (usersFile) {
				BufferedReader br = new BufferedReader(new FileReader(usersFile));
				String st; 
				while ((st = br.readLine()) != null) {
					//loginInfo[0] = username   loginInfo[1] = password				
					String[] loginInfo = st.split(":");
					if(username.equals(loginInfo[0])) {
						if(password.equals(loginInfo[1])) {
							br.close();
							return true;//user and pass match info on file
						}else {
							br.close();
							return false;//password does not match
						}
					}				
				}
				br.close();
			}
			return createAccount(username, password);//user is not on registered on file
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "File of users not found", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO Exception", e);
		}
		return false;
	}

	/**
	 * envia um ou mais ficheiros para o servidor, para a conta do utilizador local
	 * (localUserID). Caso este utilizador já tenha algum ficheiro com o mesmo nome no
	 * servidor, o servidor não substitui o ficheiro existente. O comando deve indicar, para cada
	 * ficheiro, se o envio foi realizado com sucesso ou se o ficheiro já existia no servidor.
	 * 
	 * @param nameFiles
	 * @param byteFiles
	 * @param connectedUser
	 * @requires nameFiles != null && byteFiles != null && connectedUser != null
	 * @return array of opcodes, with the same length as nameFiles and byteFiles, for each position has the respective
	 * code indicating if the file was successfuly stored
	 */
	public OpCode[] storeFiles(ArrayList<String> nameFiles, ArrayList<Byte[]> byteFiles, String connectedUser){
		File file;
		String path;
		OpCode[] result = new OpCode[nameFiles.size()];
		for(int i = 0; i < nameFiles.size();i++){
			path = ServerConst.FOLDER_SERVER_USERS + File.separator + connectedUser 
					+ File.separator + ServerConst.FOLDER_FILES + File.separator + nameFiles.get(i);
			file = new File(path);
			if(file.exists()){
				result[i] = OpCode.ERR_ALREADY_EXISTS;
			}else{
				try{
					file.getParentFile().mkdirs();
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(toPrimitives(byteFiles.get(i)));
					fos.close();
					result[i] = OpCode.OP_SUCCESSFUL;
					sempManager.addSem(path);
				}catch(Exception e){
					logger.log(Level.SEVERE, "FAILED to store the file: " + file.getName());
					result[i] = OpCode.OP_ERROR;
				}
			}
		}
		return result;
	}

	/**
	 * Turn array of Byte[] to byte[]
	 * @param oBytes
	 * @return
	 */
	private static byte[] toPrimitives(Byte[] oBytes) {

		byte[] bytes = new byte[oBytes.length];
		for(int i = 0; i < oBytes.length; i++){
			bytes[i] = oBytes[i];
		}
		return bytes;

	}
	/**
	 * @requires localUser != null
	 * @param localUser
	 * @return lista os ficheiros que o utilizador local (localUserID) tem no servidor.
	 */
	public String[] listFiles(String localUser) { //list
		File userFiles = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + localUser 
				+ File.separator + ServerConst.FOLDER_FILES);
		return userFiles.list();
	}
	/**
	 * remove um ou mais ficheiros do servidor, da conta do utilizador local. O
	 * comando deve indicar, para cada ficheiro, se a remoção foi realizada com sucesso ou um
	 * erro no caso de o ficheiro não existir no servidor.
	 * @param fileName
	 * @return
	 */
	public boolean deleteFile(String user, String fileName) {
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator + user + 
				File.separator +  ServerConst.FOLDER_FILES + File.separator + fileName;
		logger.log(Level.CONFIG, "Patg to delete: " + path);
		StampedLock sem = sempManager.getSem(path);
		if(sem == null){
			return false;
		}
		long stamp = sem.writeLock();
		try {	
			File file = new File(path);
			boolean isDeleted = file.delete();
			if(isDeleted){
				sempManager.delSem(user, path);
			}
			return isDeleted;
		}catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage(), e);
		}finally {
			sem.unlockWrite(stamp);
		}
		return false;
	}
	/**
	 * @return List of Users
	 */
	public String[] listUsers() { //users	
		return users.keySet().toArray(new String[users.size()]);
	}
	/**
	 * @param user
	 * @return true if registered
	 */
	public boolean isRegistered(String user) {
		return users.containsKey(user);
	}
	/**
	 * Friends 
	 * @param localUser
	 * @param otherUser
	 * @return localUser is friend of otherUser
	 */
	public boolean friends(String localUser, String otherUser) {
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator + otherUser + File.separator + ServerConst.FILE_NAME_TRUSTED;
		File trustedFile = new File(path);
		BufferedReader br;
		if(trustedFile.exists()) {
			try {
				br = new BufferedReader(new FileReader(trustedFile));
				String st; 
				while ((st = br.readLine()) != null) {
					if(st.equals(localUser)) {
						br.close();
						return true;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "File Not Found in Friends", e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Method Friends fail", e);
			}
		}
		return false;
	}

	/**
	 * adiciona os utilizadores trustedUserIDs como amigos do
	 * utilizador local. Se algum dos utilizadores já estiver na lista de amigos do utilizador local
	 * deve ser assinalado um erro. Os restantes utilizadores são adicionados normalmente
	 * 
	 * @param localUser
	 * @param trustedUserID
	 * @return vetor de opcode ou null em caso de erro
	 */
	public OpCode[] trusted(String localUser, String[] trustedUserID) { //trusted <trustedUserIDs>
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator + localUser 
				+ File.separator + ServerConst.FILE_NAME_TRUSTED;
		File trustedFile = new File(path);
		OpCode[] result = new OpCode[trustedUserID.length];
		BufferedReader br;
		ArrayList<String> fileContent = new ArrayList<String>();
		try {
			trustedFile.getParentFile().mkdirs();
			trustedFile.createNewFile();
			br = new BufferedReader(new FileReader(trustedFile));
			String st; 
			while ((st = br.readLine()) != null) {
				fileContent.add(st);
			}
			br.close();
			FileWriter fileWriter = new FileWriter(trustedFile,true);
			for (int i = 0; i < trustedUserID.length; i++) {
				if(localUser.equals(trustedUserID[i])) {
					result[i] = OpCode.ERR_YOURSELF;
				}else if(!isRegistered(trustedUserID[i])) {
					result[i] = OpCode.ERR_NOT_REGISTERED;
				}else if(fileContent.contains(trustedUserID[i])) {
					result[i] = OpCode.ERR_ALREADY_EXISTS;
				}else {
					fileWriter.write(trustedUserID[i] + System.getProperty("line.separator"));
					fileContent.add(trustedUserID[i]);
					result[i] = OpCode.OP_SUCCESSFUL;
				}
			}			
			fileWriter.close();
			return result;
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "File not found: " + path, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException in trusted", e);
		}
		return null;
	}

	/**
	 * 
	 * @param localUser
	 * @param untrustedUserID
	 * @return
	 */
	public OpCode[] untrusted(String localUser, String[] untrustedUserID) { //trusted <trustedUserIDs>
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator + localUser 
				+ File.separator + ServerConst.FILE_NAME_TRUSTED;
		OpCode[] result = new OpCode[untrustedUserID.length];
		File trustedFile = new File(path);
		BufferedReader br;
		ArrayList<String> fileContent = new ArrayList<String>();
		try {
			trustedFile.getParentFile().mkdirs();
			trustedFile.createNewFile();
			br = new BufferedReader(new FileReader(trustedFile));
			String st; 
			while ((st = br.readLine()) != null) {
				fileContent.add(st);
			}
			br.close();
			for(int i = 0; i < untrustedUserID.length; i++) {
				if(!isRegistered(untrustedUserID[i])) {
					result[i] = OpCode.ERR_NOT_REGISTERED;
				}else if(localUser.equals(untrustedUserID[i])) {
					result[i] = OpCode.ERR_YOURSELF;
				}else {
					boolean untrusted = fileContent.remove(untrustedUserID[i]);
					if(untrusted) {
						result[i] = OpCode.OP_SUCCESSFUL;
					}else {
						result[i] = OpCode.ERR_NOT_FOUND;
					}
				}
			}
			FileWriter fileWriter = new FileWriter(trustedFile);
			fileWriter.write("");
			fileWriter.close();
			fileWriter = new FileWriter(trustedFile,true);
			for(String str : fileContent) {
				System.out.println(str);
				fileWriter.write(str + System.getProperty("line.separator"));
			}
			fileWriter.close();
			return result;
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "File not found: " + path, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
		}
		return null;
	}
	/**
	 * 
	 * @param userOwner
	 * @param userDownloading
	 * @param nameFile
	 * @return
	 */
	public Byte[] sendFileToClient(String userOwner, String nameFile) {//download <userID> <file>
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator + userOwner 
				+ File.separator + ServerConst.FOLDER_FILES + File.separator + nameFile;
		File file = new File(path);
		if(file.exists()){
			StampedLock sem = sempManager.getSem(path);
			if(sem == null){
				logger.log(Level.SEVERE, "Semaphore null");
				return null;
			}
			long stamp = sem.readLock();
			try {
				return toObjects(Files.readAllBytes(file.toPath()));
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error to converte the file :" + nameFile + " to bytes", e);
			}finally{
				sem.unlock(stamp);
			}
		}
		return null;
	}
	private static Byte[] toObjects(byte[] bytesPrim) {
		Byte[] bytes = new Byte[bytesPrim.length];
		int i = 0;
		for (byte b : bytesPrim){
			bytes[i++] = b;
		}
		return bytes;
	}
	/**
	 * Write message on the file of user
	 * @param userSender
	 * @param userReceiver
	 * @param msg
	 * @return true if it is success 
	 */
	public boolean storeMsg(String userSender, String userReceiver, String msg) {//msg <userID> <msg>
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator + userReceiver 
				+ File.separator + ServerConst.FILE_NAME_MSG;
		File userMsgs = new File(path);
		StampedLock sem = sempManager.getSem(path);
		if(sem == null){
			return false;
		}
		FileWriter fileWriter;
		long stamp = sem.writeLock();
		try {
			fileWriter = new FileWriter(userMsgs,true);
			fileWriter.write(userSender + ":" + msg + System.getProperty("line.separator"));
			fileWriter.close();
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error to write in store Msg", e);
		}finally{
			sem.unlock(stamp);
		}
		return false;
	}
	/**
	 * Collect Msg from user
	 * @param user
	 * @return list with all messages -> null if empty
	 */
	public ArrayList<String> collectMsg(String user) {//collect
		String path = ServerConst.FOLDER_SERVER_USERS + File.separator 
				+ user + File.separator + ServerConst.FILE_NAME_MSG;
		ArrayList<String> result = new ArrayList<String>();
		File userMsgs = new File(path);
		StampedLock sem = sempManager.getSem(path);
		if(sem == null){
			logger.log(Level.CONFIG, "Semaphore Empty");
			return null;
		}
		long stamp = sem.writeLock();
		try {
			BufferedReader br = new BufferedReader(new FileReader(userMsgs));
			String st;
			while ((st = br.readLine()) != null) {					
				result.add(st);
			}				
			br.close();
			FileWriter fileWriter = new FileWriter(userMsgs);
			fileWriter.write("");
			fileWriter.close();
			//clear inbox
			return result;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Impossible Colect Messages", e);
			return null;
		}finally{
			sem.unlock(stamp);
		}
	}
	/**
	 * check if the user have this files or not
	 * @param list
	 * @param connectedUser
	 * @return lista de ficheiros que nao estao armazenados
	 */
	public void haveThisFiles(ArrayList<String> list, String connectedUser) {
		for(String fileName:  listFiles(connectedUser)){
			list.remove(fileName);
		}
	}

}
