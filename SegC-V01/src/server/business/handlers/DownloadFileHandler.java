package server.business.handlers;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import communication.Network;
import server.business.util.FileManager;
import server.business.util.FilePaths;
import server.business.util.UserValidation;

public class DownloadFileHandler {

	private FileManager fileMan;

	public  DownloadFileHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public void clientDownloadFile(String userName, String userOwner, String fileName, Socket socket, PrivateKey privKey, PublicKey pubKey) {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userOwner 
				+ File.separator + FilePaths.FOLDER_FILES + File.separator + fileName;
		if(UserValidation.isTrusted(fileMan, userOwner, userName, privKey, pubKey) && UserValidation.userNameRegisteredAndActive(userOwner)) {
			File file = fileMan.acquireFile(filePath);
			synchronized(file){
				File fileKey = new File(FilePaths.FOLDER_SERVER_USERS + File.separator + userOwner 
				+ File.separator + FilePaths.FOLDER_FILES_KEYS + File.separator + fileName + FilePaths.FILE_NAME_KEY_SUFIX);
				try {
					System.out.println("Sending " + file.getName() + " to " + userName + "  ...");
					Network.sendFileFromServer(file, fileKey, socket, privKey);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
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
