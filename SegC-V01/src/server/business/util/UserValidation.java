package server.business.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import communication.OpResult;
import facade.exceptions.ApplicationException;
import security.ContentCipher;



/**
 * The purpose of this class is to validate user's
 * for example, check if they are registered, if they are trusted by other user
 *
 */
public class UserValidation { 


	/**
	 * @param fileMan
	 * @param userName
	 * @param userToBeChecked
	 * @return true:userToBeChecked is trusted by userName
	 */
	public static boolean isTrusted(FileManager fileMan, String userName, String userToBeChecked, PrivateKey privKey, PublicKey pubKey) {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_TRUSTED;
		if(userName.equals(userToBeChecked)) {
			return true;
		}
		File trustedFile = fileMan.acquireFile(filePath);
		try {
			synchronized(trustedFile){
				File trustedFileSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
				File trustedFileKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
				byte[] result = ContentCipher.decryptFileAndCheckSig(trustedFile, trustedFileSig, trustedFileKey, privKey, pubKey);
				String inFile = new String(result);
				List<String> list = Arrays.asList(inFile.split("\n"));
				for(String currLine : list) {
					if(currLine.equals(userToBeChecked)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			fileMan.releaseFile(filePath);
		}
		return false;
	}
	
	public static List<String> listRegisteredUsers() throws IOException {
		String filePath = FilePaths.FILE_USERS_PASSWORDS;
		File userRegistFile = new File(filePath);
		List<String> result = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String curr; 
			while ((curr = br.readLine()) != null) {
				if(!isDeactivatedUserLine(curr)) {
					result.add(curr.substring( 0, curr.indexOf(":")));
				}
			}
		} catch (IOException e) {
			throw new IOException(e);
		}
		return result;
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

	public static boolean isDeactivatedUserLine(String line) {
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
	
	public static boolean userNameRegisteredAndActive(String userName) {
		String filePath = FilePaths.FILE_USERS_PASSWORDS;
		File userRegistFile = new File(filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(userRegistFile))){
			String curr; 
			while ((curr = br.readLine()) != null) {
				String[] userInfo = curr.split(":");
				if(userInfo[0].equals(userName)) {
					return !isDeactivatedUserLine(curr);
				}
			}
		} catch (IOException e) {
		}
		return false;
	}




}
