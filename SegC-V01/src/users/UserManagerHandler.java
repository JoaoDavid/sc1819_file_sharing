package users;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import security.ContentCipher;
import security.MacManager;
import server.business.util.ConstKeyStore;
import server.business.util.FileManager;
import server.business.util.FilePaths;

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
	public void createUser(String userName, String password) throws Exception {
		if(macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			if(!userNameRegistered(userName)) {
				FileOutputStream usersFile = new FileOutputStream(FilePaths.FILE_USERS_PASSWORDS,true);
				String data = userName + ":" + getSaltAndPassword(password) + System.getProperty("line.separator");
				usersFile.write(data.getBytes());
				usersFile.close();
				macM.updateMacFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
				//Creates user directory
				File userFiles = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator +  FilePaths.FOLDER_FILES);
				userFiles.mkdirs();
				userFiles.mkdir();
				//Creates msg file
				File msgFile = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_MSG);
				msgFile.createNewFile();
				//Creates msg's file signature
				File msgFileSig = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_MSG + ".sig");
				msgFileSig.createNewFile();
				//Creates msg's file .key
				File msgFileKey = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_MSG + ".key");
				msgFileKey.createNewFile();
				//Cipher
				ContentCipher.sigAndEcryptFile(msgFile, msgFileSig, msgFileKey, this.privKey, this.pubKey);
				//Create signature
				/*Signature s = Signature.getInstance("MD5withRSA");
				s.initSign(this.privKey);
				s.update(Files.readAllBytes(msgFile.toPath()));
				FileOutputStream fos = new FileOutputStream(msgFileSig);
				fos.write(s.sign());
				fos.close();
				FileOutputStream fos2 = new FileOutputStream(msgFile);
				ContentCipher contentCipher = new ContentCipher(ConstKeyStore.SYMMETRIC_KEY_ALGORITHM,ConstKeyStore.SYMMETRIC_KEY_SIZE);
				byte[] fileInBytes = Files.readAllBytes(msgFile.toPath());
				fos2.write(contentCipher.encrypt(fileInBytes));				
				Cipher c = Cipher.getInstance(pubKey.getAlgorithm());
				c.init(Cipher.WRAP_MODE, pubKey);
				FileOutputStream fosK = new FileOutputStream(msgFileKey);
				fosK.write(c.wrap(contentCipher.getKey()));*/
				
				//Creates trust file
				File trustFile = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_TRUSTED);
				trustFile.createNewFile();
				FileOutputStream fosTest = new FileOutputStream(trustFile);
				String palavra = userName + System.getProperty("line.separator");
				fosTest.write(palavra.getBytes());
				palavra = "batata" + System.getProperty("line.separator");
				fosTest.write(palavra.getBytes());
				palavra = "frita" + System.getProperty("line.separator");
				fosTest.write(palavra.getBytes());
				fosTest.close();
				
				//Creates trust's file signature
				File trustFileSig = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_TRUSTED + ".sig");
				trustFileSig.createNewFile();
				//Creates trust's file .key
				File trustFileKey = new File (FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_TRUSTED + ".key");
				trustFileKey.createNewFile();
				//Cipher
				ContentCipher.sigAndEcryptFile(trustFile, trustFileSig, trustFileKey, this.privKey, this.pubKey);		
				
				ContentCipher.decryptFileAndCheckSig(trustFile, trustFileSig, trustFileKey, this.privKey, this.pubKey);
			}else {
				throw new Exception("userName is taken");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
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
	public void removeUser(String userName) throws Exception {
		if(macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			if(!isDeactivatedUser(userName)) {
				String filePath = FilePaths.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(curr + ":" + FilePaths.DEACTIVATED_USER);
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
	}

	/**
	 * Updates user's password
	 * @throws Exception 
	 */
	public void updateUser(String userName, String password) throws Exception {
		if(macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC)) {
			if(!isDeactivatedUser(userName)) {
				String filePath = FilePaths.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(userInfo[0] + ":" + getSaltAndPassword(password));
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
				throw new Exception("User is deactivated");
			}
		}else {
			throw new Exception("MAC INVALID : ABORT");
		}
	}



	public static boolean isDeactivatedUser(String userName) {
		String filePath = FilePaths.FILE_USERS_PASSWORDS;
		File userRegistFile = new File(filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String st; 
			while ((st = br.readLine()) != null) {
				String[] userInfo = st.split(":");
				if(userInfo[0].equals(userName)) {
					return isDeactivatedUserLine(st);
				}
			}
		} catch (IOException e) {
		}
		return false;
	}

	private static boolean isDeactivatedUserLine(String line) {
		String[] userInfo = line.split(":");
		return userInfo.length == 4 && userInfo[3].equals(FilePaths.DEACTIVATED_USER);
	}

	public static boolean userNameRegistered(String userName) {
		String filePath = FilePaths.FILE_USERS_PASSWORDS;
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

	public boolean validLogin(String userName, String password) {
		File userRegistFile = new File(FilePaths.FILE_USERS_PASSWORDS);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String curr; 
			while ((curr = br.readLine()) != null) {
				String[] userInfo = curr.split(":");
				if(userInfo[0].equals(userName)) {
					if(!isDeactivatedUserLine(curr)) {
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


	public boolean boot() {
		File folder = new File(FilePaths.FOLDER_SERVER_USERS);
		if(!folder.exists()) {
			folder.getParentFile().mkdirs();
			folder.mkdir();
		}
		File userInfo = new File(FilePaths.FILE_USERS_PASSWORDS);
		File userInfoMac = new File(FilePaths.FILE_USERS_PASSWORDS_MAC);
		if(!userInfo.exists() || !userInfoMac.exists()) {
			try {
				userInfo.createNewFile();
				userInfoMac.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return macM.validRegistFile(FilePaths.FILE_USERS_PASSWORDS, FilePaths.FILE_USERS_PASSWORDS_MAC);
	}


}
