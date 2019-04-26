package server.business.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import facade.exceptions.ApplicationException;
import security.ContentCipher;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class ListFilesHandler {

	private FileManager fileMan;


	public ListFilesHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public List<String> listFiles(String username, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + username 
				+ File.separator + FilePaths.FILE_NAME_FILES_STORED;
		File storedFile = fileMan.acquireFile(filePath);
		try {
			if(storedFile != null) {//username exists
				synchronized(storedFile){
					File storedFileSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
					File storedFileKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
					byte[] result = ContentCipher.decryptFileAndCheckSig(storedFile, storedFileSig, storedFileKey, privKey, pubKey);
					List<String> list = null;
					if(result.length > 0) {
						String inFile = new String(result);
						list = Arrays.asList(inFile.split("\n"));
					}else {
						list = new ArrayList<>();
					}				
					return list;
				}
			}else {
				//username does not exist
			}
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File not found at: " + filePath);
		} catch (IOException e) {
			throw new ApplicationException("IOException in trustUser");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fileMan.releaseFile(filePath);
		}
		return null;
	}



}
