package server.business.handlers;

import java.io.File;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import communication.Network;
import communication.OpResult;
import facade.exceptions.ApplicationException;
import server.business.util.FileManager;
import server.business.util.FilePaths;
import server.business.util.UserValidation;

public class DownloadFileHandler {

	private FileManager fileMan;

	public  DownloadFileHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public void clientDownloadFile(String userName, String userOwner, String fileName, Socket socket, PrivateKey privKey, PublicKey pubKey, ListFilesHandler listFilesHandler) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userOwner 
				+ File.separator + FilePaths.FOLDER_FILES + File.separator + fileName;
		List<String> filesStored = listFilesHandler.listFiles(userOwner, privKey, pubKey);
		if(UserValidation.isTrusted(fileMan, userOwner, userName, privKey, pubKey) && UserValidation.userNameRegisteredAndActive(userOwner)) {
			if(filesStored.contains(fileName)) {
				File file = fileMan.acquireFile(filePath);
				if(file != null) {
					synchronized(file){
						File fileKey = new File(FilePaths.FOLDER_SERVER_USERS + File.separator + userOwner 
								+ File.separator + FilePaths.FOLDER_FILES_KEYS + File.separator + fileName + FilePaths.FILE_NAME_KEY_SUFIX);
						try {
							System.out.println("Sending " + file.getName() + " to " + userName + "  ...");
							Network.sendFileFromServer(file, fileKey, socket, privKey);
						} catch (ApplicationException e) {
							e.printStackTrace();
						} finally{
							fileMan.releaseFile(filePath);
						}
					}	
				}else {//fileName exists in the txt, but not in the folder
					Network.sendInt(OpResult.ERROR, socket);
					throw new ApplicationException("FILE INTEGRITY COMPROMISED");
				}
			}else {
				Network.sendInt(OpResult.NOT_FOUND, socket);
			}
		}else {
			Network.sendInt(OpResult.NOT_TRUSTED, socket);
		}
	}


}


