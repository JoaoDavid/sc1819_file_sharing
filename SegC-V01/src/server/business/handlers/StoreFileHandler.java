package server.business.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import communication.Network;
import facade.exceptions.ApplicationException;
import security.ContentCipher;
import server.business.util.FileManager;
import server.business.util.FilePaths;

public class StoreFileHandler {

	private FileManager fileMan;

	public StoreFileHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public void storeFile(String userName, Socket socket, PrivateKey privKey, PublicKey pubKey, ListFilesHandler listFilesHandler) throws ApplicationException {
		List<String> storedFiles = listFilesHandler.listFiles(userName, privKey, pubKey);
		String fileName = Network.receiveFileAndCipher(userName, socket, false, pubKey, storedFiles);		
		List<String> res = new ArrayList<>();
		if(fileName != null) {
			res.add("STORED");
			addFileName(userName, fileName, privKey, pubKey);
		}else {
			res.add("file already exists in the server");
		}
		Network.listToBuffer(res, socket);
	}

	private void addFileName(String userName, String fileName, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userName 
				+ File.separator + FilePaths.FILE_NAME_FILES_STORED;
		File file = fileMan.acquireFile(filePath);
		try {
			synchronized(file){
				File fileSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
				File fileKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
				byte[] result = ContentCipher.decryptFileAndCheckSig(file, fileSig, fileKey, privKey, pubKey);
				ByteArrayOutputStream byteArr = new ByteArrayOutputStream();

				String newFileName = fileName + "\n";
				byteArr.write(result);
				byteArr.write(newFileName.getBytes());
				ContentCipher.sigAndEcryptFile(file, fileSig, fileKey, privKey, pubKey, byteArr.toByteArray());
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

	}
}
