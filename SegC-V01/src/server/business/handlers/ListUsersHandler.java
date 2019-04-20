package server.business.handlers;

import java.io.File;

import facade.exceptions.ApplicationException;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class ListUsersHandler {
	
private FileManager fileMan;
	
	
	public ListUsersHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}
	
	public String[] listUsers() throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS;
		File user = new File(filePath);
		return user.list();
	}
	

}
