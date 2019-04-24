package server.business.handlers;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import communication.Network;
import server.business.util.FileManager;
import server.business.util.FilePaths;
import server.business.util.UserValidation;

public class DownloadFileHandler {

	private FileManager fileMan;

	public  DownloadFileHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public void clientDownloadFile(String userName, String userOwner, String fileName, Socket socket) {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userOwner 
				+ File.separator + FilePaths.FOLDER_FILES + File.separator + fileName;
		if(UserValidation.isTrusted(fileMan, userOwner, userName)) {	
			File file = fileMan.acquireFile(filePath);
			synchronized(file){
				try {
					Network.sendFile(file, socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					fileMan.releaseFile(filePath);
				}
			}			
		}else {
			Network.sendInt(-1, socket);
		}

	}

}
