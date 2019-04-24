package server.business.handlers;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
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
		PublicKey pubKey = null;
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FOLDER_FILES + File.separator;
		boolean stored = Network.receiveFileAndCipher(filePath, socket, false, pubKey);
		List<String> res = new ArrayList<String>();
		if(stored) {
			res.add("STORED");
		}else {
			res.add("NOT stored");
		}
		Network.listToBuffer(res, socket);
	}
}
