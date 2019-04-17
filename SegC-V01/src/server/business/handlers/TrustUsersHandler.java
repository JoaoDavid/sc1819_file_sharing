package server.business.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import facade.exceptions.ApplicationException;
import server.business.util.FilePaths;
import server.business.util.FileManager;

public class TrustUsersHandler {

	private FileManager fileMan;


	public TrustUsersHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean trustUser(String userName, String userNameTrusted) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FILE_NAME_TRUSTED;
		File trustedFile = fileMan.acquireFile(filePath);
		try (FileWriter fileWriter = new FileWriter(trustedFile,true);
				BufferedReader br = new BufferedReader(new FileReader(trustedFile));){
			String st; 
			while ((st = br.readLine()) != null) {
				if(st.equals(userNameTrusted)) {
					return false;
				}
			}
			fileWriter.write(userNameTrusted + System.getProperty("line.separator"));
			return true;
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File not found at: " + filePath);
		} catch (IOException e) {
			throw new ApplicationException("IOException in trustUser");
		} finally {
			fileMan.releaseFile(filePath);
		}
	}
}
