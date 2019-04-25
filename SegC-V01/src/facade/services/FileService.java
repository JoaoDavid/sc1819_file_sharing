package facade.services;

import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import facade.exceptions.ApplicationException;
import server.business.handlers.DownloadFileHandler;
import server.business.handlers.ListFilesHandler;
import server.business.handlers.RemoveFilesHandler;
import server.business.handlers.StoreFileHandler;

public class FileService {

	//private CollectMessagesHandler
	private DownloadFileHandler downloadFileHandler;
	private ListFilesHandler listFilesHandler;
	private RemoveFilesHandler removeFilesHandler;
	//private SendMessageHandler
	private StoreFileHandler storeFilesHandler;
	//private TrustUsersHandler
	//private UntrustUsersHandler
	public FileService(DownloadFileHandler downloadFileHandler, ListFilesHandler listFilesHandler,
			RemoveFilesHandler removeFilesHandler, StoreFileHandler storeFilesHandler) {
		this.downloadFileHandler = downloadFileHandler;
		this.listFilesHandler = listFilesHandler;
		this.removeFilesHandler = removeFilesHandler;
		this.storeFilesHandler = storeFilesHandler;
	}


	public List<String> listFiles(String username, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		return listFilesHandler.listFiles(username, privKey, pubKey);
	}

	public boolean removeFile(String userName, String fileName, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		return removeFilesHandler.removeFile(userName, fileName, privKey, pubKey);
	}
	
	public void clientDownloadFile(String userName, String userOwner, String fileName, Socket socket, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		downloadFileHandler.clientDownloadFile(userName, userOwner, fileName, socket, privKey, pubKey, this.listFilesHandler);
	}
	
	public void storeFile(String userName, Socket socket, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		storeFilesHandler.storeFile(userName, socket, privKey, pubKey, this.listFilesHandler);
	}
	
}
