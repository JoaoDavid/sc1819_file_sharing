package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manager {

	private static final String CLASS_NAME = Manager.class.getName();
	private final static Logger logger = Logger.getLogger(CLASS_NAME);

	private File usersFile;

	public Manager(String accountFilePath) {
		usersFile = new File(accountFilePath);
		try {
			usersFile.createNewFile();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "ERROR to open the file " , e);
		}
	}

	public boolean createAccount(String username, String password) {
		logger.log(Level.INFO, "Creating account");
		try {
			FileWriter fileWriter = new FileWriter(usersFile,true);
			String newLine = System.getProperty("line.separator");
			fileWriter.write(username + ":" + password + newLine);
			fileWriter.close();
			File userFiles = new File("users" + File.separator + username + File.separator + "files");
			userFiles.getParentFile().mkdirs(); 
			userFiles.mkdir();
			File userTrusted = new File("users" + File.separator + username + File.separator + "trusted.txt");
			userTrusted.getParentFile().mkdirs(); 
			userTrusted.createNewFile();
			File userMsg = new File("users" + File.separator + username + File.separator + "msg.txt");
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
			return createAccount(username, password);//user is not on registered on file
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public String[] listFiles(String localUser) { //list
		File userFiles = new File("users" + File.separator + localUser + File.separator + "files");
		String[] res = userFiles.list();		
		return res;
	}
	
	public String[] listUsers() { //users	
		File user = new File("users");
		String[] res = user.list();		
		return res;
	}
	
	public boolean isRegistered(String user) {
		return Arrays.asList(listUsers()).contains(user);
	}
	
	public boolean trusted(String localUser, String trustedUserID) { //trusted <trustedUserIDs>
		File trustedFile = new File("users" + File.separator + localUser + File.separator + "trusted.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(trustedFile));
			String st; 
			while ((st = br.readLine()) != null) {
				if(st.equals(trustedUserID)) {
					br.close();
					return false;
				}
			}
			br.close();
			FileWriter fileWriter = new FileWriter(trustedFile,true);
			fileWriter.write(trustedUserID + System.getProperty("line.separator"));
			System.out.println("escreveu");
			fileWriter.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean untrusted(String localUser, String untrustedUserID) { //trusted <trustedUserIDs>
		return false;
	}
	
	public boolean sendFile(String fromUser, String nameFile) {//download <userID> <file>
		return false;//rever nome funcao
	}
	
	public boolean storeMsg(String userSender, String userReceiver, String msg) {//msg <userID> <msg>
		File userMsgs = new File("users" + File.separator + userReceiver + File.separator + "msg.txt");
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
		File userMsgs = new File("users" + File.separator + user + File.separator + "msg.txt");
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