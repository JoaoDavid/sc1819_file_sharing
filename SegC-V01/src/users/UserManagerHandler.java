package users;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import facade.exceptions.ApplicationException;
import security.ContentCipher;
import security.MacManager;
import server.business.util.ConstKeyStore;
import server.business.util.FilePaths;
import server.business.util.UserValidation;

public class UserManagerHandler {



	//private KeyStore ks;
	//private FileInputStream fis;
	private SecretKey secKey;
	private PrivateKey privKey;
	private PublicKey pubKey;
	private ContentCipher cypher;
	private MessageDigest md;
	private MacManager macM;


	public UserManagerHandler(SecretKey secKey, PrivateKey privKey, PublicKey pubKey) throws Exception {
		this.secKey = secKey;
		this.privKey = privKey;
		this.pubKey = pubKey;
		macM = new MacManager(ConstKeyStore.MAC_ALGORITHM, secKey);
		md = MessageDigest.getInstance(ConstKeyStore.DIGEST_ALFORITHM);
	}


	/**
	 * Creates a new user
	 * @throws Exception 
	 */
	public boolean createUser(String userName, String password) throws Exception {
		if(macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			if(!UserValidation.userNameRegistered(userName)) {
				FileOutputStream usersFile = new FileOutputStream(FilePaths.FILE_USERS_PASSWORDS,true);
				String data = userName + ":" + getSaltAndPassword(password) + System.getProperty("line.separator");
				usersFile.write(data.getBytes());
				usersFile.close();
				macM.updateMacFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
				//Creates user directory
				File userFiles = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator +  FilePaths.FOLDER_FILES);
				userFiles.mkdirs();
				userFiles.mkdir();
				File userFilesKeys = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator +  FilePaths.FOLDER_FILES_KEYS);
				userFilesKeys.mkdirs();
				userFilesKeys.mkdir();
				//Creates msg file
				createControlFile(userName, FilePaths.FILE_NAME_MSG, privKey, pubKey);
				//Creates trust file
				createControlFile(userName, FilePaths.FILE_NAME_TRUSTED, privKey, pubKey);
				//Creates trust file
				createControlFile(userName, FilePaths.FILE_NAME_FILES_STORED, privKey, pubKey);
				return true;
			}else {
				throw new Exception("userName is taken");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
	}

	private void createControlFile(String userName, String fileName, PrivateKey privKey, PublicKey pubKey) throws Exception {
		//Creates trust file
		File file = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + fileName);
		file.createNewFile();
		//Creates trust's file signature
		File fileSig = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + fileName + ".sig");
		fileSig.createNewFile();
		//Creates trust's file .key
		File fileKey = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + fileName + ".key");
		fileKey.createNewFile();
		//Cipher
		ContentCipher.sigAndEcryptFile(file, fileSig, fileKey, this.privKey, this.pubKey);
	}

	private byte[] getSaltedPassword(String password, byte[] salt) {
		/*byte[] hash = null;
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		hash = factory.generateSecret(spec).getEncoded();
		hash = secKey.getEncoded();
		return hash;*/
		md.update(salt);
		md.update(password.getBytes());
		return md.digest();

	}

	private String getSaltAndPassword(String password) {
		byte[] salt = getSalt() ;
		byte[] hash = getSaltedPassword(password, salt);
		Base64.Encoder enc = Base64.getEncoder();
		String strSalt = enc.encodeToString(salt);
		String strHash = enc.encodeToString(hash);
		return strSalt + ":" + strHash;
	}

	private byte[] getSalt() {
		SecureRandom sr;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			//Create array for salt
			byte[] salt = new byte[16];
			//Get a random salt
			sr.nextBytes(salt);
			return salt;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * Deactivates an user
	 * @throws Exception 
	 */
	public boolean removeUser(String userName) throws Exception {
		boolean result = false;
		if(macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			if(!UserValidation.isDeactivatedUser(userName)) {
				String filePath = FilePaths.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(curr + ":" + FilePaths.DEACTIVATED_USER);
							result = true;
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
					macM.updateMacFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
				} catch (IOException e) {
				}
			}else {
				throw new Exception("user is deactivated");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
		return result;
	}

	/**
	 * Updates user's password
	 * @throws Exception 
	 */
	public boolean updateUser(String userName, String password) throws Exception {
		boolean result = false;
		if(macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			if(!UserValidation.isDeactivatedUser(userName)) {
				String filePath = FilePaths.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(userInfo[0] + ":" + getSaltAndPassword(password));
							result = true;
						}else {
							fileContent.add(curr);
						}
					}
					FileWriter fileWriterClean = new FileWriter(userRegistFile,false);
					fileWriterClean.write("");
					fileWriterClean.close();
					FileWriter fileWriter = new FileWriter(userRegistFile,true);
					for(String currLine : fileContent) {
						fileWriter.write(currLine + System.getProperty("line.separator"));
					}
					fileWriter.close();
					macM.updateMacFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
				} catch (IOException e) {
				}
			}else {
				throw new Exception("User is deactivated");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
		return result;
	}

	public boolean validLogin(String userName, String password) {
		File userRegistFile = new File(FilePaths.FILE_USERS_PASSWORDS);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String curr; 
			while ((curr = br.readLine()) != null) {
				String[] userInfo = curr.split(":");
				if(userInfo[0].equals(userName)) {
					if(!UserValidation.isDeactivatedUserLine(curr)) {
						Base64.Decoder dec = Base64.getDecoder();
						byte[] passwordCalculated = getSaltedPassword(password, dec.decode(userInfo[1]));
						return MessageDigest.isEqual(passwordCalculated, dec.decode(userInfo[2]));
					}else {
						return false;
					}				
				}
			}
		} catch (IOException e) {
		}
		return false;
	}


	public boolean boot() throws ApplicationException {
		File folder = new File(FilePaths.FOLDER_SERVER_USERS);
		if(!folder.exists()) {//first boot
			System.out.println("First boot, creating files ...");
			folder.getParentFile().mkdirs();
			folder.mkdir();
			File userInfo = new File(FilePaths.FILE_USERS_PASSWORDS);
			File userInfoMac = new File(FilePaths.FILE_USERS_PASSWORDS_MAC);
			try {
				userInfo.createNewFile();
				userInfoMac.createNewFile();
				macM.updateMacFile(userInfo.getPath(), userInfoMac.getPath());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
		}
		return false;
	}


}
