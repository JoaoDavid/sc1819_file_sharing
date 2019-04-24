package server.business.handlers;

import java.io.File;

import server.business.util.FileManager;
import server.business.util.FilePaths;

public class RemoveFilesHandler {
	
	
	private FileManager fileMan;
	
	
	public RemoveFilesHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean removeFile(String userName, String fileName) {
		boolean deleted = fileMan.removeFile(FilePaths.FOLDER_SERVER_USERS + File.separator + userName + 
				File.separator +  FilePaths.FOLDER_FILES + File.separator + fileName);
		if (deleted) {
			File fileKey = new File(FilePaths.FOLDER_SERVER_USERS + File.separator + userName + 
				File.separator +  FilePaths.FOLDER_FILES_KEYS + File.separator + fileName + FilePaths.FILE_NAME_KEY_SUFIX);
			return fileKey.delete();
		}else {
			return false;
		}
	}
}
