package server.business.handlers;

import java.io.File;
import java.util.List;

import facade.exceptions.ApplicationException;
import server.business.util.FileManager;
import server.business.util.FilePaths;
import server.business.util.UserValidation;

public class ListUsersHandler {
	
private FileManager fileMan;
	
	
	public ListUsersHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}
	
	public List<String> listUsers() throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS;
		File user = fileMan.acquireFile(filePath);
		List<String> result = null;
		synchronized(user){
			try {
				result = UserValidation.listRegisteredUsers();
			} catch (ApplicationException e) {
				e.printStackTrace();
			} finally {
				fileMan.releaseFile(filePath);
			}
		}
		return result;
	}
	

}
