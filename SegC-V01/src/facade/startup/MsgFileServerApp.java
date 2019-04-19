package facade.startup;

import server.business.handlers.CollectMessagesHandler;
import server.business.handlers.DownloadFileHandler;
import server.business.handlers.ListFilesHandler;
import server.business.handlers.RemoveFilesHandler;
import server.business.handlers.SendMessageHandler;
import server.business.handlers.StoreFileHandler;
import server.business.handlers.TrustUsersHandler;
import server.business.handlers.UntrustUsersHandler;
import server.business.util.FileManager;

public class MsgFileServerApp {


	private FileManager fileManager;

	/**
	 * Performs the start up use case
	 */
	public MsgFileServerApp() {
		this.fileManager = FileManager.getInstance();
	}

	public CollectMessagesHandler getCollectMessagesHandler() {
		return new CollectMessagesHandler(fileManager);
	}
	
	public DownloadFileHandler getDownloadFileHandler() {
		return new DownloadFileHandler(fileManager);
	}
	
	public ListFilesHandler getListFilesHandler() {
		return new ListFilesHandler(fileManager);
	}
	
	public RemoveFilesHandler getRemoveFilesHandler() {
		return new RemoveFilesHandler(fileManager);
	}
	
	public SendMessageHandler getSendMessageHandler() {
		return new SendMessageHandler(fileManager);
	}
	
	public StoreFileHandler getStoreFileHandler() {
		return new StoreFileHandler(fileManager);
	}
	
	public TrustUsersHandler getTrustUsersHandler() {
		return new TrustUsersHandler(fileManager);
	}
	
	public UntrustUsersHandler getUntrustUsersHandler() {
		return new UntrustUsersHandler(fileManager);
	}


}


