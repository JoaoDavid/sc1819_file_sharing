package users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import server.ServerConst;

public class Handler {

	public static final String MAC_ALGORITHM = "HmacSHA1";
	public static final String KEYSTORE_TYPE = "JCEKS";



	private Mac mac;
	private KeyStore ks;
	private FileInputStream fis;
	private SecretKey secKey;


	public Handler(String keyAlias, String keyPassword, String keystoreLocation, String keystorePassword) throws Exception {
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
			FileOutputStream usersFile = new FileOutputStream(ServerConst.FILE_USERS_PASSWORDS,true);
			FileOutputStream usersFileMAC = new FileOutputStream(ServerConst.FILE_USERS_PASSWORDS_MAC);
			String data = userName + ":" + saltPassoword(password) + System.getProperty("line.separator");	
			usersFile.write(data.getBytes());
			byte[] newMACBytes = mac.doFinal(Files.readAllBytes(Paths.get(ServerConst.FILE_USERS_PASSWORDS)));
			usersFileMAC.write(newMACBytes);
			usersFile.close();
			usersFileMAC.close();
		}		
	}

	private String saltPassoword(String password) {
		return password;
	}


	/**
	 * Deactivates an user
	 */
	public void removeUser(String userName) {

	}

	/**
	 * Updates user's password
	 */
	public void updateUser(String userName, String password) {

	}

	private boolean validRegistFile()  {
		try {
			Mac otherMAC = Mac.getInstance(MAC_ALGORITHM);
			otherMAC.init(secKey);			
			byte[] otherMACfinal = otherMAC.doFinal(Files.readAllBytes(Paths.get(ServerConst.FILE_USERS_PASSWORDS))); 
			byte[] savedMAC = Files.readAllBytes(Paths.get(ServerConst.FILE_USERS_PASSWORDS_MAC));
			/*for(int i = 0; i < otherMACfinal.length; i++) {
				System.out.println(otherMACfinal[i] + " : " + savedMAC[i]);
			}*/
			return Arrays.equals(otherMACfinal, savedMAC);
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
		}
		return false;

	}


}
