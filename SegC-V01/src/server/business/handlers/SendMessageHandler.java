package server.business.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import facade.exceptions.ApplicationException;
import server.business.util.FilePaths;
import server.business.util.FileManager;

public class SendMessageHandler {

	private FileManager fileMan;


	public SendMessageHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean storeMsg(String userSender, String userReceiver, String msg) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userReceiver 
				+ File.separator + FilePaths.FILE_NAME_MSG;
		try {
			File msgFile = fileMan.acquireFile(filePath);
			FileWriter fileWriter;
			fileWriter = new FileWriter(msgFile,true);
			fileWriter.write(userSender + ":" + msg + System.getProperty("line.separator"));
			fileWriter.close();
			return true;
		} catch (IOException e) {
			throw new ApplicationException("Error writing " + msg);
		} finally {
			fileMan.releaseFile(filePath);
		}
	}

}