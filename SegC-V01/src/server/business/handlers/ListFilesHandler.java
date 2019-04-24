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
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + username 
				+ File.separator + FilePaths.FOLDER_FILES;
		File userFiles = fileMan.acquireFile(filePath);
		synchronized(userFiles){
			String[] result = userFiles.list();
			if(result == null) {
				fileMan.releaseFile(filePath);
				throw new ApplicationException("Path not found");
			}
			return result;
		}
	}


}
