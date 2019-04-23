package users;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import security.ContentCipher;
import security.MacManager;
import server.ServerConst;
import server.business.util.FileManager;

public class UserManagerHandler {

	public static final String MAC_ALGORITHM = "HmacSHA256";
	public static final String KEYSTORE_TYPE = "JCEKS";
	public static final String DIGEST_ALFORITHM = "SHA-256";


	//private KeyStore ks;
	//private FileInputStream fis;
	private SecretKey secKey;
	private ContentCipher cypher;
	private MessageDigest md;
	private MacManager macM;


	public UserManagerHandler(String keyAlias, String keyPassword, String keystoreLocation, String keystorePassword) throws Exception {
		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
		FileInputStream fis = new FileInputStream(keystoreLocation);
		ks.load(fis,keystorePassword.toCharArray());
		secKey = (SecretKey) ks.getKey(keyAlias, keyPassword.toCharArray());
		macM = new MacManager(MAC_ALGORITHM, secKey);
		cypher = new ContentCipher("DES", "DES/CBC/PKCS5Padding");
		md = MessageDigest.getInstance(DIGEST_ALFORITHM);
	}


	/**
	 * Creates a new user
	 * @throws Exception 
	 */
	public void createUser(String userName, String password) throws Exception {
		if(macM.validRegistFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC)) {
			if(!userNameRegistered(userName)) {
				FileOutputStream usersFile = new FileOutputStream(ServerConst.FILE_USERS_PASSWORDS,true);
				String data = userName + ":" + getSaltAndPassword(password) + System.getProperty("line.separator");
				usersFile.write(data.getBytes());
				usersFile.close();
				macM.updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
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
		if(macM.validRegistFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC)) {
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
					macM.updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
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
		if(macM.validRegistFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC)) {
			if(!isDeactivatedUser(userName)) {
				String filePath = ServerConst.FILE_USERS_PASSWORDS;
				File userRegistFile = new File(filePath);
				try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
					List<String> fileContent = new ArrayList<String>();
					String curr; 
					while ((curr = br.readLine()) != null) {
						String[] userInfo = curr.split(":");
						if(userInfo[0].equals(userName)) {
							fileContent.add(userInfo[0] + ":" + getSaltAndPassword(password));
							//System.out.println("EM COMENTARIO FAZER");
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
						System.out.println(currLine);
						fileWriter.write(currLine + System.getProperty("line.separator"));
					}
					fileWriter.close();
					macM.updateMacFile(ServerConst.FILE_USERS_PASSWORDS, ServerConst.FILE_USERS_PASSWORDS_MAC);
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

	public boolean validLogin(String userName, String password) {
		File userRegistFile = new File(ServerConst.FILE_USERS_PASSWORDS);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String st; 
			while ((st = br.readLine()) != null) {
				String[] userInfo = st.split(":");
				if(userInfo[0].equals(userName)) {
					Base64.Decoder dec = Base64.getDecoder();
					byte[] passwordCalculated = getSaltedPassword(password, dec.decode(userInfo[1]));
					return MessageDigest.isEqual(passwordCalculated, dec.decode(userInfo[2]));
				}
			}
		} catch (IOException e) {
		}
		return false;
	}


}
