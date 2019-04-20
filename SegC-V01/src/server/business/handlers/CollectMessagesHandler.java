package server.business.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import facade.exceptions.ApplicationException;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class CollectMessagesHandler {

	private FileManager fileMan;


	public CollectMessagesHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}
	
	public List<String> collectMessages(String userName) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator 
				+ userName + File.separator + FilePaths.FILE_NAME_MSG;
		File userMsgs = fileMan.acquireFile(filePath);
		ArrayList<String> result = new ArrayList<String>();
		try ( BufferedReader br = new BufferedReader(new FileReader(userMsgs)) ){
			String st;
			while ((st = br.readLine()) != null) {					
				result.add(st);
			}	
			FileWriter fileWriter = new FileWriter(userMsgs);
			fileWriter.write("");
			fileWriter.close();
			//clears inbox
			return result;
		} catch (IOException e) {
			throw new ApplicationException("Impossible Colect Messages");
		}finally{
			fileMan.releaseFile(filePath);
		}
	}
}
