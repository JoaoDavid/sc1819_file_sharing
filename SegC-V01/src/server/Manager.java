package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Message;
import communication.OpCode;

public class Manager {

	private static final String CLASS_NAME = Manager.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	private File usersFile;


	private static Manager INSTANCE;

	private Manager() {
		usersFile = new File(ServerConst.FILE_USERS_PASSWORDS);
		if(!usersFile.exists()) {
			try {
				usersFile.getParentFile().mkdirs();
				usersFile.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "ERROR to open the file " , e);
			}
		}
	}

	public static Manager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Manager();
		}
		return INSTANCE;
	}

	public boolean createAccount(String username, String password) {
		logger.log(Level.INFO, "Creating account");
		try {
			synchronized (usersFile) {
				FileWriter fileWriter = new FileWriter(usersFile,true);
				String newLine = System.getProperty("line.separator");
				fileWriter.write(username + ":" + password + newLine);
				fileWriter.close();
			}

			File userFiles = new File(ServerConst.FOLDER_SERVER_USERS + File.separator 
					+ username + File.separator + ServerConst.FOLDER_FILES);
			userFiles.getParentFile().mkdirs(); 
			userFiles.mkdir();
			File userTrusted = new File(ServerConst.FOLDER_SERVER_USERS + File.separator 
					+ username + File.separator + ServerConst.FILE_NAME_TRUSTED);
			userTrusted.getParentFile().mkdirs(); 
			userTrusted.createNewFile();
			File userMsg = new File(ServerConst.FOLDER_SERVER_USERS + File.separator 
					+ username + File.separator + ServerConst.FILE_NAME_MSG);
			userMsg.getParentFile().mkdirs(); 
			userMsg.createNewFile();
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error creating account", e);
		}
		return false;
	}

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
							System.out.println("Login successful");
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
	 * @param succ
	 * @param failed
	 * @param msg
	 * @param connectedUser
	 * @requires succ != null && failed != null && msg != null && connectedUser != null
	 */
	public void storeFiles(ArrayList<String> succ, ArrayList<String> failed, 
			Message msg, String connectedUser){
		File file;
		for(int i = 0; i < msg.getParam().size();i++){
			file = new File(ServerConst.FOLDER_SERVER_USERS 
					+ File.separator + connectedUser 
					+ File.separator + msg.getParam().get(i));
			if(file.exists()){
				failed.add(file.getName());
			}else{
				try{
					file.getParentFile().mkdirs();
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(toPrimitives(msg.getParamBytes().get(i)));
					fos.close();
					succ.add(file.getName());
				}catch(Exception e){
					logger.log(Level.SEVERE, "FAILED to store the file: " + file.getName());
					failed.add(file.getName());
				}
			}
		}
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
		File userFiles = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + localUser + File.separator + ServerConst.FOLDER_FILES);
		return userFiles.list();
	}

	public boolean deleteFile(String fileName) {
		/* try {
		       synchronized (file) {
		          // ... open/modify/close file as per the request parameters...
		       }
		    }
		    finally {
		       releaseFile( file );
		    }*/
		return false;//fazer
	}

	public String[] listUsers() { //users	
		File user = new File(ServerConst.FOLDER_SERVER_USERS);
		return user.list();
	}

	public boolean isRegistered(String user) {
		return Arrays.asList(listUsers()).contains(user);
	}

	public boolean friends(String localUser, String otherUser) {
		File trustedFile = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + localUser + File.separator + ServerConst.FILE_NAME_TRUSTED);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(trustedFile));
			String st; 
			while ((st = br.readLine()) != null) {
				if(st.equals(otherUser)) {
					br.close();
					return true;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public OpCode trusted(String localUser, String trustedUserID) { //trusted <trustedUserIDs>
		File trustedFile = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + localUser + File.separator + ServerConst.FILE_NAME_TRUSTED);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(trustedFile));
			String st; 
			while ((st = br.readLine()) != null) {
				if(st.equals(trustedUserID)) {
					br.close();
					//return false;
					return OpCode.ERR_ALREADY_EXISTS;
				}
			}
			br.close();
			if(localUser.equals(trustedUserID)) {
				return OpCode.ERR_YOURSELF;
			}
			if(!isRegistered(trustedUserID)) {
				return OpCode.ERR_NOT_FOUND;
			}
			FileWriter fileWriter = new FileWriter(trustedFile,true);
			fileWriter.write(trustedUserID + System.getProperty("line.separator"));
			System.out.println("escreveu");
			fileWriter.close();
			return OpCode.OP_SUCCESSFUL;
			//return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return false;
		return OpCode.OP_ERROR;
	}

	public OpCode untrusted(String localUser, String untrustedUserID) { //trusted <trustedUserIDs>
		File trustedFile = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + localUser + File.separator + ServerConst.FILE_NAME_TRUSTED);
		//
		return OpCode.OP_ERROR;//fazer
	}

	public boolean sendFileToClient(String userOwner, String userDownloading, String nameFile) {//download <userID> <file>
		if(friends(userOwner,userDownloading)) {
			return false;//FAZER
		}else {
			return false;
		}
	}

	public boolean storeMsg(String userSender, String userReceiver, String msg) {//msg <userID> <msg>
		File userMsgs = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + userReceiver + File.separator + ServerConst.FILE_NAME_MSG);
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(userMsgs,true);
			fileWriter.write(userSender + ":" + msg + System.getProperty("line.separator"));
			fileWriter.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<String> collectMsg(String user) {//collect
		ArrayList<String> result = new ArrayList<String>();
		File userMsgs = new File(ServerConst.FOLDER_SERVER_USERS + File.separator + user + File.separator + ServerConst.FILE_NAME_MSG);
		if(userMsgs.length() == 0) {
			return null;//nao ha msgs na caixa
		}else {			 
			try {
				BufferedReader br = new BufferedReader(new FileReader(userMsgs));
				String st;
				while ((st = br.readLine()) != null) {					
					/*int indexSep = st.indexOf(':');
					String userFrom = st.substring(0, indexSep);
					String msg = st.substring(indexSep+1);*/
					result.add(st);
				}				
				br.close();
				FileWriter fileWriter = new FileWriter(userMsgs);
				fileWriter.write("");
				fileWriter.close();
				//clear inbox
				return result;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

	}

}