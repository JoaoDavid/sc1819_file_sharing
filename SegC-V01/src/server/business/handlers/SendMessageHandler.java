package server.business.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import communication.OpResult;
import facade.exceptions.ApplicationException;
import security.ContentCipher;
import server.business.util.FilePaths;
import server.business.util.UserValidation;
import server.business.util.FileManager;

public class SendMessageHandler {

	private FileManager fileMan;


	public SendMessageHandler(FileManager fileMan) {
		this.fileMan = fileMan;
	}

	public void storeMsg(String userSender, String userReceiver, String msg, PrivateKey privKey, PublicKey pubKey) throws ApplicationException {
		String filePath = FilePaths.FOLDER_SERVER_USERS + File.separator + userReceiver 
				+ File.separator + FilePaths.FILE_NAME_MSG;
		boolean trusted = UserValidation.isTrusted(fileMan, userReceiver, userSender, privKey, pubKey);
		if(trusted) {
			try {
				File msgFile = fileMan.acquireFile(filePath);
				synchronized(msgFile){
					File msgFileSig = new File(FilePaths.FOLDER_SERVER_USERS + File.separator + userReceiver 
							+ File.separator + FilePaths.FILE_NAME_MSG + FilePaths.FILE_NAME_SIG_SUFIX);
					File msgFileKey = new File(FilePaths.FOLDER_SERVER_USERS + File.separator + userReceiver 
							+ File.separator + FilePaths.FILE_NAME_MSG + FilePaths.FILE_NAME_KEY_SUFIX);
					byte[] msgFileByte = ContentCipher.decryptFileAndCheckSig(msgFile, msgFileSig, msgFileKey, privKey, pubKey);
					ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
					byteArr.write(msgFileByte);
					String message = userSender + ":" + msg + System.getProperty("line.separator");
					byteArr.write(message.getBytes());
					ContentCipher.sigAndEcryptFile(msgFile, msgFileSig, msgFileKey, privKey, pubKey, byteArr.toByteArray());
				}
			} catch (IOException e) {
				throw new ApplicationException(OpResult.ERROR);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ApplicationException(OpResult.ERROR);
			} finally {
				fileMan.releaseFile(filePath);
			}
		}else {
			throw new ApplicationException(OpResult.NOT_TRUSTED);
		}
	}



}
