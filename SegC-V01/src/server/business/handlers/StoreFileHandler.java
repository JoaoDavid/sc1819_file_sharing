package server.business.handlers;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import communication.Network;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class StoreFileHandler {
	
	private FileManager fileMan;

	public StoreFileHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}
	
	public void storeFile(String userName, Socket socket) {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FOLDER_FILES + File.separator;
		boolean stored = Network.receiveFile(filePath, socket, false);
		List<String> res = new ArrayList<String>();
		if(stored) {
			res.add("STORED");
		}else {
			res.add("NOT STORED");
		}
		try {
			Network.listToBuffer(res, socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
