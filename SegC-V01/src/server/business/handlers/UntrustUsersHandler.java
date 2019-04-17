package server.business.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import facade.exceptions.ApplicationException;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class UntrustUsersHandler {

	private FileManager fileMan;

	public UntrustUsersHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean untrustUser(String userName, String userNameUntrusted) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FILE_NAME_TRUSTED;

		File untrustedFile = fileMan.acquireFile(filePath);
		ArrayList<String> fileContent = new ArrayList<String>();
		boolean untrusted = false;
		try (BufferedReader br = new BufferedReader(new FileReader(untrustedFile));){
			String st; 
			while ((st = br.readLine()) != null) {
				if(!st.equals(userNameUntrusted)) {
					fileContent.add(st);
				}
			}
			untrusted = fileContent.remove(userNameUntrusted);
			if(untrusted) { //userNameUntrusted exists in the file
				FileWriter fileWriter = new FileWriter(untrustedFile);
				fileWriter.write("");
				fileWriter.close();
				fileWriter = new FileWriter(untrustedFile,true);
				for(String str : fileContent) {
					fileWriter.write(str + System.getProperty("line.separator"));
				}
				fileWriter.close();
			}//else, does not exist in the file, so there is no need to rewrite the file
			return untrusted;
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File not found at: " + filePath);
		} catch (IOException e) {
			throw new ApplicationException("IOException in untrustUser");
		} finally {
			fileMan.releaseFile(filePath);
		}


	}

}
