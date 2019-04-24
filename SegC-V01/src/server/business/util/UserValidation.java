package server.business.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



/**
 * The purpouse of this class is to validate user's
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
	public static boolean isTrusted(FileManager fileMan, String userName, String userToBeChecked) {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName + File.separator + FilePaths.FILE_NAME_TRUSTED;
		if(userName.equals(userToBeChecked)) {
			return true;
		}
		File trustedFile = fileMan.acquireFile(filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(trustedFile))){
			String st; 
			while ((st = br.readLine()) != null) {
				if(st.equals(userToBeChecked)) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			fileMan.releaseFile(filePath);
		}
		return false;
	}

	


}
