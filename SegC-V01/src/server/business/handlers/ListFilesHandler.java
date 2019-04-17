package server.business.handlers;

import java.io.File;

import facade.exceptions.ApplicationException;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class ListFilesHandler {
	
private FileManager fileMan;
	
	
	public ListFilesHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}
	
	public String[] listFiles(String username) throws ApplicationException {
		File userFiles = fileMan.acquireFile(FilePaths.FOLDER_SERVER_USERS + File.separator + username 
				+ File.separator + FilePaths.FOLDER_FILES);

		String[] result = userFiles.list();
		if(result == null) {
			throw new ApplicationException("Path not found");
		}
		return result;
	}
	

}
