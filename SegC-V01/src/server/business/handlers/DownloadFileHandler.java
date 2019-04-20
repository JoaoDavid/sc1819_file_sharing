package server.business.handlers;

import java.io.File;
import java.net.Socket;

import communication.Network;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class DownloadFileHandler {
	
	private FileManager fileMan;

	public  DownloadFileHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}
	
	public void clientDownloadFile(String userName, String fileName, Socket socket) {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FOLDER_FILES + File.separator + fileName;
		File file = fileMan.acquireFile(filePath);
		System.out.println(filePath);
		//verificar se sao trusted
		Network.sendFile(file, socket);
	}

}
