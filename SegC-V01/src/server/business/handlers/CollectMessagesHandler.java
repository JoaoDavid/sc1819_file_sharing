package server.business.handlers;

import java.io.File;
import java.io.FileWriter;
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

public class CollectMessagesHandler {

	private FileManager fileMan;


	public CollectMessagesHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public List<String> collectMessages(String userName, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator 
				+ userName + File.separator + FilePaths.FILE_NAME_MSG;
		File userMsgs = fileMan.acquireFile(filePath);
		synchronized(userMsgs){
			File userMsgsSig = new File(filePath + FilePaths.FILE_NAME_SIG_SUFIX);
			File userMsgsKey = new File(filePath + FilePaths.FILE_NAME_KEY_SUFIX);
			try (FileWriter fileWriter = new FileWriter(userMsgs)){
				byte[] result = ContentCipher.decryptFileAndCheckSig(userMsgs, userMsgsSig, userMsgsKey, privKey, pubKey);
				//Clear inbox
				fileWriter.write("");
				ContentCipher.sigAndEcryptFile(userMsgs, userMsgsSig, userMsgsKey, privKey, pubKey);
				if(result.length > 0) {
					String inFile = new String(result);
					return Arrays.asList(inFile.split("/n"));
				}else {
					return new ArrayList<String>();
				}				
			} catch (Exception e) {
				throw new ApplicationException("Impossible Colect Messages");
			}finally{
				fileMan.releaseFile(filePath);
			}
		}
	}
}
