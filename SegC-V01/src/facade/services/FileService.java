package facade.services;

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


	public String[] listFiles(String username) throws ApplicationException {
		return listFilesHandler.listFiles(username);
	}

	public boolean removeFile(String userName, String fileName) {
		return removeFilesHandler.removeFile(userName, fileName);
	}
	
}
