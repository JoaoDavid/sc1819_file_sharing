package server.business.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import facade.exceptions.ApplicationException;
import server.business.util.FilePaths;
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
		boolean trusted = isTrusted(userReceiver, userSender);
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

	private boolean isTrusted(String userName, String userToBeChecked) {
		String filePath = ServerConst.FOLDER_SERVER_USERS + File.separator + userName + File.separator + ServerConst.FILE_NAME_TRUSTED;
		File trustedFile = fileMan.acquireFile(filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(trustedFile))){
			String st; 
			while ((st = br.readLine()) != null) {
				if(st.equals(userToBeChecked)) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			fileMan.releaseFile(filePath);
		}
		return false;
	}

}
