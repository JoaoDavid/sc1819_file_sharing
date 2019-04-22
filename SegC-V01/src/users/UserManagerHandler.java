package users;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import server.ServerConst;
import server.business.util.FileManager;

public class UserManagerHandler {

	public static final String MAC_ALGORITHM = "HmacSHA1";
	public static final String KEYSTORE_TYPE = "JCEKS";



	private Mac mac;
	private KeyStore ks;
	private FileInputStream fis;
	private SecretKey secKey;


	public UserManagerHandler(String keyAlias, String keyPassword, String keystoreLocation, String keystorePassword) throws Exception {
		mac = Mac.getInstance(MAC_ALGORITHM);
		ks = KeyStore.getInstance(KEYSTORE_TYPE);
		fis = new FileInputStream(keystoreLocation);
		ks.load(fis,keystorePassword.toCharArray());
		secKey = (SecretKey) ks.getKey(keyAlias, keyPassword.toCharArray());
		mac.init(secKey);
	}


	/**
	 * Creates a new user
	 * @throws Exception 
	 */
	public void createUser(String userName, String password) throws Exception {
		if(validRegistFile()) {
			if(!userNameRegistered(userName)) {
				FileOutputStream usersFile = new FileOutputStream(ServerConst.FILE_USERS_PASSWORDS,true);
				String data = userName + ":" + saltPassoword(password) + System.getProperty("line.separator");	
				usersFile.write(data.getBytes());
				usersFile.close();
				updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
			}else {
				throw new Exception("userName is taken");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
	}

	private String saltPassoword(String password) {
		return "salt" + ":" + password;
	}

	private void updateMacFile(String filePath, String filePathMAC) throws Exception {
		FileOutputStream usersFileMAC = new FileOutputStream(ServerConst.FILE_USERS_PASSWORDS_MAC);
		byte[] newMACBytes = mac.doFinal(Files.readAllBytes(Paths.get(ServerConst.FILE_USERS_PASSWORDS)));
		usersFileMAC.write(newMACBytes);
		usersFileMAC.close();
	}


	/**
	 * Deactivates an user
	 * @throws Exception 
	 */
	public void removeUser(String userName) throws Exception {
		if(validRegistFile()) {
			if(!isDeactivatedUser(userName)) {
				String filePath = ServerConst.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(curr + ":" + ServerConst.DEACTIVATED_USER);
						}else {
							fileContent.add(curr);
						}
					}
					br.close();
					FileWriter fileWriterClean = new FileWriter(userRegistFile,false);
					fileWriterClean.write("");
					fileWriterClean.close();
					FileWriter fileWriter = new FileWriter(userRegistFile,true);
					for(String currLine : fileContent) {
						fileWriter.write(currLine + System.getProperty("line.separator"));
					}
					fileWriter.close();
					updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
				} catch (IOException e) {
				}
			}else {
				throw new Exception("user is deactivated");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
	}

	/**
	 * Updates user's password
	 * @throws Exception 
	 */
	public void updateUser(String userName, String password) throws Exception {
		if(validRegistFile()) {
			if(!isDeactivatedUser(userName)) {
				String filePath = ServerConst.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(userInfo[0] + ":" + saltPassoword(password));
						}else {
							fileContent.add(curr);
						}
					}
					br.close();
					FileWriter fileWriterClean = new FileWriter(userRegistFile,false);
					fileWriterClean.write("");
					fileWriterClean.close();
					FileWriter fileWriter = new FileWriter(userRegistFile,true);
					for(String currLine : fileContent) {
						fileWriter.write(currLine + System.getProperty("line.separator"));
					}
					fileWriter.close();
					updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
				} catch (IOException e) {
				}
			}else {
				throw new Exception("User is deactivated");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
	}

	private boolean validRegistFile()  {
		try {
			Mac otherMAC = Mac.getInstance(MAC_ALGORITHM);
			otherMAC.init(secKey);
			File userRegistFile = new File(ServerConst.FILE_USERS_PASSWORDS);
			File userRegistFileMAC = new File(ServerConst.FILE_USERS_PASSWORDS_MAC);
			if(userRegistFileMAC.length() != 0) {
				byte[] otherMACfinal = otherMAC.doFinal(Files.readAllBytes(userRegistFile.toPath())); 
				byte[] savedMAC = Files.readAllBytes(Paths.get(ServerConst.FILE_USERS_PASSWORDS_MAC));
				return Arrays.equals(otherMACfinal, savedMAC);
			}else {
				updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
				return true;
			}
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isDeactivatedUser(String userName) {
		String filePath = ServerConst.FILE_USERS_PASSWORDS;
		File userRegistFile = new File(filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String st; 
			while ((st = br.readLine()) != null) {
				String[] userInfo = st.split(":");
				if(userInfo.length == 4 && userInfo[0].equals(userName)) {
					return userInfo[3].equals(ServerConst.DEACTIVATED_USER);
				}
			}
		} catch (IOException e) {
		}
		return false;
	}

	public static boolean userNameRegistered(String userName) {
		String filePath = ServerConst.FILE_USERS_PASSWORDS;
		File userRegistFile = new File(filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String st; 
			while ((st = br.readLine()) != null) {
				String[] userInfo = st.split(":");
				if(userInfo[0].equals(userName)) {
					return true;
				}
			}
		} catch (IOException e) {
		}
		return false;
	}


}
