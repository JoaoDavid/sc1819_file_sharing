package security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import facade.exceptions.ApplicationException;
import server.business.util.FilePaths;

public class FileIntegrity {

	private PrivateKey privKey;
	private PublicKey pubKey;
	private String userName;

	public FileIntegrity(MacManager macM, PrivateKey privKey, PublicKey pubKey, String userName) {
		this.privKey = privKey;
		this.pubKey = pubKey;
		this.userName = userName;
	}


	public boolean checkControlFiles() throws ApplicationException  {
		List<String> controlFiles = new ArrayList<String>();
		controlFiles.add(FilePaths.FILE_NAME_TRUSTED);
		controlFiles.add(FilePaths.FILE_NAME_MSG);
		controlFiles.add(FilePaths.FILE_NAME_FILES_STORED);
		for(String currFile : controlFiles) {
			String path = FilePaths.FOLDER_SERVER_USERS + File.separator + this.userName + File.separator + currFile;
			File file = new File(path);
			File fileSig = new File(path + FilePaths.FILE_NAME_SIG_SUFIX);
			File fileKey = new File(path + FilePaths.FILE_NAME_KEY_SUFIX);
			ContentCipher.checkFileIntegrity(file, fileSig, fileKey, this.privKey, this.pubKey);
		}
		return true;
	}

	public boolean checkUserFiles() throws ApplicationException  {
		List<String> userFiles = listFiles();
		for(String currFile : userFiles) {
			String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
					+ File.separator + FilePaths.FOLDER_FILES + File.separator;
			String filePathKey = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
					+ File.separator + FilePaths.FOLDER_FILES_KEYS + File.separator;
			File file = new File(filePath + currFile);
			File fileKey = new File(filePathKey + currFile + FilePaths.FILE_NAME_KEY_SUFIX);
			if(!file.exists() || !fileKey.exists()) {
				return false;
			}
			if( !ContentCipher.decryptFile(file, fileKey, privKey, pubKey) ) {
				return false;
			}
		}
		return true;
	}

	public List<String> listFiles() throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FILE_NAME_FILES_STORED;
		try {
			File storedFile = new File(filePath);
			File storedFileSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
			File storedFileKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
			byte[] result = ContentCipher.decryptFileAndCheckSig(storedFile, storedFileSig, storedFileKey, privKey, pubKey);
			List<String> list = null;
			if(result.length > 0) {
				String inFile = new String(result);
				list = Arrays.asList(inFile.split("\n"));
			}else {
				list = new ArrayList<String>();
			}				
			return list;
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File not found at: " + filePath);
		} catch (IOException e) {
			throw new ApplicationException("IOException in trustUser");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
