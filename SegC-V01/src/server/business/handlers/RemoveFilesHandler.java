package server.business.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import facade.exceptions.ApplicationException;
import security.ContentCipher;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class RemoveFilesHandler {


	private FileManager fileMan;


	public RemoveFilesHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public boolean removeFile(String userName, String fileName, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		if(removeFileName(userName, fileName, privKey, pubKey)) {
			boolean deleted = fileMan.removeFile(FilePaths.FOLDER_SERVER_USERS + File.separator + userName + 
					File.separator +  FilePaths.FOLDER_FILES + File.separator + fileName);
			if (deleted) {
				File fileKey = new File(FilePaths.FOLDER_SERVER_USERS + File.separator + userName + 
						File.separator +  FilePaths.FOLDER_FILES_KEYS + File.separator + fileName + FilePaths.FILE_NAME_KEY_SUFIX);
				return fileKey.delete();
			}else {
				return false;
			}
		}else {
			return false;
		}

	}

	private boolean removeFileName(String userName, String fileName, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FILE_NAME_FILES_STORED;
		boolean removed = false;
		File storeFile = fileMan.acquireFile(filePath);
		try {
			synchronized(storeFile){
				File storedFileSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
				File storedFileKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
				byte[] result = ContentCipher.decryptFileAndCheckSig(storeFile, storedFileSig, storedFileKey, privKey, pubKey);
				String inFile = new String(result);
				List<String> list = Arrays.asList(inFile.split("\n"));
				ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
				for(String currLine : list) {
					if(currLine.equals(fileName)) {
						removed = true;
					}else {
						byteArr.write((currLine + "\n").getBytes());
					}
				}
				if(removed) { //fileName was deleted from the file
					ContentCipher.sigAndEcryptFile(storeFile, storedFileSig, storedFileKey, privKey, pubKey, byteArr.toByteArray());
				}//else, does not exist in the file, so there is no need to rewrite the file
				return removed;
			}
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File not found at: " + filePath);
		} catch (IOException e) {
			throw new ApplicationException("IOException while removing file name");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			fileMan.releaseFile(filePath);
		}		
		return removed;
	}
}
