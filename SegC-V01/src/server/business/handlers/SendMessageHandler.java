package server.business.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import facade.exceptions.ApplicationException;
import server.business.util.FilePaths;
import server.business.util.UserValidation;
import server.ServerConst;
import server.business.util.FileManager;

public class SendMessageHandler {

	private FileManager fileMan;


	public SendMessageHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean storeMsg(String userSender, String userReceiver, String msg) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userReceiver 
				+ File.separator + FilePaths.FILE_NAME_MSG;
		boolean trusted = UserValidation.isTrusted(fileMan, userReceiver, userSender);
		if(trusted) {
			try {
				File msgFile = fileMan.acquireFile(filePath);
				FileWriter fileWriter;
				fileWriter = new FileWriter(msgFile,true);
				fileWriter.write(userSender + ":" + msg + System.getProperty("line.separator"));
				fileWriter.close();
				return true;
			} catch (IOException e) {
				return false;
			} finally {
				fileMan.releaseFile(filePath);
			}
		}else {
			return false;
		}
	}

	

}
